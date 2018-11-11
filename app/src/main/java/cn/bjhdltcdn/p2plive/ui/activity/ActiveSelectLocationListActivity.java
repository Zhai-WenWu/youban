package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.ActivityLocationInfo;
import cn.bjhdltcdn.p2plive.ui.adapter.StoreSearchLocationRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * 活动选择位置列表
 */
public class ActiveSelectLocationListActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener
///*OnGetPoiSearchResultListener,*/OnGetSuggestionResultListener
{
    private RecyclerView recycleView;
    private StoreSearchLocationRecyclerViewAdapter recyclerAdapter;
    private View emptyView;
    private int pageSize = 20, pageNum = 0;
    private TextView finishView, tipTextView;
    private LoadingView loadingView;
    // 下拉刷新
    private TwinklingRefreshLayout refreshLayout;
    private TitleFragment titleFragment;
    private long userId;
    private EditText searchEdit;
    private PoiSearch mpoiSearch;
    private int currenPosition;
    private ImageView delectTextImg;
    private boolean needShowLoading = true;
    private int type;//0:选择活动位置 1：选择店铺位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_select_location_list);
        Intent intent = getIntent();
        type = intent.getIntExtra(Constants.Fields.TYPE, 0);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        initView();
        setTitle();
    }

    public void initView() {
        searchEdit = findViewById(R.id.search_edittext);
        tipTextView = findViewById(R.id.tip_tv);
        delectTextImg = findViewById(R.id.delete_view);
        if (type == 1) {
            searchEdit.setHint("店铺位置");
            tipTextView.setText("请选择店铺位置，若未找到，可挑选离您店铺最近的位置");
        }
        searchEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    poi_Search(searchEdit.getText().toString());
//                    searchButtonProcess(searchEdit);


                    return true;
                }
                return false;
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = searchEdit.getText().toString();
                if (StringUtils.isEmpty(str)) {
                    delectTextImg.setVisibility(View.INVISIBLE);
                } else {
                    delectTextImg.setVisibility(View.VISIBLE);
                }
                if (charSequence.length() <= 0) {
                    return;
                }
                /*隐藏软键盘*/
//                InputMethodManager imm = (InputMethodManager) searchEdit
//                        .getContext().getSystemService(
//                                Context.INPUT_METHOD_SERVICE);
//                if (imm.isActive()) {
//                    imm.hideSoftInputFromWindow(
//                            searchEdit.getApplicationWindowToken(), 0);
//                }

                poi_Search(searchEdit.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        delectTextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
            }
        });
        emptyView = findViewById(R.id.empty_view);
        finishView = findViewById(R.id.finish_view);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
        recycleView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerAdapter = new StoreSearchLocationRecyclerViewAdapter(this);
        recycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(1));
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
        recycleView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                PoiItem poiItem = recyclerAdapter.getItem(position);
                Intent intent = new Intent();
                ActivityLocationInfo activityLocationInfo = new ActivityLocationInfo();
                activityLocationInfo.setAddr(poiItem.getSnippet());
                activityLocationInfo.setCity(poiItem.getCityName());
                activityLocationInfo.setLongitude(poiItem.getLatLonPoint().getLongitude() + "");
                activityLocationInfo.setLatitude(poiItem.getLatLonPoint().getLatitude() + "");
                activityLocationInfo.setDistrict(poiItem.getAdName());
//                activityLocationInfo.setProvince(suggestionInfo.province);
                intent.putExtra("location", activityLocationInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        loadingView = new LoadingView(getApplicationContext());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recycleView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(false);//灵活的设置是否禁用上下拉。
        refreshLayout.setEnableLoadmore(false);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
        refreshLayout.setVisibility(View.GONE);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNum = 0;
                poi_Search(searchEdit.getText().toString());
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {

                if (!loadingView.isOnLoadFinish()) {
                    goToNextPage();
                    refreshLayout.finishLoadmore();
                }
            }
        });
        emptyView.setVisibility(View.GONE);
    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        if (type == 1) {
            titleFragment.setTitle(R.string.title_shop_location);
        } else {
            titleFragment.setTitle(R.string.title_active_location);
        }
        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        titleFragment.getRightView().setEnabled(false);
    }

    /**
     * 关键字搜索
     *
     * @param str 关键字
     */
    private void poi_Search(String str) {
        PoiSearch.Query mPoiSearchQuery = new PoiSearch.Query(str, "", App.getInstance().getCity());
        mPoiSearchQuery.requireSubPois(true);   //true 搜索结果包含POI父子关系; false
        mPoiSearchQuery.setPageSize(10);
        mPoiSearchQuery.setPageNum(pageNum);
        PoiSearch poiSearch = new PoiSearch(ActiveSelectLocationListActivity.this, mPoiSearchQuery);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    public void goToNextPage() {
        pageNum++;
        poi_Search(searchEdit.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        ProgressDialogUtils.getInstance().hideProgressDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null) {
                List<PoiItem> poiItems = poiResult.getPois();
                if (pageNum == 0) {
                    recyclerAdapter.setList(poiItems);
                } else {
                    recyclerAdapter.addList(poiItems);
                }
                recyclerAdapter.notifyDataSetChanged();
                refreshLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
