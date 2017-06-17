package com.wen.emr;

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

    public String id;
    public String time;

    /**
     * Cluster event detail
     */
    public Detail detail;

    @Override
    public String toString() {
        return "TriggerEvent{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", detail=" + detail +
                '}';
    }
}
