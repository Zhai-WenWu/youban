package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.User;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public class ChangedUserInfoResponse extends BaseResponse{
    private User user;//{User用户对象},

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
