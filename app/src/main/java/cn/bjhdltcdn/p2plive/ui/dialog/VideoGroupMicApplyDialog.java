package cn.bjhdltcdn.p2plive.ui.dialog;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.VideoGroupMicApplyEvent;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;
import cn.bjhdltcdn.p2plive.ui.adapter.VideoGroupMicApplyListAdapter;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * Created by zhudi on 2016/1/12
 */
public class VideoGroupMicApplyDialog extends DialogFragment {

    private View rootView;
    private int type;//1申请2请求列表3踢人下麦5主动下麦
    private List<RoomBaseUser> list;//请求列表
    private RoomBaseUser toBaseUser;//被踢下麦的人

    public void setToBaseUser(RoomBaseUser toBaseUser) {
        this.toBaseUser = toBaseUser;
    }

    private VideoGroupMicApplyListAdapter adapter;

    public void setList(List<RoomBaseUser> list) {
        this.list = list;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (type == 1 || type == 3 || type == 5) {
            rootView = inflater.inflate(R.layout.dialog_video_group_mic_apply, null);
        } else if (type == 2) {
            rootView = inflater.inflate(R.layout.dialog_video_group_mic_apply_list, null);
        }
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
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (type == 1 || type == 3 || type == 5) {
            TextView tv_apply_video = (TextView) rootView.findViewById(R.id.tv_apply_video);
            TextView tv_apply_voice = (TextView) rootView.findViewById(R.id.tv_apply_voice);
            TextView tv_cancle = (TextView) rootView.findViewById(R.id.tv_cancle);
            if (type == 1) {
                tv_apply_voice.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.view_line).setVisibility(View.VISIBLE);
            }
            if (type == 3) {
                tv_apply_video.setText("踢Ta下麦");
            } else if (type == 5) {
                tv_apply_video.setText("下麦");
            }
            tv_apply_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 3) {
                        EventBus.getDefault().post(new VideoGroupMicApplyEvent(type, toBaseUser));
                    } else {
                        GreenDaoUtils.getInstance().getBaseUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), new GreenDaoUtils.ExecuteCallBack() {
                            @Override
                            public void callBack(Object object) {
                                if (object instanceof BaseUser) {
                                    EventBus.getDefault().post(new VideoGroupMicApplyEvent(type, (BaseUser) object, 1));
                                }
                            }
                        });
                    }
                    onDismiss();
                }
            });
            tv_apply_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GreenDaoUtils.getInstance().getBaseUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), new GreenDaoUtils.ExecuteCallBack() {
                        @Override
                        public void callBack(Object object) {
                            if (object instanceof BaseUser) {
                                EventBus.getDefault().post(new VideoGroupMicApplyEvent(type, (BaseUser) object, 2));
                            }
                        }
                    });
                    onDismiss();
                }
            });
            tv_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDismiss();
                }
            });

        } else if (type == 2) {
            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            if (list != null) {
                adapter = new VideoGroupMicApplyListAdapter(getContext(), list);
                listView.setAdapter(adapter);
            }
        }
    }

    private void onDismiss() {
        if (rootView != null) {
            rootView = null;
        }

        dismiss();
    }

    /**
     * 刷新申请列表显示
     *
     * @param baseUser
     */
    public void setDate(RoomBaseUser baseUser) {
        adapter.setDate(baseUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
}
