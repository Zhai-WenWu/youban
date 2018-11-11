package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ClertInfo;

/**
 * Created by zhaiww on 2018/5/19.
 */

public class GetClertListResponse extends BaseResponse {
    public List<ClertInfo> list;
    public int total;

    public List<ClertInfo> getList() {
        return list;
    }

    public void setList(List<ClertInfo> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
