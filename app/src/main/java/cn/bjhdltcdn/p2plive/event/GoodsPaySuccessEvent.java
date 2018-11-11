package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.ChatBaseUser;
import cn.bjhdltcdn.p2plive.model.ProductOrder;

/**
 * 融云匿名消息
 */
public class GoodsPaySuccessEvent {
    private ProductOrder productOrder;

    public GoodsPaySuccessEvent(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }
}
