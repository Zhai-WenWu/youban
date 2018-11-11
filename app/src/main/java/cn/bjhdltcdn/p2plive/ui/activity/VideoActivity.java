//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.os.Bundle;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.FollowPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetHomeNearbyPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetRecommendListPresenter;
//import cn.bjhdltcdn.p2plive.ui.adapter.FindListAdapter;
//import cn.bjhdltcdn.p2plive.ui.fragment.FindFragment;
//import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
//
///**
// * Created by ZHAI on 2018/4/4.
// */
//
//public class VideoActivity extends BaseActivity {
//    private FindFragment findFragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_layout);
//        if (findFragment == null) {
//            findFragment = new FindFragment();
//        }
//        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), findFragment, R.id.content_frame);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (findFragment != null) {
//            findFragment = null;
//        }
//
//    }
//}
