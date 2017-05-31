package com.jhzy.receptionevaluation.ui.bean;

/**
 * Created by sxmd on 2017/2/27.
 */
@Deprecated
public class Person {
    private String name;//姓名s
    private String age = "31";//年龄
    private String bedCode = "A-303-03";//床位
    private String gender = "女";//性别
    private String sell;//汉字首拼音
    private String firstLetter;//首字母
    private String allLetter;//全拼字母
    private boolean isShowLetter;//是否需要显示字母

    public boolean isShowLetter() {
        return isShowLetter;
    }

    public void setShowLetter(boolean showLetter) {
        isShowLetter = showLetter;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", bedCode='" + bedCode + '\'' +
                ", gender='" + gender + '\'' +
                ", sell='" + sell + '\'' +
                ", firstLetter='" + firstLetter + '\'' +
                ", allLetter='" + allLetter + '\'' +
                ", isShowLetter=" + isShowLetter +
                '}';
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBedCode() {
        return bedCode;
    }

    public void setBedCode(String bedCode) {
        this.bedCode = bedCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Person(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
}
