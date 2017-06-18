package com.example.nejc.tshm;

import android.content.Context;
import android.content.DialogInterface;
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

    public static AlertDialog starostPremajhna(Context context){
        String str = "Aplikacijo lahko uporabljajo osebe ki so starejše od 16 let. Če želite uporablja";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context,str));
        builder.setCustomTitle(title(context,"OPOZORILO"));
        builder.setPositiveButton("VREDU",null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog pozabljenoGeslo(Context context){
        String str = "če ste pozabili geslo kontakdirajte odgovorne na mail:\nkabine.sherinjon@gmail.com";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context,str));
        builder.setCustomTitle(title(context,"Pozabljeno geslo"));
        builder.setPositiveButton("VREDU",null);
        AlertDialog dialog = builder.create();
        return dialog;
    }


    public static AlertDialog pogojiSodelovanja(final Context context, final RegistrationActivity activity){
        String str = "Tvoje osebne podatke (uporabniško ime, e-mail, kraj bivanja npr. Ljubljana " +
                "in telefonsko številko) bo lahko videl le tisti s katerim boš izvedel/izvedla " +
                "izmenjavo. V kolikor imaš izposojen katerikoli kos, bo vsem ostalim uporabnikom " +
                "vidno samo tvoje uporabniško ime. Po vnosu realnih podatkov je potrebno priložiti " +
                "sliko. Lahko si jo izbereš iz galerije ali narediš sveži ''selfi''.";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context,str));
        builder.setCustomTitle(title(context,"Pogoji sodelovanja"));
        builder.setPositiveButton("razumem in sprejmem pogoje", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(NetworkUtils.isNetworkConnected(context))
                    Dialog.vnesiSliko(context,activity).show();
                else
                    Dialog.networkErrorDialog(context).show();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog vnesiSliko(final Context context, final RegistrationActivity activity){
        String str = "iyberi način vnosa profilne slike.";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context,str));
        builder.setCustomTitle(title(context,"Profilna slika"));
        builder.setNegativeButton("selfi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(NetworkUtils.isNetworkConnected(context))
                    activity.dispatchTakePictureIntent();
                else
                    Dialog.networkErrorDialog(context).show();
            }
        });
        builder.setPositiveButton("iz galerije", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(NetworkUtils.isNetworkConnected(context))
                    activity.openGallery();
                else
                    Dialog.networkErrorDialog(context).show();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog imetnikOblacila(Context context,String[]user){
        String str = "Uporabnik: "+user[0]+"\nmail: "+user[1]+"\nštevilka: "+user[2]+"\nlokacija: "+user[3];
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context,str));
        builder.setCustomTitle(title(context,"Kontakti uporabnika"));
        builder.setPositiveButton("VREDU",null);
        AlertDialog dialog = builder.create();
        return dialog;
    }



    private static ScrollView dialogMessage(Context context, String titleText){
        ScrollView s_view = new ScrollView(context);
        TextView t_view = new TextView(context);
        t_view.setText(titleText);
        t_view.setPadding(60, 60, 60, 60);
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
