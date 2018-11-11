package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2017/11/10.
 *  图片对象
 */

public class Image implements Parcelable {
    private long imageId;//图片id,
    private long parentId;//所属的id,
    private int state;//图片状态(删除状态，0--->未删除，1--->删除，服务器使用),
    private String photographTime="";//拍照时间,
    private String thumbnailUrl="";//缩略图,
    private String imageUrl="";//原图,
    private int type;//实名认证照片(1/正面照片,2/反面照片,3/手持身份证照片),
    private int yesOrNoUpload;//1:要上传到服务器的0：不要上传到服务器的
    private int orderNum;//顺序
    private int total;//总数


    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPhotographTime() {
        return photographTime;
    }

    public void setPhotographTime(String photographTime) {
        this.photographTime = photographTime;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getYesOrNoUpload() {
        return yesOrNoUpload;
    }

    public void setYesOrNoUpload(int yesOrNoUpload) {
        this.yesOrNoUpload = yesOrNoUpload;
    }


    public Image() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.imageId);
        dest.writeLong(this.parentId);
        dest.writeInt(this.state);
        dest.writeString(this.photographTime);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.type);
        dest.writeInt(this.yesOrNoUpload);
    }

    protected Image(Parcel in) {
        this.imageId = in.readLong();
        this.parentId = in.readLong();
        this.state = in.readInt();
        this.photographTime = in.readString();
        this.thumbnailUrl = in.readString();
        this.imageUrl = in.readString();
        this.type = in.readInt();
        this.yesOrNoUpload = in.readInt();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
