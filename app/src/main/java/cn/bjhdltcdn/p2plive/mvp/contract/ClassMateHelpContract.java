package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by ZHAI on 2018/2/27.
 */

public interface ClassMateHelpContract {
    interface Presenter extends BasePresenter {
        /**
         * 查询帮帮忙    列表接口
         *
         * @param userId          :用户Id,
         * @param sort            :排序(1校友发布,2最新发布,3我附近的,4最热发布),
         * @param needShowLoading
         */
        void getClassmateHelpList(long userId, int sort, int pageSize, int pageNumber, boolean needShowLoading);

        /**
         * 查询我的帮帮忙列表接口
         */
        void getPublishHelpList(long userId, long toUserId, int pageSize, int pageNumber, boolean needShowLoading);

        /**
         * 点赞帮帮忙接口
         *
         * @param userId :用户Id,
         * @param helpId :帮帮忙Id,
         * @param type   :点赞类型(1 点赞  2 取消点赞),
         */
        void helpPraise(long userId, long helpId, int type);

        /**
         * 删除帮帮忙接口
         *
         * @param userId :用户Id,
         * @param helpId :帮帮忙Id,
         */
        void deleteHelp(long userId, long helpId);

        /**
         * 发布同学帮帮忙接口
         *
         * @param userId    :用户Id,
         * @param isLoading :是否显示加载框
         * @param helpId    帮帮忙Id(跟拍时传入值,其他默认0)
         */
        void saveClassmateHelp(long userId, long toUserId, String content, int topicType, String videoUrl, String videoImageUrl, long labelId, long[] secondLabelIds, boolean isLoading, long helpId);

        /**
         * 查询帮帮忙评论/回复接口
         *
         * @param userId :用户Id,
         */
        void getHelpCommentList(long userId, long helpId, int sort, int pageSize, int pageNumber);

        /**
         * 删除帮帮忙评论/回复接口
         */
        void deleteHelpComment(long userId, long commentId);

        /**
         * 获取帮帮忙详情
         *
         * @param userId 用户Id,
         * @param helpId 帮帮忙Id
         */
        void findHelpDetail(long userId, long helpId);
    }
}
