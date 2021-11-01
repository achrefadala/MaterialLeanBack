package com.achref.leanbacklib.line;


import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.achref.leanbacklib.MaterialLeanBack;
import com.achref.leanbacklib.MaterialLeanBackSettings;
import com.achref.leanbacklib.OnItemClickListenerWrapper;
import com.achref.leanbacklib.R;
import com.achref.leanbacklib.cell.CellAdapter;
import com.achref.leanbacklib.cell.CellViewHolder;


public class LineViewHolder extends RecyclerView.ViewHolder {

    protected final MaterialLeanBackSettings settings;
    protected final RecyclerView recyclerView;
    protected final MaterialLeanBack.Adapter adapter;
    protected final MaterialLeanBack.Customizer customizer;

    protected ViewGroup layout;
    protected TextView title;

    protected OnItemClickListenerWrapper onItemClickListenerWrapper;

    protected int row;
    protected boolean wrapped = false;

    public LineViewHolder(View itemView, @NonNull MaterialLeanBack.Adapter adapter, @NonNull MaterialLeanBackSettings settings, final MaterialLeanBack.Customizer customizer, final OnItemClickListenerWrapper onItemClickListenerWrapper) {
        super(itemView);
        this.adapter = adapter;
        this.settings = settings;
        this.customizer = customizer;
        this.onItemClickListenerWrapper = onItemClickListenerWrapper;

        layout = itemView.findViewById(R.id.row_layout);
        title = itemView.findViewById(R.id.row_title);

        recyclerView = itemView.findViewById(R.id.row_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void onBind(final int row) {
        this.row = row;

        {
            final String titleString = adapter.getTitleForRow(this.row);
            if (!adapter.hasRowTitle(row) || (titleString == null || titleString.trim().isEmpty()))
                title.setVisibility(View.GONE);
            else
                title.setText(titleString);

            title.setOnClickListener(view -> {
                if (onItemClickListenerWrapper != null) {
                    final MaterialLeanBack.OnItemClickListener onItemClickListener = onItemClickListenerWrapper.getOnItemClickListener();
                    if (onItemClickListener != null) {
                        onItemClickListener.onTitleClicked(row, titleString);
                    }
                }
            });

            if (settings.titleColor != null)
                title.setTextColor(settings.titleColor);
            if (settings.titleSize != -1)
                title.setTextSize(settings.titleSize);

            if (this.customizer != null)
                customizer.customizeTitle(title);
        }
        {
            if (settings.lineSpacing != null) {
                layout.setPadding(
                        layout.getPaddingLeft(),
                        layout.getPaddingTop(),
                        layout.getPaddingRight(),
                        settings.lineSpacing
                );
            }
        }

        recyclerView.setAdapter(new CellAdapter(row, adapter, settings, onItemClickListenerWrapper, height -> {
            if (!wrapped) {
                recyclerView.getLayoutParams().height = height;
                recyclerView.requestLayout();
                wrapped = true;
            }
        }));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    adapter.getEnlargedItemPosition(adapter.itemPosition);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                for (int i = 0; i < recyclerView.getChildCount(); ++i) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    if (viewHolder instanceof CellViewHolder) {
                        CellViewHolder cellViewHolder = ((CellViewHolder) viewHolder);
                        cellViewHolder.newPosition(i);
                    }
                }
            }
        });
    }
}