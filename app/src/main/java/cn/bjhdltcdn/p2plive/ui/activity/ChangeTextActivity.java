package cn.bjhdltcdn.p2plive.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.GroupOperationEvent;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * 文字修改的页面
 */
public class ChangeTextActivity extends BaseActivity {

    /**
     * 1 群昵称修改
     */
    private int type;
    /**
     * 传递的参数
     */
    private String extraText;

    private EditText editTextView;
    private ImageView cleanView;
    private ToolBarFragment titleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_text_layout);


        extraText = getIntent().getStringExtra(Constants.KEY.KEY_OBJECT);
        type = getIntent().getIntExtra(Constants.KEY.KEY_TYPE, 0);

        setTitle();

        editTextView = findViewById(R.id.edit_view);
        editTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    cleanView.setVisibility(View.VISIBLE);
                    titleFragment.getRightView().setEnabled(true);
                    titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_ffb700));
                } else {
                    cleanView.setVisibility(View.GONE);
                    titleFragment.getRightView().setEnabled(false);
                    titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_999999));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cleanView = findViewById(R.id.clean_view);
        cleanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextView.setText("");
            }
        });

        if (!StringUtils.isEmpty(extraText)) {
            editTextView.setText(extraText);

            //获取焦点
            editTextView.setFocusable(true);
            editTextView.setFocusableInTouchMode(true);
            editTextView.requestFocus();
            //将光标移至文字末尾
            editTextView.setSelection(extraText.length());
        }

        if (type == 1) {
            editTextView.setMaxLines(20);
        }


    }

    private void setTitle() {

        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        if (type == 1) {
            titleFragment.setTitleView("群名称");
        }

        titleFragment.setRightView("完成", getResources().getColor(R.color.color_999999), new ToolBarFragment.ViewOnclick() {
            @Override
            public void onClick() {

                String text = editTextView.getText().toString().trim();

                if (type == 1) {
                    if (!StringUtils.isEmpty(text) && !text.equals(extraText)) {
                        EventBus.getDefault().post(new GroupOperationEvent(5, text));
                    }

                    finish();
                }
            }
        });
    }

}
