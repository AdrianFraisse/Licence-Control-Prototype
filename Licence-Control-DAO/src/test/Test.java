package test;

import java.sql.SQLException;

import daoimpl.DAOLicence;

public class Test {

	public static void main(String[] args) {
		DAOLicence dao = DAOLicence.instance();
		try {
			System.out.println(dao.checkLicence("LICENCE")); //true			
			
		} catch (SQLException e) {
			System.err.println("Echec de la requete check licence ok");
		}
		try {
			System.out.println(dao.getChecksum("LICENCE")); //GYDFDGDGYUTSYUGDSUYGDYUSGYEGTSFA
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
