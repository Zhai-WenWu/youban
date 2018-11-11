//package cn.bjhdltcdn.p2plive.widget.postsearchtagview;
//
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.ColorFilter;
//import android.graphics.Paint;
//import android.graphics.PixelFormat;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//
///**
// * Description:帖子标签
// * Data: 2018/8/6
// *
// * @author: zhudi
// */
//public class TagDrawable extends Drawable {
//
////    private Paint bgPaint;
//    private Paint textPaint;
//    private int width;
//    private int height;
//    private String text;
//    private Tag tag;
//
//    public TagDrawable(Tag tag) {
//        init(tag);
//    }
//
//    private void init(Tag tag) {
//        //初始化数据
//        this.tag = tag;
//        this.text = tag.getText();
//        if (TextUtils.isEmpty(text)) {
//            return;
//        }
////        int bgColor = tag.getBgColor() == 0 ? Color.CYAN : tag.getBgColor();
//        int textColor = tag.getTextColor() == 0 ? Color.BLACK : tag.getTextColor();
//        float textSize = tag.getTextSize() == 0 ? 40 : tag.getTextSize();
//
////        //背景画笔
////        bgPaint = new Paint();
////        bgPaint.setAntiAlias(true);
////        bgPaint.setStyle(Paint.Style.FILL);
////        bgPaint.setColor(bgColor);
//
//        //文字画笔
//        textPaint = new Paint();
//        textPaint.setAntiAlias(true);
//        textPaint.setColor(textColor);
//        textPaint.setTextAlign(Paint.Align.CENTER);
//        textPaint.setStyle(Paint.Style.FILL);
//        textPaint.setTextSize(textSize);
//
//        //测量文字的宽高
//        Rect textRect = new Rect();
//        textPaint.getTextBounds(text, 0, text.length(), textRect);
//        //背景宽度要比文字宽度宽一些
//        width = textRect.width() + (textRect.width() / text.length() * 2);
//        //背景高度是文字高度的1.5倍
//        height = (int) (textRect.height() * 1.5);
//    }
//
//    @Override
//    public void draw(@NonNull Canvas canvas) {
//        Rect rect = getBounds();
////
////        //画背景（圆角矩形框）
//        RectF rectF = new RectF(getBounds());
//        rectF.bottom = height;
////        //留出8px空白，以免添加多个tag时出现挤在一起的情况
//        rectF.right = rectF.right - 8;
////        canvas.drawRoundRect(rectF, 360, 360, bgPaint);
////
//        int count = canvas.save();
//        canvas.translate(rect.left, rect.top);
//
//        //画文字（居中）
//        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
//        float top = fontMetrics.top;//为基线到字体上边框的距离
//        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离
//        int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式
//        canvas.drawText(text, rectF.centerX(), baseLineY, textPaint);
//        canvas.restoreToCount(count);
//    }
//
//    @Override
//    public void setAlpha(int i) {
//        textPaint.setAlpha(i);
//    }
//
//    @Override
//    public void setColorFilter(@Nullable ColorFilter colorFilter) {
//        textPaint.setColorFilter(colorFilter);
//    }
//
//    @Override
//    public int getOpacity() {
//        return PixelFormat.TRANSLUCENT;
//    }
//
//    @Override
//    public int getIntrinsicWidth() {
//        return width;
//    }
//
//    @Override
//    public int getIntrinsicHeight() {
//        return height;
//    }
//
//    @Override
//    public void setBounds(int left, int top, int right, int bottom) {
//        //设置drawable的宽高
//        super.setBounds(left, top, width, height);
//    }
//
//
//    /**
//     * 改变tag的状态
//     *
//     * @param tag
//     */
//    public void invalidateDrawable(Tag tag) {
//        init(tag);
//        invalidateSelf();
//    }
//
//    public Tag getTag() {
//        return tag;
//    }
//}
