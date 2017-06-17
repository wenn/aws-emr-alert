package com.wen.emr;

/**
 * Cluster event detail
 */
public class Detail {
    /**
     * Required default constructor
     */
    public Detail() {
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String clusterId;
    public String name;

    /**
     * Log level severity
     */
    public String severity;
    /**
     * Changed state of the cluster
     */
    public String state;
    /**
     * Message detail of the changed cluster state
     */
    public String message;

    @Override
    public String toString() {
        return "Detail{" +
                "severity='" + severity + '\'' +
                ", clusterId='" + clusterId + '\'' +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
