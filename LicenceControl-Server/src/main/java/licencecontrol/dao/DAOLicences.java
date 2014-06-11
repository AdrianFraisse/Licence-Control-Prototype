package licencecontrol.dao;

import java.sql.*;
import java.util.Date;

import licencecontrol.db.ConnectionMySql;

public class DAOLicences implements DAO {
	
	private static final String QUERY_VALIDATE_LIC = "SELECT COUNT(licence) as nb FROM `licences` WHERE `licence`= ?";
	private static final String QUERY_CHECKSUM = "SELECT `build_checksum` FROM builds b, licences l WHERE l.id_build = b.id_build AND l.licence = ?";
	private static final String QUERY_NB_MAX_USERS = "SELECT `nb_users_max` FROM `licences` WHERE `licence` = ?";

	//private static DAOLicence instance;
	private Connection connection;

	public DAOLicences() {
		connection = ConnectionMySql.getConnection();
	}

	/*
	public static DAOLicence instance() {
		if (instance == null) {
			instance = new DAOLicence();
		}
		return instance;
	}
	*/
	
	@Override
	public boolean validateLicence(String licence) throws DAOException {
		//String sql = "SELECT COUNT(licence) as nb FROM `licences` WHERE `licence`= ?";
		try {
			PreparedStatement statement = connection.prepareStatement(QUERY_VALIDATE_LIC);
			statement.setString(1, licence);
			ResultSet rs = statement.executeQuery();
			rs.first();
			int result = rs.getInt("nb");
			rs.close();
			return result > 0;
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}

	@Override
	public String getChecksum(String licence) throws DAOException {
		//String sql = "SELECT `build_checksum` FROM builds b, licences l WHERE l.id_build = b.id_build AND l.licence = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(QUERY_CHECKSUM);
			statement.setString(1, licence);
			ResultSet rs = statement.executeQuery();
			String checksum = null;
			if(rs.first()) {
				checksum = rs.getString("build_checksum");
			}
			rs.close();
			return checksum;
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		connection.close();
		super.finalize();
	}

	@Override
	public int getNbMaxUsers(String licence) throws DAOException {
		//String sql = "SELECT `nb_users_max` FROM `licences` WHERE `licence` = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(QUERY_NB_MAX_USERS);
			statement.setString(1, licence);
			ResultSet rs = statement.executeQuery();
			int nbUsersMax = 0;
			if(rs.first()) {
				nbUsersMax = rs.getInt("nb_users_max");
			}
			rs.close();
			return nbUsersMax;
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
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
}
