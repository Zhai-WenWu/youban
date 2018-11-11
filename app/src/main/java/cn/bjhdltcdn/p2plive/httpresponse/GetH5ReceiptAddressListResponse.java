package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.AddressInfo;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetH5ReceiptAddressListResponse extends BaseResponse {
    private int total;//总条数
    private List<AddressInfo> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<AddressInfo> getList() {
        return list;
    }

    public void setList(List<AddressInfo> list) {
        this.list = list;
    }
}
