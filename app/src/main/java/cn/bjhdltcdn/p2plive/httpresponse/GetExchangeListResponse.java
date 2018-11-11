package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ExchangeRecord;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetExchangeListResponse extends BaseResponse {
    private int exchangeAmount;//提现总计,
    private List<ExchangeRecord> list;//[{ExchangeRecord对象},{ExchangeRecord对象}...]提现明细列表,
    private int total;//总条数

    public int getExchangeAmount() {
        return exchangeAmount;
    }

    public void setExchangeAmount(int exchangeAmount) {
        this.exchangeAmount = exchangeAmount;
    }

    public List<ExchangeRecord> getList() {
        return list;
    }

    public void setList(List<ExchangeRecord> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
