package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by ZHUDI on 2017/12/8.
 */

public class GetAuthContentInfoResponse extends BaseResponse {
    private String content1;//提示语1(请填写以下内容并提交申请，未提交实名认证或认证不通过的用户将无法提现，且无法申请各类权限。),
    private String content2;//提示语2(注意：照片大小不要超过1.5M),
    private String content3;//提示语3(根据国家相关法律法规和相关税收政策，平台将为您代扣代缴相关税费，扣税金额为您提现金额的10%。因此您收到的实际金额与提现金额将略有差异。)

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
