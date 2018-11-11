package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.BlackUser;

/**
 * Created by ZHUDI on 2017/12/9.
 */

public class GetBlackListResponse extends BaseResponse {
    private List<BlackUser> list;//[{BlackUser黑名单对象},{BlackUser黑名单对象},....],
    private int total;//总数,

    public List<BlackUser> getList() {
        return list;
    }

    public void setList(List<BlackUser> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
