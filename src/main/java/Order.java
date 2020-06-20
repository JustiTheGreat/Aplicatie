import java.util.ArrayList;

public class Order {
    private long time;
    private String nume;
    private String adresa;
    private float cost;
    private String stare;
    private String magazin;
    private ArrayList<AllProducts> produse;
    private String timp_livrare;

    public Order(long time,String nume, String adresa, float cost, String stare, String magazin, ArrayList<AllProducts> produse,String timp_livrare) {
        this.time=time;
        this.nume = nume;
        this.adresa = adresa;
        this.cost = cost;
        this.stare = stare;
        this.magazin=magazin;
        this.produse = produse;
        this.timp_livrare=timp_livrare;
    }

    public void setStare(String stare) {
        this.stare = stare;
    }

    public void setTimpLivrare(String timp_livrare) { this.timp_livrare=timp_livrare; }

    public long getTime() {
        return time;
    }

    public String getNume() {
        return nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public float getCost() {
        return cost;
    }

    public String getStare() { return stare; }

    public String getMagazin() { return magazin; }

    public ArrayList<AllProducts> getProduse() {
        return produse;
    }

    public String getTimpLivrare() { return timp_livrare; }
}
