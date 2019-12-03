package com.man.welfare.welfare;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AppinfoiItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        int pos = parent.getChildAdapterPosition(view);
        //outRect.left = 10;
        outRect.bottom = 2;
        if (pos != 0) {
            if (pos % 2 == 0) {  //下面一行
                outRect.bottom = 2;
                outRect.top = 2;
            } else { //上面一行
                outRect.bottom = 2;
                outRect.top = 2;
            }

        }
    }
}
