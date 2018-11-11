package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.BrandShop;

/**
 * Description:
 * Data: 2018/9/18
 *
 * @author: zhudi
 */
public class GetBrandShopInfoListResponse extends BaseResponse {
    private List<BrandShop> list;
    private int total;

    public List<BrandShop> getList() {
        return list;
    }

    public void setList(List<BrandShop> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
