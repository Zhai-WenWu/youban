package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ProductOrder;

/**
 * Created by ZHUDI on 2018/3/15.
 */

public class GetMyOrderListResponse extends BaseResponse {
    private List<ProductOrder> list;
    private int total;

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
