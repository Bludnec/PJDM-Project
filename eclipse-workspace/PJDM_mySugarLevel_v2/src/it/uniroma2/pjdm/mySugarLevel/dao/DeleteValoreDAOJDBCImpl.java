package it.uniroma2.pjdm.mySugarLevel.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public class DeleteValoreDAOJDBCImpl implements DeleteValoreDAO{
	
	private Connection conn;

	public DeleteValoreDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {

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
	public boolean deleteValore(Valore valore) {
		int id_utente = valore.getId();
		String data = valore.getData();
		String orario = valore.getOrario();

		String SQL = "DELETE FROM diario WHERE \"" + id_utente + "\" AND data = \"" + data + "\" AND orario = \"" + orario + "\"";

		try {
			Statement statement = conn.createStatement();
			statement.execute(SQL);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	@Override
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
