package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by Hu_PC on 2017/11/21.
 */

public class PostPraiseResponse extends BaseResponse{
     private int isPraise;//(1 点赞  2 取消点赞),

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }
}
