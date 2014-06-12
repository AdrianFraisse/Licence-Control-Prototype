package licencecontrol.dao;

import java.util.Date;

public class DAOStub implements DAO {

	@Override
	public String getChecksum(String licence) throws DAOException {
		return "40d869c6cac79f45dc866393ccd101fe43601d10cd7da4699b832f08b8ee3423";
	}

	@Override
	public boolean validateLicence(String licence) throws DAOException {
		return true;
	}

	@Override
	public int getNbMaxUsers(String licence) throws DAOException {
		return 5;
	}

	@Override
	public boolean insertTemporaryKey(String licence, String tempKey, Date date) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getNbActiveSessions(String licence) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int sessionExists(String licence, String tempKey) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeSession(String licence, String oldKey) {
		// TODO Auto-generated method stub
		
	}

}
