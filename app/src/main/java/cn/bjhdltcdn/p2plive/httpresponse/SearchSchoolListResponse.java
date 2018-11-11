package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.SchoolInfo;

/**
 * Created by ZHUDI on 2017/11/21.
 */

public class SearchSchoolListResponse extends BaseResponse {
    private List<SchoolInfo> schoolList;//[{SchoolInfo对象},{SchoolInfo对象}...],
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SchoolInfo> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(List<SchoolInfo> schoolList) {
        this.schoolList = schoolList;
    }
}
