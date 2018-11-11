package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2017/11/10.
 * 兴趣爱好对象
 */

public class HobbyInfo implements Parcelable {
    private long hobbyId;//兴趣爱好id,
    private String hobbyName;//兴趣爱好名称,
    private String hobbyImg;//兴趣爱好图片,
    private String hobbyGrayImg;//兴趣爱好灰色图片
    private int hobbyType;//类型(1线下,2线上),
    private int isSelect;//是否选择(0未选,1已选),
    private int isCheck;//为了标志活动筛选的选中状态
    private int total;
    private int secondInterestType;// 2 标识最新
    private String hobbyDesc;// 兴趣爱好描述信息
    // 本地选择使用此字段
    private boolean isLocalSelectItem;

    public long getHobbyId() {
        return hobbyId;
    }

    public void setHobbyId(long hobbyId) {
        this.hobbyId = hobbyId;
    }

    public String getHobbyName() {
        return hobbyName;
    }

    public void setHobbyName(String hobbyName) {
        this.hobbyName = hobbyName;
    }

    public String getHobbyImg() {
        return hobbyImg;
    }

    public void setHobbyImg(String hobbyImg) {
        this.hobbyImg = hobbyImg;
    }

    public String getHobbyGrayImg() {
        return hobbyGrayImg;
    }

    public void setHobbyGrayImg(String hobbyGrayImg) {
        this.hobbyGrayImg = hobbyGrayImg;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public int getHobbyType() {
        return hobbyType;
    }

    public void setHobbyType(int hobbyType) {
        this.hobbyType = hobbyType;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSecondInterestType() {
        return secondInterestType;
    }

    public void setSecondInterestType(int secondInterestType) {
        this.secondInterestType = secondInterestType;
    }

    public String getHobbyDesc() {
        return hobbyDesc;
    }

    public void setHobbyDesc(String hobbyDesc) {
        this.hobbyDesc = hobbyDesc;
    }

    public HobbyInfo() {
    }

    public boolean isLocalSelectItem() {
        return isLocalSelectItem;
    }

    public void setLocalSelectItem(boolean localSelectItem) {
        isLocalSelectItem = localSelectItem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.hobbyId);
        dest.writeString(this.hobbyName);
        dest.writeString(this.hobbyImg);
        dest.writeString(this.hobbyGrayImg);
        dest.writeInt(this.hobbyType);
        dest.writeInt(this.isSelect);
        dest.writeInt(this.isCheck);
        dest.writeInt(this.total);
        dest.writeInt(this.secondInterestType);
        dest.writeString(this.hobbyDesc);
        dest.writeByte(this.isLocalSelectItem ? (byte) 1 : (byte) 0);
    }

    protected HobbyInfo(Parcel in) {
        this.hobbyId = in.readLong();
        this.hobbyName = in.readString();
        this.hobbyImg = in.readString();
        this.hobbyGrayImg = in.readString();
        this.hobbyType = in.readInt();
        this.isSelect = in.readInt();
        this.isCheck = in.readInt();
        this.total = in.readInt();
        this.secondInterestType = in.readInt();
        this.hobbyDesc = in.readString();
        this.isLocalSelectItem = in.readByte() != 0;
    }

    public static final Creator<HobbyInfo> CREATOR = new Creator<HobbyInfo>() {
        @Override
        public HobbyInfo createFromParcel(Parcel source) {
            return new HobbyInfo(source);
        }

        @Override
        public HobbyInfo[] newArray(int size) {
            return new HobbyInfo[size];
        }
    };
}
