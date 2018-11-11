package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.User;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public class SaveAlipayOrderResponse extends BaseResponse{
    private String signData;//支付宝订单签名数据,

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }
}
