package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwenhua on 2016/10/13.
 */
public class HomeAdvertisementTabAdapter extends PagerAdapter {
    private List<View> viewList=new ArrayList<View>();
    public HomeAdvertisementTabAdapter(List<View> viewList) {
        this.viewList=viewList;
    }

    @Override
    public int getCount() {
        if(this.viewList.size()>1){
            //设置成最大，使用户看不到边界
            return Integer.MAX_VALUE;
        }else{
            //只有一个的情况
            return this.viewList.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //对ViewPager页号求模取出View列表中要显示的项
        position %= viewList.size();
        if (position<0){
            position = viewList.size()+position;
        }
        View view = viewList.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        position %= viewList.size();
//        container.removeView(viewList.get(position));
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }
}
