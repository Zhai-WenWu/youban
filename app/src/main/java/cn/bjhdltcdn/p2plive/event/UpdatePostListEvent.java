package cn.bjhdltcdn.p2plive.event;

/**
 * Created by Hu_PC on 2017/11/23.
 */

public class UpdatePostListEvent {
    private int comeInType;//0:首页帖子 1：附近帖子
    private int type;//1:更新评论 2：更新点赞 3：删除表白 4:发布表白 ; 5:删帖子 ; 6:置顶; 7取消置顶 ；8 删除圈子内容；
    private int position;//更新的位置
    private int commentNum;
    private int isPraise;//(1 点赞  2 取消点赞),

    /**
     * 页面类型
     * 0 圈子列表
     * 1 圈子
     * 2
     */
    private int pageType;

    public UpdatePostListEvent(int type, int position, int pageType) {
        this.type = type;
        this.position = position;
        this.pageType = pageType;
    }

    public UpdatePostListEvent(int type, int position) {
        this.type = type;
        this.position = position;
    }

    public UpdatePostListEvent(int type, int position, int commentNum, int isPraise, int comeInType) {
        this.type = type;
        this.position = position;
        this.commentNum = commentNum;
        this.isPraise = isPraise;
        this.comeInType=comeInType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public int getComeInType() {
        return comeInType;
    }

    public void setComeInType(int comeInType) {
        this.comeInType = comeInType;
    }

    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }
}
