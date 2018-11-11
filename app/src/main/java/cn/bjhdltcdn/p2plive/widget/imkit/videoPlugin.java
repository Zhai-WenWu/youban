package cn.bjhdltcdn.p2plive.widget.imkit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadEngine;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.UpyunModel;
import cn.bjhdltcdn.p2plive.mvp.presenter.SaveSayLovePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.PublishVideoActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveEditSelectDialog;
import cn.bjhdltcdn.p2plive.utils.BitmapUtils;
import cn.bjhdltcdn.p2plive.utils.DateUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.UriUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.YouBanShortVideoMessage;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hu_PC on 2017/8/24.
 */

public class videoPlugin implements IPluginModule, BaseView {
    private Context context;
    //打开录制视频页面
    private final int INTENT_MOMENTS_VIDEO_CODE = 1;
    //本地选择视频
    private int VIDEO_PICKER_SELECT = 2;
    /**
     * 视频路径
     */
    private String videoPath;
    private String videoLocationPath;
    private Conversation.ConversationType conversationType;
    private String targetId;
    private String uploadUrl;
    private int imgWidth;
    private int imgHeight;
    private SaveSayLovePresenter presenter;
    private String videoImagePath;
    private long duration;
    MediaPlayer mediaPlayer;
    private Fragment fragment;
    /**
     * 视频文件格式
     */
    private String videoSuffixs[] = {"mp4", "avi", "mov","mkv", "flv","f4v", "rmvb"};

    @Override
    public Drawable obtainDrawable(Context context) {
        this.context = context;
        return ContextCompat.getDrawable(context, R.drawable.plugin_short_video);
    }

    @Override
    public String obtainTitle(Context context) {
        return "发送视频";
    }

    @Override
    public void onClick(final Fragment fragment, final RongExtension rongExtension) {
        Log.e("videoPlugin onClick", "跳到拍摄视屏界面");
        this.fragment = fragment;
        this.conversationType = rongExtension.getConversationType();
        this.targetId = rongExtension.getTargetId();
        ActiveEditSelectDialog dialog = new ActiveEditSelectDialog();
        dialog.setItemText("拍摄", "从手机相册选择");
        dialog.setItemClick(new ActiveEditSelectDialog.ItemClick() {
            @Override
            public void itemClick(int position) {
                switch (position) {
                    case 1:
                        //拍摄
                        Intent intent = new Intent(fragment.getActivity(), PublishVideoActivity.class);
                        intent.putExtra(Constants.Fields.TYPE, 3);
                        rongExtension.startActivityForPluginResult(intent, INTENT_MOMENTS_VIDEO_CODE, videoPlugin.this);
                        break;
                    case 2:
                        //从手机相册选择
                        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        pickIntent.setType("video/*");
                        pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        rongExtension.startActivityForPluginResult(pickIntent, VIDEO_PICKER_SELECT, videoPlugin.this);
                        break;
                }
            }
        });
        dialog.show(fragment.getFragmentManager());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "返回聊天界面");
        if (resultCode == RESULT_OK && (VIDEO_PICKER_SELECT == requestCode || INTENT_MOMENTS_VIDEO_CODE == requestCode)) {
            if (VIDEO_PICKER_SELECT == requestCode) {
                Uri selectedMediaUri = data.getData();
                videoLocationPath = UriUtils.getFileAbsolutePath(App.getInstance(), selectedMediaUri);
            } else if (INTENT_MOMENTS_VIDEO_CODE == requestCode) {
                videoLocationPath = data.getStringExtra(Constants.KEY.KEY_EXTRA);
            }

//            KLog.e("videoPath ==== " + videoLocationPath);

            if (StringUtils.isEmpty(videoLocationPath)) {
//                KLog.e("videoPath ==文件路径是null== " + videoLocationPath);
                return;
            }

            File file = new File(videoLocationPath);
//            KLog.e("file.length() ==== " + file.length());

            if (file.length() <= 0) {
                Utils.showToastShortTime("对不起，您选择的文件无效");
                return;
            }

            try {
                String suffix = videoLocationPath.substring(videoLocationPath.lastIndexOf(".") + 1, videoLocationPath.length());
//                KLog.e("suffix ==== " + suffix);
                if (StringUtils.isEmpty(suffix)) {
                    Utils.showToastShortTime("对不起，您选择的文件格式无效");
                    return;
                }

                // 判断文件是否是视频文件
                boolean isVideo = false;
                for (int i = 0; i < videoSuffixs.length; i++) {
                    if (videoSuffixs[i].equals(suffix)) {
                        isVideo = true;
                        break;
                    }
                }


                if (!isVideo) {
                    Utils.showToastShortTime("视频文件格式(" + suffix + ")不正确");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utils.showToastShortTime("对不起，系统解析文件错误");
                return;
            }

//            KLog.e("file.length() ==== " + file.length());
            if (file.length() / (1024 * 1024) > 50) {
                Utils.showToastShortTime("文件过大，请不要超过50M");
                return;
            }

            if (!StringUtils.isEmpty(videoLocationPath)) {
                Glide.with(fragment.getContext()).asBitmap().load(videoLocationPath).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            int width = resource.getWidth();
                            int height = resource.getHeight();
//                            KLog.d("width =1== " + width + " ; height =1== " + height);

                            float ww = BitmapUtils.getBitmapWidth(App.getInstance());
                            float hh = BitmapUtils.getBitmapHeight(App.getInstance());
//                            KLog.d("ww === " + ww + " ; hh === " + hh);

                            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
                            double scale = 1;//scale=1表示不缩放
                            if (width > height && width > ww) {//如果宽度大的话根据宽度固定大小缩放
                                scale = width / ww;
                            } else if (width < height && height > hh) {//如果高度高的话根据宽度固定大小缩放
                                scale = height / hh;
                            }

//                            KLog.d("scale === " + scale);

                            if (scale < 1) {
                                scale = 1;
                            }

                            width = (int) (width / scale);
                            height = (int) (height / scale);

//                            KLog.d("width =scale== " + width + " ; height =scale== " + height);

                            imgWidth = width;
                            imgHeight = height;
                            return;
                        }
                    }
                });

                showLoading();

                //表单上传
                final Map<String, Object> paramsMap = new HashMap<>();
                //上传空间
                paramsMap.put(Params.BUCKET, "video-hdlt-com");
                //保存路径，任选其中一个

                final String saveUrl = "/uploads/" + (BuildConfig.LOG_DEBUG ? "hdlt_youban_test/chat/message/" : "hdlt_youban_official/chat/message/") + SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) + "/" + DateUtils.getFormatDataString(new Date(), DateUtils.DATE_FORMAT_1) + "/" + System.currentTimeMillis() + ".mp4";
                paramsMap.put(Params.SAVE_KEY, saveUrl);

                //上传结果回调
                UpCompleteListener completeListener = new UpCompleteListener() {
                    @Override
                    public void onComplete(boolean isSuccess, String result) {

//                        KLog.json(result);

                        hideLoading();
                        showLoading();
                        try {
                            if (!StringUtils.isEmpty(result)) {
                                if (result.startsWith("{") && result.endsWith("}")) {
                                    final UpyunModel upyunModel = RongIMutils.getMapper().readValue(result, UpyunModel.class);
//                                    KLog.d("upyunModel === " + upyunModel);
                                    if (upyunModel != null) {
                                        if (upyunModel.getCode() == 200) {
                                            duration = upyunModel.getTime();
                                            videoPath = Constants.KEY.UPAIYUN_HOST + upyunModel.getUrl();
                                            final String save_as = saveUrl.replace(".mp4", ".png");
                                            if (getSaveSayLovePresenter() != null) {
                                                uploadUrl = upyunModel.getUrl();
                                                getSaveSayLovePresenter().upYunSnapshot(uploadUrl, save_as, "00:00:00", imgWidth, imgHeight);
                                            }
                                        }
                                    }
                                } else {
                                    final String save_as = saveUrl.replace(".mp4", ".png");
                                    if (getSaveSayLovePresenter() != null) {
                                        getSaveSayLovePresenter().upYunSnapshot(uploadUrl, save_as, "00:00:01", imgWidth, imgHeight);
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                //进度条回调
                UpProgressListener progressListener = new UpProgressListener() {
                    @Override
                    public void onRequestProgress(final long bytesWrite, final long contentLength) {
//                        KLog.d((100 * bytesWrite) / contentLength + "%");
                    }

                };

                UploadEngine.getInstance().formUpload(new File(videoLocationPath), paramsMap, Constants.KEY.OPERATOR_NAME, UpYunUtils.md5(Constants.KEY.OPERATOR_PWD), completeListener, progressListener);


            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


    }



    public SaveSayLovePresenter getSaveSayLovePresenter() {
        if (presenter == null) {
            presenter = new SaveSayLovePresenter(this);
        }
        return presenter;
    }



    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_UPYUN_SNAPSHOT)) {
            if (object instanceof Response) {
                Response response = (Response) object;
                try {
                    String responseText = response.body().string();
//                    KLog.json(responseText);
                    if (!StringUtils.isEmpty(responseText)) {

                        final Map map = RongIMutils.getMapper().readValue(responseText, Map.class);
                        if (map != null && !map.isEmpty()) {

                            Object statusCode = map.get("status_code");
//                            KLog.d("statusCode === " + statusCode);

                            if (statusCode instanceof Integer) {
                                int code = ((Integer) statusCode).intValue();
                                if (code == 200) {
                                    final Object saveAs = map.get("save_as");
//                                    KLog.d("saveAs === " + saveAs);
                                    if (saveAs instanceof String) {
                                        Activity activity = (Activity) context;
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                videoImagePath = Constants.KEY.UPAIYUN_HOST + saveAs;
                                                if (getSaveSayLovePresenter() != null) {
                                                    final YouBanShortVideoMessage videoMessage = new YouBanShortVideoMessage();
//                                                    videoMessage.setLocalPath(Uri.parse("file://" + (videoLocationPath)));
                                                    videoMessage.setMediaUrl(Uri.parse(videoPath));
                                                    videoMessage.setmThumbUri(Uri.parse(videoImagePath));
                                                    mediaPlayer = new MediaPlayer();
                                                    try {
                                                        mediaPlayer.setDataSource(videoLocationPath);
                                                        mediaPlayer.prepare();
                                                        duration = mediaPlayer.getDuration();
//                                                        KLog.e(duration);
                                                        videoMessage.setDuration(duration);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } finally {
                                                        mediaPlayer.release();
                                                    }

                                                    Message message = Message.obtain(targetId, conversationType, videoMessage);
                                                    hideLoading();
//                                                    RongIM.getInstance().sendMediaMessage(message,"[视频]", (String)null, (IRongCallback.ISendMediaMessageCallbackWithUploader )null);
                                                    RongIM.getInstance().sendMessage(message, "[视频]", null, (IRongCallback.ISendMessageCallback) null);
                                                }
                                            }
                                        });

                                    }
                                } else if (code == 40100002) {// 当前手机时间错误

                                    Utils.showToastShortTime("当前手机时间错误");

                                } else {
                                    Utils.showToastShortTime((String) map.get("msg"));
                                }
                            } else {
                                Utils.showToastShortTime(responseText);
                            }
                        }

                    } else {
                        Utils.showToastShortTime("上传视频失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    hideLoading();
                }
            }
        }
    }


    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(fragment.getActivity());
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }
}
