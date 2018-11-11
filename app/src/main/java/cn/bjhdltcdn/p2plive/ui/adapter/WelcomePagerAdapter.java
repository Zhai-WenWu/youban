package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;

public class WelcomePagerAdapter extends PagerAdapter {

    private int count;
    private RequestOptions options;

    public WelcomePagerAdapter(int count) {
        this.count = count;
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (getCount() > 0) {
            final View convertView = View.inflate(App.getInstance(), R.layout.welcome_image_page_layout, null);
            ImageView imageView = convertView.findViewById(R.id.welcome_view);
            int ids = R.drawable.guide_one;
            if (position == 1) {
                ids = R.drawable.guide_two;
            } else if (position == 2) {
                ids = R.drawable.guide_three;
            } else if (position == 3) {
                ids = R.drawable.guide_four;
            }
            Glide.with(App.getInstance()).asBitmap().load(ids).apply(options).into(imageView);
            container.addView(convertView);
            return convertView;
        }

        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}