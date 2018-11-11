package cn.bjhdltcdn.p2plive.event;


import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;

/**
 * Created by ZHUDI on 2017/1/12.
 */

public class VideoGroupMicApplyEvent {
    /**
     * 1申请，2同意上麦，3踢出下麦，4拒绝，5主动下麦
     */
    private int type;
    private BaseUser baseUser;
    private int videoType;//1(视频),2(语音),

    public VideoGroupMicApplyEvent(int type, BaseUser baseUser) {
        this.type = type;
        this.baseUser = baseUser;
    }

    public VideoGroupMicApplyEvent(int type, BaseUser baseUser, int videoType) {
        this.type = type;
        this.baseUser = baseUser;
        this.videoType = videoType;
    }

    /**
     * 1申请，2同意上麦，3踢出下麦，4拒绝，5主动下麦
     *
     * @return
     */
    public int getType() {
        return type;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public int getVideoType() {
        return videoType;
    }
}
