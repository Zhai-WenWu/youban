package cn.bjhdltcdn.p2plive.event;

import java.math.BigDecimal;
import java.util.List;

import cn.bjhdltcdn.p2plive.model.ProductImage;

/**
 * Created by ZHUDI on 2017/12/2.
 */

public class UpdateGoodsListEvent {
    private int type;//1:修改商品信息2：添加新商品
    private long productId;
    private String productName,productDesc,productDiscount,productRemainTotal;
    private BigDecimal productPrice,salePrice;
    private List<ProductImage> productImageList;

    public UpdateGoodsListEvent(int type, String productName, String productDesc, BigDecimal productPrice, String productDiscount, BigDecimal salePrice, String productRemainTotal, List<ProductImage> productImageList) {
        this.type = type;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.salePrice = salePrice;
        this.productRemainTotal = productRemainTotal;
        this.productImageList=productImageList;
    }

    public UpdateGoodsListEvent(int type, long productId, String productName, String productDesc, String productDiscount, String productRemainTotal, BigDecimal productPrice, BigDecimal salePrice, List<ProductImage> productImageList) {
        this.type = type;
        this.productId = productId;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productDiscount = productDiscount;
        this.productRemainTotal = productRemainTotal;
        this.productPrice = productPrice;
        this.salePrice = salePrice;
        this.productImageList = productImageList;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }


    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getProductRemainTotal() {
        return productRemainTotal;
    }

    public void setProductRemainTotal(String productRemainTotal) {
        this.productRemainTotal = productRemainTotal;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ProductImage> getProductImageList() {
        return productImageList;
    }

    public void setProductImageList(List<ProductImage> productImageList) {
        this.productImageList = productImageList;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
