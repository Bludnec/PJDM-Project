package it.uniroma2.pjdm.mySugarLevel.dao;

import java.util.*;
import it.uniroma2.pjdm.mySugarLevel.entity.Valore;

public interface ValoriDAO {

	public Valore loadValore(int id_utente,String data,String orario);
	
	public ArrayList<Valore> loadValoreByDate(int id_utente, String data1, String data2);
	
	public ArrayList<Valore> loadValoreByUnaData(int id_utente, String data);
	
	public int insertValore(Valore valore);
	
	public boolean deleteValore(Valore valore);
	
	public boolean modifyValore(Valore valore, String orario, int Valore, int insulina, int cibo);
	
	public void closeConnection();
	
}
