package cn.bjhdltcdn.p2plive.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wenquan on 2015/9/18.
 * android Viewpager禁用/开启滑动切换功能
 */
public class CustomViewPager extends ViewPager {
    /**
     * true 表示禁止滑动，false表示允许滑动
     */
    private boolean isCanScroll = false;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isCanScroll) {
            return false;
        } else {
            try {
                return super.onInterceptTouchEvent(event);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return false;
        }
        return !isCanScroll && super.onTouchEvent(ev);
    }

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setIsCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    //去除页面切换时的效果
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, false);
    }


}
