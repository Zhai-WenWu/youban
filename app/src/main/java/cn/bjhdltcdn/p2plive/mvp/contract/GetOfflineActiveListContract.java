//package cn.bjhdltcdn.p2plive.mvp.contract;
//
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;
//
//public interface GetOfflineActiveListContract {
//
//    /**
//     * 业务控制器
//     */
//    interface Presenter extends BasePresenter {
//
//        /**
//         *  查询线下活动列表
//         * @param userId 用户Id,
//         * @param activeType :活动类型(0全部,1其他类型),
//         * @param hobbyIds [{HobbyId},{HobbyId}...](活动类型为1时赋值),
//         * @param sort 排序(1最新发布,2活动离我距离,3发布者离我距离),
//         * @param publisherLimit 活动发布者限制(0不限,1校友发布)
//         * @param sexLimit 性别限制(0不限,1仅限男生,2仅限女生),
//         * @param pageSize 每页大小,
//         * @param pageNumber 第几页,
//         * @param isNeedShowLoading
//         */
//        void getOfflineActiveList(long userId, int activeType, List<Long> hobbyIds, int sort, int publisherLimit, int sexLimit, int pageSize, int pageNumber, boolean isNeedShowLoading);
//
//        /**
//         * 我参加的活动接口
//         * @param userId 用户Id(当前操作的用户),
//         * @param toUserId 被查看的用户Id,
//         * @param pageSize 页面大小,
//         * @param pageNumber 第几页,
//         * @param isNeedShowLoading
//         */
//        void getJoinActionList(long userId, long toUserId, int pageSize, int pageNumber, boolean isNeedShowLoading);
//
//    }
//}