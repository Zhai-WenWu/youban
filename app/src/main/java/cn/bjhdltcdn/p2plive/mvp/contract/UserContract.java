package cn.bjhdltcdn.p2plive.mvp.contract;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface UserContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 更新用户所在位置信息
         *
         * @param userId    用户Id
         * @param longitude 经度
         * @param latitude  维度
         * @param province  省
         * @param city      市
         * @param district  区县
         * @param addr      地址
         */
        void updateUserLocation(long userId, String longitude, String latitude, String province, String city, String district, String addr);


        /**
         * 查询通讯录列表接口
         *
         * @param userId     当前用户id
         * @param pageSize   页面大小
         * @param pageNumber 第几页
         */
        void getFriendList(long userId, int pageSize, int pageNumber);

        /**
         * 关注/取消关注接口
         *
         * @param type       关注类型(1-->关注;2-->取消关注)
         * @param fromUserId 点击关注的用户id
         * @param toUserId   被关注的用户id
         */
        void attentionOperation(int type, long fromUserId, long toUserId);

        /**
         * 获取个人用户信息接口
         *
         * @param userId   用户id
         * @param toUserId 被查看的用户Id,
         */
        void getUserInfo(long userId, long toUserId);

        /**
         * 我参加的活动接口
         *
         * @param userId
         */
        void getJoinActionList(long userId, long toUserId, int pageSize, int pageNumber);

        /**
         * 我发布的帖子接口
         *
         * @param userId
         */
        void getPublishPostList(long userId, long toUserId, int pageSize, int pageNumber);

        /**
         * 我的账户接口
         *
         * @param userId
         */
        void myAccount(long userId);

        /**
         * 我的交易记录
         *
         * @param userId
         */
        void myTransactionRecordList(long userId, int pageSize, int pageNumber);

        /**
         * 版本升级
         *
         * @param version
         * @param platform
         */
        void getUpgradeVersion(String version, int platform);


        /**
         * 我的礼物接口
         *
         * @param userId     用户userId
         * @param type       类型(1--->收到道具，2---->送出道具)
         * @param pageSize   页面大小
         * @param pageNumber 第几页
         */
        void myPropsList(long userId, int type, int pageSize, int pageNumber);


        /**
         * 获取黑名单列表接口
         *
         * @param userId     用户id
         * @param pageSize   每页大小
         * @param pageNumber 第几页
         */
        void getBlackList(long userId, int pageSize, int pageNumber);

        /**
         * 拉黑好友接口
         *
         * @param fromUserId 用户id
         * @param toUserId   被拉黑的用户id
         */
        void pullBlackUser(long fromUserId, long toUserId);

        /**
         * 解除好友拉黑接口
         *
         * @param blackId 黑名单名单表主键
         */
        void removeBlackList(long blackId);

        /**
         * 关注状态接口
         *
         * @param fromUserId 当前用户userId
         * @param toUserId   对方userId
         */
        void attentionStatus(long fromUserId, long toUserId);


        /**
         * 获取关注/粉丝列表
         *
         * @param type       类型(0-->关注人的列表；1-->粉丝列表),
         * @param userId     用户id
         * @param pageSize   页面大小
         * @param pageNumber 页数
         */
        void getAttentionOrFollowerList(int type, long userId, long toUserId, int pageSize, int pageNumber);

        /**
         * 获取实名认证提示信息接口
         */
        void getAuthContentInfo();

        /**
         * 实名认证接口
         *
         * @param userId      用户Id
         * @param realName    真实姓名
         * @param phoneNumber 手机号
         * @param certNumber  身份证号
         * @param imageList   [{Image对象},...]
         */
        void saveRealNameAuth(long userId, String realName, String phoneNumber, String certNumber, List<Image> imageList);

        /**
         * 实名认证接口
         *
         * @param userId      用户Id
         * @param realName    真实姓名
         * @param phoneNumber 手机号
         * @param certNumber  身份证号
         * @param imageList   [{Image对象},...]
         */
        void saveRealNameAuthIntegration(long userId, String realName, String phoneNumber, String certNumber, List<Image> imageList, String cardFrontImg, String cardBackImg);


        /**
         * 获取用户token接口
         *
         * @param userId
         */
        void findTokenByUserId(long userId);

        /**
         * 查询用户余额接口
         *
         * @param userId 用户userId
         */
        void queryUserBalance(long userId);

        /**
         * 获取用户一对一视频信息接口
         *
         * @param userId 用户userId
         */
        void findUserVideoInfo(long userId);

        /**
         * 更新用户一对一视频信息接口
         *
         * @param userId 呼叫用户Id
         * @param status 是否接收陌生人来电(1接收,2拒绝),
         */
        void updateUserVideoInfo(long userId, int status);


        /**
         * 获取私信道具列表接口
         */
        void getLetterPropsList();


        /**
         * 私信赠送礼物接口
         *
         * @param fromUserId    赠送礼物用户的Id
         * @param toUserId      接收礼物用户的id
         * @param propsId       礼物id
         * @param propsNum      礼物个数
         * @param presentedType 赠送类型 1 普通赠送,2 赠送金币
         * @param goldNum       赠送金币数 presentedType为2时使用
         */
        void letterPresentedGifts(long fromUserId, long toUserId, long propsId, int propsNum, int presentedType, int goldNum);

        /**
         * 获取实名认证状态接口
         *
         * @param userId 用户id
         */
        void findUserAuthStatus(long userId);


        /**
         * 判断回复内容是否被删除接口
         *
         * @param userId    用户Id
         * @param commentId 评论/回复id
         * @param type      类型(1表白,2帖子,3帮帮忙)
         */
        void checkIsDeleteComment(long userId, long commentId, int type);

        /**
         * 判断用户是否可以修改学校接口
         *
         * @param userId 当前用户id
         */
        void judgeUserSchool(long userId);


        /**
         * 保存学校接口
         *
         * @param userId   用户Id
         * @param schoolId 学校Id
         */
        void saveSchool(long userId, long schoolId, String schoolName);

        /**
         * 获取登录推荐数据接口
         *
         * @param userId 用户Id
         */
        void getLoginRecommendList(long userId);

        /**
         * 发送匿名消息接口
         *
         * @param fromUserId 发送用户id
         * @param toUserId   接收用户Id/聊天室id
         * @param type       类型(1个人,2聊天室),
         * @param content    消息内容
         * @param file       文件路径
         */
        void sendAnonymousMsg(long fromUserId, long toUserId, int type, String content, String file);

        /**
         * 获取匿名用户接口
         *
         * @param fromUserId
         * @param toUserId
         */
        void getAnonymityUser(long fromUserId, long toUserId);

        /**
         * 添加匿名好友
         *
         * @param fromUserId
         * @param toUserId
         */
        void saveAnonymityUser(long fromUserId, long toUserId);

        /**
         * 获取匿名好友列表
         *
         * @param userId
         * @param pageSize
         * @param pageNumber
         */
        void getAnonymityFriendList(long userId, int pageSize, int pageNumber);

        /**
         * 删除匿名好友接口
         *
         * @param fromUserId
         * @param toUserId
         */
        void deleteAnonymityUser(long fromUserId, long toUserId);

        /**
         * 修改匿名昵称接口
         *
         * @param userId
         * @param nickName
         */
        void updateAnonymityUser(long userId, String nickName);

    }
}