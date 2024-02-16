package com.keyboardstyle.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.keyboardstyle.R;
import com.keyboardstyle.adapter.ThemesAdapter;
import com.keyboardstyle.util.Helper;
import com.keyboardstyle.util.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class ThemesActivity extends AppCompatActivity {
    public int adCount = 0;
    int[] bgOpecity;
    private Bitmap bitmap;
    TextView btn_personlise;
    private File destination = null;
    String[] images_names;
    private String imgPath = null;
    private InputStream inputStreamImg;
    String[] key_normal;
    String[] key_pressed;
    int[] keysOpecity;
    private ListView list;
    public ArrayList<String> mColorPaletteForeground;
    Context mContext;

    public static String[] samples;
    SessionManager sessionManager;
    String[] space_key_normal;
    String[] space_key_pressed;
    public ThemesAdapter themesAdapter;
    boolean selectKeyboard = false;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_themes);
        this.mContext = this;

        this.mColorPaletteForeground = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.foreground)));
        this.images_names = new String[]{"keyboard_bg1", "keyboard_bg2", "keyboard_bg3", "keyboard_bg4", "keyboard_bg5", "keyboard_bg6"};
        this.samples = new String[]{"sample_1", "sample_2", "sample_3", "sample_4", "sample_5", "sample_6"};
        this.key_normal = new String[]{"os_key_normal3", "os_key_normal3", "os_key_normal3", "os_key_normal3", "os_key_normal3", "os_key_normal3"};
        this.space_key_normal = new String[]{"os_space_key_normal3", "os_space_key_normal3", "os_space_key_normal3", "os_space_key_normal3", "os_space_key_normal3", "os_space_key_normal3"};
        this.key_pressed = new String[]{"os_key_pressed3", "os_key_pressed3", "os_key_pressed3", "os_key_pressed3", "os_key_pressed3", "os_key_pressed3"};
        this.space_key_pressed = new String[]{"os_space_key_pressed3", "os_space_key_pressed3", "os_space_key_pressed3", "os_space_key_pressed3", "os_space_key_pressed3", "os_space_key_pressed3"};
        this.keysOpecity = new int[]{250, 250, 250, 250, 250, 250};
        this.bgOpecity = new int[]{230, 230, 230, 230, 230, 230};
        this.list = (ListView) findViewById(R.id.list);
        this.sessionManager = new SessionManager(this);



        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {

                selectKeyboard = true;
                sessionManager.setBackgroundColor(images_names[i]);
                sessionManager.setSample(samples[i]);
                sessionManager.setKeyNormal(key_normal[i]);
                sessionManager.setKeyPressed(key_pressed[i]);
                sessionManager.setSpaceKeyNormal(space_key_normal[i]);
                sessionManager.setSpaceKeyPressed(space_key_pressed[i]);
                sessionManager.setForegroundColor((String) mColorPaletteForeground.get(i));
                sessionManager.setBackgroundOpecity(bgOpecity[i]);
                sessionManager.setForgroundOpecity(keysOpecity[i]);
                themesAdapter.notifyDataSetChanged();

            }
        });
        this.themesAdapter = new ThemesAdapter(samples, this.images_names, ThemesActivity.this);
        this.list.setAdapter(this.themesAdapter);

        this.btn_personlise = (TextView) findViewById(R.id.btn_personlise);
        this.btn_personlise.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                if (!selectKeyboard) {
                    Toast.makeText(mContext, "Select Keyboard Theme", Toast.LENGTH_LONG).show();
                    return;
                } 
            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        return super.onOptionsItemSelected(menuItem);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.inputStreamImg = null;
        if (i == 1) {
            try {
                Uri data = intent.getData();
                this.bitmap = (Bitmap) intent.getExtras().get("data");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                this.bitmap.compress(CompressFormat.JPEG, 50, byteArrayOutputStream);
                Log.e("Activity", "Pick from Camera::>>> ");
                String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                StringBuilder sb = new StringBuilder();
                sb.append(Environment.getExternalStorageDirectory());
                sb.append("/test");
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("IMG_");
                sb3.append(format);
                sb3.append(".jpg");
                this.destination = new File(sb2, sb3.toString());
                try {
                    this.destination.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(this.destination);
                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                this.imgPath = getRealPathFromURI(data);
                this.sessionManager.setBackgroundColor(this.imgPath);
                Context context = this.mContext;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("");
                sb4.append(this.imgPath);
                Toast.makeText(context, sb4.toString(), Toast.LENGTH_SHORT).show();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else if (i == 2) {
            Uri data2 = intent.getData();
            try {
                this.bitmap = Media.getBitmap(getContentResolver(), data2);
                this.bitmap.compress(CompressFormat.JPEG, 50, new ByteArrayOutputStream());
                Log.e("Activity", "Pick from Gallery::>>> ");
                this.imgPath = getRealPathFromURI(data2);
                this.destination = new File(this.imgPath.toString());
                Context context2 = this.mContext;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("");
                sb5.append(this.imgPath);
                Toast.makeText(context2, sb5.toString(), Toast.LENGTH_SHORT).show();
                this.sessionManager.setBackgroundColor(this.imgPath);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor managedQuery = managedQuery(uri, new String[]{"_data"}, null, null, null);
        int columnIndexOrThrow = managedQuery.getColumnIndexOrThrow("_data");
        managedQuery.moveToFirst();
        return managedQuery.getString(columnIndexOrThrow);
    }


    public void onBackPressed() {
        finish();
    }
}
