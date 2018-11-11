package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2017/11/13.
 */

public class LabelInfo implements Parcelable {
    private long labelId;//标签ID,
    private String labelName;//标签名称',
    private long upperLabelId;//上级标签Id,
    private int isSelect;//是否选择(0未选择,1选择),
    private String labelDesc;//标签描述,
    private String content;//标签文案,
    private boolean isCheck;//客户端标志是否选中

    private int secondLabelCount;//二级标签数量
    private int limitMode;//限制模块(1仅帖子,2仅表白,3仅帮帮忙)
    private int selectNum;//（一级标签返回）选择的个数

    private int isImg;//是否可以发送图片(0 否,1 是)
    private int isGif;//是否可以发送gif(0 否,1 是)
    private int isVideo;//是否可以发送视频(0 否,1 是)

    private int labelType;//类型(1视频房间标签,2比赛标签,3首页帖子排序标签,4试用标签,5买家查询订单排序,6卖家查询订单排序,7店铺排序,8店铺标签,9我要助力打call的活动)

    public long getLabelId() {
        return labelId;
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public long getUpperLabelId() {
        return upperLabelId;
    }

    public void setUpperLabelId(long upperLabelId) {
        this.upperLabelId = upperLabelId;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public String getLabelDesc() {
        return labelDesc;
    }

    public void setLabelDesc(String labelDesc) {
        this.labelDesc = labelDesc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getSecondLabelCount() {
        return secondLabelCount;
    }

    public void setSecondLabelCount(int secondLabelCount) {
        this.secondLabelCount = secondLabelCount;
    }

    public int getLimitMode() {
        return limitMode;
    }

    public void setLimitMode(int limitMode) {
        this.limitMode = limitMode;
    }


    public int getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(int selectNum) {
        this.selectNum = selectNum;
    }

    public int getIsImg() {
        return isImg;
    }

    public void setIsImg(int isImg) {
        this.isImg = isImg;
    }

    public int getIsGif() {
        return isGif;
    }

    public void setIsGif(int isGif) {
        this.isGif = isGif;
    }

    public int getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
    }

    public int getLabelType() {
        return labelType;
    }

    public void setLabelType(int labelType) {
        this.labelType = labelType;
    }

    public LabelInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.labelId);
        dest.writeString(this.labelName);
        dest.writeLong(this.upperLabelId);
        dest.writeInt(this.isSelect);
        dest.writeString(this.labelDesc);
        dest.writeString(this.content);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
        dest.writeInt(this.secondLabelCount);
        dest.writeInt(this.limitMode);
        dest.writeInt(this.selectNum);
        dest.writeInt(this.isImg);
        dest.writeInt(this.isGif);
        dest.writeInt(this.isVideo);
        dest.writeInt(this.labelType);
    }

    protected LabelInfo(Parcel in) {
        this.labelId = in.readLong();
        this.labelName = in.readString();
        this.upperLabelId = in.readLong();
        this.isSelect = in.readInt();
        this.labelDesc = in.readString();
        this.content = in.readString();
        this.isCheck = in.readByte() != 0;
        this.secondLabelCount = in.readInt();
        this.limitMode = in.readInt();
        this.selectNum = in.readInt();
        this.isImg = in.readInt();
        this.isGif = in.readInt();
        this.isVideo = in.readInt();
        this.labelType = in.readInt();
    }

    public static final Creator<LabelInfo> CREATOR = new Creator<LabelInfo>() {
        @Override
        public LabelInfo createFromParcel(Parcel source) {
            return new LabelInfo(source);
        }

        @Override
        public LabelInfo[] newArray(int size) {
            return new LabelInfo[size];
        }
    };
}
