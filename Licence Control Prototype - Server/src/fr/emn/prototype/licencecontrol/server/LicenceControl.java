package fr.emn.prototype.licencecontrol.server;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import daoimpl.DAOLicence;
import daointerface.DAO;


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
		DAO dao = DAOLicence.instance();
		try {
			return dao.checkLicence(licence) && dao.getChecksum(licence).equals(checksum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
