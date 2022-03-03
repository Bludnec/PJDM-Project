package it.uniroma2.pjdm.mySugarLevel.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public class LoadValoreByDateDAOJDBCImpl implements LoadValoreByDateDAO{
	private Connection conn;

	public LoadValoreByDateDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {

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
	public ArrayList<Valore> loadValoreByDate(int id_utente, String data1, String data2) {
		String query = "SELECT * FROM diario WHERE id_utente = \""+ id_utente+"\" AND data BETWEEN \"" + data1 + "\" AND \"" + data2 + "\"" ;
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			ArrayList<Valore> res = new ArrayList<>();

			while (rset.next()) {
				int id_utente1 = rset.getInt(1);
				String data = rset.getString(2);
				String orario = rset.getString(3);
				int valore = rset.getInt(4);
				int insulina = rset.getInt(5);
				int cibo = rset.getInt(6);

				Valore m = new Valore(id_utente1,data,orario,valore,insulina,cibo);
				res.add(m);
			}

			rset.close();
			stmt.close();

			return res;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
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
