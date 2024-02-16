package com.keyboardstyle.activity;

import android.os.Build.VERSION;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.keyboardstyle.R;
import com.keyboardstyle.util.SessionManager;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements OnClickListener {
    private int adCount = 0;
    ImageView checkBoxAutoCapitalize;
    ImageView checkBoxPopup;
    ImageView checkBoxSound;
    ImageView checkBoxVibrate;
    ImageView checkbox_speech;
    private int flag = 0;
    RelativeLayout rl_auto_capitalize;
    RelativeLayout rl_popup;
    RelativeLayout rl_sound;
    RelativeLayout rl_speech;
    RelativeLayout rl_vibrate;
    SessionManager sessionManager;
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_settings);

        this.sessionManager = new SessionManager(this);
        this.checkBoxAutoCapitalize = (ImageView) findViewById(R.id.checkbox_auto_capitalize);
        this.checkBoxPopup = (ImageView) findViewById(R.id.checkbox_popup);
        this.checkBoxSound = (ImageView) findViewById(R.id.checkbox_sound);
        this.checkbox_speech = (ImageView) findViewById(R.id.checkbox_speech);
        this.checkBoxVibrate = (ImageView) findViewById(R.id.checkbox_vibrate);
        this.rl_auto_capitalize = (RelativeLayout) findViewById(R.id.rl_auto_capitalize);
        this.rl_vibrate = (RelativeLayout) findViewById(R.id.rl_vibrate);
        this.rl_popup = (RelativeLayout) findViewById(R.id.rl_popup);
        this.rl_sound = (RelativeLayout) findViewById(R.id.rl_sound);
        this.rl_speech = (RelativeLayout) findViewById(R.id.rl_speech);
        init();
        this.rl_auto_capitalize.setOnClickListener(this);
        this.rl_vibrate.setOnClickListener(this);
        this.rl_popup.setOnClickListener(this);
        this.rl_sound.setOnClickListener(this);
        this.rl_speech.setOnClickListener(this);
    }

    public void onClick(View view) {
        int ff = view.getId();
        if (ff != R.id.rl_auto_capitalize) {
            switch (ff) {
                case R.id.rl_popup:
                    this.adCount += 1;
                    if (this.adCount % 2 == 0) {

                        this.adCount = 0;
                    }
                    if (this.sessionManager.getPopup().booleanValue()) {
                        this.sessionManager.setPopup(Boolean.valueOf(false));
                        this.checkBoxPopup.setImageResource(R.drawable.uncheck);
                        return;
                    }
                    this.sessionManager.setPopup(Boolean.valueOf(true));
                    this.checkBoxPopup.setImageResource(R.drawable.tick);
                    return;
                case R.id.rl_sound:
                    this.adCount += 1;
                    if (this.adCount % 2 == 0) {

                        this.adCount = 0;
                    }
                    if (this.sessionManager.getSound().booleanValue()) {
                        this.sessionManager.setSound(Boolean.valueOf(false));
                        this.checkBoxSound.setImageResource(R.drawable.uncheck);
                        return;
                    }
                    this.sessionManager.setSound(Boolean.valueOf(true));
                    this.checkBoxSound.setImageResource(R.drawable.tick);
                    return;
                case R.id.rl_speech:
                    if (VERSION.SDK_INT >= 21) {
                        new TedPermission(this).setPermissionListener(new PermissionListener() {
                            public void onPermissionDenied(ArrayList<String> arrayList) {
                            }

                            public void onPermissionGranted() {
                                SettingsActivity.this.adCount = SettingsActivity.this.adCount + 1;
                                if (SettingsActivity.this.adCount % 2 == 0) {

                                    SettingsActivity.this.adCount = 0;
                                }
                                if (SettingsActivity.this.sessionManager.getSpeech().booleanValue()) {
                                    SettingsActivity.this.sessionManager.setSpeech(Boolean.valueOf(false));
                                    SettingsActivity.this.checkbox_speech.setImageResource(R.drawable.uncheck);
                                    return;
                                }
                                SettingsActivity.this.sessionManager.setSpeech(Boolean.valueOf(true));
                                SettingsActivity.this.checkbox_speech.setImageResource(R.drawable.tick);
                            }
                        }).setPermissions("android.permission.RECORD_AUDIO").check();
                        return;
                    }
                    return;
                case R.id.rl_vibrate:
                    this.adCount += 1;
                    if (this.adCount % 2 == 0) {

                        this.adCount = 0;
                    }
                    if (this.sessionManager.getVibration().booleanValue()) {
                        this.sessionManager.setVibration(Boolean.valueOf(false));
                        this.checkBoxVibrate.setImageResource(R.drawable.uncheck);
                        return;
                    }
                    this.sessionManager.setVibration(Boolean.valueOf(true));
                    this.checkBoxVibrate.setImageResource(R.drawable.tick);
                    return;
                default:
                    return;
            }
        }
        this.adCount += 1;
        if (this.adCount % 2 == 0) {

            this.adCount = 0;
        }
        if (this.sessionManager.isAutoCapitalize().booleanValue()) {
            this.sessionManager.setAutoCapitalize(Boolean.valueOf(false));
            this.checkBoxAutoCapitalize.setImageResource(R.drawable.uncheck);
            return;
        }
        this.sessionManager.setAutoCapitalize(Boolean.valueOf(true));
        this.checkBoxAutoCapitalize.setImageResource(R.drawable.tick);
    }


    private void init() {
        if (this.sessionManager.isAutoCapitalize().booleanValue()) {
            this.checkBoxAutoCapitalize.setImageResource(R.drawable.tick);
        } else {
            this.checkBoxAutoCapitalize.setImageResource(R.drawable.uncheck);
        }
        if (this.sessionManager.getPopup().booleanValue()) {
            this.checkBoxPopup.setImageResource(R.drawable.tick);
        } else {
            this.checkBoxPopup.setImageResource(R.drawable.uncheck);
        }
        if (this.sessionManager.getSound().booleanValue()) {
            this.checkBoxSound.setImageResource(R.drawable.tick);
        } else {
            this.checkBoxSound.setImageResource(R.drawable.uncheck);
        }
        if (this.sessionManager.getVibration().booleanValue()) {
            this.checkBoxVibrate.setImageResource(R.drawable.tick);
        } else {
            this.checkBoxVibrate.setImageResource(R.drawable.uncheck);
        }
    }

    public void onBackPressed() {
        finish();
    }
}
