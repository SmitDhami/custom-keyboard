package com.keyboardstyle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.keyboardstyle.R;

public class MainSplash extends AppCompatActivity {


    Button retry_btn;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash);

        retry_btn = (Button) findViewById(R.id.retry_btn);
        layout = (RelativeLayout) findViewById(R.id.layout);
        CheckInternet();
    }

    private void CheckInternet() {

        layout.setVisibility(View.VISIBLE);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            retry_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layout.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CheckInternet();
                        }
                    }, 200);
                }
            });
        } else {
            layout.setVisibility(View.GONE);
            startActivity(new Intent(MainSplash.this, Splesh_screen.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {

    }
}