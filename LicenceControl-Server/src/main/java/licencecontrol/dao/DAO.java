package licencecontrol.dao;

import java.sql.Timestamp;

public interface DAO {
	
	public boolean validateLicence(String licence) throws DAOException;
	public String getChecksum(String licence) throws DAOException;
	public int getNbMaxUsers(String licence) throws DAOException;
	/**
	 * Insère une clé temporaire dans la table Session
	 * @param licence La licence sur laquelle porte la clé temporaire
	 * @param key La clé temporaire
	 * @param timestamp La date d'expiration de la clé temporaire
	 * @return true si insertion effectuée, sinon false
	 */
	public boolean insertTemporaryKey(String licence, String key, Timestamp timestamp) throws DAOException;
	
}
