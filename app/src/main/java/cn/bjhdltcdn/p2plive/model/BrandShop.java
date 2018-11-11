package cn.bjhdltcdn.p2plive.model;

/**
 * Description:品牌商店铺列表对象信息
 * Data: 2018/9/18
 *
 * @author: zhudi
 */
public class BrandShop {
    /**
     * 品牌商店铺id
     */
    private long brandShopId;
    /**
     * 店铺头像
     */
    private String brandShopIcon;
    /**
     * 店铺名称
     */
    private String brandShopName;
    /**
     * 星级
     */
    private int evalScore;

    /**
     * 品牌商店铺标识
     */
    private String brandShopSignIcon;
    /**
     * 共计多少商品
     */
    private int productCount;

    /**
     * xx类服务
     */
    private int service;
    /**
     * 总销量
     */
    private int totalSales;

    public long getBrandShopId() {
        return brandShopId;
    }

    public void setBrandShopId(long brandShopId) {
        this.brandShopId = brandShopId;
    }

    public String getBrandShopIcon() {
        return brandShopIcon;
    }

    public void setBrandShopIcon(String brandShopIcon) {
        this.brandShopIcon = brandShopIcon;
    }

    public String getBrandShopName() {
        return brandShopName;
    }

    public void setBrandShopName(String brandShopName) {
        this.brandShopName = brandShopName;
    }

    public int getEvalScore() {
        return evalScore;
    }

    public void setEvalScore(int evalScore) {
        this.evalScore = evalScore;
    }

    public String getBrandShopSignIcon() {
        return brandShopSignIcon;
    }

    public void setBrandShopSignIcon(String brandShopSignIcon) {
        this.brandShopSignIcon = brandShopSignIcon;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }
}
