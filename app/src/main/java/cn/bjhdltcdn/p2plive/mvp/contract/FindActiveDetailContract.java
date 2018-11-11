package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface FindActiveDetailContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 获取活动详情
         * @param userId 用户Id,
         * @param activityId 活动Id,
         */
        void findActiveDetail(long userId, long activityId);

        /**
         * 参与活动
         * @param userId 用户Id
         * @param activityId 活动Id,
         */
        void joinActive(long userId, long activityId);

        /**
         * 退出活动
         * @param userId 用户Id
         * @param activityId 活动Id,
         */
        void signOutActive(long userId, long activityId);

        /**
         * 删除活动
         * @param userId 用户Id,
         * @param activityId 活动Id,
         */
        void deleteActive(long userId,long activityId);
    }
}