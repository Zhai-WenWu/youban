package cn.bjhdltcdn.p2plive.event;

/**
 * Created by ZHUDI on 2018/3/15.
 */

public class VideoCommentFinishEvent {
    private int module;//1帖子 2表白 4帮帮忙

    public VideoCommentFinishEvent(int module) {
        this.module = module;
    }

    public int getModule() {
        return module;
    }
}
