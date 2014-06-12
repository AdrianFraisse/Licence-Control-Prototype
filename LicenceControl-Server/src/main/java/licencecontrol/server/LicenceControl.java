package licencecontrol.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import licencecontrol.dao.DAO;
import licencecontrol.dao.DAOException;
import licencecontrol.dao.DAOLicences;
import licencecontrol.util.Utils;


@Path("/licence")
public class LicenceControl {
	
	private static final String DAO_ERROR = "1";
	private static final String INVALID_QUERY = "Invalid Query";
	private static final String LICENCE_CONTROL_FAILURE = "2";
	/**
	 * Réceptionne une requète de première validation de licence 
	 * (Ouverture d'une session)
	 * @param query la requète passée au format texte
	 * @return la réponse au format texte
	 */
	@GET
	@Path("check")
	@Produces(MediaType.TEXT_PLAIN)
	public String check(@QueryParam("query") String query) {
		String[] data = query.split(";");
		// Rejet d'une requète présentant un format erronné
		try {
			if (data.length == 3) {
				final String response = data[2] + ";";
				if (checkData(data)) {
					// Premier contrôle : reponse == token et une clé temporaire
					return response.concat(registerClient(data));
				} else return LICENCE_CONTROL_FAILURE;
			} else {
				// requète invalide
				return INVALID_QUERY;
			}
		} catch (DAOException e) {
			e.printStackTrace();
			return DAO_ERROR + ";" + e.getMessage();
		}
	}
	
	/**
	 * Réceptionne une requète de re-contrôle de licence.
	 * Actualise une session.
	 * @param query
	 * @return
	 */
	@GET
	@Path("revalidate")
	@Produces(MediaType.TEXT_PLAIN)
	public String revalidate(@QueryParam("query") String query) {
		String[] data = query.split(";");
		try {
			// On attend dans la requete la licence, le checksum, le token, la clé temp
			if (data.length == 4) {
				final String response = data[2];
				if (checkData(data)) {
					return response.concat(actualizeClient(data));
				} else return response.concat(LICENCE_CONTROL_FAILURE);
			} else {
				return INVALID_QUERY;
			}
		} catch (DAOException e) {
			e.printStackTrace();
			return DAO_ERROR + ";" + e.getMessage();
		}
	}
	
	/**
	 * Procède à l'enregistrement en base d'un triplet couple/tempKey/dateExp
	 * Appelé lors de la première vérification de licence d'une session
	 * @param data données de la requète
	 * @return la clé temporaire générée
	 * @throws DAOException 
	 */
	private String registerClient(String[] data) throws DAOException {
		final String licence = data[0];
		final String temporaryKey = Utils.generateTemporaryKey();
		DAO dao = new DAOLicences();
		final int maxUsers = dao.getNbMaxUsers(licence);
		if (maxUsers > dao.getNbActiveSessions(licence)) {
			dao.insertTemporaryKey(licence, temporaryKey, Utils.generateExpirationDate());
			return temporaryKey;
		} else {
			throw new DAOException("Max user limit reached for licence : " + licence + ". (User limit : " + maxUsers + ")");
		}
	}

	/**
	 * Actualise la session d'un client.
	 * Si la session du client existe et n'a pas expiré, génère une nouvelle clé temporaire et 
	 * supprime l'ancienne.
	 * Si la session existe mais a expiré, supprime cette session puis enregistre le client avec une nouvelle.
	 * Si la session n'existe pas, la requète est rejetée, il s'agit d'une usurpation.
	 * @param data
	 * @return
	 */
	private String actualizeClient(String[] data) {
		final String licence = data[0];
		final String  oldKey = data[3];
		DAO dao = new DAOLicences();
		try {
			final int sessionState = dao.sessionExists(licence, oldKey);
			switch (sessionState) {
			case 0 : {
				// La session est active
				dao.deleteSession(licence, oldKey);
				final String temporaryKey = Utils.generateTemporaryKey();
				dao.insertTemporaryKey(licence, temporaryKey, Utils.generateExpirationDate());
				return temporaryKey;
			} 
			// La session a expiré
			case 1 : return registerClient(data);
			// La session n'existe pas
			case 2 : return LICENCE_CONTROL_FAILURE;
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return LICENCE_CONTROL_FAILURE;
	}

	/**
	 * Contrôle l'intégrité du checkSum et la validité de la licence.
	 * Délégation à la couche DAO des traitements.
	 * @param data Tableau de chaines contenant la licence et le checksum reçus par le serveur
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