package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ProductOrder;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetSalesRecordListResponse extends BaseResponse {
    private List<ProductOrder> list;//[{ProductOrder对象},{ProductOrder对象}...]商品订单列表,
    private int total;//总数,
    private String content;//本周共交易 XX 笔订单,

    public List<ProductOrder> getList() {
        return list;
    }

    public void setList(List<ProductOrder> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
