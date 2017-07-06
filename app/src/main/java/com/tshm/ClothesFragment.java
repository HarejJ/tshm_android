package com.tshm;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClothesFragment extends Fragment implements AsyncResponse {
    private User user;
    private View view;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private LinearLayout linearLayoutReservation, linearLayoutClothesCare;
    private ImageView picture, favoriteSign, oznake,oblikovalec;
    private Button clothesCareTB, reservationTB, reservationB, clothesCareB;
    private TextView clothesCare, reservation, imeOblikovalca, tipObleke, spol_velikost, trenutniImetnik, cakalnaVrsta;
    private TextView cena;
    private RESTCallTask restTask;
    private AsyncResponse asyncResponse;
    Dress dress;
    private Context context;

    public ClothesFragment() {
        setArguments(new Bundle());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = (User) getArguments().getSerializable("user");
        dress = (Dress) getArguments().getSerializable("dress");
        fragmentTransaction = this.getFragmentManager().beginTransaction();
        view = inflater.inflate(R.layout.fragment_clothes, container, false);
        picture = (ImageView) view.findViewById(R.id.imageDress);
        favoriteSign = (ImageView) view.findViewById(R.id.favoriteDress);
        picture.setImageBitmap(ImageUtil.convert(dress.getSlika()));
        favoriteSign.setImageResource(new int[]{R.drawable.unlike, R.drawable.like}[dress.isPriljubljena() ? 1 : 0]);
        context = getContext();
        asyncResponse = this;
        oznake = (ImageView) view.findViewById(R.id.oznake);
        oznake.setImageBitmap(ImageUtil.convert(dress.getOznaka()));
        oblikovalec = (CircleImageView) view.findViewById(R.id.oblikovalec);
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
        cena = (TextView) view.findViewById(R.id.VrednostTV);

        linearLayoutReservation = (LinearLayout) view.findViewById(R.id.LinearLayoutReservation);
        linearLayoutClothesCare = (LinearLayout) view.findViewById(R.id.LinearLayoutClothesCare);

        linearLayoutReservation.setVisibility(View.GONE);
        linearLayoutClothesCare.setVisibility(View.VISIBLE);

        imeOblikovalca.setText(dress.getOblikovalec());
        tipObleke.setText(dress.getTip());
        spol_velikost.setText(dress.getSpol() + ",Velikost " + dress.getVelikost());
        trenutniImetnik.setText(dress.getTrenutniImetnik());
        oblikovalec.setImageBitmap(ImageUtil.convert(dress.getSlikaOblikovalca()));
        cakalnaVrsta.setText(String.valueOf(dress.getCakalnaVrsta()));
        cena.setText("Vrednost: "+dress.getCena()+"€");

        clothesCareTB.setOnClickListener(onClickListener);
        reservationTB.setOnClickListener(onClickListener);
        reservationB.setOnClickListener(onClickListener);
        clothesCareB.setOnClickListener(onClickListener);
        picture.setOnClickListener(onClickListener);
        favoriteSign.setOnClickListener(onClickListener);

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
                    else if(user.getMail().equals("null") || user.getPhone().equals("null"))
                        Dialog.dopolniProfil(context).show();
                    else {
                        restTask = new RESTCallTask("reservation", user.getUsername(), user.getPassword(), dress.getId_obleka(), view);
                        restTask.delegate = asyncResponse;
                        restTask.execute("POST", String.format("reservation"));
                    }
                    break;
                case R.id.imageDress:
                    ImagePanningFragment imagePanningFragment = new ImagePanningFragment();
                    imagePanningFragment.setPictureSource(ImageUtil.convert(dress.getSlika()));

                    fragmentTransaction.replace(R.id.fragment_container, imagePanningFragment).addToBackStack(null);

                    fragmentTransaction.commit();
                    break;
                case R.id.favoriteDress:
                    if (dress.isPriljubljena()) {
                        if(NetworkUtils.isNetworkConnected(context)) {
                            restTask = new RESTCallTask("odstraniPriljubljeno", user.getUsername(), user.getPassword(), dress.getId_obleka(), view);
                            restTask.delegate = asyncResponse;
                            restTask.execute("POST", String.format("odstraniPriljubljeno"));

                            dress.setPriljubljena(false);
                            Toast.makeText(context, getString(R.string.toast0), Toast.LENGTH_SHORT).show();
                            favoriteSign.setImageResource(R.drawable.unlike);
                        }
                        else
                            Dialog.networkErrorDialog(context).show();
                        break;

                    } else {
                        if(NetworkUtils.isNetworkConnected(context)) {
                            restTask = new RESTCallTask("dodajPriljubljeno", user.getUsername(), user.getPassword(), dress.getId_obleka(), view);
                            restTask.delegate = asyncResponse;
                            restTask.execute("POST", String.format("dodajPriljubljeno"));

                            dress.setPriljubljena(true);
                            Toast.makeText(context, getString(R.string.toast1), Toast.LENGTH_SHORT).show();
                            favoriteSign.setImageResource(R.drawable.like);
                        }
                        else
                            Dialog.networkErrorDialog(context).show();
                    }
                    break;
            }
        }
    };


    @Override
    public void processFinish(ArrayList<Dress> output) {

    }

    @Override
    public void clothesReserved(Dress output, boolean[] userData) {
        CharSequence text = "Oblačilo je bilo uspešno rezervirano.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        user.setRezervacija(userData[0]);
        user.setPredaja(userData[1]);
        user.setIzposojena(userData[2]);
        user.setPredajaNaprej(userData[3]);
        user.setVrnjena(userData[4]);

        user.setReservedDress(output);
        
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

    @Override
    public void dressDetail(String[] dressDeatil) {

    }

    @Override
    public void dressDetail1(String[] dressDeatil) {

    }

    @Override
    public void addFavorite() {
        user.addFavoriteDress(dress);
    }

    @Override
    public void deleteFavorite() {
        user.deleteFavoriteDress(dress);
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

}
