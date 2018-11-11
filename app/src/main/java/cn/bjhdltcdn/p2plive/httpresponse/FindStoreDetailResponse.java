package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.StoreDetail;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class FindStoreDetailResponse extends BaseResponse {
    private StoreDetail storeDetail;//店铺详情

    public StoreDetail getStoreDetail() {
        return storeDetail;
    }

    public void setStoreDetail(StoreDetail storeDetail) {
        this.storeDetail = storeDetail;
    }
}
