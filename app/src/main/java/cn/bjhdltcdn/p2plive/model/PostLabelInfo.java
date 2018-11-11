package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description:帖子标签对象
 * Data: 2018/8/4
 *
 * @author: zhudi
 */
public class PostLabelInfo implements Parcelable {
    private long postLabelId;//帖子标签id,
    private String labelName;//标签名称,
    private String labelDesc;//标签描述,
    private int labelType;//(0 官方标签,1 自定义标签),
    private int status;//状态(0正常,1禁用),
    private int total;//标签对应的帖子总数,
    private String addTime;//添加时间,

    private int isSelect;//是否选择(0未选择,1选择),

    public long getPostLabelId() {
        return postLabelId;
    }

    public void setPostLabelId(long postLabelId) {
        this.postLabelId = postLabelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelDesc() {
        return labelDesc;
    }

    public void setLabelDesc(String labelDesc) {
        this.labelDesc = labelDesc;
    }

    public int getLabelType() {
        return labelType;
    }

    public void setLabelType(int labelType) {
        this.labelType = labelType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.postLabelId);
        dest.writeString(this.labelName);
        dest.writeString(this.labelDesc);
        dest.writeInt(this.labelType);
        dest.writeInt(this.status);
        dest.writeInt(this.total);
        dest.writeString(this.addTime);
        dest.writeInt(this.isSelect);
    }

    public PostLabelInfo() {
    }

    protected PostLabelInfo(Parcel in) {
        this.postLabelId = in.readLong();
        this.labelName = in.readString();
        this.labelDesc = in.readString();
        this.labelType = in.readInt();
        this.status = in.readInt();
        this.total = in.readInt();
        this.addTime = in.readString();
        this.isSelect = in.readInt();
    }

    public static final Parcelable.Creator<PostLabelInfo> CREATOR = new Parcelable.Creator<PostLabelInfo>() {
        @Override
        public PostLabelInfo createFromParcel(Parcel source) {
            return new PostLabelInfo(source);
        }

        @Override
        public PostLabelInfo[] newArray(int size) {
            return new PostLabelInfo[size];
        }
    };
}
