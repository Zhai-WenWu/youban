package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhaiww on 2018/5/14.
 */

public class StoreInfo implements Parcelable {
    /*"storeId":店铺ID
    "userId":创建用户Id,
    "baseUser":{BaseUser对象}店主基本信息,
    "storeIcon":店铺头像,
    "title":店铺名称,
    "labelList":[{LabelInfo对象},{LabelInfo对象}...]店铺类别列表,
    "storeDesc":店铺描述,
    "locationInfo":{LocationInfo对象}店铺位置信息,
    "distributeMode":送货方式(1自提,2卖家配送,3都支持),
    "floorPrice":起送价格,
    "isRecruitClert":是否招聘店员(0不招聘,1招聘),
    "isCheck":是否支持校友验货(0不支持,1支持),
    "storeImageList":[{StoreImage对象},{StoreImage对象}...]店铺图片列表,
    "merchantId":第三方商家Id(没有默认0),
    "status":店铺状态(0开启,1审核中,2暂停营业,3删除),*/

    private long storeId;
    private long userId;
    private BaseUser baseUser;
    private String storeIcon;
    private String title;
    private List<LabelInfo> labelList;
    private String storeDesc;
    private LocationInfo locationInfo;
    private List<LabelInfo>  distributeList;//[{LabelInfo对象},{LabelInfo对象}...]送货方式列表,
    private String floorPrice;
    private int isRecruitClert;
    private int isCheck;
    private List<StoreImage> storeImageList;
    private long merchantId;
    private int status;
    private String schoolName;
    private int isPublish;//是否发布招聘信息(0未发布,1已发布),
    private long postId;//已发布返回帖子Id,
    private String distance;//距离
    private int isReportMax;
    private int isAuth;
    private int Distribution;//配送范围(0本校1全国),
    private String postage;//邮费价格,
    private int isSchoolPostage;//本校包邮(0是,1否),
    private FirstLabelInfo firstLabelInfo;


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

    public List<StoreImage> getStoreImageList() {
        return storeImageList;
    }

    public void setStoreImageList(List<StoreImage> storeImageList) {
        this.storeImageList = storeImageList;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public String getStoreIcon() {
        return storeIcon;
    }

    public void setStoreIcon(String storeIcon) {
        this.storeIcon = storeIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LabelInfo> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<LabelInfo> labelList) {
        this.labelList = labelList;
    }

    public String getStoreDesc() {
        return storeDesc;
    }

    public void setStoreDesc(String storeDesc) {
        this.storeDesc = storeDesc;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public List<LabelInfo> getDistributeList() {
        return distributeList;
    }

    public void setDistributeList(List<LabelInfo> distributeList) {
        this.distributeList = distributeList;
    }

    public String getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(String floorPrice) {
        this.floorPrice = floorPrice;
    }

    public int getIsRecruitClert() {
        return isRecruitClert;
    }

    public void setIsRecruitClert(int isRecruitClert) {
        this.isRecruitClert = isRecruitClert;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(int isPublish) {
        this.isPublish = isPublish;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getDistribution() {
        return Distribution;
    }

    public void setDistribution(int distribution) {
        Distribution = distribution;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public int getIsSchoolPostage() {
        return isSchoolPostage;
    }

    public void setIsSchoolPostage(int isSchoolPostage) {
        this.isSchoolPostage = isSchoolPostage;
    }

    public FirstLabelInfo getFirstLabelInfo() {
        return firstLabelInfo;
    }

    public void setFirstLabelInfo(FirstLabelInfo firstLabelInfo) {
        this.firstLabelInfo = firstLabelInfo;
    }

    public StoreInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.storeId);
        dest.writeLong(this.userId);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeString(this.storeIcon);
        dest.writeString(this.title);
        dest.writeTypedList(this.labelList);
        dest.writeString(this.storeDesc);
        dest.writeParcelable(this.locationInfo, flags);
        dest.writeTypedList(this.distributeList);
        dest.writeString(this.floorPrice);
        dest.writeInt(this.isRecruitClert);
        dest.writeInt(this.isCheck);
        dest.writeTypedList(this.storeImageList);
        dest.writeLong(this.merchantId);
        dest.writeInt(this.status);
        dest.writeString(this.schoolName);
        dest.writeInt(this.isPublish);
        dest.writeLong(this.postId);
        dest.writeString(this.distance);
        dest.writeInt(this.isReportMax);
        dest.writeInt(this.isAuth);
        dest.writeInt(this.Distribution);
        dest.writeString(this.postage);
        dest.writeInt(this.isSchoolPostage);
        dest.writeParcelable(this.firstLabelInfo, flags);
    }

    protected StoreInfo(Parcel in) {
        this.storeId = in.readLong();
        this.userId = in.readLong();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.storeIcon = in.readString();
        this.title = in.readString();
        this.labelList = in.createTypedArrayList(LabelInfo.CREATOR);
        this.storeDesc = in.readString();
        this.locationInfo = in.readParcelable(LocationInfo.class.getClassLoader());
        this.distributeList = in.createTypedArrayList(LabelInfo.CREATOR);
        this.floorPrice = in.readString();
        this.isRecruitClert = in.readInt();
        this.isCheck = in.readInt();
        this.storeImageList = in.createTypedArrayList(StoreImage.CREATOR);
        this.merchantId = in.readLong();
        this.status = in.readInt();
        this.schoolName = in.readString();
        this.isPublish = in.readInt();
        this.postId = in.readLong();
        this.distance = in.readString();
        this.isReportMax = in.readInt();
        this.isAuth = in.readInt();
        this.Distribution = in.readInt();
        this.postage = in.readString();
        this.isSchoolPostage = in.readInt();
        this.firstLabelInfo = in.readParcelable(FirstLabelInfo.class.getClassLoader());
    }

    public static final Creator<StoreInfo> CREATOR = new Creator<StoreInfo>() {
        @Override
        public StoreInfo createFromParcel(Parcel source) {
            return new StoreInfo(source);
        }

        @Override
        public StoreInfo[] newArray(int size) {
            return new StoreInfo[size];
        }
    };
}
