package com.example.nejc.tshm;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;


public class ClothesFragment extends Fragment implements AsyncResponse {
    private User user;
    private View view;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private LinearLayout linearLayoutReservation, linearLayoutClothesCare;
    private ImageView picture;
    private Button clothesCareTB, reservationTB, reservationB, clothesCareB;
    private TextView clothesCare, reservation, imeOblikovalca, tipObleke, spol_velikost, trenutniImetnik, cakalnaVrsta;
    private RESTCallTask restTask;
    private AsyncResponse asyncResponse;
    Dress dress;
    private Context context;

    public ClothesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = (User) getArguments().getSerializable("user");
        dress = (Dress) getArguments().getSerializable("dress");
        fragmentTransaction = this.getFragmentManager().beginTransaction();
        view = inflater.inflate(R.layout.fragment_clothes, container, false);
        picture = (ImageView) view.findViewById(R.id.imageDress);
        //BitmapDrawable background = new BitmapDrawable(ImageUtil.convert(dress.getSlika()));
        picture.setImageBitmap(ImageUtil.convert(dress.getSlika()));
        context = getContext();
        asyncResponse = this;
        clothesCareTB = (Button) view.findViewById(R.id.ClothesCareTB);
        reservationTB = (Button) view.findViewById(R.id.ReservationTB);
        reservationB = (Button) view.findViewById(R.id.ReservationB);
        clothesCareB = (Button) view.findViewById(R.id.ClothesCareB);
        clothesCare = (TextView) view.findViewById(R.id.textViewCare);
        reservation = (TextView) view.findViewById(R.id.textViewReservation);
        imeOblikovalca = (TextView) view.findViewById(R.id.ImeOblikovalca);
        tipObleke = (TextView) view.findViewById(R.id.tipObleke);
        spol_velikost = (TextView) view.findViewById(R.id.spol_velikost);
        trenutniImetnik = (TextView) view.findViewById(R.id.ownerTV);
        cakalnaVrsta = (TextView) view.findViewById(R.id.queueTV);


        linearLayoutReservation = (LinearLayout) view.findViewById(R.id.LinearLayoutReservation);
        linearLayoutClothesCare = (LinearLayout) view.findViewById(R.id.LinearLayoutClothesCare);


        imeOblikovalca.setText(dress.getOblikovalec());
        tipObleke.setText(dress.getTip());
        spol_velikost.setText(dress.getSpol() + ",Velikost " + dress.getVelikost());
        trenutniImetnik.setText(dress.getTrenutniImetnik());

        cakalnaVrsta.setText(String.valueOf(dress.getCakalnaVrsta()));

        clothesCareTB.setOnClickListener(onClickListener);
        reservationTB.setOnClickListener(onClickListener);
        reservationB.setOnClickListener(onClickListener);
        clothesCareB.setOnClickListener(onClickListener);
        picture.setOnClickListener(onClickListener);

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ClothesCareTB:
                    clothesCare.setVisibility(View.VISIBLE);
                    reservation.setVisibility(View.INVISIBLE);
                    linearLayoutReservation.setVisibility(View.GONE);
                    linearLayoutClothesCare.setVisibility(View.VISIBLE);
                    break;
                case R.id.ClothesCareB:
                    CareOfClothes fragment = new CareOfClothes();

                    fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case R.id.ReservationTB:
                    clothesCare.setVisibility(View.INVISIBLE);
                    reservation.setVisibility(View.VISIBLE);
                    linearLayoutReservation.setVisibility(View.VISIBLE);
                    linearLayoutClothesCare.setVisibility(View.GONE);
                    break;

                case R.id.ReservationB:
                    if (user.isRezervacija())
                        Dialog.reservationRefusalDialog(context).show();
                    else {
                        restTask = new RESTCallTask("reservation", user.getUsername(), user.getPassword(), dress.getId_obleka(), view);
                        restTask.delegate = asyncResponse;
                        restTask.execute("POST", String.format("reservation"));
                    }
                    break;
                case R.id.imageDress:
                    ImagePanningFragment imagePanningFragment = new ImagePanningFragment();
                    imagePanningFragment.setPictureSource(ImageUtil.convert(dress.getSlika()));

                    fragmentTransaction.replace(R.id.fragment_container, imagePanningFragment).addToBackStack(null);;
                    fragmentTransaction.commit();
                    break;
            }
        }
    };


    @Override
    public void processFinish(ArrayList<Dress> output) {

    }

    @Override
    public void clothesReserved(Dress output, boolean[] userData) {

        user.setRezervacija(userData[0]);
        user.setPredaja(userData[1]);
        user.setIzposojena(userData[2]);
        user.setPredajaNaprej(userData[3]);
        user.setVrnjena(userData[4]);

        user.setReservedDress(output);
        if (output.getCakalnaVrsta() == 0)
            user.setPredaja(true);
        Bundle args = new Bundle();
        args.putSerializable("user", (Serializable) user);

        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(args);

        fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
        fragmentTransaction.commit();
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
        Dialog.reservationRefusalDialog(context);
    }
}
