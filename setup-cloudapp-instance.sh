#!/bin/sh

sudo apt-get update -y -q
sudo apt-get upgrade -y -q
echo 'debconf debconf/frontend select Noninteractive' | sudo debconf-set-selections
sudo apt-get install -y -q

sudo apt-get install gpgv -y
sudo apt-get install gnupg -y
sudo apt-get install unzip -y


# Install Postgresql
sudo sh -c 'echo "deb https://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
sudo apt-get -y install postgresql

unzip CloudAppRelease.zip
cp CloudAppRelease/users.csv /opt/users.csv
wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb
sudo dpkg -i jdk-21_linux-x64_bin.deb 

sudo -u postgres psql
sudo -i -u postgres psql -d postgres -c "create user hariharansundaram with encrypted password 'gautham123';"
sudo -i -u postgres psql -d postgres -c "grant all privileges on database postgres to hariharansundaram;"
