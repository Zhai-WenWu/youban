package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.LabelInfo;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetStoreLabelListResponse extends BaseResponse {
    private List<LabelInfo> distanceList;//[{LabelInfo对象},{LabelInfo对象},...]校园店排序(本校/同城/周边)列表,
    private List<FirstLabelInfo> typeList;//[{LabelInfo对象},{LabelInfo对象},...]校园店类别列表,
    private int merchantSwitch;//商家开关(1开启,2关闭),

    public List<LabelInfo> getDistanceList() {
        return distanceList;
    }

    public void setDistanceList(List<LabelInfo> distanceList) {
        this.distanceList = distanceList;
    }

    public List<FirstLabelInfo> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<FirstLabelInfo> typeList) {
        this.typeList = typeList;
    }

    public int getMerchantSwitch() {
        return merchantSwitch;
    }

    public void setMerchantSwitch(int merchantSwitch) {
        this.merchantSwitch = merchantSwitch;
    }
}
