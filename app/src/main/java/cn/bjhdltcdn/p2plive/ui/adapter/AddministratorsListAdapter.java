package cn.bjhdltcdn.p2plive.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.GroupUser;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

public class AddministratorsListAdapter extends BaseAdapter {
    private List<GroupUser> list;
    private long userId;
    private ItemClick itemClick;

    public AddministratorsListAdapter(ItemClick itemClick) {
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        this.itemClick = itemClick;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<GroupUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public GroupUser getItem(int position) {
        return list == null || list.size() == 0 ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @return the mList
     */
    public List<GroupUser> getList() {
        return list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_group_administrators, null);
            viewHolder = new ViewHolder();
            viewHolder.itemHead = (ImageView) convertView.findViewById(R.id.img_icon);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.deleteView = (TextView) convertView.findViewById(R.id.tv_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (getCount() > 0) {
            final GroupUser groupUser = list.get(position);
            final BaseUser baseUser = groupUser.getBaseUser();
            if (baseUser != null) {

                Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), viewHolder.itemHead);
                viewHolder.itemName.setText(baseUser.getNickName());
                viewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClick.onitemDeltet(position, groupUser.getGroupId(), baseUser.getUserId());
                    }
                });
            }
        }

        return convertView;
    }

    class ViewHolder {
        ImageView itemHead;
        TextView itemName, deleteView;
    }

    public interface ItemClick {
        void onitemDeltet(int position, long groupId, long baseUerId);
    }

}
