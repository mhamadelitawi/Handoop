package com.handoop.mhamad.handoop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handoop.mhamad.handoop.R;

/**
 * Created by Mhamad on 01-Mar-17.
 */

public class PreviewAdapter  extends BaseAdapter {

    Context context;
    String[] previews;


    public PreviewAdapter(Context context, String[] previews) {
        this.context = context;
        this.previews = previews;
    }

    @Override
    public int getCount() {
        return previews.length;
    }

    @Override
    public Object getItem(int position) {
        return previews[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.preview_view, null);
        ((TextView) convertView.findViewById(R.id.prev)  ).setText(  previews[position]  );
        return convertView;
    }
}
