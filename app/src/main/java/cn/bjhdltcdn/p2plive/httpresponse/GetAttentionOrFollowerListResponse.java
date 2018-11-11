package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.User;

/**
 * Created by ZHUDI on 2017/12/2.
 */

public class GetAttentionOrFollowerListResponse extends BaseResponse {
    private List<User> list;//:[{User},{User},...],
    private int total;//总数,

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
