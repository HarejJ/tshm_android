package com.example.nejc.tshm;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class AboutProjectFragment extends Fragment {


    public AboutProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String text = "V povprečju vsak Slovenec letno zavrže 14 kg oblačil. "
                    +"Ta oblačila končajo na deponijah, kjer razpadajo več kot 100 let in "
                    +"z nevarnimi kemikalijami onesnažujejo okolje. Vse to pa je zgolj začetek "
                    +"začaranega kroga hitre mode. S kemikalijami so prvi v stiku delavci v "
                    +"tekstilnih tovarnah, nato pa jih skupaj s svojimi cenenimi oblačili "
                    +"oblečeš še ti.  Dokazano je, da nekatere kemikalije povzročajo neplodnost, "
                    +"motnje v imunskem sistemu in razvoj rakavih obolenj. Z nakupom oblačil hitre"
                    +" mode tako ne ogrožaš le svojega zdravja, temveč glasuješ za suženjski odnos "
                    + "med tekstilnimi multinacionalkami in delavci v azijskih potilnicah "
                    +"(»sweatshop«). Najpomembneje je, da oblačila kupuješ premišljeno! "
                    +"Več o problematiki hitre mode si lahko prebereš na naši spletni strani: " +
                     "<a href=\"http://www.kabine-sherinjon.si\">kabine-sherinjon.si</a>";

        View view = inflater.inflate(R.layout.fragment_about_project, container, false);
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.setBackgroundColor(Color.TRANSPARENT);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        webView.loadData("<p style =\"text-align:justify;font-size: 18px;color:white\">"+text+"</p>","text/html; charset=utf-8", "utf-8");

        return view;
    }

}
