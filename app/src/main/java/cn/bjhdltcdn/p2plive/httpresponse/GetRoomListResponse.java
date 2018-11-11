package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.RoomInfo;

/**
 * Created by ZHUDI on 2017/11/17.
 */

public class GetRoomListResponse extends BaseResponse {
    private List<RoomInfo> roomList;//[{RoomInfo对象},{RoomInfo对象},...],
    private int total;//总数,

    public List<RoomInfo> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomInfo> roomList) {
        this.roomList = roomList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
