package com.exp.sign;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Wenxue implements Parcelable {

    @Id
    private long id;
    private String sj;
    private String dd;
    private String ms;
    private String mc;
    private String tp;
    private String fx;
    private String zz;
    private String fp;
    private double lat;
    private double lng;

    protected Wenxue(Parcel in) {
        id = in.readLong();
        sj = in.readString();
        dd = in.readString();
        ms = in.readString();
        mc = in.readString();
        tp = in.readString();
        fx = in.readString();
        zz = in.readString();
        fp = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    @Generated(hash = 115214717)
    public Wenxue(long id, String sj, String dd, String ms, String mc, String tp,
            String fx, String zz, String fp, double lat, double lng) {
        this.id = id;
        this.sj = sj;
        this.dd = dd;
        this.ms = ms;
        this.mc = mc;
        this.tp = tp;
        this.fx = fx;
        this.zz = zz;
        this.fp = fp;
        this.lat = lat;
        this.lng = lng;
    }

    @Generated(hash = 497959365)
    public Wenxue() {
    }

    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    public static Creator<Wenxue> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Wenxue> CREATOR = new Creator<Wenxue>() {
        @Override
        public Wenxue createFromParcel(Parcel in) {
            return new Wenxue(in);
        }

        @Override
        public Wenxue[] newArray(int size) {
            return new Wenxue[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }

    public String getDd() {
        return get(dd);
    }

    public String get(String s) {
        return  s == null ? "" : s;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getMs() {
        return get(ms);
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public String getMc() {
        return get(mc);
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getTp() {
        return get(tp);
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getFx() {
        return get(fx);
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getZz() {
        return get(zz);
    }

    public void setZz(String zz) {
        this.zz = zz;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(sj);
        dest.writeString(dd);
        dest.writeString(ms);
        dest.writeString(mc);
        dest.writeString(tp);
        dest.writeString(fx);
        dest.writeString(zz);
        dest.writeString(fp);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
