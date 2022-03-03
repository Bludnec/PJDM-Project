package it.uniroma2.pjdm.mySugarLevel.dao;

import java.util.*;
import it.uniroma2.pjdm.mySugarLevel.entity.Valore;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

public class ValoriDAOJDBCImpl implements ValoriDAO{

	private Connection conn;

	public ValoriDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {

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
		}	}

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
	public ArrayList<Valore> loadValoreByUnaData(int id_utente, String data) {
		String query = "SELECT * FROM diario WHERE data = \"" + data + "\" AND id_utente = \"" + id_utente +"\"";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			ArrayList<Valore> res = new ArrayList<>();

			while (rset.next()) {
				int id_utente1 = rset.getInt(1);
				String data1 = rset.getString(2);
				String orario = rset.getString(3);
				int valore = rset.getInt(4);
				int insulina = rset.getInt(5);
				int cibo = rset.getInt(6);

				Valore m = new Valore(id_utente1,data1,orario,valore,insulina,cibo);
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
