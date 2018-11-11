package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.LabelInfo;

/**
 * Created by xiawenquan on 18/3/29.
 */

public class GetSecondLabelListResponse extends BaseResponse {

    private List<LabelInfo> labelList;
    private String content;
    private String content1;

    public List<LabelInfo> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<LabelInfo> labelList) {
        this.labelList = labelList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }
}
