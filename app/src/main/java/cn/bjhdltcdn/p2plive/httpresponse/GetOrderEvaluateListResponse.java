package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.EvaluateCountInfo;
import cn.bjhdltcdn.p2plive.model.OrderEvaluate;
import cn.bjhdltcdn.p2plive.model.ShopComment;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetOrderEvaluateListResponse extends BaseResponse {
    private List<EvaluateCountInfo> typeList;//[{EvaluateCountInfo对象},{EvaluateCountInfo对象}...]不同类型总条数,
    private List<ShopComment> list;//[{OrderEvaluate对象},{OrderEvaluate对象}...]订单评价列表,
    private int total;//总数,

    public List<EvaluateCountInfo> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<EvaluateCountInfo> typeList) {
        this.typeList = typeList;
    }

    public List<ShopComment> getList() {
        return list;
    }

    public void setList(List<ShopComment> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
