package com.example.nejc.tshm;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;


public class GalleryFragment extends Fragment implements AsyncResponse {
    private RESTCallTask restTask;
    private ArrayList<Dress> clothes = new ArrayList<>();
    private ArrayList<Integer> imaggesIds = new ArrayList<Integer>();
    private View view;
    private Context context;
    private User user;
    private LinearLayout images;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user =(User) getArguments().getSerializable("user");

        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        context = getContext();
        fragmentTransaction = this.getFragmentManager().beginTransaction();
        images = (LinearLayout) view.findViewById(R.id.ImagesLayout);

        if(NetworkUtils.isNetworkConnected(context)) {
            restTask = new RESTCallTask("clothes",user.getUsername(),user.getPassword(),view);
            restTask.delegate = this;
            restTask.execute("POST", String.format("clothes"));
        }
        else {
            Dialog.networkErrorDialog(context).show();
            return view;
        }

        return view;
    }

    @Override
    public void processFinish(ArrayList<Dress> output){

        for(int i = 0; i<output.size(); i++){
            clothes.add(output.get(i));
        }
        setImage(clothes);
        return;
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

    private void setImage(ArrayList<Dress> output){
        int size=0;
        int count =1;
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

            parmsImage.height = (int) (Resources.getSystem().getDisplayMetrics().widthPixels/1.9);
            //image
            Integer id = View.generateViewId();
            BitmapDrawable background = new BitmapDrawable(ImageUtil.convert(dress.getSlika()));
            image.setBackground(background);
            image.setId(id);
            image.setLayoutParams(parmsImage);
            image.setOnClickListener(onClickListener);

            imaggesIds.add(id);

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

    private View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int i = 0; i<imaggesIds.size(); i++){
                if(view.getId() == imaggesIds.get(i)){
                    Bundle args = new Bundle();
                    args.putSerializable("user", (Serializable) user);
                    args.putSerializable("dress", (Serializable) clothes.get(i));


                    ClothesFragment dressFragment = new ClothesFragment();
                    dressFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.fragment_container, dressFragment);
                    fragmentTransaction.commit();
                }
            }
        }
    };
}
