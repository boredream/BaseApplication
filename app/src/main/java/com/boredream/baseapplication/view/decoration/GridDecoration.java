package com.boredream.baseapplication.view.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;

public class GridDecoration extends RecyclerView.ItemDecoration {

    private int padding;
    private int itemColumnCount;

    public GridDecoration(int padding, int itemColumnCount) {
        this.padding = padding;
        this.itemColumnCount = itemColumnCount;
    }

    public GridDecoration() {
        itemColumnCount = 3;
        padding = SizeUtils.dp2px(8);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(padding, padding, padding, padding);
    }
}
