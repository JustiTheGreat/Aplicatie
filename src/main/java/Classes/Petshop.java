package Classes;

public class Petshop extends Utilizator {
    private String nume_petshop;

    public Petshop(String nume_petshop, String judet, String localitate, String strada, int numar, int telefon, String username, String parola) {
        super(judet,localitate,strada,numar,telefon,username,parola,"petshop");
        this.nume_petshop = nume_petshop;
    }

    public String getNume_petshop() {
        return nume_petshop;
    }
}
