package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.PlayComment;

/**
 * Created by ZHAI on 2018/1/6.
 */

public class PlayCommentResponse extends BaseResponse {
    public PlayComment comment;

    public PlayComment getPlayComment() {
        return comment;
    }

    public void setPlayComment(PlayComment playComment) {
        this.comment = playComment;
    }
}
