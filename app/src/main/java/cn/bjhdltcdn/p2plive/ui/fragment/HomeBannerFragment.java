package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.handler.AdvertisementHandler;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.ProductImage;
import cn.bjhdltcdn.p2plive.model.RecommendInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreImage;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeAdvertisementTabAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

/**
 * Created by Hu_PC on 2017/11/8.
 * 首页附近
 */

public class HomeBannerFragment extends BaseFragment{
    private View rootView;
    private AppCompatActivity mActivity;
    private Long userId;
    private RequestOptions options;
    public CustomViewPager viewPager;
    private int previousPosition = 0;
    public AdvertisementHandler handler = new AdvertisementHandler(new WeakReference<BaseFragment>(this));
    private RelativeLayout advertisementLayout;
    private ImageView defaultImgeView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.home_ad_header_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);

    }

    @Override
    protected void onVisible(boolean isInit) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        advertisementLayout=rootView.findViewById(R.id.layout_advertisement);
        defaultImgeView=rootView.findViewById(R.id.default_image_view);
    }

    public void setHeaderViewData(List<RecommendInfo> list) {
        try {
            options = new RequestOptions()
                    .centerCrop()
                    .transform(new GlideRoundTransform(9))
                    .placeholder(R.mipmap.error_bg)
                    .error(R.mipmap.error_bg);
            List<RecommendInfo> arrayList = null;
            final LinearLayout llPointGroup;
            if (arrayList == null) {
                arrayList = new ArrayList<RecommendInfo>();
            }
            arrayList.clear();
            if (list != null && list.size() > 0) {
                arrayList.addAll(list);
            } else {
                return;
            }
            List<View> viewList = new ArrayList<View>();
            viewPager = (CustomViewPager) rootView.findViewById(R.id.advertisement_head_view_page);
            llPointGroup = (LinearLayout) rootView.findViewById(R.id.ll_point_group);
            llPointGroup.removeAllViews();
            View advertisementView;
            String recommendImgUrl = "";
            for (int i = 0; i < list.size(); i++) {
                advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
                ImageView advertisementImage = (ImageView) advertisementView.findViewById(R.id.advertisement_image);
                final RecommendInfo recommendInfo = arrayList.get(i);
                recommendImgUrl = recommendInfo.getImgUrl();
                Glide.with(App.getInstance()).asBitmap().load(recommendImgUrl).apply(options).into(advertisementImage);
                viewList.add(advertisementView);
                advertisementView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String gotoUrl = recommendInfo.getGotoUrl();
                        if (!TextUtils.isEmpty(gotoUrl)) {
                            Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
                            intent.putExtra(Constants.Action.ACTION_EXTRA, 6);
                            intent.putExtra(Constants.KEY.KEY_URL, gotoUrl + "?userId=" + userId);
                            startActivity(intent);
                        }
                    }
                });
                if (list.size() == 1) {
                    break;
                }
                //每循环一次需要向LinearLayout中添加一个点的view对象
                View v = new View(App.getInstance());
                v.setBackgroundResource(R.drawable.point_bg);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
                params.leftMargin = 9;
                params.rightMargin = 9;
                v.setLayoutParams(params);
                v.setEnabled(false);
                llPointGroup.addView(v);
            }

            HomeAdvertisementTabAdapter homeAdvertisementTabAdapter = new HomeAdvertisementTabAdapter(viewList);
            viewPager.setAdapter(homeAdvertisementTabAdapter);
            viewPager.setIsCanScroll(false);
            viewPager.setCurrentItem(0);
            if (list.size() > 1) {
                llPointGroup.getChildAt(previousPosition).setEnabled(true);
                //开始轮播效果
                handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
            }

            final List<RecommendInfo> finalArrayList = arrayList;
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int arg1) {
                    int position = arg1 % finalArrayList.size();
                    // 把当前选中的点给切换了, 还有描述信息也切换
                    llPointGroup.getChildAt(previousPosition).setEnabled(false);
                    llPointGroup.getChildAt(position).setEnabled(true);
                    // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                    previousPosition = position;
                    handler.sendMessage(Message.obtain(handler, AdvertisementHandler.MSG_PAGE_CHANGED, arg1, 0));
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                //覆写该方法实现轮播效果的暂停和恢复
                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // 当页面的状态改变时将调用此方法
                    //arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
                    Log.d("onPageScrollStateChange", arg0 + "");
                    switch (arg0) {
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            // 正在拖动页面时执行此处
                            handler.sendEmptyMessage(AdvertisementHandler.MSG_KEEP_SILENT);
                            break;
                        case ViewPager.SCROLL_STATE_IDLE:
                            // 未拖动页面时执行此处
                            handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
                            break;
                        default:
                            break;
                    }
                }

            });


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) advertisementLayout.getLayoutParams();
            params.height = PlatformInfoUtils.getWidthOrHeight(mActivity)[0] * 30 / 75;
            advertisementLayout.setLayoutParams(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHeaderStoreData(List<StoreDetail> list) {
        try {
            if(list==null||list.size()<=0){
                Utils.ImageViewDisplayByUrl(R.mipmap.store_default_bg, defaultImgeView);
                defaultImgeView.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) advertisementLayout.getLayoutParams();
                params.height = PlatformInfoUtils.getWidthOrHeight(mActivity)[0] * 45 / 75;
                advertisementLayout.setLayoutParams(params);
                return;
            }
            defaultImgeView.setVisibility(View.GONE);
            options = new RequestOptions()
                    .centerCrop()
                    .transform(new GlideRoundTransform(9))
                    .placeholder(R.mipmap.error_bg)
                    .error(R.mipmap.error_bg);
            List<StoreDetail> arrayList = null;
            final LinearLayout llPointGroup;
            if (arrayList == null) {
                arrayList = new ArrayList<StoreDetail>();
            }
            arrayList.clear();
            if (list != null && list.size() > 0) {
                arrayList.addAll(list);
            } else {
                return;
            }
            List<View> viewList = new ArrayList<View>();
            viewPager = (CustomViewPager) rootView.findViewById(R.id.advertisement_head_view_page);
            llPointGroup = (LinearLayout) rootView.findViewById(R.id.ll_point_group);
            llPointGroup.removeAllViews();
            View advertisementView;
            String recommendImgUrl = "";
            for (int i = 0; i < list.size(); i++) {
                advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
                ImageView advertisementImage = (ImageView) advertisementView.findViewById(R.id.advertisement_image);
                ImageView videoPlayImg= (ImageView) advertisementView.findViewById(R.id.play_image_view);
                final StoreDetail storeDetail = arrayList.get(i);
                List<StoreImage> storeImageList=storeDetail.getStoreInfo().getStoreImageList();
                StoreImage storeImage = null;
                if(storeImageList!=null&&storeImageList.size()>0)
                {
                    storeImage=storeImageList.get(0);
                    if(storeImage.getImageType()==2){
                        videoPlayImg.setVisibility(View.VISIBLE);
                        recommendImgUrl = storeImage.getVideoImageUrl();
                    }else
                    {
                        videoPlayImg.setVisibility(View.GONE);
                        recommendImgUrl = storeImage.getImageUrl();
                    }
                }
                Utils.ImageViewDisplayByUrl(recommendImgUrl, advertisementImage);
                viewList.add(advertisementView);
                final StoreImage finalStoreImage = storeImage;
                advertisementView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(finalStoreImage.getImageType()==2){
                            //跳转到视频播放界面
                            Intent intent = new Intent(mActivity, VideoPlayFullScreenActivity.class);
                            intent.putExtra(Constants.Fields.VIDEO_PATH, finalStoreImage.getVideoUrl());
                            intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, finalStoreImage.getVideoImageUrl());
                            intent.putExtra("comeInType", 1);
                            intent.putExtra("storeId",storeDetail.getStoreInfo().getStoreId());
                            mActivity.startActivity(intent);
                        }else
                        {
                            //跳转到店铺详情
                            Intent intent=new Intent(getActivity(), ShopDetailActivity.class);
                            intent.putExtra(Constants.Fields.STORE_ID,storeDetail.getStoreInfo().getStoreId());
                            startActivity(intent);
                        }

                    }
                });
                if (list.size() == 1) {
                    break;
                }
                //每循环一次需要向LinearLayout中添加一个点的view对象
                View v = new View(App.getInstance());
                v.setBackgroundResource(R.drawable.point_bg);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
                params.leftMargin = 9;
                params.rightMargin = 9;
                v.setLayoutParams(params);
                v.setEnabled(false);
                llPointGroup.addView(v);
            }

            HomeAdvertisementTabAdapter homeAdvertisementTabAdapter = new HomeAdvertisementTabAdapter(viewList);
            viewPager.setAdapter(homeAdvertisementTabAdapter);
            viewPager.setIsCanScroll(false);
            viewPager.setCurrentItem(0);
            if (list.size() > 1) {
                llPointGroup.getChildAt(previousPosition).setEnabled(true);
                //开始轮播效果
                handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
            }

            final List<StoreDetail> finalArrayList = arrayList;
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int arg1) {
                    int position = arg1 % finalArrayList.size();
                    // 把当前选中的点给切换了, 还有描述信息也切换
                    llPointGroup.getChildAt(previousPosition).setEnabled(false);
                    llPointGroup.getChildAt(position).setEnabled(true);
                    // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                    previousPosition = position;
                    handler.sendMessage(Message.obtain(handler, AdvertisementHandler.MSG_PAGE_CHANGED, arg1, 0));
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                //覆写该方法实现轮播效果的暂停和恢复
                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // 当页面的状态改变时将调用此方法
                    //arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
                    Log.d("onPageScrollStateChange", arg0 + "");
                    switch (arg0) {
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            // 正在拖动页面时执行此处
                            handler.sendEmptyMessage(AdvertisementHandler.MSG_KEEP_SILENT);
                            break;
                        case ViewPager.SCROLL_STATE_IDLE:
                            // 未拖动页面时执行此处
                            handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
                            break;
                        default:
                            break;
                    }
                }

            });


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) advertisementLayout.getLayoutParams();
            params.height = PlatformInfoUtils.getWidthOrHeight(mActivity)[0] * 45 / 75;
            advertisementLayout.setLayoutParams(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStoreDetailHeadData(List<StoreImage> list) {
        try {
            if(list==null||list.size()<=0){
                Utils.ImageViewDisplayByUrl(R.mipmap.store_default_bg, defaultImgeView);
                defaultImgeView.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) advertisementLayout.getLayoutParams();
                params.height = PlatformInfoUtils.getWidthOrHeight(mActivity)[0] * 45 / 75;
                advertisementLayout.setLayoutParams(params);
                return;
            }
            defaultImgeView.setVisibility(View.GONE);
            options = new RequestOptions()
                    .centerCrop()
                    .transform(new GlideRoundTransform(9))
                    .placeholder(R.mipmap.error_bg)
                    .error(R.mipmap.error_bg);
            List<Object> arrayList = null;
            final LinearLayout llPointGroup;
            if (arrayList == null) {
                arrayList = new ArrayList<Object>();
            }
            arrayList.clear();
            if (list != null && list.size() > 0) {
                arrayList.addAll(list);
            }
            List<View> viewList = new ArrayList<View>();
            viewPager = (CustomViewPager) rootView.findViewById(R.id.advertisement_head_view_page);
            RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            llPointGroup = (LinearLayout) rootView.findViewById(R.id.ll_point_group);
            llPointGroup.removeAllViews();
            View advertisementView;
            String activeImgUrl = "";
            final List<Object> finalArrayList=new ArrayList<>();
            boolean hasVideoImg=false;
            for (int i = 0; i < arrayList.size(); i++) {
                advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
                ImageView advertisementImage = (ImageView) advertisementView.findViewById(R.id.advertisement_image);
                ImageView videoPlayImg= (ImageView) advertisementView.findViewById(R.id.play_image_view);
                Object o=arrayList.get(i);
                StoreImage image = null;
                if(o instanceof StoreImage){
                    image= (StoreImage) o;
                }
                if(image.getImageType()==2){
                    videoPlayImg.setVisibility(View.VISIBLE);
                    if(StringUtils.isEmpty(image.getVideoImageUrl())){
                        activeImgUrl=image.getVideoUrl();
                    }else{
                        activeImgUrl = image.getVideoImageUrl();
                    }
                }else{
                    videoPlayImg.setVisibility(View.GONE);
                    activeImgUrl = image.getImageUrl();
                }
                Utils.ImageViewDisplayByUrl(activeImgUrl, advertisementImage);
                viewList.add(advertisementView);
                if(i==0){
                    if(image.getImageType()==2){
                        hasVideoImg=true;
                        finalArrayList.addAll(arrayList);
                        finalArrayList.remove(0);
                    }else
                    {
                        finalArrayList.addAll(arrayList);
                    }
                }
                final int final1 = i;
                final StoreImage finalImage = image;
                final boolean finalHasVideoImg = hasVideoImg;
                advertisementImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(finalImage.getImageType()==2){
                            //跳转到视频播放界面
                            Intent intent = new Intent(mActivity, VideoPlayFullScreenActivity.class);
                            intent.putExtra(Constants.Fields.VIDEO_PATH, finalImage.getVideoUrl());
                            intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, finalImage.getVideoImageUrl());
                            mActivity.startActivity(intent);
                        }else{
                            //跳转到查看大图界面
                            if(finalHasVideoImg){
                                ImageViewPageDialog.newInstance(finalArrayList, final1-1,true).show(getActivity().getSupportFragmentManager());
                            }else{
                                ImageViewPageDialog.newInstance(finalArrayList, final1,true).show(getActivity().getSupportFragmentManager());
                            }
                        }
                    }
                });
                if (arrayList.size() == 1) {
                    break;
                }
                //每循环一次需要向LinearLayout中添加一个点的view对象
                View v = new View(App.getInstance());
                v.setBackgroundResource(R.drawable.point_bg);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
                params.leftMargin = 9;
                params.rightMargin = 9;
                v.setLayoutParams(params);
                v.setEnabled(false);
                llPointGroup.addView(v);
            }
            HomeAdvertisementTabAdapter homeAdvertisementTabAdapter = new HomeAdvertisementTabAdapter(viewList);
            viewPager.setAdapter(homeAdvertisementTabAdapter);
            viewPager.setIsCanScroll(false);
            viewPager.setCurrentItem(0);
            if (arrayList.size() > 1) {
                llPointGroup.getChildAt(previousPosition).setEnabled(true);
                //开始轮播效果
                handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
            }

            final List<Object> finalArrayList1=arrayList;
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int arg1) {
                    int position = arg1 % finalArrayList1.size();
                    // 把当前选中的点给切换了, 还有描述信息也切换
                    if (llPointGroup != null) {
                        if (llPointGroup.getChildAt(previousPosition) != null) {
                            llPointGroup.getChildAt(previousPosition).setEnabled(false);
                        }
                        if (llPointGroup.getChildAt(position) != null) {
                            llPointGroup.getChildAt(position).setEnabled(true);
                        }
                    }
                    // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                    previousPosition = position;
                    handler.sendMessage(Message.obtain(handler, AdvertisementHandler.MSG_PAGE_CHANGED, arg1, 0));
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                //覆写该方法实现轮播效果的暂停和恢复
                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // 当页面的状态改变时将调用此方法
                    //arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
                    switch (arg0) {
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            // 正在拖动页面时执行此处
                            handler.sendEmptyMessage(AdvertisementHandler.MSG_KEEP_SILENT);
                            break;
                        case ViewPager.SCROLL_STATE_IDLE:
                            // 未拖动页面时执行此处
                            handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
                            break;
                        default:
                            break;
                    }
                }

            });


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) advertisementLayout.getLayoutParams();
            params.height = PlatformInfoUtils.getWidthOrHeight(mActivity)[0] * 48 / 75;
            advertisementLayout.setLayoutParams(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setImageListData(List<Image> list) {
        try {
            options = new RequestOptions()
                    .centerCrop()
                    .transform(new GlideRoundTransform(9))
                    .placeholder(R.mipmap.error_bg)
                    .error(R.mipmap.error_bg);
            List<Object> arrayList = null;
            final LinearLayout llPointGroup;
            if (arrayList == null) {
                arrayList = new ArrayList<Object>();
            }
            arrayList.clear();
            if (list != null && list.size() > 0) {
                arrayList.addAll(list);
            }
            List<View> viewList = new ArrayList<View>();
            viewPager = (CustomViewPager) rootView.findViewById(R.id.advertisement_head_view_page);
            llPointGroup = (LinearLayout) rootView.findViewById(R.id.ll_point_group);
            llPointGroup.removeAllViews();
            View advertisementView;
            String activeImgUrl = "";
            for (int i = 0; i < arrayList.size(); i++) {
                advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
                ImageView advertisementImage = (ImageView) advertisementView.findViewById(R.id.advertisement_image);
                Object o=arrayList.get(i);
                Image image = null;
                if(o instanceof Image)
                {
                    image= (Image) o;
                }
                activeImgUrl = image.getImageUrl();
                Utils.CornerImageViewDisplayByUrl(activeImgUrl, advertisementImage,9);
                viewList.add(advertisementView);
                final List<Object> finalArrayList1 = arrayList;
                final int finalI = i;
                advertisementImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageViewPageDialog.newInstance(finalArrayList1, finalI,true).show(getActivity().getSupportFragmentManager());
                    }
                });
                if (arrayList.size() == 1) {
                    break;
                }
                //每循环一次需要向LinearLayout中添加一个点的view对象
                View v = new View(App.getInstance());
                v.setBackgroundResource(R.drawable.point_bg);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
                params.leftMargin = 9;
                params.rightMargin = 9;
                v.setLayoutParams(params);
                v.setEnabled(false);
                llPointGroup.addView(v);
            }
            HomeAdvertisementTabAdapter homeAdvertisementTabAdapter = new HomeAdvertisementTabAdapter(viewList);
            viewPager.setAdapter(homeAdvertisementTabAdapter);
            viewPager.setIsCanScroll(false);
            viewPager.setCurrentItem(0);
            if (arrayList.size() > 1) {
                llPointGroup.getChildAt(previousPosition).setEnabled(true);
                //开始轮播效果
                handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
            }

            final List<Object> finalArrayList = arrayList;
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int arg1) {
                    int position = arg1 % finalArrayList.size();
                    // 把当前选中的点给切换了, 还有描述信息也切换
                    if (llPointGroup != null) {
                        if (llPointGroup.getChildAt(previousPosition) != null) {
                            llPointGroup.getChildAt(previousPosition).setEnabled(false);
                        }
                        if (llPointGroup.getChildAt(position) != null) {
                            llPointGroup.getChildAt(position).setEnabled(true);
                        }
                    }
                    // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                    previousPosition = position;
                    handler.sendMessage(Message.obtain(handler, AdvertisementHandler.MSG_PAGE_CHANGED, arg1, 0));
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                //覆写该方法实现轮播效果的暂停和恢复
                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // 当页面的状态改变时将调用此方法
                    //arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
                    switch (arg0) {
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            // 正在拖动页面时执行此处
                            handler.sendEmptyMessage(AdvertisementHandler.MSG_KEEP_SILENT);
                            break;
                        case ViewPager.SCROLL_STATE_IDLE:
                            // 未拖动页面时执行此处
                            handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
                            break;
                        default:
                            break;
                    }
                }

            });


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) advertisementLayout.getLayoutParams();
            params.height = PlatformInfoUtils.getWidthOrHeight(mActivity)[0] * 30 / 75;
            advertisementLayout.setLayoutParams(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageListDataNoScroll(List<ProductImage> list) {
        try {
            options = new RequestOptions()
                    .centerCrop()
                    .transform(new GlideRoundTransform(9))
                    .placeholder(R.mipmap.error_bg)
                    .error(R.mipmap.error_bg);
            List<Object> arrayList = null;
            final LinearLayout llPointGroup;
            if (arrayList == null) {
                arrayList = new ArrayList<Object>();
            }
            arrayList.clear();
            if (list != null && list.size() > 0) {
                arrayList.addAll(list);
            }
            List<View> viewList = new ArrayList<View>();
            viewPager = (CustomViewPager) rootView.findViewById(R.id.advertisement_head_view_page);
            RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            final TextView numTextView=(TextView) rootView.findViewById(R.id.img_num_text_view);
            llPointGroup = (LinearLayout) rootView.findViewById(R.id.ll_point_group);
            llPointGroup.removeAllViews();
            View advertisementView;
            String activeImgUrl = "";
            for (int i = 0; i < arrayList.size(); i++) {
                advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
                ImageView advertisementImage = (ImageView) advertisementView.findViewById(R.id.advertisement_image);
                Object o=arrayList.get(i);
                ProductImage image = null;
                if(o instanceof ProductImage)
                {
                    image= (ProductImage) o;
                }
                activeImgUrl = image.getImageUrl();
                Utils.ImageViewDisplayByUrl(activeImgUrl, advertisementImage);
                viewList.add(advertisementView);
                final List<Object> finalArrayList1 = arrayList;
                final int finalI = i;
                advertisementImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageViewPageDialog.newInstance(finalArrayList1, finalI,true).show(getActivity().getSupportFragmentManager());
                    }
                });
//                if (arrayList.size() == 1) {
//                    break;
//                }
//                //每循环一次需要向LinearLayout中添加一个点的view对象
//                View v = new View(App.getInstance());
//                v.setBackgroundResource(R.drawable.point_bg);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
//                params.leftMargin = 9;
//                params.rightMargin = 9;
//                v.setLayoutParams(params);
//                v.setEnabled(false);
//                llPointGroup.addView(v);
            }
            if(arrayList==null||arrayList.size()==0){
                advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
                ImageView advertisementImage = (ImageView) advertisementView.findViewById(R.id.advertisement_image);
                RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.goods_no_image_big);
                Glide.with(App.getInstance()).asBitmap().load("").apply(options).into(advertisementImage);
                viewList.add(advertisementView);
                numTextView.setVisibility(View.GONE);
            }else if(arrayList.size()==1){
                numTextView.setVisibility(View.GONE);
            }else{
                numTextView.setText(1+"/"+arrayList.size());
                numTextView.setVisibility(View.VISIBLE);
            }
            HomeAdvertisementTabAdapter homeAdvertisementTabAdapter = new HomeAdvertisementTabAdapter(viewList);
            viewPager.setAdapter(homeAdvertisementTabAdapter);
            viewPager.setIsCanScroll(false);
            viewPager.setCurrentItem(0);
//            if (arrayList.size() > 1) {
//                llPointGroup.getChildAt(previousPosition).setEnabled(true);
//                //开始轮播效果
////                handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
//            }

            final List<Object> finalArrayList = arrayList;
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int arg1) {
                    int position = (arg1+1) % finalArrayList.size();
                    if(position==0){
                        position=finalArrayList.size();
                    }
//                    // 把当前选中的点给切换了, 还有描述信息也切换
//                    if (llPointGroup != null) {
//                        if (llPointGroup.getChildAt(previousPosition) != null) {
//                            llPointGroup.getChildAt(previousPosition).setEnabled(false);
//                        }
//                        if (llPointGroup.getChildAt(position) != null) {
//                            llPointGroup.getChildAt(position).setEnabled(true);
//                        }
//                    }
//                    // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
//                    previousPosition = position;
//                    handler.sendMessage(Message.obtain(handler, AdvertisementHandler.MSG_PAGE_CHANGED, arg1, 0));
                    numTextView.setText(position+"/"+ finalArrayList.size());
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                //覆写该方法实现轮播效果的暂停和恢复
                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // 当页面的状态改变时将调用此方法
                    //arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
//                    switch (arg0) {
//                        case ViewPager.SCROLL_STATE_DRAGGING:
//                            // 正在拖动页面时执行此处
////                            handler.sendEmptyMessage(AdvertisementHandler.MSG_KEEP_SILENT);
//                            break;
//                        case ViewPager.SCROLL_STATE_IDLE:
//                            // 未拖动页面时执行此处
////                            handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
//                            break;
//                        default:
//                            break;
//                    }
                }

            });


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) advertisementLayout.getLayoutParams();
            params.height = PlatformInfoUtils.getWidthOrHeight(mActivity)[0] * 60 / 75;
            advertisementLayout.setLayoutParams(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.clearData();
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (viewPager != null) {
            viewPager.removeAllViews();
            viewPager = null;
        }
        super.onDestroy();
    }
}
