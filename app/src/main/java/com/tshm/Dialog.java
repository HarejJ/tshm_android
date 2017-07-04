package com.tshm;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tshm.R;


public class Dialog {

    public static AlertDialog networkErrorDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String title = context.getString(R.string.dialog_title0);
        String content = context.getString(R.string.dialog_content0);
        String button0 = context.getString(R.string.dialog_button0);

        builder.setTitle(title);
        builder.setMessage(content);
        builder.setPositiveButton(button0, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog reservationApprovalDialog(Context context) {

        String title = context.getString(R.string.dialog_title1);
        String content = context.getString(R.string.dialog_content1);
        String button0 = context.getString(R.string.dialog_button0);
        String button1 = context.getString(R.string.dialog_button1);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(title(context, title));
        builder.setView(dialogMessage(context, content));
        builder.setPositiveButton(button0, null);
        builder.setNegativeButton(button1, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog reservationRefusalDialog(Context context) {

        String title = context.getString(R.string.dialog_title2);
        String content = context.getString(R.string.dialog_content2);
        String button0 = context.getString(R.string.dialog_button0);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context, content));
        builder.setCustomTitle(title(context, title));
        builder.setPositiveButton(button0, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog pozabljenoGeslo(Context context) {

        String title = context.getString(R.string.dialog_title3);
        String content = context.getString(R.string.dialog_content3);
        String button0 = context.getString(R.string.dialog_button0);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context, content));
        builder.setCustomTitle(title(context, title));
        builder.setPositiveButton(button0, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }


    public static AlertDialog pogojiSodelovanja(final Context context, final RegistrationActivity activity) {

        String title = context.getString(R.string.dialog_title4);
        //String content = context.getString(R.string.dialog_content4);
        String content = "Tvoje osebne podatke (uporabniško ime, elektronski naslov, kraj bivanja in telefonsko številko)\nbo lahko videl le tisti s katerim boš izvedel/izvedla izmenjavo, ostalim uporabnikom je vidno samo tvoje\nuporabniško ime. Po vnosu resničnih podatkov je potrebno priložiti fotografijo.\nLahko si jo izbereš iz galerije ali narediš sveži 'selfi'.\nVeč o pogojih sodelovanja\nsi lahko prebereš na naši <a href=\"http://www.kabine-sherinjon.si/wp-content/uploads/2017/06/Pogoji-uporabe_thsm.pdf\">spletni strani</a>.";
        String button2 = context.getString(R.string.dialog_button2);

        Spanned myMessage = Html.fromHtml(content);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage1(context, myMessage, true));
        builder.setCustomTitle(title(context, title));
        builder.setPositiveButton(button2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (NetworkUtils.isNetworkConnected(context))
                    Dialog.vnesiSliko(context, activity).show();
                else
                    Dialog.networkErrorDialog(context).show();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog vnesiSliko(final Context context, final RegistrationActivity activity) {

        String title = context.getString(R.string.dialog_title5);
        String content = context.getString(R.string.dialog_content5);
        String button3 = context.getString(R.string.dialog_button3);
        String button4 = context.getString(R.string.dialog_button4);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context, content));
        builder.setCustomTitle(title(context, title));
        builder.setNegativeButton(button3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (NetworkUtils.isNetworkConnected(context))
                    activity.dispatchTakePictureIntent();
                else
                    Dialog.networkErrorDialog(context).show();
            }
        });
        builder.setPositiveButton(button4, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (NetworkUtils.isNetworkConnected(context))
                    activity.openGallery();
                else
                    Dialog.networkErrorDialog(context).show();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog vnesiSliko(final Context context, final MainActivity activity) {

        String title = context.getString(R.string.dialog_title5);
        String content = context.getString(R.string.dialog_content5);
        String button3 = context.getString(R.string.dialog_button3);
        String button4 = context.getString(R.string.dialog_button4);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context, content));
        builder.setCustomTitle(title(context, title));
        builder.setNegativeButton(button3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (NetworkUtils.isNetworkConnected(context))
                    activity.dispatchTakePictureIntent();
                else
                    Dialog.networkErrorDialog(context).show();
            }
        });
        builder.setPositiveButton(button4, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (NetworkUtils.isNetworkConnected(context))
                    activity.openGallery();
                else
                    Dialog.networkErrorDialog(context).show();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog imetnikOblacila(Context context, String[] user) {

        String title = context.getString(R.string.dialog_title10);
        String button0 = context.getString(R.string.dialog_button0);

        String str = "Uporabnik: " + user[0] + "\nmail: " + user[1] + "\nštevilka: " + user[2] + "\nlokacija: " + user[3];
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context, str));
        builder.setCustomTitle(title(context, title));
        builder.setPositiveButton(button0, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog dopolniProfil(Context context) {

        String title = context.getString(R.string.dialog_title6);
        String content = context.getString(R.string.dialog_content6);
        String button0 = context.getString(R.string.dialog_button0);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage(context, content));
        builder.setCustomTitle(title(context, title));
        builder.setPositiveButton(button0, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog VrniOblacilo(Context context) {

        String title = context.getString(R.string.dialog_title7);
        String content = context.getString(R.string.dialog_content7);
        String button0 = context.getString(R.string.dialog_button0);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage1(context, content));
        builder.setCustomTitle(title(context, title));
        builder.setPositiveButton(button0, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog VrniOblaciloNujno(Context context) {

        String title = context.getString(R.string.dialog_title7);
        String content = context.getString(R.string.dialog_content7);
        String button0 = context.getString(R.string.dialog_button0);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogMessage1(context, content));
        builder.setCustomTitle(title(context, title));
        builder.setPositiveButton(button0, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }


    private static ScrollView dialogMessage(Context context, String titleText) {
        ScrollView s_view = new ScrollView(context);
        TextView t_view = new TextView(context);
        t_view.setText(titleText);
        t_view.setPadding(60, 60, 60, 60);
        t_view.setTextSize(16);
        s_view.addView(t_view);
        return s_view;
    }

    private static ScrollView dialogMessage1(Context context, String titleText) {
        ScrollView s_view = new ScrollView(context);
        TextView t_view = new TextView(context);
        t_view.setText(titleText);
        t_view.setPadding(60, 60, 60, 60);
        t_view.setTextSize(18);
        s_view.addView(t_view);
        return s_view;
    }

    private static ScrollView dialogMessage1(Context context, Spanned titleText, boolean link) {
        ScrollView s_view = new ScrollView(context);
        TextView t_view = new TextView(context);

        t_view.setText(titleText);
        t_view.setPadding(60, 60, 60, 60);
        t_view.setTextSize(18);
        t_view.setMovementMethod(LinkMovementMethod.getInstance());
        s_view.addView(t_view);
        return s_view;
    }

    private static TextView title(Context context, String titleText) {
        TextView title = new TextView(context);
        title.setText(titleText);
        title.setPadding(60, 60, 10, 10);
        title.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        title.setTextSize(28);
        return title;
    }
}
