package com.gionee.gnvoiceassist.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

/**
 * Created by liyingheng on 10/23/17.
 */

public class HomeRecyclerView extends RecyclerView {

    public HomeRecyclerView(Context context) {
        super(context);
    }

    public HomeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * 触摸事件
     * @param ev
     */
//    public void commOnTouchEvent(MotionEvent ev) {
//        int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_UP:
//                // 手指松开.
//                if (isNeedAnimation()) {
//                    animation();
//                    isCount = false;
//                }
//                break;
//            /***
//             * 排除出第一次移动计算，因为第一次无法得知y坐标， 在MotionEvent.ACTION_DOWN中获取不到，
//             * 因为此时是MyScrollView的touch事件传递到到了LIstView的孩子item上面.所以从第二次计算开始.
//             * 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0. 之后记录准确了就正常执行.
//             */
//            case MotionEvent.ACTION_MOVE:
//                final float preY = y;// 按下时的y坐标
//                float nowY = ev.getY();// 时时y坐标
//                int deltaY = (int) (preY - nowY);// 滑动距离
//                if (!isCount) {
//                    deltaY = 0; // 在这里要归0.
//                }
//
//                y = nowY;
//                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
//                if (isNeedMove()) {
//                    Log.e("TAG", "--------------11111");
//                    Log.e("TAG", "-------1111inner.getTop() = " + inner.getTop());
//                    Log.e("TAG", "-------1111inner.inner.getBottom() = " + inner.getBottom());
//                    // 初始化头部矩形
//                    if (normal.isEmpty()) {
//                        // 保存正常的布局位置
//                        normal.set(inner.getLeft(), inner.getTop(),
//                                inner.getRight(), inner.getBottom());
//                    }
////                    Log.e("TAG", "矩形：" + inner.getLeft() + "," + inner.getTop()
////                            + "," + inner.getRight() + "," + inner.getBottom());
//                    // 移动布局
//                    inner.layout(inner.getLeft(), inner.getTop() - deltaY / 2,
//                            inner.getRight(), inner.getBottom() - deltaY / 2);
//                }
//                isCount = true;
//                break;
//
//            default:
//                break;
//        }
//    }


    public void addView(View v) {

    }

    public void removeView(int position) {

    }

}
