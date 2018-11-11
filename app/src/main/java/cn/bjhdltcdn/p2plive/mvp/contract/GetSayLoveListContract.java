package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface GetSayLoveListContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 查询表白列表
         *  @param userId     用户Id,
         * @param sort       排序(1最新发布,2离我最近,3最热表白4.本校发布),
         * @param pageSize   页面大小,
         * @param pageNumber 第几页
         * @param b
         */
        void getSayLoveList(long userId, int sort, int pageSize, int pageNumber, boolean b);

        /**
         * 查询表白列表
         *
         * @param userId    用户Id,
         * @param sayLoveId 表白id,
         */
        void deleteSayLove(long userId, long sayLoveId);

        /**
         * 我发布的表白接口
         *  @param userId     用户Id(当前操作的用户),
         * @param toUserId   被查看的用户Id,
         * @param pageSize   页面大小,
         * @param pageNumber 第几页,
         * @param isNeedShowLoading
         */
        void getMySayLoveList(long userId, long toUserId, int pageSize, int pageNumber, boolean isNeedShowLoading);
    }
}