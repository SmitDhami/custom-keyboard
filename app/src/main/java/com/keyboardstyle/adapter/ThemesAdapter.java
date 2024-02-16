package com.keyboardstyle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.keyboardstyle.R;
import com.keyboardstyle.util.SessionManager;

public class ThemesAdapter extends BaseAdapter {
    private String[] images_names;
    private Context mContext;
    private String[] samples;
    private SessionManager sessionManager;


    public ThemesAdapter(String[] strArr, String[] strArr2, Context context) {
        this.samples = strArr;
        this.images_names = strArr2;
        this.mContext = context;
        sessionManager = new SessionManager(mContext);

    }

    public class ViewHolder {
        ImageView imageView;
        RelativeLayout overlay;
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }


    public int getCount() {
        return this.images_names.length;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(this.mContext, R.layout.custom_list, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
            viewHolder.overlay = (RelativeLayout) view.findViewById(R.id.overlay);
            view.setTag(viewHolder);
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.imageView.setImageResource(this.mContext.getResources().getIdentifier(this.samples[i], "drawable", this.mContext.getPackageName()));

        if (this.sessionManager.getSample().equals(this.samples[i])) {
            viewHolder.overlay.setVisibility(View.VISIBLE);
        } else {
            viewHolder.overlay.setVisibility(View.GONE);
        }
        return view;
    }
}
