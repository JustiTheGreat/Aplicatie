package Classes;

public class Client extends Utilizator {
    private String nume;
    private String prenume;

    public Client(String nume, String prenume, String judet, String localitate, String strada, int numar, int telefon, String username, String parola) {
        super(judet,localitate,strada,numar,telefon,username,parola,"client");
        this.nume = nume;
        this.prenume = prenume;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }
}
