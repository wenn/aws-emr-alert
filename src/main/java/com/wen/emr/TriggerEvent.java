package com.wen.emr;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AWS CloudWatch event POJO
 */
public class TriggerEvent {

    /**
     * Required default constructor
     */
    public TriggerEvent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public String region;
    public String id;
    public String time;

    @JsonProperty("detail-type")
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
