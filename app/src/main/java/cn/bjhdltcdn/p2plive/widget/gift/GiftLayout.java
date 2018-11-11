package cn.bjhdltcdn.p2plive.widget.gift;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import cn.bjhdltcdn.p2plive.app.App;

/**
 * 主要礼物逻辑：
 * 1. 利用一个LinkedBlockingQueue来存储所有的礼物的实体类
 * 2. 利用Handler的消息机制，每隔一段时间从队列中取一次礼物出来
 *  {
 *      2.1 如果取得礼物为空（队列中没有礼物），那么就延迟一段时间之后再次从队列中取出礼物
 *      2.2 如果从队列中取出的礼物不为空，则根据送礼物的人的UserId去寻找这个礼物是否正在显示，如果不在显示，则新建一个，如果正在显示，则直接修改数量
 *  }
 * 3. 这个礼物View的管理类中一直存在一个定时器在轮循礼物的容器下面的所有的礼物的View，当有礼物的View上次的更新时间超过最长显示时间，则移除这个View
 *  {
 *      3.1 礼物容器中显示的礼物达到两条，并且新获取的礼物和他们两个不一样，那么需要移除一个来显示新的礼物
 *      3.2 判断所有的里面的出现的时间，然后把显示最久的先移除掉（需要考虑到线程安全）
 *      3.3 定时器的线程会更新View，在获取礼物的时候也会更新View（增加线程安全控制）
 *  }
 */
public class GiftLayout extends LinearLayout {

    private GiftShowManager giftManger;

    public GiftLayout(Context context) {
        super(context);
        init(context);
    }

    public GiftLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GiftLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setLayoutTransition(new LayoutTransition());

        int padding = (int) dp2px(5);
        setOrientation(VERTICAL);
        setPadding(padding, padding, padding, padding);
        giftManger = new GiftShowManager(context, this);

    }

    public GiftShowManager getGiftManger() {
        return giftManger;
    }

    /**
     * dip转换px
     */
    public static float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, App.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * 开始显示礼物
     */
    public void start() {
        giftManger.showGift();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (giftManger != null) {
            giftManger.stop();
        }
        giftManger = null ;
    }
}
