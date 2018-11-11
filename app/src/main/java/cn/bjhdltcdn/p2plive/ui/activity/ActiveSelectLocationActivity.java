package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.ActivityLocationInfo;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 店铺选择位置列表
 */
public class ActiveSelectLocationActivity extends BaseActivity{

    private TitleFragment titleFragment;
    private TextView tipOneTextView,tipTwoTextView;
    private EditText locationEdit,detailAddressEdit;
    private ActivityLocationInfo activityLocationInfo;
    private int type;//0:选择活动位置 1：选择店铺位置
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_select_location);
        Intent intent=getIntent();
        type=intent.getIntExtra(Constants.Fields.TYPE,0);
        initView();
        setTitle();
    }

    public void initView(){
        locationEdit= findViewById(R.id.location_tv);
        locationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActiveSelectLocationActivity.this,ActiveSelectLocationListActivity.class);
                intent.putExtra(Constants.Fields.TYPE,type);
                startActivityForResult(intent,1);
            }
        });
        locationEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!StringUtils.isEmpty(locationEdit.getText())){
                    titleFragment.setRightViewColor(R.color.color_ffb700);
                    titleFragment.getRightView().setEnabled(true);
                }else{
                    titleFragment.setRightViewColor(R.color.color_999999);
                    titleFragment.getRightView().setEnabled(false);
                }
            }
        });
        detailAddressEdit= findViewById(R.id.detail_address_edit);
        tipOneTextView= findViewById(R.id.tip_tv);
        tipTwoTextView= findViewById(R.id.tip2_tv);
        if(type==1){
            //店铺位置
            locationEdit.setHint("请输入店铺位置");
            detailAddressEdit.setHint("详细地址，最好是宿舍地址～");
            tipOneTextView.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) tipTwoTextView.getLayoutParams();
            layoutParams.setMargins(Utils.dp2px(12),Utils.dp2px(10),Utils.dp2px(22),0);
        }
    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        if(type==1){
            titleFragment.setTitle(R.string.title_shop_location);
        }else{
            titleFragment.setTitle(R.string.title_active_location);
        }
        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        titleFragment.setRightViewTitle("保存", new TitleFragment.RightViewClick() {
            @Override
            public void onClick() {
                //保存定位信息
                Intent intent=new Intent();
                activityLocationInfo.setAddr(activityLocationInfo.getAddr()+detailAddressEdit.getText().toString());
                intent.putExtra("location",activityLocationInfo);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        titleFragment.getRightView().setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    activityLocationInfo=data.getParcelableExtra("location");
                    locationEdit.setText(activityLocationInfo.getAddr());
                    break;

            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
