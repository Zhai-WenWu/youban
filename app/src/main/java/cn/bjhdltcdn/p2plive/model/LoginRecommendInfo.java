package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZHAI on 2018/4/11.
 */

public class LoginRecommendInfo implements Parcelable {
    /* "recommendId":推荐ID
    "title":标题,
    "recommendType":推荐类型(1、该活动H5规则页面；2、圈子详情；3、同学帮帮忙详情；4、表白墙；5、商圈；6、附近活动列表),
    "imgUrl":图片路径,
    "gotoUrl":跳转链接,
    "parentId":父Id,
    "width":宽,
    "height":高,
    "clubType":圈子类型(0 普通 1 官方  2学校 )*/
    public long recommendId;
    public String title;
    public int recommendType;
    public String imgUrl;
    public String gotoUrl;
    public long parentId;
    public int width;
    public int height;
    public int clubType;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getClubType() {
        return clubType;
    }

    public void setClubType(int clubType) {
        this.clubType = clubType;
    }

    public long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(long recommendId) {
        this.recommendId = recommendId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(int recommendType) {
        this.recommendType = recommendType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGotoUrl() {
        return gotoUrl;
    }

    public void setGotoUrl(String gotoUrl) {
        this.gotoUrl = gotoUrl;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public LoginRecommendInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.recommendId);
        dest.writeString(this.title);
        dest.writeInt(this.recommendType);
        dest.writeString(this.imgUrl);
        dest.writeString(this.gotoUrl);
        dest.writeLong(this.parentId);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.clubType);
    }

    protected LoginRecommendInfo(Parcel in) {
        this.recommendId = in.readLong();
        this.title = in.readString();
        this.recommendType = in.readInt();
        this.imgUrl = in.readString();
        this.gotoUrl = in.readString();
        this.parentId = in.readLong();
        this.width = in.readInt();
        this.height = in.readInt();
        this.clubType = in.readInt();
    }

    public static final Creator<LoginRecommendInfo> CREATOR = new Creator<LoginRecommendInfo>() {
        @Override
        public LoginRecommendInfo createFromParcel(Parcel source) {
            return new LoginRecommendInfo(source);
        }

        @Override
        public LoginRecommendInfo[] newArray(int size) {
            return new LoginRecommendInfo[size];
        }
    };
}
