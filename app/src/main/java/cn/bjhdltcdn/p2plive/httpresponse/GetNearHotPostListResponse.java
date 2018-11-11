package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.PostInfo;

/**
 * Created by Hu_PC on 2017/11/15.
 */

public class GetNearHotPostListResponse extends BaseResponse{
    private List<PostInfo> list;//[{PostInfo},{PostInfo}...]附近热帖列表,
    private String blankHint;//空白提示语(你附近的人还没发布过内容),
    private int total;


    public List<PostInfo> getList() {
        return list;
    }

    public void setList(List<PostInfo> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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
