package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.BaseUser;

/**
 * Created by xiawenquan on 16/7/21.
 */
public class VideoEvent {
    private int type ;
    private String roomNumber;
    private BaseUser baseUser ;

    public VideoEvent(int type, String roomNumber, BaseUser baseUser) {
        this.type = type;
        this.roomNumber = roomNumber;
        this.baseUser = baseUser;
    }

    /**
     * 只用于关闭当前页面
     * @param type
     */
    public VideoEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

}
