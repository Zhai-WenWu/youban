package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by ZHUDI on 2017/11/15.
 */

public class CheckPhoneNumberResponse extends BaseResponse{
    private int flag;//(1已注册,2未注册),

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
