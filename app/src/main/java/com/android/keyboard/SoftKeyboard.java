package com.android.keyboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore.Images.Media;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.method.MetaKeyKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.keyboardstyle.R;
import com.keyboardstyle.activity.SettingsActivity;
import com.keyboardstyle.activity.ThemesActivity;
import com.keyboardstyle.util.Helper;
import com.keyboardstyle.util.KeyboardState;
import com.keyboardstyle.util.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import emojidemo.emojicon.EmojiconsKeyboard;

public class SoftKeyboard extends InputMethodService implements OnKeyboardActionListener {
    static final boolean DEBUG = false;
    private static final int DELETE_ACCELERATE_AT = 20;
    static final boolean PROCESS_HARD_KEYS = true;
    private static final int QUICK_PRESS = 200;
    private static final String TAG = "SoftKeyboard";
    private ImageView btn_insert_emoticon;
    private ImageView btn_settings;
    private ImageView btn_theme;
    private ImageView btn_voice;
    private int emojiCounter = 0;
    private EmojiconsKeyboard emojiconsKeyboard;
    private ImageView imageView;
    private LinearLayout inputViewAdds = null;
    private boolean isCapsLock = false;
    private boolean isEmojiKeyboardOpen = false;
    private RelativeLayout keyboard_layout;
    private boolean mCapsLock;
    private StringBuilder mComposing = new StringBuilder();
    private LatinKeyboard mCurKeyboard;
    private int mDeleteCount;
    private LatinKeyboardView mInputView;
    private boolean mIslistening;
    private int mLastDisplayWidth;
    private long mLastKeyTime;
    private long mLastShiftTime;
    private long mMetaState;
    private LatinKeyboard mNumericKeyboard;
    private boolean mPredictionOn;
    private LatinKeyboard mQwertyEmailKeyboard;
    private LatinKeyboard mQwertyKeyboard;
    private RelativeLayout mRelativeLayout;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private LatinKeyboard mSymbolsKeyboard;
    private LatinKeyboard mSymbolsShiftedKeyboard;
    private String mWordSeparators;
    private int openCounter;
    private SessionManager sessionManager;

    protected class SpeechRecognitionListener implements RecognitionListener {
        public void onBufferReceived(byte[] bArr) {
        }

        public void onEvent(int i, Bundle bundle) {
        }

        public void onPartialResults(Bundle bundle) {
        }

        public void onRmsChanged(float f) {
        }

        protected SpeechRecognitionListener() {
        }

        public void onBeginningOfSpeech() {
            Toast.makeText(SoftKeyboard.this, "Listning...", Toast.LENGTH_SHORT).show();
        }

        public void onEndOfSpeech() {
            SoftKeyboard.this.mSpeechRecognizer.stopListening();
            SoftKeyboard.this.btn_voice.clearAnimation();
            SoftKeyboard.this.mIslistening = false;
        }

        public void onError(int i) {
            SoftKeyboard.this.mSpeechRecognizer.startListening(SoftKeyboard.this.mSpeechRecognizerIntent);
        }

        public void onReadyForSpeech(Bundle bundle) {
            Log.d(SoftKeyboard.TAG, "onReadyForSpeech");
        }

        public void onResults(Bundle bundle) {
            ArrayList<String> arrayList = bundle.getStringArrayList("results_recognition");
            InputConnection currentInputConnection = SoftKeyboard.this.getCurrentInputConnection();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String) arrayList.get(0));
            stringBuilder.append(" ");
            currentInputConnection.setComposingText(stringBuilder.toString(), 1);
            SoftKeyboard.this.getCurrentInputConnection().finishComposingText();
        }
    }

    public View onCreateCandidatesView() {
        return null;
    }

    public void onRelease(int i) {
    }

    public void swipeRight() {
    }

    public void swipeUp() {
    }

    public void onCreate() {
        super.onCreate();
        this.mWordSeparators = getResources().getString(R.string.word_separators);
        this.sessionManager = new SessionManager(getApplicationContext());
        this.openCounter = this.sessionManager.getOpenCount();
    }

    public void onInitializeInterface() {
        this.mNumericKeyboard = new LatinKeyboard(this, R.xml.numeric);
        this.mQwertyKeyboard = new LatinKeyboard(this, R.xml.qwerty, R.integer.keyboard_normal);
        this.mQwertyEmailKeyboard = new LatinKeyboard(this, R.xml.qwerty, R.integer.keyboard_email);
        this.mSymbolsKeyboard = new LatinKeyboard(this, R.xml.symbols);
        this.mSymbolsShiftedKeyboard = new LatinKeyboard(this, R.xml.symbols_shift);
    }

    public View onCreateInputView() {
        if (this.inputViewAdds != null) {
            this.inputViewAdds.removeAllViews();
        }
        this.inputViewAdds = (LinearLayout) getLayoutInflater().inflate(R.layout.input, null);
        final Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.viberation);
        this.mRelativeLayout = (RelativeLayout) this.inputViewAdds.findViewById(R.id.emoji_keyboard_layout);
        this.keyboard_layout = (RelativeLayout) this.inputViewAdds.findViewById(R.id.keyboard_layout);
        this.imageView = (ImageView) this.inputViewAdds.findViewById(R.id.imageView);
        this.btn_insert_emoticon = (ImageView) this.inputViewAdds.findViewById(R.id.btn_insert_emoticon);
        this.btn_voice = (ImageView) this.inputViewAdds.findViewById(R.id.btn_voice);
        this.btn_theme = (ImageView) this.inputViewAdds.findViewById(R.id.btn_theme);
        this.btn_settings = (ImageView) this.inputViewAdds.findViewById(R.id.btn_settings);
        this.mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        this.mSpeechRecognizerIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        this.mSpeechRecognizerIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        this.mSpeechRecognizerIntent.putExtra("calling_package", getPackageName());
        this.mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());


        this.btn_insert_emoticon.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SoftKeyboard.this.isEmojiKeyboardOpen == false) {
                    int data = SoftKeyboard.this.mInputView.getHeight();
                    SoftKeyboard.this.mRelativeLayout.setVisibility(View.VISIBLE);
                    SoftKeyboard.this.mInputView.setVisibility(View.GONE);
                    SoftKeyboard.this.mRelativeLayout.setLayoutParams(new LayoutParams(-1, data));
              /*      SoftKeyboard.this.emojiconsKeyboard = new EmojiconsKeyboard(SoftKeyboard.this.inputViewAdds, SoftKeyboard.this.getApplicationContext());
                    SoftKeyboard.this.emojiconsKeyboard.setOnEmojiconClickedListener(new EmojiconGridFragment.OnEmojiconClickedListener() {
                        public void onEmojiconClicked(Emojicon emojicon) {
                            if (emojicon != null) {
                                SoftKeyboard.this.getCurrentInputConnection().commitText(emojicon.getEmoji(), 1);
                            }
                        }
                    });
                    SoftKeyboard.this.emojiconsKeyboard.setOnEmojiconBackspaceClickedListener(new EmojiconsFragment.OnEmojiconBackspaceClickedListener() {
                        public void onEmojiconBackspaceClicked(View view) {
                            SoftKeyboard.this.getCurrentInputConnection().sendKeyEvent(new KeyEvent(0, 0, 0, 67, 0, 0, 0, 0, 6));
                        }
                    });
                    SoftKeyboard.this.isEmojiKeyboardOpen = true;*/
                    return;
                }
                SoftKeyboard.this.mRelativeLayout.setVisibility(View.GONE);
                SoftKeyboard.this.mInputView.setVisibility(View.VISIBLE);
                SoftKeyboard.this.isEmojiKeyboardOpen = false;
            }
        });
        this.btn_theme.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SoftKeyboard.this.getApplicationContext(), ThemesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Helper.isFromKeyboardSetting = Boolean.valueOf(true);
                SoftKeyboard.this.startActivity(intent);
            }
        });
        this.btn_voice.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SoftKeyboard.this.sessionManager.getSpeech().booleanValue()) {
                    Toast.makeText(SoftKeyboard.this, "Please enable Speech to text!!", Toast.LENGTH_SHORT).show();
                } else if (SoftKeyboard.this.mIslistening) {
                    SoftKeyboard.this.mSpeechRecognizer.startListening(SoftKeyboard.this.mSpeechRecognizerIntent);
                    SoftKeyboard.this.mIslistening = true;
                    SoftKeyboard.this.btn_voice.startAnimation(loadAnimation);
                } else {
                    SoftKeyboard.this.mSpeechRecognizer.stopListening();
                    SoftKeyboard.this.mIslistening = false;
                }
            }
        });
        this.btn_settings.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SoftKeyboard.this.getApplicationContext(), SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Helper.isFromKeyboardSetting = Boolean.valueOf(true);
                SoftKeyboard.this.startActivity(intent);
            }
        });
        this.mInputView = (LatinKeyboardView) this.inputViewAdds.findViewById(R.id.keyboard);
        this.mInputView.setOnKeyboardActionListener(this);
        this.mRelativeLayout.setVisibility(View.GONE);
        this.mInputView.setVisibility(View.VISIBLE);
        this.isEmojiKeyboardOpen = false;
        return this.inputViewAdds;
    }

    private Intent getSpeechIntent() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("calling_package", getPackageName());
        intent.putExtra("android.speech.extra.PARTIAL_RESULTS", false);
        return intent;
    }

    public void onStartInput(EditorInfo editorInfo, boolean z) {
        super.onStartInput(editorInfo, z);
        this.mComposing.setLength(0);
        if (!z) {
            this.mMetaState = 0;
        }
        this.mPredictionOn = false;
        switch (editorInfo.inputType & 15) {
            case 1:
                this.mCurKeyboard = this.mQwertyKeyboard;
                this.mPredictionOn = true;
                int i = editorInfo.inputType & 4080;
                if (i == 128 || i == 144) {
                    this.mCurKeyboard = this.mQwertyKeyboard;
                    this.mPredictionOn = false;
                }
                if (i == 32 || i == 16 || i == 176) {
                    this.mCurKeyboard = this.mQwertyEmailKeyboard;
                    this.mPredictionOn = false;
                }
                if ((editorInfo.inputType & 65536) != 0) {
                    this.mPredictionOn = false;
                }
                updateShiftKeyState(editorInfo);
                break;
            case 2:
            case 3:
            case 4:
                this.mCurKeyboard = this.mNumericKeyboard;
                KeyboardState.phoneKeyboard = this.mNumericKeyboard;
                break;
            default:
                this.mCurKeyboard = this.mQwertyKeyboard;
                updateShiftKeyState(editorInfo);
                break;
        }
        KeyboardState.keyboard = this.mCurKeyboard;
        this.mCurKeyboard.setImeOptions(getResources(), editorInfo.imeOptions);
    }

    public void onFinishInput() {
        super.onFinishInput();
        this.mComposing.setLength(0);
        setCandidatesViewShown(false);
        this.mCurKeyboard = this.mQwertyKeyboard;
        if (this.mInputView != null) {
            this.mInputView.closing();
        }
    }

    public void onStartInputView(EditorInfo editorInfo, boolean z) {
        super.onStartInputView(editorInfo, z);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.sessionManager.getVibration());
        stringBuilder.append("");
        Log.d("Viberationss", stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.sessionManager.getPopup());
        stringBuilder.append("");
        Log.d("getPopuppp", stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.sessionManager.getSound());
        stringBuilder.append("");
        Log.d("getSounddd", stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.sessionManager.isAutoCapitalize());
        stringBuilder.append("");
        Log.d("isAutoCapitalizeee", stringBuilder.toString());
        this.mDeleteCount = 0;
        this.openCounter++;
        this.sessionManager.setOpenCount(this.openCounter);
        this.imageView.setImageResource(0);
        this.imageView.setImageDrawable(null);
        this.imageView.setImageDrawable(null);
        this.imageView.setImageBitmap(null);
        if (this.sessionManager.getBackgroundColor().contains("keyboard")) {
            String backgroundColor = this.sessionManager.getBackgroundColor();
            this.imageView.setVisibility(View.VISIBLE);
            this.imageView.setImageResource(getResources().getIdentifier(backgroundColor, "drawable", getPackageName()));
            this.imageView.setAlpha(this.sessionManager.getBackgroundOpecity());
        } else {
            this.imageView.setVisibility(View.VISIBLE);
            try {
                this.imageView.setImageBitmap(modifyOrientation(Media.getBitmap(getContentResolver(), Uri.fromFile(new File(this.sessionManager.getBackgroundColor()))), Uri.fromFile(new File(this.sessionManager.getBackgroundColor())).getPath()));
                this.imageView.setVisibility(View.VISIBLE);
                this.imageView.setAlpha(this.sessionManager.getBackgroundOpecity());
            } catch (IOException z2) {
                z2.printStackTrace();
            }
        }
        this.btn_insert_emoticon.setColorFilter(Color.parseColor(this.sessionManager.getForegroundColor()));
        this.btn_voice.setColorFilter(Color.parseColor(this.sessionManager.getForegroundColor()));
        this.btn_settings.setColorFilter(Color.parseColor(this.sessionManager.getForegroundColor()));
        this.btn_theme.setColorFilter(Color.parseColor(this.sessionManager.getForegroundColor()));
        if (this.sessionManager.isAutoCapitalize().booleanValue()) {
            updateShiftKeyState(editorInfo);
        }
        this.mInputView.setPreviewEnabled(this.sessionManager.getPopup().booleanValue());
        this.mCurKeyboard = KeyboardState.keyboard != null ? KeyboardState.keyboard : this.mCurKeyboard;
        setLatinKeyboard(this.mCurKeyboard);
        this.mInputView.closing();

        if (this.mCurKeyboard != this.mNumericKeyboard) {
            LatinKeyboard latinKeyboard = this.mCurKeyboard;
            LatinKeyboard latinKeyboard2 = this.mQwertyEmailKeyboard;
        }
    }

    public void onUpdateSelection(int i, int i2, int i3, int i4, int i5, int i6) {
        super.onUpdateSelection(i, i2, i3, i4, i5, i6);
        if (this.mComposing.length() <= 0) {
            return;
        }
        if (i3 != i6 || i4 != i6) {
            this.mComposing.setLength(0);
            InputConnection gh = getCurrentInputConnection();
            if (gh != null) {
                gh.finishComposingText();
            }
        }
    }

    private boolean translateKeyDown(int i, KeyEvent keyEvent) {
        this.mMetaState = MetaKeyKeyListener.handleKeyDown(this.mMetaState, i, keyEvent);
        int unicodeChar = keyEvent.getUnicodeChar(MetaKeyKeyListener.getMetaState(this.mMetaState));
        this.mMetaState = MetaKeyKeyListener.adjustMetaAfterKeypress(this.mMetaState);
        InputConnection currentInputConnection = getCurrentInputConnection();
        if (unicodeChar == 0 || currentInputConnection == null) {
            return false;
        }
        if ((Integer.MIN_VALUE & unicodeChar) != 0) {
            unicodeChar &= Integer.MAX_VALUE;
        }
        if (this.mComposing.length() > 0) {
            int deadChar = KeyEvent.getDeadChar(this.mComposing.charAt(this.mComposing.length() - 1), unicodeChar);
            if (deadChar != 0) {
                this.mComposing.setLength(this.mComposing.length() - 1);
                unicodeChar = deadChar;
            }
        }
        onKey(unicodeChar, null);
        return true;
    }


    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            if (i != 67) {
                if (i == 62 && (keyEvent.getMetaState() & 2) != 0) {
                    InputConnection currentInputConnection = getCurrentInputConnection();
                    if (currentInputConnection != null) {
                        currentInputConnection.clearMetaKeyStates(2);
                        sendDownUpKeyEvents(29);
                        sendDownUpKeyEvents(42);
                        sendDownUpKeyEvents(32);
                        sendDownUpKeyEvents(46);
                        sendDownUpKeyEvents(43);
                        sendDownUpKeyEvents(37);
                        sendDownUpKeyEvents(32);
                        return true;
                    }
                }
                if (this.mPredictionOn && translateKeyDown(i, keyEvent)) {
                    return true;
                }
            } else if (this.mComposing.length() > 0) {
                onKey(-5, null);
                return true;
            }
        } else if (keyEvent.getRepeatCount() == 0 && this.mInputView != null && this.mInputView.handleBack()) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (this.mPredictionOn) {
            this.mMetaState = MetaKeyKeyListener.handleKeyUp(this.mMetaState, i, keyEvent);
        }
        return super.onKeyUp(i, keyEvent);
    }

    private void commitTyped(InputConnection inputConnection) {
        if (this.mComposing.length() > 0) {
            inputConnection.commitText(this.mComposing, this.mComposing.length());
            this.mComposing.setLength(0);
        }
    }

    private void updateShiftKeyState(EditorInfo editorInfo) {
        if (editorInfo != null && this.mInputView != null && this.mQwertyKeyboard == this.mInputView.getKeyboard()) {
            EditorInfo currentInputEditorInfo = getCurrentInputEditorInfo();
            boolean z = false;
            Integer gdf = (currentInputEditorInfo == null || currentInputEditorInfo.inputType == 0) ? null : getCurrentInputConnection().getCursorCapsMode(editorInfo.inputType);
            LatinKeyboardView latinKeyboardView = this.mInputView;
            if (this.mCapsLock || gdf != null) {
                z = true;
            }
            latinKeyboardView.setShifted(z);
            updateShiftIcon();
        }
    }

    private void updateShiftIcon() {
        List keys = this.mQwertyKeyboard.getKeys();
        for (int i = 0; i < keys.size() - 1; i++) {
            Key key = (Key) keys.get(i);
            this.mInputView.invalidateAllKeys();
            if (key.codes[0] == -1) {
                key.label = null;
                if (this.mInputView.isShifted() && !this.mCapsLock) {
                    key.icon = getResources().getDrawable(R.drawable.ic_shift_shifted);
                    return;
                } else if (this.mInputView.isShifted() && this.mCapsLock) {
                    key.icon = getResources().getDrawable(R.drawable.ic_shift_caps);
                    return;
                } else {
                    key.icon = getResources().getDrawable(R.drawable.ic_shift_normal);
                    return;
                }
            }
        }
    }

    private boolean isAlphabet(int i) {
        return Character.isLetter(i);
    }

    private void sendKey(int i) {
        if (i == 10) {
            sendDownUpKeyEvents(66);
        } else if (i < 48 || i > 57) {
            getCurrentInputConnection().commitText(String.valueOf((char) i), 1);
        } else {
            sendDownUpKeyEvents((i - 48) + 7);
        }
    }

    public void sound_vibrate() {
        if (this.sessionManager.getSound().booleanValue()) {
            ((AudioManager) getSystemService(AUDIO_SERVICE)).playSoundEffect(0, 5.5f);
        }
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (this.sessionManager.getVibration().booleanValue()) {
            vibrator.vibrate(50);
        }
    }

    public void onKey(int i, int[] iArr) {
        sound_vibrate();
        if (isWordSeparator(i)) {
            if (this.mComposing.length() > 0) {
                commitTyped(getCurrentInputConnection());
            }
            sendKey(i);
            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (i == -5) {
            handleBackspace();
        } else if (i == -1) {
            handleShift();
        } else if (i == -2) {
            if (this.mCurKeyboard == this.mQwertyKeyboard) {
                this.mCurKeyboard = this.mSymbolsKeyboard;
                setLatinKeyboard(this.mSymbolsKeyboard);
            } else {
                this.mCurKeyboard = this.mQwertyKeyboard;
                setLatinKeyboard(this.mQwertyKeyboard);
            }
        } else if (i == -3) {
            handleClose();
        } else if (i != -100) {
            if (i != -10000) {
                if (i == -10001) {
                    this.mComposing.append("‌");
                    getCurrentInputConnection().setComposingText(this.mComposing, 1);
                } else if (i == -10002) {
                    this.mComposing.append("ẋ");
                    getCurrentInputConnection().setComposingText(this.mComposing, 1);
                } else if (i == -10003) {
                    this.mComposing.append("Ẋ");
                    getCurrentInputConnection().setComposingText(this.mComposing, 1);
                } else if (i == 1567) {
                    this.mComposing.append("؟");
                    getCurrentInputConnection().setComposingText(this.mComposing, 1);
                } else {
                    handleCharacter(i, iArr);
                }
            }
        }
    }

    private void setLatinKeyboard(LatinKeyboard latinKeyboard) {
        this.mRelativeLayout.setVisibility(View.GONE);
        this.mInputView.setVisibility(View.VISIBLE);
        this.isEmojiKeyboardOpen = false;
        latinKeyboard.setImeOptions(getResources(), getCurrentInputEditorInfo().imeOptions);
        this.mInputView.setKeyboard(latinKeyboard);
    }

    public void onText(CharSequence charSequence) {
        InputConnection currentInputConnection = getCurrentInputConnection();
        if (currentInputConnection != null) {
            currentInputConnection.beginBatchEdit();
            if (this.mComposing.length() > 0) {
                commitTyped(currentInputConnection);
            }
            currentInputConnection.commitText(charSequence, 0);
            currentInputConnection.endBatchEdit();
            updateShiftKeyState(getCurrentInputEditorInfo());
        }
    }

    private void handleBackspace() {
        InputConnection currentInputConnection = getCurrentInputConnection();
        if (currentInputConnection != null) {
            currentInputConnection.beginBatchEdit();
            int length = this.mComposing.length();
            if (length <= 0) {
                sendDownUpKeyEvents(67);
                if (this.mDeleteCount > 20) {
                    sendDownUpKeyEvents(67);
                }
            } else if (this.mDeleteCount > 20) {
                this.mComposing.setLength(0);
                currentInputConnection.setComposingText("", 1);
            } else {
                this.mComposing.delete(length - 1, length);
                currentInputConnection.setComposingText(this.mComposing, 1);
            }
            currentInputConnection.endBatchEdit();
            updateShiftKeyState(getCurrentInputEditorInfo());
        }
    }

    private void handleShift() {
        if (this.mInputView != null) {
            Keyboard keyboard = this.mInputView.getKeyboard();
            boolean z = true;
            LatinKeyboardView latinKeyboardView;
            if ((this.mQwertyKeyboard == keyboard || this.mQwertyEmailKeyboard == keyboard) && !this.mInputView.isShifted()) {
                latinKeyboardView = this.mInputView;
                if (!this.mCapsLock) {
                    if (this.mInputView.isShifted()) {
                        z = false;
                    }
                }
                latinKeyboardView.setShifted(z);
            } else if ((this.mQwertyKeyboard == keyboard || this.mQwertyEmailKeyboard == keyboard) && this.mInputView.isShifted()) {
                this.mCapsLock ^= true;
                latinKeyboardView = this.mInputView;
                if (!this.mCapsLock) {
                    if (this.mInputView.isShifted()) {
                        z = false;
                    }
                }
                latinKeyboardView.setShifted(z);
                this.isCapsLock = false;
            } else if (keyboard == this.mSymbolsKeyboard) {
                this.mSymbolsKeyboard.setShifted(true);
                setLatinKeyboard(this.mSymbolsShiftedKeyboard);
                this.mSymbolsShiftedKeyboard.setShifted(true);
            } else if (keyboard == this.mSymbolsShiftedKeyboard) {
                this.mSymbolsShiftedKeyboard.setShifted(false);
                setLatinKeyboard(this.mSymbolsKeyboard);
                this.mSymbolsKeyboard.setShifted(false);
            }
            updateShiftIcon();
        }
    }

    private void handleCharacter(int i, int[] iArr) {
        if (!(isInputViewShown() || this.mInputView.isShifted())) {
            i = Character.toUpperCase(i);
        }
        if (isAlphabet(i) || this.mPredictionOn) {
            this.mComposing.append((char) i);
            getCurrentInputConnection().setComposingText(this.mComposing, 1);
            return;
        }
        this.mComposing.append((char) i);
        getCurrentInputConnection().setComposingText(this.mComposing, 1);
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    private void handleClose() {
        commitTyped(getCurrentInputConnection());
        requestHideSelf(0);
        this.mInputView.closing();
    }

    private void checkToggleCapsLock() {
        long currentTimeMillis = System.currentTimeMillis();
        if ((this.mInputView.getKeyboard() == this.mQwertyKeyboard || this.mInputView.getKeyboard() == this.mQwertyEmailKeyboard) && this.mInputView.isShifted()) {
            this.mCapsLock = !this.mCapsLock;
            this.mLastShiftTime = 0;
            return;
        }
        this.mLastShiftTime = currentTimeMillis;
    }

    private String getWordSeparators() {
        return this.mWordSeparators;
    }

    public boolean isWordSeparator(int i) {
        return getWordSeparators().contains(String.valueOf((char) i));
    }

    public void swipeLeft() {
        handleBackspace();
    }

    public void swipeDown() {
        handleClose();
    }

    public void onPress(int i) {
        for (Key key : this.mInputView.getKeyboard().getKeys()) {
            if (key.codes[0] == i) {
                if (key.modifier) {
                    this.mInputView.setPreviewEnabled(false);
                } else if (KeyboardState.phoneKeyboard == this.mInputView.getKeyboard()) {
                    this.mInputView.setPreviewEnabled(false);
                } else {
                    this.mInputView.setPreviewEnabled(this.sessionManager.getPopup().booleanValue());
                }
            }
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean z, boolean z2) {
        Matrix matrix = new Matrix();
        float f = 1.0f;
        float f2 = z ? -1.0f : 1.0f;
        if (z2) {
            f = -1.0f;
        }
        matrix.preScale(f2, f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String str) throws IOException {
        int data = new ExifInterface(str).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        if (data == 6) {
            return rotate(bitmap, 90.0f);
        }
        if (data == 8) {
            return rotate(bitmap, 270.0f);
        }
        switch (data) {
            case 2:
                return flip(bitmap, true, false);
            case 3:
                return rotate(bitmap, 180.0f);
            case 4:
                return flip(bitmap, false, true);
            default:
                return bitmap;
        }
    }


}
