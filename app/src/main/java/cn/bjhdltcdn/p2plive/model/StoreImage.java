package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2018/5/11.
 */

public class StoreImage implements Parcelable {
    private long imageId;//图片id,
    private long parentId;//所属的id,
    private int status;//图片状态(删除状态，0--->未删除，1--->删除，服务器使用),
    private String photographTime;//拍照时间,
    private int imageType;//图片类型(1图片,2视频),
    private String thumbnailUrl;//缩略图,
    private String imageUrl;//原图,
    private String videoUrl;//视频路径,
    private String videoImageUrl;//视频图片

    public StoreImage() {
    }

    public StoreImage(long imageId, long parentId, int status, String photographTime, int imageType, String thumbnailUrl, String imageUrl, String videoUrl, String videoImageUrl) {
        this.imageId = imageId;
        this.parentId = parentId;
        this.status = status;
        this.photographTime = photographTime;
        this.imageType = imageType;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.videoImageUrl = videoImageUrl;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhotographTime() {
        return photographTime;
    }

    public void setPhotographTime(String photographTime) {
        this.photographTime = photographTime;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        this.videoImageUrl = videoImageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.imageId);
        dest.writeLong(this.parentId);
        dest.writeInt(this.status);
        dest.writeString(this.photographTime);
        dest.writeInt(this.imageType);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.imageUrl);
        dest.writeString(this.videoUrl);
        dest.writeString(this.videoImageUrl);
    }

    protected StoreImage(Parcel in) {
        this.imageId = in.readLong();
        this.parentId = in.readLong();
        this.status = in.readInt();
        this.photographTime = in.readString();
        this.imageType = in.readInt();
        this.thumbnailUrl = in.readString();
        this.imageUrl = in.readString();
        this.videoUrl = in.readString();
        this.videoImageUrl = in.readString();
    }

    public static final Creator<StoreImage> CREATOR = new Creator<StoreImage>() {
        @Override
        public StoreImage createFromParcel(Parcel source) {
            return new StoreImage(source);
        }

        @Override
        public StoreImage[] newArray(int size) {
            return new StoreImage[size];
        }
    };
}
