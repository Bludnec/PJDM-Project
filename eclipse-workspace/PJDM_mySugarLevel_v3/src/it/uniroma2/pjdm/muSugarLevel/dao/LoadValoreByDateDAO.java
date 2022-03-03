package it.uniroma2.pjdm.muSugarLevel.dao;

import java.util.ArrayList;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public interface LoadValoreByDateDAO {
	public ArrayList<Valore> loadValoreByDate(int id_utente, String data1, String data2);
	public void closeConnection();
}
