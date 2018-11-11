package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.PostImageRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.PostLabelInfoRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.StoreCategorySecondRecycleAdapter;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;

/**
 *
 */
public class PostInfoSharedFragment extends BaseFragment {

    private View rootView;
    private AppCompatActivity mActivity;
    private PostInfo postInfo;

    private RequestOptions options ;
    private int screenWidth;

    public void setObject(PostInfo postInfo) {
        this.postInfo = postInfo;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= (AppCompatActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
        options = new RequestOptions().placeholder(R.mipmap.error_bg);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_post_info_shared_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        if (postInfo != null) {
            final BaseUser baseUser = postInfo.getBaseUser();
             final int isAnonymous=postInfo.getIsAnonymous();
            if (baseUser != null) {
                TextView tipTextView = rootView.findViewById(R.id.form_user_nickname_text);
                tipTextView.setText("@" + baseUser.getNickName());
                tipTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)&& isAnonymous == 2) {
                            //跳到用户详情
                            startActivity(new Intent(getActivity(), UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                        }
                    }
                });
            }

            // 帖子内容
            TextView contentTextView = rootView.findViewById(R.id.content_text);
            contentTextView.setVisibility(View.GONE);
            if (!StringUtils.isEmpty(postInfo.getContent())) {
                contentTextView.setText(postInfo.getContent());
                contentTextView.setVisibility(View.VISIBLE);
            }

            RelativeLayout linear_image_two = (RelativeLayout) rootView.findViewById(R.id.linear_image_two);
            ImageView img_first = (ImageView) rootView.findViewById(R.id.img_first);
            ImageView video_play_img = (ImageView) rootView.findViewById(R.id.video_play_img);
            // 图片列表
            RecyclerView recyclerView = rootView.findViewById(R.id.recycler_image);
            List<Image> imageList=postInfo.getImageList();
            if (imageList == null || imageList.size() == 0) {
                recyclerView.setVisibility(View.GONE);
            } else {
                if (imageList.size() > 0 && imageList.size() < 2) {

                    linear_image_two.setVisibility(View.VISIBLE);
                    video_play_img.setVisibility(View.GONE);
                    if (imageList.size() == 1) {
                        Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(img_first);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) img_first.getLayoutParams();
                        layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
                        img_first.setLayoutParams(layoutParams);
                    }
                } else if (imageList.size() > 1) {
                    recyclerView.setVisibility(View.VISIBLE);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
                    gridLayoutManager.setAutoMeasureEnabled(true);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));
                    PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity,imageList, 3, Utils.dp2px(9));
                    recyclerView.setAdapter(postImageLIstAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            // 视频
            if (postInfo.getTopicType() == 2) {
                recyclerView.setVisibility(View.GONE);
                rootView.findViewById(R.id.linear_image_two).setVisibility(View.VISIBLE);

                // 视频封面
                ImageView imageView = rootView.findViewById(R.id.img_first);
                ImageView videoPlayImg = rootView.findViewById(R.id.video_play_img);
                videoPlayImg.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.height = (PlatformInfoUtils.getWidthOrHeight(App.getInstance())[0] -Utils.dp2px(25))*384/700;
                imageView.setLayoutParams(layoutParams);

                Glide.with(this).load(postInfo.getVideoImageUrl()).apply(options.centerCrop()).into(imageView);

            }

//            // 一级标签
//            TextView postLabelOneTextView = rootView.findViewById(R.id.post_label_one_text);
//            postLabelOneTextView.setText(postInfo.getHobbyName());
//
//            // 圈子名称
//            TextView postLabelTwoTextView = rootView.findViewById(R.id.post_label_two_text);
//            postLabelTwoTextView.setText(postInfo.getOrganName());
            TextView shareNumTextView = (TextView) rootView.findViewById(R.id.share_text);
            shareNumTextView.setText(Utils.bigNumberToStr(postInfo.getShareNumber(),postInfo.getShareNumberStr()));
            // 点赞
            TextView praisNumTextView = rootView.findViewById(R.id.praise_text);
            praisNumTextView.setText(Utils.bigNumberToStr(postInfo.getPraiseCount(),postInfo.getPraiseCountStr()));
            final int isPraise = postInfo.getIsPraise();
            if (isPraise == 1) {
                //1 : 已点赞
                praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
            } else {
                //0: 未点赞
                praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
            }

            // 评论
            TextView commentNumTextView = rootView.findViewById(R.id.comment_text);
            commentNumTextView.setText(Utils.bigNumberToStr(postInfo.getCommentCount(),postInfo.getCommentCountStr()));

            RelativeLayout storeLayout = (RelativeLayout) rootView.findViewById(R.id.store_layout);
            TextView recruitTextView= (TextView) rootView.findViewById(R.id.recruit_text);
            TextView storeNameTextView= (TextView) rootView.findViewById(R.id.store_name_text);
            RecyclerView recycler_post_label = (RecyclerView) rootView.findViewById(R.id.recycler_post_label);
            RecyclerView recycler_store_label = (RecyclerView) rootView.findViewById(R.id.store_label_recycle_view);
            FlowLayoutManager flowLayoutManager=new FlowLayoutManager(mActivity);
            recycler_post_label.setLayoutManager(flowLayoutManager);
//            LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
//            viewHolder.recycler_post_label.setLayoutManager(linearLayoutManager2);

            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
            recycler_store_label.setLayoutManager(linearLayoutManager);

            //店铺信息
            if(postInfo.getStoreDetail()!=null){
                final StoreInfo storeInfo=postInfo.getStoreDetail().getStoreInfo();
                if(storeInfo!=null){
                    storeLayout.setVisibility(View.VISIBLE);
                    if(storeInfo.getIsPublish()==1){
                        recruitTextView.setVisibility(View.VISIBLE);
                    }else{
                        recruitTextView.setVisibility(View.GONE);
                    }
                    storeNameTextView.setText(storeInfo.getTitle());
                    StoreCategorySecondRecycleAdapter secondRecycleAdapter=new StoreCategorySecondRecycleAdapter();
                    if(storeInfo.getFirstLabelInfo()!=null&&storeInfo.getFirstLabelInfo().getList()!=null){
                        secondRecycleAdapter.setList(storeInfo.getFirstLabelInfo().getList());
                    }
                    recycler_store_label.setAdapter(secondRecycleAdapter);
                }else{
                    storeLayout.setVisibility(View.GONE);
                }
                storeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                if(storeInfo.getUserId()!=SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)){
//                    onClick.onApplyClerk(storeInfo.getStoreId());
//                }
                        //跳转到店铺详情
                        Intent intent=new Intent(mActivity,ShopDetailActivity.class);
                        intent.putExtra(Constants.Fields.STORE_ID,storeInfo.getStoreId());
                        mActivity.startActivity(intent);

                    }
                });
            }else{
                storeLayout.setVisibility(View.GONE);
            }

            PostLabelInfoRecycleAdapter postLabelInfoRecycleAdapter=new PostLabelInfoRecycleAdapter(20);
            recycler_post_label.setVisibility(View.VISIBLE);
            if(postInfo.getPostLabelList()!=null&&postInfo.getPostLabelList().size()>0){
                postLabelInfoRecycleAdapter.setList(postInfo.getPostLabelList());
            }else
            {
                recycler_post_label.setVisibility(View.GONE);
            }
            recycler_post_label.setAdapter(postLabelInfoRecycleAdapter);


        }

    }

    @Override
    protected void onVisible(boolean isInit) {

    }


}
