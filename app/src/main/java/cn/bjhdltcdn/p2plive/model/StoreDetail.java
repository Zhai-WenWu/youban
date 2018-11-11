package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Hu_PC on 2018/5/11.
 */

public class StoreDetail implements Parcelable {
    private StoreInfo storeInfo;//{StoreInfo对象}店铺信息,
    private int isClert;//当前是否是店员(1店主,2店员,3普通用户,4校友),
    private int isSchoolmate;//是否是校友(1否2是),
    private double evaluateScore;//评价星级,
    private int salesVolume;//销售量,
    private String schoolName;//学校名称,
    private String distance;//距离,
    private int clertNum;//店员数量(不包含店主),
    private int isReportMax;//本月被举报次数是否超过20次, 0:正常1：超过
    private int isAuth;//开店申请状态(1等待审核  2 审核通过 3审核拒绝)
    private List<ProductInfo> productList;


    public StoreDetail() {
    }

    public StoreDetail(StoreInfo storeInfo, int isClert, int isSchoolmate, double evaluateScore, int salesVolume, String schoolName, String distance, int clertNum, int isReportMax, int isAuth, List<ProductInfo> productList) {
        this.storeInfo = storeInfo;
        this.isClert = isClert;
        this.isSchoolmate = isSchoolmate;
        this.evaluateScore = evaluateScore;
        this.salesVolume = salesVolume;
        this.schoolName = schoolName;
        this.distance = distance;
        this.clertNum = clertNum;
        this.isReportMax = isReportMax;
        this.isAuth = isAuth;
        this.productList = productList;
    }

    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }

    public int getIsClert() {
        return isClert;
    }

    public void setIsClert(int isClert) {
        this.isClert = isClert;
    }

    public double getEvaluateScore() {
        return evaluateScore;
    }

    public void setEvaluateScore(double evaluateScore) {
        this.evaluateScore = evaluateScore;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getClertNum() {
        return clertNum;
    }

    public void setClertNum(int clertNum) {
        this.clertNum = clertNum;
    }

    public int getIsReportMax() {
        return isReportMax;
    }

    public void setIsReportMax(int isReportMax) {
        this.isReportMax = isReportMax;
    }

    public int getIsSchoolmate() {
        return isSchoolmate;
    }

    public void setIsSchoolmate(int isSchoolmate) {
        this.isSchoolmate = isSchoolmate;
    }

    public int getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(int isAuth) {
        this.isAuth = isAuth;
    }

    public List<ProductInfo> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductInfo> productList) {
        this.productList = productList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.storeInfo, flags);
        dest.writeInt(this.isClert);
        dest.writeInt(this.isSchoolmate);
        dest.writeDouble(this.evaluateScore);
        dest.writeInt(this.salesVolume);
        dest.writeString(this.schoolName);
        dest.writeString(this.distance);
        dest.writeInt(this.clertNum);
        dest.writeInt(this.isReportMax);
        dest.writeInt(this.isAuth);
        dest.writeTypedList(this.productList);
    }

    protected StoreDetail(Parcel in) {
        this.storeInfo = in.readParcelable(StoreInfo.class.getClassLoader());
        this.isClert = in.readInt();
        this.isSchoolmate = in.readInt();
        this.evaluateScore = in.readDouble();
        this.salesVolume = in.readInt();
        this.schoolName = in.readString();
        this.distance = in.readString();
        this.clertNum = in.readInt();
        this.isReportMax = in.readInt();
        this.isAuth = in.readInt();
        this.productList = in.createTypedArrayList(ProductInfo.CREATOR);
    }

    public static final Creator<StoreDetail> CREATOR = new Creator<StoreDetail>() {
        @Override
        public StoreDetail createFromParcel(Parcel source) {
            return new StoreDetail(source);
        }

        @Override
        public StoreDetail[] newArray(int size) {
            return new StoreDetail[size];
        }
    };
}
