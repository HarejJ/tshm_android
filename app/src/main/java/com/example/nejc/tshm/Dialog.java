package com.example.nejc.tshm;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.ScrollView;
import android.widget.TextView;


public class Dialog {

    public static AlertDialog networkErrorDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Težave pri povezavi");
        builder.setMessage("Preverite ali imate internetno povezavo");
        builder.setPositiveButton("OK",null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog reservationApprovalDialog(Context context){
        String str = "Za potrditev rezervacije tega kosa prosim pritisnite VREDU. Trenutno stanje tvoje rezervacije  in podatek o trenutnem imetniku  lahko spremljaš na svojem profilu";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(title(context,"Potrditev rezrvacije"));
        builder.setView(dialogMessage(context,str));
        builder.setPositiveButton("VREDU",null);
        builder.setNegativeButton("NAZAJ",null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
    public static AlertDialog reservationRefusalDialog(Context context){
        String str = "Žal imaš rezerviran že nek drug kos.\nKer želimo zagotoviti čim večje kroženje oblačil je možna le 1 rezervacija na enkrat.";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context,str));
        builder.setCustomTitle(title(context,"Rezervacija ni mogoča"));
        builder.setPositiveButton("VREDU",null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private static ScrollView dialogMessage(Context context, String titleText){
        ScrollView s_view = new ScrollView(context);
        TextView t_view = new TextView(context);

        t_view.setText(titleText);
        t_view.setPadding(60, 60, 60, 60);
        //title.setTextColor(context.getResources().getColor(R.color.));
        t_view.setTextSize(22);
        s_view.addView(t_view);
        return s_view;
    }

    private static TextView title(Context context, String titleText){
        TextView title = new TextView(context);
        title.setText(titleText);
        title.setPadding(60, 60, 10, 10);
        title.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        title.setTextSize(28);
        return title;
    }
}
