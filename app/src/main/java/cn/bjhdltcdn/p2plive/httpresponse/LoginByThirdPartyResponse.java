package cn.bjhdltcdn.p2plive.httpresponse;

public class LoginByThirdPartyResponse extends LoginResponse {
    private int isFirst;//是否第一次登录 1 是  2 否,

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }
}
