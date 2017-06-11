package com.example.nejc.tshm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;


public class MainFragment extends Fragment {

    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private User user;
    private Button galleryBtn1, profileBtn1, aboutBtn1, careOfClothesBtn1;
    private ImageView left, right, radio1, radio2, radio3, radio4;
    private LinearLayout title1, title2, title3, title4, factBdy;
    private int prevPosition, position;
    private LinearLayout[] titles = new LinearLayout[4];
    private ImageView[] titlePosition = new ImageView[4];


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = (User) getArguments().getSerializable("user");

        fragmentTransaction = this.getFragmentManager().beginTransaction();
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        galleryBtn1 = (Button) view.findViewById(R.id.galleryBtn1);
        profileBtn1 = (Button) view.findViewById(R.id.profilbtn1);
        aboutBtn1 = (Button) view.findViewById(R.id.aboutBtn1);
        careOfClothesBtn1 = (Button) view.findViewById(R.id.careOfClothesBtn1);
        factBdy = (LinearLayout) view.findViewById(R.id.upperFactBody);
        left = (ImageView) view.findViewById(R.id.slideLeft);
        right = (ImageView) view.findViewById(R.id.slideRight);
        title1 = (LinearLayout) view.findViewById(R.id.TitleLL1);
        title2 = (LinearLayout) view.findViewById(R.id.TitleLL2);
        title3 = (LinearLayout) view.findViewById(R.id.TitleLL3);
        title4 = (LinearLayout) view.findViewById(R.id.TitleLL4);
        radio1 = (ImageView) view.findViewById(R.id.radio1);
        radio2 = (ImageView) view.findViewById(R.id.radio2);
        radio3 = (ImageView) view.findViewById(R.id.radio3);
        radio4 = (ImageView) view.findViewById(R.id.radio4);
        titles[0] = title1;
        titles[1] = title2;
        titles[2] = title3;
        titles[3] = title4;
        titlePosition[0] = radio1;
        titlePosition[1] = radio2;
        titlePosition[2] = radio3;
        titlePosition[3] = radio4;
        position = 0;
        prevPosition = 0;
        setPosition(0);
        galleryBtn1.setOnClickListener(onClickListener);
        profileBtn1.setOnClickListener(onClickListener);
        aboutBtn1.setOnClickListener(onClickListener);
        careOfClothesBtn1.setOnClickListener(onClickListener);
        //factBdy.
        left.setOnClickListener(onClickListener);
        right.setOnClickListener(onClickListener);

        return view;
    }

    private void setPosition(int id) {
        for (int i = 0; i < 4; i++) {
            if (i != position) {
                if (id == R.id.slideRight && prevPosition == i) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_righ);
                    animation.setDuration(300);
                    titles[i].setAnimation(animation);
                    titles[i].animate();
                    animation.start();
                } else if (id == R.id.slideLeft && prevPosition == i) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
                    animation.setDuration(300);
                    titles[i].setAnimation(animation);
                    titles[i].animate();
                    animation.start();
                }
                titlePosition[i].setImageResource(R.drawable.radiowhite);
                titles[i].setVisibility(View.GONE);
            }
        }
        if (id == R.id.slideRight) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
            animation.setDuration(300);
            titles[position].setAnimation(animation);
            titles[position].animate();
            animation.start();
        } else if (id == R.id.slideLeft) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
            animation.setDuration(300);
            titles[position].setAnimation(animation);
            titles[position].animate();
            animation.start();
        }
        titlePosition[position].setImageResource(R.drawable.radiogreen);
        titles[position].setVisibility(View.VISIBLE);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle args = new Bundle();
            switch (view.getId()) {
                case R.id.galleryBtn1:
                    args.putSerializable("user", (Serializable) user);

                    GalleryFragment galleryFragment = new GalleryFragment();
                    galleryFragment.setArguments(args);

                    fragmentTransaction.replace(R.id.fragment_container, galleryFragment);
                    fragmentTransaction.commit();
                    break;

                case R.id.profilbtn1:
                    args.putSerializable("user", (Serializable) user);

                    UserProfileFragment userProfileFragment = new UserProfileFragment();
                    userProfileFragment.setArguments(args);

                    fragmentTransaction.replace(R.id.fragment_container, userProfileFragment);
                    fragmentTransaction.commit();
                    break;

                case R.id.careOfClothesBtn1:
                    CareOfClothes careOfClothes = new CareOfClothes();
                    fragmentTransaction.replace(R.id.fragment_container, careOfClothes);
                    fragmentTransaction.commit();
                    break;

                case R.id.aboutBtn1:
                    AboutProjectFragment aboutProjectFragment = new AboutProjectFragment();
                    fragmentTransaction.replace(R.id.fragment_container, aboutProjectFragment);
                    fragmentTransaction.commit();
                    break;

                case R.id.slideRight:
                    prevPosition = position;
                    position = (position + 1) % 4;
                    setPosition(view.getId());
                    break;

                case R.id.slideLeft:
                    prevPosition = position;
                    position = (position -= 1) < 0 ? 3 : position;
                    setPosition(view.getId());
            }

        }
    };
}
