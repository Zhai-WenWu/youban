package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Hu_PC on 2018/6/15.
 */

public class ShopComment implements Parcelable {
    private long commentId;//评价Id,
    private long parentId;//父ID(订单/评论/回复ID),
    private long commentParentId;//楼中楼的父评论id, parentId  传你回复的上一条评论的主键Id；  commentParentId   传第一条评论的主键Id
    private long storeId;//店铺Id,
    private long productId;//商品Id,
    private ProductInfo productInfo;//{ProductInfo对象}商品信息,
    private String content;//评论/回复内容",
    private double evalScore;//评价分数,
    private String addTime;//评价时间,
    private int type;//内容类型(1--->评论,2--->回复),
    private BaseUser toBaseUser;//{BaseUser}被回复人,
    private BaseUser fromBaseUser;//{BaseUser}回复人,

    public ShopComment() {
    }

    private int status;//状态(0--->未删除，1--->删除),
    private int praiseCount;//点赞总数,
    private String praiseCountStr;//转译点赞总数(字符串),
    private int commentType;//评论/回复类型(0-->普通文本,1-->图/图文,2--->视频),
    private String videoUrl;//评论/回复视频路径(类型为视频使用),
    private String videoImageUrl;//评论/回复图片地址(类型为视频使用),
    private List<Image> imageList;//[{Image对象},{Image对象}...]评论回复相册,
    private List<ShopComment> replyList;//[{ShopComment},{ShopComment}...]回复列表,
    private int replyCount;//回复总条数,
    private String replyCountStr;//转译回复总条数(字符串),
    private int isPraise;//当前用户是否点赞该评论回复(0未点赞 1 点赞),

    public ShopComment(long commentId, long parentId, long commentParentId, long storeId, long productId, ProductInfo productInfo, String content, double evalScore, String addTime, int type, BaseUser toBaseUser, BaseUser fromBaseUser, int status, int praiseCount, String praiseCountStr, int commentType, String videoUrl, String videoImageUrl, List<Image> imageList, List<ShopComment> replyList, int replyCount, String replyCountStr, int isPraise) {
        this.commentId = commentId;
        this.parentId = parentId;
        this.commentParentId = commentParentId;
        this.storeId = storeId;
        this.productId = productId;
        this.productInfo = productInfo;
        this.content = content;
        this.evalScore = evalScore;
        this.addTime = addTime;
        this.type = type;
        this.toBaseUser = toBaseUser;
        this.fromBaseUser = fromBaseUser;
        this.status = status;
        this.praiseCount = praiseCount;
        this.praiseCountStr = praiseCountStr;
        this.commentType = commentType;
        this.videoUrl = videoUrl;
        this.videoImageUrl = videoImageUrl;
        this.imageList = imageList;
        this.replyList = replyList;
        this.replyCount = replyCount;
        this.replyCountStr = replyCountStr;
        this.isPraise = isPraise;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getCommentParentId() {
        return commentParentId;
    }

    public void setCommentParentId(long commentParentId) {
        this.commentParentId = commentParentId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getEvalScore() {
        return evalScore;
    }

    public void setEvalScore(double evalScore) {
        this.evalScore = evalScore;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BaseUser getToBaseUser() {
        return toBaseUser;
    }

    public void setToBaseUser(BaseUser toBaseUser) {
        this.toBaseUser = toBaseUser;
    }

    public BaseUser getFromBaseUser() {
        return fromBaseUser;
    }

    public void setFromBaseUser(BaseUser fromBaseUser) {
        this.fromBaseUser = fromBaseUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getPraiseCountStr() {
        return praiseCountStr;
    }

    public void setPraiseCountStr(String praiseCountStr) {
        this.praiseCountStr = praiseCountStr;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
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

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public List<ShopComment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ShopComment> replyList) {
        this.replyList = replyList;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getReplyCountStr() {
        return replyCountStr;
    }

    public void setReplyCountStr(String replyCountStr) {
        this.replyCountStr = replyCountStr;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.commentId);
        dest.writeLong(this.parentId);
        dest.writeLong(this.commentParentId);
        dest.writeLong(this.storeId);
        dest.writeLong(this.productId);
        dest.writeParcelable(this.productInfo, flags);
        dest.writeString(this.content);
        dest.writeDouble(this.evalScore);
        dest.writeString(this.addTime);
        dest.writeInt(this.type);
        dest.writeParcelable(this.toBaseUser, flags);
        dest.writeParcelable(this.fromBaseUser, flags);
        dest.writeInt(this.status);
        dest.writeInt(this.praiseCount);
        dest.writeString(this.praiseCountStr);
        dest.writeInt(this.commentType);
        dest.writeString(this.videoUrl);
        dest.writeString(this.videoImageUrl);
        dest.writeTypedList(this.imageList);
        dest.writeTypedList(this.replyList);
        dest.writeInt(this.replyCount);
        dest.writeString(this.replyCountStr);
        dest.writeInt(this.isPraise);
    }

    protected ShopComment(Parcel in) {
        this.commentId = in.readLong();
        this.parentId = in.readLong();
        this.commentParentId = in.readLong();
        this.storeId = in.readLong();
        this.productId = in.readLong();
        this.productInfo = in.readParcelable(ProductInfo.class.getClassLoader());
        this.content = in.readString();
        this.evalScore = in.readDouble();
        this.addTime = in.readString();
        this.type = in.readInt();
        this.toBaseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.fromBaseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.status = in.readInt();
        this.praiseCount = in.readInt();
        this.praiseCountStr = in.readString();
        this.commentType = in.readInt();
        this.videoUrl = in.readString();
        this.videoImageUrl = in.readString();
        this.imageList = in.createTypedArrayList(Image.CREATOR);
        this.replyList = in.createTypedArrayList(ShopComment.CREATOR);
        this.replyCount = in.readInt();
        this.replyCountStr = in.readString();
        this.isPraise = in.readInt();
    }

    public static final Creator<ShopComment> CREATOR = new Creator<ShopComment>() {
        @Override
        public ShopComment createFromParcel(Parcel source) {
            return new ShopComment(source);
        }

        @Override
        public ShopComment[] newArray(int size) {
            return new ShopComment[size];
        }
    };
}
