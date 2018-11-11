package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.MyProps;


public class GetWheatPropsDataResponse extends BaseResponse {
    private List<MyProps> receiveList;
    private int total;

    public List<MyProps> getReceiveList() {
        return receiveList;
    }

    public void setReceiveList(List<MyProps> receiveList) {
        this.receiveList = receiveList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
