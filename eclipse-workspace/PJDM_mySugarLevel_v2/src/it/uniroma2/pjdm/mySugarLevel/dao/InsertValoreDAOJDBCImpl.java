package it.uniroma2.pjdm.mySugarLevel.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public class InsertValoreDAOJDBCImpl implements InsertValoreDAO{
	private Connection conn;

	public InsertValoreDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection(
					"jdbc:mysql://" + ip + ":" + port + "/" + dbName
							+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					userName, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int insertValore(Valore valore) {
		String SQL = "INSERT INTO diario " + "VALUES(?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(1, valore.getId());
			pstmt.setString(2,valore.getData());
			pstmt.setString(3, valore.getOrario());
			pstmt.setInt(4, valore.getValore());
			pstmt.setInt(5, valore.getInsulina());
			pstmt.setInt(6, valore.getCibo());

			int affectedRows = pstmt.executeUpdate();

			return affectedRows;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
	}
	
	@Override
	/* Chiude le connessioni con DB*/
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			Enumeration<Driver> enumDrivers = DriverManager.getDrivers();
			while (enumDrivers.hasMoreElements()) {
				Driver driver = enumDrivers.nextElement();
				DriverManager.deregisterDriver(driver);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
