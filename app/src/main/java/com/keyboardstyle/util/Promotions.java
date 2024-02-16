package com.keyboardstyle.util;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Promotions {
    public static String serverURL = "http://applicanic.com/promotions/api/v1/index.php?action=interstitial";
    private String baseUrl = "http://applicanic.com/promotions/api/v1/";
    private Context mContext;
    private RequestQueue mRequestQueue;
    HashMap<String, String> promotionalAds = new HashMap();
    private SessionManager sessionManager;

    public Promotions(Context context) {
        this.mContext = context;
        this.sessionManager = new SessionManager(context);
    }

    public boolean getCustomInterstitialAds() {
        this.mRequestQueue = Volley.newRequestQueue(this.mContext);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.baseUrl);
        stringBuilder.append("index.php?action=interstitial");
        this.mRequestQueue.add(new JsonObjectRequest(0, stringBuilder.toString(), null, new Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    if (Boolean.valueOf(jSONObject.getBoolean("status")).booleanValue()) {
                        ArrayList arrayList = new ArrayList();
                        jSONObject = jSONObject.getJSONObject("data");
                        arrayList.add(jSONObject.getString("url"));
                        arrayList.add(jSONObject.getString("image"));
                        Promotions.this.sessionManager.saveArray(arrayList, "CUSTOM_INTERSTITIAL");
                        Promotions.this.sessionManager.setRequestCompleted(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onErrorResponse: ");
                stringBuilder.append(volleyError);
                Log.d("Volley", stringBuilder.toString());
                Promotions.this.sessionManager.setRequestCompleted(false);
            }
        }));
        return this.sessionManager.isRequestCompleted();
    }

    boolean getPromotionalAds() {
        this.mRequestQueue = Volley.newRequestQueue(this.mContext);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.baseUrl);
        stringBuilder.append("index.php?action=promotional");
        this.mRequestQueue.add(new JsonObjectRequest(0, stringBuilder.toString(), null, new Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    if (Boolean.valueOf(jSONObject.getBoolean("status")).booleanValue()) {
                        JSONArray jsonArray = jSONObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jSONObject2 = (JSONObject) jsonArray.get(i);
                            Promotions.this.promotionalAds.put(jSONObject2.getString("url"), jSONObject2.getString("image"));
                        }
                        Promotions.this.sessionManager.saveMap(Promotions.this.promotionalAds, "PROMOTIONAL_ADS");
                        Promotions.this.sessionManager.setRequestCompleted(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onErrorResponse: ");
                stringBuilder.append(volleyError);
                Log.d("Volley", stringBuilder.toString());
                Promotions.this.sessionManager.setRequestCompleted(false);
            }
        }));
        return this.sessionManager.isRequestCompleted();
    }
}
