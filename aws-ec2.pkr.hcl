packer {
  required_plugins {
    amazon = {
      version = ">= 1.2.6"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

variable "aws_region" {
  type    = string
  default = env("AWS_DEFAULT_REGION")
}

variable "subnet_id" {
  type    = string
  default = env("AWS_DEFAULT_SUBNET")
}

variable "source_ami" {
  type    = string
  default = "ami-06db4d78cb1d3bbf9" # Debian 12
}

variable "ssh_username" {
  type    = string
  default = "admin"
}

variable "ssh_password" {
  type    = string
  default = "packer"
}

source "amazon-ebs" "cloudapp-ami" {
  region          = "${var.aws_region}"
  ami_name        = "cloudapp_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for CloudApp"
  ami_regions = [
    "${var.aws_region}",
  ]

  aws_polling {
    delay_seconds = 30
    max_attempts  = 50
  }
  instance_type           = "t2.micro"
  source_ami              = "${var.source_ami}"
  ssh_username            = "${var.ssh_username}"
  subnet_id               = "${var.subnet_id}"
  temporary_key_pair_type = "ed25519"
  ssh_interface           = "public_ip"


  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/sda1"
    volume_size           = 8
    volume_type           = "gp2"
  }
}

build {
  sources = ["source.amazon-ebs.cloudapp-ami"]

  provisioner "file" {
    source      = "./target/cloud.app-0.0.1-SNAPSHOT.jar"
    destination = "/opt/cloud.app-0.0.1-SNAPSHOT.jar"
  }

  provisioner "file" {
    source      = "./setup-cloudapp-instance.sh"
    destination = "/opt/setup-cloudapp-instance.sh"
  }

  provisioner "file" {
    source      = "./application.properties"
    destination = "/opt/application.properties"
  }

  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1"
    ]
    scripts = [
      "setup-cloudapp-instance.sh"
    ]
  }
}
