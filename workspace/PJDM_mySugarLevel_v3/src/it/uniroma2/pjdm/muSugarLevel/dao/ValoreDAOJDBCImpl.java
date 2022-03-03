package it.uniroma2.pjdm.muSugarLevel.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public class ValoreDAOJDBCImpl implements ValoreDAO{
	
	private Connection conn;

	public ValoreDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {

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
	public Valore loadValore(int id_utente, String data, String orario) {
		String query = "SELECT * FROM diario WHERE id_utente = \"" + id_utente + "\" AND data = \"" + data + "\" AND orario = \"" + orario + "\"";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			Valore res = null;

			while (rset.next()) {
				int id1 = rset.getInt(1);
				String data1 = rset.getString(2);
				String orario1 = rset.getString(3);
				int valore = rset.getInt(4);
				int insulina = rset.getInt(5);
				int cibo = rset.getInt(6);
				
				res = new Valore(id1,data1,orario1,valore,insulina,cibo);

				break;
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
