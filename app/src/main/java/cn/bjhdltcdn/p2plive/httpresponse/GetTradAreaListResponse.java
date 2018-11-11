package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.StoreInfo;

/**
 * Created by xiawenquan on 18/5/28.
 */

public class GetTradAreaListResponse extends BaseResponse {

    //试用商品列表
    private List<ProductInfo> trailList;
    //闪购商品列表
    private List<ProductInfo> flashList;
    //店铺列表
    private List<StoreInfo> storeList;

    //试用跳转H5链接
    private String trailUrl;
    //闪购跳转H5链接
    private String flashUrl;

    //试用跳转H5详情
    private String trailDetailUrl;
    //闪购跳转H5详情
    private String flashDetailUrl;

    public List<ProductInfo> getTrailList() {
        return trailList;
    }

    public void setTrailList(List<ProductInfo> trailList) {
        this.trailList = trailList;
    }

    public List<ProductInfo> getFlashList() {
        return flashList;
    }

    public void setFlashList(List<ProductInfo> flashList) {
        this.flashList = flashList;
    }

    public List<StoreInfo> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreInfo> storeList) {
        this.storeList = storeList;
    }

    public String getTrailUrl() {
        return trailUrl;
    }

    public void setTrailUrl(String trailUrl) {
        this.trailUrl = trailUrl;
    }

    public String getFlashUrl() {
        return flashUrl;
    }

    public void setFlashUrl(String flashUrl) {
        this.flashUrl = flashUrl;
    }

    public String getTrailDetailUrl() {
        return trailDetailUrl;
    }

    public void setTrailDetailUrl(String trailDetailUrl) {
        this.trailDetailUrl = trailDetailUrl;
    }

    public String getFlashDetailUrl() {
        return flashDetailUrl;
    }

    public void setFlashDetailUrl(String flashDetailUrl) {
        this.flashDetailUrl = flashDetailUrl;
    }
}
