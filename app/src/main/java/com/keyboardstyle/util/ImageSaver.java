package com.keyboardstyle.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageSaver {
    private Context context;
    private String directoryName = "images";
    private boolean external;
    private String fileName = "image.png";

    public ImageSaver(Context context) {
        this.context = context;
    }

    public ImageSaver setFileName(String str) {
        this.fileName = str;
        return this;
    }

    public ImageSaver setExternal(boolean z) {
        this.external = z;
        return this;
    }

    public ImageSaver setDirectoryName(String str) {
        this.directoryName = str;
        return this;
    }

    public void save(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            OutputStream fileOutputStream2 = new FileOutputStream(createFile());
            try {
                bitmap.compress(CompressFormat.PNG, 100, fileOutputStream2);
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException bitmap2) {
                        bitmap2.printStackTrace();
                    }
                }
            } catch (Exception e) {

            } catch (Throwable th2) {

            }
        } catch (Exception e3) {

        }
    }

    private File createFile() {
        File albumStorageDir;
        if (this.external) {
            albumStorageDir = getAlbumStorageDir(this.directoryName);
        } else {
            albumStorageDir = this.context.getDir(this.directoryName, 0);
        }
        if (!(albumStorageDir.exists() || albumStorageDir.mkdirs())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error creating directory ");
            stringBuilder.append(albumStorageDir);
            Log.e("ImageSaver", stringBuilder.toString());
        }
        return new File(albumStorageDir, this.fileName);
    }

    private File getAlbumStorageDir(String str) {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str);
    }

    public static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean isExternalStorageReadable() {
        String externalStorageState = Environment.getExternalStorageState();
        if (!"mounted".equals(externalStorageState)) {
            if (!"mounted_ro".equals(externalStorageState)) {
                return false;
            }
        }
        return true;
    }

    public Bitmap load() {
        FileInputStream fileInputStream;
        Exception e;
        Throwable th;
        FileInputStream fileInputStream2 = null;
        try {
            fileInputStream = new FileInputStream(createFile());
            try {
                Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream);
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                return decodeStream;
            } catch (Exception e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    FileInputStream fileInputStream3 = fileInputStream;
                    th = th2;
                    fileInputStream2 = fileInputStream3;
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e5) {
            e = e5;
            fileInputStream = null;
            e.printStackTrace();

            return null;
        } catch (Throwable th3) {
            th = th3;

            throw null;
        }
    }
}
