import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EditeazaAnimal {
    private String shop_username;
    private ArrayList<AllProducts> all_products;
    private ListView<String> lv;
    private RadioButton fem;
    private RadioButton masc;
    private RadioButton pisici;
    private RadioButton caini;
    private RadioButton papagali;
    private RadioButton pestisori;
    private RadioButton hamsteri;
    private RadioButton az;
    private RadioButton dn;
    private int az_state,dn_state;
    private Stage stage;
    @FXML
    private ChoiceBox disp;
    int index;

    public void set(Stage stage, String shop_username,ArrayList<AllProducts> all_products, ListView<String> lv, RadioButton fem, RadioButton masc, RadioButton pisici, RadioButton caini, RadioButton papagali, RadioButton pestisori, RadioButton hamsteri, RadioButton az, RadioButton dn,int az_state,int dn_state,int index) {
        this.stage = stage;
        this.shop_username = shop_username;
        this.all_products = all_products;
        this.lv = lv;
        this.fem=fem;
        this.masc=masc;
        this.pisici=pisici;
        this.caini=caini;
        this.papagali=papagali;
        this.pestisori=pestisori;
        this.hamsteri=hamsteri;
        this.az=az;
        this.dn=dn;
        this.index=index;
        disp.getItems().addAll("DISPONIBIL","INDISPONIBIL");
        if (az.isSelected())
        {
            this.dn.setSelected(false);
            this.dn_state=0;
            if (az_state == 1) {
                this.az.setSelected(true);
                this.az_state = 0;
            } else if (az_state == 2) {
                this.az.setSelected(false);
                this.az_state = 1;
            }
        }
        else if (dn.isSelected())
        {
            this.az.setSelected(false);
            this.az_state=0;
            if (dn_state == 1) {
                this.dn.setSelected(true);
                this.dn_state = 0;
            } else if (dn_state == 2) {
                this.dn.setSelected(false);
                this.dn_state = 1;
            }
        }
        else
        {
            this.az.setSelected(false);
            this.az_state=0;
            this.dn.setSelected(false);
            this.dn_state=0;
        }
    }

    public void setCaracteristici()
    {
        disp.setValue(all_products.get(index).getDisponibilitate());
    }

    public void close()
    {
        stage.close();
    }

    public void editLV () {
        Animal a=((Animal)all_products.get(index));
        a.set(a.getNumeRasa(),a.getTip(),a.getSex(),a.getDataNasterii(),a.getCentru(),disp.getValue().toString());
        write();
        filt_and_sort();
        stage.close();
    }
    public void write() {
        try {
            FileWriter myWriter = new FileWriter("src/main/produse.txt", false);
            for (AllProducts p : all_products)
                myWriter.write(p.getObject() + " " + p.toString() + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void message(String title,String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Message.fxml"));
            Parent fxml = loader.load();
            Stage stage = new Stage();
            Message controller = loader.getController();
            controller.set(message, stage);
            stage.setTitle(title);
            stage.setScene(new Scene(fxml));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void filt_and_sort() {
        String[] c ={"FEM","MASC","PISICI","CAINI","PAPAGALI","PESTISORI","HAMSTERI"};
        boolean[] f={false,false,false,false,false,false,false},s ={false,false,false,false};
        try {
            if (fem.isSelected()) f[0] = true;
            if (masc.isSelected()) f[1] = true;
            if (pisici.isSelected()) f[2] = true;
            if (caini.isSelected()) f[3] = true;
            if (papagali.isSelected()) f[4] = true;
            if (pestisori.isSelected()) f[5] = true;
            if (hamsteri.isSelected()) f[6] = true;
            if(az.isSelected()&&az_state==0)
            {
                if(dn.isSelected())
                {
                    dn.setSelected(false);
                    dn_state =0;
                    dn.setText("Data nasterii (crescator)");
                }
                s[0]=true;
                az_state=1;
            }
            else if(az.isSelected()&&az_state==1)
                s[0]=true;
            else if(!az.isSelected()&&az_state==1)
            {
                az.setText("Alfabetic (Z-A)");
                az.setSelected(true);
                s[1]=true;
                az_state=2;
            }
            else if(az.isSelected()&&az_state==2)
                s[1]=true;
            else if(!az.isSelected()&&az_state==2)
            {
                az.setText("Alfabetic (A-Z)");
                az_state=0;
            }

            if(dn.isSelected()&& dn_state ==0)
            {
                if(az.isSelected())
                {
                    s[0]=false;s[1]=false;
                    az.setSelected(false);
                    az_state=0;
                    az.setText("Alfabetic (A-Z)");
                }
                s[2]=true;
                dn_state =1;
            }
            else if(dn.isSelected()&&dn_state==1)
                s[2]=true;
            else if(!dn.isSelected()&&dn_state ==1)
            {
                dn.setText("Pret (descrescator)");
                dn.setSelected(true);
                s[3]=true;
                dn_state =2;
            }
            else if(dn.isSelected()&&dn_state==2)
                s[3]=true;
            else if(!dn.isSelected()&&dn_state ==2)
            {
                dn.setText("Pret (crescator)");
                dn_state =0;
            }
        } catch (Exception e) { }
        ArrayList<Animal> filt = new ArrayList<Animal>();
        ArrayList<Animal> filt_and_sort = new ArrayList<Animal>();
        lv.getItems().clear();
        if(f[0]||f[1]||f[2]) {
            for (AllProducts p : all_products)
                if (p.getObject().equals("ANIMAL") && ((Animal) p).getCentru().equals(shop_username))
                    if (((Animal) p).getTip().equalsIgnoreCase(c[0]) && f[0] || ((Animal) p).getTip().equalsIgnoreCase(c[1]) && f[1] || ((Animal) p).getTip().equalsIgnoreCase(c[2]) && f[2] || ((Animal) p).getTip().equalsIgnoreCase(c[3]) && f[3] || ((Animal) p).getTip().equalsIgnoreCase(c[4]) && f[4] || ((Animal) p).getTip().equalsIgnoreCase(c[5]) && f[5] || ((Animal) p).getTip().equalsIgnoreCase(c[6]) && f[6])
                        filt.add(((Animal)p));
        }
        else
            for (AllProducts p : all_products)
                if (p.getObject().equals("ANIMAL") && ((Animal) p).getCentru().equals(shop_username))
                    filt.add(((Animal)p));
        if(!filt.isEmpty()) {
            if (s[0]||s[1]||s[2]||s[3]) {
                int i,n=filt.size();
                for (i = 0; i < n; i++) {
                    Animal aux = filt.get(0);
                    for (Animal p : filt)
                        if(s[0] && p.getNumeRasa().toUpperCase().compareTo(aux.getNumeRasa().toUpperCase())<0 || s[1] && p.getNumeRasa().toUpperCase().compareTo(aux.getNumeRasa().toUpperCase())>0 || s[2] && p.getDataNasterii() < aux.getDataNasterii() || s[3] && p.getDataNasterii() > aux.getDataNasterii())
                            aux = p;
                    filt_and_sort.add(aux);
                    filt.remove(aux);
                }
            }
            else filt_and_sort.addAll(filt);
            for (Animal p : filt_and_sort)
                lv.getItems().add(p.toLV());
        }
    }
}