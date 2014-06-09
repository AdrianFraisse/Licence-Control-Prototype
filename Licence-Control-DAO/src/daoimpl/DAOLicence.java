package daoimpl;

import java.sql.*;

import connection.ConnectionMySql;
import daointerface.DAO;

public class DAOLicence implements DAO {

	private static DAOLicence instance;
	private Connection cnx;

	private DAOLicence() {
		cnx = ConnectionMySql.getConnection();
	}

	public static DAOLicence instance() {
		if (instance == null) {
			instance = new DAOLicence();
		}
		return instance;
	}

	@Override
	public boolean checkLicence(String licence) throws SQLException {
		String sql = "SELECT COUNT(licence) as nb FROM `licences` WHERE `licence`= ?";
		PreparedStatement statement = cnx.prepareStatement(sql);
		statement.setString(1, licence);
		ResultSet rs = statement.executeQuery();
		rs.first();
		int nombreResultat = rs.getInt("nb");
		rs.close();
		return nombreResultat > 0;
	}

	@Override
	public String getChecksum(String licence) throws SQLException {
		String sql = "SELECT `build_checksum` FROM builds b, licences l WHERE l.id_build = b.id_build AND l.licence = ?";
		PreparedStatement statement = cnx.prepareStatement(sql);
		statement.setString(1, licence);
		ResultSet rs = statement.executeQuery();
		String checksum = null;
		if(rs.first()) {
			checksum = rs.getString("build_checksum");
		}
		rs.close();
		return checksum;
	}
	
	@Override
	protected void finalize() throws Throwable {
		cnx.close();
		super.finalize();
	}

	@Override
	public int getNbMaxUsers(String licence) throws SQLException {
		String sql = "SELECT `nb_users_max` FROM `licences` WHERE `licence` = ?";
		PreparedStatement statement = cnx.prepareStatement(sql);
		statement.setString(1, licence);
		ResultSet rs = statement.executeQuery();
		int nbUsersMax = 0;
		if(rs.first()) {
			nbUsersMax = rs.getInt("nb_users_max");
		}
		rs.close();
		return nbUsersMax;
	}
}
