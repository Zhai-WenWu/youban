package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.ClipCamera;

/**
 * Created by zhaiww on 2018/4/24.
 */

public class IDCardTakePhotoActivity extends BaseActivity {

    private ClipCamera camera;
    private Button btn_shoot;
    private String urlPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urlPath = getIntent().getStringExtra(Constants.Fields.IDCARDPHOTOURL);
        setContentView(R.layout.activity_id_card_take_photo);
        camera = (ClipCamera) findViewById(R.id.surface_view);
        btn_shoot = (Button) findViewById(R.id.btn_shoot);
        btn_shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    public void takePhoto() {
        String path = Environment.getExternalStorageDirectory().getPath() + urlPath;
        camera.takePicture(path);
        camera.setOnPhotoFinishListener(new ClipCamera.OnPhotoFinishListener() {
            @Override
            public void onPhotoUrl(Uri url) {
                Intent intent = new Intent();
                intent.putExtra("URL", url.toString()); //将计算的值回传回去
                setResult(2, intent);
                finish();
            }
        });
    }
}
