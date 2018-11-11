package cn.bjhdltcdn.p2plive.event;

/**
 * Created by zhaiww on 2018/6/22.
 */

public class VideoEvaluateEvent {
    public String videoPath;
    public String videoImagePath;

    public VideoEvaluateEvent(String videoPath, String videoImagePath) {
        this.videoPath = videoPath;
        this.videoImagePath = videoImagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getVideoImagePath() {
        return videoImagePath;
    }
}
