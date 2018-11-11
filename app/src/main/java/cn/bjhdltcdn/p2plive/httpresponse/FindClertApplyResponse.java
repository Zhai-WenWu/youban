package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.ApplyClert;
import cn.bjhdltcdn.p2plive.model.BaseUser;

/**
 * Created by zhaiww on 2018/5/18.
 */

public class FindClertApplyResponse extends BaseResponse {
    public ApplyClert applyClert;
    public BaseUser baseUser;

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public ApplyClert getApplyClert() {
        return applyClert;
    }

    public void setApplyClert(ApplyClert applyClert) {
        this.applyClert = applyClert;
    }
}
