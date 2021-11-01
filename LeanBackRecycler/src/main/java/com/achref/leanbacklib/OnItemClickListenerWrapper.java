package com.achref.leanbacklib;


import androidx.annotation.Nullable;

public class OnItemClickListenerWrapper {
    private MaterialLeanBack.OnItemClickListener onItemClickListener;

    @Nullable
    public MaterialLeanBack.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(MaterialLeanBack.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
