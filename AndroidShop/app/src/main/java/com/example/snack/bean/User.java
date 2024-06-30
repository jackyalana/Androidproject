package com.example.snack.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;


/**
 * 用户
 */
public class User extends DataSupport implements Serializable {
    private String account;//账号
    private String password;//密码
    private String nickName;//昵称
    private String phone;//电话
    private String address;//收货地址

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User(String account, String password, String nickName, String phone, String address) {
        this.account = account;
        this.password = password;
        this.nickName = nickName;
        this.phone = phone;
        this.address = address;
    }
}
