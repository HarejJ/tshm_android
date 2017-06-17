package com.example.nejc.tshm;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImagePanningFragment extends Fragment {

    private Bitmap picture;

    public ImagePanningFragment() {

        setPictureSource(null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        TouchImageView img = new TouchImageView(this.getContext());
        img.setImageBitmap(picture);
        img.setMaxZoom(4f);
        return img;
    }


    public void setPictureSource(Bitmap picture) {

        this.picture = picture;
    }
}
