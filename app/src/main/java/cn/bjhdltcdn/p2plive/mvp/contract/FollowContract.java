package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface FollowContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 查询表白列表
         *
         * @param userId   用户Id,
         * @param parentId 视频Id,
         * @param type     类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请,8表白,9同学帮帮忙),
         */
        void findFollowDetail(long userId, long parentId, int type);

        /**
         * 查询表白列表
         *  @param toUserId 原帖用户Id,
         * @param userId   用户Id,
         * @param sort     排序(1最热视频,2最新视频),
         * @param parentId 主视频Id(查询所有默认0),
         * @param type     类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请,8表白,9同学帮帮忙)(查询所有默认0),
         * @param needShowLoading
         */
        void getItemFollowList(long userId,long toUserId, int sort, long parentId, int type, int pageSize, int pageNumber, boolean needShowLoading);

        /**
         * 查询表白列表
         *
         * @param userId          用户Id,
         * @param sort            排序(1最热视频,2最新视频),
         * @param needShowLoading
         */
        void getAllFollowList(long userId, int sort, int pageSize, int pageNumber, boolean needShowLoading);

    }
}