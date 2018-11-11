package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.MyProps;
import cn.bjhdltcdn.p2plive.model.TransactionRecord;

/**
 * Created by ZHUDI on 2017/12/7.
 */

public class GetMyTransactionResponse extends BaseResponse {
    private List<TransactionRecord> list;
    private int total;//total,

    public List<TransactionRecord> getList() {
        return list;
    }

    public void setList(List<TransactionRecord> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
