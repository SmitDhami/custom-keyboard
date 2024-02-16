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
import com.keyboardstyle.adapter.ThemesAdapter;
import com.keyboardstyle.util.Helper;
import com.keyboardstyle.util.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    public ArrayList<String> mColorPaletteForeground;
    LinearLayout iv_settings;
    LinearLayout btn_enable_keyboard;
    LinearLayout iv_themes;
    ImageView iv_status;
    TextView iv_status_txt;
    public static String[] samples;
    SessionManager sessionManager;
    String[] space_key_normal;
    String[] space_key_pressed;
    public ThemesAdapter themesAdapter;
    boolean selectKeyboard = false;

    PackageManager pm;
    String[] images_names;
    String[] key_normal;
    String[] key_pressed;
    int[] keysOpecity;
    int[] bgOpecity;


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);


        sessionManager = new SessionManager(this);
        iv_status = (ImageView) findViewById(R.id.iv_status);
        iv_settings = (LinearLayout) findViewById(R.id.iv_settings);
        iv_themes = (LinearLayout) findViewById(R.id.iv_themes);
        btn_enable_keyboard = (LinearLayout) findViewById(R.id.btn_enable_keyboard);
        iv_status_txt = (TextView) findViewById(R.id.iv_status_txt);
        btn_enable_keyboard.setOnClickListener(this);
        iv_settings.setOnClickListener(this);
        iv_themes.setOnClickListener(this);

        this.mColorPaletteForeground = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.foreground)));
        this.images_names = new String[]{"keyboard_bg1", "keyboard_bg2", "keyboard_bg3", "keyboard_bg4", "keyboard_bg5", "keyboard_bg6"};
        this.samples = new String[]{"sample_1", "sample_2", "sample_3", "sample_4", "sample_5", "sample_6"};
        this.key_normal = new String[]{"os_key_normal3", "os_key_normal3", "os_key_normal3", "os_key_normal3", "os_key_normal3", "os_key_normal3"};
        this.space_key_normal = new String[]{"os_space_key_normal3", "os_space_key_normal3", "os_space_key_normal3", "os_space_key_normal3", "os_space_key_normal3", "os_space_key_normal3"};
        this.key_pressed = new String[]{"os_key_pressed3", "os_key_pressed3", "os_key_pressed3", "os_key_pressed3", "os_key_pressed3", "os_key_pressed3"};
        this.space_key_pressed = new String[]{"os_space_key_pressed3", "os_space_key_pressed3", "os_space_key_pressed3", "os_space_key_pressed3", "os_space_key_pressed3", "os_space_key_pressed3"};
        this.keysOpecity = new int[]{250, 250, 250, 250, 250, 250};
        this.bgOpecity = new int[]{230, 230, 230, 230, 230, 230};


        sessionManager.setBackgroundColor(images_names[0]);
        sessionManager.setSample(samples[0]);
        sessionManager.setKeyNormal(key_normal[0]);
        sessionManager.setKeyPressed(key_pressed[0]);
        sessionManager.setSpaceKeyNormal(space_key_normal[0]);
        sessionManager.setSpaceKeyPressed(space_key_pressed[0]);
        sessionManager.setForegroundColor((String) mColorPaletteForeground.get(0));
        sessionManager.setBackgroundOpecity(bgOpecity[0]);
        sessionManager.setForgroundOpecity(keysOpecity[0]);


        this.pm = getPackageManager();
    }


    public void onClick(View view) {
        int vhgv = view.getId();
//        if (vhgv != R.id.iv_settings) {
        switch (vhgv) {
            case R.id.btn_enable_keyboard:
                showInputMethodPicker();
                return;
            case R.id.iv_themes:
                adsKeyboadr_theme();
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

        startActivity(new Intent(this, SettingsActivity.class));
        Helper.isFromKeyboardSetting = Boolean.FALSE;
    }


    private void adsKeyboadr_theme() {
        startActivity(new Intent(this, ThemesActivity.class));
        Helper.isFromKeyboardSetting = Boolean.FALSE;
    }

    protected void onResume() {
        super.onResume();
        if (((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).getEnabledInputMethodList().toString().contains(getPackageName())) {
            iv_status.setImageResource(R.drawable.btn_disable_keyboard);
            iv_status_txt.setText(R.string.disable);
            return;
        }
        iv_status.setImageResource(R.drawable.btn_enable_keyboard);
        iv_status_txt.setText(R.string.enable);
        this.sessionManager.setFirstRun(Boolean.valueOf(true));
        startActivity(new Intent(this, FirstInstallActivity.class));
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
                    MainActivity.this.iv_status.setImageResource(R.drawable.btn_disable_keyboard);
                    iv_status_txt.setText(R.string.disable);
                    return;
                }
                Log.d("bbbbbbbbbb", "False");
                MainActivity.this.iv_status.setImageResource(R.drawable.btn_enable_keyboard);
                iv_status_txt.setText(R.string.enable);
            }
        }, 100);
    }

    public boolean isInputMethodEnabled() {
        return Secure.getString(getContentResolver(), "default_input_method").contains(getPackageName());
    }


    public void onBackPressed()  {
        finishAffinity();
    }

}
