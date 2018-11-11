package cn.bjhdltcdn.p2plive.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import cn.bjhdltcdn.p2plive.R;

/**
 * 此类限制在2个view
 */
public class SlideMenuDrawerLayout extends ViewGroup {
    // 默认最小边距
    private static final int MIN_DEFAULT_MARGIN = 0; // dp
    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    // 是否启用滑动(默认开启)
    private boolean isEnabledScroll = true;

    /**
     * 打开slideview后，离父容器左右两边的最小外边距
     */
    private int mMinDrawerMargin;
    // 滑动的view
    private View mSlideMenuView;
    // 不能滑动的view
    private View mContentView;

    // 向右滑动
    private ViewDragHelper mRigthHelper;
    // 向左滑动
    private ViewDragHelper mLeftHelper;

    private final static String SLIDE_LEFT = "left";
    private final static String SLIDE_RIGHT = "right";

    public final static int VIEW_SLIDE_LEFT = 0;
    public final static int VIEW_SLIDE_RIGHT = 1;
    private int slideViewOrientation = VIEW_SLIDE_LEFT;

    private DrawerSwitchListenner switchListenner ;

    public DrawerSwitchListenner getSwitchListenner() {
        return switchListenner;
    }

    public void setSwitchListenner(DrawerSwitchListenner switchListenner) {
        this.switchListenner = switchListenner;
    }

    public boolean isEnabledScroll() {
        return isEnabledScroll;
    }

    public void setEnabledScroll(boolean enabledScroll) {
        isEnabledScroll = enabledScroll;
    }

    public SlideMenuDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideMenuDrawerLayout);
        String type = a.getString(R.styleable.SlideMenuDrawerLayout_slide_view_orientation);
        mMinDrawerMargin = (int) a.getDimension(R.styleable.SlideMenuDrawerLayout_slide_view_min_default_margin, MIN_DEFAULT_MARGIN);
        a.recycle();
        if (SLIDE_LEFT.equals(type)) {
            slideViewOrientation = VIEW_SLIDE_LEFT;
        } else if (SLIDE_RIGHT.equals(type)) {
            slideViewOrientation = VIEW_SLIDE_RIGHT;
        }

        //setup drawer's minMargin
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;

        switch (slideViewOrientation) {
            case VIEW_SLIDE_LEFT:
                mLeftHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
                    @Override
                    public int clampViewPositionHorizontal(View child, int left, int dx) {
                        int newLeft = Math.max(-child.getWidth(), Math.min(left, 0));
                        return newLeft;
                    }

                    @Override
                    public boolean tryCaptureView(View child, int pointerId) {
                        return child == mSlideMenuView && isEnabledScroll;
                    }

                    @Override
                    public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                        mLeftHelper.captureChildView(mSlideMenuView, pointerId);
                    }

                    @Override
                    public void onViewReleased(View releasedChild, float xvel, float yvel) {
                        //手指抬起后缓慢移动到指定位置
                        final int childWidth = releasedChild.getWidth();
                        if (mSlideMenuView.getRight() < childWidth / 2) {
                            //关闭菜单
                            mLeftHelper.smoothSlideViewTo(mSlideMenuView, -childWidth - mMinDrawerMargin, 0);
                            if (switchListenner != null) {
                                switchListenner.closeDrawer();
                            }
                        } else {
                            //打开菜单
                            mLeftHelper.smoothSlideViewTo(mSlideMenuView, 0, 0);//相当于Scroller的startScroll方法
                            if (switchListenner != null) {
                                switchListenner.openDrawer();
                            }
                        }
                        ViewCompat.postInvalidateOnAnimation(SlideMenuDrawerLayout.this);

                    }

                    @Override
                    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                        final int childWidth = changedView.getWidth();
                        float offset = (float) (childWidth + left) / childWidth;
                        //offset can callback here
                        changedView.setVisibility(offset == 0 ? View.INVISIBLE : View.VISIBLE);
                        invalidate();
                    }

                    @Override
                    public int getViewHorizontalDragRange(View child) {
                        return mSlideMenuView == child ? child.getWidth() : 0;
                    }
                });
                //设置edge_left track
                mLeftHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
                //设置minVelocity
                mLeftHelper.setMinVelocity(minVel);

                break;

            case VIEW_SLIDE_RIGHT:
                mRigthHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
                    @Override
                    public int clampViewPositionHorizontal(View child, int left, int dx) {
                        int newLeft = Math.max(left, Math.min(child.getWidth(), 0));
                        return newLeft;
                    }

                    @Override
                    public boolean tryCaptureView(View child, int pointerId) {
                        return child == mSlideMenuView && isEnabledScroll;
                    }

                    @Override
                    public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                        mRigthHelper.captureChildView(mSlideMenuView, pointerId);
                    }

                    @Override
                    public void onViewReleased(View releasedChild, float xvel, float yvel) {

                        final int childWidth = releasedChild.getWidth();

                        //手指抬起后缓慢移动到指定位置
                        if (mSlideMenuView.getLeft() < childWidth / 2) {
                            ///开启菜单
                            mRigthHelper.smoothSlideViewTo(mSlideMenuView, 0, 0);//相当于Scroller的startScroll方法
                            ViewCompat.postInvalidateOnAnimation(SlideMenuDrawerLayout.this);
                            if (switchListenner != null) {
                                switchListenner.openDrawer();
                            }
                        } else {
                            //关闭菜单
                            mRigthHelper.smoothSlideViewTo(mSlideMenuView, childWidth - 2 * mMinDrawerMargin, 0);
                            ViewCompat.postInvalidateOnAnimation(SlideMenuDrawerLayout.this);
                            if (switchListenner != null) {
                                switchListenner.closeDrawer();
                            }
                        }

                    }

                    @Override
                    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                        final int childWidth = changedView.getWidth();
                        float offset = (float) (childWidth + left) / childWidth;
                        //offset can callback here
                        changedView.setVisibility(offset == 0 ? View.INVISIBLE : View.VISIBLE);
                        invalidate();
                    }

                    @Override
                    public int getViewHorizontalDragRange(View child) {
                        return mSlideMenuView == child ? child.getWidth() : 0;
                    }
                });
                //设置edge_left track
                mRigthHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
                //设置minVelocity
                mRigthHelper.setMinVelocity(minVel);
                break;

        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("You layout must have only 2 children!");
        }

        mContentView = getChildAt(0);
        mSlideMenuView = getChildAt(1);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        if (getChildCount() != 2) {
//            return;
//        }

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);

        View contentView = getChildAt(0);
        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        contentView.measure(contentWidthSpec, contentHeightSpec);

        View leftMenuView = getChildAt(1);
        lp = (MarginLayoutParams) leftMenuView.getLayoutParams();
        int drawerWidthSpec = 0;
        switch (slideViewOrientation) {
            case VIEW_SLIDE_LEFT:
                drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec, mMinDrawerMargin + lp.leftMargin + lp.rightMargin, lp.width);
                break;
            case VIEW_SLIDE_RIGHT:
                drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec, -mMinDrawerMargin + lp.leftMargin + lp.rightMargin, lp.width);
                break;
        }

        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height);
        leftMenuView.measure(drawerWidthSpec, drawerHeightSpec);

        mSlideMenuView = leftMenuView;
        mContentView = contentView;


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View menuView = mSlideMenuView;
        View contentView = mContentView;

        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        contentView.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + contentView.getMeasuredWidth(), lp.topMargin + contentView.getMeasuredHeight());

        lp = (MarginLayoutParams) menuView.getLayoutParams();

        final int menuWidth = menuView.getMeasuredWidth();
        int childLeft = -menuWidth;
        switch (slideViewOrientation) {
            case VIEW_SLIDE_LEFT:
                menuView.layout(childLeft, lp.topMargin, childLeft + menuWidth, lp.topMargin + menuView.getMeasuredHeight());
                break;
            case VIEW_SLIDE_RIGHT:
                menuView.layout(0, lp.topMargin, menuWidth, lp.topMargin + menuView.getMeasuredHeight());
                break;
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldInterceptTouchEvent = false;
        switch (slideViewOrientation) {
            case VIEW_SLIDE_LEFT:
                shouldInterceptTouchEvent = mLeftHelper.shouldInterceptTouchEvent(ev);
                break;
            case VIEW_SLIDE_RIGHT:
                shouldInterceptTouchEvent = mRigthHelper.shouldInterceptTouchEvent(ev);
                break;
        }
        return shouldInterceptTouchEvent;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (slideViewOrientation) {
            case VIEW_SLIDE_LEFT:
                mLeftHelper.processTouchEvent(event);
                break;
            case VIEW_SLIDE_RIGHT:
                mRigthHelper.processTouchEvent(event);
                break;
        }
        return true;
    }


    @Override
    public void computeScroll() {
        boolean iscontinueSettling = false;
        switch (slideViewOrientation) {
            case VIEW_SLIDE_LEFT:
                iscontinueSettling = mLeftHelper.continueSettling(true);
                break;
            case VIEW_SLIDE_RIGHT:
                iscontinueSettling = mRigthHelper.continueSettling(true);
                break;
        }

        if (iscontinueSettling) {
            invalidate();
        }
    }

    public void closeDrawer() {
        View menuView = mSlideMenuView;
        switch (slideViewOrientation) {
            case VIEW_SLIDE_LEFT:
                mLeftHelper.smoothSlideViewTo(menuView, -menuView.getWidth(), menuView.getTop());
                break;
            case VIEW_SLIDE_RIGHT:
                mRigthHelper.smoothSlideViewTo(menuView, menuView.getWidth() - 2 * mMinDrawerMargin, menuView.getTop());
                break;
        }


    }

    public void openDrawer() {
        View menuView = mSlideMenuView;
        switch (slideViewOrientation) {
            case VIEW_SLIDE_LEFT:
                mLeftHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
                break;
            case VIEW_SLIDE_RIGHT:
                mRigthHelper.smoothSlideViewTo(menuView, 0, 0);
                break;

        }

        ViewCompat.postInvalidateOnAnimation(SlideMenuDrawerLayout.this);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    public interface DrawerSwitchListenner{
        void openDrawer();
        void closeDrawer();
    }

}
