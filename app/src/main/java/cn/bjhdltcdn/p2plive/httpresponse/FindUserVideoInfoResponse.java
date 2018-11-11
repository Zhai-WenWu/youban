package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by ZHUDI on 2017/12/27.
 */

public class FindUserVideoInfoResponse extends BaseResponse {
    private int status;//是否接收陌生人来电(1接收,2拒绝),

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
