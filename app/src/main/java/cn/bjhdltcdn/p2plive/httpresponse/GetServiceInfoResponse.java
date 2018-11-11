package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.UseTypeInfo;

/**
 * Created by ZHUDI on 2017/11/15.
 */

public class GetServiceInfoResponse extends BaseResponse{
   private List<UseTypeInfo> serviceList;//[{服务对象},{服务对象},...],


    public List<UseTypeInfo> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<UseTypeInfo> serviceList) {
        this.serviceList = serviceList;
    }
}
