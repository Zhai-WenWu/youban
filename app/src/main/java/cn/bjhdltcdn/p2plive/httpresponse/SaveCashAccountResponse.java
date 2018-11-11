package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.CashAccount;

/**
 * Created by xiawenquan on 18/3/8.
 */

public class SaveCashAccountResponse extends BaseResponse {
    private CashAccount cashAccount;

    public CashAccount getCashAccount() {
        return cashAccount;
    }

    public void setCashAccount(CashAccount cashAccount) {
        this.cashAccount = cashAccount;
    }
}
