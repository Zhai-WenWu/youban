package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ProductDetail;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetStoreProductListResponse extends BaseResponse {
    private List<ProductDetail> list;//店铺商品列表
    private int total;//总数

    public List<ProductDetail> getList() {
        return list;
    }

    public void setList(List<ProductDetail> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
