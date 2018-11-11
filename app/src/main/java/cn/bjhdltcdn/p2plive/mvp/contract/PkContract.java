package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface PkContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 点赞
         */
        void playPraise(long playId, long userId, int type);

        /**
         * 点赞
         */
        void deletePlay(long playId, long userId);

        /**
         * 参与的pk列表
         */
        void getMyPlayList(long userId, long toUserId, int pageSize, int pageNumber, boolean isNeedShowLoading);

        /**
         * 参与的pk列表
         */
        void getPlayCommentList(long playId, int sort, int pageSize, int pageNumber);

        /**
         * 参与的pk列表
         */
        void playComment(long playId, String content, int type, long toUserId, long fromUserId);

        /**
         * 获取PK挑战详情接口
         *
         * @param userId 用户ID
         * @param playId 挑战ID
         */
        void findPlayDetail(long userId, long playId);

    }
}