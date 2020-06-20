public class Client extends Utilizatori {
    private String nume;
    private String prenume;

    public Client() {
    }

    public Client(String nume, String prenume, String judet,
                  String localitate, String strada, String numar,
                  String telefon, String username, String parola
                 ) {
        this.nume = nume;
        this.prenume = prenume;
        super.judet = judet;
        super.localitate = localitate;
        super.strada = strada;
        super.numar = numar;
        super.telefon = telefon;
        super.username = username;
        super.parola = parola;
        super.categorie="Client";
    }

    public String getNume() { return nume; }

    public String getPrenume() { return prenume; }

    public void setPrenume(String prenume) { this.prenume = prenume; }

    public void setNume(String nume) { this.nume = nume; }
}
