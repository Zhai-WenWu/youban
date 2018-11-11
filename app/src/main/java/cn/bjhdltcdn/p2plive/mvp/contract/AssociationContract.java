package cn.bjhdltcdn.p2plive.mvp.contract;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.PostLabelInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by xiawenquan on 17/11/17.
 */

public interface AssociationContract {


    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {


        /**
         * 搜索帖子标签接口
         *
         * @param userId     用户Id
         * @param content    搜索内容
         * @param pageSize   每页大小,
         * @param pageNumber 第几页,
         */
        void searchLabelByContent(long userId, String content, int pageSize, int pageNumber);

        /**
         * 发布帖子接口
         *
         * @param userId        用户Id
         * @param toUserId      被跟拍用户id
         * @param content       帖子内容
         * @param isAnonymous   是否匿名发布(1-->匿名,2-->不匿名)
         * @param topicType     帖子类型(1-->普通图文，2--->视频)
         * @param videoUrl      帖子视频路径(类型为视频使用)
         * @param videoImageUrl 帖子图片地址(类型为视频使用)
         * @param postLabelList {postLabelInfo}，{postLabelInfo}帖子标签对象,
         * @param isLoading     收到显示加载框
         * @param postId        帖子Id(跟拍时传入值,其他默认0)
         * @param storeId       店铺Id(默认0),
         * @param isRecurit     是否为招聘信息(0 不是,1 是)
         */
        void savePost(long userId, long toUserId, String content, int isAnonymous, int topicType, String videoUrl, String videoImageUrl, List<PostLabelInfo> postLabelList, boolean isLoading, long postId, long storeId,int isRecurit);
    }
}
