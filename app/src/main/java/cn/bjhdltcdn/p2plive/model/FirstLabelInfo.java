package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hu_PC on 2018/8/3.
 */

public class FirstLabelInfo implements Parcelable {
    private long firstLabelId;//一级标签id,
    private String firstLabelName;//一级标签名称,
    private String firstLabelDesc;//一级标签描述,
    private String imageUrl;//一级标签图片,
    private int status;//状态(0正常,1禁用),
    private List<SecondLabelInfo> list;//{SecondLabelInfo},{SecondLabelInfo}对象..
    private String addTime;//添加时间,
    private int isCheck;//1:选中 0：未选中 客户端记录用的

    public FirstLabelInfo() {
    }

    public FirstLabelInfo(long firstLabelId, String firstLabelName, String firstLabelDesc, String imageUrl, int status, List<SecondLabelInfo> list, String addTime) {
        this.firstLabelId = firstLabelId;
        this.firstLabelName = firstLabelName;
        this.firstLabelDesc = firstLabelDesc;
        this.imageUrl = imageUrl;
        this.status = status;
        this.list = list;
        this.addTime = addTime;
    }

    public long getFirstLabelId() {
        return firstLabelId;
    }

    public void setFirstLabelId(long firstLabelId) {
        this.firstLabelId = firstLabelId;
    }

    public String getFirstLabelName() {
        return firstLabelName;
    }

    public void setFirstLabelName(String firstLabelName) {
        this.firstLabelName = firstLabelName;
    }

    public String getFirstLabelDesc() {
        return firstLabelDesc;
    }

    public void setFirstLabelDesc(String firstLabelDesc) {
        this.firstLabelDesc = firstLabelDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SecondLabelInfo> getList() {
        return list;
    }

    public void setList(List<SecondLabelInfo> list) {
        this.list = list;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.firstLabelId);
        dest.writeString(this.firstLabelName);
        dest.writeString(this.firstLabelDesc);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.status);
        dest.writeList(this.list);
        dest.writeString(this.addTime);
    }

    protected FirstLabelInfo(Parcel in) {
        this.firstLabelId = in.readLong();
        this.firstLabelName = in.readString();
        this.firstLabelDesc = in.readString();
        this.imageUrl = in.readString();
        this.status = in.readInt();
        this.list = new ArrayList<SecondLabelInfo>();
        in.readList(this.list, SecondLabelInfo.class.getClassLoader());
        this.addTime = in.readString();
    }

    public static final Creator<FirstLabelInfo> CREATOR = new Creator<FirstLabelInfo>() {
        @Override
        public FirstLabelInfo createFromParcel(Parcel source) {
            return new FirstLabelInfo(source);
        }

        @Override
        public FirstLabelInfo[] newArray(int size) {
            return new FirstLabelInfo[size];
        }
    };
}
