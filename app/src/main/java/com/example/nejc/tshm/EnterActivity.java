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

public class EnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        Button enter = (Button) findViewById(R.id.EnterButton);
        enter.setOnClickListener(onClickListener);

        ImageView logo= (ImageView) findViewById(R.id.imageView2);
        //logo.bringToFront();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        //ob kliku na gumb "VSTOPI" odpre logIn okno
        @Override
        public void onClick(View v) {
            Log.d("view", String.valueOf(v.getId()));
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

}
