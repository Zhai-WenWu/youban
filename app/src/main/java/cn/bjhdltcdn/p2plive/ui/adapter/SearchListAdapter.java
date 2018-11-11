package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.ActivityLocationInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;

public class SearchListAdapter extends BaseAdapter {
    private List<Object> list;
    private List<OrganizationInfo> organizationInfoList=new ArrayList<>(1);
    private List<Group> groupList=new ArrayList<>(1);
    private List<BaseUser> baseUserList=new ArrayList<>(1);
    private List<ActivityInfo> ActivityInfoList=new ArrayList<>(1);
    private AppCompatActivity mActivity;
    private RequestOptions options,userOptions;
    private OnClick onClick;
    private int organSize,groupSize,baseuserSize,activeSize;
    private String  defaultImg;//默认图片
    private final int showNum=2;
    public SearchListAdapter(AppCompatActivity mActivity) {
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

    public void setList(List<OrganizationInfo> organizationInfoList,List<BaseUser> baseUserList,List<Group> groupList,List<ActivityInfo> ActivityInfoList,String defaultImg) {
        list=new ArrayList<>();
        this.defaultImg=defaultImg;
        organSize=organizationInfoList.size();
        groupSize=groupList.size();
        baseuserSize=baseUserList.size();
        activeSize=ActivityInfoList.size();

        this.organizationInfoList.clear();
        if(organSize>showNum){
            for(int i=0;i<showNum;i++){
                this.organizationInfoList.add(organizationInfoList.get(i));
            }
        }else{
            this.organizationInfoList=organizationInfoList;
        }

        this.groupList.clear();
        if(groupSize>showNum){
            for(int i=0;i<showNum;i++){
                this.groupList.add(groupList.get(i));
            }
        }else{
            this.groupList=groupList;
        }

        this.baseUserList.clear();
        if(baseuserSize>showNum){
            for(int i=0;i<showNum;i++){
                this.baseUserList.add(baseUserList.get(i));
            }
        }else{
            this.baseUserList=baseUserList;
        }

        this.ActivityInfoList.clear();
        if(activeSize>showNum){
            for(int i=0;i<showNum;i++){
                this.ActivityInfoList.add(ActivityInfoList.get(i));
            }
        }else{
            this.ActivityInfoList=ActivityInfoList;
        }

        list.addAll(this.organizationInfoList);
        list.addAll(list.size(),this.ActivityInfoList);
        list.addAll(list.size(),this.baseUserList);
        list.addAll(list.size(),this.groupList);

    }


    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @Override
    public Object getItem(int position) {
        return list == null || list.size() == 0 ? null : list.get(position);
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
            viewHolder.joinView=convertView.findViewById(R.id.orgain_join_text);
            viewHolder.moreView=convertView.findViewById(R.id.more_text);
            viewHolder.bottomLineView=convertView.findViewById(R.id.bottom_lineView);
            viewHolder.lineView=convertView.findViewById(R.id.lineView);
            viewHolder.centerLineView=convertView.findViewById(R.id.center_lineView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
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
            viewHolder.moreView.setVisibility(View.GONE);
            viewHolder.bottomLineView.setVisibility(View.GONE);
            viewHolder.lineView.setVisibility(View.VISIBLE);
            viewHolder.centerLineView.setVisibility(View.GONE);
            viewHolder.titleTextView.setVisibility(View.VISIBLE);
        }else{
            if(position==organizationInfoList.size()-1&&organSize>showNum){
                viewHolder.moreView.setVisibility(View.VISIBLE);
                viewHolder.moreView.setText("更多圈子");
                viewHolder.bottomLineView.setVisibility(View.VISIBLE);
                viewHolder.lineView.setVisibility(View.GONE);
                viewHolder.titleTextView.setVisibility(View.GONE);
                viewHolder.centerLineView.setVisibility(View.VISIBLE);
            }else if(position==organizationInfoList.size()){
                //下一组开始
                viewHolder.moreView.setVisibility(View.GONE);
                viewHolder.bottomLineView.setVisibility(View.GONE);
                viewHolder.lineView.setVisibility(View.VISIBLE);
                viewHolder.titleTextView.setVisibility(View.VISIBLE);
                viewHolder.centerLineView.setVisibility(View.GONE);
            }else if(position==organizationInfoList.size()+ActivityInfoList.size()-1&&activeSize>showNum){
                viewHolder.moreView.setVisibility(View.VISIBLE);
                viewHolder.moreView.setText("更多活动");
                viewHolder.bottomLineView.setVisibility(View.VISIBLE);
                viewHolder.lineView.setVisibility(View.GONE);
                viewHolder.titleTextView.setVisibility(View.GONE);
                viewHolder.centerLineView.setVisibility(View.VISIBLE);
            }else if(position==organizationInfoList.size()+ActivityInfoList.size()){
                //下一组开始
                viewHolder.moreView.setVisibility(View.GONE);
                viewHolder.bottomLineView.setVisibility(View.GONE);
                viewHolder.lineView.setVisibility(View.VISIBLE);
                viewHolder.titleTextView.setVisibility(View.VISIBLE);
                viewHolder.centerLineView.setVisibility(View.GONE);
            }else if(position==organizationInfoList.size()+baseUserList.size()+ActivityInfoList.size()-1&&baseuserSize>showNum){
                viewHolder.moreView.setVisibility(View.VISIBLE);
                viewHolder.moreView.setText("更多用户");
                viewHolder.bottomLineView.setVisibility(View.VISIBLE);
                viewHolder.lineView.setVisibility(View.GONE);
                viewHolder.titleTextView.setVisibility(View.GONE);
                viewHolder.centerLineView.setVisibility(View.VISIBLE);
            }else if(position==organizationInfoList.size()+baseUserList.size()+ActivityInfoList.size()){
                //下一组开始
                viewHolder.moreView.setVisibility(View.GONE);
                viewHolder.bottomLineView.setVisibility(View.GONE);
                viewHolder.lineView.setVisibility(View.VISIBLE);
                viewHolder.titleTextView.setVisibility(View.VISIBLE);
                viewHolder.centerLineView.setVisibility(View.GONE);
            }else if(position==organizationInfoList.size()+baseUserList.size()+groupList.size()+ActivityInfoList.size()-1&&groupSize>showNum){
                viewHolder.moreView.setVisibility(View.VISIBLE);
                viewHolder.moreView.setText("更多群组");
                viewHolder.bottomLineView.setVisibility(View.VISIBLE);
                viewHolder.lineView.setVisibility(View.GONE);
                viewHolder.titleTextView.setVisibility(View.GONE);
                viewHolder.centerLineView.setVisibility(View.VISIBLE);
            }else if(position==organizationInfoList.size()+baseUserList.size()+groupList.size()+ActivityInfoList.size()){
                //下一组开始
                viewHolder.moreView.setVisibility(View.GONE);
                viewHolder.bottomLineView.setVisibility(View.GONE);
                viewHolder.lineView.setVisibility(View.VISIBLE);
                viewHolder.titleTextView.setVisibility(View.VISIBLE);
                viewHolder.centerLineView.setVisibility(View.GONE);
            }else{
                viewHolder.moreView.setVisibility(View.GONE);
                viewHolder.bottomLineView.setVisibility(View.GONE);
                viewHolder.lineView.setVisibility(View.GONE);
                viewHolder.titleTextView.setVisibility(View.GONE);
                viewHolder.centerLineView.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看更多
                String text=viewHolder.moreView.getText().toString();
                if(text.equals("更多圈子")){
                    onClick.onMoreClick(1);
                }else if(text.equals("更多用户")){
                    onClick.onMoreClick(3);
                }else if(text.equals("更多群组")){
                    onClick.onMoreClick(2);
                }else if(text.equals("更多活动")){
                    onClick.onMoreClick(4);
                }

            }
        });



        return convertView;
    }

    class ViewHolder {
        ImageView userImg,orgainImg;
        TextView titleTextView,userNameView,contentView,moreView,joinView;
        View lineView,centerLineView,bottomLineView;
    }

    public interface OnClick{
        void onMoreClick(int type);
        void joinGroupClick(Group group,int position);
    }




}
