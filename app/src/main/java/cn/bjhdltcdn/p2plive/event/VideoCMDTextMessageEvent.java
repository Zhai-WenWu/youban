package cn.bjhdltcdn.p2plive.event;


import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;

/**
 * Created by xiawenquan on 16/8/5.
 */
public class VideoCMDTextMessageEvent {
    private VideoCMDTextMessageModel textMessageModel ;

    public VideoCMDTextMessageEvent(VideoCMDTextMessageModel textMessageModel) {
        this.textMessageModel = textMessageModel;
    }

    public VideoCMDTextMessageModel getTextMessageModel() {
        return textMessageModel;
    }

    public void setTextMessageModel(VideoCMDTextMessageModel textMessageModel) {
        this.textMessageModel = textMessageModel;
    }
}
