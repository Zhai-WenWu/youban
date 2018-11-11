package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.User;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public class JoinActiveResponse extends BaseResponse{
    private BaseUser baseUser;//{BaseUser对象}退出人基本信息

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }
}
