package cn.bjhdltcdn.p2plive.event;

/**
 * Created by Hu_PC on 2017/11/29.
 */

public class UpdateActiveListEvent {
    int type;//1：联网刷新列表2：本地刷新列表 3:关闭发布界面
    int position;
    private boolean delect;
    private int userNum;

    public UpdateActiveListEvent(int type,int position,boolean delect, int userNum) {
        this.type=type;
        this.position = position;
        this.delect = delect;
        this.userNum = userNum;
    }

    public UpdateActiveListEvent(int type) {
        this.type=type;
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

    public boolean isDelect() {
        return delect;
    }

    public void setDelect(boolean delect) {
        this.delect = delect;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }
}
