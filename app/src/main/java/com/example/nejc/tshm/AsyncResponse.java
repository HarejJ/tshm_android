package com.example.nejc.tshm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(ArrayList<Dress> output);
    void clothesReserved(Dress output,boolean[] user);
    void deleteReservation();
    void sprejemRezervacije();
    void predajaNaprej();
}
