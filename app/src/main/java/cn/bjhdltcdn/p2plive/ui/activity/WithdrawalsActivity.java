package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

public class WithdrawalsActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        getView();
        setTitle();
    }

    public void getView() {
        Intent intent = getIntent();
        String exchangeDescImg = intent.getStringExtra("exchangeDescImg");
        final String exchangeDescUrl = intent.getStringExtra("exchangeDescUrl");
        ImageView imageView = findViewById(R.id.image_withdrawals);
        Glide.with(this).load(exchangeDescImg).apply(new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg)).into(imageView);
        TextView agreeTextView = findViewById(R.id.tv_agree);
        String agreeStr = agreeTextView.getText().toString();
        if (!TextUtils.isEmpty(agreeStr)) {
            try {
                SpannableStringBuilder style = new SpannableStringBuilder(agreeStr);
                style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_333333)), 5, agreeStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                agreeTextView.setText(style);
            } catch (Exception e) {
                e.printStackTrace();
            }
            agreeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!StringUtils.isEmpty(exchangeDescUrl)) {
                        Intent intent = new Intent(WithdrawalsActivity.this, WXPayEntryActivity.class);
                        intent.putExtra(Constants.Action.ACTION_BROWSE, 7);
                        intent.putExtra(Constants.KEY.KEY_URL, exchangeDescUrl);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void setTitle() {
        TitleFragment fragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        fragment.setTitle(R.string.title_withdrawals);
        fragment.setLeftViewTitle(R.mipmap.back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

}
