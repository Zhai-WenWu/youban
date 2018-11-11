package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.CountInfo;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetHomeNewCountResponse extends BaseResponse {
    private int nearCount;//附近更新数量,
    private int attentionCount;//关注更新数量,
    private int discoverCount;//关注更新数量,

    public int getDiscoverCount() {
        return discoverCount;
    }

    public void setDiscoverCount(int discoverCount) {
        this.discoverCount = discoverCount;
    }

    public int getNearCount() {
        return nearCount;
    }

    public void setNearCount(int nearCount) {
        this.nearCount = nearCount;
    }

    public int getAttentionCount() {
        return attentionCount;
    }

    public void setAttentionCount(int attentionCount) {
        this.attentionCount = attentionCount;
    }
}
