package cn.bjhdltcdn.p2plive.ui.dialog;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;
import cn.bjhdltcdn.p2plive.model.VoteRecord;
import cn.bjhdltcdn.p2plive.ui.adapter.SelectVotePKUserGridAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.SelectVoteUserGridAdapter;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;


public class SelectVoteUserDialog extends DialogFragment {

    private View rootView;
    private GridView gridView;
    private TextView titleView;
    private RelativeLayout voteStatusBarLayout;
    private SelectVoteUserGridAdapter selectVoteUserGridAdapter;
    private SelectVotePKUserGridAdapter selectVotePKUserGridAdapter;
    private List<RoomBaseUser> userList;
    private List<VoteRecord> voteRecordList;
    public onClickListener onClickListener;
    public int type = 1;//1 常驻投票 2：pk投票
    private RoomBaseUser toBaseUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
//        ShareSDK.initSDK(App.getInstance());
    }


    public void setOnClickListener(SelectVoteUserDialog.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUserList(List<RoomBaseUser> userList) {
        this.userList = userList;
    }

    public void setVoteRecordList(List<VoteRecord> voteRecordList) {
        this.voteRecordList = voteRecordList;
    }

    public long getCurrentUserId() {
        return SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.select_vote_user_dialog, null);
        rootView.setAnimation(AnimationUtils.loadAnimation(App.getInstance(), R.anim.popup_enter));
        // 触摸内容区域外的需要关闭对话框
        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v instanceof ViewGroup) {
                    View layoutView = ((ViewGroup) v).getChildAt(0);
                    if (layoutView != null) {
                        float y = event.getY();
                        float x = event.getX();
                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
                        if (!rect.contains((int) x, (int) y)) {
                            onDismiss();
                        }
                        rect.setEmpty();
                        rect = null;
                    }
                }
                return false;
            }
        });

        initView();

        return rootView;
    }


    private void initView() {
        gridView = (GridView) rootView.findViewById(R.id.vote_user_gridView);
        if (type == 1) {
            selectVoteUserGridAdapter = new SelectVoteUserGridAdapter();
            selectVoteUserGridAdapter.setList(userList);
            gridView.setAdapter(selectVoteUserGridAdapter);
        } else if (type == 2) {
            selectVotePKUserGridAdapter = new SelectVotePKUserGridAdapter();
            selectVotePKUserGridAdapter.setList(voteRecordList);
            gridView.setAdapter(selectVotePKUserGridAdapter);
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectVoteUserGridAdapter != null && selectVoteUserGridAdapter.getItem(position) instanceof BaseUser) {
                    RoomBaseUser baseUser = (RoomBaseUser) selectVoteUserGridAdapter.getItem(position);
                    if (baseUser.getUserId() == getCurrentUserId()) {
                        Utils.showToastShortTime("不能给自己投票哦");
                    } else {
                        baseUser.setWheatId(position + 1);
                        onClickListener.onClick(baseUser);
                        onDismiss();
                    }
                } else if (selectVotePKUserGridAdapter != null && selectVotePKUserGridAdapter.getItem(position) instanceof VoteRecord) {
                    VoteRecord voteRecord = (VoteRecord) selectVotePKUserGridAdapter.getItem(position);
                    if (voteRecord.getUserId() == getCurrentUserId()) {
                        Utils.showToastShortTime("不能给自己投票哦");
                    } else {
                        if (toBaseUser == null) {
                            toBaseUser = new RoomBaseUser();
                        }
                        toBaseUser.setUserId(voteRecord.getUserId());
                        toBaseUser.setNickName(voteRecord.getNickName());
                        toBaseUser.setWheatId(position + 1);
                        onClickListener.onClick(toBaseUser);
                        onDismiss();
                    }
                }
            }
        });
        titleView = (TextView) rootView.findViewById(R.id.vote_title);
        voteStatusBarLayout = (RelativeLayout) rootView.findViewById(R.id.vote_status_bar_layout);
        if (type == 1) {
            titleView.setText("点击头像，为你喜欢的人投一票！");
            voteStatusBarLayout.setVisibility(View.GONE);
        } else if (type == 2) {
            titleView.setText("PK进行中，点击头像投票");
            voteStatusBarLayout.setVisibility(View.VISIBLE);
        }
    }

    private void onDismiss() {
        Animation animation = AnimationUtils.loadAnimation(App.getInstance(), R.anim.popup_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (rootView != null) {
                    rootView = null;
                }
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (rootView != null) {
            rootView.startAnimation(animation);
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            super.dismiss();
        }
    }

    public void show(FragmentManager manager) {
        show(manager, "dialog");
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
            // 解决java.lang.IllegalStateException: Can not perform this action问题
            final FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }


    public interface onClickListener {
        void onClick(BaseUser baseUser);
    }


}
