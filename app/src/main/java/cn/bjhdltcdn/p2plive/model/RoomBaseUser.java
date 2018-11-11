package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;

/**
 * Created by ZHUDI on 2017/12/16.
 */

public class RoomBaseUser extends BaseUser {
    private int userRole;//所属角色(1--->主持,2--->场控(管理员),3--->观众,4--->群主),
    private int wheat;//麦状态(2--->上麦,3--->下麦) 多人视频处使用,
    private int wheatId;//麦序,
    private int isGag;//视频用户是否被禁言(1否，2是),
    private int transfer;//主持移交状态(1 发送移交申请 2对方同意 3对方拒绝 11接收请求的标识),
    private int type;//用户视频摄像头控制(1开,2关-语音),

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getWheat() {
        return wheat;
    }

    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public int getWheatId() {
        return wheatId;
    }

    public void setWheatId(int wheatId) {
        this.wheatId = wheatId;
    }

    public int getIsGag() {
        return isGag;
    }

    public void setIsGag(int isGag) {
        this.isGag = isGag;
    }

    public int getTransfer() {
        return transfer;
    }

    public void setTransfer(int transfer) {
        this.transfer = transfer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public RoomBaseUser() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.userRole);
        dest.writeInt(this.wheat);
        dest.writeInt(this.wheatId);
        dest.writeInt(this.isGag);
        dest.writeInt(this.transfer);
        dest.writeInt(this.type);
    }

    protected RoomBaseUser(Parcel in) {
        super(in);
        this.userRole = in.readInt();
        this.wheat = in.readInt();
        this.wheatId = in.readInt();
        this.isGag = in.readInt();
        this.transfer = in.readInt();
        this.type = in.readInt();
    }

    public static final Creator<RoomBaseUser> CREATOR = new Creator<RoomBaseUser>() {
        @Override
        public RoomBaseUser createFromParcel(Parcel source) {
            return new RoomBaseUser(source);
        }

        @Override
        public RoomBaseUser[] newArray(int size) {
            return new RoomBaseUser[size];
        }
    };
}
