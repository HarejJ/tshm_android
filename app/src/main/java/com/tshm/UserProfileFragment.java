package com.tshm;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.PopupMenu;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment implements AsyncResponse {
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private User user;
    private int id;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    EditText telefonET,mailET,naslovET,krajET;
    ImageView changeProfilImage;
    AlertDialog spremeniGeslo, dopolniPodatke;
    private EditText novoGeslo1ET, novoGeslo2ET, staroGesloET;
    private static ArrayList<Dress> clothes = new ArrayList<>();
    private static ArrayList<Integer> imaggesIds = new ArrayList<Integer>();
    private RESTCallTask restTask;
    private AsyncResponse asyncResponse;
    private Context context;
    String passwd1MD5;
    View view;
    private ImageView oblekaRezervacija,menu_profil;
    private LinearLayout images;
    private TextView designerRezervacija,tipRezervacija,spol_velikostRezervacija;
    private TextView userName,name,mail,statusTw,popularTw,cakalnaVrsta,trenutniUporabnik, stDniTw;
    private LinearLayout withoutReservation,reservation,popular;
    private LinearLayout statusRezervacijeLL, statusOblacilaLL, statusOddanoLL, statusSprejetoLL;
    private Button galleryBtn,statusBtn,popularBtn,kontaktImetnika,kontaktPrejemnika;
    private Button sprejemOblacila,oddajaOblacila,izbrisiRezervacijo,predajNaprej, deleteReservation;
    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user =(User) getArguments().getSerializable("user");

        fragmentTransaction = this.getFragmentManager().beginTransaction();

        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        images = (LinearLayout) view.findViewById(R.id.priljubljene);
        withoutReservation =(LinearLayout) view.findViewById(R.id.brezRezervacije);
        reservation = (LinearLayout) view.findViewById(R.id.Rezervacija);
        popular = (LinearLayout) view.findViewById(R.id.priljubljeniKosi);

        userName = (TextView) view.findViewById(R.id.userNameTV);
        name = (TextView) view.findViewById(R.id.NameTW);
        mail = (TextView) view.findViewById(R.id.mailTW);
        statusTw = (TextView) view.findViewById(R.id.textViewStatus);
        popularTw = (TextView) view.findViewById(R.id.textViewPopular);
        galleryBtn = (Button) view.findViewById(R.id.galleryBtn);
        statusBtn = (Button) view.findViewById(R.id.status);
        popularBtn = (Button) view.findViewById(R.id.popular);
        menu_profil = (ImageView)view.findViewById(R.id.menu_profil);
        stDniTw = (TextView)view.findViewById(R.id.stDniTw);
        oblekaRezervacija = (ImageView)view.findViewById(R.id.SlikaRezervacja);
        designerRezervacija = (TextView) view.findViewById(R.id.OblikovalecRezervacija);
        tipRezervacija =(TextView) view.findViewById(R.id.TipRezervacija);
        spol_velikostRezervacija = (TextView) view.findViewById(R.id.Spol_velikostRezervacija);
        statusTw.setVisibility(View.INVISIBLE);
        popularTw.setVisibility(View.VISIBLE);
        withoutReservation.setVisibility(View.GONE);
        reservation.setVisibility(View.GONE);
        popular.setVisibility(View.GONE);





        userName.setText(user.getUsername());
        name.setText(user.getName());
        if(!user.getMail().equals("null"))
            mail.setText(user.getMail());

        deleteReservation = (Button) view.findViewById(R.id.deleteReservation);
        sprejemOblacila = (Button) view.findViewById(R.id.SprejemOblacila);
        oddajaOblacila = (Button) view.findViewById(R.id.OddajaBtn);
        izbrisiRezervacijo = (Button) view.findViewById(R.id.DeleteReservationBtn);
        predajNaprej = (Button) view.findViewById(R.id.predajNaprej);
        kontaktImetnika = (Button) view.findViewById(R.id.kontaktImetnika);
        kontaktPrejemnika =(Button) view.findViewById(R.id.kontaktPrejemnika);
        statusRezervacijeLL =(LinearLayout) view.findViewById(R.id.StatusRezervacija);
        statusOblacilaLL = (LinearLayout) view.findViewById(R.id.StatusOblacila);
        statusOddanoLL =(LinearLayout) view.findViewById(R.id.OblaciloOddano);
        statusSprejetoLL =(LinearLayout) view.findViewById(R.id.OblaciloSprejeto);
        cakalnaVrsta = (TextView) view.findViewById(R.id.cakalnaVrsta);
        trenutniUporabnik  = (TextView) view.findViewById(R.id.trenutniUporabnik);
        CircleImageView userImage =(CircleImageView) view.findViewById(R.id.profile_image);
        userImage.setImageBitmap(ImageUtil.convert(user.getImage()));
        kontaktPrejemnika.setOnClickListener(onClickListener);
        kontaktImetnika.setOnClickListener(onClickListener);
        predajNaprej.setOnClickListener(onClickListener);
        izbrisiRezervacijo.setOnClickListener(onClickListener);
        statusBtn.setOnClickListener(onClickListener);
        popularBtn.setOnClickListener(onClickListener);
        galleryBtn.setOnClickListener(onClickListener);
        sprejemOblacila.setOnClickListener(onClickListener);
        oddajaOblacila.setOnClickListener(onClickListener);
        deleteReservation.setOnClickListener(onClickListener);
        menu_profil.setOnClickListener(onClickListener);
        context = getContext();
        asyncResponse = this;
        oblekaRezervacija.setOnClickListener(onClickListener);

        restTask = new RESTCallTask("priljubljeneObleke",user.getUsername(),user.getPassword(),this.getView());
        restTask.delegate = asyncResponse;
        restTask.execute("POST", String.format("priljubljeneObleke"));

        setStatus();

        return view;

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.status:

                    statusTw.setVisibility(View.VISIBLE);
                    popularTw.setVisibility(View.INVISIBLE);
                    popular.setVisibility(View.GONE);
                    if(user.isRezervacija()) {
                        oblekaRezervacija.setImageBitmap(ImageUtil.convert(user.getReservedDress().getSlika()));
                        designerRezervacija.setText(user.getReservedDress().getOblikovalec());
                        tipRezervacija.setText(user.getReservedDress().getTip());
                        spol_velikostRezervacija.setText(user.getReservedDress().getSpol()+" Velikost "+user.getReservedDress().getVelikost());
                        stDniTw.setText(user.getReservedDress().getStDni());
                        reservation.setVisibility(View.VISIBLE);
                        withoutReservation.setVisibility(View.GONE);
                        popular.setVisibility(View.GONE);
                        statusRezervacijeLL.setVisibility(View.GONE);
                        statusOblacilaLL.setVisibility(View.GONE);
                        statusOddanoLL.setVisibility(View.GONE);
                        statusSprejetoLL.setVisibility(View.GONE);

                        if(!user.isPredaja() && !user.isIzposojena() && !user.isPredajaNaprej()
                                && !user.isVrnjena()){
                            statusRezervacijeLL.setVisibility(View.VISIBLE);
                            trenutniUporabnik.setText(String.valueOf(user.getReservedDress().getTrenutniImetnik()));
                            cakalnaVrsta.setText(String.valueOf(user.getReservedDress().getCakalnaVrsta()));
                        }
                        else if(user.isPredaja() && !user.isIzposojena() && !user.isPredajaNaprej()
                                && !user.isVrnjena()){
                            statusSprejetoLL.setVisibility(View.VISIBLE);
                        }
                        else if(user.isPredaja() && user.isIzposojena() && !user.isPredajaNaprej()
                                && !user.isVrnjena()){
                            statusOblacilaLL.setVisibility(View.VISIBLE);
                        }
                        else if(user.isPredaja() && user.isIzposojena() && user.isPredajaNaprej()
                                && !user.isVrnjena()){
                            statusOddanoLL.setVisibility(View.VISIBLE);
                        }
                        else if(user.isPredaja() && user.isIzposojena() && user.isPredajaNaprej()
                                && user.isVrnjena()){
                            reservation.setVisibility(View.GONE);
                            withoutReservation.setVisibility(View.VISIBLE);
                            popular.setVisibility(View.GONE);
                        }
                    }
                    else {
                        reservation.setVisibility(View.GONE);
                        withoutReservation.setVisibility(View.VISIBLE);
                        popular.setVisibility(View.GONE);
                    }

                    break;

                case R.id.popular:
                    statusTw.setVisibility(View.INVISIBLE);
                    popularTw.setVisibility(View.VISIBLE);
                    withoutReservation.setVisibility(View.GONE);
                    reservation.setVisibility(View.GONE);
                    popular.setVisibility(View.VISIBLE);
                    break;

                case R.id.galleryBtn:
                    Bundle args = new Bundle();
                    args.putSerializable("user", (Serializable) user);

                    GalleryFragment galleryFragment = new GalleryFragment();
                    galleryFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.fragment_container, galleryFragment).addToBackStack(null);
                    fragmentTransaction.commit();
                    break;

                case R.id.SprejemOblacila:
                    if(NetworkUtils.isNetworkConnected(context)) {
                        restTask = new RESTCallTask("izposojena", user.getUsername(), user.getPassword(), user.getReservedDress().getId_obleka(), view);
                        restTask.delegate = asyncResponse;
                        restTask.execute("POST", String.format("izposojena"));
                    } else
                        Dialog.networkErrorDialog(context).show();
                    break;


                case R.id.OddajaBtn:
                    if(NetworkUtils.isNetworkConnected(context)) {
                        restTask = new RESTCallTask("oddana",user.getUsername(),user.getPassword(),user.getReservedDress().getId_obleka(),view);
                        restTask.delegate = asyncResponse;
                        restTask.execute("POST", String.format("oddana"));
                    } else
                        Dialog.networkErrorDialog(context).show();
                    break;

                case R.id.predajNaprej:
                    if(NetworkUtils.isNetworkConnected(context)) {
                        restTask = new RESTCallTask("predajaNaprej",user.getUsername(),user.getPassword(),user.getReservedDress().getId_obleka(),view);
                        restTask.delegate = asyncResponse;
                        restTask.execute("POST", String.format("predajaNaprej"));
                    } else
                        Dialog.networkErrorDialog(context).show();

                    break;
                case R.id.DeleteReservationBtn:
                    if(NetworkUtils.isNetworkConnected(context)) {
                        restTask = new RESTCallTask("deleteReservation",user.getUsername(),user.getPassword(),user.getReservedDress().getId_obleka(),view);
                        restTask.delegate = asyncResponse;
                        restTask.execute("POST", String.format("deleteReservation"));
                    } else {
                        Dialog.networkErrorDialog(context).show();
                    }
                    break;
                case R.id.deleteReservation:
                    if(NetworkUtils.isNetworkConnected(context)) {
                        restTask = new RESTCallTask("deleteReservation",user.getUsername(),user.getPassword(),user.getReservedDress().getId_obleka(),view);
                        restTask.delegate = asyncResponse;
                        restTask.execute("POST", String.format("deleteReservation"));
                    } else {
                        Dialog.networkErrorDialog(context).show();
                    }
                    break;
                case R.id.kontaktImetnika:
                    if(NetworkUtils.isNetworkConnected(context)) {
                        restTask = new RESTCallTask("kontaktImetnika",user.getUsername(),user.getPassword(),user.getReservedDress().getId_obleka(),view);
                        restTask.delegate = asyncResponse;
                        restTask.execute("POST", String.format("kontaktImetnika"));
                } else {
                    Dialog.networkErrorDialog(context).show();
                }
                    break;
                case R.id.kontaktPrejemnika:
                    if(NetworkUtils.isNetworkConnected(context)) {
                        restTask = new RESTCallTask("kontaktprejemnika",user.getUsername(),user.getPassword(),user.getReservedDress().getId_obleka(),view);
                        restTask.delegate = asyncResponse;
                        restTask.execute("POST", String.format("kontaktprejemnika"));
                    } else {
                        Dialog.networkErrorDialog(context).show();
                    }
                    break;
                case R.id.SlikaRezervacja:
                {
                    Dress dress = user.getReservedDress();
                    if(NetworkUtils.isNetworkConnected(context)) {
                        restTask = new RESTCallTask("dressDetail1",user.getUsername(),user.getPassword(),
                                dress.getId_obleka(),dress.getId_obleka(),view);
                        restTask.delegate = asyncResponse;
                        restTask.execute("POST", String.format("dressDetail"));
                    }
                    else {
                        Dialog.networkErrorDialog(context).show();
                    }
                    break;
                }
                case R.id.menu_profil:
                {
                    openMenu();
                }
                default:
                    for (int i = 0; i<imaggesIds.size(); i++){
                        if(view.getId() == imaggesIds.get(i)){
                            id = imaggesIds.get(i);
                            if(NetworkUtils.isNetworkConnected(context)) {
                                restTask = new RESTCallTask("dressDetail",user.getUsername(),user.getPassword(),
                                        clothes.get(id).getId_obleka(),clothes.get(id).getId_obleka(),view);
                                restTask.delegate = asyncResponse;
                                restTask.execute("POST", String.format("dressDetail"));
                            }
                            else {
                                Dialog.networkErrorDialog(context).show();
                            }
                            break;
                        }
                    }

            }
        }
    };


    @Override
    public void processFinish(ArrayList<Dress> output) {
        clothes.clear();
        imaggesIds.clear();
        for(int i = 0; i<output.size(); i++){
            clothes.add(output.get(i));
        }
        if(clothes.size()>0)
            setImage(clothes);
        return;
    }
    public void setStatus(){
        statusTw.setVisibility(View.VISIBLE);
        popularTw.setVisibility(View.INVISIBLE);
        popular.setVisibility(View.GONE);
        if(user.isRezervacija()) {
            oblekaRezervacija.setImageBitmap(ImageUtil.convert(user.getReservedDress().getSlika()));
            designerRezervacija.setText(user.getReservedDress().getOblikovalec());
            tipRezervacija.setText(user.getReservedDress().getTip());
            spol_velikostRezervacija.setText(user.getReservedDress().getSpol() + " Velikost " + user.getReservedDress().getVelikost());
            stDniTw.setText(user.getReservedDress().getStDni());
            reservation.setVisibility(View.VISIBLE);
            withoutReservation.setVisibility(View.GONE);
            popular.setVisibility(View.GONE);
            statusRezervacijeLL.setVisibility(View.GONE);
            statusOblacilaLL.setVisibility(View.GONE);
            statusOddanoLL.setVisibility(View.GONE);
            statusSprejetoLL.setVisibility(View.GONE);

            if (!user.isPredaja() && !user.isIzposojena() && !user.isPredajaNaprej()
                    && !user.isVrnjena()) {
                statusRezervacijeLL.setVisibility(View.VISIBLE);
                trenutniUporabnik.setText(String.valueOf(user.getReservedDress().getTrenutniImetnik()));
                cakalnaVrsta.setText(String.valueOf(user.getReservedDress().getCakalnaVrsta()));
            } else if (user.isPredaja() && !user.isIzposojena() && !user.isPredajaNaprej()
                    && !user.isVrnjena()) {
                statusSprejetoLL.setVisibility(View.VISIBLE);
            } else if (user.isPredaja() && user.isIzposojena() && !user.isPredajaNaprej()
                    && !user.isVrnjena()) {
                statusOblacilaLL.setVisibility(View.VISIBLE);
            } else if (user.isPredaja() && user.isIzposojena() && user.isPredajaNaprej()
                    && !user.isVrnjena()) {
                statusOddanoLL.setVisibility(View.VISIBLE);
            } else if (user.isPredaja() && user.isIzposojena() && user.isPredajaNaprej()
                    && user.isVrnjena()) {
                reservation.setVisibility(View.GONE);
                withoutReservation.setVisibility(View.VISIBLE);
                popular.setVisibility(View.GONE);
            }
        }
        else {
            reservation.setVisibility(View.GONE);
            withoutReservation.setVisibility(View.VISIBLE);
            popular.setVisibility(View.GONE);
        }
    }

    @Override
    public void clothesReserved(Dress output, boolean[] user) {

    }

    @Override
    public void deleteReservation() {
        user.setReservedDress(null);
        user.setIzposojena(false);
        user.setRezervacija(false);

        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);
        GalleryFragment galleryFragment = new GalleryFragment();
        galleryFragment.setArguments(args);

        fragmentTransaction.replace(R.id.fragment_container, galleryFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void sprejemRezervacije() {
        user.setIzposojena(true);
        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);
        statusRezervacijeLL.setVisibility(View.GONE);
        statusOblacilaLL.setVisibility(View.GONE);
        statusOddanoLL.setVisibility(View.GONE);
        statusSprejetoLL.setVisibility(View.GONE);
        statusOblacilaLL.setVisibility(View.VISIBLE);
    }

    @Override
    public void predajaNaprej() {
        user.setPredajaNaprej(true);
        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);
        statusRezervacijeLL.setVisibility(View.GONE);
        statusOblacilaLL.setVisibility(View.GONE);
        statusOddanoLL.setVisibility(View.VISIBLE);
        statusSprejetoLL.setVisibility(View.GONE);
        statusOblacilaLL.setVisibility(View.GONE);
    }

    @Override
    public void oddajaRezervacije() {
        user.setVrnjena(true);
        user.setRezervacija(false);
        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);
        GalleryFragment galleryFragment = new GalleryFragment();
        galleryFragment.setArguments(args);

        fragmentTransaction.replace(R.id.fragment_container, galleryFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void oddajaRezervacijeZavrnjena() {

    }

    @Override
    public void kontaktImetnika(String[] user) {
        Dialog.imetnikOblacila(context, user).show();
    }

    @Override
    public void clothesNotReserved() {

    }

    @Override
    public void dressDetail(String[] dressDeatil) {
        clothes.get(id).setTip(dressDeatil[0]);
        clothes.get(id).setVelikost(dressDeatil[1]);
        clothes.get(id).setOblikovalec(dressDeatil[2]);
        clothes.get(id).setSlikaOblikovalca(dressDeatil[3]);
        clothes.get(id).setTrenutniImetnik(dressDeatil[4]);
        clothes.get(id).setCakalnaVrsta(Integer.parseInt(dressDeatil[5]));
        clothes.get(id).setSpol(dressDeatil[6]);
        clothes.get(id).setOznaka(dressDeatil[7]);
        clothes.get(id).setPriljubljena(Integer.valueOf(dressDeatil[8])>0 ? true:false);


        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);
        args.putSerializable("dress", (Serializable) clothes.get(id));
        ClothesFragment dressFragment = new ClothesFragment();
        dressFragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, dressFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void dressDetail1(String[] dressDeatil) {
        user.getReservedDress().setTip(dressDeatil[0]);
        user.getReservedDress().setVelikost(dressDeatil[1]);
        user.getReservedDress().setOblikovalec(dressDeatil[2]);
        user.getReservedDress().setSlikaOblikovalca(dressDeatil[3]);
        user.getReservedDress().setTrenutniImetnik(dressDeatil[4]);
        user.getReservedDress().setCakalnaVrsta(Integer.parseInt(dressDeatil[5]));
        user.getReservedDress().setSpol(dressDeatil[6]);
        user.getReservedDress().setOznaka(dressDeatil[7]);
        user.getReservedDress().setPriljubljena(Integer.valueOf(dressDeatil[8])>0 ? true:false);


        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);
        args.putSerializable("dress", (Serializable) user.getReservedDress());
        ClothesFragment dressFragment = new ClothesFragment();
        dressFragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, dressFragment).addToBackStack(null);
        fragmentTransaction.commit();
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
        CharSequence text = "Tvoji osebni podatki so bili spremenjeni.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        dopolniPodatke.dismiss();

    }


    private void setImage(ArrayList<Dress> output){
        imaggesIds.clear();
        int size=0;
        int count =1;
        Integer id = 0;
        LinearLayout.LayoutParams paramsLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        LinearLayout.LayoutParams parmsImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.95f);

        LinearLayout right = new LinearLayout(context);
        LinearLayout line = new LinearLayout(context);
        line.setWeightSum(2);
        line.setLayoutParams(paramsLine);
        for (Dress dress : output){
            LinearLayout left = new LinearLayout(context);
            LinearLayout image = new LinearLayout(context);

            //image layout
            left.setWeightSum(1);
            left.setGravity(count%2 ==0 ? Gravity.RIGHT : Gravity.LEFT);
            parmsImage.height = (int) (Resources.getSystem().getDisplayMetrics().widthPixels/1.7);
            parmsImage.width = (int) (left.getWidth());
            //image

            BitmapDrawable background = new BitmapDrawable(ImageUtil.convert(dress.getSlika()));
            image.setBackground(background);
            image.setId(id);
            image.setLayoutParams(parmsImage);
            image.setOnClickListener(onClickListener);

            imaggesIds.add(id);
            id++;
            params.width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels/2);
            left.setLayoutParams(params);

            left.addView(image);
            line.addView(left);

            count++;
            if(count % 3 == 0){
                images.addView(line);

                line = new LinearLayout(context);
                line.addView(space());

                images.addView(line);
                line = new LinearLayout(context);
                count = 1;
                line.setLayoutParams(paramsLine);
                line.setWeightSum(2);
            }
        }
        if(count % 3 != 0){
            BitmapDrawable background = new BitmapDrawable(ImageUtil.convert(output.get(0).getSlika()));
            right.setMinimumWidth(size);
            right.setBackgroundColor(Color.BLACK);
            right.setLayoutParams(params);
            right.setMinimumWidth(size);
            line.addView(right);
            images.addView(line);

        }

        return;
    }
    private LinearLayout space(){
        LinearLayout space = new LinearLayout(context);
        space.setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
        space.setMinimumHeight(20);
        return space;

    }

    private void openMenu(){
        final MainActivity mainActivity =  MainActivity.mainActivity;
        PopupMenu popup = new PopupMenu(mainActivity, menu_profil);
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
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(mainActivity);
                    View mView = mainActivity.getLayoutInflater().inflate(R.layout.dailog_change_password,null);
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
                                staroGesloET.setError("Trenutno geslo ni pravilno");
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
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(mainActivity);
                    View mView = mainActivity.getLayoutInflater().inflate(R.layout.dialog_add_user_data,null);
                    telefonET = (EditText) mView.findViewById(R.id.telET);
                    mailET = (EditText) mView.findViewById(R.id.mailET);
                    naslovET = (EditText) mView.findViewById(R.id.nasET);
                    krajET = (EditText) mView.findViewById(R.id.krajET);
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
                            if(krajET.length() == 0){
                                krajET.setError("Vnesite kraj");
                                vneseniPodatki =false;
                            }
                            if(!vneseniPodatki)
                                return;
                            String naslov = naslovET.getText().toString();
                            String mail = mailET.getText().toString();
                            String telefon = telefonET.getText().toString();
                            String kraj = krajET.getText().toString();

                            if(!validatedNumber(telefon)){
                                telefonET.setError("Telefonska številka je nepravilna");
                                return;
                            }
                            if(!emailValidator(mail)){
                                mailET.setError("e-mail je nepravilen");
                                return;
                            }
                            RESTCallTask restTask = new RESTCallTask("spremeniPodatke", user.getUsername(),
                                    user.getPassword(),naslov,mail,kraj,telefon, view);
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
