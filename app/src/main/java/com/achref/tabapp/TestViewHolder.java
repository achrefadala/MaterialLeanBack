package com.achref.tabapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.achref.leanbacklib.MaterialLeanBack;

public class TestViewHolder extends MaterialLeanBack.ViewHolder {

    protected TextView textView;
    protected ImageView imageView;

    public TestViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView);
        imageView = itemView.findViewById(R.id.imageView);
    }
}
