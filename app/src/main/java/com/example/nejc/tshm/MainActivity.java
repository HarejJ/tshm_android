package com.example.nejc.tshm;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.widget.PopupMenu;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    ImageView changeProfilImage;
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    User user = null;
    String newImage;
    Context context;
    View view;
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
        user = (User) i.getSerializableExtra("User");


        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);
        //Set the fragment initially
        MainFragment mainFragment = new MainFragment();
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
                        if (id == R.id.selfi) {
                            if(NetworkUtils.isNetworkConnected(context))
                                mainActivity.dispatchTakePictureIntent();
                            else
                                Dialog.networkErrorDialog(context).show();
                        }
                        if(id == R.id.galerija){
                            if(NetworkUtils.isNetworkConnected(context))
                                mainActivity.openGallery();
                            else
                                Dialog.networkErrorDialog(context).show();
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
}
