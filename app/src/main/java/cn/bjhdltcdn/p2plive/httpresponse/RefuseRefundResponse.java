package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.ProductOrder;

/**
 * Created by zhaiww on 2018/4/17.
 */

public class RefuseRefundResponse extends BaseResponse {
    public int orderStatus;
    public int isClertStr;

    public int getIsClertStr() {
        return isClertStr;
    }

    public void setIsClertStr(int isClertStr) {
        this.isClertStr = isClertStr;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
