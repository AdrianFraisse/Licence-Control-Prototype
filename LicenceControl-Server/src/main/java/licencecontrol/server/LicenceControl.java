package licencecontrol.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import licencecontrol.dao.DAO;
import licencecontrol.dao.DAOException;
import licencecontrol.dao.DAOLicences;


@Path("/licence")
public class LicenceControl {

	@GET
	@Path("validate")
	@Produces(MediaType.TEXT_PLAIN)
	public String validate(@QueryParam("query") String query) {
		String[] data = query.split(";");
		
		if (validateData(data[1], data[0])) {
			// returns the token
			return data[2];
		}
		
		return "0";
	}
	
	private boolean validateData(String licence, String checksum) {
		DAO dao = new DAOLicences();
		try {
			return dao.validateLicence(licence) && dao.getChecksum(licence).equals(checksum);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
