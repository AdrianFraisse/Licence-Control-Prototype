package licencecontrol.dao;

public class Test {

	public static void main(String[] args) {
		DAO dao = new DAOImpl();
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
	}
}
