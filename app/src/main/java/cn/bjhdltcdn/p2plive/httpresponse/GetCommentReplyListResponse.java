package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.Comment;

/**
 * Created by Hu_PC on 2017/11/17.
 */

public class GetCommentReplyListResponse extends BaseResponse {
    private Comment commentInfo;//{Comment帖子/ConfessionComment表白/PlayCommentPK挑战/HelpComment帮帮忙对象},
    private List<Comment> list;//[{Comment对象},{Comment对象},...]评价回复列表,
    private int total;//总数,
    private String blankHint;//空白提示语,

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

    public Comment getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(Comment commentInfo) {
        this.commentInfo = commentInfo;
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
