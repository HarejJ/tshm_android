package com.tshm;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.ButtonBarLayout;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment implements AsyncResponse {
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private User user;
    private int id;

    private static ArrayList<Dress> clothes = new ArrayList<>();
    private static ArrayList<Integer> imaggesIds = new ArrayList<Integer>();
    private RESTCallTask restTask;
    private AsyncResponse asyncResponse;
    private Context context;
    private ImageView oblekaRezervacija;
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

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
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

    }

    @Override
    public void spremeniPodatke() {

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

}
