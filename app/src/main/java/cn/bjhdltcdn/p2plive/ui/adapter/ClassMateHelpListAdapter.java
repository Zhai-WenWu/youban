//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.HelpInfo;
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.OriginalInfo;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ClassMateHelpDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PKParkActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
//import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
//import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * Created by ZHAI on 2018/2/22.
// */
//
//public class ClassMateHelpListAdapter extends BaseAdapter {
//    private AppCompatActivity mActivity;
//    private List<HelpInfo> mList;
//    private int type;
//    private final RequestOptions options;
//    private final int screenWidth;
//    private final RequestOptions gifOptions;
//    private String firstImageUrl;
//    private String videoImageUrl;
//    private boolean praiceRefresh = true;
//
//    public ClassMateHelpListAdapter(AppCompatActivity activity) {
//        this.mActivity = activity;
//        options = new RequestOptions()
//                .centerCrop()
//                .transform(new GlideRoundTransform(9))
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg);
//        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
//        gifOptions = new RequestOptions().centerCrop().transform(new GlideRoundTransform(9))
//                .placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//
//    }
//
//
//    public void addHelpInfo(HelpInfo helpInfo) {
//
//        if (helpInfo == null) {
//            return;
//        }
//
//        if (this.mList == null) {
//            this.mList = new ArrayList<>(1);
//        }
//
//        this.mList.add(0, helpInfo);
//        notifyDataSetChanged();
//
//    }
//
//    @Override
//    public int getCount() {
//        return mList == null ? 0 : mList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return mList == null || mList.size() == 0 ? null : mList.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(final int i, View view, ViewGroup viewGroup) {
//        final ViewHolder viewHolder;
//
//        if (view == null) {
//            view = LayoutInflater.from(App.getInstance()).inflate(R.layout.schoolmate_list_item_layout, null);
//            viewHolder = new ClassMateHelpListAdapter.ViewHolder();
//            viewHolder.user_img = view.findViewById(R.id.user_img);
//            viewHolder.user_nickname_text = view.findViewById(R.id.user_nickname_text);
//            viewHolder.tv_school_name = view.findViewById(R.id.tv_school_name);
//            viewHolder.alumnus_text = view.findViewById(R.id.alumnus_text);
//            viewHolder.time_text = view.findViewById(R.id.time_text);
//            viewHolder.distance_text = view.findViewById(R.id.distance_text);
//            viewHolder.content_text = view.findViewById(R.id.content_text);
//            viewHolder.linear_image_two = view.findViewById(R.id.linear_image_two);
//            viewHolder.img_first = view.findViewById(R.id.img_first);
//            viewHolder.video_play_img = view.findViewById(R.id.video_play_img);
//            viewHolder.recycler_image = view.findViewById(R.id.recycler_image);
//            viewHolder.delete_img = view.findViewById(R.id.delete_img);
//            viewHolder.share_text = view.findViewById(R.id.share_text);
//            viewHolder.praise_text = view.findViewById(R.id.praise_text);
//            viewHolder.comment_text = view.findViewById(R.id.comment_text);
//            viewHolder.user_age_text = view.findViewById(R.id.user_age_text);
//            viewHolder.user_city = view.findViewById(R.id.user_city);
//            viewHolder.recycler_image.setHasFixedSize(true);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
//            gridLayoutManager.setAutoMeasureEnabled(true);
//            viewHolder.recycler_image.setLayoutManager(gridLayoutManager);
//            viewHolder.recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));
//            if (type == 2) {
//                viewHolder.delete_img.setVisibility(View.VISIBLE);
//                viewHolder.share_text.setVisibility(View.GONE);
//            } else {
//                viewHolder.delete_img.setVisibility(View.GONE);
//                viewHolder.share_text.setVisibility(View.VISIBLE);
//            }
//            view.setTag(viewHolder);
//
//        } else {
//
//            viewHolder = (ViewHolder) view.getTag();
//        }
//
//        final HelpInfo helpInfo = mList.get(i);
//        if (helpInfo != null) {
//            final BaseUser userInfo = helpInfo.getBaseUser();
//            if (userInfo != null) {
//                // 头像
//                Glide.with(mActivity).load(userInfo.getUserIcon()).apply(options).into(viewHolder.user_img);
//                // 昵称
//                viewHolder.user_nickname_text.setText(userInfo.getNickName());
//                // 学习名称
//                String schoolName = userInfo.getSchoolName();
//                viewHolder.tv_school_name.setText(schoolName);
//                // 显示校友标识
//                int isSchoolmate = userInfo.getIsSchoolmate();
//                //年龄
//                if (userInfo.getSex() == 1) {
//                    //男性
//                    viewHolder.user_age_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//                } else if (userInfo.getSex() == 2) {
//                    //女性
//                    viewHolder.user_age_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//                }
//
//                // 年龄
//                viewHolder.user_age_text.setText(helpInfo.getBaseUser().getAge() + "岁");
//                viewHolder.user_age_text.setPadding(0, Utils.dp2px(7), Utils.dp2px(10), 0);
//                if (isSchoolmate == 2) {
//                    viewHolder.alumnus_text.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.alumnus_text.setVisibility(View.INVISIBLE);
//                }
//
//                //城市
//                if (userInfo.getUserId() != SafeSharePreferenceUtils.getInt(Constants.Fields.USER_ID, 0)) {
//                    viewHolder.user_city.setVisibility(View.VISIBLE);
//                    viewHolder.distance_text.setVisibility(View.VISIBLE);
//                    viewHolder.user_city.setText(helpInfo.getCity());
//                    String distance = helpInfo.getDistance();
//                    if (!StringUtils.isEmpty(distance)) {
//                        viewHolder.distance_text.setText(helpInfo.getDistance());
//                    } else {
//                        viewHolder.distance_text.setText(helpInfo.getBaseUser().getDistance());
//                    }
//
//                } else {
//                    viewHolder.user_city.setVisibility(View.GONE);
//                    viewHolder.distance_text.setVisibility(View.GONE);
//                }
//
//                // 点击跳转到用户详情
//                viewHolder.user_img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (userInfo.getUserId() != SafeSharePreferenceUtils.getInt(Constants.Fields.USER_ID, 0)) {
//                            mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, userInfo));
//                        }
//                    }
//                });
//            }
//        }
//        viewHolder.time_text.setText(helpInfo.getAddTime());
//        //跟拍人以及内容
//        String content = helpInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.content_text.setText(content);
//            viewHolder.content_text.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.content_text.setVisibility(View.GONE);
//        }
//        //跟拍对象
//        final OriginalInfo originalInfo = helpInfo.getOriginalInfo();
//        //标签展示
//        long labelId = helpInfo.getLabelId();
//        String labelName = helpInfo.getLabelName();
//        if (helpInfo.getIsOriginal() == 1 && originalInfo != null && !StringUtils.isEmpty(labelName)) {
//            //跟拍加参赛
//            viewHolder.content_text.setVisibility(View.VISIBLE);
//            viewHolder.content_text.setText(Utils.getFollowAndMatchContent(originalInfo, labelName, helpInfo.getScondlabelList(), content));
//        } else {
//            //只有跟拍
//            if (helpInfo.getIsOriginal() == 1 && originalInfo != null) {
//                viewHolder.content_text.setVisibility(View.VISIBLE);
//                viewHolder.content_text.setText(Utils.getFollowContent(originalInfo, content));
//            }
//            //只有参赛
//            if (!StringUtils.isEmpty(labelName)) {
//                viewHolder.content_text.setVisibility(View.VISIBLE);
//                viewHolder.content_text.setText(Utils.getMatchContent(labelId, labelName, helpInfo.getScondlabelList(), content));
//            }
//        }
//        viewHolder.content_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到视频广场界面
//                if (helpInfo.getIsOriginal() == 1 && originalInfo != null) {
//                    Intent intent = new Intent(mActivity, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, helpInfo.getParentId());
//                    intent.putExtra(Constants.Fields.TO_USER_ID, originalInfo.getUserId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_HELPINFO);
//                    mActivity.startActivity(intent);
//                } else {
//                    Intent intent = new Intent(mActivity, ClassMateHelpDetailActivity.class);
//                    intent.putExtra(Constants.Fields.POSITION, i);
//                    intent.putExtra(Constants.KEY.KEY_OBJECT, helpInfo);
//                    mActivity.startActivity(intent);
//                }
//            }
//        });
//
//        if (helpInfo.getHelpType() == 1) {
//            final List<Image> imageList = helpInfo.getImageList();
//            if (imageList != null) {
//                if (imageList.size() == 1) {
//                    viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//                    viewHolder.video_play_img.setVisibility(View.GONE);
//                    viewHolder.recycler_image.setVisibility(View.GONE);
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//                    firstImageUrl = imageList.get(0).getImageUrl();
//
//                    if (praiceRefresh) {
//                        if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
//                            Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.img_first);
//                        } else {
//                            Glide.with(App.getInstance()).load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.img_first);
//                        }
//                    }
//
//                    layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                    viewHolder.img_first.setLayoutParams(layoutParams);
//
//                    viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (mActivity != null) {
//                                ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
//                            }
//                        }
//                    });
//
//                } else if (imageList.size() > 1) {
//                    if (praiceRefresh) {
//                        viewHolder.linear_image_two.setVisibility(View.GONE);
//                        viewHolder.recycler_image.setVisibility(View.VISIBLE);
//                        PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
//                        viewHolder.recycler_image.setAdapter(postImageLIstAdapter);
//                        postImageLIstAdapter.setOnItemListener(new ItemListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                if (mActivity != null) {
//                                    ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
//                                }
//                            }
//                        });
//                    }
//                }
//            } else {
//                viewHolder.linear_image_two.setVisibility(View.GONE);
//                viewHolder.recycler_image.setVisibility(View.GONE);
//            }
//        } else if (helpInfo.getHelpType() == 2) {
//            viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//            viewHolder.video_play_img.setVisibility(View.VISIBLE);
//            viewHolder.recycler_image.setVisibility(View.GONE);
//            //视频
//            videoImageUrl = helpInfo.getVideoImageUrl();
//            Glide.with(App.getInstance()).load(helpInfo.getVideoImageUrl()).apply(options).into(viewHolder.img_first);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//            viewHolder.img_first.setLayoutParams(layoutParams);
//            viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //跳转到视频播放界面
//                    Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
//                    intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, helpInfo.getHelpId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_HELPINFO);
//                    mActivity.startActivity(intent);
//                }
//            });
//        }
//
//        viewHolder.praise_text.setText(Utils.bigNumberToStr(helpInfo.getPraiseCount(), helpInfo.getPraiseCountStr()));
//
//        if (helpInfo.getIsPraise() == 1) {
//            //1 : 已点赞
//            viewHolder.praise_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//        } else {
//            //0: 未点赞
//            viewHolder.praise_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//        }
//        viewHolder.comment_text.setText(String.valueOf(helpInfo.getCommentCount()));
//        viewHolder.praise_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mOnBottomClick.onPraise(i, helpInfo.getHelpId(), helpInfo.getIsPraise());
//            }
//        });
//        viewHolder.delete_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mOnBottomClick.onDelete(i, helpInfo.getHelpId());
//            }
//        });
//
//        viewHolder.share_text.setText(Utils.bigNumberToStr(helpInfo.getShareNumber(), helpInfo.getShareNumberStr()));
//
//        viewHolder.share_text.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                String imageUrl = "";
//                if (helpInfo.getHelpType() == 1) {
//                    if (helpInfo.getImageList() != null && helpInfo.getImageList().size() > 0) {
//                        imageUrl = helpInfo.getImageList().get(0).getThumbnailUrl();
//                    }
//                }
//
//                ShareUtil.getInstance().showShare(mActivity, ShareUtil.CLASSMATEHELP, helpInfo.getHelpId(), helpInfo, "", "", "", imageUrl, false);
//            }
//        });
//        return view;
//    }
//
//    /*public void updateSingleRow(ListView listView, long id) {
//
//        if (listView != null) {
//            int start = listView.getFirstVisiblePosition();
//            for (int i = 0, j = listView.getLastVisiblePosition(); i <= j; i++)
//                if (id == ((HelpInfo) listView.getItemAtPosition(i)).getHelpId()) {
//                    View view = listView.getChildAt(i);
//                    getView(i, view, listView);
//                    break;
//                }
//        }
//    }*/
//
//    public void setData(List<HelpInfo> data) {
//        this.mList = data;
//    }
//
//    public List<HelpInfo> getmList() {
//        return mList;
//    }
//
//    public void addData(List<HelpInfo> list) {
//        mList.addAll(list);
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public void setPraiceRefresh(boolean praiceRefresh) {
//        this.praiceRefresh = praiceRefresh;
//    }
//
//    class ViewHolder {
//        public CircleImageView user_img;
//        public TextView user_nickname_text;
//        public TextView tv_school_name;
//        public ImageView alumnus_text;
//        public TextView time_text;
//        public TextView distance_text;
//        public TextView content_text;
//        public RelativeLayout linear_image_two;
//        public ImageView img_first;
//        public ImageView video_play_img;
//        public RecyclerView recycler_image;
//        public ImageView delete_img;
//        public TextView praise_text;
//        public TextView share_text;
//        public TextView user_age_text;
//        public TextView user_city;
//        public TextView comment_text;
//    }
//
//    OnBottomClick mOnBottomClick;
//
//    public void setOnBottomClick(OnBottomClick onBottomClick) {
//        this.mOnBottomClick = onBottomClick;
//    }
//
//    public interface OnBottomClick {
//
//        void onDelete(int position, long helpId);
//
//        void onPraise(int position, long helpId, int isPraise);
//
//        void onShare(int position, long helpId, String imageUrl);
//    }
//}
