package com.android.keyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;

import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.keyboardstyle.R;
import com.keyboardstyle.util.KeyboardState;
import com.keyboardstyle.util.SessionManager;

import java.util.List;

public class LatinKeyboardView extends KeyboardView {
    static final int KEYCODE_OPTIONS = -100;
    private Context mContext;
    private SessionManager sessionManager;

    int count;

    public LatinKeyboardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        init();
    }

    public LatinKeyboardView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        init();
    }

    private void init() {
        this.sessionManager = new SessionManager(this.mContext);
    }

    protected boolean onLongPress(Key key) {
        if (key.codes[0] == -3) {
            getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null);
            return true;
        } else if (key.codes[0] == 48) {
            getOnKeyboardActionListener().onKey(43, null);
            return true;
        } else {
            if (key.codes[0] != 113) {
                if (key.codes[0] != 81) {
                    if (key.codes[0] != 119) {
                        if (key.codes[0] != 87) {
                            if (key.codes[0] != 101) {
                                if (key.codes[0] != 69) {
                                    if (key.codes[0] != 114) {
                                        if (key.codes[0] != 82) {
                                            if (key.codes[0] != 116) {
                                                if (key.codes[0] != 84) {
                                                    if (key.codes[0] != 121) {
                                                        if (key.codes[0] != 89) {
                                                            if (key.codes[0] != 117) {
                                                                if (key.codes[0] != 85) {
                                                                    if (key.codes[0] != 105) {
                                                                        if (key.codes[0] != 73) {
                                                                            if (key.codes[0] != 111) {
                                                                                if (key.codes[0] != 79) {
                                                                                    if (key.codes[0] != 112) {
                                                                                        if (key.codes[0] != 80) {
                                                                                            if (key.codes[0] == -3) {
                                                                                                getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null);
                                                                                                return true;
                                                                                            } else if (key.codes[0] == 48 && getKeyboard() == KeyboardState.phoneKeyboard) {
                                                                                                getOnKeyboardActionListener().onText("+");
                                                                                                return true;
                                                                                            } else if (key.codes[0] != 42 || getKeyboard() != KeyboardState.phoneKeyboard) {
                                                                                                return super.onLongPress(key);
                                                                                            } else {
                                                                                                getOnKeyboardActionListener().onText("#");
                                                                                                return true;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    getOnKeyboardActionListener().onKey(48, null);
                                                                                    return true;
                                                                                }
                                                                            }
                                                                            getOnKeyboardActionListener().onKey(57, null);
                                                                            return true;
                                                                        }
                                                                    }
                                                                    getOnKeyboardActionListener().onKey(56, null);
                                                                    return true;
                                                                }
                                                            }
                                                            getOnKeyboardActionListener().onKey(55, null);
                                                            return true;
                                                        }
                                                    }
                                                    getOnKeyboardActionListener().onKey(54, null);
                                                    return true;
                                                }
                                            }
                                            getOnKeyboardActionListener().onKey(53, null);
                                            return true;
                                        }
                                    }
                                    getOnKeyboardActionListener().onKey(52, null);
                                    return true;
                                }
                            }
                            getOnKeyboardActionListener().onKey(51, null);
                            return true;
                        }
                    }
                    getOnKeyboardActionListener().onKey(50, null);
                    return true;
                }
            }
            getOnKeyboardActionListener().onKey(49, null);
            return true;
        }
    }

    public boolean setShifted(boolean z) {
        Keyboard keyboard = getKeyboard();
        if (!(keyboard instanceof LatinKeyboard)) {
            return false;
        }
        keyboard.setShifted(z);
        invalidateAllKeys();
        return true;
    }

    public void setPreviewEnabled(boolean z) {
        super.setPreviewEnabled(z);
    }

    @SuppressLint("ResourceType")
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        String foregroundColor = this.sessionManager.getForegroundColor();
        Display defaultDisplay = ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        i = getResources().getDimensionPixelSize(R.dimen.dp16);
        i2 = getResources().getDimensionPixelSize(R.dimen.dp24);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.canvasKeyY);
        Paint paint = new Paint();
        paint.setTextAlign(Align.CENTER);
        paint.setColor(Color.parseColor(foregroundColor));
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setStrokeJoin(Join.ROUND);
        List<Key> keys = getKeyboard().getKeys();
        for (Key key : keys) {
            Drawable drawable = null;
            int i3;
            int identifier;
            if (key.pressed) {
                identifier = getResources().getIdentifier(sessionManager.getKeyPressed(), "drawable", getContext().getPackageName());
                if (key.modifier) {
                    identifier = getResources().getIdentifier(sessionManager.getSpaceKeyPressed(), "drawable", getContext().getPackageName());
                }
                drawable = ContextCompat.getDrawable(mContext, identifier);
            } else {

                identifier = getResources().getIdentifier(sessionManager.getKeyNormal(), "drawable", getContext().getPackageName());
                if (key.modifier) {
                    identifier = getResources().getIdentifier(sessionManager.getSpaceKeyNormal(), "drawable", getContext().getPackageName());
                }
//                try {
                drawable = ContextCompat.getDrawable(mContext, identifier);
//                } catch (Exception e) {
//                    Toast.makeText(mContext, "Please Select Keyboard first", Toast.LENGTH_LONG).show();
//                }

            }
            drawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
            drawable.setAlpha(sessionManager.getForgroundOpecity());
            drawable.draw(canvas2);
            String charSequence = key.label == null ? null : adjustCase(key.label).toString();
            if (key.label != null) {
                if (charSequence.length() <= 1 || key.codes.length >= 2) {
                    paint.setTextSize((float) i2);
                } else {
                    paint.setTextSize((float) i);
                }
                paint.setShadowLayer(1.0f, 0.0f, 0.0f, Color.parseColor("#d0d0d0"));
                canvas2.drawText(charSequence, (float) (key.x + (key.width / 2)), (float) ((key.y + (key.height / 2)) + 10), paint);
                paint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
                i3 = i;
            } else {
                i3 = i;
                key.icon.setBounds(key.x + ((key.width - key.icon.getIntrinsicWidth()) / 2), key.y + ((key.height - key.icon.getIntrinsicHeight()) / 2), (key.x + ((key.width - key.icon.getIntrinsicWidth()) / 2)) + key.icon.getIntrinsicWidth(), (key.y + ((key.height - key.icon.getIntrinsicHeight()) / 2)) + key.icon.getIntrinsicHeight());
                if (key.codes[0] == 32) {
                    key.icon.setBounds(key.x + 25, key.y, (key.x + key.width) - 25, (key.y + key.height) - 10);
                }
                key.icon.setColorFilter(Color.parseColor(foregroundColor), Mode.SRC_ATOP);
                key.icon.draw(canvas2);
            }
            i = i3;
        }
        for (Key key2 : keys) {
            paint.setTextSize(getResources().getDimension(R.dimen.canvasTextSize));
            if (key2.label != null) {
                if (!key2.label.toString().equals("q")) {
                    if (!key2.label.toString().equals("Q")) {
                        if (key2.codes[0] != 119) {
                            if (key2.codes[0] != 87) {
                                if (!key2.label.toString().equals("e")) {
                                    if (!key2.label.toString().equals("E")) {
                                        if (!key2.label.toString().equals("r")) {
                                            if (!key2.label.toString().equals("R")) {
                                                if (!key2.label.toString().equals("t")) {
                                                    if (!key2.label.toString().equals("T")) {
                                                        if (!key2.label.toString().equals("y")) {
                                                            if (!key2.label.toString().equals("Y")) {
                                                                if (!key2.label.toString().equals("u")) {
                                                                    if (!key2.label.toString().equals("U")) {
                                                                        if (!key2.label.toString().equals("i")) {
                                                                            if (!key2.label.toString().equals("I")) {
                                                                                if (!key2.label.toString().equals("o")) {
                                                                                    if (!key2.label.toString().equals("o")) {
                                                                                        if (!key2.label.toString().equals("p")) {
                                                                                            if (!key2.label.toString().equals("P")) {
                                                                                                if (key2.label.toString().equals("ا")) {
                                                                                                    canvas2.drawText(String.valueOf("آ"), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
                                                                                                }
                                                                                            }
                                                                                        }
//                                                                                        canvas2.drawText(String.valueOf(0), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
                                                                                    }
                                                                                }
//                                                                                canvas2.drawText(String.valueOf(9), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
                                                                            }
                                                                        }
//                                                                        canvas2.drawText(String.valueOf(8), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
                                                                    }
                                                                }
//                                                                canvas2.drawText(String.valueOf(7), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
                                                            }
                                                        }
//                                                        canvas2.drawText(String.valueOf(6), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
                                                    }
                                                }
//                                                canvas2.drawText(String.valueOf(5), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
                                            }
                                        }
//                                        canvas2.drawText(String.valueOf(4), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
                                    }
                                }
//                                canvas2.drawText(String.valueOf(3), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
                            }
                        }
//                        canvas2.drawText(String.valueOf(2), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
                    }
                }
//                canvas2.drawText(String.valueOf(1), (float) ((key2.x + (key2.width / 2)) + 10), (float) (key2.y + dimensionPixelSize), paint);
            }
        }
    }

    private CharSequence adjustCase(CharSequence charSequence) {
        return (!getKeyboard().isShifted() || charSequence == null || charSequence.length() >= 3 || !Character.isLowerCase(charSequence.charAt(0))) ? charSequence : charSequence.toString().toUpperCase();
    }
}
