package test;

import java.sql.SQLException;

import daoimpl.DAOLicence;

public class Test {

	public static void main(String[] args) {
		DAOLicence dao = DAOLicence.instance();
		try {
			System.out.println(dao.checkLicence("licence-proto")); //true			
			
		} catch (SQLException e) {
			System.err.println("Echec de la requete check licence ok");
		}
		try {
			System.out.println(dao.getChecksum("licence-proto")); //40d869c6cac79f45dc866393ccd101fe43601d10cd7da4699b832f08b8ee3423
		} catch (SQLException e) {
			System.err.println("Echec de la requete checksum existant");
		}
		try {
			System.out.println(dao.getChecksum("FOO"));
		} catch (SQLException e) {
			System.err.println("Echec de la requete checksum inexistant"); //
		}
		try {
			System.out.println(dao.checkLicence("FOO")); //false		
			
		} catch (SQLException e) {
			System.err.println("Echec de la requete check licence ko");
		}
	}
}
