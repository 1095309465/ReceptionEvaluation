package com.jhzy.receptionevaluation.ui.bean.drugnext;

/**
 * Created by nakisaRen
 * on 17/5/2.
 */

public class DrugsBean {
    /**
     * DrugId : 1
     * DrugName : 青霉素
     * DoseUnit : 单位
     * DoseId : 1
     * Dosage : 1
     */

    private int DrugId;
    private String DrugName;
    private String DoseUnit;
    private int DoseId;
    private double Dosage;
    private int minimum;//库存预警数量
    private int currentAmount;//当前库存


    public int getMinimum() {
        return minimum;
    }


    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }


    public int getCurrentAmount() {
        return currentAmount;
    }


    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }


    public int getDrugId() { return DrugId;}


    public void setDrugId(int DrugId) { this.DrugId = DrugId;}


    public String getDrugName() { return DrugName;}


    public void setDrugName(String DrugName) { this.DrugName = DrugName;}


    public String getDoseUnit() { return DoseUnit;}


    public void setDoseUnit(String DoseUnit) { this.DoseUnit = DoseUnit;}


    public int getDoseId() { return DoseId;}


    public void setDoseId(int DoseId) { this.DoseId = DoseId;}


    public double getDosage() { return Dosage;}


    public void setDosage(double Dosage) { this.Dosage = Dosage;}
}
