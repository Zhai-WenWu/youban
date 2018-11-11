package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.CommentPraiseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOrderEvaluateListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveOrderEvaluateResponse;
import cn.bjhdltcdn.p2plive.model.Comment;
import cn.bjhdltcdn.p2plive.model.EvaluateCountInfo;
import cn.bjhdltcdn.p2plive.model.ShopComment;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.OrderPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.GoodsCommentRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.HomePostLabelRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.GlideCacheUtil;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;

/**
 * Created by Hu_PC on 2017/11/8.
 * 商品评价列表fragment
 */

public class EvaluateRecycleFragment extends BaseFragment implements BaseView {
    private View rootView;
    private ShopDetailActivity mActivity;
    private RefreshLayout refreshLayout;
    private GetStoreListPresenter getStoreListPresenter;
    private PostCommentListPresenter postPresenter;
    private OrderPresenter orderPresenter;
    private RecyclerView labelRecycleView,recycleView;
    private HomePostLabelRecyclerViewAdapter homePostLabelRecyclerViewAdapter;
    private GoodsCommentRecycleAdapter goodsCommentRecycleAdapter;
    private View emptyView;
    private int currenPosition,selectLabelPosition;
    private long userId;
    private int pageSize=10,pageNumber=1;
    private long storeId,storeUserId;
    private List<EvaluateCountInfo> evaluateCountInfoList;

    //评论用的字段
    private long productId,toUserId,parentId,commentParentId;
    private boolean payloads;//是否刷新评论列表的图片以及视频


    public GetStoreListPresenter getGetStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    private PostCommentListPresenter getPostPresenter() {

        if (postPresenter == null) {
            postPresenter = new PostCommentListPresenter(this);
        }
        return postPresenter;
    }

    public OrderPresenter getOrderPresenter() {
        if (orderPresenter == null) {
            orderPresenter = new OrderPresenter(this);
        }
        return orderPresenter;
    }

    public void setStoreId(long storeId,long storeUserId) {
        this.storeId = storeId;
        this.storeUserId=storeUserId;
        if(goodsCommentRecycleAdapter==null){
            goodsCommentRecycleAdapter = new GoodsCommentRecycleAdapter(mActivity);
            goodsCommentRecycleAdapter.setStoreUserId(storeUserId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (ShopDetailActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_evaluate_recycle, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
    }

    @Override
    protected void onVisible(boolean isInit) {
        if(isInit){
            userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
            if (evaluateCountInfoList == null || evaluateCountInfoList.size() < 1) {
                getGetStoreListPresenter().getOrderEvaluateList(userId,storeId,0,pageSize,pageNumber);
            }
        }
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void getOrderEvaluateList(int pageNumber){
        this.pageNumber=pageNumber;
        if(evaluateCountInfoList!=null&&evaluateCountInfoList.size()>0){
            getGetStoreListPresenter().getOrderEvaluateList(userId,storeId,evaluateCountInfoList.get(selectLabelPosition).getType(),pageSize,pageNumber);
        }else{
            getGetStoreListPresenter().getOrderEvaluateList(userId,storeId,0,pageSize,pageNumber);
        }
    }

    public void comment(String content){
        getOrderPresenter().saveOrderEvaluate(userId,0,productId,toUserId,userId,parentId,commentParentId,2,0,content,0,"","");
    }


    private void initView() {
//        refreshLayout = (RefreshLayout) rootView.findViewById(R.id.refreshLayout);
//        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                getOrderEvaluateList(pageNumber);
//            }
//        });
//        refreshLayout.setEnableLoadMore(false);
//        refreshLayout.setEnableAutoLoadMore(false);
//        refreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹

        emptyView = rootView.findViewById(R.id.empty_view);
        recycleView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recycleView.setHasFixedSize(true);
        if(goodsCommentRecycleAdapter==null){
            goodsCommentRecycleAdapter = new GoodsCommentRecycleAdapter(mActivity);
            goodsCommentRecycleAdapter.setStoreUserId(storeUserId);
        }else{
            goodsCommentRecycleAdapter.setActivity(mActivity);
        }
        recycleView.setHasFixedSize(true);
        LinearLayoutManager itemLinearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        recycleView.setLayoutManager(itemLinearlayoutManager);
        recycleView.setAdapter(goodsCommentRecycleAdapter);
        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== SCROLL_STATE_DRAGGING ){
                    if(mActivity.getSendViewVisible()==View.VISIBLE)
                    {
                        mActivity.setSendViewVisible(View.GONE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });
        goodsCommentRecycleAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到商品详情页
                currenPosition = position;

            }
        });
        goodsCommentRecycleAdapter.setViewClick(new GoodsCommentRecycleAdapter.ViewClick() {
            @Override
            public void onPraise(long commentId, int type, int position) {
                getPostPresenter().commentPraise(commentId, type, userId, 5);
            }

            @Override
            public void onComment(long commentParentid,long parentid, int type,long productid,long toUserid,String toUserNickName,int position){
                //弹出输入框
                if(commentParentid==parentid){
                    mActivity.comment("");
                }else{
                    mActivity.comment(toUserNickName);
                }

                productId=productid;
                toUserId=toUserid;
                parentId=parentid;
                commentParentId=commentParentid;
            }

            @Override
            public void attentionView(int type, long userId, int position) {

            }

            @Override
            public void moreImg(int type, int position) {

            }

            @Override
            public void moreComment(long commentId) {

            }

            @Override
            public void ReplyListComment(Comment comment) {

            }

            @Override
            public void sortViewClick() {

            }
        });
        emptyView.setVisibility(View.VISIBLE);

        labelRecycleView = rootView.findViewById(R.id.recycler_label);
        homePostLabelRecyclerViewAdapter = new HomePostLabelRecyclerViewAdapter(this.getActivity());
        labelRecycleView.setHasFixedSize(true);
        homePostLabelRecyclerViewAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mActivity.getSendViewVisible()==View.VISIBLE)
                {
                    mActivity.setSendViewVisible(View.GONE);
                }
                selectLabelPosition = position;
                Object o=homePostLabelRecyclerViewAdapter.getItem(position);
                if(o instanceof EvaluateCountInfo){
                    EvaluateCountInfo evaluateCountInfo = (EvaluateCountInfo) o;
                    evaluateCountInfo.setCheck(true);
                    for (int i = 0; i < homePostLabelRecyclerViewAdapter.getList().size(); i++) {
                        Object o1=homePostLabelRecyclerViewAdapter.getList().get(i);
                        if(o1 instanceof EvaluateCountInfo) {
                            EvaluateCountInfo evaluateCountInfo1= (EvaluateCountInfo) o1;
                            if (evaluateCountInfo1.getType() != evaluateCountInfo.getType()) {
                                evaluateCountInfo1.setCheck(false);
                            }
                        }
                    }
                    homePostLabelRecyclerViewAdapter.notifyDataSetChanged();
                    getOrderEvaluateList(1);
                }


            }
        });
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            if (pageNumber == 1) {
                if(mActivity!=null){
                    mActivity.finishRefresh(false);
                }
            } else {
                if(mActivity!=null) {
                    mActivity.finishLoadMore(false);
//                    refreshLayout.finishLoadMore(false);
                }
            }
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_GETORDEREVALUATELIST)) {
            if (object instanceof GetOrderEvaluateListResponse) {
                GetOrderEvaluateListResponse getOrderEvaluateListResponse = (GetOrderEvaluateListResponse) object;
                if (pageNumber == 1) {
                    if(mActivity!=null){
                        mActivity.finishRefresh(true);
                    }
                } else {
                    if(mActivity!=null) {
                        mActivity.finishLoadMore(true);
//                        refreshLayout.finishLoadMore();
                    }
                }
                if (getOrderEvaluateListResponse.getCode() == 200) {
                    //评论标签列表
                    if(evaluateCountInfoList==null||evaluateCountInfoList.size()<=0){
                        evaluateCountInfoList = getOrderEvaluateListResponse.getTypeList();
                        if (evaluateCountInfoList != null && evaluateCountInfoList.size() > 0) {
                            evaluateCountInfoList.get(0).setCheck(true);
                            List<Object> list = new ArrayList<Object>();
                            list.addAll(evaluateCountInfoList);
                            homePostLabelRecyclerViewAdapter.setList(list);
                            homePostLabelRecyclerViewAdapter.notifyDataSetChanged();
                            if (homePostLabelRecyclerViewAdapter.getItemCount() > 0) {
                                GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), evaluateCountInfoList.size());
                                labelRecycleView.setLayoutManager(layoutManager);
                                labelRecycleView.setAdapter(homePostLabelRecyclerViewAdapter);
                            } else {
                                labelRecycleView.setVisibility(View.GONE);
                            }
                        }
                    }

                    //评论列表
                    List<ShopComment> list = getOrderEvaluateListResponse.getList();
                    if(list!=null&&list.size()>0){
                        if(pageNumber==1){
                            if(payloads){
                                goodsCommentRecycleAdapter.setList(list,payloads);
                                payloads=false;
                            }else{
                                goodsCommentRecycleAdapter.setList(list,false);
                            }

                        }else{
                            goodsCommentRecycleAdapter.addList(list);
                        }
                    }else{
                        goodsCommentRecycleAdapter.removeAllList();
                    }
                    if(getOrderEvaluateListResponse.getTotal()<=pageNumber*pageSize){
                        //没有更多数据时  下拉刷新不可用
                        mActivity.setEnableLoadMore(false,1);
//                        refreshLayout.setEnableLoadMore(false);
                    }else
                    {
                        //有更多数据时  下拉刷新才可用
                        mActivity.setEnableLoadMore(true,1);
//                        refreshLayout.setEnableLoadMore(true);
                        pageNumber++;
                    }

                    if (getOrderEvaluateListResponse.getTotal() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        ((TextView)emptyView.findViewById(R.id.empty_textView)).setText(getOrderEvaluateListResponse.getBlankHint());
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                } else {
                    Utils.showToastShortTime(getOrderEvaluateListResponse.getMsg());
                }
            }
        }else if (InterfaceUrl.URL_COMMENTPRAISE.equals(apiName)) {

            if (object instanceof CommentPraiseResponse) {
                CommentPraiseResponse response = (CommentPraiseResponse) object;
                Utils.showToastShortTime(response.getMsg());
            }

        }if (apiName.equals(InterfaceUrl.URL_SAVEORDEREVALUATE)) {
            if (object instanceof SaveOrderEvaluateResponse) {
                SaveOrderEvaluateResponse saveOrderEvaluateResponse = (SaveOrderEvaluateResponse) object;
                Utils.showToastShortTime(saveOrderEvaluateResponse.getMsg());
                if(saveOrderEvaluateResponse.getCode()==200){
                    //刷新评论列表
                    payloads=true;
                    getOrderEvaluateList(1);
                }
            }
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
