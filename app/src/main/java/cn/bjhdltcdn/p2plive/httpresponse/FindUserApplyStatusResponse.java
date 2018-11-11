package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by zhaiww on 2018/7/10.
 */

public class FindUserApplyStatusResponse extends BaseResponse {
    private int status;// 1等待审核,2审核通过,3审核拒绝,4审核超时

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
