package Classes;

public class Animal extends AllProducts {
    private String sex;
    private int anul_nasterii;

    public Animal(String denumire, String categorie, String sex, int anul_nasterii, String magazin, String disponibilitate, Long id) {
        super(denumire,categorie,magazin,disponibilitate,"animal",id);
        this.sex = sex;
        this.anul_nasterii = anul_nasterii;
    }

    public String getSex() {
        return sex;
    }

    public int getAnul_nasterii() {
        return anul_nasterii;
    }

    public String toLV()
    {
        return "DENUMIRE: " + this.getDenumire() + "     TIP: " + this.getCategorie() + "     SEX: " + this.getSex() + "     DATA_NASTERII: " + this.getAnul_nasterii()
                + "     VANZATOR: " + this.getMagazin() + "     DISPONIBILITATE: " + this.getDisponibilitate() + "     ID: " + this.getId();
    }

    public void set(String denumire,String categorie,String sex,int anul_nasterii,String magazin,String disponibilitate,String object,Long id) {
        this.sex=sex;
        this.anul_nasterii=anul_nasterii;
        this.setDenumire(denumire);
        this.setCategorie(categorie);
        this.setMagazin(magazin);
        this.setDisponibilitate(disponibilitate);
        this.setObject(object);
        this.setId(id);
    }
}
