package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by Zhai_PC on 2018/4/17.
 */

public class ProductInfo implements Parcelable {
    /*  "productId":商品id,
    "productName":商品名称,
    "productDesc":商品描述信息,
    "productImg":商品图片,
    "productType":商品类型(1试用商品,2店铺商品),
    "productTotal":商品总数量,
    "productSaleTotal":商品售出数量,
    "productRemainTotal":商品剩余数量,
    "productPrice":商品原价,
    "productDiscount":商品折扣,
    "salePrice":商品售价,
    "startTime":开始时间,
    "endTime":结束时间,
    "saleTel":售后服务电话,
    "isApply":用户是否已申请试用(0未试用,1已试用),*/
    public long productId;
    public String productName;
    public String productDesc;
    public String productImg;
    public int productType;
    public int productTotal;
    public int productSaleTotal;
    public int productRemainTotal;
    public BigDecimal productPrice;
    public String productDiscount;
    public BigDecimal salePrice;
    public String startTime;
    public String endTime;
    public String saleTel;
    public int isApply;
    public int isHot;//是否热推商品(1否,2是),
    public int isNew;//是否新商品(1否,2是),
    public int status;//商品状态(1正常,2下架),
    private int productNum;//购买数量,
    private int saledTotal;//销售总次数


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getProductTotal() {
        return productTotal;
    }

    public void setProductTotal(int productTotal) {
        this.productTotal = productTotal;
    }

    public int getProductSaleTotal() {
        return productSaleTotal;
    }

    public void setProductSaleTotal(int productSaleTotal) {
        this.productSaleTotal = productSaleTotal;
    }

    public int getProductRemainTotal() {
        return productRemainTotal;
    }

    public void setProductRemainTotal(int productRemainTotal) {
        this.productRemainTotal = productRemainTotal;
    }



    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSaleTel() {
        return saleTel;
    }

    public void setSaleTel(String saleTel) {
        this.saleTel = saleTel;
    }

    public int getIsApply() {
        return isApply;
    }

    public void setIsApply(int isApply) {
        this.isApply = isApply;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
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

    public int getSaledTotal() {
        return saledTotal;
    }

    public void setSaledTotal(int saledTotal) {
        this.saledTotal = saledTotal;
    }

    public ProductInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productDesc);
        dest.writeString(this.productImg);
        dest.writeInt(this.productType);
        dest.writeInt(this.productTotal);
        dest.writeInt(this.productSaleTotal);
        dest.writeInt(this.productRemainTotal);
        dest.writeSerializable(this.productPrice);
        dest.writeString(this.productDiscount);
        dest.writeSerializable(this.salePrice);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.saleTel);
        dest.writeInt(this.isApply);
        dest.writeInt(this.isHot);
        dest.writeInt(this.isNew);
        dest.writeInt(this.status);
        dest.writeInt(this.productNum);
        dest.writeInt(this.saledTotal);
    }

    protected ProductInfo(Parcel in) {
        this.productId = in.readLong();
        this.productName = in.readString();
        this.productDesc = in.readString();
        this.productImg = in.readString();
        this.productType = in.readInt();
        this.productTotal = in.readInt();
        this.productSaleTotal = in.readInt();
        this.productRemainTotal = in.readInt();
        this.productPrice = (BigDecimal) in.readSerializable();
        this.productDiscount = in.readString();
        this.salePrice = (BigDecimal) in.readSerializable();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.saleTel = in.readString();
        this.isApply = in.readInt();
        this.isHot = in.readInt();
        this.isNew = in.readInt();
        this.status = in.readInt();
        this.productNum = in.readInt();
        this.saledTotal = in.readInt();
    }

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
        @Override
        public ProductInfo createFromParcel(Parcel source) {
            return new ProductInfo(source);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };
}
