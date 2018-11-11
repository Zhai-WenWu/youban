package cn.bjhdltcdn.p2plive.model;

/**
 * Created by Hu_PC on 2017/11/16.
 */

public class NearOrganInfo {
    private OrganizationInfo organizationInfo;//{OrganizationInfo}圈子对象,
    private int nearPerson;//附近参与圈子的人数,

    public OrganizationInfo getOrganizationInfo() {
        return organizationInfo;
    }

    public void setOrganizationInfo(OrganizationInfo organizationInfo) {
        this.organizationInfo = organizationInfo;
    }

    public int getNearPerson() {
        return nearPerson;
    }

    public void setNearPerson(int nearPerson) {
        this.nearPerson = nearPerson;
    }
}
