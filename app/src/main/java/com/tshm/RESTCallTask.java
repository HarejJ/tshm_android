/*
    Created by Nejc on 6. 05. 2017.
*/
package com.tshm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.view.View;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


class RESTCallTask extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    private String username;
    private String password;
    private String password1;
    private String password2;
    private String activityName;
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private String image;
    private String idObleke;
    private String location;
    private String birthDay;
    private Activity activity;
    private View view;

    /*
        spremeni profilno sliko
     */
    RESTCallTask(Activity activity, String activityName, String username, String passwd,String image,View view) {
        this.activity = activity;
        this.activityName = activityName;
        this.username = username;
        this.password = passwd;
        this.image = image;
        this.view = view;
    }


    /*
     konstruktor za login določi username, geslo, activity(da lahko preidemo na drug)
     za logIn
     */
    RESTCallTask(Activity activity, String activityName, String username, String passwd, View view) {
        this.activity = activity;
        this.activityName = activityName;
        this.username = username;
        this.password = passwd;
        this.view = view;
    }

    /*
     konstruktor za login določi username, geslo, activity(da lahko preidemo na drug)
     za logIn
     */

    RESTCallTask(String activityName, String username, String password, String naslov,
                 String mail,String telefon,String telefon1, View view) {
        this.activityName = activityName;
        this.username = username;
        this.password = password;
        this.location = naslov;
        this.mail = mail;
        this.phone = telefon;
        this.view = view;
    }

    /*
     konstruktor za registracijo določi username, geslo,ime in priimek, telefonsko in mail activity(da lahko preidemo na drug)
     in podatke za avdentikacijo(za enkrat ne potrebujemo)
     */
    RESTCallTask(Activity activity, String activityName, String firstName,String lastName, String userName,
                 String passwd1, String passwd2, View view, String image) {
        this.activity = activity;
        this.activityName = activityName;
        this.view = view;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = userName;
        this.password = passwd1;
        this.password2 = passwd2;
        this.image = image;

    }

    //konstruktor detail
    RESTCallTask(String activityName, String username, String passwd, String dressID,String dressid, View view) {
        this.activityName = activityName;
        this.username = username;
        this.password = passwd;
        this.idObleke = dressID;
        this.view = view;
    }

    //konstruktor nastavi view
    RESTCallTask(String activityName, String username, String passwd, View view) {
        this.activityName = activityName;
        this.username = username;
        this.password = passwd;
        this.view = view;
    }

    //konstruktor nastavi view
    RESTCallTask(String activityName, String username, String passwd, String idObleke, View view) {
        this.activityName = activityName;
        this.username = username;
        this.password = passwd;
        this.view = view;
        this.idObleke = idObleke;
    }
    //konstruktor spremeni geslo
    RESTCallTask(String activityName, String username, String passwd, String password1,String password2,String password3, View view) {
        this.activityName = activityName;
        this.username = username;
        this.password = passwd;
        this.password1 = password1;
        this.password2 = password2;

        this.view = view;
    }

    protected String doInBackground(String... params) {
        String url = "http://tshm.herokuapp.com/%s";
        String method = "", parameter = "", callURL = "";//parametri ki jih potrebujemo za povezavo

        if (params.length > 0)
            method = params[0];//POST, GET

        if (params.length > 1)
            parameter = params[1];// del url naslova

        callURL = String.format(url, parameter);// https://tshm.herokuapp.com/(Login)
        Log.d("callURL", callURL);
        if (method.equals("POST"))//če je POST
            try {
                return POST(callURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    // če je method POST
    private String POST(String callURL) throws Exception {
        JSONObject jsonObject = new JSONObject();
        switch (activityName) {
            case "login":
                jsonObject = LoginJson();
                break;
            case "register":
                jsonObject = RegistrationJson();
                break;
            case "clothes":
                jsonObject = DefaultJson();
                break;
            case "reservation":
                jsonObject = ReservationJson();
                break;
            case "deleteReservation":
                jsonObject = ReservationJson();
                break;
            case "izposojena":
                jsonObject = ReservationJson();
                break;
            case "predajaNaprej":
                jsonObject = ReservationJson();
                break;
            case "oddana":
                jsonObject = ReservationJson();
                break;
            case "kontaktImetnika":
                jsonObject = ReservationJson();
                break;
            case "kontaktprejemnika":
                jsonObject = ReservationJson();
                break;
            case "changeImage":
                jsonObject = changeImageJson();
                break;
            case "dressDetail":
                jsonObject = dressDetailJson();
                break;
            case "dressDetail1":
                jsonObject = dressDetailJson();
                break;
            case "dodajPriljubljeno":
                jsonObject = ReservationJson();
                break;
            case "odstraniPriljubljeno":
                jsonObject = ReservationJson();
                break;
            case "priljubljeneObleke":
                jsonObject = priljubljeneJson();
                break;
            case "spremeniGeslo":
                jsonObject = spremeniGesloJson();
                break;
            case "spremeniPodatke":
                jsonObject = spremeniPodatkeJson();
                break;
        }
        String result = "";
        Log.d("",jsonObject.toString());
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(callURL);
            httpost.setEntity(new StringEntity(String.valueOf(jsonObject), "UTF-8"));

            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            //Handles what is returned from the page
            HttpResponse response = httpclient.execute(httpost);// rezultat ki ga vrne service
            HttpEntity httpEntity = response.getEntity();

            result = String.valueOf(response.getStatusLine().getStatusCode()) + "#"; //status code
            result += EntityUtils.toString(httpEntity);// rezultat v string

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;//vrne status_code@vrednost
    }

    protected void onPostExecute(String result) {
        switch (activityName) {
            case "login":
                try {
                    LogInRegistrationResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "register":
                try {
                    LogInRegistrationResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "clothes":
                try {
                    clothesResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "reservation":
                try {
                    reservationResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "deleteReservation":
                try {
                    deleteReservationResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "izposojena":
                try {
                    sprejemRezervacijeResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "predajaNaprej":
                try {
                    predajaNaprejResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "oddana":
                try {
                    oddajaRezervacijeResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "kontaktImetnika":
                try {
                    kontaktImetnika(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "kontaktprejemnika":
                try {
                    kontaktprejemnika(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "changeImage":
                try {
                    changeImageResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "dressDetail":
                try {
                    dressDetailResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "dressDetail1":
                try {
                    dressDetailResponse1(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "dodajPriljubljeno":
                try {
                    addFavoriteResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "odstraniPriljubljeno":
                try {
                    deleteFavoriteResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "priljubljeneObleke":
                try {
                    clothesResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "spremeniGeslo":
                try {
                    spremeniGesloResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "spremeniPodatke":
                try {
                    spremeniPodatkeResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /*
    naredi JSON objekt ki vsebuje uporabniško ime in geslo
    uporabi se v requestu za prijavo
    */
    private JSONObject LoginJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("Geslo", password);
        return jsonObject;
    }
    /*
    naredi JSON objekt ki vsebuje uporabniško ime in geslo
    uporabi se v requestu za prijavo
    */
    private JSONObject spremeniGesloJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("StaroGeslo", password);
        jsonObject.put("NovoGeslo1", password1);
        jsonObject.put("NovoGeslo2", password2);
        return jsonObject;
    }

    /*
    naredi JSON objekt ki vsebuje uporabniško ime in geslo
    uporabi se v requestu za prijavo
    */
    private JSONObject spremeniPodatkeJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("Geslo", password);
        jsonObject.put("Mail", mail);
        jsonObject.put("Stevilka", phone);
        jsonObject.put("Naslov", location);
        return jsonObject;
    }

    /*
    naredi JSON objekt ki vsebuje uporabniško ime geslo,....
    uporabi se v requestu za registracijo
    */
    private JSONObject RegistrationJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("Geslo1", password);
        jsonObject.put("Geslo2", password2);
        jsonObject.put("Ime", firstName);
        jsonObject.put("Priimek", lastName);
        jsonObject.put("Slika", image);
        return jsonObject;
    }

    // json za spremembo profilne
    private JSONObject dressDetailJson() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("Geslo", password);
        jsonObject.put("Obleka", idObleke);
        return jsonObject;
    }
    // json za spremembo profilne
    private JSONObject priljubljeneJson() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("Geslo", password);
        return jsonObject;
    }

    // json za spremembo profilne
    private JSONObject changeImageJson() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("Geslo", password);
        jsonObject.put("Slika", image);
        return jsonObject;
    }
    /*
   naredi JSON objekt ki vsebuje uporabniško ime geslo,....
   uporabi se v requestu za pridobitec oblek
   */
    private JSONObject DefaultJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("Geslo", password);
        return jsonObject;
    }

    /*
   naredi JSON objekt ki vsebuje uporabniško ime geslo,....
   uporabi se v requestu za rezervacijo
   */
    private JSONObject ReservationJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("Geslo", password);
        jsonObject.put("IdObleke", idObleke);
        return jsonObject;
    }

    /*
    glede na status obdela podatke(izpiše error ali zamenja activity)
     */
    private void LogInRegistrationResponse(String result) throws JSONException {
        String[] res = result.split("#");
        //textView za izpis errorja
        TextView error = (TextView) activity.findViewById(R.id.errorTV);
        error.setTextColor(activity.getResources().getColor(R.color.error));// rdeča barva texta
        //pretvorimo v json objekt
        JSONObject jsonRes = new JSONObject(res[1]);

        //če je code status >400 izpiši napako
        if (Integer.parseInt(res[0]) >= 400) {
            error.setText(jsonRes.getString("error"));
        }
        if (Integer.parseInt(res[0]) == 200) {
            error.setText("");

            User user = new User(jsonRes.getString("uporabniskoIme"), jsonRes.getString("Ime"),
                    password, jsonRes.getString("Mail"), jsonRes.getString("Telefonska"), 1, "", jsonRes.getString("Slika"));
            if (jsonRes.getString("id_obleka").compareTo("null") != 0) {
                Dress dress = new Dress(jsonRes.getString("id_obleka"), jsonRes.getString("slika_obleke"),
                        jsonRes.getString("tip"), jsonRes.getString("velikost"), jsonRes.getString("displayName"),
                        jsonRes.getString("slikaOblikovalca"), jsonRes.getString("trenutniIzposojevalec"),
                        jsonRes.getString("rezervacije"), "0",jsonRes.getString("datumIzposoje"),"","");
                user.setReservedDress(dress);

                user.setRezervacija(Boolean.parseBoolean(jsonRes.getString("rezervirana")));
                user.setPredaja(Boolean.parseBoolean(jsonRes.getString("predaja")));
                user.setIzposojena(Boolean.parseBoolean(jsonRes.getString("izposojena")));
                user.setPredajaNaprej(Boolean.parseBoolean(jsonRes.getString("predajaNaprej")));
                user.setVrnjena(Boolean.parseBoolean(jsonRes.getString("vrnjena")));
            }
            if(delegate != null)
                delegate.logIn(user);
            else{
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("User", (Serializable) user);
                this.view.getContext().startActivity(intent);
            }
        }
    }

    /*
    rezervacija obleke
     */
    private void reservationResponse(String result) throws JSONException {
        String[] res = result.split("#");
        boolean[] user = new boolean[5];
        if (Integer.parseInt(res[0]) == 200) {
            JSONObject jsonObject = new JSONObject(res[1]);
            Dress dress = new Dress(jsonObject.getString("id_obleka"), jsonObject.getString("slika"),
                    jsonObject.getString("tip"), jsonObject.getString("velikost"), jsonObject.getString("displayName"),
                     jsonObject.getString("slikaOblikovalca"),jsonObject.getString("trenutniIzposojevalec"),
                    jsonObject.getString("rezervacije"), "0", "0", "","");
            user[0] = (Boolean.parseBoolean(jsonObject.getString("rezervirana")));
            user[1] = (Boolean.parseBoolean(jsonObject.getString("predaja")));
            user[2] = (Boolean.parseBoolean(jsonObject.getString("izposojena")));
            user[3] = (Boolean.parseBoolean(jsonObject.getString("predajaNaprej")));
            user[4] = (Boolean.parseBoolean(jsonObject.getString("vrnjena")));
            delegate.clothesReserved(dress, user);
        }
        delegate.clothesNotReserved();
    }
    private void changeImageResponse(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {

        }
    }
    private void sprejemRezervacijeResponse(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.sprejemRezervacije();
        }
    }

    private void oddajaRezervacijeResponse(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.oddajaRezervacije();
        } else
            delegate.oddajaRezervacijeZavrnjena();


    }

    private void predajaNaprejResponse(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.predajaNaprej();
        }
    }

    private void deleteReservationResponse(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.deleteReservation();
        }
    }
    private void addFavoriteResponse(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.addFavorite();
        }
    }
    private void deleteFavoriteResponse(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.deleteFavorite();
        }
    }

    private void kontaktImetnika(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            JSONObject jsonObject = new JSONObject(res[1]);
            String[] user = new String[4];
            user[0] = (jsonObject.getString("uporabnik"));
            user[1] = (jsonObject.getString("mail"));
            user[2] = (jsonObject.getString("stevilka"));
            user[3] = (jsonObject.getString("lokacija"));
            delegate.kontaktImetnika(user);
        }
    }

    private void kontaktprejemnika(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            JSONObject jsonObject = new JSONObject(res[1]);
            String[] user = new String[4];
            user[0] = (jsonObject.getString("uporabnik"));
            user[1] = (jsonObject.getString("mail"));
            user[2] = (jsonObject.getString("stevilka"));
            user[3] = (jsonObject.getString("lokacija"));
            delegate.kontaktImetnika(user);
        }
    }
    private void dressDetailResponse (String result) throws JSONException {
        String[] res = result.split("#");
        JSONObject jsonObject = new JSONObject(res[1]);
        String[] dress = new String[9];
        if (Integer.parseInt(res[0]) == 200) {
            dress[0]=jsonObject.getString("tip");
            dress[1]=jsonObject.getString("velikost");
            dress[2]=jsonObject.getString("displayName");
            dress[3]=jsonObject.getString("slikaOblikovalca");
            dress[4]= jsonObject.getString("trenutniIzposojevalec");
            dress[5]=jsonObject.getString("rezervacije");
            dress[6]=jsonObject.getString("spol");
            dress[7]=jsonObject.getString("oznaka");
            dress[8] = jsonObject.getString("priljubljena");
            delegate.dressDetail(dress);
        }
    }
    private void dressDetailResponse1 (String result) throws JSONException {
        String[] res = result.split("#");
        JSONObject jsonObject = new JSONObject(res[1]);
        String[] dress = new String[9];
        if (Integer.parseInt(res[0]) == 200) {
            dress[0]=jsonObject.getString("tip");
            dress[1]=jsonObject.getString("velikost");
            dress[2]=jsonObject.getString("displayName");
            dress[3]=jsonObject.getString("slikaOblikovalca");
            dress[4]= jsonObject.getString("trenutniIzposojevalec");
            dress[5]=jsonObject.getString("rezervacije");
            dress[6]=jsonObject.getString("spol");
            dress[7]=jsonObject.getString("oznaka");
            dress[8] = jsonObject.getString("priljubljena");
            delegate.dressDetail1(dress);
        }
    }
    private void clothesResponse(String result) throws JSONException {
        ArrayList<Dress> clothes = new ArrayList<Dress>();
        String[] res = result.split("#");
        JSONArray jsonRes = new JSONArray(res[1]);
        if (Integer.parseInt(res[0]) == 200) {
            for (int i = 0; i < jsonRes.length(); i++) {
                JSONObject jsonObject = jsonRes.getJSONObject(i);
                Dress dress = new Dress(jsonObject.getString("id_obleka"), jsonObject.getString("slika"),
                        "","", "", "", "", "", "0", "0","","");
                clothes.add(dress);
            }
            delegate.processFinish(clothes);
        }
    }

    private void spremeniGesloResponse(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.spremeniGeslo();
        }
    }
    private void spremeniPodatkeResponse(String result) throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.spremeniPodatke();
        }
    }

}
