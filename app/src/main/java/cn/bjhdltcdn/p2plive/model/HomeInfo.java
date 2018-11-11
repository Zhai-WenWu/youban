package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2017/11/10.
 * 首页关注对象
 */

public class HomeInfo implements Parcelable {
    private BaseUser baseUser;//{BaseUser}基本信息对象,
    private int type;//类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请,8表白,9同学帮帮忙,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白)
    private ShareInfo shareInfo;//{ShareInfo}分享对象(为分享时取值),
    private PostInfo postInfo;//{PostInfo}帖子对象,
    private OrganizationInfo organizationInfo;//{OrganizationInfo}圈子对象,
    private ActivityInfo activityInfo;//{ActivityInfo}活动对象,
    private RoomInfo roomInfo;//{RoomInfo}房间对象,
    private PlayInfo playInfo;//{PlayInfo}PK挑战对象,
    private OrganApply organApply;//{OrganApply}圈子申请对象,
    private GroupApply groupApply;//{GroupApply}群组申请对象,
    private SayLoveInfo sayLoveInfo;//{SayLoveInfo}表白对象,
    private HelpInfo helpInfo;//{HelpInfo}帮帮忙对象,

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    /**
     * 类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请,8表白,9同学帮帮忙,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白)
     * @return
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PostInfo getPostInfo() {
        return postInfo;
    }

    public void setPostInfo(PostInfo postInfo) {
        this.postInfo = postInfo;
    }

    public OrganizationInfo getOrganizationInfo() {
        return organizationInfo;
    }

    public void setOrganizationInfo(OrganizationInfo organizationInfo) {
        this.organizationInfo = organizationInfo;
    }

    public ActivityInfo getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(ActivityInfo activityInfo) {
        this.activityInfo = activityInfo;
    }


    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    public PlayInfo getPlayInfo() {
        return playInfo;
    }

    public void setPlayInfo(PlayInfo playInfo) {
        this.playInfo = playInfo;
    }

    public OrganApply getOrganApply() {
        return organApply;
    }

    public void setOrganApply(OrganApply organApply) {
        this.organApply = organApply;
    }

    public GroupApply getGroupApply() {
        return groupApply;
    }

    public void setGroupApply(GroupApply groupApply) {
        this.groupApply = groupApply;
    }

    public ShareInfo getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(ShareInfo shareInfo) {
        this.shareInfo = shareInfo;
    }

    public SayLoveInfo getSayLoveInfo() {
        return sayLoveInfo;
    }

    public void setSayLoveInfo(SayLoveInfo sayLoveInfo) {
        this.sayLoveInfo = sayLoveInfo;
    }

    public HelpInfo getHelpInfo() {
        return helpInfo;
    }

    public void setHelpInfo(HelpInfo helpInfo) {
        this.helpInfo = helpInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseUser, flags);
        dest.writeInt(this.type);
        dest.writeParcelable(this.shareInfo, flags);
        dest.writeParcelable(this.postInfo, flags);
        dest.writeParcelable(this.organizationInfo, flags);
        dest.writeParcelable(this.activityInfo, flags);
        dest.writeParcelable(this.roomInfo, flags);
        dest.writeParcelable(this.playInfo, flags);
        dest.writeParcelable(this.organApply, flags);
        dest.writeParcelable(this.groupApply, flags);
        dest.writeParcelable(this.sayLoveInfo, flags);
        dest.writeParcelable(this.helpInfo, flags);
    }

    public HomeInfo() {
    }

    protected HomeInfo(Parcel in) {
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.type = in.readInt();
        this.shareInfo = in.readParcelable(ShareInfo.class.getClassLoader());
        this.postInfo = in.readParcelable(PostInfo.class.getClassLoader());
        this.organizationInfo = in.readParcelable(OrganizationInfo.class.getClassLoader());
        this.activityInfo = in.readParcelable(ActivityInfo.class.getClassLoader());
        this.roomInfo = in.readParcelable(RoomInfo.class.getClassLoader());
        this.playInfo = in.readParcelable(PlayInfo.class.getClassLoader());
        this.organApply = in.readParcelable(OrganApply.class.getClassLoader());
        this.groupApply = in.readParcelable(GroupApply.class.getClassLoader());
        this.sayLoveInfo = in.readParcelable(SayLoveInfo.class.getClassLoader());
        this.helpInfo = in.readParcelable(HelpInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<HomeInfo> CREATOR = new Parcelable.Creator<HomeInfo>() {
        @Override
        public HomeInfo createFromParcel(Parcel source) {
            return new HomeInfo(source);
        }

        @Override
        public HomeInfo[] newArray(int size) {
            return new HomeInfo[size];
        }
    };
}
