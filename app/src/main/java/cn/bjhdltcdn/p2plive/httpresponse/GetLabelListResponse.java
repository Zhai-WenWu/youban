package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.LabelInfo;

/**
 * Created by ZHUDI on 2017/11/17.
 */

public class GetLabelListResponse extends BaseResponse{
    private List<LabelInfo> labelList;//[{LabelInfo对象},{LabelInfo对象},...],
    private List<LabelInfo> reportList;//试用报告列表

    private String content1;//热门赛事：(labelList不为空返回)
    private String content2;//当前热门赛事一共3项(labelList不为空返回)
    private String content3;//你有一份试用报告需要提交(reportList不为空返回)


    public List<LabelInfo> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<LabelInfo> labelList) {
        this.labelList = labelList;
    }


    public List<LabelInfo> getReportList() {
        return reportList;
    }

    public void setReportList(List<LabelInfo> reportList) {
        this.reportList = reportList;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }
}
