package com.example.snack.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.snack.R;
import com.example.snack.bean.User;
import com.example.snack.widget.ActionBar;

import org.litepal.crud.DataSupport;


/**
 * 重置密码
 */
public class PasswordActivity extends AppCompatActivity {
    private Activity activity;
    private ActionBar mTitleBar;//标题栏
    private EditText etAccount;
    private EditText etPhone;
    private EditText etNewPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity =this;
        setContentView(R.layout.activity_password);
        etAccount = findViewById(R.id.et_account);
        etPhone = findViewById(R.id.et_age);
        etNewPassword = findViewById(R.id.et_new_password);
        mTitleBar = (ActionBar)findViewById(R.id.myActionBar);
        mTitleBar.setData(activity,"重置密码", R.drawable.ic_back, 0, 0,getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
    }

    //保存信息
    public void save(View v){
        //关闭虚拟键盘
        InputMethodManager inputMethodManager= (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        String account = etAccount.getText().toString();
        String age = etPhone.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        if ("".equals(account)){//账号不能为空
            Toast.makeText(activity,"账号不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(age)){//为空
            Toast.makeText(activity,"电话为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(newPassword)){//密码为空
            Toast.makeText(activity,"新密码为空", Toast.LENGTH_LONG).show();
            return;
        }
        User user = DataSupport.where("account = ? and phone = ?", account,age).findFirst(User.class);
        if (user != null) {
            user.setPassword(newPassword);
            user.save();
            Toast.makeText(activity, "密码修改成功", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(activity, "账号或者电话错误", Toast.LENGTH_SHORT).show();
        }
    }
}
