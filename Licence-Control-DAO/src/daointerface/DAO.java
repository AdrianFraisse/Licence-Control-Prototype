package daointerface;

import java.sql.SQLException;

public interface DAO {
	boolean checkLicence(String licence) throws SQLException;
	
	String getChecksum(String licence) throws SQLException;
	
	int getNbMaxUsers(String licence) throws SQLException;
}
