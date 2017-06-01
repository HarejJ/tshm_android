package com.example.nejc.tshm;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
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
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String name,userName,passwd1,passwd2,phone,mail,passwd1MD5,passwd2MD5;
    private RegistrationActivity registrationActivity;
    private View view1;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationActivity = this;

        context = this;
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
        view1 = view;
        name = ((EditText) findViewById(R.id.Name)).getText().toString();
        userName = ((EditText) findViewById(R.id.User)).getText().toString();
        passwd1 = ((EditText) findViewById(R.id.Password1)).getText().toString();
        passwd2 = ((EditText) findViewById(R.id.Password2)).getText().toString();
        phone = ((EditText) findViewById(R.id.PhoneNumber)).getText().toString();
        mail = ((EditText) findViewById(R.id.Email)).getText().toString();
        TextView error = (TextView) findViewById(R.id.Error);
        //izpolnjena vsa polja
        if(name.length() == 0 || userName.length() == 0 || passwd1.length() == 0
                ||passwd2.length() == 0 ||phone.length() == 0 || mail.length() == 0){
            error.setText("izpolnite vsa polja");
            return;
        }

        passwd1MD5 = null;
        passwd2MD5 = null;
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
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            error.setText("e-mail nalov je neveljaven");
            return;
        }
        if(!validatedNumber(phone)){
            error.setText("telefonska stevilka je neveljavna");
            return;
        }
        if(NetworkUtils.isNetworkConnected(context))
            dispatchTakePictureIntent();
        else
            Dialog.networkErrorDialog(context).show();

    }
    private boolean validatedNumber(String phone){
        if(phone.charAt(0) == '+') {
            if(phone.length() != 12)
                return false;
            for (int i = 0; i<phone.length(); i++)
                if(!(phone.charAt(0) >= '0') && !(phone.charAt(0) <= '9'))
                    return false;
            return true;
        }
        else if(phone.charAt(0) >= '0' && phone.charAt(0) <= '9'){
            if(phone.length() != 9)
                return false;
            return true;

        }
        return false;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String image=ImageUtil.convert(imageBitmap);
            /*
            for (int i = 0; i<byteArray.length; i++)
                image+= i<byteArray.length? byteArray[i]+" " :byteArray[i];
            */
            RESTCallTask restTask = new RESTCallTask(registrationActivity, "register", name, userName,
                    passwd1MD5, passwd2MD5, phone, mail,view1,image);
            restTask.execute("POST", String.format("register"));

        }
    }
}
