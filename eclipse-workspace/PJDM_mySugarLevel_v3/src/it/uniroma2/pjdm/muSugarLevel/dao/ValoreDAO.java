package it.uniroma2.pjdm.muSugarLevel.dao;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public interface ValoreDAO {
	public Valore loadValore(int id_utente,String data,String orario);
	public int insertValore(Valore valore);
	public boolean deleteValore(Valore valore);
	
	public void closeConnection();
}
