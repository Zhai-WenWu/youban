package cn.bjhdltcdn.p2plive.event;


import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;

/**
 * Created by xiawenquan on 16/8/1.
 */
public class GiftShowEvent {
    private VideoCMDTextMessageModel videoCMDTextMessageModel;

    public GiftShowEvent(VideoCMDTextMessageModel videoCMDTextMessageModel) {
        this.videoCMDTextMessageModel = videoCMDTextMessageModel;
    }

    public VideoCMDTextMessageModel getVideoCMDTextMessageModel() {
        return videoCMDTextMessageModel;
    }
}
