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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.Serializable;


class RESTCallTask extends AsyncTask<String,Void,String>{
    private String username;
    private String password;
    private String password2;
    private String activityName;
    private String name;
    private String phone;
    private String mail;
    private String image;
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
                 String passwd1, String passwd2, String phone, String mail, View view,String image) {
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

            //TODO registracija, pridobi obleke, rezervacija,...
        }
        Log.d("jsonObject",jsonObject.toString());
        String result = "";
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(callURL);
            httpost.setEntity(new StringEntity(String.valueOf(jsonObject)));

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
        Log.d("result", result);
        JSONObject jsonRes = new JSONObject(res[1]);

        //če je code status >400 izpiši napako
        if (Integer.parseInt(res[0]) >= 400){
            Log.d("slika",image);
            error.setText(jsonRes.getString("error"));
        }
        if(Integer.parseInt(res[0]) == 200){
            error.setText("");
            Log.d("json",jsonRes.toString());
            /*String[] img = jsonRes.getString("Slika").split(" ");
            byte[] bytes = new byte[img.length];
            for (int j=0; j<img.length; j++)
                bytes[j]= Byte.parseByte(img[j]);
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            */
            User user = new User(jsonRes.getString("uporabniskoIme"),jsonRes.getString("Ime"),
                    password,jsonRes.getString("Mail"),jsonRes.getString("Telefonska"),1,"",jsonRes.getString("Slika"));

            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("User", (Serializable) user);
            this.view.getContext().startActivity(intent);
            //TODO dokončaj Backend da vrne izdelke
        }
    }
}
