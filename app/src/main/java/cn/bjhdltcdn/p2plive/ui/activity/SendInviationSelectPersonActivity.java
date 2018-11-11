package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.ui.adapter.AssociationBlackListTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.SendInviationSelectAlumnusFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.SendInviationSelectCityFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;

/**
 * 我的礼物
 */
public class SendInviationSelectPersonActivity extends BaseActivity {
    private CustomViewPager mViewPager;
    private TabLayout mTabLayout;
    private AssociationBlackListTabFragmentAdapter adapter;
    private TextView okTextView;
    private int maxSelectNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_inviation_select_person);
        maxSelectNum=getIntent().getIntExtra("selectmaxnum",0);
        setTitle();
        init();
    }

    private void init() {
        mViewPager = findViewById(R.id.pager_gift);
        mTabLayout = findViewById(R.id.tabs);

        adapter = new AssociationBlackListTabFragmentAdapter(getSupportFragmentManager());
        final SendInviationSelectAlumnusFragment sendInviationSelectAlumnusFragment = new SendInviationSelectAlumnusFragment();
        adapter.addFragment(sendInviationSelectAlumnusFragment, "校友");

        final SendInviationSelectCityFragment sendInviationSelectCityFragment = new SendInviationSelectCityFragment();
        adapter.addFragment(sendInviationSelectCityFragment, "同城");
        sendInviationSelectAlumnusFragment.setMaxSelectNum(maxSelectNum);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setIsCanScroll(true);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            View tabView = adapter.getTabView(i);
            tab.setCustomView(tabView);
        }
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
                textView.setTextColor(getResources().getColor(R.color.color_333333));
                textView.setBackgroundColor(getResources().getColor(R.color.color_ffee00));

                mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    sendInviationSelectAlumnusFragment.setMaxSelectNum(maxSelectNum);
                }else if(tab.getPosition()==1)
                {
                    sendInviationSelectCityFragment.setMaxSelectNum(maxSelectNum);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
                textView.setTextColor(getResources().getColor(R.color.color_999999));
                textView.setBackgroundColor(getResources().getColor(R.color.color_ffffff));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        okTextView=findViewById(R.id.ok_text_view);
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到指定人筛选界面
                int destineNum=sendInviationSelectAlumnusFragment.getSelectPerson().size()+sendInviationSelectCityFragment.getSelectPerson().size();
                if(destineNum<3){
                    Utils.showToastShortTime("最少选定3个用户");
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("destinenum",destineNum );
                    intent.putExtra(Constants.Fields.SELECT_ALUMNUS_PERSON, (Serializable) sendInviationSelectAlumnusFragment.getSelectPerson());
                    intent.putExtra(Constants.Fields.SELECT_CITY_PERSON, (Serializable) sendInviationSelectCityFragment.getSelectPerson());
                    setResult(RESULT_OK, intent);
                    finish(); //结束当前的activity的生命周期
                }

            }
        });
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("指定人");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
