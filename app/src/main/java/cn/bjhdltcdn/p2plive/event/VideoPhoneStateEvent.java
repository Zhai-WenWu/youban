package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;

/**
 * Created by xiawenquan on 16/10/13.
 * 视频状态下，接听电话事件
 */

public class VideoPhoneStateEvent {
    private VideoCMDTextMessageModel messageModel;

    /**
     * 视频类型状态
     * 0 接收对方的电话状态通知
     * 1 本地电话状态通知 响铃（接听中）,摘机（有呼入和呼出）
     * 2 本地电话状态通知 空闲（无呼入或已挂机）
     */
    private int videoTypeStatus;

    public VideoPhoneStateEvent() {
    }

    public VideoPhoneStateEvent(int videoTypeStatus) {
        this.videoTypeStatus = videoTypeStatus;
    }

    public VideoPhoneStateEvent(VideoCMDTextMessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public VideoCMDTextMessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(VideoCMDTextMessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public int getVideoTypeStatus() {
        return videoTypeStatus;
    }

    public void setVideoTypeStatus(int videoTypeStatus) {
        this.videoTypeStatus = videoTypeStatus;
    }
}
