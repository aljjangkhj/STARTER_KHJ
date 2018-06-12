package com.timer.hyunjae.starttimer.ui;

public class ListViewItem {

    private String countStr;
    private String timeListStr;

    public void setcount(String count){
        countStr = count;
    }

    public void setTimeList(String timeList){
        timeListStr = timeList;
    }

    public String getCountStr(){
        return this.countStr;
    }

    public String getTimeListStr(){
        return  this.timeListStr;
    }
}
