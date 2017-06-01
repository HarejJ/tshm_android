package com.example.nejc.tshm;

import android.content.Context;
import android.support.v7.app.AlertDialog;



public class Dialog {

    public static AlertDialog networkErrorDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Težave pri povezavi");
        builder.setMessage("Preverite ali imate internetno povezavo");
        builder.setPositiveButton("OK",null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
