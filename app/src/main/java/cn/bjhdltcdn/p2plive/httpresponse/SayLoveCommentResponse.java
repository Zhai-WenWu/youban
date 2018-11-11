package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.ConfessionComment;

/**
 * Created by Hu_PC on 2017/11/17.
 */

public class SayLoveCommentResponse extends BaseResponse{
    private ConfessionComment comment;//{ConfessionComment对象},

    public ConfessionComment getComment() {
        return comment;
    }

    public void setComment(ConfessionComment comment) {
        this.comment = comment;
    }
}
