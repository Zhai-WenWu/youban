//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
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
//import cn.bjhdltcdn.p2plive.ui.adapter.AllAssociationListAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.AssociationAllFragment;
//import cn.bjhdltcdn.p2plive.ui.fragment.PublishBottomLayoutFragment;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//
///**
// * 圈子全部列表
// */
//public class AssociationAllListActivity extends BaseActivity {
//
//
//    private AssociationAllFragment mFragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_association_all_list_layout);
//
//        setTitle();
//
//        mFragment = (AssociationAllFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
//        if (mFragment == null) {
//            mFragment = new AssociationAllFragment();
//        }
//
//        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.content_frame);
//
//        mFragment.onVisible(true);
//
//    }
//
//    private void setTitle() {
//        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle("全部");
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                onBackPressed();
//            }
//        });
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (mFragment != null) {
//            mFragment = null;
//        }
//    }
//}
