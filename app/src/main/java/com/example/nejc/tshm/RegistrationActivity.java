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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText name,userName,passwd1,passwd2,phone,mail,birthday,location;
    private  String passwd1MD5,passwd2MD5;
    private RegistrationActivity registrationActivity;
    private View view1;
    private TextView error;
    private Context context;
    private Date date;
    boolean vnesena= true;
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
        name = ((EditText) findViewById(R.id.Name));
        userName = ((EditText) findViewById(R.id.User));
        passwd1 = ((EditText) findViewById(R.id.Password1));
        passwd2 = ((EditText) findViewById(R.id.Password2));
        phone = ((EditText) findViewById(R.id.PhoneNumber));
        mail = ((EditText) findViewById(R.id.Email));
        error = (TextView) findViewById(R.id.Error);
        birthday = ((EditText) findViewById(R.id.Date));
        location = ((EditText) findViewById(R.id.Location));
        //izpolnjena vsa polja
        if(name.length() == 0) {
            name.setError("Vnesite ime in priimek");
            vnesena = false;
        }
        if(userName.length() == 0) {
            userName.setError("Vnesite uporabniško ime");
            vnesena = false;
        }
        if(passwd1.length() == 0) {
            passwd1.setError("Vnesite geslo");
            vnesena = false;
        }
        if(passwd2.length() == 0) {
            passwd2.setError("ponovno vnesite geslo");
            vnesena = false;
        }
        if(phone.length() == 0) {
            phone.setError("vnesite telefonsko številko");
            vnesena = false;
        }
        if(mail.length() == 0) {
            mail.setError("Vnesite e-mail");
            vnesena = false;
        }
        if(mail.length() == 0) {
            mail.setError("Vnesite e-mail");
            vnesena = false;
        }
        if(birthday.length() == 0) {
            birthday.setError("Vnesite datum rojstva");
            vnesena = false;
        }
        if(location.length() == 0) {
            mail.setError("Vnesite prebivališče");
            vnesena = false;
        }
        if(vnesena == false)
            return;
        passwd1MD5 = null;
        passwd2MD5 = null;
        MD5 hash = new MD5();
        //šifriranje
        try {
            passwd1MD5 = hash.md5(passwd1.getText().toString());
            passwd2MD5 = hash.md5(passwd2.getText().toString());
        } catch (NoSuchAlgorithmException e) {
            passwd2.setText("Težava pri šifriranju gesla");
            return;
        }
        // preverimo ali sta gesli enaki
        if(!passwd1MD5.equals(passwd2MD5)){
            passwd1.setError("vnešeni gesli nista enaki");
            passwd2.setError("vnešeni gesli nista enaki");
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()){
            error.setText("e-mail nalov je neveljaven");
            return;
        }
        if(!validatedNumber(phone.getText().toString())){
            error.setText("telefonska stevilka je neveljavna");
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        try {
            date = formatter.parse(birthday.getText().toString());
            
        } catch (ParseException e) {
            birthday.setError("vnesite veljaven datum rojstva");
            return;
        }
        if(NetworkUtils.isNetworkConnected(context))
            dispatchTakePictureIntent();
        else
            Dialog.networkErrorDialog(context).show();

    }

    //pravilnost vnešene telefonske številke
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

    //pravilnost vnešenega datuma
    private  boolean validateDate(String birthDate){
        String[] date = birthDate.split(".");
        if(Integer.parseInt(date[1]) > 12 || Integer.parseInt(date[1]) < 1)
            return false;
        if(Integer.parseInt(date[2]) <19)
            return false;
        return true;
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

            RESTCallTask restTask = new RESTCallTask(registrationActivity, "register", name.getText().toString(), userName.getText().toString(),
                    passwd1MD5, passwd2MD5, phone.getText().toString(), mail.getText().toString(),view1,image,location.getText().toString(),birthday.getText().toString());
            restTask.execute("POST", String.format("register"));

        }
    }
}
