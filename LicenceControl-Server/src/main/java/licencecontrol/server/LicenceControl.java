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
	
	private static final String SERVER_ERROR = "0";
	private static final String DAO_ERROR = "1";
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
		final String response = data[2] + ";";
		try {
			if (data.length == 3 && checkData(data)) {
				// Premier contrôle : reponse == token et une clé temporaire
				return response.concat(registerClient(data));
			} else {
				// TODO reponse refus
				return SERVER_ERROR;
			}
		} catch (DAOException e) {
			e.printStackTrace();
			return DAO_ERROR;
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
		if (data.length == 4) {
			
			return "0";
		} else {
			return "0";
		}
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
