package it.uniroma2.pjdm.mySugarLevel.dao;

import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public interface ModifyValoreDAO {
	public boolean modifyValore(Valore valore, String orario, int Valore, int insulina, int cibo);
	public void closeConnection();
}
