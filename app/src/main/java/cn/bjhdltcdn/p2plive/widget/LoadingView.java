package cn.bjhdltcdn.p2plive.widget;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.IBottomView;

import cn.bjhdltcdn.p2plive.R;

/**
 * Created by Hu_PC on 2017/4/11.
 */

public class LoadingView extends FrameLayout implements IBottomView {

    private ProgressBar loadingView;
    private TextView loadingTextView;
    private boolean onLoadFinish;
    public LoadingView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View rootView = View.inflate(getContext(), R.layout.loading_layout,null);
        loadingView= (ProgressBar) rootView.findViewById(R.id.progress_loading);
        loadingTextView = (TextView) rootView.findViewById(R.id.tv_loading);
        addView(rootView);
    }

    @Override
    public View getView() {
        return this;
    }

    public void setOnLoadFinish(boolean onLoadFinish) {
        this.onLoadFinish = onLoadFinish;
    }

    public boolean isOnLoadFinish() {
        return onLoadFinish;
    }

    @Override
    public void onPullingUp(float fraction, float maxBottomHeight, float bottomHeight) {
        //正在上拉的过程
        if(!onLoadFinish){
            loadingView.setVisibility(GONE);
            loadingTextView.setText("松开载入数据");
        }else{
            loadingView.setVisibility(GONE);
            loadingTextView.setText("已全部加载");
        }

    }

    @Override
    public void startAnim(float maxBottomHeight, float bottomHeight) {

    }

    @Override
    public void onPullReleasing(float fraction, float maxBottomHeight, float bottomHeight) {
        if(!onLoadFinish) {
            //向上拉/下拉释放时回调的状态
            loadingView.setVisibility(VISIBLE);
            loadingTextView.setText(R.string.str_loading_default_hint);
        }else {
            loadingView.setVisibility(GONE);
            loadingTextView.setText("已全部加载");
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void reset() {

    }
}
