package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.ProductOrder;

/**
 * Created by zhaiww on 2018/4/17.
 */

public class FindOrderDetailResponse extends BaseResponse {
    public ProductOrder orderInfo;

    public ProductOrder getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(ProductOrder orderInfo) {
        this.orderInfo = orderInfo;
    }
}
