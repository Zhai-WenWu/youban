package cn.bjhdltcdn.p2plive.ui.dialog;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.model.AddressBook;
import cn.bjhdltcdn.p2plive.model.Group;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiawenquan on 17/12/27.
 */

public class GroupSharedItemDialog extends DialogFragment {

    private View rootView;
    private TextView tipView;
    private TextView cancelView;
    private TextView okView;
    private String shareStr;
    /**
     * 需要分享的群信息
     */
    private Group group;

    public void setGroup(Group group) {
        this.group = group;
    }

    private Object selectObject;

    public void setSelectObject(Object selectObject) {
        this.selectObject = selectObject;
    }

    public void setShareStr(String shareStr) {
        this.shareStr = shareStr;
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.group_share_item_dialog_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        // 触摸内容区域外的需要关闭对话框
        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (v instanceof ViewGroup) {
                    View layoutView = ((ViewGroup) v).getChildAt(0);
                    if (layoutView != null) {
                        float y = event.getY();
                        float x = event.getX();
                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
                        if (!rect.contains((int) x, (int) y)) {
                            dismiss();
                        }
                        rect.setEmpty();
                        rect = null;
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (selectObject == null) {
            dismiss();
            return;
        }


        // 头像
        RequestOptions options = new RequestOptions();

        String nickName = "";
        String userIcon = "";
        CircleImageView circleImageView = rootView.findViewById(R.id.user_image_view);
        if (selectObject instanceof Group) {
            Group group = (Group) selectObject;
            userIcon = group.getGroupImg();
            nickName = group.getGroupName();
            options.placeholder(R.mipmap.error_group_icon);
        } else if (selectObject instanceof AddressBook) {
            AddressBook addressBook = (AddressBook) selectObject;
            userIcon = addressBook.getUserIcon();
            nickName = addressBook.getNickName();
            options.placeholder(R.mipmap.error_user_icon);
        }

        Glide.with(getActivity()).load(userIcon).apply(options).into(circleImageView);

        // 昵称
        TextView nikeTextView = rootView.findViewById(R.id.nike_text_view);
        nikeTextView.setText(nickName);

        // 要分享的群对象昵称
        TextView groupNickView = rootView.findViewById(R.id.group_nick_view);
        tipView= rootView.findViewById(R.id.tip_text_view);
        if(group!=null){
            groupNickView.setText("【" + group.getGroupName() + "】的群名片");
            tipView.setText("发送给：");
        }else{
            groupNickView.setText("   " + shareStr);
            tipView.setText("分享给：");
        }

        cancelView = rootView.findViewById(R.id.cancel_view);
        okView = rootView.findViewById(R.id.ok_view);

        // 取消按钮
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onClick(null);
                }
                dismiss();
            }
        });

        // 确定按钮
        okView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemClickListener != null) {
                    itemClickListener.onClick(selectObject);
                }
                dismiss();
            }
        });

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


    public interface ItemClickListener {

        void onClick(Object object);
    }
}
