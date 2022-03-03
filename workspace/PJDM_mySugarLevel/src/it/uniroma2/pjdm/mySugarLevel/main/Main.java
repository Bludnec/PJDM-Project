package it.uniroma2.pjdm.mySugarLevel.main;

import it.uniroma2.pjdm.mySugarLevel.dao.*;

public class Main {

	public static void main(String[] args) {
		String ip = "127.0.0.1";
		String port = "3306";
		String dbName = "mySugarLevel";
		String userName = "root";
		String password = "lorenzo1";
		
		ValoriDAO dao = new ValoriDAOJDBCImpl(ip, port, dbName, userName, password);
		
		dao.closeConnection();

		System.out.println("\nConnection Closed");
	}
}
