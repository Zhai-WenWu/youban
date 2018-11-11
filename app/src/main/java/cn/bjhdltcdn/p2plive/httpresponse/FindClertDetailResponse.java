package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ClertDetail;
import cn.bjhdltcdn.p2plive.model.ProductOrder;

/**
 * Created by zhaiww on 2018/5/22.
 */

public class FindClertDetailResponse extends BaseResponse {
    /*"clertDetail":{ClertDetail对象}店员详情对象,
    "list":[{ProductOrder对象},{ProductOrder对象}...]店员接单商品列表,
    "total":总数,
    "userRole":当前用户角色,*/
    public ClertDetail clertDetail;
    public List<ProductOrder> list;
    public int total;
    public int userRole;

    public ClertDetail getClertDetail() {
        return clertDetail;
    }

    public void setClertDetail(ClertDetail clertDetail) {
        this.clertDetail = clertDetail;
    }

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

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }
}
