package licencecontrol.dao;

import java.sql.Timestamp;

import licencecontrol.server.LicenceControlTest;


public class Test {

	public static void main(String[] args) {
		DAO dao = new DAOStub();
		String licence = "licence-proto";
		try {
			System.out.println(dao.validateLicence(licence)); //true			
			
		} catch (DAOException e) {
			System.err.println("Echec de la requete check licence ok");
		}
		try {
			System.out.println(dao.getChecksum(licence)); //40d869c6cac79f45dc866393ccd101fe43601d10cd7da4699b832f08b8ee3423
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
			System.out.println(dao.getNbMaxUsers(licence)); //2		
			
		} catch (DAOException e) {
			System.err.println("Echec de la requete getNbMaxUsers");
		}
		try {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis()+(1000*60*60));
			boolean ok = dao.insertTemporaryKey(licence, "key_to_delete", timestamp);
			System.out.println(ok); //true		
			
		} catch (DAOException e) {
			System.err.println("Echec de la requete InsertTemporaryKey " + e.getMessage());
		}
		try {
			int nb = dao.getNbActiveSessions(licence);
			System.out.println(nb); //1 	
			
		} catch (DAOException e) {
			System.err.println("Echec de la requete getNbSessionsActives " + e.getMessage());
		}
		try {
			boolean ok = dao.deleteSession("key_to_delete", licence);
			System.out.println(ok); //true		
			
		} catch (DAOException e) {
			System.err.println("Echec de la requete deleteSession " + e.getMessage());
		}
		try {
			// on ajoute une clé active
			Timestamp timestamp = new Timestamp(System.currentTimeMillis()+(1000*60*60));
			dao.insertTemporaryKey(licence, "key_test_session_state", timestamp);
			
			SessionState state = dao.sessionExists("key_test_session_state", licence);
			System.out.println(state); //Active
			//on supprime
			dao.deleteSession("key_test_session_state", licence);
		} catch (DAOException e) {
			System.err.println("Echec de la requete SessionState ACTIVE" + e.getMessage());
		}
		try {
			// on ajoute une clé expirée
			Timestamp timestamp = new Timestamp(System.currentTimeMillis()-(1000*60*60));
			dao.insertTemporaryKey(licence, "key_test_session_state", timestamp);
			
			SessionState state = dao.sessionExists("key_test_session_state", licence);
			System.out.println(state); //Expired
			//on supprime
			dao.deleteSession("key_test_session_state", licence);
		} catch (DAOException e) {
			System.err.println("Echec de la requete SessionState EXPIRED" + e.getMessage());
		}
		try {
			SessionState state = dao.sessionExists("foo_key", licence);
			System.out.println(state); //Null
		} catch (DAOException e) {
			System.err.println("Echec de la requete SessionState NULL" + e.getMessage());
		}
		
		LicenceControlTest lc = new LicenceControlTest();
		System.out.println(lc.check("licence-proto;40d869c6cac79f45dc866393ccd101fe43601d10cd7da4699b832f08b8ee3423;token"));
		System.out.println(lc.revalidate("licence-proto;40d869c6cac79f45dc866393ccd101fe43601d10cd7da4699b832f08b8ee3423;token;gcl9asn71v4u6n0mejf2l3g3aa"));
	}
}
