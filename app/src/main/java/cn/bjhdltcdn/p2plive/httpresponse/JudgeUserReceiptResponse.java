package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class JudgeUserReceiptResponse extends BaseResponse {
    private int orderStatus;//订单状态(0待收货,1已收货,2 买家已付款 3 退款申请中 4 退款成功),

    private int sellerSign;

    private int buyerSign;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getSellerSign() {
        return sellerSign;
    }

    public void setSellerSign(int sellerSign) {
        this.sellerSign = sellerSign;
    }

    public int getBuyerSign() {
        return buyerSign;
    }

    public void setBuyerSign(int buyerSign) {
        this.buyerSign = buyerSign;
    }
}
