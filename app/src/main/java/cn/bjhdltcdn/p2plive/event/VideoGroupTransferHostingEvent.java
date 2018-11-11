package cn.bjhdltcdn.p2plive.event;


import cn.bjhdltcdn.p2plive.model.BaseUser;

/**
 * Created by ZHUDI on 2017/3/8.
 */

public class VideoGroupTransferHostingEvent {
    private int type;//1 收到移交申请 2收到同意 3收到拒绝
    private BaseUser baseUser;

    public VideoGroupTransferHostingEvent() {
    }

    public VideoGroupTransferHostingEvent(int type, BaseUser baseUser) {
        this.type = type;
        this.baseUser = baseUser;
    }

    public int getType() {
        return type;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }
}
