package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.User;

/**
 * Created by Hu_PC on 2017/11/8.
 */

public class LoginResponse extends BaseResponse {
    private User user;//{User用户对象},
    private String token;//融云sdk的token,
    private String sysToken;//用户token，之后所有的请求接口都要传入token,

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSysToken() {
        return sysToken;
    }

    public void setSysToken(String sysToken) {
        this.sysToken = sysToken;
    }

}
