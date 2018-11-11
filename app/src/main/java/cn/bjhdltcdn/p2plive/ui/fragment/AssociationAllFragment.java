//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.orhanobut.logger.Logger;
//
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHobbyListResponse;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationTypeListActivity;
//import cn.bjhdltcdn.p2plive.ui.adapter.AllAssociationListAdapter;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//
///**
// * Created by Hu_PC on 2017/11/8.
// * 全部圈子
// */
//
//public class AssociationAllFragment extends BaseFragment implements BaseView {
//    private View rootView;
//    private RecyclerView recyclerView;
//    private AllAssociationListAdapter adapter;
//
//    private AssociationPresenter associationPresenter;
//
//    public AssociationPresenter getAssociationPresenter() {
//        if (associationPresenter == null) {
//            associationPresenter = new AssociationPresenter(this);
//        }
//        return associationPresenter;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_association_all, null);
//        }
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initView();
//    }
//
//    @Override
//    public void onVisible(boolean isInit) {
//
//        Logger.d("isInit == " + isInit);
//        if (isInit) {
//            getAssociationPresenter().getHobbyList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 1, 1,false);
//        }
//
//
//    }
//
//    private void initView() {
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
//        layoutManager.setAutoMeasureEnabled(true);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new AllAssociationListAdapter();
//        recyclerView.setAdapter(adapter);
//
//        adapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                HobbyInfo info = adapter.getList().get(position);
//                startActivity(new Intent(getActivity(), AssociationTypeListActivity.class).putExtra(Constants.KEY.KEY_OBJECT, info));
//            }
//        });
//
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (!isAdded()) {
//            return;
//        }
//
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            Utils.showToastShortTime(e.getMessage());
//            return;
//        }
//
//        if (InterfaceUrl.URL_GETHOBBYLIST.equals(apiName)) {
//            if (object instanceof GetHobbyListResponse) {
//                GetHobbyListResponse response = (GetHobbyListResponse) object;
//
//                if (response.getCode() == 200) {
//                    List<HobbyInfo> list = response.getHobbyList();
//
//                    // 添加学校圈子创建入口
//                    HobbyInfo hobbyInfo = new HobbyInfo();
//                    hobbyInfo.setHobbyId(-1);
//                    hobbyInfo.setHobbyName("校友创建");
//                    hobbyInfo.setHobbyDesc("离你最近，快来看看你的校友在玩些什么");
//                    list.add(0,hobbyInfo);
//
//                    // 添加学校圈子校友录入口
//                    HobbyInfo hobbyInfo1 = new HobbyInfo();
//                    hobbyInfo1.setHobbyId(-2);
//                    hobbyInfo1.setHobbyName("校友录");
//                    hobbyInfo1.setHobbyDesc("同学校友，所有学校的集中营");
//                    list.add(list.size(),hobbyInfo1);
//
//                    adapter.setList(list);
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//            }
//        }
//
//
//    }
//
//    @Override
//    public void showLoading() {
//        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//}
