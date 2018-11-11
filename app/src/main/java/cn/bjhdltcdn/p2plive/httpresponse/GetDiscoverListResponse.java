package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ChatInfo;
import cn.bjhdltcdn.p2plive.model.LaunchPlay;
import cn.bjhdltcdn.p2plive.model.RoomInfo;

/**
 * Created by ZHUDI on 2017/11/17.
 */

public class GetDiscoverListResponse extends BaseResponse {
    private List<RoomInfo> roomList;//[{RoomInfo},{RoomInfo}...]房间列表,
    private List<ChatInfo> chatRoomList;//[{ChatInfo},{ChatInfo}...]匿名聊天室列表,
    private List<LaunchPlay> pkList;//[{LaunchPlay对象},{LaunchPlay对象}...]全民活动/PK挑战列表,
    private int total;//总数,

    public List<RoomInfo> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomInfo> roomList) {
        this.roomList = roomList;
    }

    public List<LaunchPlay> getPkList() {
        return pkList;
    }

    public void setPkList(List<LaunchPlay> pkList) {
        this.pkList = pkList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ChatInfo> getChatRoomList() {
        return chatRoomList;
    }

    public void setChatRoomList(List<ChatInfo> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }
}
