# AWS EMR alert
Receive alerts to chat clients from state changes in EMR.

![Spark Client](https://raw.githubusercontent.com/wenn/aws-emr-alert/master/images/emr.spark.chat.png)

## Setup

#### Step 1: Create and upload config to AWS S3
#### Step 2: Config AWS Lambda

1. Assemble and upload uber jar: `sbt assembly`
2. Set environment variable `S3_CONFIG_PATH` to point to S3 config
3. Add handler `com.wen.emr.Alert::handler`
4. Attach `AmazonS3ReadOnlyAccess` policy to Lambda role

#### Step 3: Config AWS CloudWatch rule

1. Select Event Source as `Event Pattern`
2. Select Target as `Lambda Function`

## Config

```
spark.roomId=<room id>
spark.token=<api token>
```

## Client Support
- Cisco Spark

