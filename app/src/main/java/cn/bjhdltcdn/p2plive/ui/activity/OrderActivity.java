package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeTabRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;

/**
 * Created by Zhai_PC on 2018/4/16.
 */

public class OrderActivity extends BaseActivity {

    private RecyclerView tabRecycleView;
    private RecyclerView rv_order_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle();
        initView();
    }

    private void initView() {
        HomeTabRecyclerViewAdapter homeTabRecyclerViewAdapter = new HomeTabRecyclerViewAdapter(this);
        tabRecycleView = (RecyclerView) findViewById(R.id.home_tab_recycler_view);
        rv_order_item = (RecyclerView) findViewById(R.id.rv_order_item);
        tabRecycleView.setHasFixedSize(true);
        LinearLayoutManager tabLinearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.HORIZONTAL, false);
        tabRecycleView.setLayoutManager(tabLinearlayoutManager);
        rv_order_item.setHasFixedSize(true);
        LinearLayoutManager itemLinearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        rv_order_item.setLayoutManager(itemLinearlayoutManager);
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("我的礼物");
    }
}
