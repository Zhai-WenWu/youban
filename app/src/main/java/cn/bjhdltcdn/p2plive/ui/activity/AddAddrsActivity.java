package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateAddrsListEvent;
import cn.bjhdltcdn.p2plive.event.UpdateGoodsPayAddressEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveH5ReceiptAddressResponse;
import cn.bjhdltcdn.p2plive.model.AddressInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * Created by Hu_PC on 2017/11/9.
 * 添加收货地址
 */

public class AddAddrsActivity extends BaseActivity implements BaseView {

    private GetStoreListPresenter presenter;
    private long userId;
    private TitleFragment titleFragment;
    private EditText nameEditView,phoneNumEditView,addrsEditView;
    private CheckBox checkBox;
    private Button okBtn;
    private AddressInfo addressInfo;
    private int isDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_addrs);
        Intent intent=getIntent();
        addressInfo=intent.getParcelableExtra("addressInfo");
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        presenter = new GetStoreListPresenter(this);
        setTitle();
        initView();
    }

    private void initView() {
        nameEditView=findViewById(R.id.name_edit_view);
        phoneNumEditView=findViewById(R.id.phone_num_edit_view);
        addrsEditView=findViewById(R.id.addrs_edit_view);
        checkBox=findViewById(R.id.rules_checkbox);
        okBtn=findViewById(R.id.ok_btn_view);
        initAddrs(addressInfo);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加商品
                String name=nameEditView.getText().toString();
                if (StringUtils.isEmpty(name)) {
                    Utils.showToastShortTime("请输入收件人姓名");
                    return;
                }
                String phoneNum=phoneNumEditView.getText().toString();
                if (StringUtils.isEmpty(phoneNum)) {
                    Utils.showToastShortTime("请输入联系电话");
                    return;
                }
                String addrs=addrsEditView.getText().toString();
                if (StringUtils.isEmpty(addrs)) {
                    Utils.showToastShortTime("请输入收货地址");
                    return;
                }
                isDefault=0;//是否设为默认地址(0不默认,1默认),
                if(checkBox.isChecked()){
                    isDefault=1;
                }
                if(addressInfo!=null){
                    addressInfo.setUserId(userId);
                    addressInfo.setContactName(name);
                    addressInfo.setPhoneNumber(phoneNum);
                    addressInfo.setAddress(addrs);
                    addressInfo.setIsDefault(isDefault);
                    presenter.updateH5ReceiptAddress(userId,addressInfo);
                }else{
                    addressInfo=new AddressInfo();
                    addressInfo.setUserId(userId);
                    addressInfo.setContactName(name);
                    addressInfo.setPhoneNumber(phoneNum);
                    addressInfo.setAddress(addrs);
                    addressInfo.setIsDefault(isDefault);
                    presenter.saveH5ReceiptAddress(userId,addressInfo);
                }
            }
        });
    }

    public void initAddrs(AddressInfo addressInfo){
        //初始化商品信息
        if(addressInfo!=null){
            nameEditView.setText(addressInfo.getContactName());
            phoneNumEditView.setText(addressInfo.getPhoneNumber());
            addrsEditView.setText(addressInfo.getAddress());
            if(addressInfo.getIsDefault()==1){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
        }
    }


    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        if(addressInfo!=null) {
            titleFragment.setTitle(R.string.title_edit_addrs);
        }else{
            titleFragment.setTitle(R.string.title_add_addrs);
        }

        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    @Override
    public void updateView(String apiName, Object object) {

        if (isFinishing()) {
            return;
        }
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_SAVEH5RECEIPTADDRESS)) {
            if (object instanceof SaveH5ReceiptAddressResponse) {
                final SaveH5ReceiptAddressResponse saveH5ReceiptAddressResponse = (SaveH5ReceiptAddressResponse) object;
                int code = saveH5ReceiptAddressResponse.getCode();
                Utils.showToastShortTime(saveH5ReceiptAddressResponse.getMsg());
                if (code == 200) {
                    EventBus.getDefault().post(new UpdateAddrsListEvent());
                    if(isDefault==1){
                        addressInfo.setAddressId(saveH5ReceiptAddressResponse.getAddressId());
                        EventBus.getDefault().post(new UpdateGoodsPayAddressEvent(addressInfo));
                    }else{
                        EventBus.getDefault().post(new UpdateGoodsPayAddressEvent(null));
                    }
                    finish();
                }
            }
        }else if (apiName.equals(InterfaceUrl.URL_UPDATEH5RECEIPTADDRESS)) {
            if (object instanceof BaseResponse) {
                final BaseResponse baseResponse = (BaseResponse) object;
                int code = baseResponse.getCode();
                Utils.showToastShortTime(baseResponse.getMsg());
                if (code == 200) {
                    EventBus.getDefault().post(new UpdateAddrsListEvent());
                    if(isDefault==1){
                        EventBus.getDefault().post(new UpdateGoodsPayAddressEvent(addressInfo));
                    }else
                    {
                        EventBus.getDefault().post(new UpdateGoodsPayAddressEvent(null));
                    }
                    finish();
                }
            }
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.onDestroy();
        }
        presenter = null;
        if (titleFragment != null) {
            titleFragment = null;
        }



    }


}
