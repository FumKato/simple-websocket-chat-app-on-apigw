# simple-websocket-chat-app-on-apigw
Sample websocket app for Amazon API Gateway.
This app is written by Java.

## Requirements
* Java 11+
* Maven
* AWS CLI
* SAM CLI

## How To Deploy
Build this project.
```bash
mvn clean package
```

Create S3 bucket for uploading artifact, if you have not.
```bash
aws aws s3 mb s3://YOUR_BUCKET_NAME
```

Package and deploy app.
```bash
sam package --output-template-file packaged.yaml --s3-bucket YOUR_BUCKET_NAME
sam deploy --template-file packaged.yaml --stack-name simple-websocket-chat-app --capabilities CAPABILITY_IAM
```

## How To Use
Install WebSocket client like wscat.
Then, connect WebSocket endpoint. (You can see it in stack output values)
```bash
wscat -c wss://YOUR_APP_ENDPOINT
```

Send message.
```bash
{"action":"sendmessage","message":"ENTER MESSAGE HERE"}
```
