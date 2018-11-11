package cn.bjhdltcdn.p2plive.model;

/**
 * Created by ZHAI on 2018/1/4.
 */

public class OccupationIndexInfo {
    private static OccupationIndexInfo occupationIndexInfo;

    public static OccupationIndexInfo getInstent() {
        if (occupationIndexInfo == null) {
            occupationIndexInfo = new OccupationIndexInfo();
        }
        return occupationIndexInfo;
    }

    public int index = -1;//记录职业列表选中状态

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
