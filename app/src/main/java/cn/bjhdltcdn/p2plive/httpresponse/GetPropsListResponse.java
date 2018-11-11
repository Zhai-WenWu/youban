package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.Props;

/**
 * Created by ZHUDI on 2017/12/25.
 */

public class GetPropsListResponse extends BaseResponse {
    private List<Props> list;//[{Props对象},{Props对象}]一对一视频,
    private List<Props> roomPropsList;//[{Props对象},{Props对象}]多人房间,

    public List<Props> getList() {
        return list;
    }

    public void setList(List<Props> list) {
        this.list = list;
    }

    public List<Props> getRoomPropsList() {
        return roomPropsList;
    }

    public void setRoomPropsList(List<Props> roomPropsList) {
        this.roomPropsList = roomPropsList;
    }
}
