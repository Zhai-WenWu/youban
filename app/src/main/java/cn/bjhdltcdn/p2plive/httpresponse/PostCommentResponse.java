package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.Comment;

/**
 * Created by xiawenquan on 17/11/25.
 */

public class PostCommentResponse extends BaseResponse {
    private Comment comment;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
