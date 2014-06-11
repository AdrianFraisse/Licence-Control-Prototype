package licencecontrol.dao;

import java.sql.Date;
import java.sql.Timestamp;


public class Test {

	public static void main(String[] args) {
		DAO dao = new DAOLicences();
		try {
			System.out.println(dao.validateLicence("licence-proto")); //true			
			
		} catch (DAOException e) {
			System.err.println("Echec de la requete check licence ok");
		}
		try {
			System.out.println(dao.getChecksum("licence-proto")); //40d869c6cac79f45dc866393ccd101fe43601d10cd7da4699b832f08b8ee3423
		} catch (DAOException e) {
			System.err.println("Echec de la requete checksum existant");
		}
		try {
			System.out.println(dao.getChecksum("FOO"));
		} catch (DAOException e) {
			System.err.println("Echec de la requete checksum inexistant"); //
		}
		try {
			System.out.println(dao.validateLicence("FOO")); //false		
			
		} catch (DAOException e) {
			System.err.println("Echec de la requete check licence ko");
		}
		try {
			System.out.println(dao.getNbMaxUsers("licence-proto")); //2		
			
		} catch (DAOException e) {
			System.err.println("Echec de la requete getNbMaxUsers");
		}
		try {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis()+(1000*60*60));
			Date date = new Date(1402508820);
			boolean ok = dao.insertTemporaryKey("licence-proto", "yahhci6yyahhci6yyahhci6yyahhci6y", timestamp);
			System.out.println(ok); //true		
			
		} catch (DAOException e) {
			System.err.println("Echec de la requete InsertTemporaryKey " + e.getMessage());
		}
	}
}
