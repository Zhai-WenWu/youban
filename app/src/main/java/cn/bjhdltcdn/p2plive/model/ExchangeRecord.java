package cn.bjhdltcdn.p2plive.model;

/**
 * Created by Hu_PC on 2018/5/25.
 */

public class ExchangeRecord {
    private long recordId;//主键Id,
    private long userId;//用户ID,
    private long storeId;//店铺Id,
    private int accountType;//账号类型(1--->支付宝，2--->微信，3--->银行),
    private int totalGold;//总金额,
    private int exchangeGold;//提现的金额,
    private int chargeGold;//缴纳5%手续费的金额,
    private int amount;//兑换金额,
    private String addTime;//提现时间,
    private int status;//提现状态(0未转账,1已转账),
    private int type;//类型(3--->转账,4--->提现,5--->红包),
    private BaseUser toBaseUser;//{BaseUser对象}用户信息,



    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }

    public int getExchangeGold() {
        return exchangeGold;
    }

    public void setExchangeGold(int exchangeGold) {
        this.exchangeGold = exchangeGold;
    }

    public int getChargeGold() {
        return chargeGold;
    }

    public void setChargeGold(int chargeGold) {
        this.chargeGold = chargeGold;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BaseUser getToBaseUser() {
        return toBaseUser;
    }

    public void setToBaseUser(BaseUser toBaseUser) {
        this.toBaseUser = toBaseUser;
    }
}
