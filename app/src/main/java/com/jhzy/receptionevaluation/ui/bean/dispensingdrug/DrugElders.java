package com.jhzy.receptionevaluation.ui.bean.dispensingdrug;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by bigyu2012 on 2017/4/27.
 */

public class DrugElders implements Parcelable{
    /**
     * ElderId : 12
     * name : 于飞
     * age : 85
     * bedCode : A001-03
     * tags : [{"timeid":1,"type":"早餐前","isCompleted":0},{"timeid":1,"type":"中餐前","isCompleted":0}]
     */

    private int ElderId;
    private String name;
    private int age;
    private String bedCode;
    private String PhotoUrl;
    private String Gender;

    protected DrugElders(Parcel in) {
        ElderId = in.readInt();
        name = in.readString();
        age = in.readInt();
        bedCode = in.readString();
        PhotoUrl = in.readString();
        Gender = in.readString();
        isSelected = in.readByte() != 0;
        isShowSelectedView = in.readByte() != 0;
        sell = in.readString();
        firstLetter = in.readString();
        allLetter = in.readString();
        isShowLetter = in.readByte() != 0;
    }

    public static final Creator<DrugElders> CREATOR = new Creator<DrugElders>() {
        @Override
        public DrugElders createFromParcel(Parcel in) {
            return new DrugElders(in);
        }

        @Override
        public DrugElders[] newArray(int size) {
            return new DrugElders[size];
        }
    };

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isShowSelectedView() {
        return isShowSelectedView;
    }

    public void setShowSelectedView(boolean showSelectedView) {
        isShowSelectedView = showSelectedView;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    //是否选中
    private boolean isSelected;
    //是否显示选中的试图
    private boolean isShowSelectedView;

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    private List<TagsBean> tags;

    private String sell;//汉字首拼音
    private String firstLetter;//首字母
    private String allLetter;//全拼字母
    private boolean isShowLetter;//是否需要显示字母


    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getAllLetter() {
        return allLetter;
    }

    public void setAllLetter(String allLetter) {
        this.allLetter = allLetter;
    }

    public boolean isShowLetter() {
        return isShowLetter;
    }

    public void setShowLetter(boolean showLetter) {
        isShowLetter = showLetter;
    }

    public int getElderId() {
        return ElderId;
    }

    public void setElderId(int ElderId) {
        this.ElderId = ElderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBedCode() {
        return bedCode;
    }

    public void setBedCode(String bedCode) {
        this.bedCode = bedCode;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ElderId);
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(bedCode);
        dest.writeString(PhotoUrl);
        dest.writeString(Gender);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isShowSelectedView ? 1 : 0));
        dest.writeString(sell);
        dest.writeString(firstLetter);
        dest.writeString(allLetter);
        dest.writeByte((byte) (isShowLetter ? 1 : 0));
    }
}