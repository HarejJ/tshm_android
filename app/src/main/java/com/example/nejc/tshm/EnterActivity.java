package com.example.nejc.tshm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        Button enter = (Button) findViewById(R.id.EnterButton);
        Button registration = (Button) findViewById(R.id.EnterRegistrationButton);


        enter.setOnClickListener(enter_onClickListener);
        registration.setOnClickListener(registration_onClickListener);
    }

    private View.OnClickListener enter_onClickListener = new View.OnClickListener() {
        //ob kliku na gumb "VSTOPI" odpre logIn okno
        @Override
        public void onClick(View v) {
            Log.d("view", String.valueOf(v.getId()));
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    private View.OnClickListener registration_onClickListener = new View.OnClickListener() {
        //ob kliku na gumb "REQISTRACIJA" odpre reqistration okno
        @Override
        public void onClick(View v) {
            Log.d("view", String.valueOf(v.getId()));
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

}
