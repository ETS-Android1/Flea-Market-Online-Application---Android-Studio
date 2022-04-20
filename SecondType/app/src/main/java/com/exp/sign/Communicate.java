package com.exp.sign;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.sqlite.greendao.BmobUserDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Communicate {

    @Id
    private long id;
    private long r_id; // from id
    private long u_id; // des id
    private String msg; // 聊天内容json
    private int read;

    @Generated(hash = 349805096)
    public Communicate(long id, long r_id, long u_id, String msg, int read) {
        this.id = id;
        this.r_id = r_id;
        this.u_id = u_id;
        this.msg = msg;
        this.read = read;
    }

    @Generated(hash = 1467605337)
    public Communicate() {
    }

    public void setRead(int read) {
        this.read = read;
    }

    public void addReadFlag(int flag) {
        this.read += flag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getR_id() {
        return r_id;
    }

    public void setR_id(long r_id) {
        this.r_id = r_id;
    }

    public long getU_id() {
        return u_id;
    }

    public void setU_id(long u_id) {
        this.u_id = u_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ChatMsg> convertToMsg() {
        if (TextUtils.isEmpty(msg)) {
            return new ArrayList<>();
        }
        return JSON.parseArray(msg, ChatMsg.class);
    }

    @Override
    public String toString() {
        if (r_id == App.id()) {
            return App.app.getDaoSession().getBmobUserDao()
                    .queryBuilder()
                    .where(BmobUserDao.Properties.Id.eq(u_id))
                    .list().get(0).getName();
        } else {
            return App.app.getDaoSession().getBmobUserDao()
                    .queryBuilder()
                    .where(BmobUserDao.Properties.Id.eq(r_id))
                    .list().get(0).getName();
        }
    }

    public int getRead() {
        return this.read;
    }

    public boolean isRead() {
        long minId = Math.min(Math.abs(new Long(r_id).intValue()), Math.abs(new Long(u_id).intValue()));
        if (this.read == App.intid() || this.read >= minId * 2) {
            return true;
        }
        return false;
    }
}
