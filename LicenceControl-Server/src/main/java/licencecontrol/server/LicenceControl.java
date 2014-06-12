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
	 * R�ceptionne une requ�te de premi�re validation de licence 
	 * (Ouverture d'une session)
	 * @param query la requ�te pass�e au format texte
	 * @return la r�ponse au format texte
	 */
	@GET
	@Path("check")
	@Produces(MediaType.TEXT_PLAIN)
	public String check(@QueryParam("query") String query) {
		String[] data = query.split(";");
		// Rejet d'une requ�te pr�sentant un format erronn�
		try {
			if (data.length == 3) {
				final String response = data[2] + ";";
				if (checkData(data)) {
					// Premier contr�le : reponse == token et une cl� temporaire
					return response.concat(registerClient(data));
				} else return LICENCE_CONTROL_FAILURE;
			} else {
				// requ�te invalide
				return INVALID_QUERY;
			}
		} catch (DAOException e) {
			e.printStackTrace();
			return DAO_ERROR + ";" + e.getMessage();
		}
	}
	
	/**
	 * R�ceptionne une requ�te de re-contr�le de licence.
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
			// On attend dans la requete la licence, le checksum, le token, la cl� temp
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
	 * Proc�de � l'enregistrement en base d'un triplet couple/tempKey/dateExp
	 * Appel� lors de la premi�re v�rification de licence d'une session
	 * @param data donn�es de la requ�te
	 * @return la cl� temporaire g�n�r�e
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
	 * V�rifie en base que 
	 * @param data
	 * @return
	 */
	private String actualizeClient(String[] data) {
		final String licence = data[0];
		final String  oldKey = data[3];
		final String temporaryKey = Utils.generateTemporaryKey();
		DAO dao = new DAOLicences();
		if (dao.sessionExists(licence, oldKey) == 0) {
			dao.removeSession(licence, oldKey);
			dao.insertTemporaryKey(licence, temporaryKey, Utils.generateExpirationDate());
			return temporaryKey;
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