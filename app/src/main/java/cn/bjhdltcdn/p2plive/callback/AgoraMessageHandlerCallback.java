package cn.bjhdltcdn.p2plive.callback;

import cn.bjhdltcdn.p2plive.ui.activity.BaseEngineEventHandlerActivity;
import io.agora.rtc.IRtcEngineEventHandler;

/**
 * 声网视频消息回调
 */
public class AgoraMessageHandlerCallback extends IRtcEngineEventHandler {

    private BaseEngineEventHandlerActivity mHandlerActivity;

    //显示房间内其他用户的视频
    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

        if (mHandlerActivity != null) {
            mHandlerActivity.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
        }
    }

    //用户进入
    @Override
    public void onUserJoined(int uid, int elapsed) {
        if (mHandlerActivity != null) {
            mHandlerActivity.onUserJoined(uid, elapsed);
        }
    }

    //用户退出
    @Override
    public void onUserOffline(int uid, int reason) {

        if (mHandlerActivity != null) {
            mHandlerActivity.onUserOffline(uid, reason);
        }
    }

    /**
     * 监听其他用户停止/重启视频回调
     *
     * @param muted True - 该用户暂停了视频发送
     *              False - 该用户恢复了视频发送
     */

    @Override
    public void onUserMuteVideo(int uid, boolean muted) {

        if (mHandlerActivity != null) {
            mHandlerActivity.onUserMuteVideo(uid, muted);
        }
    }

    /**
     * 提示有其他用户启用/关闭了视频功能。
     * 关闭视频功能是指该用户只能进行语音通话,不能显示、发送自己的视频,也不能接收、显示别人的视频。
     *
     * @param uid
     * @param enabled True: 该用户已启用了视频功能
     *                False: 该用户已关闭了视频功能
     */
    @Override
    public void onUserEnableVideo(int uid, boolean enabled) {
        if (mHandlerActivity != null) {
            mHandlerActivity.onUserEnableVideo(uid, enabled);
        }
    }

    //更新聊天数据
    @Override
    public void onRtcStats(RtcStats stats) {

        if (mHandlerActivity != null) {
            mHandlerActivity.onUpdateSessionStats(stats);
        }
    }


    //离开频道回调
    @Override
    public void onLeaveChannel(RtcStats stats) {
        if (mHandlerActivity != null) {
            mHandlerActivity.onLeaveChannel(stats);
        }
    }

    @Override
    public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
        if (mHandlerActivity != null) {
            mHandlerActivity.onFirstLocalVideoFrame(width, height, elapsed);
        }
    }


    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        super.onJoinChannelSuccess(channel, uid, elapsed);
        if (mHandlerActivity != null) {
            mHandlerActivity.onJoinChannelSuccess(channel, uid, elapsed);
        }
    }

    @Override
    public void onError(int err) {
        if (mHandlerActivity != null) {
            mHandlerActivity.onError(err);
        }
    }

    public void setActivity(BaseEngineEventHandlerActivity activity) {
        this.mHandlerActivity = activity;
    }

    @Override
    public void onConnectionInterrupted() {
        if (mHandlerActivity != null) {
            mHandlerActivity.onConnectionInterrupted();
        }
    }

    @Override
    public void onConnectionLost() {
        if (mHandlerActivity != null) {
            mHandlerActivity.onConnectionLost();
        }
    }

    @Override
    public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
        if (mHandlerActivity != null) {
            mHandlerActivity.onRejoinChannelSuccess(channel, uid, elapsed);
        }
    }


}
