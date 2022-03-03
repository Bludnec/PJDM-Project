package it.uniroma2.pjdm.mySugarLevel.dao;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public interface InsertValoreDAO {
	public int insertValore(Valore valore);
	public void closeConnection();
}
