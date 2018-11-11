package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.ProductInfoDetail;
import cn.bjhdltcdn.p2plive.model.StoreDetail;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetStoreListResponse extends BaseResponse {
    private int total;//总数,
    private List<StoreDetail> list;//店铺详情列表,
    private List<ProductInfoDetail>  productInfoList;
    private String blankHint;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<StoreDetail> getList() {
        return list;
    }

    public void setList(List<StoreDetail> list) {
        this.list = list;
    }

    @Override
    public String getBlankHint() {
        return blankHint;
    }

    @Override
    public void setBlankHint(String blankHint) {
        this.blankHint = blankHint;
    }

    public List<ProductInfoDetail> getProductInfoList() {
        return productInfoList;
    }

    public void setProductInfoList(List<ProductInfoDetail> productInfoList) {
        this.productInfoList = productInfoList;
    }
}
