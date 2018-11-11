package cn.bjhdltcdn.p2plive.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.hardware.TuSDKRecordVideoCamera;
import org.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;
import org.lasque.tusdk.core.video.TuSDKVideoResult;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by Hu_PC on 2017/12/5.
 */

public class PublishVideoActivity extends BaseActivity implements View.OnClickListener {
    private TuSDKRecordVideoCamera videoCamera;
    private RelativeLayout rela_video, rela_record, rela_ok, rela_time;
    private LinearLayout linear_btn;//底部取消和确定
    private TextView tv_time, tv_title, tv_cancel, tv_cancel_ovl;
    private ImageView img_record, img_switch;
    private final int MIN_TIME = 2;
    private int MAX_TIME = 300;
    private String videoPath;
    private int type;//1表白，帖子 2.pk 3：私聊发送短视频 4评论
    private int flag;//type=4 1帖子，2表白，4帮帮忙

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        if (type == 3) {
            MAX_TIME = 30;
        } else {
            MAX_TIME = 300;
            if (type == 4) {
                flag = getIntent().getIntExtra(Constants.Fields.FLAG, 0);
            }
        }
        setContentView(R.layout.activity_publish_video);
        Utils.hideNavigationBar(this);
        init();
        initCamera();
    }

    private void init() {
        rela_video = (RelativeLayout) findViewById(R.id.rela_video);
        rela_time = (RelativeLayout) findViewById(R.id.rela_time);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setText(MAX_TIME + "s");
        rela_record = (RelativeLayout) findViewById(R.id.rela_record);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        img_record = (ImageView) findViewById(R.id.img_record);
        img_switch = (ImageView) findViewById(R.id.img_switch);
        linear_btn = (LinearLayout) findViewById(R.id.linear_btn);
        tv_cancel_ovl = (TextView) findViewById(R.id.tv_cancel_ovl);
        rela_ok = (RelativeLayout) findViewById(R.id.rela_ok);
        tv_cancel.setOnClickListener(this);
        img_record.setOnClickListener(this);
        img_switch.setOnClickListener(this);
        tv_cancel_ovl.setOnClickListener(this);
        rela_ok.setOnClickListener(this);
    }

    /**
     * 图涂相关设置
     */
    private void initCamera() {
        // 录制相机采集配置，目前只支持硬编录制
        TuSDKVideoCaptureSetting captureSetting = new TuSDKVideoCaptureSetting();
        captureSetting.fps = 30;
        captureSetting.videoAVCodecType = TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC;
        videoCamera = new TuSDKRecordVideoCamera(this, captureSetting, rela_video);
        // 录制模式
        videoCamera.setRecordMode(TuSDKRecordVideoCamera.RecordMode.Normal);

        //设置最长最短录制时长
        videoCamera.setMinRecordingTime(MIN_TIME);
        videoCamera.setMaxRecordingTime(MAX_TIME);
        // 限制录制尺寸不超过 1280
        videoCamera.setPreviewEffectScale(1.0f);
        videoCamera.setPreviewMaxSize(1280);
        // 编码配置
        TuSDKVideoEncoderSetting encoderSetting = TuSDKVideoEncoderSetting.getDefaultRecordSetting();
        // 1: 1 输出，必须和 regionRatio 保持一致比例
        // 这里可以根据实际使用场景，设为固定的值，比如 480 * 480
        encoderSetting.videoSize = TuSdkSize.create(0, 0);
        encoderSetting.videoQuality = TuSDKVideoEncoderSetting.VideoQuality.RECORD_MEDIUM1;
        videoCamera.setVideoEncoderSetting(encoderSetting);
        // 是否开启动态贴纸
        videoCamera.setEnableLiveSticker(false);
        // 是否开启美颜 (默认: false)
        videoCamera.setEnableBeauty(true);
        // 禁用自动持续对焦 (默认: false)
        videoCamera.setDisableContinueFoucs(true);
        // 启用防闪烁功能，默认关闭。
        videoCamera.setAntibandingMode(CameraConfigs.CameraAntibanding.Auto);

        //设置录制结果回调
        videoCamera.setVideoDelegate(new TuSDKRecordVideoCamera.TuSDKRecordVideoCameraDelegate() {
            @Override
            public void onMovieRecordComplete(TuSDKVideoResult tuSDKVideoResult) {
                img_record.setPressed(false);
                rela_record.setVisibility(View.GONE);
                linear_btn.setVisibility(View.VISIBLE);
                videoPath = tuSDKVideoResult.videoPath.toString();
            }

            @Override
            public void onMovieRecordProgressChanged(float v, float v1) {
                tv_time.setText((MAX_TIME - (int) v1) + "s");
            }

            @Override
            public void onMovieRecordStateChanged(TuSDKRecordVideoCamera.RecordState recordState) {

                if (recordState == TuSDKRecordVideoCamera.RecordState.Recording) { // 开始录制
                    Utils.showToastShortTime("开始录制");
                    img_record.setPressed(true);

                } else if (recordState == TuSDKRecordVideoCamera.RecordState.Paused) // 已暂停录制
                {

                } else if (recordState == TuSDKRecordVideoCamera.RecordState.RecordCompleted) { //录制完成弹出提示（续拍模式下录过程中超过最大时间时调用）


                } else if (recordState == TuSDKRecordVideoCamera.RecordState.Saving) { // 正在保存视频

                    Utils.showToastShortTime("视频保存中");
                }
            }

            @Override
            public void onMovieRecordFailed(TuSDKRecordVideoCamera.RecordError recordError) {
                if (recordError == TuSDKRecordVideoCamera.RecordError.MoreMaxDuration) { // 超过最大时间 （超过最大时间是再次调用startRecording时会调用）
                    Utils.showToastShortTime("超过最大时间");
                } else if (recordError == TuSDKRecordVideoCamera.RecordError.SaveFailed) { // 视频保存失败
                    Utils.showToastShortTime("视频保存失败");
                } else if (recordError == TuSDKRecordVideoCamera.RecordError.InvalidRecordingTime) {//录制时间过短
                    Utils.showToastShortTime("录制时间过短");
                }
            }
        });
        //视频保存到手机相册
        videoCamera.setSaveToAlbum(true);
        videoCamera.changeRegionRatio(0);
        // 初始化参数
        videoCamera.initOutputSettings();

        videoCamera.startCameraCapture();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.img_record:
                tv_title.setVisibility(View.INVISIBLE);
                img_record.setPressed(true);
                if (!videoCamera.isRecording()) {
                    videoCamera.startRecording();
                } else {
                    if (videoCamera.getMovieDuration() >= MIN_TIME) {
                        videoCamera.stopRecording();
                    } else {
                        Utils.showToastShortTime("视频时长小于" + MIN_TIME + "s");
                    }
                }
                break;
            case R.id.img_switch:
                videoCamera.rotateCamera();
                break;
            case R.id.tv_cancel_ovl:
                rela_record.setVisibility(View.VISIBLE);
                rela_time.setVisibility(View.VISIBLE);
                linear_btn.setVisibility(View.GONE);
                tv_title.setVisibility(View.VISIBLE);
                tv_time.setText(MAX_TIME + "s");
                break;
            case R.id.rela_ok:
                Intent intent = null;
                if (type == 1) {//表白/帖子
                    intent = new Intent(this, PublishActivity.class);
                } else if (type == 2) {//PK
                } else if (type == 3) {//私聊
                    intent = new Intent(this, ConversationActivity.class);
                } else if (type == 4) {//评论
                    if (flag == 1) {//帖子
                        intent = new Intent(this, PostDetailActivity.class);
                    } else if (flag == 2) {//表白
                    } else if (flag == 4) {//帮帮忙
                    }
                }
                intent.putExtra(Constants.KEY.KEY_EXTRA, videoPath);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        videoCamera.resumeCameraCapture();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoCamera.pauseCameraCapture();
    }

    public void setOutputImageOrientation(boolean isLandscape) {
        if (isLandscape) {
            videoCamera.setOutputImageOrientation(InterfaceOrientation.PortraitUpsideDown);
        } else {
            videoCamera.setOutputImageOrientation(InterfaceOrientation.Portrait);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (BuildConfig.LOG_DEBUG) {
            String message = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "屏幕设置为：横屏" : "屏幕设置为：竖屏";
            Utils.showToastShortTime(message);
        }

        setOutputImageOrientation(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (videoCamera != null) {
            videoCamera.stopCameraCapture();
            videoCamera.destroy();
        }

    }
}
