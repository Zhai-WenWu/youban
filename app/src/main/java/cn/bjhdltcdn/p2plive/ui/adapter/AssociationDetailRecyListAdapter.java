//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.content.Intent;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.TextUtils;
//import android.text.style.ImageSpan;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
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
//import cn.bjhdltcdn.p2plive.api.ApiData;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.handler.AdvertisementHandler;
//import cn.bjhdltcdn.p2plive.handler.AdvertisementHandlerInActivity;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.AssociationDetailRecyListItemModel;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.ChatInfo;
//import cn.bjhdltcdn.p2plive.model.Group;
//import cn.bjhdltcdn.p2plive.model.HelpInfo;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.OrganBaseInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.OriginalInfo;
//import cn.bjhdltcdn.p2plive.model.PlayInfo;
//import cn.bjhdltcdn.p2plive.model.PostInfo;
//import cn.bjhdltcdn.p2plive.model.RecommendInfo;
//import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
//import cn.bjhdltcdn.p2plive.model.StoreInfo;
//import cn.bjhdltcdn.p2plive.mvp.contract.UserContract;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.ui.activity.ActiveListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailInActiveListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailPKVideoListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationInfoActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationTypeListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ClassMateHelpListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.GroupCreateActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PKParkActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.SayLoveListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
//import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
//import cn.bjhdltcdn.p2plive.utils.RongIMutils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * Created by xiawenquan on 18/4/14.
// */
//
//public class AssociationDetailRecyListAdapter extends BaseRecyclerAdapter {
//
//    //类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请,8表白,9同学帮帮忙,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白)
//
//    /**
//     * 1 帖子
//     */
//    private final int ITEM_1 = 1;
//    /**
//     * 3 活动
//     */
//    private final int ITEM_3 = 3;
//    /**
//     * 5 pk挑战
//     */
//    private final int ITEM_5 = 5;
//
//    /**
//     * 8 表白
//     */
//    private final int ITEM_8 = 8;
//
//    /**
//     * 9 同学帮帮忙
//     */
//    private final int ITEM_9 = 9;
//
//    /**
//     * 100 列表头部
//     */
//    private final int ITEM_100 = 100;
//
//    /**
//     * 101 列表头部广告
//     */
//    private final int ITEM_101 = 101;
//
//    /**
//     * 102 列表空数据的展示
//     */
//    private final int ITEM_102 = 102;
//
//    /**
//     * 圈子类型(0-->普通圈子，1-->官方圈子，2-->学校圈子),
//     */
//    private int orgType;
//
//    /**
//     * 是否禁言(0不显示,1解除禁言,2禁言)
//     */
//    private int isGag;
//    private OrganizationInfo organizationInfo;
//
//    public void setIsGag(int isGag) {
//        this.isGag = isGag;
//    }
//
//    // 圈子下帖子活动列表
//    private List list;
//
//    public List getList() {
//        return list;
//    }
//
//    private int previousPosition = 0;
//
//    private AdHeaderViewViewHolder adHeaderViewViewHolder;
//
//    public AdHeaderViewViewHolder getAdHeaderViewViewHolder() {
//        return adHeaderViewViewHolder;
//    }
//
//    /**
//     * 广告处理消息
//     */
//    public AdvertisementHandlerInActivity handler;
//
//    public void setHandler(AdvertisementHandlerInActivity handler) {
//        this.handler = handler;
//    }
//
//    private AppCompatActivity mActivity;
//    private RequestOptions options, userOptions, gifOptions, matchOptions;
//    private int screenWidth;
//
//    private JoinOrganizationCallBack joinOrganizationCallBack;
//
//    public void setJoinOrganizationCallBack(JoinOrganizationCallBack joinOrganizationCallBack) {
//        this.joinOrganizationCallBack = joinOrganizationCallBack;
//    }
//
//
//    public OrganizationInfo getOrganizationInfo() {
//        return organizationInfo;
//    }
//
//    private JoinGroupCallBack joinGroupCallBack;
//
//    public void setJoinGroupCallBack(JoinGroupCallBack joinGroupCallBack) {
//        this.joinGroupCallBack = joinGroupCallBack;
//    }
//
//    private AssociationDetailListAdapter.ItemWidgetOnClick itemWidgetOnClick;
//
//    public void setItemWidgetOnClick(AssociationDetailListAdapter.ItemWidgetOnClick itemWidgetOnClick) {
//        this.itemWidgetOnClick = itemWidgetOnClick;
//    }
//
//    public AssociationDetailRecyListAdapter(AppCompatActivity mActivity) {
//
//        this.mActivity = mActivity;
//        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg).transform(new GlideRoundTransform(9));
//
//        gifOptions = new RequestOptions()
//                .centerCrop()
//                .transform(new GlideRoundTransform(9))
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//
//
//        userOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon).error(R.mipmap.error_user_icon);
//        matchOptions = new RequestOptions()
//                .placeholder(R.mipmap.post_match_error_icon)
//                .error(R.mipmap.post_match_error_icon);
//
//        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
//
//    }
//
//
//    public void addItem(HomeInfo homeInfo) {
//        if (this.list != null && this.list.size() > 1) {
//            Object object = this.list.get(1);
//            if (object instanceof AssociationDetailRecyListItemModel) {
//                AssociationDetailRecyListItemModel model = (AssociationDetailRecyListItemModel) object;
//                if (model.getType() == 101) {// 判断是否有广告位
//                    this.list.add(2, homeInfo);
//                    notifyItemInserted(2);
//                    notifyItemRangeChanged(2, this.list.size() - 2);
//                }
//            } else {
//                this.list.add(1, homeInfo);
//                notifyItemInserted(1);
//                notifyItemRangeChanged(1, this.list.size() - 1);
//
//            }
//        }
//    }
//
//    /**
//     * 设置头部数据
//     *
//     * @param model
//     */
//    public synchronized void setHeaderViewData(AssociationDetailRecyListItemModel model, Object object) {
//        if (this.list == null) {
//            this.list = new ArrayList(1);
//        }
//
//
//        if (object == null) {//添加新数据
//            this.list.add(0, model);
//            notifyItemRangeInserted(0, 1);
//        } else {
//            notifyItemChanged(0, object);
//
//        }
//
//
//    }
//
//    /**
//     * 设置广告位置
//     *
//     * @param model
//     */
//    public synchronized void setADHeaderViewData(AssociationDetailRecyListItemModel model) {
//        if (this.list == null) {
//            this.list = new ArrayList(1);
//        }
//
//        //添加新数据
//        this.list.add(1, model);
//        notifyItemRangeInserted(1, 1);
//
//
//    }
//
//
//    /**
//     * 设置空数据
//     *
//     * @param model
//     */
//    public synchronized void setEmptyViewData(AssociationDetailRecyListItemModel model) {
//        if (this.list == null) {
//            this.list = new ArrayList(1);
//        }
//
//        int size = this.list.size();
//
//        this.list.add(model);
//        notifyItemRangeInserted(size, 1);
//
//    }
//
//
//    /**
//     * 下拉刷新后操作
//     *
//     * @param list
//     */
//    public synchronized void setOnRefreshData(List list) {
//        if (list == null || list.size() == 0) {
//
//            return;
//        }
//
//        if (this.list == null) {
//            this.list = new ArrayList(1);
//        }
//
//        if (getItemCount() > 0) {
//            list.add(0, getList().get(0));
//        }
//
//        // 判断是否有推荐对象
//        boolean isHasRecomment = false;
//        if (getItemCount() > 1) {
//            Object object = getList().get(1);
//            if (object instanceof AssociationDetailRecyListItemModel) {
//                AssociationDetailRecyListItemModel model = (AssociationDetailRecyListItemModel) object;
//                if (model.getType() == 101) {
//                    list.add(1, object);
//                    isHasRecomment = true;
//                }
//            }
//        }
//
//
//        this.list.clear();
//
//        this.list.addAll(list);
//
//        notifyItemRangeChanged(isHasRecomment ? 2 : 1, this.list.size());
//
//    }
//
//
//    /**
//     * 加载第一页数据此方法
//     *
//     * @param list
//     */
//    public synchronized void setList(List list) {
//        if (list == null || list.size() == 0) {
//            return;
//        }
//
//        if (this.list == null || this.list.size() == 0) {
//            this.list = new ArrayList(1);
//        }
//
//        int size = this.list.size();
//
//        this.list.addAll(list);
//
//        notifyItemRangeInserted(size, list.size());
//
//    }
//
//
//    /**
//     * 加载更多操作
//     *
//     * @param list
//     */
//    public synchronized void setListAll(List list) {
//        if (this.list == null || this.list.size() == 0) {
//            return;
//        }
//
//        if (list == null || list.size() == 0) {
//            return;
//        }
//
//        int size = this.list.size();
//
//        this.list.addAll(list);
//
//        notifyItemRangeInserted(size, list.size());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return list == null ? 0 : list.size();
//    }
//
//
//    @Override
//    public int getItemViewType(int position) {
//
//        if (list == null || list.size() == 0) {
//            return super.getItemViewType(position);
//        }
//
//        Object object = list.get(position);
//        if (object instanceof HomeInfo) {
//            HomeInfo homeInfo = (HomeInfo) object;
//            return homeInfo.getType();
//        } else if (object instanceof AssociationDetailRecyListItemModel) {
//            AssociationDetailRecyListItemModel model = (AssociationDetailRecyListItemModel) object;
//            return model.getType();
//        }
//
//        return super.getItemViewType(position);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position, @NonNull List<Object> payloads) {
//        super.onBindViewHolder(holder, position, payloads);
//
//        HomeInfo homeInfo = null;
//        AssociationDetailRecyListItemModel model = null;
//        Object object = list.get(position);
//        if (object instanceof HomeInfo) {
//            homeInfo = (HomeInfo) object;
//        } else if (object instanceof AssociationDetailRecyListItemModel) {
//            model = (AssociationDetailRecyListItemModel) object;
//        }
//
//
//        int itemType = getItemViewType(position);
//
//        if (payloads == null || payloads.isEmpty()) {
//            switch (itemType) {
//
//                case ITEM_1:
//                    if (homeInfo != null) {
//                        if (holder instanceof PostInfoItemViewHolder) {
//
//                            PostInfoItemViewHolder postInfoItemViewHolder = (PostInfoItemViewHolder) holder;
//                            bindPostInfoData(postInfoItemViewHolder, homeInfo, position, false);
//
//                        }
//
//                    }
//
//                    break;
//
//                case ITEM_3:
//
//                    if (homeInfo != null) {
//                        if (holder instanceof ActiveItemViewHolder) {
//
//                            ActiveItemViewHolder activeItemViewHolder = (ActiveItemViewHolder) holder;
//                            bindActiveItemData(activeItemViewHolder, homeInfo, position, false);
//
//                        }
//
//                    }
//
//                    break;
//
//                case ITEM_5:
//
//                    if (homeInfo != null) {
//                        if (holder instanceof PKItemViewHolder) {
//
//                            PKItemViewHolder pkItemViewHolder = (PKItemViewHolder) holder;
//                            bindPKData(pkItemViewHolder, homeInfo, position, false);
//
//                        }
//
//                    }
//
//                    break;
//
//                case ITEM_8:
//
//                    if (homeInfo != null) {
//                        if (holder instanceof SayLoveItemViewHolder) {
//
//                            SayLoveItemViewHolder sayLoveItemViewHolder = (SayLoveItemViewHolder) holder;
//                            bindSayLoveItemData(sayLoveItemViewHolder, homeInfo, position, false);
//
//                        }
//
//                    }
//
//                    break;
//
//                case ITEM_9:
//
//                    if (homeInfo != null) {
//                        if (holder instanceof ClassMateHelpItemViewHolder) {
//
//                            ClassMateHelpItemViewHolder classMateHelpItemViewHolder = (ClassMateHelpItemViewHolder) holder;
//                            bindClassMateHelpData(classMateHelpItemViewHolder, homeInfo, position, false);
//
//                        }
//
//                    }
//
//
//                    break;
//
//                case ITEM_100:
//
//                    if (model != null) {
//                        if (holder instanceof HeaderViewHolder) {
//
//                            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
//                            bindHeaderData(model, headerViewHolder, false);
//                        }
//
//                    }
//
//                    break;
//
//                case ITEM_101:
//
//                    if (model != null) {
//                        if (holder instanceof AdHeaderViewViewHolder) {
//
//                            AdHeaderViewViewHolder adHeaderViewViewHolder = (AdHeaderViewViewHolder) holder;
//                            bindADViewData(model, adHeaderViewViewHolder, false);
//                        }
//
//                    }
//
//                    break;
//
//                case ITEM_102:
//
//                    if (holder instanceof EmptyViewViewHolder) {
//                        EmptyViewViewHolder emptyViewViewHolder = (EmptyViewViewHolder) holder;
//                        if (model != null) {
//                            if (!StringUtils.isEmpty(model.getEmptyText())) {
//                                emptyViewViewHolder.emptyTextView.setText(model.getEmptyText());
//                            }
//                        }
//
//                    }
//
//                    break;
//
//            }
//        } else {
//
//            switch (itemType) {
//
//                case ITEM_1:
//                    if (homeInfo != null) {
//                        if (holder instanceof PostInfoItemViewHolder) {
//
//                            PostInfoItemViewHolder postInfoItemViewHolder = (PostInfoItemViewHolder) holder;
//                            bindPostInfoData(postInfoItemViewHolder, homeInfo, position, true);
//
//                        }
//
//                    }
//
//                    break;
//
//                case ITEM_3:
//
//                    if (homeInfo != null) {
//                        if (holder instanceof ActiveItemViewHolder) {
//
//                            ActiveItemViewHolder activeItemViewHolder = (ActiveItemViewHolder) holder;
//                            bindActiveItemData(activeItemViewHolder, homeInfo, position, true);
//
//                        }
//
//                    }
//
//                    break;
//
//                case ITEM_5:
//                    if (homeInfo != null) {
//                        if (holder instanceof PKItemViewHolder) {
//
//                            PKItemViewHolder pkItemViewHolder = (PKItemViewHolder) holder;
//                            bindPKData(pkItemViewHolder, homeInfo, position, true);
//
//                        }
//
//                    }
//                    break;
//
//                case ITEM_8:
//
//
//                    if (homeInfo != null) {
//                        if (holder instanceof SayLoveItemViewHolder) {
//
//                            SayLoveItemViewHolder sayLoveItemViewHolder = (SayLoveItemViewHolder) holder;
//                            bindSayLoveItemData(sayLoveItemViewHolder, homeInfo, position, true);
//
//                        }
//
//                    }
//
//
//                    break;
//
//                case ITEM_9:
//
//                    if (homeInfo != null) {
//                        if (holder instanceof ClassMateHelpItemViewHolder) {
//
//                            ClassMateHelpItemViewHolder classMateHelpItemViewHolder = (ClassMateHelpItemViewHolder) holder;
//                            bindClassMateHelpData(classMateHelpItemViewHolder, homeInfo, position, true);
//
//                        }
//
//                    }
//
//
//                    break;
//
//                case ITEM_100:
//                    if (holder instanceof HeaderViewHolder) {
//
//                        Object object1 = payloads.get(0);
//                        if (object1 instanceof OrganizationInfo) {
//                            OrganizationInfo organizationInfo = (OrganizationInfo) object1;
//                            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
//                            model.setOrganizationInfo(organizationInfo);
//                            bindHeaderData(model, headerViewHolder, true);
//                        }
//
//                    }
//                    break;
//
//                case ITEM_101:
//
//                    if (model != null) {
//                        if (holder instanceof AdHeaderViewViewHolder) {
//
//                            AdHeaderViewViewHolder adHeaderViewViewHolder = (AdHeaderViewViewHolder) holder;
//                            bindADViewData(model, adHeaderViewViewHolder, true);
//                        }
//
//                    }
//
//                    break;
//
//                case ITEM_102:
//
//                    if (holder instanceof EmptyViewViewHolder) {
//                        EmptyViewViewHolder emptyViewViewHolder = (EmptyViewViewHolder) holder;
//                        if (model != null) {
//                            if (!StringUtils.isEmpty(model.getEmptyText())) {
//                                emptyViewViewHolder.emptyTextView.setText(model.getEmptyText());
//                            }
//                        }
//
//                    }
//
//                    break;
//
//                default:
//
//                    break;
//
//            }
//        }
//    }
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//
//        View convertView = null;
//        switch (itemType) {
//
//            case ITEM_1:
//                convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.association_post_list_item_layout, null);
//                return new PostInfoItemViewHolder(convertView);
//
//            case ITEM_3:
//
//                convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.association_active_list_item_layout, null);
//                return new ActiveItemViewHolder(convertView);
//
//            case ITEM_5:
//                convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.association_pk_list_item_layout, null);
//                return new PKItemViewHolder(convertView);
//
//            case ITEM_8:
//                convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.home_recommend_post_list_item_layout, null);
//                return new SayLoveItemViewHolder(convertView);
//
//            case ITEM_9:
//                convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.schoolmate_list_item_layout, null);
//                return new ClassMateHelpItemViewHolder(convertView);
//
//            case ITEM_100:
//                convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.activity_association_detail_header_layout, null);
//                return new HeaderViewHolder(convertView);
//
//            case ITEM_101:
//                convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.home_ad_header_layout, null);
//                adHeaderViewViewHolder = new AdHeaderViewViewHolder(convertView);
//                if (mActivity instanceof AssociationDetailActivity) {
//                    AssociationDetailActivity associationDetailActivity = (AssociationDetailActivity) mActivity;
//                    associationDetailActivity.viewPager = adHeaderViewViewHolder.customViewPager;
//                }
//                return adHeaderViewViewHolder;
//
//            case ITEM_102:
//
//                convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.empty_page_layout, null);
//                AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Utils.dp2px(300));
//                convertView.setLayoutParams(params);
//                return new EmptyViewViewHolder(convertView);
//
//            default:
//                return new ItemType0ViewViewHolder(new TextView(App.getInstance()));
//
//        }
//
//
//    }
//
//
//    /**
//     * 绑定广告位数据
//     *
//     * @param model
//     * @param adHeaderViewViewHolder
//     * @param isUpdate
//     */
//    private void bindADViewData(AssociationDetailRecyListItemModel model, final AdHeaderViewViewHolder adHeaderViewViewHolder, boolean isUpdate) {
//
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) adHeaderViewViewHolder.layoutAdvertisement.getLayoutParams();
//        params.height = PlatformInfoUtils.getWidthOrHeight(App.getInstance())[0] * 30 / 75;
//        adHeaderViewViewHolder.layoutAdvertisement.setLayoutParams(params);
//
//        adHeaderViewViewHolder.llPointGroupView.removeAllViews();
//
//        final List<RecommendInfo> list = model.getRecommendInfoList();
//
//        List<View> viewList = new ArrayList<View>();
//
//        for (int i = 0; i < list.size(); i++) {
//            View advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
//            ImageView advertisementImage = advertisementView.findViewById(R.id.advertisement_image);
//            final RecommendInfo recommendInfo = list.get(i);
//
//            // 绑定图片
//            String recommendImgUrl = recommendInfo.getImgUrl();
//            Utils.CornerImageViewDisplayByUrl(recommendImgUrl, advertisementImage);
//            viewList.add(advertisementView);
//
//            advertisementView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String gotoUrl = recommendInfo.getGotoUrl();
//                    if (!TextUtils.isEmpty(gotoUrl)) {
//                        Intent intent = new Intent(mActivity, WXPayEntryActivity.class);
//                        intent.putExtra(Constants.Action.ACTION_EXTRA, 6);
//                        Object object = getList().get(0);
//                        if (object instanceof AssociationDetailRecyListItemModel) {
//                            AssociationDetailRecyListItemModel associationDetailRecyListItemModel = (AssociationDetailRecyListItemModel) object;
//                            if (associationDetailRecyListItemModel.getType() == 100) {
//                                OrganizationInfo organizationInfo = associationDetailRecyListItemModel.getOrganizationInfo();
//                                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                                intent.putExtra(Constants.KEY.KEY_URL, gotoUrl + "?userId=" + userId + "&organId=" + organizationInfo.getOrganId());
//                            }
//                        }
//
//                        mActivity.startActivity(intent);
//                    }
//                }
//            });
//
//            if (list.size() == 1) {
//                break;
//            }
//
//            //每循环一次需要向LinearLayout中添加一个点的view对象
//            View v = new View(App.getInstance());
//            v.setBackgroundResource(R.drawable.point_bg);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(14, 14);
//            layoutParams.leftMargin = 9;
//            layoutParams.rightMargin = 9;
//            v.setLayoutParams(layoutParams);
//            v.setEnabled(false);
//
//            adHeaderViewViewHolder.llPointGroupView.addView(v);
//
//        }
//
//        HomeAdvertisementTabAdapter homeAdvertisementTabAdapter = new HomeAdvertisementTabAdapter(viewList);
//        adHeaderViewViewHolder.customViewPager.setAdapter(homeAdvertisementTabAdapter);
//        adHeaderViewViewHolder.customViewPager.setIsCanScroll(false);
//        adHeaderViewViewHolder.customViewPager.setCurrentItem(0);
//        if (list.size() > 1) {
//            adHeaderViewViewHolder.llPointGroupView.getChildAt(previousPosition).setEnabled(true);
//            //开始轮播效果
//            handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
//        }
//
//        adHeaderViewViewHolder.customViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int arg1) {
//                int position = arg1 % list.size();
//                // 把当前选中的点给切换了, 还有描述信息也切换
//                adHeaderViewViewHolder.llPointGroupView.getChildAt(previousPosition).setEnabled(false);
//                adHeaderViewViewHolder.llPointGroupView.getChildAt(position).setEnabled(true);
//                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
//                previousPosition = position;
//                handler.sendMessage(Message.obtain(handler, AdvertisementHandler.MSG_PAGE_CHANGED, arg1, 0));
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//
//            //覆写该方法实现轮播效果的暂停和恢复
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//                // 当页面的状态改变时将调用此方法
//                //arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
//                Log.d("onPageScrollStateChange", arg0 + "");
//                switch (arg0) {
//                    case ViewPager.SCROLL_STATE_DRAGGING:
//                        // 正在拖动页面时执行此处
//                        handler.sendEmptyMessage(AdvertisementHandler.MSG_KEEP_SILENT);
//                        break;
//                    case ViewPager.SCROLL_STATE_IDLE:
//                        // 未拖动页面时执行此处
//                        handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//        });
//
//
//    }
//
//
//    /**
//     * 更新圈子信息
//     *
//     * @param model
//     */
//
//    private void bindHeaderData(AssociationDetailRecyListItemModel model, HeaderViewHolder headerViewHolder, boolean isUpdate) {
//
//        // 圈子信息
//        organizationInfo = model.getOrganizationInfo();
//
//        long chatId = organizationInfo.getChatId();
//
//        if (chatId > 0) {
//            headerViewHolder.ll_circle_name_and_num.setVisibility(View.VISIBLE);
//            if (organizationInfo.getChatInfo() != null) {
//                headerViewHolder.tv_circle_name.setText(organizationInfo.getChatInfo().getChatName());
//                Utils.ImageViewDisplayByUrl(organizationInfo.getChatInfo().getChatIcon(), headerViewHolder.iv_chat_icon);
//                headerViewHolder.tv_circle_online_num.setText(organizationInfo.getChatInfo().getOnlineNumber() + "人");
//                if (organizationInfo.getChatInfo().getIsLock() == 1) {
//                    headerViewHolder.lockView.setImageResource(R.drawable.lock_chatroom);
//                } else if (organizationInfo.getChatInfo().getIsLock() == 0) {
//                    headerViewHolder.lockView.setImageResource(R.drawable.unlock_chatroom);
//                }
//                headerViewHolder.lockView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (organizationInfo.getChatInfo().getUserRole() == 1) {
//                            charRoomOnClick.onClockClick(organizationInfo.getChatInfo());
//                        }
//                    }
//                });
//            }
//        } else {
//            headerViewHolder.ll_circle_name_and_num.setVisibility(View.GONE);
//        }
//
//        headerViewHolder.ll_circle_name_and_num.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChatInfo chatInfo = organizationInfo.getChatInfo();
//                if (chatInfo != null) {
//                    charRoomOnClick.onClick(chatInfo);
//                }
//
//            }
//        });
//
//        orgType = organizationInfo.getType();
//
//        //学校圈子
//        if (organizationInfo.getType() == 2) {
//            headerViewHolder.titleLayout1.setVisibility(View.VISIBLE);
//            // 学校圈子标题
//            headerViewHolder.titleTextView.setGravity(Gravity.CENTER);
//            headerViewHolder.titleTextView.setText((organizationInfo != null ? organizationInfo.getOrganName() : ""));
//            int lineCount = headerViewHolder.titleTextView.getLineCount();
//            if (lineCount > 1) {
//                headerViewHolder.titleTextView.setGravity(Gravity.CENTER_VERTICAL);
//            }
//
//            //学校圈子封面
//            if (mActivity != null && !mActivity.isFinishing()) {
//                if (!isUpdate) {
//                    Glide.with(mActivity).load(R.mipmap.org_school_header_bg).into(headerViewHolder.titleImageView);
//                }
//            }
//
//            // 返回按钮
//            headerViewHolder.leftTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (mActivity != null && !mActivity.isFinishing()) {
//                        mActivity.finish();
//                    }
//
//                }
//            });
//
//            // 标题左边按钮
//            headerViewHolder.rightImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (mActivity != null && !mActivity.isFinishing() && organizationInfo != null) {
//                        mActivity.startActivity(new Intent(mActivity, AssociationInfoActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo).putExtra(Constants.Fields.POSITION, 1));
//                    }
//
//                }
//            });
//
//            if (organizationInfo.getType() == 3) {
//                headerViewHolder. tv_anonymous_text.setVisibility(View.VISIBLE);
//            }
//
//
//        } else {// 普通圈子
//
//            headerViewHolder.titleLayout1.setVisibility(View.GONE);
//
//        }
//
//
//        // 通用组建绑定数据
//        // pk视频
//        headerViewHolder.middleBtnView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mActivity != null && !mActivity.isFinishing()) {
//                    Intent intent = new Intent(mActivity, AssociationDetailPKVideoListActivity.class);
//                    intent.putExtra(Constants.KEY.KEY_OBJECT, organizationInfo);
//                    intent.putExtra(Constants.Fields.IS_GAG, isGag);
//                    mActivity.startActivity(intent);
//                }
//
//            }
//        });
//
//        // 圈子下的活动
//        headerViewHolder.rightBtnView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (mActivity != null && !mActivity.isFinishing()) {
//                    Intent intent = new Intent(mActivity, AssociationDetailInActiveListActivity.class);
//                    intent.putExtra(Constants.KEY.KEY_OBJECT, organizationInfo);
//                    intent.putExtra(Constants.Fields.IS_GAG, isGag);
//                    mActivity.startActivity(intent);
//                }
//
//            }
//        });
//
//        // 圈子封面
//        ImageView imageView = headerViewHolder.imageView;
//        if (organizationInfo.getOrganImg() != null && mActivity != null && !mActivity.isFinishing()) {
//            if (!isUpdate) {
//                Glide.with(mActivity).load(organizationInfo.getOrganImg()).apply(new RequestOptions().centerCrop().transform(new GlideRoundTransform(9)).placeholder(R.mipmap.error_bg)).into(imageView);
//            }
//        }
//
//        // 圈子昵称
//        headerViewHolder.nickTextView.setText(organizationInfo.getOrganName());
//        if (mActivity != null && !mActivity.isFinishing()) {
//            // 成员数据
//            SpannableString spannableString = new SpannableString("成员 " + organizationInfo.getMemberCount() + " (# " + organizationInfo.getTotalMen() + "人，# " + organizationInfo.getTotalWomen() + "人)");
//            // 男性图标替换
//            ImageSpan imgBoySpan = new ImageSpan(mActivity.getApplicationContext(), R.mipmap.boy_icon);
//            int statIndex = 3 + String.valueOf(organizationInfo.getMemberCount()).length() + 2;
//            int endIndex = statIndex + 1;
//            spannableString.setSpan(imgBoySpan, statIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            // 女性图标替换
//            statIndex = endIndex + 1 + String.valueOf(organizationInfo.getTotalMen()).length() + 2;
//            endIndex = statIndex + 1;
//            ImageSpan imgGirlSpan = new ImageSpan(mActivity.getApplicationContext(), R.mipmap.girl_icon);
//            spannableString.setSpan(imgGirlSpan, statIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            headerViewHolder.memberTextView.setText(spannableString);
//
//        }
//
//        // 圈子id
//        headerViewHolder.idTextView.setText("ID:" + organizationInfo.getOrganId() + "");
//
//        // 帖子数据
//        headerViewHolder.postCountView.setText("帖子 " + organizationInfo.getPostCount());
//
//        // 圈子简介
//        headerViewHolder.descTextView.setText(organizationInfo.getDescription());
//
//
//        // 圈子申请状态
//        if (organizationInfo.getUserRole() == 4) {
//            headerViewHolder.btnView1.setText("申请中");
//            headerViewHolder.btnView1.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//            headerViewHolder.btnView1.setOnClickListener(null);
//        } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2 || organizationInfo.getUserRole() == 3) {
//            headerViewHolder.btnView1.setText("已加入");
//            if (mActivity != null && !mActivity.isFinishing()) {
//                headerViewHolder.btnView1.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
//            }
//
//            headerViewHolder.btnView1.setBackgroundResource(R.drawable.shape_round_10_stroke_d8d8d8);
//            headerViewHolder.btnView1.setOnClickListener(null);
//        } else {
//            headerViewHolder.btnView1.setText("加入");
//            headerViewHolder.btnView1.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//            if (mActivity != null && !mActivity.isFinishing()) {
//                headerViewHolder.btnView1.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
//            }
//
//            headerViewHolder.btnView1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //入圈验证(1-->直接加入,2-->申请同意后可加入),
//                    if (organizationInfo.getJoinLimit() == 2) {
//                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                        dialog.setText("", "本圈子为私密圈子，需要先发送申请，管理员同意后才能加入，发送？", "取消", "发送");
//                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                            @Override
//                            public void onLeftClick() {
//                                //取消
//                            }
//
//                            @Override
//                            public void onRightClick() {
//                                //申请
//
//                                if (joinOrganizationCallBack != null) {
//                                    joinOrganizationCallBack.joinOrganizationCallBack(organizationInfo, 2);
//                                }
//
//                            }
//                        });
//                        dialog.show(mActivity.getSupportFragmentManager());
//                    } else {
//
//                        if (joinOrganizationCallBack != null) {
//                            joinOrganizationCallBack.joinOrganizationCallBack(organizationInfo, 1);
//                        }
//                    }
//
//                }
//            });
//        }
//
//
//        // 分享按钮
//        headerViewHolder.btnView3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mActivity != null && !mActivity.isFinishing()) {
//                    ShareUtil.getInstance().showShare(mActivity, ShareUtil.ORGAIN, organizationInfo.getOrganId(), organizationInfo, "", "", "分享兴趣圈子“" + organizationInfo.getOrganName() + "社”，我们在友伴等你加入！", organizationInfo.getOrganImg(), true);
//                }
//            }
//        });
//
//
//        // 还没创建群
//        if (organizationInfo.getGroup() == null) {
//            headerViewHolder.layoutView2.setVisibility(View.GONE);
//            headerViewHolder.layoutView3.setVisibility(View.VISIBLE);
//
//            View layoutView4 = headerViewHolder.layoutView3.findViewById(R.id.layout_view_4);
//            TextView textView1 = headerViewHolder.layoutView3.findViewById(R.id.text_view_1);
//            textView1.setText("创建圈子聊天群组");
//
//            headerViewHolder.layoutView3.findViewById(R.id.add_image_view).setVisibility(View.VISIBLE);
//
//            // 所属角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0
//            if (organizationInfo.getUserRole() == 1) {
//                layoutView4.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//                layoutView4.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (mActivity != null && !mActivity.isFinishing()) {
//                            mActivity.startActivity(new Intent(mActivity, GroupCreateActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//                        }
//
//                    }
//                });
//
//            } else {
//
//                textView1.setText("圈主暂未开通圈子聊天群功能");
//                headerViewHolder.layoutView3.findViewById(R.id.add_image_view).setVisibility(View.GONE);
//                layoutView4.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//                layoutView4.setOnClickListener(null);
//
//            }
//
//            if (organizationInfo.getType() == 3) {
//                headerViewHolder.layoutView3.setVisibility(View.GONE);
//            }
//
//        } else {
//
//            headerViewHolder.layoutView2.setVisibility(View.VISIBLE);
//            headerViewHolder.layoutView3.setVisibility(View.GONE);
//
//            headerViewHolder.btnView2.setText("加入群");
//
//            // 群昵称
//            headerViewHolder.groupDescView.setText(organizationInfo.getGroup().getGroupName());
//            // (0不在,1在)
//            if (organizationInfo.getGroup().getIsExistGroup() == 0) {
//                headerViewHolder.btnView2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (organizationInfo.getGroup().getGroupMode() == 2) {
//                            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                            dialog.setText("", "本群为私密群，需申请同意后才能加入，现在要申请入群吗？", "取消", "申请进群");
//                            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                                @Override
//                                public void onLeftClick() {
//                                    //取消
//                                }
//
//                                @Override
//                                public void onRightClick() {
//                                    //申请
////
//                                    if (joinGroupCallBack != null) {
//                                        joinGroupCallBack.joinGroupCallBack(organizationInfo.getGroup());
//                                    }
//                                }
//                            });
//                            dialog.show(mActivity.getSupportFragmentManager());
//
//                        } else {
//
//                            if (joinGroupCallBack != null) {
//                                joinGroupCallBack.joinGroupCallBack(organizationInfo.getGroup());
//                            }
//                        }
//
//
//                    }
//                });
//            } else if (organizationInfo.getGroup().getIsExistGroup() == 2) {
//                headerViewHolder.btnView2.setText("申请中");
//                headerViewHolder.btnView2.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//                headerViewHolder.btnView2.setOnClickListener(null);
//            } else {
//                headerViewHolder.btnView2.setText("发起群聊");
//                headerViewHolder.btnView2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (organizationInfo.getGroup() != null && mActivity != null && !mActivity.isFinishing()) {
//                            RongIMutils.startGroupChat(mActivity, organizationInfo.getGroup().getGroupId() + "", organizationInfo.getGroup().getGroupName());
//                        }
//
//                    }
//                });
//            }
//        }
//
//
//        if (organizationInfo.getType()==3){
//            headerViewHolder.tv_anonymous_text.setVisibility(View.VISIBLE);
//        }
//
//
//    }
//
//
//    /**
//     * 绑定帖子数据
//     *
//     * @param viewHolder
//     * @param homeInfo
//     * @param position
//     * @param isUpdate
//     */
//    private void bindPostInfoData(PostInfoItemViewHolder viewHolder, final HomeInfo homeInfo, final int position, boolean isUpdate) {
//
//        if (viewHolder == null || homeInfo == null) {
//            return;
//        }
//
//        viewHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getItemCount() - 1) {
//            viewHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//
//        final PostInfo postInfo = homeInfo.getPostInfo();
//
//        if (postInfo == null) {
//            return;
//        }
//
//
//        final BaseUser baseUser = postInfo.getBaseUser();
//        if (baseUser != null) {
//            // 头像
//            if (!isUpdate) {
//                Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(viewHolder.userImg);
//            }
//
//            // 学校
//            viewHolder.schoolTextView.setText(baseUser.getSchoolName());
//
//            // 昵称
//            viewHolder.userNickNameTextView.setText(baseUser.getNickName());
//
//            //是否匿名发布帖子(1--->匿名，2--->不匿名)
//            if (postInfo.getIsAnonymous() == 2) {// 实名
//                if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //校友 并且不是自己
//                    viewHolder.alumnusView.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.alumnusView.setVisibility(View.GONE);
//                }
//
//                // 性别 1 男；2女
//                viewHolder.userAgeTextView.setText(baseUser.getAge() + "岁");
//                int sex = baseUser.getSex();
//                if (sex == 1) {
//                    //男性
//                    viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//                } else if (sex == 2) {
//                    //女性
//                    viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//                }
//                viewHolder.userLevelTextView.setVisibility(View.VISIBLE);
//                // 城市
//                viewHolder.cityTextView.setText(baseUser.getCity());
//                viewHolder.cityTextView.setPadding(Utils.dp2px(10), 0, 0, 0);
//                // 距离
//                viewHolder.locationTextView.setText(baseUser.getDistance());
//
//            } else {// 匿名
//
//                viewHolder.alumnusView.setVisibility(View.GONE);
//
//                viewHolder.userAgeTextView.setText("");
//
//                viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//
//                viewHolder.userLevelTextView.setVisibility(View.GONE);
//                // 城市
//                viewHolder.cityTextView.setText(baseUser.getCity());
//                viewHolder.cityTextView.setPadding(0, 0, 0, 0);
//                // 距离
//                viewHolder.locationTextView.setText(baseUser.getDistance());
//            }
//        }
//
//        //自己不显示城市
//        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//            viewHolder.cityTextView.setVisibility(View.GONE);
//        }
//
//        // 用户级别
//        Utils.getActiveLevel(viewHolder.userLevelTextView, postInfo.getActiveLevel());
//
//        // 时间
//        viewHolder.timeTextView.setText(postInfo.getAddTime());
//
//        //跟拍人以及内容
//        String content = postInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.contentTextView.setText(content);
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.contentTextView.setVisibility(View.GONE);
//        }
//
//        //招聘信息
//        final StoreInfo storeInfo=postInfo.getStoreInfo();
//        if(storeInfo!=null){
//            viewHolder.recruitTextView.setVisibility(View.VISIBLE);
//            viewHolder.contentTextView.setPadding(0,0,0,0);
//        }else{
//            viewHolder.recruitTextView.setVisibility(View.GONE);
//            viewHolder.contentTextView.setPadding(0,Utils.dp2px(15),0,0);
//        }
//        viewHolder.recruitTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                if(storeInfo.getUserId()!=SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)){
////                    itemWidgetOnClick.onApplyClerk(storeInfo.getStoreId());
////                }
//                //跳转到店铺详情
//                Intent intent=new Intent(mActivity,ShopDetailActivity.class);
//                intent.putExtra(Constants.Fields.STORE_ID,storeInfo.getStoreId());
//                mActivity.startActivity(intent);
//            }
//        });
//
//        final OriginalInfo originalInfo = postInfo.getOriginalInfo();
//        //标签展示
//        long labelId = postInfo.getLabelId();
//        String labelName = postInfo.getLabelName();
//        if (postInfo.getIsOriginal() == 1 && originalInfo != null && !StringUtils.isEmpty(labelName)) {
//            //跟拍加参赛
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//            viewHolder.contentTextView.setText(Utils.getFollowAndMatchContent(originalInfo, labelName, postInfo.getScondlabelList(), content));
//        } else {
//            //只有跟拍
//            if (postInfo.getIsOriginal() == 1 && originalInfo != null) {
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getFollowContent(originalInfo, content));
//            }
//            //只有参赛
//            if (!StringUtils.isEmpty(labelName)) {
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getMatchContent(labelId, labelName, postInfo.getScondlabelList(), content));
//            }
//        }
//        viewHolder.contentTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (postInfo.getIsOriginal() == 1 && originalInfo != null) {
//                    //跳转到视频广场界面
//                    Intent intent = new Intent(mActivity, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, postInfo.getParentId());
//                    intent.putExtra(Constants.Fields.TO_USER_ID, originalInfo.getUserId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_POST);
//                    mActivity.startActivity(intent);
//                } else {
//
//                    getOnItemListener().onItemClick(v, position);
//
//                }
//            }
//        });
//
//
//        if (orgType == 2) {
//            List<OrganBaseInfo> organBaseInfoList = postInfo.getOrganList();
//            if (organBaseInfoList != null && organBaseInfoList.size() > 0) {
//                OrganBaseInfo organBaseInfo = organBaseInfoList.get(0);
//                // 一级标签
//                viewHolder.labelFirstTextView.setText(organBaseInfo.getHobbyName());
//                //只有学校圈子才显示圈子标签
//                viewHolder.labelFirstTextView.setVisibility(View.VISIBLE);
//                // 二级标签
//                if (organBaseInfo != null) {
//                    if (organBaseInfo.getHobbyId() == 15 && organBaseInfoList.size() >= 2) {
//                        //匿名圈子 如果第一个是大学圈子就显示圈子列表标签的第二个 因为第一个是大学圈子 服务器没办法排序
//                        organBaseInfo = organBaseInfoList.get(1);
//                        if (organBaseInfo != null) {
//                            viewHolder.postLabelTwoLayout.setVisibility(View.VISIBLE);
//                            viewHolder.labelSecondTextView.setText(organBaseInfo.getOrganName());
//                        } else {
//                            viewHolder.postLabelTwoLayout.setVisibility(View.GONE);
//                        }
//                    } else {
//                        viewHolder.postLabelTwoLayout.setVisibility(View.VISIBLE);
//                        viewHolder.labelSecondTextView.setText(organBaseInfo.getOrganName());
//                    }
//                } else {
//                    viewHolder.postLabelTwoLayout.setVisibility(View.GONE);
//                }
//                final OrganBaseInfo finalOrganBaseInfo = organBaseInfo;
//                viewHolder.postLabelTwoLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //跳转到圈子详情
//                        if (finalOrganBaseInfo != null) {
//                            OrganizationInfo organizationInfo = new OrganizationInfo();
//                            organizationInfo.setOrganId(finalOrganBaseInfo.getOrganId());
//                            organizationInfo.setOrganName(finalOrganBaseInfo.getOrganName());
//                            mActivity.startActivity(new Intent(mActivity, AssociationDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//                        }
//                    }
//                });
//            }
//
//        }
//
//
//        //是否置顶(0取消,1置顶)
//        if (postInfo.getIsTop() == 1) {
//            viewHolder.postStickView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.postStickView.setVisibility(View.INVISIBLE);
//        }
//
//
//        // 分享
//        viewHolder.shareTextView.setText(Utils.bigNumberToStr(postInfo.getShareNumber(), postInfo.getShareNumberStr()));
//
//        viewHolder.shareTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String imgUrl = "";
//
//                if (postInfo.getImageList() != null && postInfo.getImageList().size() > 0) {
//                    imgUrl = postInfo.getImageList().get(0).getThumbnailUrl();
//                }
//
//                if (postInfo.getContentLimit() == 2 && organizationInfo.getType() == 2) {
//                    return;
//                }
//
//                ShareUtil.getInstance().showShare(mActivity, ShareUtil.POST, postInfo.getPostId(), postInfo, "", "", "", imgUrl, true);
//            }
//        });
//
//        // 点赞
//        viewHolder.praisNumTextView.setText(Utils.bigNumberToStr(postInfo.getPraiseCount(), postInfo.getPraiseCountStr()));
//        final int isPraise = postInfo.getIsPraise();
//        if (isPraise == 1) {
//            //1 : 已点赞
//            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//        } else {
//            //0: 未点赞
//            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//        }
//
//        // 点赞事件
//        viewHolder.praisNumTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (postInfo.getContentLimit() == 2 && organizationInfo.getType() == 2) {
//                    return;
//                }
//
//                //点赞 (1 点赞  2 取消点赞)
//                int type = (isPraise == 0 | isPraise == 2) ? 1 : 2;
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.onPraise(postInfo.getPostId(), type, position);
//                }
//                postInfo.setIsPraise(type);
//                postInfo.setPraiseCount(postInfo.getPraiseCount() + (type == 1 ? 1 : -1));
//                notifyItemChanged(position, true);
//
//            }
//        });
//
//        // 评论数量
//        viewHolder.commentNumTextView.setText(Utils.bigNumberToStr(postInfo.getCommentCount(), postInfo.getCommentCountStr()));
//
//        if (!isUpdate) {
//
//            // 图片显示区域
//            viewHolder.recyclerImage.setVisibility(View.GONE);
//            viewHolder.linearImageTwo.setVisibility(View.GONE);
//            viewHolder.imgFirst.setOnClickListener(null);
//
//
//            if (postInfo.getTopicType() == 2) {
//                //视频
//                viewHolder.linearImageTwo.setVisibility(View.VISIBLE);
//                viewHolder.videoPlayImg.setVisibility(View.VISIBLE);
//
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.imgFirst.getLayoutParams();
//                if (layoutParams != null) {
//                    layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                    viewHolder.imgFirst.setLayoutParams(layoutParams);
//                }
//
//                Glide.with(App.getInstance()).asBitmap().load(postInfo.getVideoImageUrl()).apply(options).into(viewHolder.imgFirst);
//
//                viewHolder.imgFirst.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //imgFirst
//                        Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
//                        intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_POST);
//                        intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, postInfo.getPostId());
//                        mActivity.startActivity(intent);
//                    }
//                });
//            } else {
//
//                final List<Image> imageList = postInfo.getImageList();
//                if (imageList != null && imageList.size() > 0) {
//
//                    if (imageList.size() == 1) {// 显示一张图片的样式
//                        viewHolder.linearImageTwo.setVisibility(View.VISIBLE);
//                        viewHolder.videoPlayImg.setVisibility(View.GONE);
//
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.imgFirst.getLayoutParams();
//                        if (layoutParams != null) {
//                            if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
//                                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                                layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                                viewHolder.imgFirst.setLayoutParams(layoutParams);
//                                Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.imgFirst);
//                            } else {
//                                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                                layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                                viewHolder.imgFirst.setLayoutParams(layoutParams);
//                                Glide.with(App.getInstance()).asBitmap().load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.imgFirst);
//                            }
//
//                        }
//
//                        viewHolder.imgFirst.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (mActivity != null && !mActivity.isFinishing()) {
//                                    ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
//                                }
//                            }
//                        });
//
//
//                    } else {// 显示多张图片的样式
//                        PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
//                        viewHolder.recyclerImage.setAdapter(postImageLIstAdapter);
//
//                        postImageLIstAdapter.setOnItemListener(new ItemListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//
//                                if (mActivity != null && !mActivity.isFinishing()) {
//                                    ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
//                                }
//
//                            }
//                        });
//
//                        viewHolder.recyclerImage.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        }
//
//
//        // 更多按钮
//        viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//
//        if (orgType == 2 && postInfo.getContentLimit() == 2) {
//            viewHolder.contentTextView.setText("发布内容只限圈内可见");
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//            viewHolder.linearImageTwo.setVisibility(View.GONE);
//            viewHolder.recyclerImage.setVisibility(View.GONE);
//        }
//
//        viewHolder.userImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //跳到用户详情(自己和匿名不可点击)
//                    if (postInfo.getIsAnonymous() == 1) {
//                        oneToOneCharOnClick.onClick(baseUser.getUserId());
//                    } else {
//                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                    }
//                }
//            }
//        });
//
//    }
//
//
//    /**
//     * 绑定活动数据
//     *
//     * @param activeHolder
//     * @param homeInfo
//     * @param position
//     * @param isUpdate
//     */
//    private void bindActiveItemData(ActiveItemViewHolder activeHolder, final HomeInfo homeInfo, final int position, boolean isUpdate) {
//
//        if (activeHolder == null || homeInfo == null) {
//            return;
//        }
//
//        final ActivityInfo activityInfo = homeInfo.getActivityInfo();
//        if (activityInfo == null) {
//            return;
//        }
//
//        activeHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getItemCount() - 1) {
//            activeHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//        // 跳转到附近活动
//        activeHolder.activetopView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mActivity != null) {
//
//                    Intent intent = new Intent(mActivity, ActiveListActivity.class);
//                    intent.putExtra(Constants.Fields.TO_USER_ID, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//                    intent.putExtra(Constants.Fields.COME_IN_TYPE, 1);
//                    mActivity.startActivity(intent);
//
//
//                }
//            }
//        });
//
//        // 用户信息
//        final BaseUser baseUser = homeInfo.getBaseUser();
//        if (baseUser != null) {
//            // 用户头像
//            if (!isUpdate) {
//                Glide.with(mActivity).asBitmap().apply(userOptions).load(baseUser.getUserIcon()).into(activeHolder.userImg);
//            }
//
//            activeHolder.userImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                        //跳到用户详情
//                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                    }
//                }
//            });
//
//            // 学校
//            activeHolder.schoolTextView.setText(baseUser.getSchoolName());
//
//            if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                //校友 并且不是自己
//                activeHolder.alumnusView.setVisibility(View.VISIBLE);
//            } else {
//                activeHolder.alumnusView.setVisibility(View.GONE);
//            }
//
//            // 用户昵称
//            activeHolder.nickNameView.setText(baseUser.getNickName());
//
//            int sex = baseUser.getSex();
//            if (sex == 1) {
//                //男性
//                activeHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//            } else if (sex == 2) {
//                //女性
//                activeHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//            }
//            activeHolder.userAgeView.setText(baseUser.getAge() + "岁");
//
//            // 城市
//            activeHolder.cityTextView.setText(baseUser.getCity());
//
//            // 位置
//            activeHolder.locationView.setText(baseUser.getDistance());
//
//        }
//
//        // 用户级别
//        Utils.getActiveLevel(activeHolder.userLevelView, activityInfo.getActiveLevel());
//
//        // 活动时间
//        activeHolder.timeTextView.setText(activityInfo.getActivityTime());
//
//        // 活动状态(1报名中,2报名结束)
//        activeHolder.activeStatusImgView.setVisibility(View.INVISIBLE);
//        int status = activityInfo.getStatus();
//        if (status == 2) {
//            activeHolder.activeStatusImgView.setVisibility(View.VISIBLE);
//            activeHolder.activeStatusImgView.setImageResource(R.mipmap.active_status_unenable_icon);
//        }
//
//        // 是否置顶(0取消,1置顶)
//        if (activityInfo.getIsTop() == 0) {
//            activeHolder.topView.setVisibility(View.INVISIBLE);
//        } else {
//            activeHolder.topView.setVisibility(View.VISIBLE);
//        }
//
//        // 活动的兴起标签
//        if (!isUpdate) {
//            List<HobbyInfo> hobbyInfoList = activityInfo.getHobbyList();
//            if (hobbyInfoList != null) {
//                ActiveListHobbyRecyclerViewAdapter hobbyRecyclerViewAdapter = new ActiveListHobbyRecyclerViewAdapter(mActivity);
//                hobbyRecyclerViewAdapter.setList(hobbyInfoList);
//                activeHolder.hobbyRecycleView.setAdapter(hobbyRecyclerViewAdapter);
//            }
//        }
//
//        if (!isUpdate) {
//            final ActiveListOrganizationRecyclerViewAdapter activeListOrganizationRecyclerViewAdapter = new ActiveListOrganizationRecyclerViewAdapter(mActivity);
//            activeListOrganizationRecyclerViewAdapter.setList((activityInfo.getOrganList() != null && activityInfo.getOrganList().size() > 3) ? activityInfo.getOrganList().subList(0, 3) : activityInfo.getOrganList());
//            activeHolder.orgainzationRecycleView.setAdapter(activeListOrganizationRecyclerViewAdapter);
//            activeListOrganizationRecyclerViewAdapter.setOnItemListener(new ItemListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    //跳转到圈子详情
//                    OrganizationInfo organizationInfo = activeListOrganizationRecyclerViewAdapter.getItem(position);
//                    mActivity.startActivity(new Intent(mActivity, AssociationDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//                }
//            });
//        }
//
//
//        if (activityInfo.getOrganList() != null && activityInfo.getOrganList().size() > 2) {
//            activeHolder.moreImgView.setVisibility(View.VISIBLE);
//        } else {
//            activeHolder.moreImgView.setVisibility(View.INVISIBLE);
//        }
//
//        // 圈子更多标签
//        activeHolder.moreImgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到圈子列表页面
//                Intent intent = new Intent(mActivity, AssociationTypeListActivity.class);
//                if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //我的圈子列表
//                    intent.putExtra(Constants.Fields.TYPE, 2);
//                } else {
//                    //他的圈子列表
//                    intent.putExtra(Constants.Fields.TYPE, 3);
//                }
//                intent.putExtra(Constants.Fields.Ta_USER_ID, baseUser.getUserId());
//                mActivity.startActivity(intent);
//
//            }
//        });
//
//
//        // 图片显示
//        if (!isUpdate) {
//            activeHolder.activeImgView.setOnClickListener(null);
//            String imgUrl = null;
//            if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                activeHolder.activeImgView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (mActivity != null && !mActivity.isFinishing()) {
//                            ImageViewPageDialog.newInstance(activityInfo.getImageList(), 0).show(mActivity.getSupportFragmentManager());
//                        }
//                    }
//                });
//            }
//
//            // 显示默认图
//            if (StringUtils.isEmpty(imgUrl)) {
//                String host = ApiData.getInstance().getHost();
//                host = host.substring(0, host.lastIndexOf("/"));
//                imgUrl = host + "/uploadfile/system/activity/activity_img.png";
//            }
//
//            Utils.CornerImageViewDisplayByUrl(imgUrl, activeHolder.activeImgView);
//
//        }
//
//
//        // 活动主题
//        activeHolder.activeContentView.setText(activityInfo.getTheme());
//
//        // 更多按钮
//        activeHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//
//    }
//
//
//    /**
//     * 绑定PK数据
//     *
//     * @param pkHolder
//     * @param homeInfo
//     * @param position
//     * @param isUpdate
//     */
//    private void bindPKData(PKItemViewHolder pkHolder, final HomeInfo homeInfo, final int position, boolean isUpdate) {
//
//        if (pkHolder == null || homeInfo == null) {
//            return;
//        }
//
//        final PlayInfo playInfo = homeInfo.getPlayInfo();
//        if (playInfo == null) {
//            return;
//        }
//
//        pkHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getItemCount() - 1) {
//            pkHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//
//        // 用户信息
//        final BaseUser baseUser = homeInfo.getBaseUser();
//        if (baseUser != null) {
//            // 用户头像
//            if (!isUpdate) {
//                Glide.with(mActivity).asBitmap().apply(options).load(baseUser.getUserIcon()).into(pkHolder.userImg);
//            }
//
//            pkHolder.userImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                        //跳到用户详情
//                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                    }
//                }
//            });
//
//            if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                //校友 并且不是自己
//                pkHolder.alumnusView.setVisibility(View.VISIBLE);
//            } else {
//                pkHolder.alumnusView.setVisibility(View.GONE);
//            }
//
//            // 学校
//            pkHolder.schoolTextView.setText(baseUser.getSchoolName());
//
//            // 用户昵称
//            pkHolder.nickNameView.setText(baseUser.getNickName());
//
//            int sex = baseUser.getSex();
//            if (sex == 1) {
//                //男性
//                pkHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//            } else if (sex == 2) {
//                //女性
//                pkHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//            }
//            pkHolder.userAgeView.setText(baseUser.getAge() + "岁");
//
//            // 城市
//            pkHolder.cityTextView.setText(baseUser.getCity());
//
//            // 位置
//            pkHolder.locationView.setText(baseUser.getDistance());
//
//        }
//
//        // 用户级别
//        Utils.getActiveLevel(pkHolder.userLevelView, playInfo.getActiveLevel());
//
//        // 是否置顶(0取消,1置顶)
//        if (playInfo.getIsTop() == 0) {
//            pkHolder.topView.setVisibility(View.INVISIBLE);
//        } else {
//            pkHolder.topView.setVisibility(View.VISIBLE);
//        }
//
//        // 活动时间
//        pkHolder.timeTextView.setText(playInfo.getAddTime());
//
//        // pk标题
//        if (playInfo.getLaunchPlay() != null) {
//            pkHolder.pkTitleTextView.setText(playInfo.getLaunchPlay().getTitle());
//        }
//
//        // PK描述(个人用户编辑的内容)
//        pkHolder.pkContentTextView.setText(playInfo.getTitle());
//
//        // pk视频
//        if (!isUpdate) {
//            Glide.with(App.getInstance()).asBitmap().load(playInfo.getVideoImageUrl()).apply(options).into(pkHolder.pkImageView);
//        }
//
//
//        // 更多按钮
//        pkHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//
//    }
//
//    /**
//     * 绑定表白墙
//     *
//     * @param viewHolder
//     * @param homeInfo
//     * @param position
//     */
//    private void bindSayLoveItemData(SayLoveItemViewHolder viewHolder, final HomeInfo homeInfo, final int position, boolean isUpdate) {
//
//        if (viewHolder == null || homeInfo == null) {
//            return;
//        }
//
//        if (homeInfo.getSayLoveInfo() == null) {
//            return;
//        }
//
//        viewHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getItemCount() - 1) {
//            viewHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//
//        viewHolder.userLevelTextView.setVisibility(View.GONE);
//
//
//        final SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//        final BaseUser baseUser = sayLoveInfo.getBaseUser();
//
//        final int isAnonymous = sayLoveInfo.getIsAnonymous();
//
//        // 头像
//        if (!isUpdate) {
//            Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(viewHolder.userImg);
//        }
//
//        viewHolder.userImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //跳到用户详情(自己和匿名不可点击)
//                    if (sayLoveInfo.getIsAnonymous() == 1) {
//                        oneToOneCharOnClick.onClick(baseUser.getUserId());
//                    } else {
//                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                    }
//                }
//            }
//        });
//
//        // 昵称
//        viewHolder.userNickNameTextView.setText(baseUser.getNickName());
//
//        //学校名称
//        if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
//            viewHolder.schoolNameTextView.setVisibility(View.VISIBLE);
//            viewHolder.schoolNameTextView.setText(baseUser.getSchoolName());
//        } else {
//            viewHolder.schoolNameTextView.setVisibility(View.GONE);
//        }
//
//        //校友
//        if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//            //校友 并且不是自己
//            viewHolder.alumnus_tv.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.alumnus_tv.setVisibility(View.GONE);
//        }
//
//        // 年龄
//        viewHolder.userAgeTextView.setText(baseUser.getAge() + "岁");
//
//        // 性别
//        int sex = baseUser.getSex();
//        if (sex == 1) {
//            //男性
//            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//        } else if (sex == 2) {
//            //女性
//            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//        }
//
//        // 发布时间
//        viewHolder.timeTextView.setText(sayLoveInfo.getAddTime());
//
//        // 距离
//        String distance = baseUser.getDistance();
//        if ((!StringUtils.isEmpty(distance))) {
//            viewHolder.locationTextView.setText(distance);
//            viewHolder.locationTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.locationTextView.setVisibility(View.GONE);
//        }
//
//        // 城市
//        if ((!StringUtils.isEmpty(baseUser.getCity()))) {
//            viewHolder.cityTextView.setText(baseUser.getCity());
//            viewHolder.cityTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.cityTextView.setVisibility(View.GONE);
//        }
//
//
////        if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
////            //自己不展示位置
////            viewHolder.locationTextView.setVisibility(View.GONE);
////            viewHolder.cityTextView.setVisibility(View.GONE);
////        }
//
//        //跟拍人以及内容
//        String content = sayLoveInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.contentTextView.setText(content);
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.contentTextView.setVisibility(View.GONE);
//        }
//
//
//        final OriginalInfo originalInfo = sayLoveInfo.getOriginalInfo();
//        //标签展示
//        long labelId = sayLoveInfo.getLabelId();
//        String labelName = sayLoveInfo.getLabelName();
//        if (sayLoveInfo.getIsOriginal() == 1 && originalInfo != null && !StringUtils.isEmpty(labelName)) {
//            //跟拍加参赛
//            viewHolder.contentTextView.setVisibility(View.VISIBLE);
//            viewHolder.contentTextView.setText(Utils.getFollowAndMatchContent(originalInfo, labelName, sayLoveInfo.getScondlabelList(), content));
//        } else {
//            //只有跟拍
//            if (sayLoveInfo.getIsOriginal() == 1 && originalInfo != null) {
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getFollowContent(originalInfo, content));
//            }
//            //只有参赛
//            if (!StringUtils.isEmpty(labelName)) {
//                viewHolder.contentTextView.setVisibility(View.VISIBLE);
//                viewHolder.contentTextView.setText(Utils.getMatchContent(labelId, labelName, sayLoveInfo.getScondlabelList(), content));
//            }
//        }
//        viewHolder.contentTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sayLoveInfo.getIsOriginal() == 1 && originalInfo != null) {
//                    //跳转到视频广场界面
//                    Intent intent = new Intent(mActivity, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, sayLoveInfo.getParentId());
//                    intent.putExtra(Constants.Fields.TO_USER_ID, originalInfo.getUserId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_SAYLOVE);
//                    mActivity.startActivity(intent);
//                } else {
//
//                    getOnItemListener().onItemClick(v, position);
//
//                }
//            }
//        });
//
//
//        // 是否匿名
//        if (sayLoveInfo.getIsAnonymous() != 1) {
//            viewHolder.userAgeTextView.setVisibility(View.VISIBLE);
//            viewHolder.userAgeTextView.setPadding(0, 0, Utils.dp2px(10), 0);
//        } else {
//            viewHolder.userAgeTextView.setVisibility(View.VISIBLE);
//            viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//            viewHolder.userAgeTextView.setText("");
//            viewHolder.userAgeTextView.setPadding(0, 0, 0, 0);
//        }
//
//
//        // 分享数量
//        int shareCount = sayLoveInfo.getShareNumber();
//        String shareCountStr = sayLoveInfo.getShareNumberStr();
//        viewHolder.shareNumTextView.setText(Utils.bigNumberToStr(shareCount, shareCountStr));
//
//        // 分享按钮事件
//        viewHolder.shareNumTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //分享弹窗
//                String imgUrl = "";
//                if (sayLoveInfo.getConfessionType() == 2) {
//                    imgUrl = sayLoveInfo.getVideoImageUrl();
//                } else {
//                    if (sayLoveInfo.getImageList() != null && sayLoveInfo.getImageList().size() > 0) {
//                        imgUrl = sayLoveInfo.getImageList().get(0).getThumbnailUrl();
//                    }
//                }
//                ShareUtil.getInstance().showShare(mActivity, ShareUtil.SAYLOVE, sayLoveInfo.getSayLoveId(), sayLoveInfo, "", "", "", imgUrl, true);
//
//            }
//        });
//
//        // 点赞
//        int praiseCount = sayLoveInfo.getPraiseCount();
//        String praiseCountStr = sayLoveInfo.getPraiseCountStr();
//        viewHolder.praisNumTextView.setText(Utils.bigNumberToStr(praiseCount, praiseCountStr));
//
//
//        final int isPraise = sayLoveInfo.getIsPraise();
//        if (isPraise == 1) {
//            //1 : 已点赞
//            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//        } else {
//            //0: 未点赞
//            viewHolder.praisNumTextView.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//        }
//
//        //点赞事件 (1 点赞  2 取消点赞),
//        viewHolder.praisNumTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.onPraise(sayLoveInfo.getSayLoveId(), homeInfo.getType(), position);
//                }
//
//            }
//        });
//
//        // 评论数量
//        int commentCount = sayLoveInfo.getCommentCount();
//        String commentCountStr = sayLoveInfo.getCommentCountStr();
//        viewHolder.commentNumTextView.setText(Utils.bigNumberToStr(commentCount, commentCountStr));
//
//        if (orgType == 2) {
//            // 一级标签
//            viewHolder.labelFirstTextView.setText("");
//            viewHolder.labelFirstTextView.setVisibility(View.VISIBLE);
//
//            viewHolder.labelSecondTextView.setText("校园表白墙");
//            viewHolder.labelTwoTextTipsView.setText("");
//            viewHolder.postLabelTwoLayout.setVisibility(View.VISIBLE);
//
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.postLabelTwoLayout.getLayoutParams();
//            layoutParams.leftMargin = 0;
//
//            viewHolder.postLabelTwoLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //附近表白
//                    Intent intent = new Intent(mActivity, SayLoveListActivity.class);
//                    intent.putExtra(Constants.Fields.USER_ID, 0);
//                    intent.putExtra("comeInType", 1);
//                    mActivity.startActivity(intent);
//                }
//            });
//
//        } else {
//            // 一级标签
//            // 二级标签
//
//            viewHolder.labelFirstTextView.setVisibility(View.INVISIBLE);
//            viewHolder.labelSecondTextView.setVisibility(View.INVISIBLE);
//        }
//
//
//        // 图片显示区域
//        if (!isUpdate) {
//            viewHolder.recycler_image.setVisibility(View.GONE);
//            viewHolder.linear_image_two.setVisibility(View.GONE);
//            if (sayLoveInfo.getConfessionType() == 2) {
//                viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//                viewHolder.video_play_img.setVisibility(View.VISIBLE);
//                Utils.CornerImageViewDisplayByUrl(sayLoveInfo.getVideoImageUrl(), viewHolder.img_first);
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                viewHolder.img_first.setLayoutParams(layoutParams);
//                viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //跳转到视频播放界面
//                        Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
//                        intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_SAYLOVE);
//                        intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, sayLoveInfo.getSayLoveId());
//                        mActivity.startActivity(intent);
//                    }
//                });
//            } else {
//                final List<Image> imageList = sayLoveInfo.getImageList();
//                if (imageList != null) {
//                    if (imageList.size() > 0 && imageList.size() <= 1) {
//
//                        viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//                        viewHolder.video_play_img.setVisibility(View.GONE);
//                        if (imageList.size() == 1) {
//                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//
//                            if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
//                                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                                layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                                viewHolder.img_first.setLayoutParams(layoutParams);
//                                Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.img_first);
//                            } else {
//                                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                                layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                                viewHolder.img_first.setLayoutParams(layoutParams);
//                                Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(options).into(viewHolder.img_first);
//                            }
//
//
//                            viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (mActivity != null) {
//                                        ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
//                                    }
//                                }
//                            });
//                        }
//                    } else if (imageList.size() > 1) {
//                        viewHolder.linear_image_two.setVisibility(View.GONE);
//                        PostImageRecycleAdapter postImageLIstAdapter = new PostImageRecycleAdapter(mActivity, imageList, 3, Utils.dp2px(9));
//                        viewHolder.recycler_image.setAdapter(postImageLIstAdapter);
//
//                        postImageLIstAdapter.setOnItemListener(new ItemListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                if (mActivity != null) {
//                                    ImageViewPageDialog.newInstance(imageList, position).show(mActivity.getSupportFragmentManager());
//                                }
//                            }
//                        });
//
//                        viewHolder.recycler_image.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        }
//
//
//        // 更多按钮
//        viewHolder.moreImg.setVisibility(View.VISIBLE);
//        viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//
//    }
//
//
//    /**
//     * 绑定同学帮帮忙数据
//     *
//     * @param viewHolder
//     * @param homeInfo
//     * @param position
//     */
//    private void bindClassMateHelpData(ClassMateHelpItemViewHolder viewHolder, final HomeInfo homeInfo, final int position, boolean isUpdate) {
//        if (viewHolder == null || homeInfo == null) {
//            return;
//        }
//
//        if (homeInfo.getHelpInfo() == null) {
//            return;
//        }
//
//        final HelpInfo helpInfo = homeInfo.getHelpInfo();
//
//        viewHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getItemCount() - 1) {
//            viewHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//        // 用户信息
//        final BaseUser baseUser = homeInfo.getBaseUser();
//        if (baseUser != null) {
//            // 头像
//            if (!isUpdate) {
//                Glide.with(mActivity).load(baseUser.getUserIcon()).apply(options).into(viewHolder.user_img);
//            }
//
//            // 昵称
//            viewHolder.user_nickname_text.setText(baseUser.getNickName());
//            // 学校名称
//            String schoolName = baseUser.getSchoolName();
//            viewHolder.tv_school_name.setText(schoolName);
//
//            //年龄
//            if (baseUser.getSex() == 1) {
//                //男性
//                viewHolder.user_age_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//            } else if (baseUser.getSex() == 2) {
//                //女性
//                viewHolder.user_age_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//            }
//
//            // 年龄
//            viewHolder.user_age_text.setText(helpInfo.getBaseUser().getAge() + "岁");
//            viewHolder.user_age_text.setPadding(0, Utils.dp2px(7), Utils.dp2px(10), 0);
//
//            // 显示校友标识
//            int isSchoolmate = baseUser.getIsSchoolmate();
//            if (isSchoolmate == 2) {
//                viewHolder.alumnus_text.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.alumnus_text.setVisibility(View.INVISIBLE);
//            }
//
//            // 点击跳转到用户详情
//            viewHolder.user_img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getInt(Constants.Fields.USER_ID, 0)) {
//                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                    }
//                }
//            });
//        }
//
//        //城市
//        viewHolder.user_city.setText(helpInfo.getCity());
//
//        if (orgType == 2) {
//            viewHolder.fromTagView.setVisibility(View.VISIBLE);
//            viewHolder.fromTagLayoutView.setVisibility(View.VISIBLE);
//            viewHolder.fromTagLayoutView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //同学帮帮忙
//                    if (mActivity != null) {
//                        Intent intent = new Intent(mActivity, ClassMateHelpListActivity.class);
//                        intent.putExtra(Constants.Fields.TYPE, 1);
//                        mActivity.startActivity(intent);
//                    }
//
//                }
//            });
//
//        } else {
//            viewHolder.fromTagView.setVisibility(View.GONE);
//            viewHolder.fromTagLayoutView.setVisibility(View.GONE);
//        }
//
//        // 发布时间
//        viewHolder.time_text.setText(helpInfo.getAddTime());
//        // 显示距离
//        viewHolder.distance_text.setText(helpInfo.getDistance());
//
//        //跟拍人以及内容
//        String content = helpInfo.getContent();
//        if (!StringUtils.isEmpty(content)) {
//            viewHolder.content_text.setText(content);
//            viewHolder.content_text.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.content_text.setVisibility(View.GONE);
//        }
//
//
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
//                if (helpInfo.getIsOriginal() == 1 && originalInfo != null) {
//                    //跳转到视频广场界面
//                    Intent intent = new Intent(mActivity, PKParkActivity.class);
//                    intent.putExtra(Constants.Fields.PARENT_ID, helpInfo.getParentId());
//                    intent.putExtra(Constants.Fields.TO_USER_ID, originalInfo.getUserId());
//                    intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_HELPINFO);
//                    mActivity.startActivity(intent);
//                } else {
//
//                    getOnItemListener().onItemClick(v, position);
//
//                }
//            }
//        });
//
//
//        // 普通文图
//        if (!isUpdate) {
//            if (helpInfo.getHelpType() == 1) {
//                final List<Image> imageList = helpInfo.getImageList();
//                if (imageList != null) {
//                    if (imageList.size() == 1) {
//                        viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//                        viewHolder.video_play_img.setVisibility(View.GONE);
//                        viewHolder.recycler_image.setVisibility(View.GONE);
//
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//
//                        if (!StringUtils.isEmpty(imageList.get(0).getImageUrl()) && imageList.get(0).getImageUrl().toLowerCase().endsWith(".gif")) {
//                            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                            viewHolder.img_first.setLayoutParams(layoutParams);
//                            Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.img_first);
//
//                        } else {
//                            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                            layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                            viewHolder.img_first.setLayoutParams(layoutParams);
//                            Glide.with(App.getInstance()).asGif().load(imageList.get(0).getImageUrl()).apply(gifOptions).into(viewHolder.img_first);
//                        }
//
//                        viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (mActivity != null) {
//                                    ImageViewPageDialog.newInstance(imageList, 0).show(mActivity.getSupportFragmentManager());
//                                }
//                            }
//                        });
//
//                    } else if (imageList.size() > 1) {
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
//                } else {
//                    viewHolder.linear_image_two.setVisibility(View.GONE);
//                    viewHolder.recycler_image.setVisibility(View.GONE);
//                }
//
//            } else if (helpInfo.getHelpType() == 2) {//视频
//                viewHolder.linear_image_two.setVisibility(View.VISIBLE);
//                viewHolder.video_play_img.setVisibility(View.VISIBLE);
//                viewHolder.recycler_image.setVisibility(View.GONE);
//                //视频
//                Utils.CornerImageViewDisplayByUrl(helpInfo.getVideoImageUrl(), viewHolder.img_first);
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img_first.getLayoutParams();
//                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                layoutParams.height = (screenWidth - Utils.dp2px(25)) * 384 / 700;
//                viewHolder.img_first.setLayoutParams(layoutParams);
//                viewHolder.img_first.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //跳转到视频播放界面
//                        Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
//                        intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_HELPINFO);
//                        intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, helpInfo.getHelpId());
//                        mActivity.startActivity(intent);
//                    }
//                });
//            }
//
//        }
//
//        // 点赞
//        viewHolder.praise_text.setText(Utils.bigNumberToStr(helpInfo.getPraiseCount(), helpInfo.getPraiseCountStr()));
//        if (helpInfo.getIsPraise() == 1) {
//            //1 : 已点赞
//            viewHolder.praise_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_icon), null, null, null);
//        } else {
//            //0: 未点赞
//            viewHolder.praise_text.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.home_post_praise_nor_icon), null, null, null);
//        }
//        viewHolder.praise_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.onPraise(helpInfo.getHelpId(), homeInfo.getType(), position);
//                }
//
//            }
//        });
//
//        // 评论数量
//        viewHolder.comment_text.setText(Utils.bigNumberToStr(helpInfo.getCommentCount(), helpInfo.getCommentCountStr()));
//
//        // 更多按钮
//        viewHolder.moreImg.setVisibility(View.VISIBLE);
//        viewHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//    }
//
//
//    static class PostInfoItemViewHolder extends BaseViewHolder {
//
//        View convertView;
//        ImageView userImg, imgFirst, videoPlayImg, isMatchImg;
//        TextView userNickNameTextView, userAgeTextView, timeTextView, locationTextView, contentTextView,recruitTextView,
//                labelFirstTextView, labelSecondTextView, praisNumTextView, commentNumTextView;
//
//        TextView schoolTextView;
//
//        ImageView userLevelTextView;
//        ImageView postStickView;
//
//        RelativeLayout linearImageTwo;
//        RecyclerView recyclerImage;
//
//        ImageView alumnusView;
//        ImageView moreImg;
//        TextView shareTextView;
//
//        TextView cityTextView;
//        View lineView2;
//
//        RelativeLayout postLabelTwoLayout;
//
//
//        public PostInfoItemViewHolder(View itemView) {
//            super(itemView);
//
//            convertView = itemView;
//            userImg = (ImageView) convertView.findViewById(R.id.user_img);
//            schoolTextView = (TextView) convertView.findViewById(R.id.school_text_view);
//            userNickNameTextView = (TextView) convertView.findViewById(R.id.user_nickname_text);
//            userLevelTextView = (ImageView) convertView.findViewById(R.id.user_level_text);
//            isMatchImg = (ImageView) convertView.findViewById(R.id.ismatch_img);
//            userAgeTextView = (TextView) convertView.findViewById(R.id.user_age_text);
//            timeTextView = (TextView) convertView.findViewById(R.id.time_text);
//            alumnusView = (ImageView) convertView.findViewById(R.id.alumnus_text);
//            cityTextView = (TextView) convertView.findViewById(R.id.city_text);
//            locationTextView = (TextView) convertView.findViewById(R.id.location_text);
//            contentTextView = (TextView) convertView.findViewById(R.id.content_text);
//            recruitTextView= (TextView) convertView.findViewById(R.id.recruit_text);
//
//            labelFirstTextView = (TextView) convertView.findViewById(R.id.post_label_one_text);
//            labelSecondTextView = (TextView) convertView.findViewById(R.id.post_label_two_text);
//            praisNumTextView = (TextView) convertView.findViewById(R.id.praise_text);
//            commentNumTextView = (TextView) convertView.findViewById(R.id.comment_text);
//            postStickView = (ImageView) convertView.findViewById(R.id.post_stick_view);
//            moreImg = (ImageView) convertView.findViewById(R.id.more_img);
//            shareTextView = (TextView) convertView.findViewById(R.id.share_text);
//
//
//            linearImageTwo = (RelativeLayout) convertView.findViewById(R.id.linear_image_two);
//            imgFirst = (ImageView) convertView.findViewById(R.id.img_first);
//            videoPlayImg = (ImageView) convertView.findViewById(R.id.video_play_img);
//            recyclerImage = (RecyclerView) convertView.findViewById(R.id.recycler_image);
//
//            recyclerImage.setHasFixedSize(true);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
//            gridLayoutManager.setAutoMeasureEnabled(true);
//            recyclerImage.setLayoutManager(gridLayoutManager);
//            recyclerImage.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));
//
//            lineView2 = convertView.findViewById(R.id.line_view_2);
//
//            postLabelTwoLayout = convertView.findViewById(R.id.post_label_two_layout);
//
//
//        }
//
//    }
//
//
//    static class ActiveItemViewHolder extends BaseViewHolder {
//        View convertView;
//        TextView activeTopLayout;
//        ImageView moreImg;
//        CircleImageView userImg;
//        TextView schoolTextView;
//        ImageView userLevelView;
//        ImageView alumnusView;
//        TextView nickNameView;
//        TextView timeTextView;
//        TextView userAgeView;
//        TextView locationView;
//        ImageView activeStatusImgView;
//        ImageView topView;
//        ImageView activeImgView;
//        TextView activeContentView;
//        RecyclerView hobbyRecycleView;
//        ImageView moreImgView;
//        TextView cityTextView;
//        TextView activetopView;
//        View lineView2;
//
//        RecyclerView orgainzationRecycleView;
//
//
//        public ActiveItemViewHolder(View itemView) {
//            super(itemView);
//
//            convertView = itemView;
//            activeTopLayout = convertView.findViewById(R.id.active_top_layout);
//            activetopView = convertView.findViewById(R.id.active_top_view);
//            moreImg = convertView.findViewById(R.id.more_img);
//            userImg = convertView.findViewById(R.id.user_img);
//            schoolTextView = (TextView) convertView.findViewById(R.id.school_text_view);
//            nickNameView = convertView.findViewById(R.id.user_nickname_text);
//            userLevelView = convertView.findViewById(R.id.user_level_img);
//            alumnusView = convertView.findViewById(R.id.alumnus_img);
//            timeTextView = convertView.findViewById(R.id.time_text);
//            userAgeView = convertView.findViewById(R.id.user_age_text);
//            cityTextView = (TextView) convertView.findViewById(R.id.city_text);
//            locationView = convertView.findViewById(R.id.location_text);
//            activeStatusImgView = convertView.findViewById(R.id.active_status_img);
//            topView = convertView.findViewById(R.id.post_stick_view);
//            activeImgView = convertView.findViewById(R.id.active_img);
//            activeContentView = convertView.findViewById(R.id.active_name_tv);
//
//            hobbyRecycleView = (RecyclerView) convertView.findViewById(R.id.recycler_hobby);
//            hobbyRecycleView.setHasFixedSize(true);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 4);
//            gridLayoutManager.setAutoMeasureEnabled(true);
//            hobbyRecycleView.setLayoutManager(gridLayoutManager);
//            hobbyRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(12), 4, false));
//
//            orgainzationRecycleView = convertView.findViewById(R.id.recycler_orgainzation);
//            orgainzationRecycleView.setHasFixedSize(true);
//
//            GridLayoutManager orgainzationlayout = new GridLayoutManager(App.getInstance(), 3);
//            gridLayoutManager.setAutoMeasureEnabled(true);
//
//            orgainzationRecycleView.setLayoutManager(orgainzationlayout);
//            orgainzationRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(10), 3, false));
//
//            moreImgView = convertView.findViewById(R.id.orgainzation_more_image);
//
//
//            lineView2 = convertView.findViewById(R.id.line_view_2);
//
//        }
//
//    }
//
//
//    static class PKItemViewHolder extends BaseViewHolder {
//        View convertView;
//        ImageView moreImg;
//        CircleImageView userImg;
//        TextView schoolTextView;
//        ImageView userLevelView;
//        ImageView alumnusView;
//        TextView nickNameView;
//        TextView timeTextView;
//        TextView userAgeView;
//        TextView locationView;
//        ImageView topView;
//
//        TextView pkTitleTextView;
//        TextView pkContentTextView;
//
//        ImageView pkImageView;
//        ImageView videoPlayImg;
//        View imageLayout;
//
//        TextView cityTextView;
//
//        View lineView2;
//
//        public PKItemViewHolder(View itemView) {
//            super(itemView);
//
//            convertView = itemView;
//            moreImg = convertView.findViewById(R.id.more_img);
//            userImg = convertView.findViewById(R.id.user_img);
//            schoolTextView = convertView.findViewById(R.id.school_text_view);
//            nickNameView = convertView.findViewById(R.id.user_nickname_text);
//            userLevelView = convertView.findViewById(R.id.user_level_img);
//            alumnusView = convertView.findViewById(R.id.alumnus_img);
//            timeTextView = convertView.findViewById(R.id.add_time_view);
//            userAgeView = convertView.findViewById(R.id.user_age_text);
//            cityTextView = convertView.findViewById(R.id.city_text);
//            locationView = convertView.findViewById(R.id.location_text);
//            topView = convertView.findViewById(R.id.post_stick_view);
//            pkTitleTextView = convertView.findViewById(R.id.pk_title_text);
//            pkContentTextView = convertView.findViewById(R.id.pk_content_text);
//            pkImageView = convertView.findViewById(R.id.img_bg);
//            videoPlayImg = convertView.findViewById(R.id.video_play_img);
//            imageLayout = convertView.findViewById(R.id.image_layout);
//
//            lineView2 = convertView.findViewById(R.id.line_view_2);
//
//        }
//    }
//
//    static class SayLoveItemViewHolder extends BaseViewHolder {
//        View convertView;
//        ImageView userImg;
//        ImageView img_first;
//        ImageView video_play_img;
//        ImageView alumnus_tv;
//        ImageView userLevelTextView;
//        ImageView deleteImg;
//
//        TextView userNickNameTextView;
//        TextView userAgeTextView;
//        TextView timeTextView;
//        ImageView moreImg;
//        TextView locationTextView;
//        TextView cityTextView;
//        TextView contentTextView;
//        TextView labelFirstTextView;
//        TextView shareNumTextView;
//        TextView praisNumTextView;
//        TextView commentNumTextView;
//        TextView schoolNameTextView;
//        RelativeLayout linear_image_two;
//        RecyclerView recycler_image;
//        View divider;
//        ImageView postStickView;
//
//        View postLabelTwoLayout;
//
//        TextView labelSecondTextView;
//        TextView labelTwoTextTipsView;
//
//        View lineView2;
//
//        public SayLoveItemViewHolder(View itemView) {
//            super(itemView);
//
//            convertView = itemView;
//            userNickNameTextView = (TextView) convertView.findViewById(R.id.user_nickname_text);
//            schoolNameTextView = (TextView) convertView.findViewById(R.id.school_name_text_view);
//            deleteImg = (ImageView) convertView.findViewById(R.id.delete_img);
//            alumnus_tv = (ImageView) convertView.findViewById(R.id.alumnus_text);
//            userLevelTextView = (ImageView) convertView.findViewById(R.id.user_level_text);
//            userAgeTextView = (TextView) convertView.findViewById(R.id.user_age_text);
//            timeTextView = (TextView) convertView.findViewById(R.id.time_text);
//            moreImg = convertView.findViewById(R.id.more_img);
//            locationTextView = (TextView) convertView.findViewById(R.id.location_text);
//            cityTextView = (TextView) convertView.findViewById(R.id.city_text);
//            contentTextView = (TextView) convertView.findViewById(R.id.content_text);
//
//            labelFirstTextView = (TextView) convertView.findViewById(R.id.post_label_one_text);
//            labelTwoTextTipsView = (TextView) convertView.findViewById(R.id.post_label_two_text_tips_view);
//            postLabelTwoLayout = convertView.findViewById(R.id.post_label_two_layout);
//            labelSecondTextView = convertView.findViewById(R.id.post_label_two_text);
//            postLabelTwoLayout.setVisibility(View.GONE);
//
//            shareNumTextView = (TextView) convertView.findViewById(R.id.share_text);
//            praisNumTextView = (TextView) convertView.findViewById(R.id.praise_text);
//            commentNumTextView = (TextView) convertView.findViewById(R.id.comment_text);
//            postStickView = (ImageView) convertView.findViewById(R.id.post_stick_view);
//            postStickView.setVisibility(View.INVISIBLE);
//
//            userImg = (ImageView) convertView.findViewById(R.id.user_img);
//            linear_image_two = (RelativeLayout) convertView.findViewById(R.id.linear_image_two);
//            img_first = (ImageView) convertView.findViewById(R.id.img_first);
//            video_play_img = (ImageView) convertView.findViewById(R.id.video_play_img);
//            recycler_image = (RecyclerView) convertView.findViewById(R.id.recycler_image);
//            divider = (View) convertView.findViewById(R.id.divider_line);
//            recycler_image.setHasFixedSize(true);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
//            gridLayoutManager.setAutoMeasureEnabled(true);
//            recycler_image.setLayoutManager(gridLayoutManager);
//            recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));
//
//            lineView2 = convertView.findViewById(R.id.line_view_2);
//
//        }
//    }
//
//
//    static class ClassMateHelpItemViewHolder extends BaseViewHolder {
//        View convertView;
//        public CircleImageView user_img;
//        public TextView user_nickname_text;
//        public TextView tv_school_name;
//        public ImageView alumnus_text;
//        public ImageView moreImg;
//        public TextView time_text;
//        public TextView distance_text;
//        public TextView content_text;
//        public RelativeLayout linear_image_two;
//        public ImageView img_first;
//        public ImageView video_play_img;
//        public RecyclerView recycler_image;
//        public ImageView delete_img;
//        public TextView praise_text;
//        public TextView comment_text;
//        public TextView user_age_text;
//        public TextView user_city;
//        public View lineView2;
//        public TextView fromTagView;
//        public View fromTagLayoutView;
//
//        public ClassMateHelpItemViewHolder(View itemView) {
//            super(itemView);
//
//            convertView = itemView;
//
//            moreImg = convertView.findViewById(R.id.more_img);
//            user_img = convertView.findViewById(R.id.user_img);
//            user_nickname_text = convertView.findViewById(R.id.user_nickname_text);
//            tv_school_name = convertView.findViewById(R.id.tv_school_name);
//            alumnus_text = convertView.findViewById(R.id.alumnus_text);
//            time_text = convertView.findViewById(R.id.time_text);
//            distance_text = convertView.findViewById(R.id.distance_text);
//            content_text = convertView.findViewById(R.id.content_text);
//            linear_image_two = convertView.findViewById(R.id.linear_image_two);
//            img_first = convertView.findViewById(R.id.img_first);
//            video_play_img = convertView.findViewById(R.id.video_play_img);
//            recycler_image = convertView.findViewById(R.id.recycler_image);
//            delete_img = convertView.findViewById(R.id.delete_img);
//            delete_img.setVisibility(View.GONE);
//            praise_text = convertView.findViewById(R.id.praise_text);
//            comment_text = convertView.findViewById(R.id.comment_text);
//            user_age_text = convertView.findViewById(R.id.user_age_text);
//            user_city = convertView.findViewById(R.id.user_city);
//
//            fromTagView = convertView.findViewById(R.id.from_tag_view);
//            fromTagLayoutView = convertView.findViewById(R.id.from_tag_layout_view);
//
//            recycler_image.setHasFixedSize(true);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
//            gridLayoutManager.setAutoMeasureEnabled(true);
//            recycler_image.setLayoutManager(gridLayoutManager);
//            recycler_image.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(9), 3, false));
//
//            lineView2 = convertView.findViewById(R.id.line_view_2);
//
//        }
//    }
//
//
//    static class HeaderViewHolder extends BaseViewHolder {
//
//        // 学校圈子布局
//        public RelativeLayout titleLayout1;
//        public ImageView titleImageView;
//        public TextView titleTextView;
//        public TextView leftTextView;
//        public ImageView rightImageView;
//
//        // 正常圈子布局
//        public ImageView imageView;
//        public TextView nickTextView;
//        public TextView idTextView;
//        public TextView memberTextView;
//        public TextView postCountView;
//        public TextView descTextView;
//        public Button btnView3;
//        public Button btnView1;
//        public View lineView1;
//        public LinearLayout layoutView2;
//        public TextView groupDescView;
//        public TextView btnView2;
//        public RelativeLayout layoutView3;
//        public RelativeLayout layoutView4;
//        public ImageView addImageView;
//        public TextView textView1;
//        public View lineView2;
//
//
//        public View btnLayout;
//        public TextView leftBtnView;
//        public TextView middleBtnView;
//        public TextView tv_circle_name;
//        public TextView tv_circle_online_num;
//        public TextView rightBtnView;
//        public TextView tv_anonymous_text;
//        public LinearLayout ll_circle_name_and_num;
//        public ImageView lockView;
//        public ImageView iv_chat_icon;
//
//
//        public HeaderViewHolder(View itemView) {
//            super(itemView);
//
//            titleLayout1 = itemView.findViewById(R.id.title_layout_1);
//            titleImageView = itemView.findViewById(R.id.title_image_view);
//            titleTextView = itemView.findViewById(R.id.title_text_view);
//            leftTextView = itemView.findViewById(R.id.left_text_view);
//            rightImageView = itemView.findViewById(R.id.right_image_view);
//
//            //////////////////////////////////
//            imageView = itemView.findViewById(R.id.image_view);
//            nickTextView = itemView.findViewById(R.id.nicke_text_view);
//            idTextView = itemView.findViewById(R.id.id_text_view);
//            memberTextView = itemView.findViewById(R.id.member_text_view);
//            postCountView = itemView.findViewById(R.id.post_count_view);
//            descTextView = itemView.findViewById(R.id.desc_text_view);
//            btnView3 = itemView.findViewById(R.id.btn_view_3);
//            btnView1 = itemView.findViewById(R.id.btn_view_1);
//            lineView1 = itemView.findViewById(R.id.line_view_1);
//            layoutView2 = itemView.findViewById(R.id.layout_view_2);
//            groupDescView = itemView.findViewById(R.id.group_desc_view);
//            btnView2 = itemView.findViewById(R.id.btn_view_2);
//            layoutView3 = itemView.findViewById(R.id.layout_view_3);
//            layoutView4 = itemView.findViewById(R.id.layout_view_4);
//            addImageView = itemView.findViewById(R.id.add_image_view);
//            textView1 = itemView.findViewById(R.id.text_view_1);
//            lineView2 = itemView.findViewById(R.id.line_view_2);
//
//            btnLayout = itemView.findViewById(R.id.btn_layout);
//            leftBtnView = itemView.findViewById(R.id.left_btn_view);
//            middleBtnView = itemView.findViewById(R.id.middle_btn_view);
//            rightBtnView = itemView.findViewById(R.id.right_btn_view);
//
//            ll_circle_name_and_num = itemView.findViewById(R.id.ll_circle_name_and_num);
//            tv_circle_name = itemView.findViewById(R.id.tv_circle_name);
//            tv_circle_online_num = itemView.findViewById(R.id.tv_circle_online_num);
//            lockView = itemView.findViewById(R.id.iv_lock_chatroom);
//            iv_chat_icon = itemView.findViewById(R.id.iv_chat_icon);
//            tv_anonymous_text = itemView.findViewById(R.id.tv_anonymous_text);
//
//        }
//    }
//
//
//    public static class AdHeaderViewViewHolder extends BaseViewHolder {
//
//        public RelativeLayout layoutAdvertisement;
//        public CustomViewPager customViewPager;
//        public LinearLayout llPointGroupView;
//
//        public AdHeaderViewViewHolder(View itemView) {
//            super(itemView);
//
//            layoutAdvertisement = itemView.findViewById(R.id.layout_advertisement);
//            customViewPager = itemView.findViewById(R.id.advertisement_head_view_page);
//            llPointGroupView = itemView.findViewById(R.id.ll_point_group);
//
//        }
//    }
//
//
//    static class EmptyViewViewHolder extends BaseViewHolder {
//
//        public TextView emptyTextView;
//
//        public EmptyViewViewHolder(View itemView) {
//            super(itemView);
//
//            emptyTextView = itemView.findViewById(R.id.empty_textView);
//
//        }
//    }
//
//    public static class ItemType0ViewViewHolder extends BaseViewHolder {
//
//        public ItemType0ViewViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//
//    /**
//     * 加入圈子回调
//     */
//    public static interface JoinOrganizationCallBack {
//        /**
//         * 加入圈子接口
//         *
//         * @param organizationInfo 圈子对象
//         * @param type             1 加入；2 申请
//         */
//        public void joinOrganizationCallBack(OrganizationInfo organizationInfo, int type);
//
//    }
//
//
//    /**
//     * 加入群回调
//     */
//    public static interface JoinGroupCallBack {
//        /**
//         * 加入圈子接口
//         *
//         * @param group 群对象
//         */
//        public void joinGroupCallBack(Group group);
//
//    }
//
//    public static interface ItemWidgetOnClick {
//        void onPraise(long postId, int type, int position);
//
//        void onOrgain(long orgainId, int position);
//
//        void moreImg(int type, int position);
//
//        void onApplyClerk(long storeId);
//    }
//
//    public CharRoomOnClick charRoomOnClick;
//    public OneToOneCharOnClick oneToOneCharOnClick;
//
//    public void setOneToOneCharOnClick(OneToOneCharOnClick oneToOneCharOnClick) {
//        this.oneToOneCharOnClick = oneToOneCharOnClick;
//    }
//
//    public void setCharRoomOnClick(CharRoomOnClick charRoomOnClick) {
//        this.charRoomOnClick = charRoomOnClick;
//    }
//
//    public interface CharRoomOnClick {
//        void onClick(ChatInfo chatInfo);
//
//        void onClockClick(ChatInfo chatInfo);
//    }
//
//    public interface OneToOneCharOnClick {
//        void onClick(long toUserId);
//    }
//
//
//}
