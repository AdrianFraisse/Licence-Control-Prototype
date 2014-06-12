package licencecontrol.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import licencecontrol.dao.DAO;
import licencecontrol.dao.DAOException;
import licencecontrol.dao.DAOLicences;
import licencecontrol.dao.SessionState;
import licencecontrol.util.Crypto;
import licencecontrol.util.Utils;


@Path("/licence")
public class LicenceControl {
	
	private static final String SERVER_ERROR = "0";
	private static final String DAO_ERROR = "1";
	private static final String LICENCE_CONTROL_FAILURE = "2";
	private static final String USER_LIMIT_REACHED = "3";
	private static final String UNREGISTERED = "4";
	private static final String INVALID_QUERY = "Invalid Query";
	
	/**
	 * R�ceptionne une requ�te validation de licence 
	 * (Ouverture d'une session ou actualisation)
	 * @param query la requ�te pass�e au format texte
	 * @return la r�ponse au format texte
	 */
	@GET
	@Path("register")
	@Produces(MediaType.TEXT_PLAIN)
	public String register(@QueryParam("query") String query) {
		// Rejet d'une requ�te pr�sentant un format erronn�
		try {
			// d�cryptage de la requ�te
			byte[] bytes = Crypto.decryptData(Utils.stringToByteArray(query), Crypto.getPrivateKey());
			String uncipherQuery = new String(bytes);
            String[] data = uncipherQuery.split(";");
            // Premi�re v�rification
			if (data.length == 3) {
				if (checkData(data)) {
					// Premier contr�le : reponse == token et une cl� temporaire
					return registerClient(data);
				} else return LICENCE_CONTROL_FAILURE;
			} else if (data.length == 4) {
				// Requ�te d'actualisation
				if (checkData(data)) {
					return actualizeClient(data);
				} else return LICENCE_CONTROL_FAILURE;
			} else {
				// requ�te invalide
				return INVALID_QUERY;
			}
		} catch (DAOException e) {
			return DAO_ERROR;
		} catch (Exception e) {
			e.printStackTrace();
            return SERVER_ERROR;
        }
	}
	
	/**
	 * R�ceptionne une requ�te de lib�ration de session
	 * supprime la session en base
	 * @param query
	 * @return message de confirmation
	 */
	@GET
	@Path("unregister")
	@Produces(MediaType.TEXT_PLAIN)
	public String unregister(@QueryParam("query") String query) {
		String[] data = query.split(";");
		try {
			// On attend dans la requete la licence, le checksum, la cl� temp
			if (data.length == 3) {
				if (checkData(data)) {
					return unregisterClient(data);
				} else return LICENCE_CONTROL_FAILURE;
			} else {
				return INVALID_QUERY;
			}
		} catch (DAOException e) {
			return DAO_ERROR;
		}
	}
	
	/**
	 * Supprime la session d'un client
	 * @param data
	 * @return
	 * @throws DAOException
	 */
	private String unregisterClient(String[] data) throws DAOException {
		final String licence = data[0];
		final String oldKey = data[1];
		DAO dao = new DAOLicences();
		if (dao.deleteSession(oldKey, licence)) {
			return UNREGISTERED;
		} else {
			return INVALID_QUERY;
		}
	}
	
	/**
	 * Proc�de � l'enregistrement en base d'un triplet couple/tempKey/dateExp
	 * Appel� lors de la premi�re v�rification de licence d'une session
	 * @param data donn�es de la requ�te
	 * @return la cl� temporaire g�n�r�e
	 * @throws DAOException 
	 */
	private String registerClient(String[] data) throws DAOException {
		final String licence = data[0];
		final String token = data[2];
		final String temporaryKey = Utils.generateTemporaryKey();
		DAO dao = new DAOLicences();
		final int maxUsers = dao.getNbMaxUsers(licence);
		if (maxUsers > dao.getNbActiveSessions(licence)) {
			dao.insertTemporaryKey(licence, temporaryKey, Utils.generateExpirationDate());
			return token + ";" + temporaryKey;
		} else {
			return USER_LIMIT_REACHED;
		}
	}

	/**
	 * Actualise la session d'un client.
	 * Si la session du client existe et n'a pas expir�, g�n�re une nouvelle cl� temporaire et 
	 * supprime l'ancienne.
	 * Si la session existe mais a expir�, supprime cette session puis enregistre le client avec une nouvelle.
	 * Si la session n'existe pas, la requ�te est rejet�e, il s'agit d'une usurpation.
	 * @param data
	 * @return nouvelle cl� temporaire, ou message d'erreur
	 * @throws DAOException 
	 */
	private String actualizeClient(String[] data) throws DAOException {
		final String licence = data[0];
		final String oldKey = data[3];
		final String token = data[2];
		DAO dao = new DAOLicences();
		final SessionState sessionState = dao.sessionExists(licence, oldKey);
		switch (sessionState) {
		case ACTIVE : {
			// La session est active
			dao.deleteSession(licence, oldKey);
			final String temporaryKey = Utils.generateTemporaryKey();
			dao.insertTemporaryKey(licence, temporaryKey, Utils.generateExpirationDate());
			return token + ";" + temporaryKey;
		} 
		// La session a expir�
		case EXPIRED : return registerClient(data);
		// La session n'existe pas
		case NULL : return LICENCE_CONTROL_FAILURE;
		}
		return LICENCE_CONTROL_FAILURE;
	}

	/**
	 * Contr�le l'int�grit� du checkSum et la validit� de la licence.
	 * D�l�gation � la couche DAO des traitements.
	 * @param data Tableau de chaines contenant la licence et le checksum re�us par le serveur
	 * @return true si la licence et le checkSum sont connus en base
	 * @throws DAOException 
	 */
	private boolean checkData(String[] data) throws DAOException {
		final String licence = data[0];
		final String checkSum = data[1];
		DAO dao = new DAOLicences();
		return dao.validateLicence(licence) && dao.getChecksum(licence).equals(checkSum);
	}
}
