package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.HelpComment;

/**
 * Created by ZHAI on 2018/3/1.
 */

public class GetHelpCommentListResponse extends BaseResponse {
    public List<HelpComment> list;
    public int total;

    public List<HelpComment> getList() {
        return list;
    }

    public void setList(List<HelpComment> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
