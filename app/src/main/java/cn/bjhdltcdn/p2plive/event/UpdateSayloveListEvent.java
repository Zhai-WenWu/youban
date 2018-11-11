package cn.bjhdltcdn.p2plive.event;

/**
 * Created by Hu_PC on 2017/11/23.
 */

public class UpdateSayloveListEvent {
    private int type;//1:更新评论2：更新点赞3：删除表白4:发布表白
    private int position;//更新的位置
    private int commentNum;
    private int isPraise;//(1 点赞  2 取消点赞),

    public UpdateSayloveListEvent(int type, int position, int commentNum, int isPraise) {
        this.type = type;
        this.position = position;
        this.commentNum = commentNum;
        this.isPraise = isPraise;
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
}
