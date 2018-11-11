package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import io.rong.imlib.model.ChatRoomInfo;

/**
 * Created by Hu_PC on 2017/11/9.
 * 圈子
 */

public class OrganizationInfo implements Parcelable {


    private long organId;//圈子Id,
    private long userId;//圈子创建用户Id
    private String organImg;//圈子封面,
    private String organName;//圈子名称,
    private String hobbyName;// 圈子类别名称
    private String secondHobbyName;// 圈子二级类别名称(兴趣爱好)
    private int sexLimit;//性别限制(1-->不限,2-->仅男生,3-->仅女生),
    private int joinLimit;//入圈验证(1-->直接加入,2-->申请同意后可加入),
    private int contentLimit;//圈子内容限制(1-->全部可见,2-->仅圈友可见),
    private int anonymousLimit;//匿名限制(1-->允许匿名,2-->不允许匿名),
    private String description;//圈子简介,
    private int type;//圈子类型(0-->普通圈子，1-->官方圈子 2 - 学校),
    private String updateTime;//更新时间
    private String addTime;//创建时间,
    private int memberCount;//成员数量,
    private int postCount;//帖子数量,
    private int userRole;//所属角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
    private String activeLevel;//用户在圈子的活跃等级,
    private Group group;//{Group}圈子官方群(空表示没有,反之有),
    private int myUserRole;//我在圈子所属角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0，只用于用户查看关注列表，返回自己在圈子的角色
    private BaseUser baseUser;//{BaseUser}用户基本信息对象,
    private int isSelect;//是否选择(0未选,1已选),
    private int isPublic;//是否公开圈子(默认为0--->公开,1--->隐藏),
    private int totalMen;//圈子男成员数量
    private int totalWomen;//圈子女成员数量
    private int isCreateChatInfo;
    private String matchUrl;//是否报名图片路径
    private long chatId;
    private ChatInfo chatInfo;
    private boolean atFirst;

    public boolean getAtFirst() {
        return atFirst;
    }

    public void setAtFirst(boolean atFirst) {
        this.atFirst = atFirst;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public ChatInfo getChatInfo() {
        return chatInfo;
    }

    public void setChatInfo(ChatInfo chatInfo) {
        this.chatInfo = chatInfo;
    }

    public int getIsCreateChatInfo() {
        return isCreateChatInfo;
    }

    public void setIsCreateChatInfo(int isCreateChatInfo) {
        this.isCreateChatInfo = isCreateChatInfo;
    }

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
    }

    public String getOrganImg() {
        return organImg;
    }

    public void setOrganImg(String organImg) {
        this.organImg = organImg;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public int getSexLimit() {
        return sexLimit;
    }

    public void setSexLimit(int sexLimit) {
        this.sexLimit = sexLimit;
    }

    public int getJoinLimit() {
        return joinLimit;
    }

    public void setJoinLimit(int joinLimit) {
        this.joinLimit = joinLimit;
    }

    public int getContentLimit() {
        return contentLimit;
    }

    public void setContentLimit(int contentLimit) {
        this.contentLimit = contentLimit;
    }

    public int getAnonymousLimit() {
        return anonymousLimit;
    }

    public void setAnonymousLimit(int anonymousLimit) {
        this.anonymousLimit = anonymousLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public String getActiveLevel() {
        return activeLevel;
    }

    public void setActiveLevel(String activeLevel) {
        this.activeLevel = activeLevel;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getHobbyName() {
        return hobbyName;
    }

    public void setHobbyName(String hobbyName) {
        this.hobbyName = hobbyName;
    }

    public String getSecondHobbyName() {
        return secondHobbyName;
    }

    public void setSecondHobbyName(String secondHobbyName) {
        this.secondHobbyName = secondHobbyName;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getMyUserRole() {
        return myUserRole;
    }

    public void setMyUserRole(int myUserRole) {
        this.myUserRole = myUserRole;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getTotalMen() {
        return totalMen;
    }

    public void setTotalMen(int totalMen) {
        this.totalMen = totalMen;
    }

    public int getTotalWomen() {
        return totalWomen;
    }

    public void setTotalWomen(int totalWomen) {
        this.totalWomen = totalWomen;
    }

    public String getMatchUrl() {
        return matchUrl;
    }

    public void setMatchUrl(String matchUrl) {
        this.matchUrl = matchUrl;
    }

    public OrganizationInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.organId);
        dest.writeLong(this.userId);
        dest.writeString(this.organImg);
        dest.writeString(this.organName);
        dest.writeString(this.hobbyName);
        dest.writeString(this.secondHobbyName);
        dest.writeInt(this.sexLimit);
        dest.writeInt(this.joinLimit);
        dest.writeInt(this.contentLimit);
        dest.writeInt(this.anonymousLimit);
        dest.writeString(this.description);
        dest.writeInt(this.type);
        dest.writeString(this.updateTime);
        dest.writeString(this.addTime);
        dest.writeInt(this.memberCount);
        dest.writeInt(this.postCount);
        dest.writeInt(this.userRole);
        dest.writeString(this.activeLevel);
        dest.writeParcelable(this.group, flags);
        dest.writeInt(this.myUserRole);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeInt(this.isSelect);
        dest.writeInt(this.isPublic);
        dest.writeInt(this.totalMen);
        dest.writeInt(this.totalWomen);
        dest.writeInt(this.isCreateChatInfo);
        dest.writeString(this.matchUrl);
        dest.writeLong(this.chatId);
        dest.writeParcelable(this.chatInfo, flags);
        dest.writeByte(this.atFirst ? (byte) 1 : (byte) 0);
    }

    protected OrganizationInfo(Parcel in) {
        this.organId = in.readLong();
        this.userId = in.readLong();
        this.organImg = in.readString();
        this.organName = in.readString();
        this.hobbyName = in.readString();
        this.secondHobbyName = in.readString();
        this.sexLimit = in.readInt();
        this.joinLimit = in.readInt();
        this.contentLimit = in.readInt();
        this.anonymousLimit = in.readInt();
        this.description = in.readString();
        this.type = in.readInt();
        this.updateTime = in.readString();
        this.addTime = in.readString();
        this.memberCount = in.readInt();
        this.postCount = in.readInt();
        this.userRole = in.readInt();
        this.activeLevel = in.readString();
        this.group = in.readParcelable(Group.class.getClassLoader());
        this.myUserRole = in.readInt();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.isSelect = in.readInt();
        this.isPublic = in.readInt();
        this.totalMen = in.readInt();
        this.totalWomen = in.readInt();
        this.isCreateChatInfo = in.readInt();
        this.matchUrl = in.readString();
        this.chatId = in.readLong();
        this.chatInfo = in.readParcelable(ChatInfo.class.getClassLoader());
        this.atFirst = in.readByte() != 0;
    }

    public static final Creator<OrganizationInfo> CREATOR = new Creator<OrganizationInfo>() {
        @Override
        public OrganizationInfo createFromParcel(Parcel source) {
            return new OrganizationInfo(source);
        }

        @Override
        public OrganizationInfo[] newArray(int size) {
            return new OrganizationInfo[size];
        }
    };

    public boolean isAtFirst() {
        return atFirst;
    }
}
