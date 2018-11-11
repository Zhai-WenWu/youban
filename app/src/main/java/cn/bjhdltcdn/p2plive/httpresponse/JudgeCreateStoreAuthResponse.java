package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.BaseUser;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class JudgeCreateStoreAuthResponse extends BaseResponse {
    private int isCreateStore;//用户店铺状态(1已开店,调用店铺详情;2已认证未申请,调用申请开店;3已认证申请拒绝,调用申请开店;4已认证已申请未创建,调用创建店铺),
    private BaseUser baseUser;//{BaseUser对象}已认证用户基本信息,
    private long storeId;//店铺Id(没有店铺返回0),

    public int getIsCreateStore() {
        return isCreateStore;
    }

    public void setIsCreateStore(int isCreateStore) {
        this.isCreateStore = isCreateStore;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }
}
