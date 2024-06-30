package com.example.snack.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snack.R;
import com.example.snack.bean.User;
import com.example.snack.widget.ActionBar;

import org.litepal.crud.DataSupport;


/**
 * 用户明细
 */
public class UserDetailActivity extends AppCompatActivity {
    private ActionBar mActionBar;//标题栏
    private Activity mActivity;
    private TextView account;
    private EditText nickName;
    private EditText phone;
    private EditText address;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        mActivity = this;
        account = findViewById(R.id.account);
        nickName = findViewById(R.id.nickName);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        mActionBar = findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(mActivity,"用户信息", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        mUser = (User) getIntent().getSerializableExtra("user");
        if (mUser != null) {
            account.setText(mUser.getAccount());
            nickName.setText(mUser.getNickName());
            phone.setText(String.valueOf(mUser.getPhone()));
            address.setText(mUser.getAddress());
        }
    }

    //保存
    public void save(View view){
        User user = DataSupport.where("account = ?",mUser.getAccount()).findFirst(User.class);
        String nickNameStr = nickName.getText().toString();
        String phoneStr = phone.getText().toString();
        String addressStr = address.getText().toString();
        if ("".equals(nickNameStr)) {
            Toast.makeText(mActivity,"昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(phoneStr)) {
            Toast.makeText(mActivity,"电话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(addressStr)) {
            Toast.makeText(mActivity,"地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user != null) {
            user.setNickName(nickNameStr);
            user.setPhone(String.valueOf(phoneStr));
            user.setAddress(addressStr);
            user.save();
            Toast.makeText(mActivity,"保存成功", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(mActivity,"保存失败", Toast.LENGTH_SHORT).show();
        }

    }
}
