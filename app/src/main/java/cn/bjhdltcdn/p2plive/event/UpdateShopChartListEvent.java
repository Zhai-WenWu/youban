package cn.bjhdltcdn.p2plive.event;

import java.math.BigDecimal;

import cn.bjhdltcdn.p2plive.model.OrganMember;
import cn.bjhdltcdn.p2plive.model.ProductDetail;

/**
 * Created by ZHUDI on 2018/1/27.
 */

public class UpdateShopChartListEvent {
    private int type;//1:更新购物车 2：更新商品列表3:清空购物车 4:从商品详情更新商品列表
    private ProductDetail productDetail;
    private int total;
    private BigDecimal totalMoney=new BigDecimal(0);

    public UpdateShopChartListEvent(int type) {
        this.type = type;
    }

    public UpdateShopChartListEvent(int type, ProductDetail productDetail, int total, BigDecimal totalMoney) {
        this.type = type;
        this.productDetail = productDetail;
        this.total = total;
        this.totalMoney = totalMoney;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }
}
