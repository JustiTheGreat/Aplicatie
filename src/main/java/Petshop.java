public class Petshop extends Utilizatori{
    private String numepetshop;

    public Petshop() {
    }

    public Petshop(String numepetshop, String judet, String localitate, String strada,
                   String numar, String telefon, String username, String parola
                  ) {
        this.numepetshop = numepetshop;
        super.judet = judet;
        super.localitate = localitate;
        super.strada = strada;
        super.numar = numar;
        super.telefon = telefon;
        super.username = username;
        super.parola = parola;
        super.categorie="Petshop";
    }

    public String getNumepetshop() {
        return numepetshop;
    }

    public void setNumepetshop(String numepetshop) {
        this.numepetshop = numepetshop;
    }
}
