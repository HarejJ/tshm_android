/*
    Created by Nejc on 6. 05. 2017.
*/
package com.example.nejc.tshm;
import android.app.Activity;
import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.view.View;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;



class RESTCallTask extends AsyncTask<String,Void,String>{
    private String username;
    private String password;
    private String activityName;
    private Activity activity;
    //private View view;
    //private String auth; // authentication username:geslo

    /*
     konstruktor za login določi username, geslo, activity(da lahko preidemo na drug)
     in podatke za avdentikacijo(za enkrat ne potrebujemo)
     */
    RESTCallTask(Activity activity,View view, String activityName, String username, String passwd) {
        this.activity = activity;
        this.activityName = activityName;
        this.username = username;
        this.password = passwd;
        //this.auth = String.format("%s:%s", username, passwd);
        //this.view=view;
    }

    protected String doInBackground(String... params) {
        String url="http://tshm.herokuapp.com/%s";
        String method="", parameter="", callURL="";//parametri ki jih potrebujemo za povezavo
        boolean authorization = false;//nastavimo avdentikacijo na false

        if (params.length > 0)
            method = params[0];//POST, GET

        if (params.length > 1)
            parameter = params[1];// del url naslova

        callURL = String.format(url, parameter);// https://tshm.herokuapp.com/(Login)

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
            //TODO registracija, pridobi obleke, rezervacija,...
        }
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
        return result;//vrne status_code@vrednost
    }
    protected void onPostExecute(String result) {
        switch (activityName) {
            case "login":
                try {
                    LogInResponse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /*
    naredi JSON objekt ki vsebuje uporabniško ime in geslo
    uporabi se v
    */
    private JSONObject LoginJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UporabniskoIme", username);
        jsonObject.put("Geslo", password);
        return jsonObject;
    }
    /*
    glede na status obdela podatke(izpiše error ali zamenja activity)
     */
    private void LogInResponse(String result) throws JSONException {
        String[] res = result.split("#");
        //textView za izpis errorja
        TextView error = (TextView) activity.findViewById(R.id.Error);
        error.setTextColor(activity.getResources().getColor(R.color.error));// rdeča barva texta
        //pretvorimo v json objekt
        JSONObject json = new JSONObject(res[1]);
        //če je code status >400 izpiši napako
        if (Integer.parseInt(res[0]) >= 400){
            error.setText(json.getString("error"));
        }
        if(Integer.parseInt(res[0]) == 200){
            error.setText("");
            //TODO zamenjaj na mainActivity dokončati
            //TODO dokončaj Backend da vrne izdelke
        }
    }
}
