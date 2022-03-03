package it.uniroma2.pjdm.mySugarLevel.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public class ModifyValoreDAOJDBCImpl implements ModifyValoreDAO{
	
	private Connection conn;

	public ModifyValoreDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {

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
	/* Se viene cambiato solo un valore, prendere i valori di prima e cambiarli*/
	public boolean modifyValore(Valore oldVal, String orario, int valore, int insulina, int cibo) {
		int oldId_utente = oldVal.getId();
		String oldData = oldVal.getData();
		String oldOrario = oldVal.getOrario();
		
		String SQL = "UPDATE diario SET orario = \""+ orario + "\", valore = " + valore + ", insulina = " + insulina + ", cibo = " + cibo + " WHERE id_utente = \"" + oldId_utente + "\" AND data = \"" + oldData + "\" AND orario = \"" + oldOrario + "\"";
		
		try {
			Statement statement = conn.createStatement();
			statement.execute(SQL);
			return true;
			
			
		} catch (SQLException e){
			e.printStackTrace();
			return false;
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
