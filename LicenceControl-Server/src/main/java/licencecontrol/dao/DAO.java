package licencecontrol.dao;

import java.sql.Timestamp;

public interface DAO {
	
	public boolean validateLicence(String licence) throws DAOException;
	public String getChecksum(String licence) throws DAOException;
	public int getNbMaxUsers(String licence) throws DAOException;
	public boolean insertTemporaryKey(String licence, String tempKey, Timestamp timestamp) throws DAOException;
	public int getNbActiveSessions(String licence) throws DAOException;
	
}
