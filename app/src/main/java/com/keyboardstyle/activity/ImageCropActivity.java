package com.keyboardstyle.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.android.keyboard.LatinKeyboard;
import com.android.keyboard.LatinKeyboardView;
import com.android.keyboard.SoftKeyboard;
import com.keyboardstyle.R;
import com.keyboardstyle.util.ColorPickerDialog;
import com.keyboardstyle.util.ColorPickerDialog.OnColorSelectedListener;
import com.keyboardstyle.util.SessionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageCropActivity extends AppCompatActivity implements OnSeekBarChangeListener {


    private Intent GalIntent;
    private ImageView btn_insert_emoticon;
    private ImageView btn_settings;
    private ImageView btn_theme;
    private ImageView btn_voice;
    private ImageView buttonColor;
    private ImageView buttonGallery;
    private ImageView imageView;
    private LatinKeyboardView latinKeyboardView;

    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SessionManager sessionManager;
    private TextView tv_opacity_value;
    private TextView tv_opacity_value2;
    private Uri uri;


    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_crop);

        this.sessionManager = new SessionManager(this);
        this.latinKeyboardView = (LatinKeyboardView) findViewById(R.id.keyboard);
        this.btn_insert_emoticon = (ImageView) findViewById(R.id.btn_insert_emoticon);
        this.btn_voice = (ImageView) findViewById(R.id.btn_voice);
        this.btn_theme = (ImageView) findViewById(R.id.btn_theme);
        this.btn_settings = (ImageView) findViewById(R.id.btn_settings);
        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.buttonGallery = (ImageView) findViewById(R.id.button1);
        this.buttonColor = (ImageView) findViewById(R.id.button2);
        this.tv_opacity_value = (TextView) findViewById(R.id.tv_opacity_value);
        this.tv_opacity_value2 = (TextView) findViewById(R.id.tv_opacity_value2);
        this.seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        this.seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        this.seekBar1.setOnSeekBarChangeListener(this);
        this.seekBar2.setOnSeekBarChangeListener(this);
        this.seekBar1.setProgress(this.sessionManager.getBackgroundOpecity());
        this.seekBar2.setProgress(this.sessionManager.getForgroundOpecity());

        if (this.sessionManager.getBackgroundColor().contains("keyboard")) {
            String string = this.sessionManager.getBackgroundColor();
            this.imageView.setVisibility(View.VISIBLE);
            this.imageView.setImageResource(getResources().getIdentifier(string, "drawable", getPackageName()));
            this.imageView.setAlpha(this.sessionManager.getBackgroundOpecity());
        } else {
            this.imageView.setVisibility(View.VISIBLE);
            try {
                this.imageView.setImageBitmap(SoftKeyboard.modifyOrientation(Media.getBitmap(getContentResolver(), Uri.fromFile(new File(this.sessionManager.getBackgroundColor()))), Uri.fromFile(new File(this.sessionManager.getBackgroundColor())).getPath()));
                this.imageView.setVisibility(View.VISIBLE);
                this.imageView.setAlpha(this.sessionManager.getBackgroundOpecity());
            } catch (IOException bundle2) {
                bundle2.printStackTrace();
            }
        }

        btn_insert_emoticon.setColorFilter(Color.parseColor(this.sessionManager.getForegroundColor()));
        this.btn_voice.setColorFilter(Color.parseColor(this.sessionManager.getForegroundColor()));
        this.btn_settings.setColorFilter(Color.parseColor(this.sessionManager.getForegroundColor()));
        this.btn_theme.setColorFilter(Color.parseColor(this.sessionManager.getForegroundColor()));
        this.latinKeyboardView.setPreviewEnabled(false);
        this.latinKeyboardView.setKeyboard(new LatinKeyboard(this, R.xml.qwerty, R.integer.keyboard_normal));
        this.latinKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionListener() {
            public void onKey(int i, int[] iArr) {
            }

            public void onPress(int i) {
            }

            public void onRelease(int i) {
            }

            public void onText(CharSequence charSequence) {
            }

            public void swipeDown() {
            }

            public void swipeLeft() {
            }

            public void swipeRight() {
            }

            public void swipeUp() {
            }
        });
        EnableRuntimePermission();
        this.buttonGallery.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                GetImageFromGallery();
            }
        });
        this.buttonColor.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                showColorPickerDialogDemo();
            }
        });
    }

    public void GetImageFromGallery() {

        GalIntent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(this.GalIntent, "Select Image From Gallery"), 2);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.action_restore) {
            return super.onOptionsItemSelected(menuItem);
        }
        this.sessionManager.setForegroundColor("#000000");
        this.sessionManager.setBackgroundOpecity(CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
        this.sessionManager.setForgroundOpecity(CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
        this.latinKeyboardView.invalidateAllKeys();
        this.seekBar1.setProgress(this.sessionManager.getBackgroundOpecity());
        this.seekBar2.setProgress(this.sessionManager.getForgroundOpecity());
        return true;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);

        if (i != 0 || i2 != -1) {
            if (i == 2) {
                if (intent != null) {
                    this.uri = intent.getData();
                    showCropDialog(this.uri);
                }
            } else if (i == 1 && intent != null) {
                this.latinKeyboardView.invalidateAllKeys();
                intent.getExtras();
                String string = getRealPathFromURI(intent.getData());
                this.sessionManager.setBackgroundColor(string);
                try {
                    this.imageView.setVisibility(View.VISIBLE);
                    this.imageView.setImageBitmap(SoftKeyboard.modifyOrientation(Media.getBitmap(getContentResolver(), Uri.fromFile(new File(string))), Uri.fromFile(new File(string)).getPath()));
                } catch (IOException i3) {
                    i3.printStackTrace();
                }
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor carsor = managedQuery(uri, new String[]{"_data"}, null, null, null);
        int columnIndexOrThrow = carsor.getColumnIndexOrThrow("_data");
        carsor.moveToFirst();
        return carsor.getString(columnIndexOrThrow);
    }

    public void EnableRuntimePermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
    }

    private void showCropDialog(Uri uri) {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.crop_dialog);
        dialog.setCancelable(true);
        final CropImageView cropImageView = (CropImageView) dialog.findViewById(R.id.CropImageView);
        cropImageView.setImageUriAsync(uri);
        TextView textView = (TextView) dialog.findViewById(R.id.tv_cancel);
        ((TextView) dialog.findViewById(R.id.tv_ok)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
//                Bitmap bitmap = cropImageView.getCroppedImage(600, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
                Bitmap bitmap = cropImageView.getCroppedImage(1080, 720);
                if (bitmap != null) {
                    cropImageView.setImageBitmap(bitmap);
                }
                sessionManager.setBackgroundColor(saveToInternalStorage(bitmap));
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
                dialog.dismiss();
            }
        });
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String saveToInternalStorage(Bitmap bitmap) {
        String file = Environment.getExternalStorageDirectory().toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(file);
        stringBuilder.append("/req_images");
        File file2 = new File(stringBuilder.toString());
        file2.mkdirs();
        File file3 = new File(file2, "Image-.jpg");
        if (file3.exists()) {
            file3.delete();
        }
        try {
            OutputStream fileOutputStream = new FileOutputStream(file3);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException bitmap2) {
            bitmap2.printStackTrace();
        }
        return file3.getAbsolutePath();
    }

    private void showColorPickerDialogDemo() {
        new ColorPickerDialog(this, -1, new OnColorSelectedListener() {
            public void onColorSelected(int i) {
                String string = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(Color.red(i)), Integer.valueOf(Color.green(i)), Integer.valueOf(Color.blue(i))});
                Log.d("Colorrrrrr", string);
                sessionManager.setForegroundColor(string);
                latinKeyboardView.invalidateAllKeys();
                btn_insert_emoticon.setColorFilter(Color.parseColor(string));
                btn_voice.setColorFilter(Color.parseColor(string));
                btn_settings.setColorFilter(Color.parseColor(string));
                btn_theme.setColorFilter(Color.parseColor(string));
            }
        }).show();
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        switch (seekBar.getId()) {
            case R.id.seekBar1 /*2131230966*/:
                TextView textView = this.tv_opacity_value;
                StringBuilder sb = new StringBuilder();
                sb.append(i);
                sb.append(" %");
                textView.setText(sb.toString());
                this.sessionManager.setBackgroundOpecity(i);
                this.imageView.setAlpha(i);
                return;
            case R.id.seekBar2 /*2131230967*/:
                TextView textView2 = this.tv_opacity_value2;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(i);
                sb2.append(" %");
                textView2.setText(sb2.toString());
                this.latinKeyboardView.invalidateAllKeys();
                this.sessionManager.setForgroundOpecity(i);
                return;
            default:
                return;
        }
    }


    public void onBackPressed() {
        finish();
    }
}
