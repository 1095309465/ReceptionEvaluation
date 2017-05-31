package com.jhzy.receptionevaluation.ui.bean.eldersInfo;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/3/6.
 */

public class Elder implements Parcelable {
    private int ElderID;
    private String ElderName;
    private String PhotoUrl;
    private String BirthDate;
    private String BedTitle;
    private String Address;
    private int Age;

    private String Gender;


    protected Elder(Parcel in) {
        ElderID = in.readInt();
        ElderName = in.readString();
        PhotoUrl = in.readString();
        BirthDate = in.readString();
        BedTitle = in.readString();
        Address = in.readString();
        Age = in.readInt();
        Gender = in.readString();
        sell = in.readString();
        firstLetter = in.readString();
        allLetter = in.readString();
        isShowLetter = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ElderID);
        dest.writeString(ElderName);
        dest.writeString(PhotoUrl);
        dest.writeString(BirthDate);
        dest.writeString(BedTitle);
        dest.writeString(Address);
        dest.writeInt(Age);
        dest.writeString(Gender);
        dest.writeString(sell);
        dest.writeString(firstLetter);
        dest.writeString(allLetter);
        dest.writeByte((byte) (isShowLetter ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Elder> CREATOR = new Creator<Elder>() {
        @Override
        public Elder createFromParcel(Parcel in) {
            return new Elder(in);
        }

        @Override
        public Elder[] newArray(int size) {
            return new Elder[size];
        }
    };

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    private String sell;//汉字首拼音
    private String firstLetter;//首字母
    private String allLetter;//全拼字母
    private boolean isShowLetter;//是否需要显示字母

    @Override
    public String toString() {
        return "Elder{" +
                "ElderID=" + ElderID +
                ", ElderName='" + ElderName + '\'' +
                ", PhotoUrl='" + PhotoUrl + '\'' +
                ", BirthDate='" + BirthDate + '\'' +
                ", BedTitle='" + BedTitle + '\'' +
                ", Address='" + Address + '\'' +
                ", Age=" + Age +
                ", Gender='" + Gender + '\'' +
                ", sell='" + sell + '\'' +
                ", firstLetter='" + firstLetter + '\'' +
                ", allLetter='" + allLetter + '\'' +
                ", isShowLetter=" + isShowLetter +
                '}';
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        if (TextUtils.isEmpty(sell)) {
            return;
        }
        sell = sell.toUpperCase();
        this.sell = sell;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        if (TextUtils.isEmpty(firstLetter)) {
            return;
        }
        firstLetter = firstLetter.toUpperCase();
        this.firstLetter = firstLetter;
    }

    public String getAllLetter() {
        return allLetter;
    }

    public void setAllLetter(String allLetter) {
        if (TextUtils.isEmpty(allLetter)) {
            return;
        }
        allLetter = allLetter.toUpperCase();
        this.allLetter = allLetter;
    }

    public boolean isShowLetter() {
        return isShowLetter;
    }

    public void setShowLetter(boolean showLetter) {
        isShowLetter = showLetter;
    }

    public int getElderID() {
        return ElderID;
    }

    public void setElderID(int ElderID) {
        this.ElderID = ElderID;
    }

    public String getElderName() {
        return ElderName;
    }

    public void setElderName(String ElderName) {
        this.ElderName = ElderName;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String BirthDate) {
        this.BirthDate = BirthDate;
    }

    public String getBedTitle() {
        return BedTitle;
    }

    public void setBedTitle(String BedTitle) {
        this.BedTitle = BedTitle;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }


}