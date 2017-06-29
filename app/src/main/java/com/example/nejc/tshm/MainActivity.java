package com.example.nejc.tshm;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AsyncResponse{
    ImageView changeProfilImage;
    NavigationView navigationView = null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    Toolbar toolbar = null;
    User user = null;
    String newImage;
    String passwd1MD5;
    Context context;
    View view;
    AsyncResponse asyncResponse;
    EditText staroGesloET,novoGeslo1ET,novoGeslo2ET,mailET,naslovET,telefonET;
    AlertDialog spremeniGeslo;
    AlertDialog dopolniPodatke;
    CircleImageView profilImage;
    MainActivity mainActivity;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private  static final int PICK_IMAGE = 100;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mainActivity =this;
        Intent i = getIntent();


        pref = getSharedPreferences("login.conf",Context.MODE_PRIVATE);
        editor = pref.edit();
        asyncResponse = this;
        user = (User) i.getSerializableExtra("User");
        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);
        //Set the fragment initially
        final MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(args);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mainFragment).addToBackStack(null);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // nastavi uporabniško ime v glavi menuja
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.userMenu);
        nav_user.setText(user.getName());
        //nastavi sliko
        profilImage = (CircleImageView) hView.findViewById(R.id.profile_image);
        profilImage.setImageBitmap(ImageUtil.convert(user.getImage()));

        changeProfilImage = (ImageView) hView.findViewById(R.id.changeProfilImage);

        changeProfilImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
                PopupMenu popup = new PopupMenu(MainActivity.this, changeProfilImage);
                popup.getMenuInflater().inflate(R.menu.poupup_menu_image, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if(id == R.id.spremeniSliko){
                            if(NetworkUtils.isNetworkConnected(context))
                                Dialog.vnesiSliko(context,mainActivity).show();
                            else
                                Dialog.networkErrorDialog(context).show();
                        }
                        else if(id == R.id.spremeniGeslo){
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.dailog_change_password,null);
                            staroGesloET = (EditText) mView.findViewById(R.id.staroGesloET);
                            novoGeslo1ET = (EditText) mView.findViewById(R.id.novoGesloET1);
                            novoGeslo2ET = (EditText) mView.findViewById(R.id.novoGesloET2);
                            Button mButtonPotrdi = (Button) mView.findViewById(R.id.potrdiBTN);
                            Button mButtonPreklici = (Button) mView.findViewById(R.id.prekliciBTN);
                            mBuilder.setView(mView);
                            spremeniGeslo =mBuilder.create();
                            spremeniGeslo.show();
                            mButtonPotrdi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    boolean vneseniPodatki = true;
                                    if(staroGesloET.length() == 0){
                                        staroGesloET.setError("Vnesite trenutno geslo");
                                        vneseniPodatki =false;
                                    }
                                    if(novoGeslo1ET.length() == 0){
                                        novoGeslo1ET.setError("Vnesite novo geslo");
                                        vneseniPodatki =false;
                                    }
                                    if(novoGeslo2ET.length() == 0){
                                        novoGeslo2ET.setError("Vnesite novo geslo");
                                        vneseniPodatki =false;
                                    }
                                    if(!vneseniPodatki)
                                        return;
                                    String staroGeslo = staroGesloET.getText().toString();
                                    String novoGeslo1 = novoGeslo1ET.getText().toString();
                                    String novoGeslo2 = novoGeslo2ET.getText().toString();

                                    String passwdMD5 = null;
                                    passwd1MD5 = null;
                                    String passwd2MD5 = null;
                                    MD5 hash = new MD5();
                                    //šifriranje
                                    try {
                                        passwdMD5 = hash.md5(staroGeslo);
                                        passwd1MD5 = hash.md5(novoGeslo1);
                                        passwd2MD5 = hash.md5(novoGeslo2);
                                    } catch (NoSuchAlgorithmException e) {
                                        novoGeslo2ET.setText("Težava pri šifriranju gesla");
                                        return;
                                    }
                                    if(passwdMD5.compareTo(user.getPassword())!= 0){
                                        staroGesloET.setError("trenutno geslo ni pravilno");
                                        return;
                                    }
                                    if(passwd1MD5.compareTo(passwd2MD5)!= 0){
                                        novoGeslo2ET.setError("Novi gesli nista enaki");
                                        return;
                                    }

                                    RESTCallTask restTask = new RESTCallTask("spremeniGeslo", user.getUsername(),
                                            passwdMD5,passwd1MD5,passwd2MD5,passwd2MD5, view);
                                    restTask.delegate = asyncResponse;
                                    restTask.execute("POST", String.format("spremeniGeslo"));

                                }
                            });
                            mButtonPreklici.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    spremeniGeslo.dismiss();
                                }
                            });
                        }
                        else if(id == R.id.dopolniProfil){
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.dialog_add_user_data,null);
                            telefonET = (EditText) mView.findViewById(R.id.telET);
                            mailET = (EditText) mView.findViewById(R.id.mailET);
                            naslovET = (EditText) mView.findViewById(R.id.nasET);
                            mBuilder.setView(mView);
                            dopolniPodatke =mBuilder.create();
                            dopolniPodatke.show();
                            Button mButtonPotrdi = (Button) mView.findViewById(R.id.PotrdiBtn);
                            Button mButtonPreklici = (Button) mView.findViewById(R.id.PrekliciBtn);

                            mButtonPreklici.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dopolniPodatke.dismiss();
                                }
                            });
                            mButtonPotrdi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    boolean vneseniPodatki = true;
                                    if(telefonET.length() == 0){
                                        telefonET.setError("Vnesite telefonsko številko");
                                        vneseniPodatki =false;
                                    }
                                    if(mailET.length() == 0){
                                        mailET.setError("Vnesite elektronsko pošto");
                                        vneseniPodatki =false;
                                    }
                                    if(naslovET.length() == 0){
                                        naslovET.setError("Vnesite naslov");
                                        vneseniPodatki =false;
                                    }
                                    if(!vneseniPodatki)
                                        return;
                                    String naslov = naslovET.getText().toString();
                                    String mail = mailET.getText().toString();
                                    String telefon = telefonET.getText().toString();
                                    if(!validatedNumber(telefon)){
                                        telefonET.setError("Telefonska številka je nepravilna");
                                        return;
                                    }
                                    if(!emailValidator(mail)){
                                        mailET.setError("e-mail je nepraviln");
                                        return;
                                    }
                                    RESTCallTask restTask = new RESTCallTask("spremeniPodatke", user.getUsername(),
                                            user.getPassword(),naslov,mail,telefon,telefon, view);
                                    restTask.delegate = asyncResponse;
                                    restTask.execute("POST", String.format("spremeniPodatke"));

                                }
                            });
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getFragmentManager().popBackStack();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Bundle args = new Bundle();
            args.putSerializable("user", (Serializable) user);

            MainFragment fragment = new MainFragment();
            fragment.setArguments(args);

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_user) {
            Bundle args = new Bundle();
            args.putSerializable("user", (Serializable) user);

            UserProfileFragment fragment = new UserProfileFragment();
            fragment.setArguments(args);

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_gallery) {
            Bundle args = new Bundle();
            args.putSerializable("user", (Serializable) user);

            GalleryFragment fragment = new GalleryFragment();
            fragment.setArguments(args);

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_about) {
            AboutProjectFragment fragment = new AboutProjectFragment();

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_care) {
            CareOfClothes fragment = new CareOfClothes();

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_help) {
            HelpFragment fragment = new HelpFragment();

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_about_aplication) {
            AboutAPPFragment fragment = new AboutAPPFragment();

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_logout) {
            editor = pref.edit();
            editor.clear();
            editor.commit();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                newImage=ImageUtil.convert(bitmap);
                user.setImage(newImage);
                profilImage.setImageBitmap(ImageUtil.convert(newImage));
                RESTCallTask restTask = new RESTCallTask(mainActivity, "changeImage", user.getUsername(),
                        user.getPassword(),newImage, view);
                restTask.execute("POST", String.format("changeImage"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            newImage=ImageUtil.convert(imageBitmap);
            user.setImage(newImage);
            profilImage.setImageBitmap(ImageUtil.convert(newImage));
            RESTCallTask restTask = new RESTCallTask(mainActivity, "changeImage", user.getUsername(),
                    user.getPassword(),newImage,view);
            restTask.execute("POST", String.format("changeImage"));

        }
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
    public void dressDetail1(String[] dressDeatil) {

    }

    @Override
    public void addFavorite() {

    }

    @Override
    public void deleteFavorite() {

    }

    @Override
    public void logIn(User user) {

    }

    @Override
    public void spremeniGeslo() {
        editor = pref.edit();
        String userName = pref.getString("username","");
        String password = pref.getString("password","");
        if(!(userName.equals("") && userName.equals(""))){
            editor.putString("password",passwd1MD5);
            editor.apply();
            editor.commit();
        }

        user.setPassword(passwd1MD5);
        spremeniGeslo.dismiss();
        CharSequence text = "Geslo spremenjeno";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    @Override
    public void spremeniPodatke() {
        user.setMail(mailET.getText().toString());
        user.setPhone(telefonET.getText().toString());
        CharSequence text = "Vaši osebni podatki so bili spremenjeni";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        dopolniPodatke.dismiss();

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
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
