package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySql {
	private static String url = "jdbc:mysql://localhost/";
	private static String dbName = "licencedb";
	private static String username = "root";
	private static String password = "";
	
	private static Connection cnx;
	
	public static Connection getConnection() {
		if (cnx == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				cnx = DriverManager.getConnection(url+dbName, username, password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cnx;
	}
	
	
}
