package cn.bjhdltcdn.p2plive.event;


import cn.bjhdltcdn.p2plive.model.BaseUser;

/**
 * Created by ZHUDI on 2017/2/10.
 */

public class VideoGroupGetUserInfoEvent {
    private BaseUser baseUser;

    public VideoGroupGetUserInfoEvent(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }
}
