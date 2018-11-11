package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by ZHUDI on 2018/1/9.
 */

public class FindUserAuthStatusResponse extends BaseResponse {
    private int authStatus;//实名认证状态(0未认证,1等待审核,2已认证,3审核失败),

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }
}
