package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface GetHomeNearbyContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 查询附近最新更新条数
         * @param userId 用户Id,
         */
        void getNewCountList(long userId);

        /**
         * 更新刷新时间
         * @param userId 用户Id,
         * @param type 类型(1线下活动,2在玩儿啥,3表白邂逅,4圈子热帖),
         */
        void updateRefreshTime(long userId, int type);

        /**
         * 查询首页更新条数接口
         * @param userId 用户Id
         */
        void getHomeNewCountList(long userId);

        /**
         * 查询首页更新条数接口
         * @param userId 用户Id
         */
        void getHomeNewCount(long userId);

    }
}