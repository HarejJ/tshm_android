package com.example.nejc.tshm;
import java.io.Serializable;

class User implements Serializable{
    private String username;
    private String name;
    private String password;
    private String mail;
    private String phone;
    private int level;
    private String levelName;
    //konstruktor
    User(String username, String name,String password, String mail, String phone,
         int level, String levelName){

        this.username = username;
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.phone = phone;
        this.level = level;
        this.levelName = levelName;

    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public int getLevel() {
        return level;
    }

    public String getLevelName() {
        return levelName;
    }
}
