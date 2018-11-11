package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.HomeInfo;

/**
 * Created by Hu_PC on 2017/11/10.
 */

public class GetApplyListResponse extends BaseResponse{
    private List<HomeInfo> list;//[{HomeInfo},{HomeInfo}...]首页关注,
    private int total;//总数,

    public List<HomeInfo> getList() {
        return list;
    }

    public void setList(List<HomeInfo> list) {
        this.list = list;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
