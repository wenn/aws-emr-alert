[![Build Status](https://travis-ci.org/wenn/aws-emr-alert.svg?branch=master)](https://travis-ci.org/wenn/aws-emr-alert)

# AWS EMR alert
Receive alerts to chat clients from state changes in EMR.

![Spark Client](https://raw.githubusercontent.com/wenn/aws-emr-alert/master/images/emr.spark.chat.png)

## Setup

1. Create and upload config to AWS S3
2. Config AWS Lambda
    1. Assemble and upload uber jar: `sbt assembly`
    2. Set environment variable `S3_CONFIG_PATH` to point to S3 config; example, `s3://bucket/prefix/emr-alert.conf`
    3. Add handler `com.wen.emr.Alert::handler`
    4. Attach `AmazonS3ReadOnlyAccess` policy to Lambda role

3. Config AWS CloudWatch rule
    1. Select Event Source as `Event Pattern`
    2. Select Target as `Lambda Function`

## Config

```
spark.roomId=<room id>
spark.token=<api token>
spark.cluster.name.regex=<regex to match cluster name>
spark.cluster.status="WAITING,STARTING..."
spark.cluster.step.status="FAILED,CANCELLED..."
```

## Client Support
- Cisco Spark

