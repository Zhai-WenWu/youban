package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.RoomInfo;

/**
 * Created by ZHUDI on 2018/1/26.
 */

public class FindRoomDetailResponse extends BaseResponse {
    private RoomInfo roomInfo;

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }
}
