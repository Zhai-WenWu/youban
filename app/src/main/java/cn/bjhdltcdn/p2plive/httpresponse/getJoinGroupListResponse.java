package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.Group;

/**
 * Created by ZHAI on 2017/12/27.
 */

public class getJoinGroupListResponse extends BaseResponse {
    public List<Group> list;
    public int total;
    public String blankHint;

    public List<Group> getList() {
        return list;
    }

    public void setList(List<Group> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String getBlankHint() {
        return blankHint;
    }

    @Override
    public void setBlankHint(String blankHint) {
        this.blankHint = blankHint;
    }
}
