//package cn.bjhdltcdn.p2plive.event;
//
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//
///**
// * 上传帖子结果展示对话框
// */
//
//public class PublishSuccessLabelFragmentDialogEvent {
//    /**
//     * type ：1 标识子home页面显示；2 标识在跟拍页面显示
//     */
//    private int type = 1;
//
//    private long labelId;
//
//    private List<OrganizationInfo> list;
//
//    /**
//     * 发布模块 2圈子，3表白，4同学帮帮忙
//     */
//    private int moudle;
//
//    private long  postId;
//
//    public PublishSuccessLabelFragmentDialogEvent(int type, long labelId) {
//        this.type = type;
//        this.labelId = labelId;
//    }
//
//    public PublishSuccessLabelFragmentDialogEvent(int type, int moudle, long labelId, List<OrganizationInfo> list) {
//        this.type = type;
//        this.moudle = moudle;
//        this.labelId = labelId;
//        this.list = list;
//    }
//
//    public PublishSuccessLabelFragmentDialogEvent(int type, int moudle, long labelId, List<OrganizationInfo> list, long postId) {
//        this.type = type;
//        this.labelId = labelId;
//        this.list = list;
//        this.moudle = moudle;
//        this.postId = postId;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public long getLabelId() {
//        return labelId;
//    }
//
//    public void setLabelId(long labelId) {
//        this.labelId = labelId;
//    }
//
//    public List<OrganizationInfo> getList() {
//        return list;
//    }
//
//    public void setList(List<OrganizationInfo> list) {
//        this.list = list;
//    }
//
//    public int getMoudle() {
//        return moudle;
//    }
//
//    public void setMoudle(int moudle) {
//        this.moudle = moudle;
//    }
//
//    public long getPostId() {
//        return postId;
//    }
//
//    public void setPostId(long postId) {
//        this.postId = postId;
//    }
//}
