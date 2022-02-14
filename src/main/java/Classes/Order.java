package Classes;

import java.util.ArrayList;

public class Order {
    private Long id;
    private String username_client;
    private String judet_client;
    private String localitate_client;
    private String strada_client;
    private int numar_client;
    private int telefon_client;
    private float cost;
    private String stare;
    private String username_magazin;
    private ArrayList<AllProducts> produse;
    private String timp_livrare;
    private String tip_comanda;

    public Order(Long id, String username_client, String judet_client, String localitate_client, String strada_client, int numar_client, int telefon_client, float cost,
                 String stare, String username_magazin, ArrayList<AllProducts> produse, String timp_livrare, String tip_comanda) {
        this.id = id;
        this.username_client = username_client;
        this.judet_client = judet_client;
        this.localitate_client = localitate_client;
        this.strada_client = strada_client;
        this.numar_client = numar_client;
        this.telefon_client = telefon_client;
        this.cost = cost;
        this.stare = stare;
        this.username_magazin = username_magazin;
        this.produse = produse;
        this.timp_livrare = timp_livrare;
        this.tip_comanda = tip_comanda;
    }

    public long getId() {
        return id;
    }

    public String getUsername_client() {
        return username_client;
    }

    public String getJudet_client() {
        return judet_client;
    }

    public String getLocalitate_client() {
        return localitate_client;
    }

    public String getStrada_client() {
        return strada_client;
    }

    public int getNumar_client() {
        return numar_client;
    }

    public int getTelefon_client() {
        return telefon_client;
    }

    public float getCost() {
        return cost;
    }

    public String getStare() {
        return stare;
    }

    public String getUsername_magazin() {
        return username_magazin;
    }

    public ArrayList<AllProducts> getProduse() {
        return produse;
    }

    public String getTimp_livrare() {
        return timp_livrare;
    }

    public String getTip_comanda() {
        return tip_comanda;
    }

    public void setProduse(ArrayList<AllProducts> produse) {
        this.produse = produse;
    }

    public String toStringClient() {
        String s = "";/*
        for (AllProducts p : produse)
            if (s.equals(""))
                s = s + p.getDenumire();
            else
                s = s + ", " + p.getDenumire();*/
        return "ID: " + id + " VANZATOR: " + username_magazin + " COST: " + cost + " STARE: " + stare + " TIMP_LIVRARE: " + timp_livrare + " PRODUSE: " + s;
    }

    public String toStringMagazin() {
        String s = "";/*
        for (AllProducts p : produse)
            if (s.equals(""))
                s = s + p.getDenumire();
            else
                s = s + ", " + p.getDenumire();*/
        return "ID: " + id + " CUMPARATOR: " + username_client + " COST: " + cost + " STARE: " + (stare.equals("trimisa") ? "primita" : stare) + " TIMP_LIVRARE: " + timp_livrare + " PRODUSE: " + s;
    }

    public void setStare(String stare) {
        this.stare = stare;
    }

    public void setTimp_livrare(String timp_livrare) {
        this.timp_livrare = timp_livrare;
    }
}
