package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.ShopComment;

/**
 * Created by zhaiww on 2018/5/14.
 */

public class SaveOrderEvaluateResponse extends BaseResponse {
    public ShopComment comment;

    public ShopComment getComment() {
        return comment;
    }

    public void setComment(ShopComment comment) {
        this.comment = comment;
    }
}
