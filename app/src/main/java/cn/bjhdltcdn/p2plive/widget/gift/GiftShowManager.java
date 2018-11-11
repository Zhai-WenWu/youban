package cn.bjhdltcdn.p2plive.widget.gift;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 礼物显示管理类
 * 主要礼物逻辑：
 * 1. 利用一个LinkedBlockingQueue来存储所有的礼物的实体类
 * 2. 利用Handler的消息机制，每隔一段时间从队列中取一次礼物出来
 * {
 * 2.1 如果取得礼物为空（队列中没有礼物），那么就延迟一段时间之后再次从队列中取出礼物
 * 2.2 如果从队列中取出的礼物不为空，则根据送礼物的人的UserId去寻找这个礼物是否正在显示，如果不在显示，则新建一个，如果正在显示，则直接修改数量
 * }
 * 3. 这个礼物View的管理类中一直存在一个定时器在轮循礼物的容器下面的所有的礼物的View，当有礼物的View上次的更新时间超过最长显示时间，则移除这个View
 * {
 * 3.1 礼物容器中显示的礼物达到两条，并且新获取的礼物和他们两个不一样，那么需要移除一个来显示新的礼物
 * 3.2 判断所有的里面的出现的时间，然后把显示最久的先移除掉（需要考虑到线程安全）
 * 3.3 定时器的线程会更新View，在获取礼物的时候也会更新View（增加线程安全控制）
 * }
 */

public class GiftShowManager {
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 礼物的队列
     */
    private ArrayBlockingQueue<VideoCMDTextMessageModel> blockingQueue;

    /**
     * 礼物的容器
     */
    private LinearLayout giftInFoContainerView;

    /**
     * 礼物View出现的动画
     */
    private TranslateAnimation inAnim;
    /**
     * 礼物View消失的动画
     */
    private TranslateAnimation outAnim;

    /**
     * 垃圾回收view
     */
    private List<View> viewArrayList = new ArrayList<>();

    /**
     * 队列大小
     */
    private final int QUEUE_SIZE = 500;

    /**
     * 轮询礼物容器的所有的子View判断是否超过显示的最长时间
     */
    private Timer timer;

    /**
     * 从队列中获取礼物
     */
    private final static int GET_QUEUE_GIFT = 10000;

    /**
     * 显示礼物
     */
    private final static int SHOW_GIFT_FLAG = 20000;

    /**
     * 当礼物的View显示超时，删除礼物View
     */
    private final static int REMOVE_GIFT_VIEW = 30000;

    /**
     * 是否停止标记
     */
    private boolean isStop = false;

    /**
     * 延迟时间(毫秒)
     */
    private long DELAY_MILLIS = 500;

    /**
     * 修改礼物数量的动画
     */
    private NumAnimator giftNumAnimator;

    /**
     * 消息处理器
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isStop) {
                return;
            }

            switch (msg.what) {
                case GET_QUEUE_GIFT://如果是从队列中获取礼物实体的消息
                    if (blockingQueue == null) {
                        return;
                    }

                    //如果从队列中获取的礼物不为空，那么就将礼物展示在界面上
                    VideoCMDTextMessageModel videoCMDTextMessageModel = blockingQueue.poll();
                    if (videoCMDTextMessageModel != null) {
                        Message giftMsg = Message.obtain();
                        giftMsg.obj = videoCMDTextMessageModel;
                        giftMsg.what = SHOW_GIFT_FLAG;
                        handler.sendMessage(giftMsg);
                    } else {
                        //如果这次从队列中获取的消息是礼物是空的，则一定时间之后重新获取
                        handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, DELAY_MILLIS);
                    }
                    break;

                case SHOW_GIFT_FLAG://处理显示礼物的消息
                    if (giftInFoContainerView == null) {
                        return;
                    }


                    final VideoCMDTextMessageModel videoCMDTextMessageModel1 = (VideoCMDTextMessageModel) msg.obj;

                    if (videoCMDTextMessageModel1 == null) {
                        return;
                    }

                    if (handler == null) {
                        return;
                    }

                    //显示道具特效
                    Props props = videoCMDTextMessageModel1.getProps();
                    if (videoCMDTextMessageModel1.getBaseUser() != null) {
                        props.setGenerateId(videoCMDTextMessageModel1.getBaseUser().getUserId() + "_" + props.getPropsId());
                    }

                    if (props == null) {
                        return;
                    }

                    int num = 1;
                    View giftView = giftInFoContainerView.findViewWithTag(props.getGenerateId());
                    //获取的礼物的实体，判断送的人不在显示
                    if (giftView == null) {
                        //首先需要判断下Gift ViewGroup下面的子View是否超过两个
                        int count = giftInFoContainerView.getChildCount();
                        if (count >= 2) {//如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的
                            View giftView1 = giftInFoContainerView.getChildAt(0);

                            TextView nameTv1 = (TextView) giftView1.findViewById(R.id.gift_name_view);
                            long lastTime1 = (Long) nameTv1.getTag();

                            View giftView2 = giftInFoContainerView.getChildAt(1);
                            TextView nameTv2 = (TextView) giftView2.findViewById(R.id.gift_name_view);
                            long lastTime2 = (Long) nameTv2.getTag();
                            Message rmMsg = new Message();
                            if (lastTime1 > lastTime2) {//如果第二个View显示的时间比较长
                                rmMsg.obj = 1; // 发送下标

                            } else {//如果第一个View显示的时间长
                                rmMsg.obj = 0;// 发送下标
                            }
                            rmMsg.what = REMOVE_GIFT_VIEW;
                            handler.sendMessage(rmMsg);
                        }

                        //获取礼物的View的布局
                        giftView = obtainView();

                        //设置view标识
                        giftView.setTag(props.getGenerateId());


                        //显示礼物的数量
                        TextView tv = (TextView) giftView.findViewById(R.id.gift_name_view);
                        tv.setVisibility(View.VISIBLE);
                        final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.gift_num);
//                        if (props.getPropsId() == 27) {
//                            giftNum.setVisibility(View.GONE);
//                            tv.setText(String.format("花费了%d金币", pushMessage1.getGoldCount()));
//                        } else {
                        giftNum.setTag(num);
                        giftNum.setText("X" + num);
                        giftNum.setVisibility(View.VISIBLE);
                        tv.setText("赠送了一个" + props.getPropsName());
//                        }


                        tv.setTag(System.currentTimeMillis());
                        //将礼物的View添加到礼物的ViewGroup中
                        giftInFoContainerView.addView(giftView);

                        giftView.startAnimation(inAnim);//播放礼物View出现的动
                        inAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                if (giftNumAnimator == null) {
                                    return;
                                }
                                giftNumAnimator.start(giftNum);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });

                    } else {//如果送的礼物正在显示（只是修改下数量）

                        TextView tv = (TextView) giftView.findViewById(R.id.gift_name_view);
                        tv.setTag(System.currentTimeMillis());
                        tv.setVisibility(View.VISIBLE);
                        //显示礼物的数量
                        final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.gift_num);
//                        if (props.getPropsId() == 27) {
//                            giftNum.setVisibility(View.GONE);
//                        } else {
                        int showNum = (Integer) giftNum.getTag() + num;
                        giftNum.setText("X" + (showNum));
                        giftNum.setTag(showNum);
                        giftNum.setVisibility(View.VISIBLE);
//                        }
                        tv.setText("赠送了一个" + props.getPropsName());

                        if (giftNumAnimator != null) {
                            giftNumAnimator.start(giftNum);
                        }

                    }

                    // 设置个人消息
                    BaseUser msgUser = videoCMDTextMessageModel1.getBaseUser();
                    if (msgUser != null) {
                        //头像
                        ImageView userImageView = (ImageView) giftView.findViewById(R.id.user_image_view);
                        Utils.ImageViewDisplayByUrl(msgUser.getUserIcon(), userImageView);
                        //名称
                        TextView userNameView = (TextView) giftView.findViewById(R.id.user_name_view);
                        //道具图片
                        ImageView giftViewItem = (ImageView) giftView.findViewById(R.id.gift_view);
                        giftViewItem.setVisibility(View.VISIBLE);

//                        if (props.getPropsId() == 27) {
//                            userNameView.setText(msgUser.getNickName() + "抢到了头排");
//                            giftViewItem.setVisibility(View.GONE);
//                        } else {
                        userNameView.setText(msgUser.getNickName());
                        Utils.ImageViewDisplayByUrl(props.getPropUrl(), giftViewItem);
                        giftViewItem.setVisibility(View.VISIBLE);
//                        }
                    }

                    break;

                case REMOVE_GIFT_VIEW:
                    int index = (int) msg.obj;
                    if (giftInFoContainerView == null) {
                        return;
                    }

                    final View removeView = giftInFoContainerView.getChildAt(index);
                    if (removeView == null) {
                        return;
                    }

                    if (outAnim == null) {
                        return;
                    }

                    outAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (giftInFoContainerView == null) {
                                return;
                            }

                            giftInFoContainerView.removeView(removeView);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    removeView.startAnimation(outAnim);
                    break;
            }
        }
    };


    public GiftShowManager(Context cxt, final LinearLayout giftInFoContainerView) {
        this.mContext = cxt;
        this.giftInFoContainerView = giftInFoContainerView;
        blockingQueue = new ArrayBlockingQueue<VideoCMDTextMessageModel>(QUEUE_SIZE);
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(cxt, R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(cxt, R.anim.gift_out);
        giftNumAnimator = new NumAnimator();


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (GiftShowManager.this.giftInFoContainerView == null) {
                    return;
                }

                if (handler == null) {
                    return;
                }

                int count = GiftShowManager.this.giftInFoContainerView.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = GiftShowManager.this.giftInFoContainerView.getChildAt(i);
                    TextView nameView = (TextView) view.findViewById(R.id.gift_name_view);
                    long nowtime = System.currentTimeMillis();
                    long upTime = (long) nameView.getTag();
                    if ((nowtime - upTime) >= 10000) {
                        Message msg = Message.obtain();
                        msg.obj = i;// view 下标
                        msg.what = REMOVE_GIFT_VIEW;
                        handler.sendMessage(msg);
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 2000, 2000);

    }

    /**
     * 创建一个view
     *
     * @return
     */
    private View obtainView() {
        if (viewArrayList == null) {
            return null;
        }

        View view = null;
        if (viewArrayList.size() <= 0) {
            //如果垃圾回收中没有view,则生成一个
            view = View.inflate(mContext, R.layout.gift_item, null);
            view.findViewById(R.id.ll_bg).setBackgroundDrawable(new GiftBgDrawable());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            giftInFoContainerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {


                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    if (viewArrayList != null) {
                        viewArrayList.add(view);
                    }
                }
            });
        } else {
            view = viewArrayList.get(0);
            viewArrayList.remove(view);
        }

        return view;
    }

    //开始显示礼物
    public void showGift() {
        handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, DELAY_MILLIS);//轮询队列获取礼物
    }

    /**
     * 放入礼物到队列
     *
     * @param pushMessage
     * @return 添加是否成功
     */
    public boolean addGift(VideoCMDTextMessageModel pushMessage) {
        if (pushMessage == null) {
            return false;
        }

        if (blockingQueue == null) {
            return false;
        }

        if (blockingQueue.size() >= QUEUE_SIZE) {
            return false;
        } else {
            return blockingQueue.add(pushMessage);
        }
    }

    /**
     * 礼物停止
     */
    public void stop() {
        isStop = true;
        if (timer != null) {
            timer.cancel();
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        if (blockingQueue != null) {
            blockingQueue.clear();
        }
        blockingQueue = null;

        if (viewArrayList != null) {
            viewArrayList.clear();
        }
        viewArrayList = null;

        if (giftInFoContainerView != null) {
            giftInFoContainerView.removeAllViews();
        }
        giftInFoContainerView = null;
    }


    /**
     * 修改礼物数量的动画
     */
    public class NumAnimator {
        private Animator lastAnimator = null;

        public void start(View view) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }

            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",
                    1.5f, 1.0f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",
                    1.5f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            lastAnimator = animSet;
            animSet.setDuration(200);
            animSet.setInterpolator(new OvershootInterpolator());
            //两个动画同时执行
            animSet.playTogether(anim1, anim2);
            animSet.start();
            animSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, DELAY_MILLIS);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, DELAY_MILLIS);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });

        }

    }
}
