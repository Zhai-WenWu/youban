package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.AddressInfo;
import cn.bjhdltcdn.p2plive.model.OrganMember;

/**
 * Created by ZHUDI on 2018/1/27.
 */

public class UpdateGoodsPayAddressEvent {

    private AddressInfo addressInfo;

    public UpdateGoodsPayAddressEvent(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }
}
