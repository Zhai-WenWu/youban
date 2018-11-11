package cn.bjhdltcdn.p2plive.httpresponse;


import java.util.List;

import cn.bjhdltcdn.p2plive.model.HelpInfo;

/**
 * Created by ZHAI on 2018/2/27.
 */

public class GetClassmateHelpListResponse extends BaseResponse {
    private List<HelpInfo> list;//同学帮帮忙列表,
    private int total;

    public List<HelpInfo> getList() {
        return list;
    }

    public void setList(List<HelpInfo> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
