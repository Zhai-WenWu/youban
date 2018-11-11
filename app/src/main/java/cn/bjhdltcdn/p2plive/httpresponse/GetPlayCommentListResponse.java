package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.PlayComment;

/**
 * Created by ZHAI on 2017/12/23.
 */

public class GetPlayCommentListResponse extends BaseResponse {
    public List<PlayComment> list;
    public int total;

    public List<PlayComment> getList() {
        return list;
    }

    public void setList(List<PlayComment> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
