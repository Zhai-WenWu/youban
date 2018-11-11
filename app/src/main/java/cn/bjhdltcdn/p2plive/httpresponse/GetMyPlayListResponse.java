package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.PlayInfo;

/**
 * Created by ZHAI on 2017/12/23.
 */

public class GetMyPlayListResponse extends BaseResponse{
    private List<PlayInfo> list;
    private int total;

    public List<PlayInfo> getList() {
        return list;
    }

    public void setList(List<PlayInfo> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
