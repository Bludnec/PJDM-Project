package it.uniroma2.pjdm.mySugarLevel.entity;

public class Valore {

	private int id_utente;
	private String data;
	private String orario;
	private int valore;
	private int insulina;
	private int cibo;
	
	public Valore(int id_utente,String data,String orario,int valore,int insulina,int cibo ) {
		this.id_utente = id_utente;
		this.data = data;
		this.orario = orario;
		this.valore = valore;
		this.insulina = insulina;
		this.cibo = cibo;
	}
	
	public int getId() {
		return id_utente;
	}
	public void setId(int id_utente) {
		this.id_utente = id_utente;
	}
	public int getValore() {
		return valore;
	}
	public void setValore(int valore) {
		this.valore = valore;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getOrario() {
		return orario;
	}
	public void setOrario(String orario) {
		this.orario = orario;
	}
	public int getInsulina() {
		return insulina;
	}
	public void setInsulina(int insulina) {
		this.insulina = insulina;
	}
	public int getCibo() {
		return cibo;
	}
	public void setCibo(int cibo) {
		this.cibo = cibo;
	}
}
