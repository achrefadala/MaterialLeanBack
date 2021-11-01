package com.achref.leanbacklib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.achref.leanbacklib.line.LineAdapter;
import com.achref.leanbacklib.line.LineViewHolder;
import com.nineoldandroids.view.ViewHelper;

public class MaterialLeanBack extends FrameLayout {

    private RecyclerView recyclerView;
    private ImageView imageBackground;
    private View imageBackgroundOverlay;

    private Adapter adapter;
    private Customizer customizer;
    private MaterialLeanBackSettings settings = new MaterialLeanBackSettings();

    private OnItemClickListenerWrapper onItemClickListenerWrapper = new OnItemClickListenerWrapper();

    public MaterialLeanBack(Context context) {
        super(context);
    }

    public MaterialLeanBack(Context context, AttributeSet attrs) {
        super(context, attrs);
        settings.handleAttributes(context, attrs);
    }

    public MaterialLeanBack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        settings.handleAttributes(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        addView(LayoutInflater.from(getContext()).inflate(R.layout.mlb_layout, this, false));

        imageBackground = findViewById(R.id.mlb_imageBackground);
        imageBackgroundOverlay = findViewById(R.id.mlb_imageBackgroundOverlay);

        if (settings.backgroundId != null)
            imageBackground.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), settings.backgroundId));

        if (settings.backgroundOverlay != null)
            ViewHelper.setAlpha(imageBackgroundOverlay, settings.backgroundOverlay);
        if (settings.backgroundOverlayColor != null)
            imageBackgroundOverlay.setBackgroundColor(settings.backgroundOverlayColor);

        recyclerView = findViewById(R.id.mlb_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(new LineAdapter(settings, adapter, customizer, onItemClickListenerWrapper));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListenerWrapper.setOnItemClickListener(onItemClickListener);
    }

    public Customizer getCustomizer() {
        return customizer;
    }

    public void setCustomizer(Customizer customizer) {
        this.customizer = customizer;
    }

    public ImageView getImageBackground() {
        return imageBackground;
    }

    public void smoothScrollTo(int row, int column) {
        final int rowToGo = row + LineAdapter.PLACEHOLDER_START_SIZE;
        final int columnToGo = column + LineAdapter.PLACEHOLDER_START_SIZE;

        recyclerView.smoothScrollToPosition(rowToGo);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                final RecyclerView.ViewHolder childViewHolder = recyclerView.findViewHolderForLayoutPosition(rowToGo);
                if (childViewHolder instanceof LineViewHolder) {
                    LineViewHolder lineViewHolder = (LineViewHolder) childViewHolder;
                    lineViewHolder.getRecyclerView().smoothScrollToPosition(columnToGo);
                }
            }
        }, 2000);
    }

    public interface Customizer {
        void customizeTitle(TextView textView);
    }

    public interface OnItemClickListener {
        void onTitleClicked(int row, String text);

        void onItemClicked(int row, int column);
    }

    public static abstract class Adapter<VH extends ViewHolder> {

        public int itemPosition = 0;

        public int getLineCount() {
            return 0;
        }

        public int getCellsCount(int row) {
            return 0;
        }

        public VH onCreateViewHolder(ViewGroup viewGroup, int row) {
            return null;
        }

        public void onBindViewHolder(VH viewHolder, int i) {
        }

        public String getTitleForRow(int row) {
            return null;
        }

        public boolean hasRowTitle(int row) {
            return true;
        }

        public boolean isCustomView(int row) {
            return false;
        }

        //if you want to set a custom view into the MaterialLeanBack, eg: a header
        public RecyclerView.ViewHolder getCustomViewForRow(ViewGroup viewGroup, int row) {
            return null;
        }

        public void onBindCustomView(RecyclerView.ViewHolder viewHolder, int row) {
        }

        public int getEnlargedItemPosition(int position) {
            return position;
        }
    }

    public static class ViewHolder {

        public int row;
        public int cell;
        public View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }

    }
}
