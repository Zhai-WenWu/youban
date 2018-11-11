package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateShopDetailEvent;
import cn.bjhdltcdn.p2plive.httpresponse.GetRecommendMerchantListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetStoreLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetStoreListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JudgeCreateStoreAuthResponse;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetRecommendListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.ShopRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.HomeBannerFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.StoreFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.ShopCategoryPopWindow;

/**
 *
 * 校园店列表界面
 */
public class StoreActivity extends BaseActivity{
    private StoreFragment shopFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_pay_entry_layout);
        shopFragment = (StoreFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (shopFragment == null) {
                shopFragment = new StoreFragment();
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), shopFragment, R.id.content_frame);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (shopFragment != null) {
            shopFragment = null;
        }

    }
}
