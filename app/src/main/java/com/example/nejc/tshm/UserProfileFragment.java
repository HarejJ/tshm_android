package com.example.nejc.tshm;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.ButtonBarLayout;
import android.text.Layout;
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
    private RESTCallTask restTask;
    private AsyncResponse asyncResponse;
    private Context context;
    private ImageView oblekaRezervacija;
    private TextView designerRezervacija,tipRezervacija,spol_velikostRezervacija;
    private TextView userName,name,mail,statusTw,popularTw;
    private LinearLayout withoutReservation,reservation,popular;
    private LinearLayout statusRezervacijeLL, statusOblacilaLL, statusOddanoLL, statusSprejetoLL;
    private Button galleryBtn,statusBtn,popularBtn;
    private Button sprejemOblacila,oddajaOblacila,izbrisiRezervacijo,predajNaprej;
    private LinearLayout statusRezervacija,statusOblacila,oblaciloOddano,oblaciloSprejeto;
    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user =(User) getArguments().getSerializable("user");

        fragmentTransaction = this.getFragmentManager().beginTransaction();
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

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


        oblekaRezervacija = (ImageView)view.findViewById(R.id.SlikaRezervacja);
        designerRezervacija = (TextView) view.findViewById(R.id.OblikovalecRezervacija);
        tipRezervacija =(TextView) view.findViewById(R.id.TipRezervacija);
        spol_velikostRezervacija = (TextView) view.findViewById(R.id.Spol_velikostRezervacija);

        statusTw.setVisibility(View.INVISIBLE);
        popularTw.setVisibility(View.VISIBLE);
        withoutReservation.setVisibility(View.GONE);
        reservation.setVisibility(View.GONE);
        popular.setVisibility(View.VISIBLE);
        userName.setText(user.getUsername());
        name.setText(user.getName());
        mail.setText(user.getMail());

        statusRezervacija = (LinearLayout) view.findViewById(R.id.StatusRezervacija);
        statusOblacila = (LinearLayout) view.findViewById(R.id.StatusOblacila);
        oblaciloOddano = (LinearLayout) view.findViewById(R.id.OblaciloOddano);
        oblaciloSprejeto = (LinearLayout) view.findViewById(R.id.OblaciloSprejeto);

        sprejemOblacila = (Button) view.findViewById(R.id.SprejemOblacila);
        oddajaOblacila = (Button) view.findViewById(R.id.OddajaBtn);
        izbrisiRezervacijo = (Button) view.findViewById(R.id.DeleteReservationBtn);
        predajNaprej = (Button) view.findViewById(R.id.predajNaprej);

        statusRezervacijeLL =(LinearLayout) view.findViewById(R.id.StatusRezervacija);
        statusOblacilaLL = (LinearLayout) view.findViewById(R.id.StatusOblacila);
        statusOddanoLL =(LinearLayout) view.findViewById(R.id.OblaciloOddano);
        statusSprejetoLL =(LinearLayout) view.findViewById(R.id.OblaciloSprejeto);

        CircleImageView userImage =(CircleImageView) view.findViewById(R.id.profile_image);
        userImage.setImageBitmap(ImageUtil.convert(user.getImage()));

        predajNaprej.setOnClickListener(onClickListener);
        izbrisiRezervacijo.setOnClickListener(onClickListener);
        statusBtn.setOnClickListener(onClickListener);
        popularBtn.setOnClickListener(onClickListener);
        galleryBtn.setOnClickListener(onClickListener);
        sprejemOblacila.setOnClickListener(onClickListener);
        context = getContext();
        asyncResponse = this;
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
                        spol_velikostRezervacija .setText(user.getReservedDress().getSpol()+" Velikost "+user.getReservedDress().getVelikost());

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
                        }
                        else if(user.isPredaja() && !user.isIzposojena() && !user.isPredajaNaprej()
                                && !user.isVrnjena()){
                            statusSprejetoLL.setVisibility(View.VISIBLE);
                        }
                        else if(user.isPredaja() && user.isIzposojena() && !user.isPredajaNaprej()
                                && !user.isVrnjena()){
                            statusOblacila.setVisibility(View.VISIBLE);
                        }
                        else if(user.isPredaja() && user.isIzposojena() && user.isPredajaNaprej()
                                && !user.isVrnjena()){
                            statusOddanoLL.setVisibility(View.VISIBLE);
                        }
                        else if(user.isPredaja() && user.isIzposojena() && user.isPredajaNaprej()
                                && !user.isVrnjena()){
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
                    withoutReservation.setVisibility(View.INVISIBLE);
                    reservation.setVisibility(View.INVISIBLE);
                    popular.setVisibility(View.VISIBLE);
                    break;

                case R.id.galleryBtn:
                    Bundle args = new Bundle();
                    args.putSerializable("user", (Serializable) user);

                    GalleryFragment galleryFragment = new GalleryFragment();
                    galleryFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.fragment_container, galleryFragment);
                    fragmentTransaction.commit();
                    break;

                case R.id.SprejemOblacila:
                    restTask = new RESTCallTask("izposojena",user.getUsername(),user.getPassword(),user.getReservedDress().getId_obleka(),view);
                    restTask.delegate = asyncResponse;
                    restTask.execute("POST", String.format("izposojena"));

                    break;
                case R.id.predajNaprej:
                    restTask = new RESTCallTask("predajaNaprej",user.getUsername(),user.getPassword(),user.getReservedDress().getId_obleka(),view);
                    restTask.delegate = asyncResponse;
                    restTask.execute("POST", String.format("predajaNaprej"));

                    break;
                case R.id.DeleteReservationBtn:
                    restTask = new RESTCallTask("deleteReservation",user.getUsername(),user.getPassword(),user.getReservedDress().getId_obleka(),view);
                    restTask.delegate = asyncResponse;
                    restTask.execute("POST", String.format("deleteReservation"));
                    break;
            }
        }
    };


    @Override
    public void processFinish(ArrayList<Dress> output) {

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

        fragmentTransaction.replace(R.id.fragment_container, galleryFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void sprejemRezervacije() {
        user.setIzposojena(true);
        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);
        statusOblacila.setVisibility(View.GONE);
        statusOddanoLL.setVisibility(View.VISIBLE);

    }

    @Override
    public void predajaNaprej() {
        user.setIzposojena(true);
        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);
        statusSprejetoLL.setVisibility(View.GONE);
        statusOblacila.setVisibility(View.VISIBLE);
    }
}
