package com.example.shizhan.customfront.model;


/**
 * Created by candy on 2016/8/4.
 */
public class Record {
    private long RecordId;
    private long CustomId;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getRecordId() {
        return RecordId;
    }

    public void setRecordId(long recordId) {
        RecordId = recordId;
    }

    public long getCustomId() {
        return CustomId;
    }

    public void setCustomId(long customId) {
        CustomId = customId;
    }



}
