package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.BaseUser;

/**
 * Created by xiawenquan on 17/12/27.
 */

public class SendActivityInviationResponse extends BaseResponse {
    private List<BaseUser> list;
    private int total;

    public List<BaseUser> getList() {
        return list;
    }

    public void setList(List<BaseUser> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
