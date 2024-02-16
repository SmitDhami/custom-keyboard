package com.keyboardstyle.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keyboardstyle.R;
import com.keyboardstyle.util.Helper;
import com.keyboardstyle.util.SessionManager;
import com.squareup.picasso.Target;
import static com.keyboardstyle.activity.Splesh_screen.addtype;
import static com.keyboardstyle.activity.Splesh_screen.exit;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    LinearLayout iv_settings;
    LinearLayout btn_enable_keyboard;


    TextView iv_status_txt;

    PackageManager pm;
    SessionManager sessionManager;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);

        sessionManager = new SessionManager(this);

        iv_settings = (LinearLayout) findViewById(R.id.iv_settings);
        btn_enable_keyboard = (LinearLayout) findViewById(R.id.btn_enable_keyboard);
        iv_status_txt = (TextView) findViewById(R.id.iv_status_txt);
        btn_enable_keyboard.setOnClickListener(this);
        iv_settings.setOnClickListener(this);

        this.pm = getPackageManager();
    }


    public void onClick(View view) {
        int vhgv = view.getId();
//        if (vhgv != R.id.iv_settings) {
        switch (vhgv) {
            case R.id.btn_enable_keyboard:
                showInputMethodPicker();
                return;
            case R.id.iv_settings:
                checkads_settings();
                return;
            default:
                return;
        }
//        }
    }

    private void checkads_settings() {

        startActivity(new Intent(this, com.keyboardstyle.activity.SettingsActivity.class));
        Helper.isFromKeyboardSetting = Boolean.FALSE;
    }

    protected void onResume() {
        super.onResume();
        if (((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).getEnabledInputMethodList().toString().contains(getPackageName())) {

            iv_status_txt.setText(R.string.disable);
            return;
        }

        iv_status_txt.setText(R.string.enable);
        this.sessionManager.setFirstRun(Boolean.valueOf(true));
        startActivity(new Intent(this, com.keyboardstyle.activity.FirstInstallActivity.class));
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
                Log.d("bbbbbbbbbb", ComponentName.unflattenFromString(Secure.getString(MainActivity.this.getContentResolver(), "default_input_method")).getPackageName());
                if (MainActivity.this.isInputMethodEnabled()) {
                    Log.d("bbbbbbbbbb", "True");

                    iv_status_txt.setText(R.string.disable);
                    return;
                }
                Log.d("bbbbbbbbbb", "False");

                iv_status_txt.setText(R.string.enable);
            }
        }, 100);
    }

    public boolean isInputMethodEnabled() {
        return Secure.getString(getContentResolver(), "default_input_method").contains(getPackageName());
    }

    public void onBackPressed() {
        AlertDialog.Builder alertdialogbox = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dailog_backpress, null);
        alertdialogbox.setView(view);
        final AlertDialog alertDialog = alertdialogbox.create();
        Button btnyes = (Button) view.findViewById(R.id.yesbtn);
        Button btnno = (Button) view.findViewById(R.id.nobtn);

        btnno.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnyes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAds();
            }
        });

        if (!isFinishing()) {
            alertDialog.show();
        }
    }

    private void exitAds() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, com.keyboardstyle.activity.ExitActivity.class));
            }
        }, 1000);
    }

}
