package com.example.nejc.tshm;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dress implements Serializable {
    private String id_obleka;
    private String slika;
    private String tip;
    private String velikost;
    private String spol;
    private String oblikovalec;
    private String slikaOblikovalca;
    private String[] oznake;
    private String trenutniImetnik;
    private int cakalnaVrsta;
    private int steviloDni;
    private boolean priljubljena;
    private String oznaka;

    Dress(String id_obleka, String slika, String tip, String velikost, String Oblikovalec,
          String slikaOblikovalca, String trenutniIzposojevalec, String rezervacije, String priljubljena,
          String datumIzposoje,String spol, String oznaka) {
        this.id_obleka = id_obleka;
        this.slika = slika;
        this.tip = tip;
        this.velikost = velikost;
        this.oblikovalec = Oblikovalec;
        this.trenutniImetnik = trenutniIzposojevalec;
        this.spol = spol;
        this.slikaOblikovalca = slikaOblikovalca;
        this.steviloDni = calculateDays(datumIzposoje);
        this.cakalnaVrsta = Integer.parseInt(rezervacije);
        this.priljubljena = priljubljena.equals("1");
        this.oznaka = oznaka;
    }
    private int calculateDays(String datumIzposoje) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date;
        Date dateNow;
        try {
            date = dateFormat.parse(datumIzposoje);
            dateNow = new Date();
            long diff = Math.abs(dateNow.getTime() - date.getTime());
            long diffDays = (diff / (24 * 60 * 60 * 1000));
            return (int) diffDays;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getId_obleka() {
        return id_obleka;
    }

    public String getSlika() {
        return slika;
    }

    public String getOznaka() {
        return oznaka;
    }

    public String getTip() {
        return tip;
    }

    public String getVelikost() {
        return velikost.compareTo("null") == 0 ? "unisex" : velikost;
    }


    public String getSpol() {

        return spol.compareTo("null") == 0 ? "unisex" : spol;
    }

    public String getStDni() {

        return String.valueOf(7 - steviloDni);
    }

    public String getOblikovalec() {
        return oblikovalec;
    }

    public String getTrenutniImetnik() {
        return trenutniImetnik.compareTo("null") == 0 ? "" : trenutniImetnik;
    }

    public int getCakalnaVrsta() {
        return cakalnaVrsta;
    }

    public String getSlikaOblikovalca() {
        return slikaOblikovalca;
    }

    public String[] getOznake() { return oznake; }

    public boolean isPriljubljena() { return priljubljena; }

    public void setOznake(String[] oznake) {
        this.oznake = oznake;
    }

    public void setPriljubljena(boolean priljubljena) { this.priljubljena = priljubljena; }

    public void setCakalnaVrsta(int cakalnaVrsta) {
        this.cakalnaVrsta = cakalnaVrsta;
    }
}
