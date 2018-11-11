package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.HelpComment;

/**
 * Created by ZHUDI on 2018/2/26.
 */

public class HelpCommentResponse extends BaseResponse {
    private HelpComment comment;//{Comment帖子/ConfessionComment表白/PlayCommentPK挑战/HelpComment帮帮忙 对象},

    public HelpComment getComment() {
        return comment;
    }

    public void setComment(HelpComment comment) {
        this.comment = comment;
    }
}
