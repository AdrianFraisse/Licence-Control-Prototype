package licencecontrol.dao;

import java.util.Date;

public interface DAO {
	
	public boolean validateLicence(String licence) throws DAOException;
	public String getChecksum(String licence) throws DAOException;
	public int getNbMaxUsers(String licence) throws DAOException;
	public boolean insertTemporaryKey(String licence, String tempKey, Date date);
	public int getNbActiveSessions(String licence);
	public int sessionExists(String licence, String tempKey);
	public void removeSession(String licence, String oldKey);
	
}
