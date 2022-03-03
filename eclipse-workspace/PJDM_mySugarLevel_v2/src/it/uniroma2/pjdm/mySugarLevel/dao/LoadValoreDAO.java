package it.uniroma2.pjdm.mySugarLevel.dao;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public interface LoadValoreDAO {
	public Valore loadValore(int id_utente,String data,String orario);
	public void closeConnection();
}
