package com.example.nejc.tshm;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

class User implements Serializable{
    private String username;
    private String name;
    private String password;
    private String mail;
    private String phone;
    private int level;
    private String levelName;
    private String  image;
    private boolean rezervacija;
    private boolean predaja;
    private boolean sprejem;
    private boolean izposojena;
    private boolean predajaNaprej;
    private boolean vrnjena;
    private Dress reservedDress;

    public ArrayList<Dress> getFavoriteDress() {
        return favoriteDress;
    }
    public void addFavoriteDress(Dress favoriteDress) {
        this.favoriteDress.add(favoriteDress);
    }
    public void deleteFavoriteDress(Dress dress){
        for (int i = 0; i<favoriteDress.size(); i++){
            if(favoriteDress.get(i).getId_obleka() == dress.getId_obleka()){
                favoriteDress.remove(i);
                break;
            }
        }
    }

    private ArrayList<Dress> favoriteDress;
    //konstruktor
    User(String username, String name,String password, String mail, String phone,
         int level, String levelName, String image){
        favoriteDress = new ArrayList<Dress>();
        this.username = username;
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.phone = phone;
        this.level = level;
        this.levelName = levelName;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isRezervacija() {
        return rezervacija;
    }

    public void setRezervacija(boolean rezervacija) {
        this.rezervacija = rezervacija;
    }

    public boolean isPredaja() {
        return predaja;
    }

    public void setPredaja(boolean predaja) {
        this.predaja = predaja;
    }

    public boolean isSprejem() {
        return sprejem;
    }

    public void setSprejem(boolean sprejem) {
        this.sprejem = sprejem;
    }

    public boolean isIzposojena() {
        return izposojena;
    }

    public void setIzposojena(boolean izposojena) {
        this.izposojena = izposojena;
    }

    public boolean isPredajaNaprej() {
        return predajaNaprej;
    }

    public void setPredajaNaprej(boolean predajaNaprej) {
        this.predajaNaprej = predajaNaprej;
    }

    public boolean isVrnjena() {
        return vrnjena;
    }

    public void setVrnjena(boolean vrnjena) {
        this.vrnjena = vrnjena;
    }

    public Dress getReservedDress() {
        return reservedDress;
    }

    public void setReservedDress(Dress reservedDress) {
        this.reservedDress = reservedDress;
    }
}
