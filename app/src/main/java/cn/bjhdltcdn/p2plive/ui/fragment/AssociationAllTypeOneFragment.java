//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import cn.bjhdltcdn.p2plive.R;
//
///**
// * Created by Hu_PC on 2017/11/8.
// * 全部圈子中的第一类别圈子
// */
//
//public class AssociationAllTypeOneFragment extends BaseFragment{
//    private View rootView;
//    private AppCompatActivity mActivity;
//    private  TextView tv_title;
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mActivity = (AppCompatActivity) context;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_association_all_type_one, null);
//        }
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initView();
//    }
//
//    @Override
//    protected void onVisible(boolean isInit) {
//
//    }
//
//    private void initView(){
//       tv_title = (TextView) rootView.findViewById(R.id.text_view);
//        String str = getArguments().getString("type");
//        if(str!=null){
//            tv_title.setText(str);
//        }
//    };
//
//    public void setType(String type){
//        //得到数据
//        String str = type;
//        if(str!=null){
//            tv_title.setText(str);
//        }
//    }
//
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//}
