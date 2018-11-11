package cn.bjhdltcdn.p2plive.event;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

/**
 * Created by xiawenquan on 18/3/2.
 */

public class AssociationSelectEvent {
    List<OrganizationInfo> selectList;

    public AssociationSelectEvent(List<OrganizationInfo> selectList) {
        this.selectList = selectList;
    }

    public List<OrganizationInfo> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<OrganizationInfo> selectList) {
        this.selectList = selectList;
    }
}
