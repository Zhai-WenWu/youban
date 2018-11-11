package cn.bjhdltcdn.p2plive.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;


/**
 * 设置recyclerView中GridLayout布局item间距
 */
public class GridLayoutSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;     //分割线Drawable
    private int mSpaceWidth; // 分割线大小
    private int mSpanCount; // RecyclerView有多少列
    private boolean mHasPadding; // RecyclerView是否有Padding

    /**
     * @param mSpaceWidth      分割线大小
     * @param mSpanCount  行数
     * @param mHasPadding 是否有Padding
     */
    public GridLayoutSpacesItemDecoration(int mSpaceWidth, int mSpanCount, boolean mHasPadding) {
        this.mSpaceWidth = mSpaceWidth;
        this.mSpanCount = mSpanCount;
        this.mHasPadding = mHasPadding;
    }

    /**
     * @param mSpaceWidth      分割线大小
     * @param mSpanCount  行数
     * @param mHasPadding 是否有Padding
     * @param colorId     分割线颜色
     */
    public GridLayoutSpacesItemDecoration(int mSpaceWidth, int mSpanCount, boolean mHasPadding, int colorId) {
        this.mSpaceWidth = mSpaceWidth;
        this.mSpanCount = mSpanCount;
        this.mHasPadding = mHasPadding;
        mDivider = new ColorDrawable(colorId);

    }

    /**
     * @param mSpace      分割线大小
     * @param mSpanCount  行数
     * @param mHasPadding 是否有Padding\
     * @param divider     分割线
     */
    public GridLayoutSpacesItemDecoration(int mSpace, int mSpanCount, boolean mHasPadding, Drawable divider) {
        this.mSpaceWidth = mSpace;
        this.mSpanCount = mSpanCount;
        this.mHasPadding = mHasPadding;
        mDivider = divider;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 初始化列数
        if (mSpanCount == 0) {
            this.mSpanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        }
        // item下标
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % mSpanCount; // item column

        if (mHasPadding) {
            outRect.left = mSpaceWidth - column * mSpaceWidth / mSpanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * mSpaceWidth / mSpanCount; // (column + 1) * ((1f / spanCount) * spacing)
            if (position < mSpanCount) { // top edge
                outRect.top = mSpaceWidth;
            }
            outRect.bottom = mSpaceWidth; // item bottom
        } else {
            outRect.left = column * mSpaceWidth / mSpanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = mSpaceWidth - (column + 1) * mSpaceWidth / mSpanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= mSpanCount) {
                outRect.top = mSpaceWidth; // item top
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        if (layoutManager == null || layoutManager.getItemCount() == 0 || mDivider == null) {
            return;
        }

        drawHorizontal(c, parent);
        drawVertical(c, parent);

    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) {
                return;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (params == null) {
                return;
            }
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin + mSpaceWidth;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mSpaceWidth;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

        // 绘制最左边的left
        if (mHasPadding) {
            for (int i = 0; i < childCount; i++) {
                if (i % mSpanCount == 0) {
                    final View child = parent.getChildAt(i);
                    if (child == null) {
                        return;
                    }
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    if (params == null) {
                        return;
                    }
                    final int top = child.getTop() + params.topMargin;
                    int bottom = child.getBottom() + params.bottomMargin + mSpaceWidth;
                    final int left = child.getLeft() + params.rightMargin;
                    final int right = left + mSpaceWidth;
                    mDivider.setBounds(0, top, mSpaceWidth, bottom);
                    mDivider.draw(c);

                }

            }
        }


    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) {
                return;
            }

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (params == null) {
                return;
            }
            final int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin;
            if (mHasPadding) {
                right = child.getRight() + params.rightMargin + mSpaceWidth;
            }
            int top = child.getBottom() - params.bottomMargin;
            final int bottom = top + mSpaceWidth;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

        //绘制第一行top
        if (mHasPadding) {
            for (int i = 0; i < mSpanCount; i++) {
                final View child = parent.getChildAt(i);
                if (child == null) {
                    return;
                }
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                if (params == null) {
                    return;
                }

                final int left = child.getLeft() - params.leftMargin - mSpaceWidth;
                final int right = child.getRight() + params.rightMargin + mSpaceWidth;
                final int top = child.getTop() - params.bottomMargin - mSpaceWidth;
                final int bottom = top + mSpaceWidth;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

    }


}