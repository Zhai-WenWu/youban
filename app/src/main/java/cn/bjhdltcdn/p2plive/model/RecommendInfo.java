package cn.bjhdltcdn.p2plive.model;

/**
 * Created by Hu_PC on 2017/11/16.
 */

public class RecommendInfo {
    private Long recommendId;//推荐对象ID
    private String title;//标题,
    private int recommendType;//推荐类型(1 banner),
    private String imgUrl;//图片路径,
    private String gotoUrl;//跳转链接

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(int recommendType) {
        this.recommendType = recommendType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGotoUrl() {
        return gotoUrl;
    }

    public void setGotoUrl(String gotoUrl) {
        this.gotoUrl = gotoUrl;
    }
}
