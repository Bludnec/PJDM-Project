package pjdm.pjdm2021.mysugarlevel;

public class ElementoLista {

    private int valore;
    private String orario;
    private boolean insulina;
    private boolean cibo;
    private String data;

    public ElementoLista(int valore, String orario, boolean insulina, boolean cibo) {
        this.valore = valore;
        this.orario = orario;
        this.insulina = insulina;
        this.cibo = cibo;
    }

    public int getValore() {
        return valore;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    public String getOrario() {
        return orario;
    }

    public void setOrario(String orario) {
        this.orario = orario;
    }

    public boolean isInsulina() {
        return insulina;
    }

    public void setInsulina(boolean insulina) {
        this.insulina = insulina;
    }

    public boolean isCibo() {
        return cibo;
    }

    public void setCibo(boolean cibo) {
        this.cibo = cibo;
    }
}

