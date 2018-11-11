package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.PlayInfo;

/**
 * Created by ZHAI on 2017/12/20.
 */

public class GetPlayListResponse extends BaseResponse {
    private List<PlayInfo> list;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<PlayInfo> getList() {

        return list;
    }

    public void setList(List<PlayInfo> list) {
        this.list = list;
    }
}
