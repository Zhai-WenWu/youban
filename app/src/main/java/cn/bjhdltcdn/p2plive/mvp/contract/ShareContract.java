package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by xiawenquan on 17/11/17.
 */

public interface ShareContract {


    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 分享次数记录接口
         *
         * @param userId   用户Id
         * @param parentId 分享类型Id,
         * @param type     类型(1帖子,2圈子,3活动,4房间,5PK挑战,6表白),
         */
        void saveShareNumber(long userId, long parentId, int type);

        /**
         * 分享数据到关注用户接口
         *
         * @param userId   用户Id,
         * @param parentId 分享类型Id,
         * @param type     类型(1帖子,2圈子,3活动,4房间,5PK挑战,6表白),
         * @param content  分享的内容,
         */
        void saveShareAttention(long userId, long parentId, int type, String content);

        /**
         * 分享数据到关注用户接口
         *
         * @param userId            用户Id,
         * @param toUserId          被查看的用户Id,
         * @param isNeedShowLoading
         */
        void getMyShareList(long userId, long toUserId, int pageSize, int pageNumber, boolean isNeedShowLoading);

        /**
         * 删除我的分享接口
         *
         * @param userId  用户Id(当前操作的用户),
         * @param shareId 分享Id,
         */
        void deleteMyShare(long userId, long shareId);

        /**
         * @param userId      用户Id,
         * @param postLabelId 帖子标签Id,
         * @param pageSize
         * @param pageNumber
         */
        void getPostListByLabelId(long userId, long postLabelId, int pageSize, int pageNumber);
    }
}
