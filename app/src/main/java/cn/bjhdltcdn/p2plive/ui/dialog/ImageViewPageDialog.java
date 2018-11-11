package cn.bjhdltcdn.p2plive.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.ui.adapter.ImageViewPageAdapter;


/**
 * Created by wenquan on 2015/11/9.
 */
public class ImageViewPageDialog extends DialogFragment {
    private View rootView;
    private ViewPager viewPager;
    private ImageViewPageAdapter adapter ;

    private List<Object> mList;
    private int position ;

    private TextView mTextView ;

    public static ImageViewPageDialog newInstance(List<Object> mList, int currentItem,boolean b) {
        ImageViewPageDialog f = new ImageViewPageDialog();
        Bundle args = new Bundle();
        if (mList instanceof ArrayList) {
            args.putSerializable(Constants.Action.ACTION_EXTRA, (ArrayList) mList);
        }else if (mList instanceof LinkedList) {
            args.putSerializable(Constants.Action.ACTION_EXTRA, (LinkedList) mList);
        }

        args.putInt(Constants.Action.ACTION_POSITION_EXTRA, currentItem);
        f.setArguments(args);

        return f;
    }

    public static ImageViewPageDialog newInstance(List<Image> mList, int currentItem) {
        ImageViewPageDialog f = new ImageViewPageDialog();
        Bundle args = new Bundle();
        if (mList instanceof ArrayList) {
            args.putSerializable(Constants.Action.ACTION_EXTRA, (ArrayList) mList);
        }else if (mList instanceof LinkedList) {
            args.putSerializable(Constants.Action.ACTION_EXTRA, (LinkedList) mList);
        }

        args.putInt(Constants.Action.ACTION_POSITION_EXTRA, currentItem);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_image_viewpage_layout, null);
        initView();
        return rootView;
    }


    private void initView(){

        mTextView = (TextView) rootView.findViewById(R.id.tile_text_view);

        Bundle bundle = getArguments();
        Object object = bundle.getSerializable(Constants.Action.ACTION_EXTRA);
        if (object instanceof ArrayList){
            mList = (ArrayList)bundle.getSerializable(Constants.Action.ACTION_EXTRA);
        }else if (object instanceof LinkedList){
            mList = (LinkedList)bundle.getSerializable(Constants.Action.ACTION_EXTRA);
        }
        position = bundle.getInt(Constants.Action.ACTION_POSITION_EXTRA, -1);

        viewPager = (ViewPager)rootView.findViewById(R.id.view_page);
        adapter = new ImageViewPageAdapter(mList,this);

        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position++;
                if(mList!=null){
                    mTextView.setText(position + "/" + mList.size());
                }else{
                    mTextView.setText(position + "/" + 0);
                }

                adapter.setPosition(position-1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(mList!=null){
            mTextView.setText((position <= 0 ? (position + 1) : position) + "/" + mList.size());
        }else
        {
            mTextView.setText((position <= 0 ? (position + 1) : position) + "/" +0);
        }
        viewPager.setCurrentItem(position);

    }

    @Override
    public void dismiss() {
        ImageViewPageDialog.super.dismissAllowingStateLoss();
    }

    public void show(FragmentManager manager) {
        show(manager, "dialog");
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            // 解决java.lang.IllegalStateException: Can not perform this action问题
            final FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }



}
