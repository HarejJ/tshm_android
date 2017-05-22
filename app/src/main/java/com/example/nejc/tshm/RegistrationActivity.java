package com.example.nejc.tshm;

import java.security.MessageDigest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class RegistrationActivity extends AppCompatActivity {
    private RegistrationActivity registrationActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationActivity = this;

        Button registration =(Button) findViewById(R.id.RegistrationButton);
        registration.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.RegistrationButton:
                    registration(view);
                    break;
            }
        }
    };

    private void registration(View view) {
        String name = ((EditText) findViewById(R.id.Name)).getText().toString();
        String userName = ((EditText) findViewById(R.id.User)).getText().toString();
        String passwd1 = ((EditText) findViewById(R.id.Password1)).getText().toString();
        String passwd2 = ((EditText) findViewById(R.id.Password2)).getText().toString();
        String phone = ((EditText) findViewById(R.id.PhoneNumber)).getText().toString();
        String mail = ((EditText) findViewById(R.id.Email)).getText().toString();
        TextView error = (TextView) findViewById(R.id.Error);
        //izpolnjena vsa polja
        if(name.length() == 0 || userName.length() == 0 || passwd1.length() == 0
                ||passwd2.length() == 0 ||phone.length() == 0 || mail.length() == 0){
            error.setText("izpolnite vsa polja");
            return;
        }

        String passwd1MD5 = null;
        String passwd2MD5 = null;
        MD5 hash = new MD5();
        //šifriranje
        try {
            passwd1MD5 = hash.md5(passwd1);
            passwd2MD5 = hash.md5(passwd2);
        } catch (NoSuchAlgorithmException e) {
            error.setText("Težava pri šifriranju gesla");
            return;
        }
        // preverimo ali sta gesli enaki
        if(!passwd1MD5.equals(passwd2MD5)){
            error.setText("vnešeni gesli nista enaki");
            return;
        }
        RESTCallTask restTask = new RESTCallTask(registrationActivity, "register", name, userName,
                passwd1MD5, passwd2MD5, phone, mail,view);

        restTask.execute("POST", String.format("register"));
    }
}
