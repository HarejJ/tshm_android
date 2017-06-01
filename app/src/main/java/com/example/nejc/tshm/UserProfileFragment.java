package com.example.nejc.tshm;


import android.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    User user;
    TextView userName,name,mail,statusTw,popularTw;
    LinearLayout withoutReservation,reservation,popular;
    Button galleryBtn;
    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentTransaction = this.getFragmentManager().beginTransaction();
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        user =(User) getArguments().getSerializable("user");
        withoutReservation =(LinearLayout) view.findViewById(R.id.brezRezervacije);
        userName = (TextView) view.findViewById(R.id.userNameTV);
        name = (TextView) view.findViewById(R.id.NameTW);
        mail = (TextView) view.findViewById(R.id.mailTW);
        statusTw = (TextView) view.findViewById(R.id.textViewStatus);
        popularTw = (TextView) view.findViewById(R.id.textViewPopular);
        galleryBtn = (Button) view.findViewById(R.id.galleryBtn);

        statusTw.setVisibility(View.INVISIBLE);
        popularTw.setVisibility(View.VISIBLE);
        withoutReservation.setVisibility(View.INVISIBLE);

        userName.setText(user.getUsername());
        name.setText(user.getName());
        mail.setText(user.getMail());
        // slika
        /*
        String[] img = user.getImage().split(" ");
        byte[] bytes = new byte[img.length];
        for (int j=0; j<img.length; j++)
            bytes[j]= Byte.parseByte(img[j]);
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        */

        CircleImageView userImage =(CircleImageView) view.findViewById(R.id.profile_image);
        userImage.setImageBitmap(ImageUtil.convert(user.getImage()));
        Button status = (Button) view.findViewById(R.id.status);
        Button popular = (Button) view.findViewById(R.id.popular);

        status.setOnClickListener(onClickListener);
        popular.setOnClickListener(onClickListener);
        galleryBtn.setOnClickListener(onClickListener);
        return view;

    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.status:
                    statusTw.setVisibility(View.VISIBLE);
                    popularTw.setVisibility(View.INVISIBLE);
                    withoutReservation.setVisibility(View.VISIBLE);
                    break;

                case R.id.popular:
                    statusTw.setVisibility(View.INVISIBLE);
                    popularTw.setVisibility(View.VISIBLE);
                    withoutReservation.setVisibility(View.INVISIBLE);
                    break;

                case R.id.galleryBtn:
                    GalleryFragment fragment = new GalleryFragment();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();
                    break;
            }
        }
    };


}
