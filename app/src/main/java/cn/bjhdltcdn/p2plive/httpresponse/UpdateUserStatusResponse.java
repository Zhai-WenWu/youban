package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.RoomInfo;

/**
 * Created by ZHUDI on 2017/11/17.
 */

public class UpdateUserStatusResponse extends BaseResponse{
    private RoomInfo roomInfo;//{RoomInfo对象},

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }
}
