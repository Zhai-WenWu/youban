package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.MyProps;

/**
 * Created by ZHUDI on 2017/12/7.
 */

public class MyPropsListResponse extends BaseResponse {
    private List<MyProps> list;//[{MyProps对象,MyProps对象},...],
    private int total;//总数,
    private int totalGold;//收到礼物金币总数,
    private int unExchangeGold;//未提现收益,
    private String exchangeDescImg;//提现说明图片路径,
    private String exchangeDescUrl;//提现说明链接,

    public List<MyProps> getList() {
        return list;
    }

    public void setList(List<MyProps> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }

    public int getUnExchangeGold() {
        return unExchangeGold;
    }

    public void setUnExchangeGold(int unExchangeGold) {
        this.unExchangeGold = unExchangeGold;
    }

    public String getExchangeDescImg() {
        return exchangeDescImg;
    }

    public void setExchangeDescImg(String exchangeDescImg) {
        this.exchangeDescImg = exchangeDescImg;
    }

    public String getExchangeDescUrl() {
        return exchangeDescUrl;
    }

    public void setExchangeDescUrl(String exchangeDescUrl) {
        this.exchangeDescUrl = exchangeDescUrl;
    }
}
