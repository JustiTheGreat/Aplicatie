public class Animal extends AllProducts {
    private String tip;
    private String nume_rasa;
    private String sex;
    private int data_nasterii;
    private String centru;
    private String disponibilitate;

    public Animal(String nume_rasa,String tip, String sex, int data_nasterii, String centru, String disponibilitate) {
        super("ANIMAL");
        this.tip = tip;
        this.nume_rasa = nume_rasa;
        this.sex = sex;
        this.data_nasterii = data_nasterii;
        this.centru = centru;
        this.disponibilitate = disponibilitate;
    }

    public void set( String nume_rasa, String tip,String sex, int data_nasterii, String centru, String disponibilitate) {
        this.tip = tip;
        this.nume_rasa = nume_rasa;
        this.sex = sex;
        this.data_nasterii = data_nasterii;
        this.centru = centru;
        this.disponibilitate = disponibilitate;
    }

    public String getTip() {
        return tip;
    }

    public String getNumeRasa() {
        return nume_rasa;
    }

    public String getSex() {
        return sex;
    }

    public int getDataNasterii() {
        return data_nasterii;
    }

    public String getCentru() {
        return centru;
    }

    public String getDisponibilitate() {
        return disponibilitate;
    }

    public void setDisponibilitate(String disponibilitate) {
        this.disponibilitate=disponibilitate;
    }

    public String toString()
    {
        return nume_rasa + " " + tip + " " + sex + " " + data_nasterii + " " + centru + " " + disponibilitate;
    }

    public String toLV()
    {
        return "RASA: " + nume_rasa + "     TIP: " + tip + "     SEX: " + sex + "     DATA NASTERII: " + data_nasterii + "     CENTRU: " + centru + "     DISPONIBILITATE: " + disponibilitate;
    }
}
