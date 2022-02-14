package Classes;

abstract public class Utilizator {
    private String judet;
    private String localitate;
    private String strada;
    private int numar;
    private int telefon;
    private String username;
    private String parola;
    private String categorie;

    public Utilizator(String judet, String localitate, String strada, int numar, int telefon, String username, String parola, String categorie) {
        this.judet = judet;
        this.localitate = localitate;
        this.strada = strada;
        this.numar = numar;
        this.telefon = telefon;
        this.username = username;
        this.parola = parola;
        this.categorie = categorie;
    }

    public String toString()
    {
        return "USERNAME: "+getUsername()+" CATEGORIE: "+ this.getCategorie() +" JUDET: "+getJudet()+" TELEFON: "+getTelefon();
    }

    public String getJudet() {
        return judet;
    }

    public String getLocalitate() {
        return localitate;
    }

    public String getStrada() {
        return strada;
    }

    public int getNumar() {
        return numar;
    }

    public int getTelefon() {
        return telefon;
    }

    public String getUsername() {
        return username;
    }

    public String getParola() {
        return parola;
    }

    public String getCategorie() {
        return categorie;
    }
}
