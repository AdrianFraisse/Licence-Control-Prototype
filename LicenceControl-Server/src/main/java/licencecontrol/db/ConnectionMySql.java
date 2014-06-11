package licencecontrol.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySql {
	private static final String DB_URL = "jdbc:mysql://localhost/licencedb";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "";
	
	private static Connection connection;
	
	public static Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}
	
	
}
