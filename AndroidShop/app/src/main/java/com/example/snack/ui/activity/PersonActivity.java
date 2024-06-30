package com.example.snack.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.snack.R;
import com.example.snack.bean.User;
import com.example.snack.util.SPUtils;
import com.example.snack.widget.ActionBar;

import org.litepal.crud.DataSupport;


/**
 * 个人信息
 */
public class PersonActivity extends AppCompatActivity {
    private Activity mActivity;
    private ActionBar mTitleBar;//标题栏
    private TextView tvAccount;
    private TextView etNickName;
    private TextView etPhone;
    private TextView etAddress;
    private Button btnSave;//保存
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        mActivity = this;
        tvAccount = findViewById(R.id.tv_account);
        etNickName = findViewById(R.id.tv_nickName);
        etPhone = findViewById(R.id.tv_phone);
        etAddress = findViewById(R.id.tv_address);
        btnSave = findViewById(R.id.btn_save);
        mTitleBar = (ActionBar) findViewById(R.id.myActionBar);
        mTitleBar.setData(mActivity,"个人信息", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        initView();
    }

    private void initView() {
        String account = (String) SPUtils.get(mActivity,"account","");
        User user = DataSupport.where("account = ?", account).findFirst(User.class);
        if (user != null) {
            tvAccount.setText(user.getAccount());
            etNickName.setText(user.getNickName());
            etPhone.setText(String.valueOf(user.getPhone()));
            etAddress.setText(user.getAddress());
        }
        //保存
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = tvAccount.getText().toString();
                String nickName = etNickName.getText().toString();
                String age = etPhone.getText().toString();
                String email = etAddress.getText().toString();
                User user1 = DataSupport.where("account = ?",account).findFirst(User.class);
                if ("".equals(nickName)) {
                    Toast.makeText(mActivity,"昵称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(age)) {
                    Toast.makeText(mActivity,"电话不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(email)) {
                    Toast.makeText(mActivity,"收货地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                user1.setNickName(nickName);
                user1.setPhone(String.valueOf(age));
                user1.setAddress(email);
                user1.save();
                Toast.makeText(mActivity,"保存成功", Toast.LENGTH_SHORT).show();
                finish();//关闭页面
            }
        });
       /* btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.Instance.getMainActivity().finish();
                SPUtils.remove(mActivity,"account");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });*/
    }
}
