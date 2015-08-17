#!/bin/bash

function install(){
	wget https://github.com/nawrasg/Atlantis/archive/master.zip &&
	unzip master.zip &&
	mv Atlantis-master/Server/Webapp/dist/* ./ &&
	mv Atlantis-master/Server/backend ./backend &&
	php Atlantis-master/Server/install.php $1 atlantis $2 $3 &&
	chmod 777 backend/classes/weather.json && 
	chmod 777 backend/classes/settings.json &&
	mkdir backend/home &&
	crontab Atlantis-master/Server/crontab
}

printf "Welcome to Atlantis !\nThis script will help you downloading your new home automation system.\n"
printf "Please send a message to support@nawrasg.fr if you are facing any problem or if you want to say hi :)\n\n"

printf "Please enter your database host address: "
read ngHost
printf "Please enter your database user name: "
read ngUser
printf "Please enter your database user password: "
read ngPassword

printf "You entered the following answers:\n"
printf "Host: $ngHost \n"
printf "User: $ngUser \n"
printf "Password: $ngPassword \n\n"

read -p "Would you like to proceed? (y/n)" ngChoice
case "$ngChoice" in
	y|Y ) printf "Installing..." && install $ngHost $ngUser $ngPassword;;
	n|N ) printf "Installation cancelled!";;
	* ) printf "Installation cancelled!";;
esac