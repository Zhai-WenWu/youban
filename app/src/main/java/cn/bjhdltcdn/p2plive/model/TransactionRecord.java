package cn.bjhdltcdn.p2plive.model;


/**
 * Created by ZHAI on 2017/12/16.
 */

public class TransactionRecord {
    /* "recordId":交易记录id,
             "time":交易记录时间,
             "type":交易类型(1--->充值，2--->赠送他人，3--->他人赠送),
             "presenterUserId":赠送人id,
             "presenterNickName":赠送人昵称,
             "income":收入,
             "money":当次交易金额(充值)
     "totalAmount":总额*/
    private long recordId;
    private String time;
    private int type;
    private long presenterUserId;
    private String presenterNickName;
    private String income;
    private String money;
    private long toUserId;

    public String getTotalAmount() {
        return totalAmount;
    }

    private String totalAmount;

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPresenterUserId() {
        return presenterUserId;
    }

    public void setPresenterUserId(long presenterUserId) {
        this.presenterUserId = presenterUserId;
    }

    public String getPresenterNickName() {
        return presenterNickName;
    }

    public void setPresenterNickName(String presenterNickName) {
        this.presenterNickName = presenterNickName;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
