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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Toast;

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
    private EditText firstNameET,lastNameET,userNameET,passwd1ET,passwd2ET;
    private String passwd1MD5,passwd2MD5;
    private RegistrationActivity registrationActivity;
    private View view1;
    private TextView errorTV;
    private Context context;
    private Button dateBtn;
    boolean vnesena= true;
    private  static final int PICK_IMAGE = 100;

    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registrationActivity = this;

        lastNameET = ((EditText) findViewById(R.id.firstNameET));
        firstNameET = ((EditText) findViewById(R.id.lastNameET));
        userNameET = ((EditText) findViewById(R.id.userNameET));
        passwd1ET = ((EditText) findViewById(R.id.password1ET));
        passwd2ET = ((EditText) findViewById(R.id.password2ET));
        errorTV = (TextView) findViewById(R.id.errorTV);

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
        //izpolnjena vsa polja
        vnesena = true;
        if(firstNameET.length() == 0) {
            firstNameET.setError("Vnesite ime");
            vnesena = false;
        }
        if(lastNameET.length() == 0) {
            lastNameET.setError("Vnesite priimek");
            vnesena = false;
        }
        if(userNameET.length() == 0) {
            userNameET.setError("Vnesite uporabniško ime");
            vnesena = false;
        }
        if(passwd1ET.length() == 0) {
            passwd1ET.setError("Vnesite geslo");
            vnesena = false;
        }
        if(passwd2ET.length() == 0) {
            passwd2ET.setError("ponovno vnesite geslo");
            vnesena = false;
        }
        if(vnesena == false)
            return;

        passwd1MD5 = null;
        passwd2MD5 = null;
        MD5 hash = new MD5();
        //šifriranje
        try {
            passwd1MD5 = hash.md5(passwd1ET.getText().toString());
            passwd2MD5 = hash.md5(passwd2ET.getText().toString());
        } catch (NoSuchAlgorithmException e) {
            passwd2ET.setText("Težava pri šifriranju gesla");
            return;
        }
        // preverimo ali sta gesli enaki
        if(!passwd1MD5.equals(passwd2MD5)){
            passwd1ET.setError("vnešeni gesli nista enaki");
            passwd2ET.setError("vnešeni gesli nista enaki");
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
                int scale = 10;
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri), null, o2);

                String image=ImageUtil.convert(bitmap);

                String firstName =firstNameET.getText().toString();
                String lastName = lastNameET.getText().toString();
                String username = userNameET.getText().toString();
                RESTCallTask restTask = new RESTCallTask(registrationActivity, "register",
                        firstName,lastName,username, passwd1MD5, passwd2MD5,view1,image);
                CharSequence text = "shranjevanje podatkov. Prosimo počakajte";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

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

            String firstName =firstNameET.getText().toString();
            String lastName = lastNameET.getText().toString();
            String username = userNameET.getText().toString();
            RESTCallTask restTask = new RESTCallTask(registrationActivity, "register",
                    firstName,lastName,username, passwd1MD5, passwd2MD5,view1,image);
            CharSequence text = "shranjevanje podatkov. Prosimo počakajte";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            restTask.execute("POST", String.format("register"));

        }
    }

}


