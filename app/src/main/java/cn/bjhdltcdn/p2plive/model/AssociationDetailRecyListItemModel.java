package cn.bjhdltcdn.p2plive.model;

import java.util.List;

/**
 * Created by xiawenquan on 18/4/14.
 */

public class AssociationDetailRecyListItemModel {

    /**
     * 100 列表头部
     * 101 列表头部广告
     * 102 列表空数据的展示
     */
    private int type;
    //列表头部数据
    private OrganizationInfo organizationInfo;
    //列表头部广告
    private List<RecommendInfo> recommendInfoList;
    //列表空数据的展示数据
    private String emptyText;

    /**
     * 100 列表头部
     * 101 列表头部广告
     * 102 列表空数据的展示
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public OrganizationInfo getOrganizationInfo() {
        return organizationInfo;
    }

    public void setOrganizationInfo(OrganizationInfo organizationInfo) {
        this.organizationInfo = organizationInfo;
    }

    public List<RecommendInfo> getRecommendInfoList() {
        return recommendInfoList;
    }

    public void setRecommendInfoList(List<RecommendInfo> recommendInfoList) {
        this.recommendInfoList = recommendInfoList;
    }

    public String getEmptyText() {
        return emptyText;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }
}
