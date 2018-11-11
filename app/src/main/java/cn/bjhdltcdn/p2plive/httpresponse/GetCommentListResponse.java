package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ConfessionComment;

/**
 * Created by Hu_PC on 2017/11/17.
 */

public class GetCommentListResponse extends BaseResponse{
    private List<ConfessionComment> list;//[{ConfessionComment对象},{ConfessionComment对象},...]评价回复列表,
    private int total;//总数,
    private String blankHint;//空白提示语(暂无评论，快来写下第一条评论吧),


    public List<ConfessionComment> getList() {
        return list;
    }

    public void setList(List<ConfessionComment> list) {
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
