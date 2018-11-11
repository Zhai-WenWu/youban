package cn.bjhdltcdn.p2plive.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * textView中添加icon 对齐
 */
public class VerticalImageSpan extends ImageSpan {
    private int move;//位移的距离

    public VerticalImageSpan(Drawable drawable, int move) {
        super(drawable);
        this.move = move;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;//计算y方向的位移
        canvas.save();
        canvas.translate(x + Utils.dp2px(move), transY);//绘制图片位移一段距离
        b.draw(canvas);
        canvas.restore();

    }
}

