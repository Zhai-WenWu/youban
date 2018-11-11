package cn.bjhdltcdn.p2plive.widget.gift;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by huwenhua on 2016/8/11.
 */
public class GiftBigAnimation {
    public static GiftBigAnimation giftBigAnimation;
    public static Handler handler;

    private GiftBigAnimation() {
    }

    public static GiftBigAnimation getInstance() {
        if (giftBigAnimation == null) {
            giftBigAnimation = new GiftBigAnimation();
            handler = new Handler();
        }
        return giftBigAnimation;
    }

    public void showPropsAnimation(final ImageView animationView, Props props) {

        if (TextUtils.isEmpty(props.getAnimatedUrl())) {
            return;
        }

        if (animationView == null) {
            return;
        }


        try {
            if (animationView.getAnimation() != null) {
//                ViewHelper.setScaleX(animationView, 0F);
//                ViewHelper.setScaleY(animationView, 0F);

                animationView.getAnimation().cancel();
                animationView.getAnimation().reset();

            }

            if (animationView.getDrawable() != null && animationView.getDrawable() instanceof GifDrawable) {
                GifDrawable gifDrawable = (GifDrawable) animationView.getDrawable();
                gifDrawable.stop();
            }

            animationView.clearAnimation();

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(animationView, "scaleX", 0.0f, 1.0f),
                    ObjectAnimator.ofFloat(animationView, "scaleY", 0.0f, 1.0f),
                    ObjectAnimator.ofFloat(animationView, "alpha", 0, 1)
            );
            set.setDuration(1 * 1000).start();
            RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.ic_launcher);
            Glide.with(App.getInstance()).asGif().load(props.getAnimatedUrl()).apply(options)
                    .into(animationView);
//            Utils.ImageViewDisplayByUrl(props.getAnimatedUrl(), animationView);
            animationView.setVisibility(View.VISIBLE);

            if (handler == null) {
                handler = new Handler();
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (animationView.getDrawable() != null && animationView.getDrawable() instanceof GifDrawable) {
                        GifDrawable gifDrawable = (GifDrawable) animationView.getDrawable();
                        gifDrawable.stop();
//                        gifDrawable.recycle();
                    }

                    if (animationView.getAnimation() != null) {
                        animationView.getAnimation().cancel();
                    }

                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(
                            ObjectAnimator.ofFloat(animationView, "scaleX", 1.0f, 0.0f),
                            ObjectAnimator.ofFloat(animationView, "scaleY", 1.0f, 0.0f),
                            ObjectAnimator.ofFloat(animationView, "alpha", 1, 0)
                    );
                    set.setDuration(1 * 1000).start();

                    if (animationView.getAnimation() != null) {
                        animationView.getAnimation().cancel();
                    }

                }
            }, 7000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//    /**
//     * 飘屏礼物显示特效
//     *
//     * @param animationView
//     */
//    public void showPropsAnimationFutterScreenGift(final View animationView) {
//
//        if (animationView == null) {
//            return;
//        }
//
//        try {
//            if (animationView.getAnimation() != null) {
//
//                animationView.getAnimation().cancel();
//                animationView.getAnimation().reset();
//
//            }
//
//            animationView.clearAnimation();
//
//            animationView.setVisibility(View.VISIBLE);
//
//            AnimatorSet set = new AnimatorSet();
//            set.playTogether(
//                    ObjectAnimator.ofFloat(animationView, "translationX", 200, 0),
//                    ObjectAnimator.ofFloat(animationView, "translationY", 0, 0),
//                    ObjectAnimator.ofFloat(animationView, "alpha", 0, 1)
//
//            );
//            set.setDuration(1 * 1000).start();
//
//
//            if (handler == null) {
//                handler = new Handler();
//            }
//
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//
//                    if (animationView.getAnimation() != null) {
//                        animationView.getAnimation().cancel();
//                    }
//
//                    AnimatorSet set = new AnimatorSet();
//                    set.playTogether(
//                            ObjectAnimator.ofFloat(animationView, "translationX", 0, -200),
//                            ObjectAnimator.ofFloat(animationView, "translationY", 0.0f, 1.0f),
//                            ObjectAnimator.ofFloat(animationView, "alpha", 1, 0)
//                    );
//                    set.setDuration(1 * 1000).start();
//
//                    if (animationView.getAnimation() != null) {
//                        animationView.getAnimation().cancel();
//                    }
//                }
//            }, 10 * 1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public void onDestroy() {

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        handler = null;

        if (giftBigAnimation != null) {
            giftBigAnimation = null;
        }
    }


}
