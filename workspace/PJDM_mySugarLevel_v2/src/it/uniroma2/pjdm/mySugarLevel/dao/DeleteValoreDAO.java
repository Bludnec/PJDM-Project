package it.uniroma2.pjdm.mySugarLevel.dao;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public interface DeleteValoreDAO {
	public boolean deleteValore(Valore valore);
	public void closeConnection();
}
