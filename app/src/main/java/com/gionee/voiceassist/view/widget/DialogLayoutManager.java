package com.gionee.voiceassist.view.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by liyingheng on 1/19/18.
 */

public class DialogLayoutManager extends LinearLayoutManager {

    private final float SCROLL_SPEED_MS_PERINCH = 3f;
    private float mSpeedRatio = 1f;

    public DialogLayoutManager(Context context) {
        super(context);
    }

    public DialogLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public DialogLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller scroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return DialogLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                // 每滚动一个像素所需要的时间（ms为单位）
                // 每像素所需滚动时间 = 每dp滚动时间 / 像素密度
                return SCROLL_SPEED_MS_PERINCH / displayMetrics.density;
            }
        };
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int origin = super.scrollVerticallyBy((int)(dy * mSpeedRatio), recycler, state);
        if (origin == (int) (dy * mSpeedRatio)) {
            return dy;
        }
        return origin;
    }



    public void scrollToBottom(RecyclerView recyclerView) {
        scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }

    public void setScrollSpeedRatio(float speedRatio) {
        mSpeedRatio = speedRatio;
    }

}
