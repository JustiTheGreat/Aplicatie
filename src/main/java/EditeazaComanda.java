import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EditeazaComanda {
    private String shop_username;
    private String shop_adress;
    private ArrayList<AllProducts> all_products;
    private ArrayList<Order> shop_orders = new ArrayList<Order>();
    @FXML
    private ListView<String> lv = new ListView<String>();
    private Stage stage;
    private int index;
    @FXML
    private ChoiceBox stare;

    public void set(Stage stage,String shop_username,String shop_adress,ArrayList<AllProducts> all_products,ArrayList<Order> shop_orders, ListView<String> lv,int index) {
        this.stage = stage;
        this.shop_username=shop_username;
        this.shop_adress=shop_adress;
        this.all_products=all_products;
        this.shop_orders=shop_orders;
        this.lv=lv;
        this.index = index;
        setCaracteristici();
    }

    public void setCaracteristici() {
        stare.getItems().addAll("PRIMITA","EXPEDIATA","ANULATA");
        stare.setValue(shop_orders.get(index).getStare().toUpperCase());
    }

    public void close() { stage.close(); }

    public void editLV () {
        if(stare.getValue().toString().equalsIgnoreCase("PRIMITA")) {
            if(shop_orders.get(index).getStare().equals("EXPEDIATA"))message("Eroare!","Comanda a fost deja expediata!");
            else if(shop_orders.get(index).getStare().equals("ANULATA"))message("Eroare!","Comanda este deja anulata!");
        }
        else if(stare.getValue().toString().equals("EXPEDIATA"))
        {
            if(shop_orders.get(index).getStare().equals("TRIMISA"))
            {
                boolean ok=true;
                Integer[] cant=new Integer[all_products.size()];
                for(AllProducts p : all_products)
                    if (p.getObject().equals("PRODUCT"))
                        cant[all_products.indexOf(p)]=new Integer(((Product)p).getCantitate());
                    else cant[all_products.indexOf(p)]=new Integer(0);
                for (AllProducts p : shop_orders.get(index).getProduse())
                    if (p.getDisponibilitate().equals("DISPONIBIL"))
                        for (AllProducts q : all_products)
                            if (p.getObject().equals("ANIMAL") && q.getObject().equals("ANIMAL") && ((Animal) p).getNumeRasa().equals(((Animal) q).getNumeRasa())) {
                                if (q.getDisponibilitate().equals("INDISPONIBIL"))
                                    ok = false;
                            }
                            else if (p.getObject().equals("PRODUCT")&&q.getObject().equals("PRODUCT") && ((Product) p).getNume().equals(((Product) q).getNume()))
                                cant[all_products.indexOf(q)]=cant[all_products.indexOf(q)].intValue()-1;
                for(Integer i:cant)if(i.intValue()<0)ok=false;
                if(!ok)message("Eroare!", "Comanda nu poate fi realizata!");
                else
                {
                    for (AllProducts p : shop_orders.get(index).getProduse())
                    if (p.getDisponibilitate().equals("DISPONIBIL"))
                        for (AllProducts q : all_products)
                            if (p.getObject().equals("ANIMAL") && q.getObject().equals("ANIMAL") && ((Animal) p).getNumeRasa().equals(((Animal) q).getNumeRasa()))
                            {
                                p.setDisponibilitate("INDISPONIBIL");
                                q.setDisponibilitate("INDISPONIBIL");
                            }
                            else if (p.getObject().equals("PRODUCT")&&q.getObject().equals("PRODUCT") && ((Product) p).getNume().equals(((Product) q).getNume())) {
                                p.setDisponibilitate("INDISPONIBIL");
                                ((Product)q).setCantitate(((Product)q).getCantitate()-1);
                                if(((Product)q).getCantitate()==0)q.setDisponibilitate("INDISPONIBIL");
                            }
                    shop_orders.get(index).setStare("EXPEDIATA");
                            shop_orders.get(index).setTimpLivrare(shop_orders.get(index).getAdresa().equals(shop_adress)?"24h":"48h");
                    write();
                    write2();
                }
            }
            else if(shop_orders.get(index).getStare().equals("EXPEDIATA"))message("Eroare!","Comanda a fost deja expediata!");
            else message("Eroare!","Comanda este deja anulata!");
        }
        else if(stare.getValue().equals("ANULATA")) {
            if(shop_orders.get(index).getStare().equals("TRIMISA")) {
                for (AllProducts p : shop_orders.get(index).getProduse())
                    p.setDisponibilitate("INDISPONIBIL");
                shop_orders.get(index).setStare("ANULATA");
            }
            else if(shop_orders.get(index).getStare().equals("EXPEDIATA"))message("Eroare!","Comanda a fost deja expediata!");
            else message("Eroare!","Comanda este deja anulata!");
            write();
        }
        stage.close();
    }
    public void write() {
        if (!shop_orders.isEmpty())
            try {
                FileWriter myWriter = new FileWriter("src/main/comenzi.txt", false);
                for(Order o:shop_orders)
                {
                    String produse = "";
                    for (AllProducts p : o.getProduse())
                        if (p.getObject().equals("ANIMAL"))
                            produse = produse + " " + ((Animal) p).getNumeRasa();
                        else produse = produse + " " + ((Product) p).getNume();
                    myWriter.write("TIME:" + o.getTime() + " NUME:" + o.getNume() + " ADRESA:" + o.getAdresa() + " COST:" + o.getCost() + " STARE:" + o.getStare() + " MAGAZIN:" + o.getMagazin() + " TIMP_LIVRARE:" + o.getTimpLivrare() + " OBIECTE:" + produse+"\n");
                    for (AllProducts p : o.getProduse())
                        myWriter.write(p.getObject() + " " + p.toString() + "\n");
                }
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        else System.out.println("gol");
        reloadLV();
    }

    public void write2() {
        try {
            FileWriter myWriter = new FileWriter("src/main/produse.txt", false);
            for (AllProducts p : all_products)
                myWriter.write(p.getObject() + " " + p.toString() + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void reloadLV()
    {
        lv.getItems().clear();
        if(shop_orders.isEmpty()) lv.getItems().add("Nu ati primit nici o comanda!");
        else for (Order o : shop_orders)
            if(o.getMagazin().equals(shop_username)) {
                String produse = "";
                for (AllProducts p : o.getProduse())
                    if (p.getObject().equals("ANIMAL"))
                        produse = produse + " " + ((Animal) p).getNumeRasa();
                    else produse = produse + " " + ((Product) p).getNume();
                lv.getItems().add("TIME: " + o.getTime() + "   NUME: " + o.getNume() + "   ADRESA: " + o.getAdresa() + "   COST: " + o.getCost() + "   STARE: " + (o.getStare().equals("TRIMISA")?"PRIMITA":o.getStare()) + "   MAGAZIN: " + o.getMagazin() + "   TIMP_LIVRARE: " + o.getTimpLivrare() + "   OBIECTE: " + produse);
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
}
