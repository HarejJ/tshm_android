package com.tshm;


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
        String text = "Oblačila spremljajo naš vsakdan, česar se zavedajo tudi podjetja, ki te prepričujejo, da je sreča pogojena z nenehnim kupovanjem novih.<br/><br/>Proizvodnja tako imenovane »hitre mode« negativno učinkuje na okolje - v tretjem svetu s pridelovanjem surovin, z uporabo nevarnih (v EU in ZDA prepovedanih) kemikalij, v razvitih državah pa z odpadki, ko oblačila po kratkem času zavržeš.<br/><br/>Oblačila hitre mode slabo vplivajo na tvoje zdravje, saj lahko vsebujejo škodljive snovi, za katere je dokazano, da slabijo hormonsko ravnovesje, imunski sistem in pospešujejo nastanek novotvorb.<br/><br/>Z nakupom teh oblačil pa ne ogrožaš le svojega zdravja, temveč hkrati podpiraš suženjski odnos med tekstilnimi podjetji in delavci v azijskih potilnicah (»sweatshop«).<br/><br/>Najpomembneje je, da oblačila kupuješ premišljeno! Več o problematiki hitre mode si lahko prebereš na naši spletni strani: <a href=\"http://www.kabine-sherinjon.si\">kabine-sherinjon.si</a>.";
        //String text = getString(R.string.o_hitri_modi_content1);

        View view = inflater.inflate(R.layout.fragment_about_project, container, false);
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.setBackgroundColor(Color.TRANSPARENT);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        webView.loadData("<p style =\"text-align:justify;font-size: 18px;color:white\">" + text + "</p>", "text/html; charset=utf-8", "utf-8");

        return view;
    }

}
