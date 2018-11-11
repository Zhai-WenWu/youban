package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.orhanobut.logger.Logger;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetFriendListResponse;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.AddressBook;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.PlayInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.AnonymousFriendsListActivity;
import cn.bjhdltcdn.p2plive.ui.activity.GroupListActivity;
import cn.bjhdltcdn.p2plive.ui.activity.SelectAddressBookActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.AddressBookListAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.GroupSharedItemDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * Created by Hu_PC on 2017/11/8.
 * 通讯录
 */

public class MessageAddressBookFragment extends BaseFragment implements BaseView {

    private static final String INDEX_STRING_TOP = "↑";

    private View rootView;
    private View emptyView;
    private TextView empty_tv;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SuspensionDecoration mDecoration;

    private AddressBookListAdapter adapter;

    /**
     * 已选择的
     */
    private List<AddressBook> selctlist;

    public List<AddressBook> getSelctlist() {
        return selctlist;
    }

    private List<AddressBook> list;

    private int pageSize = 100;
    private int pageNumber = 1;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;

    private UserPresenter presenter;
    /**
     * 30005 群分享,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白
     */
    private Object object;

    public Object getObject() {
        return object;
    }

    public UserPresenter getPresenter() {
        if (presenter == null) {
            presenter = new UserPresenter(this);
        }
        return presenter;
    }

    /**
     * type 默认0 分享 1添加好友
     */
    private int type;

    public int getType() {
        return type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_message_address_book, null);
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
        object = getActivity().getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);
        type = getActivity().getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        initView();
        RongIMutils.setIsCanOnUserPortraitClick(true);

    }

    private void initView() {

        if (list == null) {
            list = new ArrayList<>(1);
        }

        //使用indexBar
        mTvSideBarHint = (TextView) rootView.findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) rootView.findViewById(R.id.indexBar);//IndexBar

        recyclerView = rootView.findViewById(R.id.recycle_view);
        emptyView = rootView.findViewById(R.id.empty_view);
        empty_tv = rootView.findViewById(R.id.empty_textView);
        layoutManager = new LinearLayoutManager(App.getInstance());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AddressBookListAdapter(type);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {

                //30005 群分享,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白
                if (object == null) {
                    if (position == 0) {
                        startActivity(new Intent(getActivity(), GroupListActivity.class));
                    } else if (position == 1) {
                        startActivity(new Intent(getActivity(), AnonymousFriendsListActivity.class));
                    } else {
                        AddressBook addressBook = adapter.getList().get(position);
                        RongIMutils.startToConversation(getActivity(), addressBook.getUserId() + "", addressBook.getNickName());
                    }

                    return;
                }

                if (object instanceof Group) {
                    showDialog(object, 30005, object, position);
                } else if (object instanceof PostInfo) {
                    showDialog("分享给Ta一个帖子", 11, object, position);
                } else if (object instanceof OrganizationInfo) {
                    showDialog("分享给Ta一个圈子", 12, object, position);
                } else if (object instanceof ActivityInfo) {
                    showDialog("分享给Ta一个活动", 13, object, position);
                } else if (object instanceof RoomInfo) {
                    showDialog("分享给Ta一个聊天频道", 14, object, position);
                } else if (object instanceof PlayInfo) {
                    showDialog("分享给Ta一个PK挑战", 15, object, position);
                } else if (object instanceof SayLoveInfo) {
                    showDialog("分享给Ta一个校园表白", 18, object, position);
                }

            }
        });

        adapter.setUserIconClickListener(new AddressBookListAdapter.UserIconClickListener() {
            @Override
            public void onClick(AddressBook addressBook) {

                if (addressBook.getUserId() < 1) {
                    return;
                }

                BaseUser baseUser = new BaseUser();
                baseUser.setUserId(addressBook.getUserId());
                baseUser.setUserName(addressBook.getUserName());
                baseUser.setNickName(addressBook.getNickName());
                baseUser.setSex(addressBook.getSex());
                baseUser.setUserIcon(addressBook.getUserIcon());
                baseUser.setUserBigIcon(addressBook.getUserBigIcon());
                baseUser.setLocation(addressBook.getLocation());

                startActivity(new Intent(getActivity(), UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));

            }
        });

        recyclerView.addItemDecoration(mDecoration = new SuspensionDecoration(App.getInstance(), list));
        //如果add两个，那么按照先后顺序，依次渲染。
        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1), true));

        //indexbar初始化
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(layoutManager);//设置RecyclerView的LayoutManager

        setData(null);

    }


    private void showDialog(Object message, final int messageType, final Object sharedObject, int position) {

        if (position == 0) {

            //30005 群分享,11分享帖子,12分享圈子,13分享活动,14分享房间,15分享PK挑战,18分享表白
            if (sharedObject instanceof Group) {

                Group group = (Group) sharedObject;
                startActivity(new Intent(getActivity(), GroupListActivity.class).putExtra(Constants.KEY.KEY_OBJECT, group));

            } else if (sharedObject instanceof PostInfo) {

                PostInfo postInfo = (PostInfo) sharedObject;
                startActivity(new Intent(getActivity(), GroupListActivity.class).putExtra(Constants.KEY.KEY_OBJECT, postInfo));

            } else if (sharedObject instanceof OrganizationInfo) {

                OrganizationInfo organizationInfo = (OrganizationInfo) sharedObject;
                startActivity(new Intent(getActivity(), GroupListActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));

            } else if (sharedObject instanceof ActivityInfo) {

                ActivityInfo activityInfo = (ActivityInfo) sharedObject;
                startActivity(new Intent(getActivity(), GroupListActivity.class).putExtra(Constants.KEY.KEY_OBJECT, activityInfo));

            } else if (sharedObject instanceof RoomInfo) {

                RoomInfo roomInfo = (RoomInfo) sharedObject;
                startActivity(new Intent(getActivity(), GroupListActivity.class).putExtra(Constants.KEY.KEY_OBJECT, roomInfo));

            } else if (sharedObject instanceof PlayInfo) {

                PlayInfo playInfo = (PlayInfo) sharedObject;
                startActivity(new Intent(getActivity(), GroupListActivity.class).putExtra(Constants.KEY.KEY_OBJECT, playInfo));

            } else if (sharedObject instanceof SayLoveInfo) {

                SayLoveInfo sayLoveInfo = (SayLoveInfo) sharedObject;
                startActivity(new Intent(getActivity(), GroupListActivity.class).putExtra(Constants.KEY.KEY_OBJECT, sayLoveInfo));

            }

            if (sharedObject != null) {
                getActivity().finish();
            }

        } else {
            if (selctlist == null) {
                selctlist = new ArrayList<>(1);
            }
            AddressBook addressBook = adapter.getList().get(position);
            if (type == 1) {
                if (addressBook.getIsSelect() == 0) {
                    addressBook.setIsSelect(1);
                    if (!selctlist.contains(addressBook)) {
                        selctlist.add(addressBook);
                    }
                } else if (addressBook.getIsSelect() == 1) {
                    addressBook.setIsSelect(0);
                    if (selctlist.contains(addressBook)) {
                        selctlist.remove(addressBook);
                    }
                }
                adapter.notifyItemChanged(position);
            } else {

                if (sharedObject != null) {
                    GroupSharedItemDialog dialog = new GroupSharedItemDialog();
                    if (message instanceof String) {
                        dialog.setShareStr(((String) message));
                    } else if (message instanceof Group) {
                        dialog.setGroup(((Group) message));
                    }


                    dialog.setSelectObject(addressBook);

                    dialog.setItemClickListener(new GroupSharedItemDialog.ItemClickListener() {
                        @Override
                        public void onClick(Object selectObject) {

                            if (selectObject == null) {
                                return;
                            }

                            String pushContent = null;
                            switch (messageType) {
                                case 11:
                                    pushContent = "分享一个帖子";
                                    break;
                                case 12:
                                    pushContent = "分享一个圈子";
                                    break;

                                case 13:
                                    pushContent = "分享一个活动";
                                    break;

                                case 14:
                                    pushContent = "分享一个聊天频道";

                                    break;
                                case 15:
                                    pushContent = "分享一个PK挑战";
                                    break;

                                case 18:
                                    pushContent = "分享一个表白";
                                    break;

                                default:
                                    pushContent = "";
                                    break;

                            }

                            RongIMutils.sendSharedMessage(pushContent, messageType, selectObject, sharedObject);
                            getActivity().finish();
                        }
                    });

                    dialog.show(getChildFragmentManager());

                    return;


                }

            }
        }

    }

    @Override
    public void onVisible(boolean isInit) {

        Logger.d("isInit === " + isInit);
        RongIMutils.setIsCanOnUserPortraitClick(true);
        getPresenter().getFriendList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            if (e instanceof SocketTimeoutException) {
                Utils.showToastShortTime("网络连接超时");
                return;
            }
            Utils.showToastShortTime(e.getMessage());
            return;
        }

        if (InterfaceUrl.URL_GETFRIENDLIST.equals(apiName)) {
            if (object instanceof GetFriendListResponse) {
                GetFriendListResponse response = (GetFriendListResponse) object;

                if (response.getCode() == 200) {
                    setData(response.getList());
                    if (response.getList().size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(response.getBlankHint())) {
                            empty_tv.setText(response.getBlankHint());
                        }
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                } else {
                    Utils.showToastShortTime(response.getMsg());
                }

            }
        }
    }

    private void setData(List<AddressBook> mList) {

        if (list == null) {
            list = new ArrayList<>(1);
        } else {
            list.clear();
        }


        AddressBook addressBook = new AddressBook();
        addressBook.setNickName("群组列表");
        addressBook.setTop(true);
        addressBook.setBaseIndexTag(INDEX_STRING_TOP);
        list.add(addressBook);

        if (object == null) {
            AddressBook addressBookNM = new AddressBook();
            addressBookNM.setNickName("匿名好友");
            addressBookNM.setTop(true);
            addressBookNM.setBaseIndexTag(INDEX_STRING_TOP);
            list.add(addressBookNM);
        }


        if (mList != null && mList.size() > 0) {
            list.addAll(mList);
        }

        adapter.setList(list);

        mIndexBar.setmSourceDatas(list)//设置数据
                .invalidate();

        mDecoration.setmDatas(list);
    }


    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    @Override
    public void onDetach() {
        super.onDetach();


        if (layoutManager != null) {
            layoutManager.removeAllViews();
            layoutManager = null;
        }


        if (list != null) {
            list.clear();
        }
        list = null;
    }
}
