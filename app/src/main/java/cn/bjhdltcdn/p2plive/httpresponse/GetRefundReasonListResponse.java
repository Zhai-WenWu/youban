package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.RefundReason;

/**
 * Created by zhaiww on 2018/5/16.
 */

public class GetRefundReasonListResponse extends BaseResponse {
    public List <RefundReason> list;

    public List<RefundReason> getList() {
        return list;
    }

    public void setList(List<RefundReason> list) {
        this.list = list;
    }
}
