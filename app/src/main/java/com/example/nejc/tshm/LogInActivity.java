package com.example.nejc.tshm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.dynamic.LifecycleDelegate;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.SSLSessionContext;

public class LogInActivity extends AppCompatActivity implements AsyncResponse{
    private LogInActivity logInActivity;
    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText usernameET, passwordET;
    private TextView errorTV;
    private CheckBox rememberMe;
    private AsyncResponse asyncResponse;
    private String passwdHash;
    private boolean rememberLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logInActivity = this;
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_log_in);
        context = this;
        asyncResponse =this;
        Button logIn = (Button) findViewById(R.id.LogInButton);
        TextView registration = (TextView) findViewById(R.id.RegistrationText);
        TextView forgotPasword = (TextView) findViewById(R.id.ForgotPassword);
        usernameET = (EditText) findViewById(R.id.UserName);
        passwordET = (EditText) findViewById(R.id.Password);
        errorTV = (TextView) findViewById(R.id.errorTV);
        rememberMe = (CheckBox) findViewById(R.id.RememberMe);
        rememberLogIn = rememberMe.isChecked();
        pref = getSharedPreferences("login.conf",Context.MODE_PRIVATE);
        editor = pref.edit();
        String userName = pref.getString("username","");
        String password = pref.getString("password","");

        if(!(userName.equals("") && userName.equals(""))){
            RESTCallTask restTask = new RESTCallTask(logInActivity, "login", userName, password,logInActivity.getCurrentFocus());
            restTask.delegate = asyncResponse;
            restTask.execute("POST", String.format("login"));
        }

        logIn.setOnClickListener(onClickListener);
        registration.setOnClickListener(onClickListener);
        forgotPasword.setOnClickListener(onClickListener);
        rememberMe.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.LogInButton:
                    if(NetworkUtils.isNetworkConnected(context))
                        Login(view);
                    else
                        Dialog.networkErrorDialog(context).show();
                    break;

                case  R.id.RegistrationText:
                    Intent intent = new Intent(logInActivity, RegistrationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ForgotPassword:
                    Dialog.pozabljenoGeslo(context).show();
                    break;
                case R.id.RememberMe:
                    rememberLogIn = rememberMe.isChecked();
            }
        }
    };
    private void Login(View view) {
        String username = (usernameET.getText().toString());
        String password = (passwordET.getText().toString());

        errorTV.setTextColor(getResources().getColor(R.color.error));
        if(username.length()==0 || password.length() == 0){
            errorTV.setText("Vnesite uporabniško ime in geslo");
            return;
        }
        passwdHash = "";
        errorTV.setText("");
        MD5 hash = new MD5();
        try {
            passwdHash = hash.md5(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        RESTCallTask restTask = new RESTCallTask(logInActivity, "login", username, passwdHash,view);
        restTask.delegate = asyncResponse;
        restTask.execute("POST", String.format("login"));
    }

    @Override
    public void processFinish(ArrayList<Dress> output) {

    }

    @Override
    public void clothesReserved(Dress output, boolean[] user) {

    }

    @Override
    public void deleteReservation() {

    }

    @Override
    public void sprejemRezervacije() {

    }

    @Override
    public void predajaNaprej() {

    }

    @Override
    public void oddajaRezervacije() {

    }

    @Override
    public void oddajaRezervacijeZavrnjena() {

    }

    @Override
    public void kontaktImetnika(String[] user) {

    }

    @Override
    public void clothesNotReserved() {

    }

    @Override
    public void dressDetail(String[] dressDeatil) {

    }

    @Override
    public void addFavorite() {

    }

    @Override
    public void deleteFavorite() {

    }

    @Override
    public void logIn(User user) {
        if(rememberLogIn){
            editor.putString("username",usernameET.getText().toString());
            editor.putString("password",passwdHash);
            editor.apply();
        }
        Intent intent = new Intent(logInActivity, MainActivity.class);
        intent.putExtra("User", (Serializable) user);
        startActivity(intent);
    }

    @Override
    public void spremeniGeslo() {

    }
}
