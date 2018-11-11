package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2018/9/1.
 */

public class ProductInfoDetail implements Parcelable {
    private ProductInfo productInfo;
    private int isClert;//当前是否是店员(1店主,2店员,3普通用户,4用户已申请店员)
    private int isSchoolmate;//是否是校友(1否2是)
    private int isReportMax;//本月被举报次数是否超过20次(0,否1是)
    private int isAuth;//开店申请状态(1等待审核  2 审核通过 3审核拒绝)
    private long storeId;//
    private String title;//

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
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

    public int getIsReportMax() {
        return isReportMax;
    }

    public void setIsReportMax(int isReportMax) {
        this.isReportMax = isReportMax;
    }

    public int getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(int isAuth) {
        this.isAuth = isAuth;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProductInfoDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.productInfo, flags);
        dest.writeInt(this.isClert);
        dest.writeInt(this.isSchoolmate);
        dest.writeInt(this.isReportMax);
        dest.writeInt(this.isAuth);
        dest.writeLong(this.storeId);
        dest.writeString(this.title);
    }

    protected ProductInfoDetail(Parcel in) {
        this.productInfo = in.readParcelable(ProductInfo.class.getClassLoader());
        this.isClert = in.readInt();
        this.isSchoolmate = in.readInt();
        this.isReportMax = in.readInt();
        this.isAuth = in.readInt();
        this.storeId = in.readLong();
        this.title = in.readString();
    }

    public static final Creator<ProductInfoDetail> CREATOR = new Creator<ProductInfoDetail>() {
        @Override
        public ProductInfoDetail createFromParcel(Parcel source) {
            return new ProductInfoDetail(source);
        }

        @Override
        public ProductInfoDetail[] newArray(int size) {
            return new ProductInfoDetail[size];
        }
    };
}
