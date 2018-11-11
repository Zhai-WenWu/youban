package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


/**
 * 解析视频信令的bean类
 */
public class VideoCMDTextMessageModel implements Parcelable {

    private String extra = "";
    // 信令发送的时间
    private String addTime;
    private BaseUser baseUser;
    //房间号
    private String roomNumber;
    //文本消息
    private String messageTips;
    //类型
    private int messageType;
    //分类
    private int type;
    private BaseUser toUserInfo;
    private Props props;
//    // 游戏道具
//    private long propsId;
//    private List<String> list;
//    private int propsNum;//礼物数量、
//
//    // 卡图片
//    private String propsUrl;
//    // 礼物名称
//    private String propsName;
//    // 房间id
//    private String roomId;
//    // 房间昵称
//    private String roomName;
//    // 礼物金币数
//    private long goldNum;


    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getMessageTips() {
        return messageTips;
    }

    public void setMessageTips(String messageTips) {
        this.messageTips = messageTips;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BaseUser getToUserInfo() {
        return toUserInfo;
    }

    public void setToUserInfo(BaseUser toUserInfo) {
        this.toUserInfo = toUserInfo;
    }

    public Props getProps() {
        return props;
    }

    public void setProps(Props props) {
        this.props = props;
    }

    public VideoCMDTextMessageModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.extra);
        dest.writeString(this.addTime);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeString(this.roomNumber);
        dest.writeString(this.messageTips);
        dest.writeInt(this.messageType);
        dest.writeInt(this.type);
        dest.writeParcelable(this.toUserInfo, flags);
        dest.writeParcelable(this.props, flags);
    }

    protected VideoCMDTextMessageModel(Parcel in) {
        this.extra = in.readString();
        this.addTime = in.readString();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.roomNumber = in.readString();
        this.messageTips = in.readString();
        this.messageType = in.readInt();
        this.type = in.readInt();
        this.toUserInfo = in.readParcelable(BaseUser.class.getClassLoader());
        this.props = in.readParcelable(Props.class.getClassLoader());
    }

    public static final Creator<VideoCMDTextMessageModel> CREATOR = new Creator<VideoCMDTextMessageModel>() {
        @Override
        public VideoCMDTextMessageModel createFromParcel(Parcel source) {
            return new VideoCMDTextMessageModel(source);
        }

        @Override
        public VideoCMDTextMessageModel[] newArray(int size) {
            return new VideoCMDTextMessageModel[size];
        }
    };
}
