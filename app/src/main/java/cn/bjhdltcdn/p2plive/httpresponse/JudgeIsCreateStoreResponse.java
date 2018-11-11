package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.StoreDetail;

/**
 * Description:
 * Data: 2018/8/3
 *
 * @author: zhudi
 */
public class JudgeIsCreateStoreResponse extends BaseResponse {
    private StoreDetail storeDetail;

    public StoreDetail getStoreDetail() {
        return storeDetail;
    }

    public void setStoreDetail(StoreDetail storeDetail) {
        this.storeDetail = storeDetail;
    }
}
