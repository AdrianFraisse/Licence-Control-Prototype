package licencecontrol.dao;

import java.sql.Timestamp;

public interface DAO {
	
	public boolean validateLicence(String licence) throws DAOException;
	public String getChecksum(String licence) throws DAOException;
	public int getNbMaxUsers(String licence) throws DAOException;
	/**
	 * Ins�re une cl� temporaire dans la table Session
	 * @param licence La licence sur laquelle porte la cl� temporaire
	 * @param key La cl� temporaire
	 * @param timestamp La date d'expiration de la cl� temporaire
	 * @return true si insertion effectu�e, sinon false
	 */
	public boolean insertTemporaryKey(String licence, String key, Timestamp timestamp) throws DAOException;
	
}
