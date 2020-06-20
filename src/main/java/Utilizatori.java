abstract public class Utilizatori {
    protected String judet;
    protected String localitate;
    protected String strada;
    protected String numar;
    protected String telefon;
    protected String username;
    protected String parola;
    protected String categorie;

    public String getJudet() {
        return judet;
    }

    public String getLocalitate() {
        return localitate;
    }

    public String getStrada() {
        return strada;
    }

    public String getNumar() {
        return numar;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getUsername() {
        return username;
    }

    public String getParola() {
        return parola;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public void setLocalitate(String localitate) {
        this.localitate = localitate;
    }

    public void setStrada(String strada) {
        this.strada = strada;
    }

    public void setNumar(String numar) {
        this.numar = numar;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) { this.categorie = categorie; }
}
