package com.exp.sign;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class BmobUser {

    @Id
    private long id;
    private String number;
    private String name;
    private String username;
    private String password;
    private String sex;
    private String phone;
    private String address;
    private String email;

    @Generated(hash = 1402322161)
    public BmobUser(long id, String number, String name, String username,
                    String password, String sex, String phone, String address,
                    String email) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    @Generated(hash = 2008257999)
    public BmobUser() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
