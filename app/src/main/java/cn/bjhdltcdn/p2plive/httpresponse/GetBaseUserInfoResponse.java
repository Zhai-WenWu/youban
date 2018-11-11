package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.BaseUser;

/**
 * Created by xiawenquan on 17/11/23.
 */

public class GetBaseUserInfoResponse extends BaseResponse {

    private BaseUser user;

    public BaseUser getUser() {
        return user;
    }

    public void setUser(BaseUser user) {
        this.user = user;
    }
}
