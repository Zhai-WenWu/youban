package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.HomeInfo;

/**
 * Created by Hu_PC on 2017/11/10.
 */

public class GetAttentionListResponse extends BaseResponse{
    private List<HomeInfo> list;//[{HomeInfo},{HomeInfo}...]首页关注,
    private int total;//总数,
    private String defaultImg;//活动默认图片url,
    private String blankHint;//空白提示语(你关注的人还没有任何动向),

    public List<HomeInfo> getList() {
        return list;
    }

    public void setList(List<HomeInfo> list) {
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
