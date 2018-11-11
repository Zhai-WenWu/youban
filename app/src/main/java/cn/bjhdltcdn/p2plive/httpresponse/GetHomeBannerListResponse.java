package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.RecommendInfo;

/**
 * Created by Hu_PC on 2017/11/16.
 */

public class GetHomeBannerListResponse extends BaseResponse {
    private List<RecommendInfo> list;//[{RecommendInfo},{RecommendInfo},...],
    /**
     * 当type为5时，官方logo图
     */
    private String logoUrl;

    public List<RecommendInfo> getList() {
        return list;
    }

    public void setList(List<RecommendInfo> list) {
        this.list = list;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
