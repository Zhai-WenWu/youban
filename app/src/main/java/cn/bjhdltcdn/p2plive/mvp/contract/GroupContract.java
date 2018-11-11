package cn.bjhdltcdn.p2plive.mvp.contract;

import java.util.List;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by xiawenquan on 17/11/27.
 */

public interface GroupContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 加入群组接口
         *
         * @param userId    当前用户id
         * @param groupId   群组id
         * @param groupMode 成员进群方式(1直接入群,2申请进群)
         */
        void joinGroup(long userId, long groupId, int groupMode);

        /**
         * 查询群组列表接口
         *
         * @param userId 当前用户id
         * @param b
         */
        void getGroupList(long userId, boolean b);

        /**
         * 我加入的群组接口
         *
         * @param userId 当前用户id
         * @param isNeedShowLoading
         */
        void getJoinGroupList(long userId, long toUserId, int pageSize, int pageNumber, boolean isNeedShowLoading);


        /**
         * 创建群组接口
         *
         * @param userId     当前用户id
         * @param relationId 群组关联Id(type=0传值0,type=1圈子Id,type=2活动Id)
         * @param groupName  群名称
         * @param groupMode  成员进群方式(1直接入群,2申请进群)
         * @param type       群组类型(默认为0--->普通群,1--->圈子群,2--->活动临时讨论群)
         * @param imgUrl     群组头像（http开头的链接）
         */
        void createGroup(long userId, long relationId, String groupName, int groupMode, int type,String imgUrl);


        /**
         * 查询群组成员列表接口
         *
         * @param userId     当前用户id
         * @param groupId    群组Id
         * @param pageSize   页面大小
         * @param pageNumber 第几页
         */
        void getGroupUserList(long userId, long groupId, int pageSize, int pageNumber);

        /**
         * 退出群组接口
         *
         * @param groupId    群组id
         * @param userId     执行操作的用户id
         * @param outUserIds [退出的用户id,退出的用户id,...]
         */
        void signOutGroup(long groupId, long userId, List<Long> outUserIds);

        /**
         * 解散群组接口
         *
         * @param groupId 群组id
         * @param userId  用户id
         */
        void disbandGroup(long groupId, long userId);


        /**
         * 获取群组信息接口
         *
         * @param groupId 群组id
         */
        void getGroupInfo(long groupId);


        /**
         * 分享群组信息接口
         *
         * @param userId   分享用户
         * @param groupId  群组id
         * @param toUserId 被分享用户Id
         */
        void shareGroupInfo(long userId, long groupId, long toUserId);

        /**
         * 修改群组信息接口
         *
         * @param groupId       群组id
         * @param groupName     群名称
         * @param groupMode     成员进群方式(1直接入群,2申请进群)
         * @param isDisturbMode 消息免打扰模式(默认为0--->关闭,1--->开启)
         * @param isPublic      是否公开圈子(默认为0--->公开,1--->隐藏)
         */
        void updateGroup(long groupId, String groupName, int groupMode, int isDisturbMode, int isPublic);

        /**
         * 审核入群申请接口
         *
         * @param userId  当前用户id
         * @param applyId 申请Id
         * @param type    类型(1同意,2拒绝)
         */
        void auditeGroupApply(long userId, long applyId, int type);

        /**
         * 查询待处理申请列表
         *
         * @param userId     当前用户id,
         * @param pageSize   页面大小,
         * @param pageNumber 第几页,
         */
        void getApplyList(long userId, int pageSize, int pageNumber);

        /**
         * 删除群组管理员接口
         *
         * @param groupId
         * @param managerId
         */
        void deleteGroupManager(long groupId, long managerId);

        /**
         * 设置管理员接口
         *
         * @param groupId    群组id,
         * @param managerIds [用户id,用户id,...]管理员id,
         */
        void setManager(long groupId, List<Long> managerIds);
    }


}
