package licencecontrol.dao;

public interface DAO {
	
	public boolean validateLicence(String licence) throws DAOException;
	public String getChecksum(String licence) throws DAOException;
	public int getNbMaxUsers(String licence) throws DAOException;
	
}
