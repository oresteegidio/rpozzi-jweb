#!/bin/bash
#bash setenv.sh

CONTAINER_NAME="robimysql"
MYSQL_DB_NAME="robimysql"
MYSQL_DB_USER="root"
MYSQL_DB_PWD="robi"
MYSQL_DATA_CONTAINER="mysqldata"
#[OSX]
MYSQL_DATA_VOLUME="/Users/robertopozzi/mysql/data"
#[Windows]
#MYSQL_DATA_VOLUME="/c/Users/IBM_ADMIN/mysql/data"

echo "Container Name 		= " $CONTAINER_NAME
echo "Database Name 		= " $MYSQL_DB_NAME
echo "DB Username 		= " $MYSQL_DB_USER
echo "DB Password 		= " $MYSQL_DB_PWD
echo "MYSQL Data Volume 	= " $MYSQL_DATA_VOLUME

##############################################################################
############## Create a Data Only container to store MySql data ##############
##############################################################################
docker run -d -v /var/lib/mysql --name $MYSQL_DATA_CONTAINER mysql:5.6.26 true
#docker run -d -v /var/lib/mysql --name $MYSQL_DATA_CONTAINER busybox true

#################################################
############## Run MySql container ##############
#################################################
docker run --name $CONTAINER_NAME --volumes-from $MYSQL_DATA_CONTAINER -e MYSQL_DATABASE=$MYSQL_DB_NAME -e MYSQL_ROOT_PASSWORD=$MYSQL_DB_PWD -d -p 3307:3306 mysql:5.6.26
#docker run --name $CONTAINER_NAME -v /Users/robertopozzi/mysql/data:/var/lib/mysql -e MYSQL_DATABASE=$MYSQL_DB_NAME -e MYSQL_ROOT_PASSWORD=$MYSQL_DB_PWD -d -p 3307:3306 mysql:5.6.26

#####################################################################
############## Run MySql container in interactive mode ##############
#####################################################################
#docker run --name $CONTAINER_NAME --volumes-from $MYSQL_DATA_CONTAINER -e MYSQL_DATABASE=$MYSQL_DB_NAME -e MYSQL_ROOT_PASSWORD=$MYSQL_DB_PWD -it -p 3307:3306 mysql:5.6.26 /bin/bash



#############################################
############## Push to Bluemix ##############
#############################################
#[Tag the image with my private namespace in the IBM Containers registry]
#docker tag robimysql registry.ng.bluemix.net/rpozzi/robimysql:latest

#[Push the image to the IBM Containers registry]
#docker push registry.ng.bluemix.net/rpozzi/robimysql:latest

#[Run container on Bluemix (based on the image pushed in IBM Containers registry)]
#cf ic run -p 9080 --name robimysql registry.ng.bluemix.net/rpozzi/robimysql:latest

#[Request a public ip and bind it to the container]
#cf ic ip request
#cf ic ip bind <IP_ADDRESS_OBTAINED> robimysql