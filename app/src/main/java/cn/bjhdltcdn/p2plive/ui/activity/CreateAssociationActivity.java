//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.app.Instrumentation;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.KeyEvent;
//import android.view.View;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.event.CreateAssociationEvent;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.ui.fragment.CreateAssociationFragmentStup0;
//import cn.bjhdltcdn.p2plive.ui.fragment.CreateAssociationFragmentStup1;
//import cn.bjhdltcdn.p2plive.ui.fragment.CreateAssociationFragmentStup2;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
//
///**
// * 创建圈子页面
// */
//public class CreateAssociationActivity extends BaseActivity {
//
//    private ToolBarFragment titleFragment;
//
//    // 选择的图片路径
//    private String selectFileUrl;
//    // 圈子名称
//    private String AssociationName;
//    // 圈子简介
//    private String AssociationIntroduction;
//
//    // 圈子类别一级
//    private HobbyInfo firstHobbyInfo;
//    // 二级类别(为自定义类别传入0)
//    private HobbyInfo secondHobbyInfo;
//    // 二级类别自定义名称
//    private String customName;
//    //是否创建聊天室(0 不创建  1 创建)
//    private int isCreateChatInfo;
//    //匿名限制(1-->允许匿名,2-->不允许匿名),
//    private int anonymousLimit = 1;
//    //全匿名3  自定义0
//    private int type;
//    //性别限制(1-->不限,2-->仅男生,3-->仅女生)
//    private int sexLimit = 1;
//    //圈子内容限制(1-->全部可见,2-->仅圈友可见)暂时不用,
//    private int contentLimit = 1;
//
//    public int getContentLimit() {
//        return contentLimit;
//    }
//
//    public void setContentLimit(int contentLimit) {
//        this.contentLimit = contentLimit;
//    }
//
//    public int getSexLimit() {
//        return sexLimit;
//    }
//
//    public void setSexLimit(int sexLimit) {
//        this.sexLimit = sexLimit;
//    }
//
//    public int getAnonymousLimit() {
//        return anonymousLimit;
//    }
//
//    public void setAnonymousLimit(int anonymousLimit) {
//        this.anonymousLimit = anonymousLimit;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public int getIsCreateChatInfo() {
//        return isCreateChatInfo;
//    }
//
//    public void setIsCreateChatInfo(int isCreateChatInfo) {
//        this.isCreateChatInfo = isCreateChatInfo;
//    }
//
//    public void setSelectFileUrl(String selectFileUrl) {
//        this.selectFileUrl = selectFileUrl;
//    }
//
//    public void setAssociationName(String associationName) {
//        AssociationName = associationName;
//    }
//
//    public void setAssociationIntroduction(String associationIntroduction) {
//        AssociationIntroduction = associationIntroduction;
//    }
//
//    public String getSelectFileUrl() {
//        return selectFileUrl;
//    }
//
//    public String getAssociationName() {
//        return AssociationName;
//    }
//
//    public String getAssociationIntroduction() {
//        return AssociationIntroduction;
//    }
//
//    public HobbyInfo getFirstHobbyInfo() {
//        return firstHobbyInfo;
//    }
//
//    public void setFirstHobbyInfo(HobbyInfo firstHobbyInfo) {
//        this.firstHobbyInfo = firstHobbyInfo;
//    }
//
//    public HobbyInfo getSecondHobbyInfo() {
//        return secondHobbyInfo;
//    }
//
//    public void setSecondHobbyInfo(HobbyInfo secondHobbyInfo) {
//        this.secondHobbyInfo = secondHobbyInfo;
//    }
//
//    public String getCustomName() {
//        return customName;
//    }
//
//    public void setCustomName(String customName) {
//        this.customName = customName;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_association_layout);
//
//        setTitle();
//
//        EventBus.getDefault().register(this);
//
//
//        EventBus.getDefault().post(new CreateAssociationEvent(0));
//
//
//    }
//
//
//    private void setTitle() {
//        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setLeftView(new ToolBarFragment.ViewOnclick() {
//            @Override
//            public void onClick() {
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 模拟物理按键，必须在子线程执行
//                        Instrumentation inst = new Instrumentation();
//                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//                    }
//                }).start();
//
//
//            }
//        });
//
//        titleFragment.setRightViewIsVisibility(View.GONE);
//
//    }
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//
//            int count = getSupportFragmentManager().getBackStackEntryCount();
//
//            if (count == 3) {
//                titleFragment.setTitleView("选择圈子分类及标签（2/3）");
//            } else if (count == 2) {
//                titleFragment.setTitleView("创建圈子（1/3）");
//                setSecondHobbyInfo(null);
//                setCustomName(null);
//            } else if (count == 1) {
////                //是否创建聊天室(0 不创建  1 创建)
////                private int isCreateChatInfo;
////                //匿名限制(1-->允许匿名,2-->不允许匿名),
////                private int anonymousLimit = 1;
////                //全匿名3  自定义0
////                private int type;
////                //性别限制(1-->不限,2-->仅男生,3-->仅女生)
////                private int sexLimit = 1;
////                //圈子内容限制(1-->全部可见,2-->仅圈友可见)暂时不用,
////                private int contentLimit = 1;
//                setIsCreateChatInfo(0);
//                setAnonymousLimit(1);
//                setType(0);
//                setSexLimit(1);
//                setContentLimit(1);
//                finish();
//                return true;
//            }
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventCallback(CreateAssociationEvent event) {
//        if (event == null) {
//            return;
//        }
//
//        Fragment mFragment = null;
//
//        switch (event.getStup()) {
//            case 0:
//                titleFragment.setTitleView("创建圈子（1/3）");
//                if (mFragment == null) {
//                    mFragment = new CreateAssociationFragmentStup0();
//                }
//                break;
//
//            case 1:
//                titleFragment.setTitleView("选择圈子分类及标签（2/3）");
//                if (mFragment == null) {
//                    mFragment = new CreateAssociationFragmentStup2();
//                }
//                break;
//
//            case 2:
//                titleFragment.setTitleView("输入圈子基本资料（3/3）");
//                if (mFragment == null) {
//                    mFragment = new CreateAssociationFragmentStup1();
//                }
//                break;
//
//        }
//
//        ActivityUtils.addFragmentToBackStackToActivity(getSupportFragmentManager(), mFragment, R.id.content_frame);
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        EventBus.getDefault().unregister(this);
//    }
//}
