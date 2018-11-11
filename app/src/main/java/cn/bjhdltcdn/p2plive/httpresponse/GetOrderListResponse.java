package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductOrder;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetOrderListResponse extends BaseResponse {
    private List<ProductOrder> list;//订单信息列表
    private int total;//总条数

    public List<ProductOrder> getList() {
        return list;
    }

    public void setList(List<ProductOrder> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
