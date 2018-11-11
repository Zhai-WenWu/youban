package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by xiawenquan on 17/12/25.
 */

public class FindTokenByUserIdResponse extends BaseResponse {
    private String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
