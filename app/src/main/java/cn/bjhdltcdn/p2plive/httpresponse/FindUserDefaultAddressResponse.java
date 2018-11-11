package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.AddressInfo;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class FindUserDefaultAddressResponse extends BaseResponse {
    private AddressInfo addressInfo;

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }
}
