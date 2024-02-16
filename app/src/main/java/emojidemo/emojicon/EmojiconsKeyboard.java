package emojidemo.emojicon;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.keyboardstyle.R;

import java.util.Arrays;
import java.util.List;

import emojidemo.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import emojidemo.emojicon.emoji.Emojicon;
import emojidemo.emojicon.emoji.Nature;
import emojidemo.emojicon.emoji.Objects;
import emojidemo.emojicon.emoji.People;
import emojidemo.emojicon.emoji.Places;
import emojidemo.emojicon.emoji.Symbols;


import static emojidemo.emojicon.EmojiconGridView.mEmojiconPopup;

public class EmojiconsKeyboard extends RelativeLayout implements OnPageChangeListener, EmojiconRecents {
    private ViewPager emojisPager;
    Context mContext;
    private int mEmojiTabLastSelectedIndex = -1;
    private View[] mEmojiTabs;
    private PagerAdapter mEmojisAdapter;
    private EmojiconRecentsManager mRecentsManager;
    LinearLayout mRootView;
    OnEmojiconBackspaceClickedListener onEmojiconBackspaceClickedListener;
    public OnEmojiconClickedListener onEmojiconClickedListener;

    public interface OnEmojiconBackspaceClickedListener {
        void onEmojiconBackspaceClicked(View view);
    }

    public static class RepeatListener implements OnTouchListener {
        private final OnClickListener clickListener;
        private View downView;
        private Handler handler = new Handler();
        private Runnable handlerRunnable = new Runnable() {
            public void run() {
                if (RepeatListener.this.downView != null) {
                    RepeatListener.this.handler.removeCallbacksAndMessages(RepeatListener.this.downView);
                    RepeatListener.this.handler.postAtTime(this, RepeatListener.this.downView, SystemClock.uptimeMillis() + ((long) RepeatListener.this.normalInterval));
                    RepeatListener.this.clickListener.onClick(RepeatListener.this.downView);
                }
            }
        };
        private int initialInterval;
        private final int normalInterval;

        public RepeatListener(int i, int i2, OnClickListener onClickListener) {
            if (onClickListener == null) {
                throw new IllegalArgumentException("null runnable");
            }
            if (i >= 0) {
                if (i2 >= 0) {
                    this.initialInterval = i;
                    this.normalInterval = i2;
                    this.clickListener = onClickListener;
                    return;
                }
            }
            throw new IllegalArgumentException("negative interval");
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case 0:
                    this.downView = view;
                    this.handler.removeCallbacks(this.handlerRunnable);
                    this.handler.postAtTime(this.handlerRunnable, this.downView, SystemClock.uptimeMillis() + ((long) this.initialInterval));
                    this.clickListener.onClick(view);
                    return true;
                case 1:
                case 3:
                case 4:
                    this.handler.removeCallbacksAndMessages(this.downView);
                    this.downView = null;
                    return true;
                default:
                    return false;
            }
        }
    }

    private static class EmojisPagerAdapter extends PagerAdapter {
        private List<EmojiconGridView> views;

        public boolean isViewFromObject(View view, Object obj) {
            return obj == view;
        }

        EmojisPagerAdapter(List<EmojiconGridView> list) {
            this.views = list;
        }

        EmojiconRecentsGridView getRecentFragment() {
            for (EmojiconGridView emojiconGridView : this.views) {
                if (emojiconGridView instanceof EmojiconRecentsGridView) {
                    return (EmojiconRecentsGridView) emojiconGridView;
                }
            }
            return null;
        }

        public int getCount() {
            return this.views.size();
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            View emojiicongridview = ((EmojiconGridView) this.views.get(i)).rootView;
            viewGroup.addView(emojiicongridview, 0);
            return i;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    public EmojiconsKeyboard(LinearLayout linearLayout, Context context) {
        super(context);
        this.mContext = context;
        this.mRootView = linearLayout;
        populateEmojiKeyboard();
    }

    public void setOnEmojiconClickedListener(OnEmojiconClickedListener onEmojiconClickedListener) {
        this.onEmojiconClickedListener = onEmojiconClickedListener;
    }

    public void setOnEmojiconBackspaceClickedListener(OnEmojiconBackspaceClickedListener onEmojiconBackspaceClickedListener) {
        this.onEmojiconBackspaceClickedListener = onEmojiconBackspaceClickedListener;
    }

    private View populateEmojiKeyboard() {
        int i;
        RelativeLayout relativeLayout = (RelativeLayout) this.mRootView.findViewById(R.id.emoji_keyboard_layout);
        this.emojisPager = (ViewPager) relativeLayout.findViewById(R.id.emojis_pager);
        this.emojisPager.setOnPageChangeListener(this);

//        this.mEmojisAdapter = new EmojisPagerAdapter(Arrays.asList(new EmojiconGridView[]{new EmojiconRecentsGridView(mContext, null, null, mEmojiconPopup)}));
        this.mEmojisAdapter = new EmojisPagerAdapter(Arrays.asList(new EmojiconGridView[]{new EmojiconRecentsGridView(this.mContext, null, null, mEmojiconPopup), new EmojiconGridView(this.mContext, People.DATA, this, mEmojiconPopup), new EmojiconGridView(this.mContext, Nature.DATA, this, mEmojiconPopup), new EmojiconGridView(this.mContext, Objects.DATA, this, mEmojiconPopup), new EmojiconGridView(this.mContext, Places.DATA, this, mEmojiconPopup), new EmojiconGridView(this.mContext, Symbols.DATA, this, mEmojiconPopup)}));
        this.emojisPager.setAdapter(this.mEmojisAdapter);
        this.mEmojiTabs = new View[6];
        this.mEmojiTabs[0] = relativeLayout.findViewById(R.id.emojis_tab_0_recents);
        this.mEmojiTabs[1] = relativeLayout.findViewById(R.id.emojis_tab_1_people);
        this.mEmojiTabs[2] = relativeLayout.findViewById(R.id.emojis_tab_2_nature);
        this.mEmojiTabs[3] = relativeLayout.findViewById(R.id.emojis_tab_3_objects);
        this.mEmojiTabs[4] = relativeLayout.findViewById(R.id.emojis_tab_4_cars);
        this.mEmojiTabs[5] = relativeLayout.findViewById(R.id.emojis_tab_5_punctuation);
        for (i = 0; i < mEmojiTabs.length; i++) {
           final int j = i ;
            this.mEmojiTabs[i].setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    EmojiconsKeyboard.this.emojisPager.setCurrentItem(j);
                }
            });
        }
        relativeLayout.findViewById(R.id.emojis_backspace).setOnTouchListener(new RepeatListener(1000, 50, new OnClickListener() {
            public void onClick(View view) {
                if (EmojiconsKeyboard.this.onEmojiconBackspaceClickedListener != null) {
                    EmojiconsKeyboard.this.onEmojiconBackspaceClickedListener.onEmojiconBackspaceClicked(view);
                }
            }
        }));
        this.mRecentsManager = EmojiconRecentsManager.getInstance(relativeLayout.getContext());
        i = this.mRecentsManager.getRecentPage();
        if (i == 0 && this.mRecentsManager.size() == 0) {
            i = 1;
        }
        if (i == 0) {
            onPageSelected(i);
        } else {
            this.emojisPager.setCurrentItem(i, false);
        }
        return relativeLayout;
    }

    public void addRecentEmoji(Context context, Emojicon emojicon) {
        ((EmojisPagerAdapter) this.emojisPager.getAdapter()).getRecentFragment().addRecentEmoji(context, emojicon);
    }

    public void onPageSelected(int i) {
        if (this.mEmojiTabLastSelectedIndex != i) {
            switch (i) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    if (this.mEmojiTabLastSelectedIndex >= 0 && this.mEmojiTabLastSelectedIndex < this.mEmojiTabs.length) {
                        this.mEmojiTabs[this.mEmojiTabLastSelectedIndex].setSelected(false);
                    }
                    this.mEmojiTabs[i].setSelected(true);
                    this.mEmojiTabLastSelectedIndex = i;
                    this.mRecentsManager.setRecentPage(i);
                    break;
                default:
                    break;
            }
        }
    }
}
