package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by ZHUDI on 2017/12/26.
 */

public class QueryUserBalanceResponse extends BaseResponse {
    private int userBalance;//用户余额,

    public int getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(int userBalance) {
        this.userBalance = userBalance;
    }
}
