package cn.bjhdltcdn.p2plive.ui.activity;

import io.agora.rtc.IRtcEngineEventHandler;

/**
 * 视频页面处理
 */
public class BaseEngineEventHandlerActivity extends BaseActivity {
    /**
     * 加入频道回调
     * @param channel 房间号
     * @param uid   用户id
     * @param elapsed 延时
     */
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
    }

    /**
     * 重新加入频道回调
     * @param channel   房间号
     * @param uid       用户id
     * @param elapsed   延时
     */
    public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
    }

    /**
     * 发生错误回调
     * 表示 SDK 运行时出现了（网络或媒体相关的）错误
     *  SDK 上报的错误意味着 SDK无法自动恢复
     *  需要 APP 干预或提示用户
     *  比如启动通话失败时， SDK 会上报ERR_START_CALL 错误
     *  APP 可以提示用户启动通话失败，并调用 leaveChannel 退出频道
     *  ERR_INVALID_VENDOR_KEY(101):无效的厂商 Key。
     *  ERR_INVALID_CHANNEL_NAME(102): 无效的频道名。
     *  ERR_LOOKUP_CHANNEL_REJECTED(105): 查找频道失败，意味着服务器主动拒绝了请求。
     *  ERR_OPEN_CHANNEL_REJECTED(107): 加入频道失败，意味着媒体服务器主动拒绝了请求。
     *  ERR_LOAD_MEDIA_ENGINE(1001): 加载媒体引擎失败。
     *  ERR_START_CALL（ 1002） : 打开本地音视频设备、启动通话失败。
     *  ERR_START_CAMERA(1003): 打开本地摄像头失败。
     * @param err
     */
    public void onError(int err) {
    }

    public void onCameraReady() {
    }

    /**
     * 声音质量回调
     * 在通话中,该回调方法每两秒触发一次,报告当前通话的(嘴到耳)音频质量。默认启用。
     * @param uid
     * @param quality
     *  QUALITY_EXCELLENT( =1)
        QUALITY_GOOD( = 2)
        QUALITY_POOR( = 3)
        QUALITY_BAD( = 4)
        QUALITY_VBAD( = 5)
        QUALITY_DOWN( = 6)
     * @param delay 延迟
     * @param lost  丢包率
     */
    public void onAudioQuality(int uid, int quality, short delay, short lost) {
    }

    /**
     * 离开频道回调
     * 应用程序调用 leaveChannel()方法时， SDK 提示应用程序离开频道成功。
     * 在该回调方法中，应用程序可以得到此次通话的总通话时长、 SDK 收发数据的流量等信息。
     * @param stats 会话数据
     */
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
    }

    /**
     * 说话声音音量提示回调
     * @param speakers  说话者(数组)(uid: 说话者的用户 ID;volume:说话者的音量(0~255))
     * @param totalVolume   (混音后的)总音量(0~255)
     */
    public void onAudioVolumeIndication(IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
    }

    /**
     * 网络质量报告回调
     * 报告网络质量,该回调函数每 2 秒触发一次。
     * @param quality
     */
    public void onNetworkQuality(int quality) {
    }

    /**
     * 其他用户加入当前频道回调
     * @param uid   用户 ID
     * @param elapsed   joinChannel 开始到该回调触发的延迟(毫秒)
     */
    public void onUserJoined(int uid, int elapsed) {
    }

    /**
     * 其他用户离开当前频道回调
     * 提示有用户离开了频道(或掉线)。
     * 注:SDK 判断用户离开频道(或掉线)的依据是超时:在一定时间内(15 秒)没有收到对方的 任何数据包,判定为对方掉线。在网络较差的情况下,可能会有误报。建议可靠的掉线检测应该 由信令来做。
     * @param uid   用户id
 *     @param reason    离线原因：
     *                  Constants.USER_OFFLINE_QUIT:用户主动离开
     *                  Constants.USER_OFFLINE_DROPPED:因过长时间收不到对方数据包,超时掉线。注意:由于 SDK 使用的是不可靠通道,也有可能对方主动离开本方没收到对方离开消息而误判为超时掉线。
     */
    public void onUserOffline(int uid,int reason ) {
    }

    /**
     * 统计数据回调
     * 该回调定期上报 Rtc Engine 的运行时的状态,每两秒触发一次。
     * @param stats
     */
    public void onUpdateSessionStats(IRtcEngineEventHandler.RtcStats stats) {
    }

    /**
     * 用户静音回调
     * 提示有其他用户将他的音频流静音/取消静音
     * @param uid   用户 ID
     * @param muted True – 该用户将音频静音False – 该用户取消了音频静音
     */
    public void onUserMuteAudio(int uid, boolean muted) {
    }

    /**
     * 提示有其他用户启用/关闭了视频功能。
     * 关闭视频功能是指该用户只能进行语音通话,不能显示、发送自己的视频,也不能接收、显示别人的视频。
     * @param uid
     * @param enabled
     * True: 该用户已启用了视频功能
     * False: 该用户已关闭了视频功能
     */
    public void onUserEnableVideo(int uid, boolean enabled) {
    }

    /**
     * 用户停止/重启视频回调
     * @param uid   用户 ID
     * @param muted True 该用户暂停了视频发送
     *              False 该用户恢复了视频发送
     */
    public void onUserMuteVideo(int uid, boolean muted) {
    }


    public void onLocalVideoStat(int sentBytes, int sentFrames) {
    }

    /**
     * 远端视频显示回调
     * 第一帧远程视频显示在视图上时,触发此调用。应用程序可在此调用中获知出图时间(elapsed)。
     * @param uid   用户ID
     * @param width 视频流宽(像素)
     * @param height    视频流高(像素)
     * @param elapsed   加入频道开始到该回调触发的延迟(毫秒)
     */
    public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {
    }

    /**
     * 提示第一帧本地视频画面已经显示在屏幕上。
     * @param width 视频流宽(像素)
     * @param height    视频流高(像素)
     * @param elapsed   加入频道开始到该回调触发的延迟(毫秒)
     */
    public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
    }

    /**
     * 远端视频接收解码回调
     * 收到第一帧远程视频流并解码成功时,触发此调用。应用程序可以在此回调中设置该用户的 view。
     * @param uid   用户ID,指定是哪个用户的视频流
     * @param width 视频流宽(像素)
     * @param height    视频流高(像素)
     * @param elapsed   加入频道开始到该回调触发的延迟(毫秒)
     */
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
    }

    /**
     * 连接丢失回调
     * 该回调方法表示 SDK 和服务器失去了网络连接,并且尝试自动重连一段时间(默认 10 秒)后仍 未连上。
     * 该回调触发后,SDK 仍然会尝试重连,重连成功后会触发 onRejoinChannelSuccess 回调。
     */
    public void onConnectionLost() {}


    /**
     * 连接中断回调
     * 回调在 SDK 刚失去和服务器连接时触发
     */
    public void onConnectionInterrupted() {}



}
