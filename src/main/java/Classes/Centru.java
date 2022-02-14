package Classes;

public class Centru extends Utilizator {
    private String nume_centru;

    public Centru(String nume_centru, String judet, String localitate, String strada, int numar, int telefon, String username, String parola) {
        super(judet,localitate,strada,numar,telefon,username,parola,"centru");
        this.nume_centru = nume_centru;
    }

    public String getNume_centru() {
        return nume_centru;
    }
}
