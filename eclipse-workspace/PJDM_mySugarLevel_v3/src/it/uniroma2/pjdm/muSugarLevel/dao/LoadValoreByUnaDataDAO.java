package it.uniroma2.pjdm.muSugarLevel.dao;

import java.util.ArrayList;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public interface LoadValoreByUnaDataDAO {
	public ArrayList<Valore> loadValoreByUnaData(int id_utente, String data);
	public void closeConnection();
}
