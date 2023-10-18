#!/bin/sh

sudo apt-get update
sudo apt-get upgrade
cp users.csv /opt/users.csv
mkdir ~/Downloads
cd ~/Downloads
wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb
sudo dpkg -i jdk-21_linux-x64_bin.deb 
sudo apt-get -y install postgresql
systemctl start postgresql
sudo -i -u postgres psql -d postgres -c "create user hariharansundaram with encrypted password 'gautham123';"
sudo -i -u postgres psql -d postgres -c "grant all privileges on database postgres to hariharansundaram;"
