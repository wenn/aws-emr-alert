package com.wen.emr;

import com.google.gson.annotations.SerializedName;

/**
 * AWS CloudWatch event POJO
 */
public class TriggerEvent {

    /**
     * Required default constructor
     */
    public TriggerEvent() {
    }

    public String region;
    public String id;
    public String time;

    @SerializedName("detail-type")
    public String detailType;

    /**
     * Cluster event detail
     */
    public Detail detail;

    @Override
    public String toString() {
        return "TriggerEvent{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", detailType='" + detailType + '\'' +
                ", region='" + region + '\'' +
                ", detail=" + detail +
                '}';
    }
}
