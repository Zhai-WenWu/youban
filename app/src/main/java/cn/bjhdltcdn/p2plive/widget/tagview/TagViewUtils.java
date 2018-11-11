package cn.bjhdltcdn.p2plive.widget.tagview;

import android.content.Context;

/**
 * Created by xiawenquan on 17/7/20.
 */

public class TagViewUtils {
    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
