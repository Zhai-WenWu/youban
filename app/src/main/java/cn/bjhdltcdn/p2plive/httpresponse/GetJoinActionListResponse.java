package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ActivityInfo;

/**
 * Created by Hu_PC on 2017/11/13.
 */

public class GetJoinActionListResponse extends BaseResponse {
    private List<ActivityInfo> list;//[{ActivityInfo},{ActivityInfo}...]线下活动列表,
    private int total;//总数,
    private String defaultImg;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ActivityInfo> getList() {

        return list;
    }

    public void setList(List<ActivityInfo> list) {
        this.list = list;
    }

    public String getDefaultImg() {
        return defaultImg;
    }
}
