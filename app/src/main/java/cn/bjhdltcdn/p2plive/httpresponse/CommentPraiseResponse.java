package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by ZHUDI on 2018/3/3.
 */

public class CommentPraiseResponse extends BaseResponse {
    private int isPraise;//(1 点赞  2 取消点赞),

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }
}
