package com.jiang.geo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaceData implements SearchSuggestion, Serializable {

    public static List<PlaceData> sPlaceData;

    protected PlaceData(Parcel in) {
        name = in.readString();
        introduce = in.readString();
        score = in.readDouble();
        phone = in.readString();
        address = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public PlaceData() {
    }

    public PlaceData(String name, String introduce, double score, String phone, String address, double lat, double lng) {
        this.name = name;
        this.introduce = introduce;
        this.score = score;
        this.phone = phone;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public static final Creator<PlaceData> CREATOR = new Creator<PlaceData>() {
        @Override
        public PlaceData createFromParcel(Parcel in) {
            return new PlaceData(in);
        }

        @Override
        public PlaceData[] newArray(int size) {
            return new PlaceData[size];
        }
    };

    public static List<PlaceData> getPlaceData(Context context) {
        try {
            String xml = getFromAssets(context, "place.xml");
            sPlaceData = JSONArray.parseArray(xml, PlaceData.class);
            if (sPlaceData == null) sPlaceData = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sPlaceData;
    }

    public static String getFromAssets(Context context, String fileName) throws IOException {
        InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));

        BufferedReader bufReader = new BufferedReader(inputReader);
        String line = "";
        String Result = "";
        while ((line = bufReader.readLine()) != null)
            Result += line;
        return Result;
    }

    private String name;
    private String introduce;
    private double score;
    private String phone;
    private String address;
    private double lat;
    private double lng;
    private ArrayList<String> imgs;

    public static List<PlaceData> getPlaceData() {
        return sPlaceData;
    }

    public static void setPlaceData(List<PlaceData> placeData) {
        sPlaceData = placeData;
    }

    public static Creator<PlaceData> getCREATOR() {
        return CREATOR;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public static List<PlaceData> search(String newQuery) {
        List<PlaceData> temp = new ArrayList<>();
        for (PlaceData data : sPlaceData) {
            if (data != null && !TextUtils.isEmpty(data.name) && data.name.toUpperCase().indexOf(newQuery.toUpperCase()) != -1) {
                temp.add(data);
            }
        }
        return temp;
    }

    @Override
    public String getBody() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getShareString() {
        return name + ", " + introduce;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(introduce);
        parcel.writeDouble(score);
        parcel.writeString(phone);
        parcel.writeString(address);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }

    @Override
    public String toString() {
        return name;
    }

}
