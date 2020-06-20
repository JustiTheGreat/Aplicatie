public class Centru extends Utilizatori{
    private String numecentru;

    public Centru() {
    }

    public Centru(String numecentru, String judet, String localitate, String strada,
                  String numar, String telefon, String username, String parola
                 ) {
        this.numecentru = numecentru;
        super.judet = judet;
        super.localitate = localitate;
        super.strada = strada;
        super.numar = numar;
        super.telefon = telefon;
        super.username = username;
        super.parola = parola;
        super.categorie="Centru";
    }

    public String getNumecentru() {
        return numecentru;
    }

    public void setNumecentru(String numecentru) {
        this.numecentru = numecentru;
    }

}
