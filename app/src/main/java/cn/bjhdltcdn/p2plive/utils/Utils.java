package cn.bjhdltcdn.p2plive.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.model.OriginalInfo;
import cn.bjhdltcdn.p2plive.model.ProductImage;
import cn.bjhdltcdn.p2plive.model.StoreImage;
import cn.bjhdltcdn.p2plive.model.User;

/**
 * Created by wenquan on 2015/10/16.
 */
public class Utils {
    /**
     * 媒体播放类
     */
    private static MediaPlayer mediaPlayer;

    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, App.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * toast弹出
     */
    private static Toast toast;

    public static void showToastShortTime(String content) {
        if (toast == null) {
            toast = Toast.makeText(App.getInstance(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void showToastLongTime(String content) {
        if (toast == null) {
            toast = Toast.makeText(App.getInstance(), content, Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * 图片显示
     *
     * @param imgUrl    图片的路径
     * @param imageView 控件
     */
    public static void ImageViewDisplayByUrl(String imgUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.error_bg);
        Glide.with(App.getInstance()).asBitmap().load(imgUrl).apply(options).into(imageView);
    }

    /**
     * 图片显示
     *
     * @param imgUrl    图片的路径
     * @param imageView 控件
     */
    public static void ImageViewDisplayByUrl(int imgUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.error_bg);
        Glide.with(App.getInstance()).asBitmap().load(imgUrl).apply(options).into(imageView);
    }

    /**
     * 圆角图片显示
     *
     * @param imgUrl         图片的路径
     * @param imageView      控件
     * @param roundTransform 圆角值
     */
    public static void CornerImageViewDisplayByUrl(String imgUrl, ImageView imageView, int roundTransform) {
        RequestOptions options = new RequestOptions().centerCrop().transform(new GlideRoundTransform(roundTransform)).error(R.mipmap.error_bg);
        Glide.with(App.getInstance()).asBitmap().load(imgUrl).apply(options).into(imageView);
    }

    /**
     * 动态图片显示
     *
     * @param imgUrl    图片的路径
     * @param imageView 控件
     */
    public static void ImageViewGifDisplayByUrl(String imgUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.error_bg);
        Glide.with(App.getInstance()).asGif().load(imgUrl).apply(options).into(imageView);
    }

    /**
     * 动态圆角图片显示
     *
     * @param imgUrl    图片的路径
     * @param imageView 控件
     */

    static RequestOptions gifOptions = new RequestOptions()
            .centerCrop()
            .transform(new GlideRoundTransform(9))
            .placeholder(R.mipmap.error_bg)
            .error(R.mipmap.error_bg);

    public static void CornerImageViewGifDisplayByUrl(String imgUrl, ImageView imageView) {
        Glide.with(App.getInstance()).asGif().load(imgUrl).apply(gifOptions).into(imageView);
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    public static void hideNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View decorview = activity.getWindow().getDecorView();
            decorview.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 唤醒屏幕
     *
     * @param context
     */
    public static void wakeUpAndUnlock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    /**
     * 播发声音
     */
    public static void playVoice(String musicName) {
        //开始播放来电铃声
        AssetFileDescriptor fileDescriptor;
        try {
            fileDescriptor = App.getInstance().getAssets().openFd(musicName);
            if (getMediaPlayer().isPlaying()) {
                getMediaPlayer().stop();
            }
            getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
            getMediaPlayer().setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            getMediaPlayer().setLooping(true);
            getMediaPlayer().prepare();
            getMediaPlayer().start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取媒体播放类
     *
     * @return
     */
    private static MediaPlayer getMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        return mediaPlayer;
    }

    /**
     * 释放资源
     */
    public static void releaseSource() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        mediaPlayer = null;
    }


    /**
     * 隐藏输入法
     *
     * @param editText
     */
    public static void hideSystemSoftInputKeyboard(EditText editText) {
        if (editText != null) {
            editText.clearFocus();
            final InputMethodManager imm = (InputMethodManager)
                    App.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
        }
    }

    public static String getUUid() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * 检测当前页面是否在前台
     *
     * @param c
     * @param className
     * @return true 在前台，false在后台
     */
    public static boolean isForeground(Context c, String className) {
        if (c == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            int index = cpn.getClassName().lastIndexOf(".");
            if (index > -1) {
                try {
                    String temp = cpn.getClassName().substring(index + 1, cpn.getClassName().length());
                    if (className.equals(temp)) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }

    /**
     * 获取视频的房间号
     *
     * @return
     */
    public static String getRoomNumber() {
        return "hdlt:" + getUUid().toString().toLowerCase();
    }

    /**
     * user转换baseuser
     *
     * @param user
     * @return
     */
    public static BaseUser userToBaseUser(User user) {
        if (user != null) {
            BaseUser baseUser = new BaseUser();
            baseUser.setUserId(user.getUserId());
            baseUser.setUserName(user.getUserName());
            baseUser.setNickName(user.getNickName());
            baseUser.setUserIcon(user.getUserIcon());
            baseUser.setUserBigIcon(user.getUserBigIcon());
            baseUser.setUserLevel(user.getUserLevel());
            baseUser.setSex(user.getSex());
            baseUser.setAge(user.getAge());
            baseUser.setLocation(user.getLocation());
            baseUser.setCity(user.getCity());
            baseUser.setSchoolName(user.getSchoolInfo() != null ? user.getSchoolInfo().getSchoolName() : user.getSchoolName());
            baseUser.setPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : user.getUserName());
            return baseUser;
        }
        return null;
    }

    /**
     * 数量超过9999，1w，2w显示
     *
     * @param count  数量
     * @param bigStr 转换后的String
     * @return
     */
    public static String bigNumberToStr(int count, String bigStr) {
        if (count > 9999) {
            return bigStr;
        } else {
            return String.valueOf(count);
        }
    }

    /**
     * 拼接跟拍内容以及原内容
     *
     * @param originalInfo 跟拍对象
     * @param content      原内容
     * @return
     */
    public static SpannableString getFollowContent(OriginalInfo originalInfo, String content) {
        //被跟拍人名称
        String followNickName = originalInfo.getNickName();
        int nickNameLengt = followNickName.length();
        if (nickNameLengt > 4) {
            followNickName = followNickName.substring(0, 4) + "...";
        }
        //被跟拍人的内容
        String followContent = originalInfo.getContent();
        int contentLength = followContent.length();
        if (contentLength > 5) {
            followContent = followContent.substring(0, 5) + "...";
        }
        SpannableString spannableString;
        if (StringUtils.isEmpty(followContent)) {
            spannableString = new SpannableString("我跟拍了#" + followNickName + "发布的视频# " + content);
            spannableString.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_536895)), 0, followNickName.toString().length() + 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString = new SpannableString("我跟拍了#" + followNickName + "发布的视频：" + followContent + "# " + content);
            spannableString.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_536895)), 0, followNickName.toString().length() + 13 + followContent.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    /**
     * 拼接参赛内容以及原内容
     *
     * @param labelName      一级标签名称
     * @param scondlabelList 二级标签列表
     * @param content        原内容
     * @return
     */
    public static SpannableString getMatchContent(Long labelId, String labelName, List<String> scondlabelList, String content) {

        StringBuilder stringBuilder = new StringBuilder();
        String secondLabelStr = "";
        if (scondlabelList != null) {
            for (int i = 0; i < scondlabelList.size(); i++) {
                String scondlabelName = scondlabelList.get(i);
                if (i == 0) {
                    stringBuilder.append("：" + scondlabelName);
                } else {
                    stringBuilder.append("、" + scondlabelName);
                }
            }
        }
        secondLabelStr = stringBuilder.toString();

        //标签内容
        labelName = labelName + secondLabelStr;
        int labelLength = labelName.toString().length();
        if (labelLength > 15) {
            labelName = labelName.substring(0, 15) + "...";
        }
        SpannableString spannableString;
        spannableString = new SpannableString("我参加了#" + labelName + "# " + content);
        int textColor = R.color.color_8b572a;
        if (labelId == 21) {
            textColor = R.color.color_4a90e2;
        }
        spannableString.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(textColor)), 0, labelName.toString().length() + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 拼接跟拍内容以及参赛标签以及原内容
     *
     * @param originalInfo 跟拍对象
     * @param content      原内容
     * @return
     */
    public static SpannableString getFollowAndMatchContent(OriginalInfo originalInfo, String labelName, List<String> scondlabelList, String content) {
        //被跟拍人名称
        String followNickName = originalInfo.getNickName();
        int nickNameLengt = followNickName.length();
        if (nickNameLengt > 4) {
            followNickName = followNickName.substring(0, 4) + "...";
        }
        //标签
        StringBuilder stringBuilder = new StringBuilder();
        String secondLabelStr = "";
        if (scondlabelList != null) {
            for (int i = 0; i < scondlabelList.size(); i++) {
                String scondlabelName = scondlabelList.get(i);
                if (i == 0) {
                    stringBuilder.append("：" + scondlabelName);
                } else {
                    stringBuilder.append("、" + scondlabelName);
                }
            }
        }
        secondLabelStr = stringBuilder.toString();

        //标签内容
        labelName = labelName + secondLabelStr;
        int labelLength = labelName.toString().length();
        if (labelLength > 15) {
            labelName = labelName.substring(0, 15) + "...";
        }
        SpannableString spannableString;

        spannableString = new SpannableString("我跟拍了“" + followNickName + "”发布的视频，参加了#" + labelName + "# " + content);
        spannableString.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_536895)), 0, followNickName.toString().length() + 18 + labelName.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 跳转到本应用的权限设置界面
     *
     * @param context
     */
    public static void goAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    //上次点击的时间
    private static long lastClickTime = 0;
    //时间间隔
    private static int spaceTime = 1000;

    /**
     * 判断快速点击后是否允许点击
     *
     * @return
     */
    public static boolean isAllowClick() {

        long currentTime = System.currentTimeMillis();//当前系统时间

        boolean isAllowClick;//是否允许点击

        if (currentTime - lastClickTime > spaceTime) {

            isAllowClick = true;

        } else {
            isAllowClick = false;

        }

        lastClickTime = currentTime;

        return isAllowClick;

    }

    /**
     * image转换ProductImage
     *
     * @param image
     * @return
     */
    public static ProductImage imageToProductImage(Image image) {
        if (image != null) {
            ProductImage productImage = new ProductImage();
            productImage.setImageId(image.getImageId());
            productImage.setImageUrl(image.getImageUrl());
            if (StringUtils.isEmpty(image.getThumbnailUrl())) {
                productImage.setThumbnailUrl(image.getImageUrl());
            } else {
                productImage.setThumbnailUrl(image.getThumbnailUrl());
            }
            productImage.setProductId(image.getParentId());
            productImage.setStatus(image.getState());
            return productImage;
        }
        return null;
    }

    /**
     * image转换ProductImage
     *
     * @param image
     * @return
     */
    public static StoreImage imageToStoreImage(Image image, int imageType, String videoUrl, String videoImageUrl) {
        if (image != null) {
            StoreImage storeImage = new StoreImage();
            storeImage.setImageId(image.getImageId());
            storeImage.setImageUrl(image.getImageUrl());
            if (StringUtils.isEmpty(image.getThumbnailUrl())) {
                storeImage.setThumbnailUrl(image.getImageUrl());
            } else {
                storeImage.setThumbnailUrl(image.getThumbnailUrl());
            }
            storeImage.setParentId(image.getParentId());
            storeImage.setStatus(image.getState());
            storeImage.setImageType(imageType);
            storeImage.setVideoUrl(videoUrl);
            storeImage.setVideoImageUrl(videoImageUrl);
            return storeImage;
        }
        return null;
    }

}


