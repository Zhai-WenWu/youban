package cn.bjhdltcdn.p2plive.httpresponse;


import java.util.List;

import cn.bjhdltcdn.p2plive.model.RoomBaseUser;

/**
 * Created by ZHUDI on 2017/1/4.
 */

public class GetOnlineUserListResponse extends BaseResponse {
    private List<RoomBaseUser> roomUserList;//[{BaseUser对象},{BaseUser对象},...]
    private int total;//房间在线总人数

    public List<RoomBaseUser> getRoomUserList() {
        return roomUserList;
    }

    public void setRoomUserList(List<RoomBaseUser> roomUserList) {
        this.roomUserList = roomUserList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
