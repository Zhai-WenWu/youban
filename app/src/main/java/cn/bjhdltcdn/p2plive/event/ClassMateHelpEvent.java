package cn.bjhdltcdn.p2plive.event;

/**
 * Created by Hu_PC on 2017/11/23.
 */

public class ClassMateHelpEvent {
    private int type;//1:刷新 2：更新点赞 5:删除
    private int position;//更新的位置
    private int isPraise;//(1 点赞  2 取消点赞),

    public ClassMateHelpEvent(int type, int position, int isPraise) {
        this.type = type;
        this.position = position;
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

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }
}
