package fr.emn.prototype.licencecontrol.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fr.emn.prototype.licencecontrol.data.DAO;
import fr.emn.prototype.licencecontrol.data.DAOStub;

@Path("/licence")
public class LicenceControl {

	@GET
	@Path("check")
	@Produces(MediaType.TEXT_PLAIN)
	public String check(@QueryParam("query") String query) {
		System.out.println("check query=" + query);
		return "token, query = " + query;
	}
	
	private boolean checkData(String licence, String checksum) {
		DAO dao = new DAOStub();
		return dao.checkLicence(licence) && dao.getChecksum(licence).equals(checksum);
	}
	
}
