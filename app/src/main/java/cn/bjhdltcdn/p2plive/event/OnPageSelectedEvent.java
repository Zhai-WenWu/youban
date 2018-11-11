package cn.bjhdltcdn.p2plive.event;

/**
 * Created by xiawenquan on 17/12/1.
 */

public class OnPageSelectedEvent {
    // 0 首页；1 圈子 ； 2 发现；3 消息 ；4 我
    private int tabSelectIndex ;

    public OnPageSelectedEvent(int tabSelectIndex) {
        this.tabSelectIndex = tabSelectIndex;
    }

    /**
     *  0 首页；1 圈子 ； 2 发现；3 消息 ；4 我
     * @return
     */
    public int getTabSelectIndex() {
        return tabSelectIndex;
    }

    public void setTabSelectIndex(int tabSelectIndex) {
        this.tabSelectIndex = tabSelectIndex;
    }
}
