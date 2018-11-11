package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ReportType;

public class GetReportTypeResponse {
    private List<ReportType> reprotTypeList ;
    private int code ;
    private String msg ;

    public List<ReportType> getReprotTypeList() {
        return reprotTypeList;
    }

    public void setReprotTypeList(List<ReportType> reprotTypeList) {
        this.reprotTypeList = reprotTypeList;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
