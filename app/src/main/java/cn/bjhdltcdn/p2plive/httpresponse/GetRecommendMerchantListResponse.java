package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreInfo;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetRecommendMerchantListResponse extends BaseResponse {
    private int type;
    private List<StoreDetail> storeList;//推荐店铺列表,

    public List<StoreDetail> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreDetail> storeList) {
        this.storeList = storeList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
