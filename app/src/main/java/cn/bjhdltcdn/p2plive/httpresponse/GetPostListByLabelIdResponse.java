package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.ArrayList;

import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetPostListByLabelIdResponse extends BaseResponse {

    private ArrayList<PostInfo> list;
    private int total;

    public ArrayList<PostInfo> getList() {
        return list;
    }

    public void setList(ArrayList<PostInfo> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
