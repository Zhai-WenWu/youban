package cn.bjhdltcdn.p2plive.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置recyclerView中item间距
 */
public class LinearLayoutSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private boolean isVertical;//true为纵向列表
    private boolean isTopPadding;

    public LinearLayoutSpaceItemDecoration(int space) {
        this.space = space;
        isVertical = true;
    }

    public LinearLayoutSpaceItemDecoration(int space, boolean isVertical) {
        this.space = space;
        this.isVertical = isVertical;
    }

    public LinearLayoutSpaceItemDecoration(int space, boolean isVertical,boolean isTopPadding) {
        this.space = space;
        this.isVertical = isVertical;
        this.isTopPadding = isTopPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (isVertical) {
            if (isTopPadding) {
                outRect.top = space;
            }
            outRect.bottom = space;
//            outRect.left = 0;
//            outRect.right = 0;
        } else {
//            outRect.top = 0;
//            outRect.left = 0;

            outRect.right = space;
        }

//        final int itemCount = parent.getAdapter().getItemCount();
//        // 第一个位置
//        int position = parent.getChildLayoutPosition(view);
//        if (position == (itemCount - 1)) {// 最后一个item
//            outRect.bottom = space;
//        }

    }


}