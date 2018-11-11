package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class FindExchangeInfoResponse extends BaseResponse {
    private String content;//提示内容(提现规则 当前仅支持支付宝提现 每日可提现一次，最大金额不能大于5W-手续费5%),
    private String saleAmount;//销售总额,
    private double minAmount;//最小提现金额
    private double maxAmount;//最大提现金额
    private double remainExchangeAmount;//可提现金额,
    private int isDayExchange;//当天是否提现(0未提现,1已提现),
    private int authStatus;//实名认证状态(0未认证,1等待审核,2已认证,3审核失败),
    private String account;//支付宝账户(没有默认空字符串),

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public double getRemainExchangeAmount() {
        return remainExchangeAmount;
    }

    public void setRemainExchangeAmount(double remainExchangeAmount) {
        this.remainExchangeAmount = remainExchangeAmount;
    }

    public int getIsDayExchange() {
        return isDayExchange;
    }

    public void setIsDayExchange(int isDayExchange) {
        this.isDayExchange = isDayExchange;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
