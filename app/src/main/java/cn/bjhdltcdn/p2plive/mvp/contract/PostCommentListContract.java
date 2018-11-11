package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public interface PostCommentListContract {
    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 获取帖子详情
         *
         * @param userId 用户Id,
         * @param postId 帖子Id,
         */
        void findPostDetail(long userId, long postId);

        /**
         * 查询帖子评论/回复列表
         *
         * @param userId     用户Id
         * @param parentId   帖子Id,
         * @param sort       排序(1按热度,2按时间)
         * @param pageSize   页面大小,
         * @param pageNumber 第几页,
         */
        void getPostCommentList(long userId, long parentId, int sort, int pageSize, int pageNumber);

        /**
         * 点赞帖子
         *
         * @param userId 用户Id,
         * @param postId 帖子Id,
         * @param type   点赞类型(1 点赞  2 取消点赞),
         */
        void postPraise(long userId, long postId, int type);



        /**
         * 帖子评论/回复接口
         *
         * @param content       评论/回复内容
         * @param type          内容类型(1--->评论,2--->回复)
         * @param toUserId      被回复人的id
         * @param fromUserId    评论/回复人的id
         * @param postId        帖子Id
         * @param parentId      评论commentId(评论时传0)
         * @param anonymousType 匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名)
         */
        void postComment(String content, int type, long toUserId, long fromUserId, long postId, long parentId, int anonymousType);


        /**
         * 查询帖子列表接口
         *
         * @param userId     用户Id
         * @param organId    圈子Id(Id=0查询全部)
         * @param sort       排序(1最新发布,2离我最近,3最热帖子
         * @param pageSize   页面大小
         * @param pageNumber 第几页
         */
        void getPostList(long userId, long organId, int sort, int pageSize, int pageNumber);


        /**
         * 删除帖子评论/回复接口
         *
         * @param userId    用户Id
         * @param commentId 评论/回复id
         */
        void deleteComment(long userId, long commentId);


        /**
         * 帖子置顶接口
         *
         * @param organId    圈子Id
         * @param relationId 关联Id(1帖子Id,3活动Id,5PK挑战Id)
         * @param category   类别(1帖子,3活动,5PK挑战)
         * @param type       内容类型(1--->置顶,2--->取消置顶)
         */
        void postTop(long organId, long relationId, int category, int type);


        /**
         * 删除帖子接口
         *
         * @param userId 用户Id
         * @param postId 帖子Id
         */
        void deletePost(long userId, long postId);


        /**
         * 查询圈子下的帖子活动接口
         *
         * @param userId     用户Id
         * @param organId    圈子Id
         * @param type       类型(1全部,2视频帖子和pk,3活动)
         * @param pageSize   页面大小
         * @param pageNumber 第几页
         */
        void getPostAndActivityList(long userId, long organId, int type, int pageSize, int pageNumber, boolean needShowLoading);

        /**
         * 评论回复包含图片视频接口
         *
         * @param userId          上传人id
         * @param moduleId        所属id(1-->帖子Id,2-->表白Id,3-->PK挑战Id,4-->帮帮忙Id)
         * @param type            模块(1-->帖子,2-->表白,3-->PK挑战,4-->帮帮忙)
         * @param content         评论/回复内容
         * @param contentType     内容类型(1--->评论,2--->回复)
         * @param anonymousType   匿名类型(0--->不匿名，1--->评论用户匿名,2--->回复用户匿名，3--->回复与被回复用户匿名),
         * @param toUserId        被回复人的id
         * @param fromUserId      评论/回复人的id
         * @param parentId        评论commentId(评论时传0)
         * @param commentParentId 楼中楼的父评论id
         * @param commentType     评论/回复类型(1-->普通图文，2--->视频)
         * @param videoUrl        评论/回复视频路径(类型为视频使用)
         * @param videoImageUrl   评论/回复图片地址(类型为视频使用)
         * @param photoGraphTime  拍照时间
         * @param file            图片源
         */
        void commentUploadImage(long userId, long moduleId, int type, String content, int contentType, int anonymousType, long toUserId,
                                long fromUserId, long parentId, long commentParentId, int commentType, String videoUrl, String videoImageUrl,
                                String photoGraphTime, String file);

        /**
         * 评论回复点赞接口
         *
         * @param commentId 主键Id,
         * @param type      点赞类型(1 点赞  2 取消点赞),
         * @param userId    点赞的人id,
         * @param module    模块(1帖子,2表白,3PK挑战,4帮帮忙),
         */
        void commentPraise(long commentId, int type, long userId, int module);


        /**
         * 查询回复列表接口
         *
         * @param userId     用户Id,
         * @param commentId  评论Id,
         * @param parentId   父Id(1帖子Id,2表白Id,3PK挑战Id,4帮帮忙Id),
         * @param module     模块(1帖子,2表白,3PK挑战,4帮帮忙),
         * @param sort       排序(1按热度,2按时间),
         * @param pageSize   每页大小,
         * @param pageNumber 第几页,
         */
        void getReplyList(long userId, long commentId, long parentId, int module, int sort, int pageSize, int pageNumber);
    }
}
