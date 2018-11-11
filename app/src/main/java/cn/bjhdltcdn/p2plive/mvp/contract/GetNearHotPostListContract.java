package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface GetNearHotPostListContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         *  查询附近热帖
         * @param userId 用户Id,
         * @param sort 排序(1最新发布,2离我最近),
         */
        void getNearHotPostList(long userId, int sort,int pageSize,int pageNumber);

    }
}