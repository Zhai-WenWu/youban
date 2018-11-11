package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.StoreDetail;

/**
 * Created by ZHUDI on 2017/12/2.
 */

public class UpdateShopDetailEvent {

    private int type;//0：从编辑店铺返回1：从申请店员返回
    private StoreDetail storeDetail;//店铺详情

    public UpdateShopDetailEvent(StoreDetail storeDetail) {
        this.storeDetail = storeDetail;
    }

    public UpdateShopDetailEvent(int type, StoreDetail storeDetail) {
        this.type = type;
        this.storeDetail = storeDetail;
    }

    public StoreDetail getStoreDetail() {
        return storeDetail;
    }

    public void setStoreDetail(StoreDetail storeDetail) {
        this.storeDetail = storeDetail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
