package com.android.keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;
import androidx.core.content.ContextCompat;

import com.keyboardstyle.R;


public class LatinKeyboard extends Keyboard {
    private Context mContext;
    private Key mEnterKey;

    private static class LatinKey extends Key {
        LatinKey(Resources resources, Row row, int i, int i2, XmlResourceParser xmlResourceParser) {
            super(resources, row, i, i2, xmlResourceParser);
        }

        public boolean isInside(int i, int i2) {
            Object obj = (this.edgeFlags & 1) > 0 ? 1 : null;
            Object obj2 = (this.edgeFlags & 2) > 0 ? 1 : null;
            Object obj3 = (this.edgeFlags & 4) > 0 ? 1 : null;
            Object obj4 = (this.edgeFlags & 8) > 0 ? 1 : null;
            if ((i >= this.x || (obj != null && i <= this.x + this.width)) && ((i < this.x + this.width || (obj2 != null && i >= this.x)) && (i2 >= this.y || (obj3 != null && i2 <= this.y + this.height)))) {
                if (i2 < this.y + this.height) {
                    return true;
                }
                if (obj4 != null && i2 >= this.y) {
                    return true;
                }
            }
            return false;
        }
    }

    public LatinKeyboard(Context context, int i) {
        super(context, i);
        this.mContext = context;
    }

    public LatinKeyboard(Context context, int i, int i2) {
        super(context, i, i2);
        this.mContext = context;
    }

    public LatinKeyboard(Context context, int i, CharSequence charSequence, int i2, int i3) {
        super(context, i, charSequence, i2, i3);
    }

    protected Key createKeyFromXml(Resources resources, Row row, int i, int i2, XmlResourceParser xmlResourceParser) {
        Key latinKey = new LatinKey(resources, row, i, i2, xmlResourceParser);
        if (latinKey.codes[0] == 10) {
            this.mEnterKey = latinKey;
        }
        return latinKey;
    }

    void setImeOptions(Resources resources, int i) {
        if (this.mEnterKey != null) {
            switch (i & 1073742079) {
                case 2:
                    this.mEnterKey.iconPreview = null;
                    this.mEnterKey.icon = null;
                    this.mEnterKey.label = resources.getText(R.string.label_go_key);
                    break;
                case 3:
                    this.mEnterKey.icon = ContextCompat.getDrawable(this.mContext, R.drawable.ic_search);
                    this.mEnterKey.label = null;
                    break;
                case 4:
                    this.mEnterKey.iconPreview = null;
                    this.mEnterKey.icon = null;
                    this.mEnterKey.label = resources.getText(R.string.label_send_key);
                    break;
                case 5:
                    this.mEnterKey.iconPreview = null;
                    this.mEnterKey.icon = null;
                    this.mEnterKey.label = resources.getText(R.string.label_next_key);
                    break;
                case 6:
                    this.mEnterKey.iconPreview = null;
                    this.mEnterKey.icon = null;
                    this.mEnterKey.label = resources.getText(R.string.label_done_key);
                    break;
                default:
                    this.mEnterKey.icon = ContextCompat.getDrawable(this.mContext, R.drawable.ic_keyboard_return);
                    this.mEnterKey.label = null;
                    break;
            }
        }
    }
}
