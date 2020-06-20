public class Product extends AllProducts{
    private String nume;
    private Float pret;
    private String categorie;
    private String magazin;
    private int cantitate;
    private String disponibilitate;

    public Product(String nume, Float pret, String categorie, String magazin, int cantitate, String disponibilitate){
        super("PRODUCT");
        this.nume=nume;
        this.pret=pret;
        this.categorie=categorie;
        this.magazin=magazin;
        this.cantitate=cantitate;
        this.disponibilitate=disponibilitate;
    }

    public void set(String nume,Float pret,String categorie,String magazin,int cantitate,String disponibilitate) {
        this.nume = nume;
        this.pret = pret;
        this.categorie = categorie;
        this.magazin = magazin;
        this.cantitate = cantitate;
        this.disponibilitate = disponibilitate;
    }

    public String getNume() {
        return nume;
    }

    public Float getPret() {
        return pret;
    }

    public int getCantitate() {
        return cantitate;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getMagazin() {
        return magazin;
    }



    public String getDisponibilitate() {
        return disponibilitate;
    }

    public void setDisponibilitate(String disponibilitate) {
        this.disponibilitate=disponibilitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate=cantitate;
    }

    public String toString()
    {
        return nume + " " + pret + " " + categorie + " " + magazin + " " + cantitate + " " + disponibilitate;
    }

    public String toLV()
    {
        return "DENUMIRE: " + nume + "     PRET: " + pret + "     CATEGORIE: " + categorie + "     MAGAZIN: " + magazin + "     CANTITATE: " + cantitate + "     DISPONIBILITATE: " + disponibilitate;
    }
}
