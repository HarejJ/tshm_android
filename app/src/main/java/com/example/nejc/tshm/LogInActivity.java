package com.example.nejc.tshm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LogInActivity extends AppCompatActivity {
    LogInActivity logInActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logInActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Button logIn = (Button) findViewById(R.id.LogInButton);
        TextView registration = (TextView) findViewById(R.id.RegistrationText);
        TextView forgotPasword = (TextView) findViewById(R.id.ForgotPassword);

        logIn.setOnClickListener(onClickListener);
        registration.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.LogInButton:
                    Login(view);
                    break;
                case  R.id.RegistrationText:
                    Intent intent = new Intent(logInActivity, RegistrationActivity.class);
                    startActivity(intent);
            }
        }
    };
    private void Login(View view) {
        String username = ((EditText)findViewById(R.id.UserName)).getText().toString();
        String password = (((EditText)findViewById(R.id.Password)).getText().toString());
        TextView error = (TextView) findViewById(R.id.Error);
        error.setTextColor(getResources().getColor(R.color.error));
        if(username.length()==0 || password.length() == 0){
            error.setText("Vnesite uporabniško ime in geslo");
            return;
        }
        String passwdHash = "";
        error.setText("");
        MD5 hash = new MD5();
        try {
            Log.d("geslo",password);
            passwdHash = hash.md5(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Log.d("1","pride");
        RESTCallTask restTask = new RESTCallTask(logInActivity, "login", username, passwdHash,view);
        Log.d("2","pride");
        restTask.execute("POST", String.format("login"));
    }
}
