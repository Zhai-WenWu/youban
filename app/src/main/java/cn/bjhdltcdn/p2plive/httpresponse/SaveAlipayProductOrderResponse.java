package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class SaveAlipayProductOrderResponse extends BaseResponse {
    private String signData;//支付宝订单签名数据,

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }
}
