package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.ArrayList;

import cn.bjhdltcdn.p2plive.model.HomeInfo;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetMyShareListResponse extends BaseResponse {
    /*"list":[{HomeInfo对象},{HomeInfo对象}...]我的分享列表,
    "total":总条数,
    "defaultImg"：活动默认图片url,*/
    private ArrayList<HomeInfo> list;
    private int total;
    private String defaultImg;

    public ArrayList<HomeInfo> getList() {
        return list;
    }

    public void setList(ArrayList<HomeInfo> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(String defaultImg) {
        this.defaultImg = defaultImg;
    }
}
