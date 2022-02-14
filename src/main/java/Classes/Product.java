package Classes;

public class Product extends AllProducts {
    private Float pret;
    private int cantitate;

    public Product(String denumire, String categorie, Float pret, int cantitate, String magazin, String disponibilitate, Long id) {
        super(denumire,categorie,magazin,disponibilitate,"product",id);
        this.pret = pret;
        this.cantitate = cantitate;
    }

    public Float getPret() {
        return pret;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void set(String denumire,String categorie,Float pret,int cantitate,String magazin,String disponibilitate,String object,Long id) {
        this.pret=pret;
        this.cantitate=cantitate;
        this.setDenumire(denumire);
        this.setCategorie(categorie);
        this.setMagazin(magazin);
        this.setDisponibilitate(disponibilitate);
        this.setObject(object);
        this.setId(id);
    }

    public void setCantitate(int cantitate) { this.cantitate=cantitate; }

    public String toLV() {
        return "DENUMIRE: " + this.getDenumire() + "     TIP: " + this.getCategorie() + "     PRET: " + this.getPret() + "     CANTITATE: " + this.getCantitate()
                + "     VANZATOR: " + this.getMagazin() + "     DISPONIBILITATE: " + this.getDisponibilitate() + "     ID: " + this.getId();
    }
}
