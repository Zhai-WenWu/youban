package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public interface GetCommentListContract {
    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 获取表白详情
         *
         * @param userId    用户Id,
         * @param sayLoveId 表白Id,
         */
        void findSayLoveDetail(long userId, long sayLoveId);

        /**
         * 查询帖子评论/回复列表
         *
         * @param parentId   表白Id,
         * @param sort       (1评论最新,2评论最早),
         * @param pageSize   页面大小,
         * @param pageNumber 第几页,
         */
        void getCommentList(long userId,long parentId, int sort, int pageSize, int pageNumber, boolean isNeedShowLoading);

        /**
         * 表白评论/回复
         *
         * @param sayLoveId  主键Id,
         * @param content    "评论/回复内容",
         * @param type       内容类型(1--->评论,2--->回复)
         * @param toUserId   被回复人的id
         * @param fromUserId 评论/回复人的id
         * @param commentId  评论Id(commentId),
         */
        void sayLoveComment(long sayLoveId, String content, int type, long toUserId, long fromUserId, long commentId);

        /**
         * 表白点赞
         *
         * @param sayLoveId 主键Id,
         * @param type      点赞类型(1 点赞  2 取消点赞),
         * @param userId    点赞的人id,
         */
        void sayLovePraise(long sayLoveId, int type, long userId);

        /**
         * 删除表白评论/回复
         *
         * @param commentId 评论/回复id,
         */
        void deleteSayLoveComment(long commentId);

        /**
         * 删除表白
         *
         * @param userId    用户Id,
         * @param sayLoveId :表白id,
         */
        void deleteSayLove(long userId, long sayLoveId);
    }
}
