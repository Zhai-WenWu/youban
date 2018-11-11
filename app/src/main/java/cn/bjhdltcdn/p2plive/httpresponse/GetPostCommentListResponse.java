package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.Comment;

/**
 * Created by Hu_PC on 2017/11/17.
 */

public class GetPostCommentListResponse extends BaseResponse{
    private List<Comment> list;//[{Comment对象},{Comment对象},...]评价回复列表,
    private int total;//总数,

    public List<Comment> getList() {
        return list;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
