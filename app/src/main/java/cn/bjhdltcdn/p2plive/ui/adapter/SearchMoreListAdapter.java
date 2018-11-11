package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.ActivityLocationInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

public class SearchMoreListAdapter extends BaseAdapter {
    private List<Object> list;
    private AppCompatActivity mActivity;
    private RequestOptions options,userOptions;
    private OnClick onClick;
    private String  defaultImg;//默认图片
    public SearchMoreListAdapter(AppCompatActivity mActivity) {
       this.mActivity=mActivity;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
        userOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_user_icon)
                .error(R.mipmap.error_user_icon);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<Object> list,String defaultImg) {
        this.list = list;
        this.defaultImg=defaultImg;
    }

    public void addList(List<Object> list){
        this.list.addAll(list);
    }


    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @Override
    public Object getItem(int position) {
        return list == null || list.size() == 0 ? null : list.get(position-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @return the mList
     */
    public List<Object> getList() {
        return list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.search_list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView=convertView.findViewById(R.id.title_text);
            viewHolder.userImg=convertView.findViewById(R.id.user_img);
            viewHolder.orgainImg=convertView.findViewById(R.id.organ_img);
            viewHolder.userNameView=convertView.findViewById(R.id.name_text);
            viewHolder.contentView=convertView.findViewById(R.id.content_text);
            viewHolder.moreView=convertView.findViewById(R.id.more_text);
            viewHolder.lineView=convertView.findViewById(R.id.lineView);
            viewHolder.centerLineView=convertView.findViewById(R.id.center_lineView);
            viewHolder.joinView=convertView.findViewById(R.id.orgain_join_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.moreView.setVisibility(View.GONE);
        viewHolder.lineView.setVisibility(View.GONE);
        viewHolder.titleTextView.setVisibility(View.GONE);
        Object o=list.get(position);
        if(o instanceof OrganizationInfo){
            viewHolder.titleTextView.setText("圈子");
            OrganizationInfo organizationInfo= (OrganizationInfo) o;
            viewHolder.userImg.setVisibility(View.INVISIBLE);
            viewHolder.orgainImg.setVisibility(View.VISIBLE);
            Glide.with(App.getInstance()).load(organizationInfo.getOrganImg()).apply(options).into(viewHolder.orgainImg);
            viewHolder.userNameView.setText(organizationInfo.getOrganName());
            viewHolder.contentView.setText("成员  "+organizationInfo.getMemberCount()+"      帖子  "+organizationInfo.getPostCount());
            viewHolder.joinView.setVisibility(View.GONE);
        }else if(o instanceof ActivityInfo){
            viewHolder.titleTextView.setText("活动");
            ActivityInfo activityInfo= (ActivityInfo) o;
            viewHolder.userImg.setVisibility(View.INVISIBLE);
            viewHolder.orgainImg.setVisibility(View.VISIBLE);
            if(activityInfo.getImageList()!=null&&activityInfo.getImageList().size()>0){
                Glide.with(App.getInstance()).load(activityInfo.getImageList().get(0).getThumbnailUrl()).apply(options).into(viewHolder.orgainImg);
            }else{
                Glide.with(App.getInstance()).load(defaultImg).apply(options).into(viewHolder.orgainImg);
            }
            viewHolder.userNameView.setText(activityInfo.getTheme());
            ActivityLocationInfo activityLocationInfo=activityInfo.getLocationInfo();
            String city = "",addr="";
            if(activityLocationInfo!=null){
                city= activityLocationInfo.getCity();
                addr=activityLocationInfo.getAddr();
            }
            viewHolder.contentView.setText(activityInfo.getActivityTime()+"    "+city+"      "+addr);
            viewHolder.joinView.setVisibility(View.GONE);
        }else if(o instanceof BaseUser){
            viewHolder.titleTextView.setText("用户");
            BaseUser baseUser= (BaseUser) o;
            viewHolder.userImg.setVisibility(View.VISIBLE);
            viewHolder.orgainImg.setVisibility(View.INVISIBLE);
            Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(viewHolder.userImg);
            viewHolder.userNameView.setText(baseUser.getNickName());
            viewHolder.contentView.setText("ID:"+baseUser.getUserId());
            viewHolder.joinView.setVisibility(View.GONE);
        }else if(o instanceof Group){
            viewHolder.titleTextView.setText("群组");
            final Group group= (Group) o;
            viewHolder.userImg.setVisibility(View.VISIBLE);
            viewHolder.orgainImg.setVisibility(View.INVISIBLE);
            Glide.with(App.getInstance()).load(group.getGroupImg()).apply(userOptions).into(viewHolder.userImg);
            viewHolder.userNameView.setText(group.getGroupName());
            viewHolder.contentView.setText(group.getNumber()+"人");
            viewHolder.joinView.setVisibility(View.VISIBLE);
            viewHolder.joinView.setEnabled(true);
            if(group.getIsExistGroup()==0){
                viewHolder.joinView.setText("  加入群  ");
                viewHolder.joinView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                viewHolder.joinView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
            }else if(group.getIsExistGroup()==2){
                viewHolder.joinView.setText("  申请中  ");
                viewHolder.joinView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                viewHolder.joinView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
                viewHolder.joinView.setEnabled(false);
            }else{
                viewHolder.joinView.setText("发起群聊");
                viewHolder.joinView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                viewHolder.joinView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_stroke_333333));
            }
            viewHolder.joinView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.joinGroupClick(group,position);
                }
            });

        }
        if(position==0){
            viewHolder.centerLineView.setVisibility(View.GONE);
        }else{
            viewHolder.centerLineView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView userImg,orgainImg;
        TextView titleTextView,userNameView,contentView,moreView,joinView;
        View lineView,centerLineView;
    }

    public interface OnClick{
        void joinGroupClick(Group group,int position);
    }


}
