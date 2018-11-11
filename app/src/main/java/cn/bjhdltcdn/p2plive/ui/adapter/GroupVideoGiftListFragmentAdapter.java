package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;

/**
 * 多人视频礼物分页显示
 */

public class GroupVideoGiftListFragmentAdapter extends PagerAdapter {

    private int pageCount;
    private List<Props> list;

    private List<GroupVideoGiftAdapter> itemAdapterList;

    private GropVideoGiftAdapterItemListener onItemListener;

    public void setOnItemListener(GropVideoGiftAdapterItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public GroupVideoGiftListFragmentAdapter(int pageCount, List<Props> list) {
        this.pageCount = pageCount;
        this.list = list;
    }

    public List<Props> getList() {
        return list;
    }

    public List<GroupVideoGiftAdapter> getItemAdapterList() {
        return itemAdapterList;
    }

    /**
     * 释放资源
     */
    public void setReleaseResource() {

//        if (list != null) {
//            list.clear();
//        }
//        list = null;

        if (itemAdapterList != null) {
            itemAdapterList.clear();
        }
        itemAdapterList = null;

        if (onItemListener != null) {
            onItemListener = null;
        }


    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View rootView = View.inflate(App.getInstance(), R.layout.fragment_group_video_gift_list_layout, null);
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(App.getInstance(), 4, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final List<Props> subList = list.subList(position * 8, list.size() > (position + 1) * 8 ? (position + 1) * 8 : list.size());
        final GroupVideoGiftAdapter adapter = new GroupVideoGiftAdapter(subList);
        // 默认第一页的第一item被选中
        if (position == 0) {
            adapter.setDefaultSelectedItem(0);
            adapter.setItemSelectPosition(0);
        }

        recyclerView.setAdapter(adapter);
        if (itemAdapterList == null) {
            itemAdapterList = new ArrayList<>(1);
        }
        itemAdapterList.add(adapter);

        adapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (onItemListener != null) {
                    if (adapter.getItemCount() > 0) {
                        // 清除之前点击的item状态
                        if (adapter.getItemSelectPosition() > -1 && adapter.getItemSelectPosition() < adapter.getItemCount()) {
                            Props itemSelectProps = adapter.getList().get(adapter.getItemSelectPosition());
                            itemSelectProps.setSelected(false);
                            adapter.notifyItemChanged(adapter.getItemSelectPosition());
                        }

                        Props props = adapter.getList().get(position);
                        // 点击回调监听器
                        onItemListener.onItemClick(props);
                        // 刷新礼物列表
                        props.setSelected(true);
                        adapter.notifyItemChanged(position);
                        adapter.setItemSelectPosition(position);


                    }

                }
            }
        });
        container.addView(rootView);

        return rootView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    public interface GropVideoGiftAdapterItemListener {
        void onItemClick(Props props);
    }
}
