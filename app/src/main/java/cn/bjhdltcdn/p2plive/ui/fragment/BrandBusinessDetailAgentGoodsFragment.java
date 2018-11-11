package cn.bjhdltcdn.p2plive.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.bjhdltcdn.p2plive.R;

/**
 * Created by Administrator on 2018/8/28.
 */

public class BrandBusinessDetailAgentGoodsFragment extends BaseFragment {

    private View rootView;

    @Override
    protected void onVisible(boolean isInit) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_brandbusiness_detail_agentgoods, container, false);
        return rootView;
    }
}
