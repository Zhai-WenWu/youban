package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;

/**
 * Created by Hu_PC on 2017/11/13.
 */

public class GetOfflineActiveListResponse  extends BaseResponse{
    private List<ActivityInfo> list;//[{ActivityInfo},{ActivityInfo}...]线下活动列表,
    private int total;//总数,
    private String defaultImg;//默认图片
    private String blankHint;//空白提示语(你附近没有活动，你可以第一个发起活动哦),

    public List<ActivityInfo> getList() {
        return list;
    }

    public void setList(List<ActivityInfo> list) {
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

    @Override
    public String getBlankHint() {
        return blankHint;
    }

    @Override
    public void setBlankHint(String blankHint) {
        this.blankHint = blankHint;
    }
}
