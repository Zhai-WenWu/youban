package cn.bjhdltcdn.p2plive.mvp.contract;

import java.util.List;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by ZHUDI on 2017/12/11.
 */

public interface ChatRoomContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 开启/关闭视频房间接口
         *
         * @param userId       用户的id
         * @param roomId       房间id(关闭房间使用)
         * @param roomName     房间主题(开启房间使用)
         * @param status       房间状态(0--->开启,1--->关闭(暂不使用),2--->注销(本期关闭即注销))
         * @param passwordType 加密类型(0--->不加密,1--->密码)
         * @param password     密码(passwordType为1时使用)
         * @param labelList    标签Id集合
         * @param customList   自定义标签
         */
        void updateRoomStatus(long userId, long roomId, String roomName, int status, int passwordType,
                              String password, List<Long> labelList, List<String> customList);

        /**
         * 进入/退出视频房间接口
         *
         * @param toUserId     用户的id
         * @param roomId       房间id(关闭房间使用)
         * @param type         操作类型 0 进入房间 1 主动退出房间 2 用户被移除房间
         * @param passwordType 加密类型(0--->不加密,1--->密码)
         * @param password     密码(passwordType为1时使用)
         */
        void updateUserStatus(long toUserId, long roomId, int type, int passwordType, String password);

        /**
         * 用户上/下麦接口
         *
         * @param userId   用户id
         * @param toUserId 上/下麦用户id
         * @param roomId   房间id
         * @param wheat    1申请上麦,2上麦,3下麦,4拒绝
         * @param type     1(视频),2(语音)
         */
        void upOrDownWheat(long userId, long toUserId, long roomId, int wheat, int type);

        /**
         * 用户视频摄像头控制接口
         *
         * @param userId 用户Id
         * @param roomId 房间Id
         * @param type   类型(1开,2关）
         */
        void controlUserCamera(long userId, long roomId, int type);

        /**
         * 获取上麦申请用户列表接口
         *
         * @param roomId 房间id
         */
        void getOnWheatApplyList(long roomId);

        /**
         * 获取上麦用户列表
         *
         * @param roomId
         */
        void getOnWheatList(long roomId);

        /**
         * 获取本轮投票数据接口
         *
         * @param userId 用户Id,
         * @param roomId 房间的id,
         * @param voteId 投票Id,
         * @param sort   排序(1是上麦排序 2是投票排序),
         */
        void getCurrentVoteData(long userId, long roomId, long voteId, int sort);

        /**
         * 移交主持权限接口
         *
         * @param roomId   房间的id,
         * @param transfer 移交状态 1 发送移交申请 2对方同意 3对方拒绝 11接收请求的标识,
         * @param userId   发送用户ID,
         * @param toUserId 接收用户ID,
         */
        void transferHosting(long roomId, int transfer, long userId, long toUserId);

        /**
         * 发起/关闭投票接口
         *
         * @param roomId 房间的id
         * @param userId 发起/关闭用户
         * @param type   1发起 2关闭
         * @param voteId 投票Id(关闭时必填)
         */
        void initiateVote(long roomId, long userId, int type, long voteId);

        /**
         * 获取房主信息接口
         *
         * @param roomId 房间id
         */
        void getOwnerInfo(long roomId);

        /**
         * 禁言接口
         *
         * @param roomId   房间的id
         * @param type     是否禁言(1否-取消,2是-开启)
         * @param userId   禁言用户ID
         * @param toUserId 被禁言用户ID
         */
        void banningComments(long roomId, int type, long userId, long toUserId);

        /**
         * 多人视频关注/取消关注接口
         *
         * @param type       关注类型(1-->关注;2-->取消关注)
         * @param roomId     房间id
         * @param fromUserId 点击关注的用户id
         * @param toUserId   被关注的用户id
         */
        void attentionOperation(int type, long roomId, long fromUserId, long toUserId);


        /**
         * 赠送道具接口
         *
         * @param fromUserId    赠送道具用户的Id
         * @param toUserId      接收道具用户的id
         * @param propsId       道具id
         * @param presentedType 赠送类型 1 普通赠送 2 赠送金币
         * @param goldNum       赠送金币数 presentedType为2时使用
         * @param propsNum      礼物个数
         */
        void presentedProps(long fromUserId, long toUserId, long propsId, int presentedType, int goldNum, int propsNum);

        /**
         * 获取道具列表接口
         */
        void getPropsList();

        /**
         * 校验一对一视频接口
         *
         * @param userId       呼叫用户Id
         * @param answerUserId 接听用户Id
         */
        void checkOneToOneVideo(long userId, long answerUserId);

        /**
         * 获取房间中所有在线用户接口
         *
         * @param roomId
         */
        void getOnlineUserList(long roomId);

        /**
         * 获取礼物赠送选择数列表接口
         */
        void giftNumList();


        /**
         * 多人视频赠送礼物接口
         *
         * @param fromUserId    赠送礼物用户的Id
         * @param toUserId      接收礼物用户的id
         * @param propsId       礼物id
         * @param propsNum      礼物个数
         * @param parentId      房间ID
         * @param voteId        投票Id
         * @param presentedType 1 普通赠送,2 赠送金币
         * @param goldNum       赠送金币数 presentedType为2时使用
         */
        void presentedGifts(long fromUserId, long toUserId, long propsId, int propsNum, long parentId, long voteId, int presentedType, long goldNum);

        /**
         * 获取个人用户信息接口
         *
         * @param userId   用户id
         * @param toUserId 被查看的用户Id,
         * @param roomId   房间Id(从房间查看用户信息时传入,其他默认0),
         */
        void getUserInfo(long userId, long toUserId, long roomId);


        /**
         * 用户投票接口
         *
         * @param roomId   房间的id
         * @param userId   投票用户
         * @param toUserId 被投用户
         * @param voteId   投票Id
         */
        void userVoting(long roomId, long userId, long toUserId, long voteId);

        /**
         * 麦上嘉宾查看礼物数据接口
         *
         * @param roomId     房间的Id
         * @param type       类型(1本场收获,2累计收获)
         * @param userId     用户Id,
         * @param pageSize   页面大小(不分页时传0),
         * @param pageNumber 第几页(不分页时传0),
         */
        void getWheatPropsData(long roomId, int type, long userId, int pageSize, int pageNumber);

        /**
         * 获取本场金币统计数据
         *
         * @param roomId 房间的Id
         * @param userId 用户Id
         */
        void getCurrentGoldStatistic(long roomId, long userId);


        /**
         * 一对一视频接口
         *
         * @param userId      用户Id,
         * @param fromUserId  呼叫用户Id,
         * @param toUserId    接听用户Id,
         * @param callType    呼叫类型(1语音,2视频),
         * @param type        类型(1呼叫,2接听,3拒绝,4结束,5未接),
         * @param channelName 房间号,
         */
        void oneToOneVideo(long userId, long fromUserId, long toUserId, int callType, int type, String channelName);

        /**
         * 获取多人视频详情接口
         *
         * @param userId
         * @param roomId
         */
        void findRoomDetail(long userId, long roomId);

        /**
         * 开启/关闭匿名聊天室接口
         *
         * @param userId      用户的id
         * @param chatId      聊天室id(关闭聊天室使用)
         * @param chatName    聊天室主题(开启聊天室使用)
         * @param status      聊天室状态(0--->开启,1--->关闭,2--->注销)
         * @param schoolLimit 校友限制(0--->不限制,1--->校友)
         * @param sexLimit    性别限制(0--->不限制,1--->男,2--->女)
         * @param labelList   标签Id集合
         * @param customList  自定义标签
         */
        void updateChatRoom(long userId, String chatId, String chatName, int status, int schoolLimit,
                            int sexLimit, List<Long> labelList, List<String> customList);

        /**
         * 进入/退出聊天室接口
         *
         * @param userId   当前用户Id
         * @param toUserId 用户的id 类型type为2时使用，此为被移除用户ID,
         * @param chatId   聊天室id,
         * @param type     操作类型 0 进入聊天室 1 主动退出聊天室 2 用户被移除聊天室,
         */
        void updateChatRoomUser(long userId, long toUserId, String chatId, int type);

        /**
         * 查询多人视频列表接口
         *
         * @param userId     操作用户id
         * @param pageSize   页面大小
         * @param pageNumber 第几页
         */
        void getRoomList(long userId, int pageSize, int pageNumber);

        /**
         * 查询聊天室列表接口
         *
         * @param userId     操作用户id
         * @param pageSize   页面大小
         * @param pageNumber 第几页
         * @param sort       排序(标签ID),
         */
        void getChatRoomList(long userId, int pageSize, int pageNumber, int sort);


        /**
         * 修改聊天室锁接口
         *
         * @param userId
         * @param chatId 聊天室id(关闭聊天室使用),
         * @param isLock 是否上锁(0---->否，1---是)
         */
        void updateChatRoomLock(long userId, String chatId, int isLock);
    }
}
