package cn.bjhdltcdn.p2plive.utils;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by Hu_PC on 2018/1/3.
 */

public class Solve7PopupWindow extends PopupWindow {


    public Solve7PopupWindow(View mMenuView, int matchParent, int matchParent1) {
        super(mMenuView, matchParent,matchParent1);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }
}
