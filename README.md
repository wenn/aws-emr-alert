# AWS EMR alert
Receive alerts to chat clients from state changes in EMR.

![Spark Client](https://raw.githubusercontent.com/wenn/aws-emr-alert/master/images/emr.spark.chat.png)

## Setup

### Step 1: Assemble uber jar
    sbt assembly
### Step 2: Load config file to AWS S3
### Step 3: Config AWS Lambda

1. Upload uber jar
2. Set environment variable `S3_CONFIG_PATH` to point to S3 config file
3. Add handler `com.wen.emr.Alert::handler`
4. Attach `AmazonS3ReadOnlyAccess` policy to Lambda role

### Step 4: Config AWS CloudWatch

1. Select Event Source as `Event Pattern`
2. Select Target as `Lambda Function`

