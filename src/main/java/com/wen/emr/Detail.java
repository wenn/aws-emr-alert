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
