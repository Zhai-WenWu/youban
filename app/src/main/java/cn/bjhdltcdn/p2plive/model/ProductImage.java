package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2018/5/11.
 */

public class ProductImage implements Parcelable {
    private long imageId;//图片id,
    private long productId;//商品ID,
    private int status;//图片状态(删除状态，0--->未删除，1--->删除，服务器使用),
    private String thumbnailUrl;//缩略图,
    private String imageUrl;//原图,

    public ProductImage() {
    }

    public ProductImage(long imageId, long productId, int status, String thumbnailUrl, String imageUrl) {
        this.imageId = imageId;
        this.productId = productId;
        this.status = status;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.imageId);
        dest.writeLong(this.productId);
        dest.writeInt(this.status);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.imageUrl);
    }

    protected ProductImage(Parcel in) {
        this.imageId = in.readLong();
        this.productId = in.readLong();
        this.status = in.readInt();
        this.thumbnailUrl = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<ProductImage> CREATOR = new Parcelable.Creator<ProductImage>() {
        @Override
        public ProductImage createFromParcel(Parcel source) {
            return new ProductImage(source);
        }

        @Override
        public ProductImage[] newArray(int size) {
            return new ProductImage[size];
        }
    };
}
