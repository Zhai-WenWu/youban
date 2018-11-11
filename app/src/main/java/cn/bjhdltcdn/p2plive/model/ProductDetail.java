package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hu_PC on 2018/5/11.
 */

public class ProductDetail implements Parcelable {
    private ProductInfo productInfo;//{ProductInfo对象}商品对象,
    private List<ProductImage> imageList;//[{ProductImage对象},{ProductImage对象}...]商品图片列表,
    private List<ProductImage> detaiLimageList;//[{ProductImage对象},{ProductImage对象}...]商品详情图片列表,
    private int applyCount;//申请人数,
    private long applyId;//申请Id(没有值默认0),
    private int buyCount;//购买人数,
    private List<ProductDetail> productList;//[{ProductDetail对象},{ProductDetail对象}...]推荐商品列表,
    private int isSchoolmate;//是否是校友(1否2是),
    private int isClert;//当前是否是店员(1店主,2店员,3普通用户,4校友),

    public ProductDetail() {
    }

    public ProductDetail(ProductInfo productInfo, List<ProductImage> imageList, List<ProductImage> detaiLimageList, int applyCount, long applyId, int buyCount, List<ProductDetail> productList, int isSchoolmate, int isClert) {
        this.productInfo = productInfo;
        this.imageList = imageList;
        this.detaiLimageList = detaiLimageList;
        this.applyCount = applyCount;
        this.applyId = applyId;
        this.buyCount = buyCount;
        this.productList = productList;
        this.isSchoolmate = isSchoolmate;
        this.isClert = isClert;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public List<ProductImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<ProductImage> imageList) {
        this.imageList = imageList;
    }

    public int getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(int applyCount) {
        this.applyCount = applyCount;
    }

    public long getApplyId() {
        return applyId;
    }

    public void setApplyId(long applyId) {
        this.applyId = applyId;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public List<ProductDetail> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDetail> productList) {
        this.productList = productList;
    }

    public int getIsClert() {
        return isClert;
    }

    public void setIsClert(int isClert) {
        this.isClert = isClert;
    }

    public int getIsSchoolmate() {
        return isSchoolmate;
    }

    public void setIsSchoolmate(int isSchoolmate) {
        this.isSchoolmate = isSchoolmate;
    }

    public List<ProductImage> getDetaiLimageList() {
        return detaiLimageList;
    }

    public void setDetaiLimageList(List<ProductImage> detaiLimageList) {
        this.detaiLimageList = detaiLimageList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.productInfo, flags);
        dest.writeTypedList(this.imageList);
        dest.writeTypedList(this.detaiLimageList);
        dest.writeInt(this.applyCount);
        dest.writeLong(this.applyId);
        dest.writeInt(this.buyCount);
        dest.writeTypedList(this.productList);
        dest.writeInt(this.isSchoolmate);
        dest.writeInt(this.isClert);
    }

    protected ProductDetail(Parcel in) {
        this.productInfo = in.readParcelable(ProductInfo.class.getClassLoader());
        this.imageList = in.createTypedArrayList(ProductImage.CREATOR);
        this.detaiLimageList = in.createTypedArrayList(ProductImage.CREATOR);
        this.applyCount = in.readInt();
        this.applyId = in.readLong();
        this.buyCount = in.readInt();
        this.productList = in.createTypedArrayList(ProductDetail.CREATOR);
        this.isSchoolmate = in.readInt();
        this.isClert = in.readInt();
    }

    public static final Creator<ProductDetail> CREATOR = new Creator<ProductDetail>() {
        @Override
        public ProductDetail createFromParcel(Parcel source) {
            return new ProductDetail(source);
        }

        @Override
        public ProductDetail[] newArray(int size) {
            return new ProductDetail[size];
        }
    };
}
