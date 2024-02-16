package com.keyboardstyle.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.keyboardstyle.R;

public class Splesh_screen extends AppCompatActivity {

    public static int splashAds = 0;
    public static String addtype = "admob";
    public static String main_ads_type = "admob";
    public static int exit = 0;
    public static int increment = 1;
    public static int counter = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh_screen);
        startActivity(new Intent(Splesh_screen.this,MainActivity.class));
    }
}
