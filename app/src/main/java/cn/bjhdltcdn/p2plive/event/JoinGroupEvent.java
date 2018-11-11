package cn.bjhdltcdn.p2plive.event;

/**
 * Created by ZHAI on 2018/2/2.
 */

public class JoinGroupEvent {
    public int groupItemPosition;
    public int IsExistGroup;

    public JoinGroupEvent(int groupItemPosition, int isExistGroup) {
        this.groupItemPosition = groupItemPosition;
        this.IsExistGroup = isExistGroup;
    }

    public int getGroupItemPosition() {
        return groupItemPosition;
    }


    public int getIsExistGroup() {
        return IsExistGroup;
    }

}
