package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by ZHUDI on 2017/12/6.
 */

public class MyAccountResponse extends BaseResponse {
    private int userBalance;//用户余额,
    private int userLevel;//用户级别,
    private int payAmount;//累计充值总额,
    private int currentLevelValue;//用户当前等级数值,
    private int nextLevelValue;//下一等级数值,

    public int getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(int userBalance) {
        this.userBalance = userBalance;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public int getCurrentLevelValue() {
        return currentLevelValue;
    }

    public void setCurrentLevelValue(int currentLevelValue) {
        this.currentLevelValue = currentLevelValue;
    }

    public int getNextLevelValue() {
        return nextLevelValue;
    }

    public void setNextLevelValue(int nextLevelValue) {
        this.nextLevelValue = nextLevelValue;
    }
}
