package com.example.nejc.tshm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button registration =(Button) findViewById(R.id.RegistrationButton);
        registration.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO: preveri ali so vsa polja vnešena
            //TODO: preveri ali sta oba gesla enaka
            //TODO: zašifriraj gesla
            //TODO: post request z json podatki
        }
    };
}
