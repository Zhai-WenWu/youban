package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.CallHistory;
import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;
import cn.bjhdltcdn.p2plive.ui.dialog.CallRequestDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.VideoChatFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;


/**
 * 视频聊天页面
 */
public class VideoChatActivity extends BaseEngineEventHandlerActivity {

    private Fragment mFragment;


    /***
     * type的值
     * 发布角度：
     * 1---->发布者等待状态
     * 2---->发布者视频状态
     * <p>
     * 应征者角度：
     * 3---->订单失效状态
     * 4---->发布者占线状态
     * 5---->发布者空闲
     * 6---->应征者视频状态
     */
    private int type;
    // 对方的用户信息
    private BaseUser baseUser;

    // 房间号
    private String roomNumber = "";

    /**
     * 1视频呼叫；2语音呼叫
     */
    private int videoType;

    /**
     * 是否为视频接收方
     * 1为接收呼叫
     */
    private int code;
    /**
     * 第三人呼叫UI
     */
    private CallRequestDialog mCallRequestDialog;

    private CallHistory callHistory;

    // 接收者解析视频信令对象
    private VideoCMDTextMessageModel videoCMDTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        initData(intent);

        mFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mFragment == null) {
            mFragment = VideoChatFragment.getVideoFragment(type);
        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.content_frame);
        SafeSharePreferenceUtils.saveBoolean(Constants.Fields.VIDEO_STATUS, true);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //这一句必须的，否则Intent无法获得最新的数据
        setIntent(intent);

        initData(intent);

        ((VideoChatFragment) mFragment).setType(type);
        ((VideoChatFragment) mFragment).addFragmentToActivity(type);


    }

    private void initData(Intent intent) {

        if (intent.hasExtra(Constants.Fields.BASEUSER)) {
            baseUser = intent.getParcelableExtra(Constants.Fields.BASEUSER);
        }

        if (intent.hasExtra(Constants.Fields.ROOMNUMBER)) {
            roomNumber = intent.getStringExtra(Constants.Fields.ROOMNUMBER);
        }

        if (intent.hasExtra(Constants.Fields.TYPE)) {
            type = intent.getIntExtra(Constants.Fields.TYPE, 0);
        }
        if (intent.hasExtra(Constants.Fields.VIDEO_TYPE)) {
            videoType = intent.getIntExtra(Constants.Fields.VIDEO_TYPE, 0);
        }
        if (intent.hasExtra(Constants.Fields.CODE)) {
            code = intent.getIntExtra(Constants.Fields.CODE, 0);
        }
        if (intent.hasExtra(Constants.Fields.CALL_HISTORY)) {
            callHistory = intent.getParcelableExtra(Constants.Fields.CALL_HISTORY);
        }

        if (intent.hasExtra(Constants.Fields.EXTRA)) {
            videoCMDTextMessage = intent.getParcelableExtra(Constants.Fields.EXTRA);

            if (videoCMDTextMessage != null) {
                if (TextUtils.isEmpty(roomNumber)) {
                    roomNumber = videoCMDTextMessage.getRoomNumber();
                }
            }

        }

        //保证取消上次视频，防止其他视频接入时失败
        App.getInstance().getRtcEngine().leaveChannel();
        Utils.wakeUpAndUnlock(App.getInstance());

    }


    public BaseUser getBaseUser() {
        return baseUser;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public VideoCMDTextMessageModel getVideoCMDTextMessage() {
        return videoCMDTextMessage;
    }

    public void setVideoCMDTextMessage(VideoCMDTextMessageModel videoCMDTextMessage) {
        this.videoCMDTextMessage = videoCMDTextMessage;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, final int width, final int height, final int elapsed) {
        super.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mFragment != null) {
                    ((VideoChatFragment) mFragment).setupRemoteView(uid, width, height, elapsed);
                }

            }
        });

    }

    @Override
    public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
        super.onFirstLocalVideoFrame(width, height, elapsed);


    }

    @Override
    public void onUserMuteVideo(int uid, boolean muted) {
        if (mFragment != null) {
            ((VideoChatFragment) mFragment).getRemoteVideoStatus(uid, muted);
        }
    }

    @Override
    public void onUserJoined(final int uid, final int elapsed) {
        super.onUserJoined(uid, elapsed);
        runOnUiThread(new Runnable() { // 呼叫方,回调此方法
            @Override
            public void run() {

                // 停止呼叫音乐
                Utils.releaseSource();

                if (mFragment != null) {
                    type = 6;

                    ((VideoChatFragment) mFragment).setType(type);
                    ((VideoChatFragment) mFragment).addFragmentToActivity(type);

                    ((VideoChatFragment) mFragment).setupRemoteView(uid, 0, 0, 0);
                }
            }
        });
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        finish();
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        super.onJoinChannelSuccess(channel, uid, elapsed);

        roomNumber = channel;
        if (code != 0) {//接收到呼叫请求不为0
            return;
        }
        if (getBaseUser() == null) {
            return;
        }

        if (SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) == 0) {
            return;
        }

        //播放呼叫音乐
        Utils.playVoice("call_music.mp3");

//        int videoType;//1视频呼叫 2语音呼叫
//
//        if (((VideoChatFragment) mFragment).isLocalVideoOpen()) {
//            videoType = 1;
//        } else {
//            videoType = 2;
//        }
        RongIMutils.callRequest(getBaseUser().getUserId(), channel, videoType);
    }


    /**
     * 发生错误回调
     * 表示 SDK 运行时出现了（网络或媒体相关的）错误
     * SDK 上报的错误意味着 SDK无法自动恢复
     * 需要 APP 干预或提示用户
     * 比如启动通话失败时， SDK 会上报ERR_START_CALL 错误
     * ERR_OK 0 正常情况的返回值,意味着没有错误
     * ERR_FAILED 1 一般性的错误(没有明确归类的错误原因)
     * ERR_INVALID_ARGUMENT 2 API 调用了无效的参数。例如指定的频道名含 有非法字符
     * ERR_NOT_READY 3 SDK 的模块没有准备好,例如某个 API 调用 依赖于某个模块,但是那个模块还没有准备好服务
     * ERR_NOT_SUPPORTED 4 SDK 不支持该功能
     * ERR_REFUSED  5 调用被拒绝。仅供 SDK 内部使用,不通过 API 或者回调事件返回给应用程序。
     * ERR_BUFFER_TOO_SMALL 6 传入的缓冲区大小不足以存放返回的数据
     * <p/>
     * APP 可以提示用户启动通话失败，并调用 leaveChannel 退出频道
     * ERR_INVALID_VENDOR_KEY(101):无效的厂商 Key。
     * ERR_INVALID_CHANNEL_NAME(102): 无效的频道名。
     * ERR_LOOKUP_CHANNEL_REJECTED(105): 查找频道失败，意味着服务器主动拒绝了请求。
     * ERR_OPEN_CHANNEL_REJECTED(107): 加入频道失败，意味着媒体服务器主动拒绝了请求。
     * ERR_LOAD_MEDIA_ENGINE(1001): 加载媒体引擎失败。
     * ERR_START_CALL（ 1002） : 打开本地音视频设备、启动通话失败。
     * ERR_START_CAMERA(1003): 打开本地摄像头失败。
     *
     * @param err
     */
    @Override
    public void onError(int err) {
    }

    /**
     * 重新加入频道回调
     *
     * @param channel 房间号
     * @param uid     用户id
     * @param elapsed 延时
     */
    @Override
    public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
        super.onRejoinChannelSuccess(channel, uid, elapsed);
    }

    /**
     * 连接丢失回调
     * 该回调方法表示 SDK 和服务器失去了网络连接,并且尝试自动重连一段时间(默认 10 秒)后仍 未连上。
     * 该回调触发后,SDK 仍然会尝试重连,重连成功后会触发 onRejoinChannelSuccess 回调。
     */
    @Override
    public void onConnectionLost() {
        super.onConnectionLost();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showToastShortTime("网络不给力视频连接中断");
                finish();
            }
        });

    }

    public CallHistory getCallHistory() {
        return callHistory;
    }

    public void setCallHistory(CallHistory callHistory) {
        this.callHistory = callHistory;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {

            if (!RongIMutils.isCanOnUserPortraitClick()) {
                RongIMutils.setIsCanOnUserPortraitClick(true);
            }

            Utils.releaseSource();

            if (videoCMDTextMessage != null) {
                videoCMDTextMessage = null;
            }

            if (mFragment != null) {
                mFragment = null;
            }

            try {
                if (mCallRequestDialog != null) {
                    mCallRequestDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mCallRequestDialog = null;

            App.getInstance().getRtcEngine().leaveChannel();
            App.getInstance().setEngineEventHandlerActivity(null);

            if (callHistory != null) {
                callHistory = null;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
