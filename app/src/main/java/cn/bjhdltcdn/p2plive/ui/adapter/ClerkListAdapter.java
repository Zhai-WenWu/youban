package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ClertInfo;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.AskDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.PayOffDialog;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zhaiww on 2018/5/19.
 */

public class ClerkListAdapter extends BaseRecyclerAdapter {
    private AppCompatActivity mActivity;
    private List<ClertInfo> mList;
    private boolean deleteVisible;
    private int comeInType;//1:发工资店员列表0：店员列表
    private double totalMoney;

    public List<ClertInfo> getmList() {
        return mList;
    }

    public void setDeleteVisible(boolean deleteVisible) {
        this.deleteVisible = deleteVisible;
    }


    public ClerkListAdapter(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    public void setComeInType(int comeInType) {
        this.comeInType = comeInType;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public void setmList(List<ClertInfo> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mList!=null)
        {
            return mList.size();
        }else{
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        return new ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_clerk_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder viewHolder = (ViewHolder) holder;
        if (deleteVisible) {
            viewHolder.img_delete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.img_delete.setVisibility(View.GONE);
        }
        final BaseUser baseUser = mList.get(position).getBaseUser();
        if (baseUser != null) {
            Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), viewHolder.img_icon);
            viewHolder.tv_name.setText(baseUser.getNickName());
            viewHolder.tv_scool.setText(baseUser.getSchoolName());
            viewHolder.tv_num.setText("接单次数："+mList.get(position).getReceiptNum());
        }

        viewHolder.img_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
            }
        });

        if(comeInType==1){
            viewHolder.tv_clerk.setText("发工资");
        }else{
            viewHolder.tv_clerk.setText("解雇该店员");
        }

        viewHolder.tv_clerk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comeInType==1){
                  //发工资
                    PayOffDialog payOffDialog=new PayOffDialog();
                    payOffDialog.setItemClick(new PayOffDialog.ItemClick() {
                        @Override
                        public void itemClick(String moneyStr) {
                            Double moneyDouble=Double.parseDouble(moneyStr);
                            if(moneyDouble>totalMoney)
                            {
                               Utils.showToastShortTime("您的账户余额不足");
                            }else{
                                onFireClick.payOffCliclk(baseUser.getUserId(),moneyStr);
                            }

                        }
                    });
                    payOffDialog.show(mActivity.getSupportFragmentManager());
                }else{
                    //解雇店员
                    new AskDialog.Builder()
                            .content("TA将不再为你的小店服务，是否解雇该店员？")
                            .leftBtnText("取消")
                            .rightBtnText("解雇")
                            .rightClickListener(new AskDialog.OnRightClickListener() {
                                @Override
                                public void onClick() {
                                    onFireClick.onFireClick(position);
                                }
                            })
                            .build()
                            .show(mActivity.getSupportFragmentManager());
                }

            }
        });
    }

    public class ViewHolder extends BaseViewHolder {
        public LinearLayout img_delete;
        public TextView tv_clerk;
        public TextView tv_name;
        public TextView tv_num;
        public TextView tv_scool;
        public CircleImageView img_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            img_delete = itemView.findViewById(R.id.img_delete);
            tv_clerk = itemView.findViewById(R.id.tv_clerk);
            img_icon = itemView.findViewById(R.id.img_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_num= itemView.findViewById(R.id.tv_num);
            tv_scool = itemView.findViewById(R.id.tv_scool);
        }
    }

    OnFireClick onFireClick;

    public void setOnFireClick(OnFireClick onFireClick) {
        this.onFireClick = onFireClick;
    }

    public interface OnFireClick {
        void onFireClick(int position);
        void payOffCliclk(long toUserId,String moneyStr);

    }
}
