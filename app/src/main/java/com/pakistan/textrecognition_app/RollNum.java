package com.pakistan.textrecognition_app;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RollNum {

    @SerializedName("roll_num")
    @Expose
    private String rollNum;

    public RollNum(String rollNum) {
        super();
        this.rollNum = rollNum;
    }

    public RollNum() {

    }

    public String getRollNum() {
        return rollNum;
    }

    public void setRollNum(String rollNum) {
        this.rollNum = rollNum;
    }

}
