package com.exp.sign;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMsg implements Parcelable {

    private String time;
    private long from;
    private String content;

    public ChatMsg() {
    }

    public ChatMsg(String time, long from, String content) {
        this.time = time;
        this.from = from;
        this.content = content;
    }

    protected ChatMsg(Parcel in) {
        time = in.readString();
        from = in.readLong();
        content = in.readString();
    }

    public static final Creator<ChatMsg> CREATOR = new Creator<ChatMsg>() {
        @Override
        public ChatMsg createFromParcel(Parcel in) {
            return new ChatMsg(in);
        }

        @Override
        public ChatMsg[] newArray(int size) {
            return new ChatMsg[size];
        }
    };

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeLong(from);
        dest.writeString(content);
    }
}
