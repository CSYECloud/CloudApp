aws ec2 create-launch-template-version \
    --launch-template-name "cloudapp-ec2-launchtemplate" \
    --version-description launch-template-versioning \
    --source-version '$Latest' \
    --launch-template-data "ImageId=$AMI_ID" --profile dev

aws autoscaling update-auto-scaling-group --auto-scaling-group-name csye6225_asg \
  --launch-template LaunchTemplateName=cloudapp-ec2-launchtemplate,Version='$Latest'

aws autoscaling start-instance-refresh \
    --auto-scaling-group-name csye6225_asg \
    --preferences '{"InstanceWarmup": 60, "MinHealthyPercentage": 100}'