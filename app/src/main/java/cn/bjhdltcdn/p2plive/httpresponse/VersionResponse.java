package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by ZHAI on 2017/12/18.
 */

public class VersionResponse extends BaseResponse {
    /* "version":最新版本号,
     "isUpgrade":是否升级 0 不升级 1 强制升级 2 普通升级,3 当前版本补丁
     "linkUrl":链接地址,*/
    private String version;
    private int isUpgrade;
    private String linkUrl;//链接地址,
    private String shareUrl;//普通分享地址,
    private String wxShareUrl;//微信分享地址,
    private String qqShareUrl;//qq分享地址,

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getIsUpgrade() {
        return isUpgrade;
    }

    public void setIsUpgrade(int isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getWxShareUrl() {
        return wxShareUrl;
    }

    public void setWxShareUrl(String wxShareUrl) {
        this.wxShareUrl = wxShareUrl;
    }

    public String getQqShareUrl() {
        return qqShareUrl;
    }

    public void setQqShareUrl(String qqShareUrl) {
        this.qqShareUrl = qqShareUrl;
    }
}
