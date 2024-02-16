package com.keyboardstyle.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    private static final String AUTO_CAPITALIZE_KEY = "auto_capitalize";
    private static final String BACKGROUND_COLOR = "background_color";
    private static final String BACKGROUND_IMAGE = "background_image";
    private static final String BACKGROUND_OPECITY = "background_opecity";
    private static final String ENABLE_KEY = "enable";
    private static final String FOREGROUND_COLOR = "foreground_color";
    private static final String FORGROUND_OPECITY = "forground_opecity";
    private static final String KEY_NORMAL = "os_key_normal1";
    private static final String KEY_PRESSED = "space_key_normal";
    private static final String OPEN_COUNT = "open_count";
    private static final String POPUP_KEY = "popup";
    private static final String PREFS_NAME = "keyboard_prefs";
    private static final String SAMPLE_IMAGE = "sample_image";
    private static final String SOUND_KEY = "sound";
    private static final String SPACE_KEY_NORMAL = "os_key_pressed1";
    private static final String SPACE_KEY_PRESSED = "space_key_pressed";
    private static final String SPEECH_KEY = "speech";
    private static final String TRANSLITERATION_KEY = "transliteration";
    private static final String TRANSLITERATION_OFF_COLOR = "transliteration_off_color";
    private static final String TRANSLITERATION_ON_COLOR = "transliteration_on_color";
    private static final String VIBRATION_KEY = "vibration";
    private Editor editor;
    public boolean isRequestCompleted = false;
    private SharedPreferences mPrefs;
    private Boolean removeAds;
    private String removeAdsKey = "REMOVE_ADS";

    public SessionManager(Context context) {
        this.mPrefs = context.getSharedPreferences(PREFS_NAME, 0);
        this.editor = this.mPrefs.edit();
    }

    public Boolean isAutoCapitalize() {
        return Boolean.valueOf(this.mPrefs.getBoolean(AUTO_CAPITALIZE_KEY, true));
    }

    public void setAutoCapitalize(Boolean bool) {
        this.editor.putBoolean(AUTO_CAPITALIZE_KEY, bool.booleanValue());
        this.editor.apply();
    }

    public Boolean getSound() {
        return Boolean.valueOf(this.mPrefs.getBoolean(SOUND_KEY, false));
    }

    public void setSound(Boolean bool) {
        this.editor.putBoolean(SOUND_KEY, bool.booleanValue());
        this.editor.apply();
    }

    public Boolean getSpeech() {
        return Boolean.valueOf(this.mPrefs.getBoolean(SPEECH_KEY, false));
    }

    public void setSpeech(Boolean bool) {
        this.editor.putBoolean(SPEECH_KEY, bool.booleanValue());
        this.editor.apply();
    }

    public Boolean getTransliteration() {
        return Boolean.valueOf(this.mPrefs.getBoolean(TRANSLITERATION_KEY, true));
    }

    public void setTransliteration(Boolean bool) {
        this.editor.putBoolean(TRANSLITERATION_KEY, bool.booleanValue());
        this.editor.apply();
    }

    public Boolean getVibration() {
        return Boolean.valueOf(this.mPrefs.getBoolean(VIBRATION_KEY, false));
    }

    public void setVibration(Boolean bool) {
        this.editor.putBoolean(VIBRATION_KEY, bool.booleanValue());
        this.editor.apply();
    }

    public Boolean getPopup() {
        return Boolean.valueOf(this.mPrefs.getBoolean(POPUP_KEY, false));
    }

    public void setPopup(Boolean bool) {
        this.editor.putBoolean(POPUP_KEY, bool.booleanValue());
        this.editor.apply();
    }

    public String getBackgroundColor() {
        return this.mPrefs.getString(BACKGROUND_COLOR, "keyboard_bg1");
    }

    public void setBackgroundColor(String str) {
        this.editor.putString(BACKGROUND_COLOR, str);
        this.editor.apply();
    }

    public int getBackgroundOpecity() {
        return this.mPrefs.getInt(BACKGROUND_OPECITY, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    public void setBackgroundOpecity(int i) {
        this.editor.putInt(BACKGROUND_OPECITY, i);
        this.editor.apply();
    }

    public int getForgroundOpecity() {
        return this.mPrefs.getInt(FORGROUND_OPECITY, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    public void setForgroundOpecity(int i) {
        this.editor.putInt(FORGROUND_OPECITY, i);
        this.editor.apply();
    }

    public String getBackgroundImage() {
        return this.mPrefs.getString(BACKGROUND_IMAGE, "keyboard_bg1");
    }

    public void setBackgroundImage(String str) {
        this.editor.putString(BACKGROUND_IMAGE, str);
        this.editor.apply();
    }

    public String getSample() {
        return this.mPrefs.getString(SAMPLE_IMAGE, "sample1");
    }

    public void setSample(String str) {
        this.editor.putString(SAMPLE_IMAGE, str);
        this.editor.apply();
    }

    public String getKeyNormal() {
        return this.mPrefs.getString(KEY_NORMAL, KEY_NORMAL);
    }

    public void setKeyNormal(String str) {
        this.editor.putString(KEY_NORMAL, str);
        this.editor.apply();
    }

    public String getKeyPressed() {
        return this.mPrefs.getString(KEY_PRESSED, SPACE_KEY_NORMAL);
    }

    public void setKeyPressed(String str) {
        this.editor.putString(KEY_PRESSED, str);
        this.editor.apply();
    }

    public String getSpaceKeyNormal() {
        return this.mPrefs.getString(SPACE_KEY_NORMAL, KEY_NORMAL);
    }

    public void setSpaceKeyNormal(String str) {
        this.editor.putString(SPACE_KEY_NORMAL, str);
        this.editor.apply();
    }

    public String getSpaceKeyPressed() {
        return this.mPrefs.getString(SPACE_KEY_PRESSED, KEY_NORMAL);
    }

    public void setSpaceKeyPressed(String str) {
        this.editor.putString(SPACE_KEY_PRESSED, str);
        this.editor.apply();
    }

    public String getForegroundColor() {
        return this.mPrefs.getString(FOREGROUND_COLOR, "#000000");
    }

    public void setForegroundColor(String str) {
        this.editor.putString(FOREGROUND_COLOR, str);
        this.editor.apply();
    }

    public String getBtnOnForegroundColor() {
        return this.mPrefs.getString(TRANSLITERATION_ON_COLOR, "#FFFFFF");
    }

    public void setBtnOnForegroundColor(String str) {
        this.editor.putString(TRANSLITERATION_ON_COLOR, str);
        this.editor.apply();
    }

    public String getBtnOffForegroundColor() {
        return this.mPrefs.getString(TRANSLITERATION_OFF_COLOR, "#F94541");
    }

    public void setBtnOffForegroundColor(String str) {
        this.editor.putString(TRANSLITERATION_OFF_COLOR, str);
        this.editor.apply();
    }

    public int getOpenCount() {
        return this.mPrefs.getInt(OPEN_COUNT, 0);
    }

    public void setOpenCount(int i) {
        this.editor.putInt(OPEN_COUNT, i);
        this.editor.apply();
    }

    public Boolean getFirstRun() {
        return Boolean.valueOf(this.mPrefs.getBoolean(ENABLE_KEY, false));
    }

    public void setFirstRun(Boolean bool) {
        this.editor.putBoolean(ENABLE_KEY, bool.booleanValue());
        this.editor.apply();
    }

    public Boolean getRemoveAds() {
        this.removeAds = Boolean.valueOf(this.mPrefs.getBoolean(this.removeAdsKey, false));
        return this.removeAds;
    }

    public void setRemoveAds(Boolean bool) {
        this.removeAds = bool;
        this.editor.putBoolean(this.removeAdsKey, bool.booleanValue());
        this.editor.apply();
    }

    public boolean saveArray(ArrayList<String> arrayList, String str) {
        Editor edit = this.mPrefs.edit();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("_size");
        edit.putInt(stringBuilder.toString(), arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append("_");
            stringBuilder2.append(i);
            edit.putString(stringBuilder2.toString(), (String) arrayList.get(i));
        }
        return edit.commit();
    }

    public void setRequestCompleted(boolean z) {
        Editor edit = this.mPrefs.edit();
        this.isRequestCompleted = z;
        edit.putBoolean("IS_REQUEST_COMPLETED", this.isRequestCompleted);
        edit.apply();
    }

    public void saveMap(Map<String, String> map, String str) {
        if (this.mPrefs != null) {
            String string = new JSONObject(map).toString();
            Editor edit = this.mPrefs.edit();
            edit.putString(str, string);
            edit.apply();
        }
    }

    public Map<String, String> loadMap(String str) {
        Map<String, String> hashMap = new HashMap();
        try {
            if (this.mPrefs != null) {
                JSONObject jSONObject = new JSONObject(this.mPrefs.getString(str, new JSONObject().toString()));
                Iterator<String> stringIterator = jSONObject.keys();
                while (stringIterator.hasNext()) {
                    String str2 = (String) stringIterator.next();
                    hashMap.put(str2, (String) jSONObject.get(str2));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public boolean isRequestCompleted() {
        return this.mPrefs.getBoolean("IS_REQUEST_COMPLETED", false);
    }

    public ArrayList<String> loadArray(String str) {
        SharedPreferences sharedPreferences = this.mPrefs;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("_size");
        int i = 0;
        int i2 = sharedPreferences.getInt(stringBuilder.toString(), 0);
        ArrayList<String> arrayList = new ArrayList();
        while (i < i2) {
            SharedPreferences sharedPreferences2 = this.mPrefs;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append("_");
            stringBuilder2.append(i);
            arrayList.add(sharedPreferences2.getString(stringBuilder2.toString(), null));
            i++;
        }
        return arrayList;
    }
}
