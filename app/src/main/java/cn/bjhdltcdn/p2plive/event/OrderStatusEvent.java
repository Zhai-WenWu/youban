package cn.bjhdltcdn.p2plive.event;

/**
 * Created by zhaiww on 2018/4/21.
 */

public class OrderStatusEvent {


    public int position;
    public int orderStatus;
    public int buyerSign;

    public int getBuyerSign() {
        return buyerSign;
    }

    public void setBuyerSign(int buyerSign) {
        this.buyerSign = buyerSign;
    }

    public OrderStatusEvent(int position, int orderStatus) {
        this.orderStatus = orderStatus;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public int getOrderStatus() {
        return orderStatus;
    }
}
