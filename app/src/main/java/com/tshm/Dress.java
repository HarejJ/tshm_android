package com.tshm;

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
        this.cakalnaVrsta = 0;
        this.priljubljena = priljubljena.equals("1");
        this.oznaka = oznaka;
        if(rezervacije.length()>0)
            this.cakalnaVrsta= Integer.parseInt(rezervacije);
    }
    private int calculateDays(String datumIzposoje) {
        if(datumIzposoje.length()<3 || datumIzposoje.equals("null"))
            return 0;
        DateFormat dateFormat = new SimpleDateFormat("dd.MMM.yyyy");
        Date date;
        Date dateNow;
        int diffDays =0;
        try {
            String[] s = datumIzposoje.split(" ");
            datumIzposoje=s[1]+"."+s[2]+"."+s[3];
            date = dateFormat.parse(datumIzposoje);
            dateNow = new Date();
            long diff = Math.abs(dateNow.getTime() - date.getTime());
            diffDays = (int) (diff / (24 * 60 * 60 * 1000));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diffDays;
    }

    public String getId_obleka() {
        return id_obleka;
    }

    public String getSlika() {
        return slika;
    }

    public void setId_obleka(String id_obleka) {
        this.id_obleka = id_obleka;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setVelikost(String velikost) {
        this.velikost = velikost;
    }

    public void setSpol(String spol) {
        this.spol = spol;
    }

    public void setOblikovalec(String oblikovalec) {
        this.oblikovalec = oblikovalec;
    }

    public void setSlikaOblikovalca(String slikaOblikovalca) {
        this.slikaOblikovalca = slikaOblikovalca;
    }

    public void setTrenutniImetnik(String trenutniImetnik) {
        this.trenutniImetnik = trenutniImetnik;
    }

    public void setSteviloDni(int steviloDni) {
        this.steviloDni = steviloDni;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
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

        return String.valueOf(14 - steviloDni);
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


    public boolean isPriljubljena() { return priljubljena; }


    public void setPriljubljena(boolean priljubljena) { this.priljubljena = priljubljena; }

    public void setCakalnaVrsta(int cakalnaVrsta) {
        this.cakalnaVrsta = cakalnaVrsta;
    }
}
