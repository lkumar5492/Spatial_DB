package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {

	private Connection con;

	public Connection connect() {

		try {
			Class.forName("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "lava");

		} catch (Exception e) {
			System.out.println("Exception while connecting to DB::::");
			e.printStackTrace();
		}
		return con;

	}

	public void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
