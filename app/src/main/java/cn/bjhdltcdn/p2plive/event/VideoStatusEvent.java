package cn.bjhdltcdn.p2plive.event;

/**
 * Created by xiawenquan on 16/8/5.
 */
public class VideoStatusEvent {
    /**
     * 视频状态
     * 0 对方空闲；1 对方占线
     */
    private int videoStatus;

    public VideoStatusEvent(int videoStatus) {
        this.videoStatus = videoStatus;
    }

    public int getVideoStatus() {
        return videoStatus;
    }

}
