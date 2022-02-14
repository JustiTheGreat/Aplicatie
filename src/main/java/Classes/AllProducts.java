package Classes;

abstract public class AllProducts {
    private String denumire;
    private String categorie;
    private String magazin;
    private String disponibilitate;
    private String object;
    private Long id;

    public AllProducts(String denumire,String categorie,String magazin,String disponibilitate,String object,long id) {
        this.denumire=denumire;
        this.categorie=categorie;
        this.magazin=magazin;
        this.disponibilitate=disponibilitate;
        this.object = object;
        this.id=id;
    }

    public String getDenumire() { return denumire; }

    public String getCategorie() { return categorie; }

    public String getMagazin() { return magazin; }

    public String getDisponibilitate() { return disponibilitate; }

    public String getObject() { return object; }

    public Long getId() { return id; }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setMagazin(String magazin) {
        this.magazin = magazin;
    }

    public void setDisponibilitate(String disponibilitate) {
        this.disponibilitate = disponibilitate;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setId(Long id) {
        this.id = id;
    }

    abstract public String toLV();
}
