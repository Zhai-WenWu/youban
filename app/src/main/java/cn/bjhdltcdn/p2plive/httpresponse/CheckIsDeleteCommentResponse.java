package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by xiawenquan on 18/1/22.
 */

public class CheckIsDeleteCommentResponse extends BaseResponse {
    //删除状态(0未删除,1删除)
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
