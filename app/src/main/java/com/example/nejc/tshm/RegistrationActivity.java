package com.example.nejc.tshm;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RegistrationActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText name,userName,passwd1,passwd2,phone,mail,birthday,location;
    private String passwd1MD5,passwd2MD5;
    private RegistrationActivity registrationActivity;
    private View view1;
    private TextView error;
    private Context context;
    private Button dateBtn;
    boolean vnesena= true;
    private  static final int PICK_IMAGE = 100;

    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registrationActivity = this;

        name = ((EditText) findViewById(R.id.Name));
        userName = ((EditText) findViewById(R.id.User));
        passwd1 = ((EditText) findViewById(R.id.Password1));
        passwd2 = ((EditText) findViewById(R.id.Password2));
        phone = ((EditText) findViewById(R.id.PhoneNumber));
        mail = ((EditText) findViewById(R.id.Email));
        error = (TextView) findViewById(R.id.Error);
        birthday = ((EditText) findViewById(R.id.Date));
        location = ((EditText) findViewById(R.id.Location));

        context = this;
        Button registration =(Button) findViewById(R.id.RegistrationButton);
        registration.setOnClickListener(onClickListener);

        mDisplayDate = (EditText) findViewById(R.id.Date);
        dateBtn = (Button) findViewById(R.id.DateBtn);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegistrationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "." + month + "." + year;
                mDisplayDate.setText(date);
            }
        };

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
            location.setError("Vnesite prebivališče");
            vnesena = false;
        }
        if(vnesena == false)
            return;
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date;
        Date dateNow;
        try {
            date = dateFormat.parse(birthday.getText().toString());
            dateNow = new Date();

            if(!checkAge(date,dateNow)){
                Dialog.starostPremajhna(context).show();
                return;
            }
        } catch (ParseException e) {
        }

        if(birthday.getText().toString() =="")

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
        Dialog.pogojiSodelovanja(context, registrationActivity).show();
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

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    public void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);

    }


    private boolean checkAge(Date age, Date dateNow){
        long diff = Math.abs(dateNow.getTime() - age.getTime());
        long diffDays = (diff / (24 * 60 * 60 * 1000))/365;
        if(diffDays >= 16)
            return true;
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Uri imageUri = data.getData();
            try {
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    getIntent().setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    getIntent().setAction(Intent.ACTION_OPEN_DOCUMENT);
                    getIntent().addCategory(Intent.CATEGORY_OPENABLE);
                }
                int scale = 20;
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri), null, o2);

                String image=ImageUtil.convert(bitmap);

                RESTCallTask restTask = new RESTCallTask(registrationActivity, "register", name.getText().toString(), userName.getText().toString(),
                        passwd1MD5, passwd2MD5, phone.getText().toString(), mail.getText().toString(),view1,image,location.getText().toString(),birthday.getText().toString());
                restTask.execute("POST", String.format("register"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            String image=ImageUtil.convert(imageBitmap);

            RESTCallTask restTask = new RESTCallTask(registrationActivity, "register", name.getText().toString(), userName.getText().toString(),
                    passwd1MD5, passwd2MD5, phone.getText().toString(), mail.getText().toString(),view1,image,location.getText().toString(),birthday.getText().toString());
            restTask.execute("POST", String.format("register"));

        }
    }

}


