package com.achref.leanbacklib.cell

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.achref.leanbacklib.*

class CellAdapter(
    private val row: Int,
    private val adapter: MaterialLeanBack.Adapter<*>,
    private val settings: MaterialLeanBackSettings,
    private val onItemClickListenerWrapper: OnItemClickListenerWrapper?,
    private var heightCalculatedCallback: HeightCalculatedCallback?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) PLACEHOLDER_START else if (position == itemCount - 1) PLACEHOLDER_END else CELL
    }

    interface HeightCalculatedCallback {
        fun onHeightCalculated(height: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): RecyclerView.ViewHolder {
        val view: View
        when (type) {
            PLACEHOLDER_START -> {
                view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.mlb_placeholder, viewGroup, false)
                return PlaceHolderViewHolder(view, true, settings.paddingLeft)
            }
            PLACEHOLDER_END -> {
                view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.mlb_placeholder_end, viewGroup, false)
                return PlaceHolderViewHolder(view, true, settings.paddingRight)
            }
            else -> {
                view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.mlb_cell, viewGroup, false)

                //simulate wrap_content
                view.viewTreeObserver.addOnPreDrawListener(object :
                    ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        if (heightCalculatedCallback != null) heightCalculatedCallback!!.onHeightCalculated(
                            view.height
                        )
                        view.viewTreeObserver.removeOnPreDrawListener(this)
                        return false
                    }
                })
            }
        }
        return CellViewHolder(view, row, adapter, settings)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is CellViewHolder) {
            viewHolder.isEnlarged = position != 1
            viewHolder.newPosition(position - PLACEHOLDER_START_SIZE)
            viewHolder.onBind()
            viewHolder.itemView.setOnClickListener {
                if (onItemClickListenerWrapper != null) {
                    val onItemClickListener =
                        onItemClickListenerWrapper.onItemClickListener
                    onItemClickListener?.onItemClicked(row, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return adapter.getCellsCount(row) + PLACEHOLDER_START_SIZE + PLACEHOLDER_END_SIZE
    }

    companion object {
        const val PLACEHOLDER_START = 3000
        const val PLACEHOLDER_END = 3001
        const val PLACEHOLDER_START_SIZE = 1
        const val PLACEHOLDER_END_SIZE = 1
        const val CELL = 3002
    }
}