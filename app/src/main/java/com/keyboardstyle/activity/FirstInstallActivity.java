package com.keyboardstyle.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView.ScaleType;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.keyboardstyle.R;
import com.keyboardstyle.util.SessionManager;

import java.util.HashMap;


public class FirstInstallActivity extends AppCompatActivity implements OnClickListener {
    Boolean isChoosed = Boolean.valueOf(false);
    TextView iv_enable;
    TextView iv_select;
    LinearLayout ll_enable;
    LinearLayout ll_select;
    SessionManager sessionManager;


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_firstinstall);
        this.sessionManager = new SessionManager(this);

        HashMap hashMap = new HashMap();
        hashMap.put("Professional Resume", "file:///android_asset/a.png");
        hashMap.put("Save your time and effort", "file:///android_asset/b.png");
        hashMap.put("Ace a job interview", "file:///android_asset/c.png");
        hashMap.put("Ace a job interview", "file:///android_asset/d.png");
        for (Object str : hashMap.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.image((String) hashMap.get(str)).setScaleType(ScaleType.Fit);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", String.valueOf(str));
//            this.mDemoSlider.addSlider(textSliderView);
        }

        iv_enable = (TextView) findViewById(R.id.iv_enable);
        iv_select = (TextView) findViewById(R.id.iv_select);
        ll_enable = (LinearLayout) findViewById(R.id.ll_enable);
        ll_select = (LinearLayout) findViewById(R.id.ll_select);
        iv_enable.setOnClickListener(this);
        iv_select.setOnClickListener(this);

        // show Native Ads
//        loadNativeAd();


    }

    protected void onResume() {
        super.onResume();
        if (((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).getEnabledInputMethodList().toString().contains(getPackageName())) {
            return;
        }
    }

    public void onClick(View view) {
        int fg = view.getId();
        if (fg == R.id.iv_enable) {
            enableInputMethod();
        } else if (fg == R.id.iv_select) {
            showInputMethodPicker();
        }
    }

    private void enableInputMethod() {

        startActivityForResult(new Intent("android.settings.INPUT_METHOD_SETTINGS"), 0);
    }


    private void showInputMethodPicker() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showInputMethodPicker();
        }
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Log.d("bbbbbbbbbb", ComponentName.unflattenFromString(Secure.getString(FirstInstallActivity.this.getContentResolver(), "default_input_method")).getPackageName());
                if (FirstInstallActivity.this.isInputMethodEnabled()) {
                    Log.d("bbbbbbbbbb", "True");
                    FirstInstallActivity.this.isChoosed = Boolean.valueOf(true);

                    startActivity(new Intent(FirstInstallActivity.this, MainActivity.class));

                    sessionManager.setFirstRun(Boolean.valueOf(false));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                    return;
                }
                Log.d("bbbbbbbbbb", "False");
                FirstInstallActivity.this.isChoosed = Boolean.valueOf(false);
            }
        }, 100);
    }


    public boolean isInputMethodEnabled() {
        return Secure.getString(getContentResolver(), "default_input_method").contains(getPackageName());
    }
}
