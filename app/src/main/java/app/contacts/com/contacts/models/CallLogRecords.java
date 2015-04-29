package app.contacts.com.contacts.models;

import android.graphics.Bitmap;
import android.net.Uri;

public class CallLogRecords {

    private String callerName;
    private String callNumber;
    private String callDisplayNumber;
    private String callDayTime;
    private String callDuration;
    private int callType;
    private Bitmap picture;

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallDisplayNumber() {
        return callDisplayNumber;
    }

    public void setCallDisplayNumber(String callDisplayNumber) {
        this.callDisplayNumber = callDisplayNumber;
    }

    public String getCallDayTime() {
        return callDayTime;
    }

    public void setCallDayTime(String callDayTime) {
        this.callDayTime = callDayTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
