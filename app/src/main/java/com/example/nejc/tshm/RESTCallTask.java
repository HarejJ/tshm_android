/*
    Created by Nejc on 6. 05. 2017.
*/
package com.example.nejc.tshm;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.security.interfaces.DSAKey;
import java.util.ArrayList;


class RESTCallTask extends AsyncTask<String,Void,String>{
    public AsyncResponse delegate = null;
    private String username;
    private String password;
    private String password2;
    private String activityName;
    private String name;
    private String phone;
    private String mail;
    private String image;
    private String idObleke;
    private String location;
    private String birthDay;
    private Activity activity;
    private View view;



    /*
     konstruktor za login določi username, geslo, activity(da lahko preidemo na drug)
     za logIn
     */
     RESTCallTask(Activity activity, String activityName, String username, String passwd,View view) {
        this.activity = activity;
        this.activityName = activityName;
        this.username = username;
        this.password = passwd;
        this.view = view;
    }

    /*
     konstruktor za registracijo določi username, geslo,ime in priimek, telefonsko in mail activity(da lahko preidemo na drug)
     in podatke za avdentikacijo(za enkrat ne potrebujemo)
     */
    RESTCallTask(Activity activity, String activityName,  String name, String userName,
                 String passwd1, String passwd2, String phone, String mail, View view,String image,String location, String birthday) {
        this.activity = activity;
        this.activityName = activityName;
        this.view = view;
        this.name = name;
        this.username = userName;
        this.password = passwd1;
        this.password2 = passwd2;
        this.phone = phone;
        this.mail = mail;
        this.image = image;
        this.location = location;
        this.birthDay = birthday;

    }


    //konstruktor nastavi view
    RESTCallTask(String activityName, String username, String passwd,View view) {
        this.activityName = activityName;
        this.username = username;
        this.password = passwd;
        this.view = view;
    }
    //konstruktor nastavi view
    RESTCallTask(String activityName, String username, String passwd,String idObleke, View view) {
        this.activityName = activityName;
        this.username = username;
        this.password = passwd;
        this.view = view;
        this.idObleke = idObleke;
    }

    protected String doInBackground(String... params) {
        String url="http://tshm.herokuapp.com/%s";
        String method="", parameter="", callURL="";//parametri ki jih potrebujemo za povezavo

        if (params.length > 0)
            method = params[0];//POST, GET

        if (params.length > 1)
            parameter = params[1];// del url naslova

        callURL = String.format(url, parameter);// https://tshm.herokuapp.com/(Login)
        Log.d("callURL",callURL);
        if (method.equals("POST"))//če je POST
            try {
                return POST(callURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }
    // če je method POST
    private String POST(String callURL) throws Exception{
        JSONObject jsonObject = new JSONObject();
        switch (activityName) {
            case "login":
                jsonObject = LoginJson();
                break;
            case  "register":
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


        }
        Log.d("jsonObject",jsonObject.toString());
        String result = "";
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(callURL);
            httpost.setEntity(new StringEntity(String.valueOf(jsonObject),"UTF-8"));

            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            //Handles what is returned from the page
            HttpResponse response = httpclient.execute(httpost);// rezultat ki ga vrne service
            HttpEntity httpEntity = response.getEntity();

            result = String.valueOf(response.getStatusLine().getStatusCode())+"#";//status code
            result += EntityUtils.toString(httpEntity);// rezultat v string

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("result",result);
        return result;//vrne status_code@vrednost
    }
    protected void onPostExecute(String result) {
        switch (activityName) {
            case "login":
                try {LogInRegistrationResponse(result);}
                catch (JSONException e) {e.printStackTrace();}
                break;
            case "register":
                try {LogInRegistrationResponse(result);}
                catch (JSONException e) {e.printStackTrace();}
                break;
            case "clothes":
                try {clothesResponse(result);}
                catch (JSONException e) {e.printStackTrace();}
                break;
            case "reservation":
                try {
                    reservationResponse(result);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "deleteReservation":
                try {
                    deleteReservationResponse(result);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "izposojena":
                try {
                    sprejemRezervacijeResponse(result);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "predajaNaprej":
                try {
                    predajaNaprejResponse(result);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "oddana":
                try {
                    oddajaRezervacijeResponse(result);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case  "kontaktImetnika":
                try {
                    kontaktImetnika(result);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case  "kontaktprejemnika":
                try {
                    kontaktprejemnika(result);
                }
                catch (JSONException e) {
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
    naredi JSON objekt ki vsebuje uporabniško ime geslo,....
    uporabi se v requestu za registracijo
    */
    private JSONObject RegistrationJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("Geslo1", password);
        jsonObject.put("Geslo2", password2);
        jsonObject.put("Mail", mail);
        jsonObject.put("Telefonska", phone);
        jsonObject.put("Ime", name);
        jsonObject.put("Priimek", name);
        jsonObject.put("Slika",image);
        jsonObject.put("Lokacija",location);
        jsonObject.put("datumRojstva",birthDay);
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
        jsonObject.put("IdObleke",idObleke);
        return jsonObject;
    }

    /*
    glede na status obdela podatke(izpiše error ali zamenja activity)
     */
    private void LogInRegistrationResponse(String result) throws JSONException {
        String[] res = result.split("#");
        //textView za izpis errorja
        TextView error = (TextView) activity.findViewById(R.id.Error);
        error.setTextColor(activity.getResources().getColor(R.color.error));// rdeča barva texta
        //pretvorimo v json objekt
        JSONObject jsonRes = new JSONObject(res[1]);

        //če je code status >400 izpiši napako
        if (Integer.parseInt(res[0]) >= 400){
            error.setText(jsonRes.getString("error"));
        }
        if(Integer.parseInt(res[0]) == 200){
            error.setText("");
            Log.d("json",jsonRes.toString());

            User user = new User(jsonRes.getString("uporabniskoIme"),jsonRes.getString("Ime"),
                    password,jsonRes.getString("Mail"),jsonRes.getString("Telefonska"),1,"",jsonRes.getString("Slika"));
            if( jsonRes.getString("id_obleka").compareTo("null") !=0) {
                Dress dress = new Dress(jsonRes.getString("id_obleka"), jsonRes.getString("slika_obleke"),
                        jsonRes.getString("tip"), jsonRes.getString("velikost"), jsonRes.getString("displayName"),
                        jsonRes.getString("slikaOblikovalca"),jsonRes.getString("trenutniIzposojevalec"),
                        jsonRes.getString("rezervacije"));
                user.setReservedDress(dress);

                user.setRezervacija(Boolean.parseBoolean(jsonRes.getString("rezervirana")));
                user.setPredaja(Boolean.parseBoolean(jsonRes.getString("predaja")));
                user.setIzposojena(Boolean.parseBoolean(jsonRes.getString("izposojena")));
                user.setPredajaNaprej(Boolean.parseBoolean(jsonRes.getString("predajaNaprej")));
                user.setVrnjena(Boolean.parseBoolean(jsonRes.getString("vrnjena")));
            }


            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("User", (Serializable) user);
            this.view.getContext().startActivity(intent);
        }
    }

    /*
    rezervacija obleke
     */
    private  void reservationResponse(String result)throws JSONException {
        String[] res = result.split("#");
        boolean[]user = new boolean[5];
        if (Integer.parseInt(res[0]) == 200) {
            JSONObject jsonObject = new JSONObject(res[1]);
            Dress dress = new Dress(jsonObject.getString("id_obleka"), jsonObject.getString("slika"),
                    jsonObject.getString("tip"), jsonObject.getString("velikost"), jsonObject.getString("displayName"),
                    jsonObject.getString("trenutniIzposojevalec"), jsonObject.getString("slikaOblikovalca"),
                    jsonObject.getString("rezervacije"));
            user[0]=(Boolean.parseBoolean(jsonObject.getString("rezervirana")));
            user[1]=(Boolean.parseBoolean(jsonObject.getString("predaja")));
            user[2]=(Boolean.parseBoolean(jsonObject.getString("izposojena")));
            user[3]=(Boolean.parseBoolean(jsonObject.getString("predajaNaprej")));
            user[4]=(Boolean.parseBoolean(jsonObject.getString("vrnjena")));
            delegate.clothesReserved(dress, user);
        }
        delegate.clothesNotReserved();
    }

    private  void sprejemRezervacijeResponse(String result)throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.sprejemRezervacije();
        }
    }
    private  void oddajaRezervacijeResponse(String result)throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.oddajaRezervacije();
        }else
            delegate.oddajaRezervacijeZavrnjena();


    }
    private  void predajaNaprejResponse(String result)throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            delegate.predajaNaprej();
        }
    }
    private  void deleteReservationResponse(String result)throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {

            delegate.deleteReservation();
        }
    }

    private  void kontaktImetnika(String result)throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            JSONObject jsonObject = new JSONObject(res[1]);
            String[]user = new String[4];
            user[0]=(jsonObject.getString("uporabnik"));
            user[1]=(jsonObject.getString("mail"));
            user[2]=(jsonObject.getString("stevilka"));
            user[3]=(jsonObject.getString("lokacija"));
            delegate.kontaktImetnika(user);
        }
    }

    private  void kontaktprejemnika(String result)throws JSONException {
        String[] res = result.split("#");
        if (Integer.parseInt(res[0]) == 200) {
            JSONObject jsonObject = new JSONObject(res[1]);
            String[]user = new String[4];
            user[0]=(jsonObject.getString("uporabnik"));
            user[1]=(jsonObject.getString("mail"));
            user[2]=(jsonObject.getString("stevilka"));
            user[3]=(jsonObject.getString("lokacija"));
            delegate.kontaktImetnika(user);
        }
    }
    private void clothesResponse(String result) throws  JSONException{
        ArrayList<Dress> clothes = new ArrayList<Dress>();
        String[] res = result.split("#");
        JSONArray jsonRes = new JSONArray(res[1]);
        if(Integer.parseInt(res[0]) == 200) {
            for(int i = 0; i<jsonRes.length(); i++){
                JSONObject jsonObject = jsonRes.getJSONObject(i);
                Dress dress = new Dress(jsonObject.getString("id_obleka"),jsonObject.getString("slika"),
                        jsonObject.getString("tip"),jsonObject.getString("velikost"),jsonObject.getString("displayName"),
                        jsonObject.getString("slikaOblikovalca"),jsonObject.getString("trenutniIzposojevalec"),
                        jsonObject.getString("rezervacije"));
                clothes.add(dress);
            }
            delegate.processFinish(clothes);
        }
    }
}
