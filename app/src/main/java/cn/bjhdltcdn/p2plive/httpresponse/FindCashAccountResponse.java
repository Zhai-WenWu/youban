package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.CashAccount;

/**
 * Created by xiawenquan on 18/3/8.
 */

public class FindCashAccountResponse extends BaseResponse {
    private CashAccount accountInfo;

    public CashAccount getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(CashAccount accountInfo) {
        this.accountInfo = accountInfo;
    }
}
