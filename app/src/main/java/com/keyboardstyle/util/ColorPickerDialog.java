package com.keyboardstyle.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.RelativeLayout;

public class ColorPickerDialog extends AlertDialog {
    private ColorPicker colorPickerView;
    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
                case -2:
                    dialogInterface.dismiss();
                    return;
                case -1:
                    ColorPickerDialog.this.onColorSelectedListener.onColorSelected(ColorPickerDialog.this.colorPickerView.getColor());
                    return;
                default:
                    return;
            }
        }
    };
    private final OnColorSelectedListener onColorSelectedListener;

    public interface OnColorSelectedListener {
        void onColorSelected(int i);
    }

    @SuppressLint("ResourceType")
    public ColorPickerDialog(Context context, int i, OnColorSelectedListener onColorSelectedListener2) {
        super(context);
        this.onColorSelectedListener = onColorSelectedListener2;
        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(13);
        this.colorPickerView = new ColorPicker(context);
        this.colorPickerView.setColor(i);
        relativeLayout.addView(this.colorPickerView, layoutParams);
        setButton(-1, context.getString(17039370), this.onClickListener);
        setButton(-2, context.getString(17039360), this.onClickListener);
        setView(relativeLayout);
    }


}
