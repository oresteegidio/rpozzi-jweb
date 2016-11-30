#!/bin/bash
#bash setenv.sh

CONTAINER_NAME="robipostgres"
POSTGRES_DB_NAME="robipostgres"
POSTGRES_DB_USER="root"
POSTGRES_DB_PWD="robi"
POSTGRES_DATA_CONTAINER="mysqldata"
#[OSX]
POSTGRES_DATA_VOLUME="/Users/robertopozzi/mysql/data"
#[Windows]
#POSTGRES_DATA_VOLUME="/c/Users/IBM_ADMIN/mysql/data"

echo "Container Name 		= " $CONTAINER_NAME
echo "Database Name 		= " $POSTGRES_DB_NAME
echo "DB Username 		= " $POSTGRES_DB_USER
echo "DB Password 		= " $POSTGRES_DB_PWD
echo "Postgres Data Volume 	= " $POSTGRES_DATA_VOLUME

##############################################################################
############## Create a Data Only container to store Postgres data ##############
##############################################################################
#docker run -d -v /var/lib/mysql --name $POSTGRES_DATA_CONTAINER postgres:9.5.3 true
#docker run -d -v /var/lib/mysql --name $POSTGRES_DATA_CONTAINER busybox true

#################################################
############## Run Postgres container ##############
#################################################
docker run --name $CONTAINER_NAME -e POSTGRES_PASSWORD=$POSTGRES_DB_PWD -it -p 5433:5432 postgres:9.5.3

#docker run --name $CONTAINER_NAME --volumes-from $POSTGRES_DATA_CONTAINER -e MYSQL_DATABASE=$POSTGRES_DB_NAME -e MYSQL_ROOT_PASSWORD=$POSTGRES_DB_PWD -d -p 3307:3306 mysql:5.6.26
#docker run --name $CONTAINER_NAME -v /Users/robertopozzi/mysql/data:/var/lib/mysql -e MYSQL_DATABASE=$POSTGRES_DB_NAME -e MYSQL_ROOT_PASSWORD=$POSTGRES_DB_PWD -d -p 3307:3306 mysql:5.6.26

#####################################################################
############## Run Postgres container in interactive mode ##############
#####################################################################
#docker run --name $CONTAINER_NAME --volumes-from $POSTGRES_DATA_CONTAINER -e MYSQL_DATABASE=$POSTGRES_DB_NAME -e MYSQL_ROOT_PASSWORD=$POSTGRES_DB_PWD -it -p 3307:3306 mysql:5.6.26 /bin/bash



#############################################
############## Push to Bluemix ##############
#############################################
#[Tag the image with my private namespace in the IBM Containers registry]
#docker tag robipostgres registry.ng.bluemix.net/rpozzi/robipostgres:latest

#[Push the image to the IBM Containers registry]
#docker push registry.ng.bluemix.net/rpozzi/robipostgres:latest

#[Run container on Bluemix (based on the image pushed in IBM Containers registry)]
#cf ic run -p 9080 --name robipostgres registry.ng.bluemix.net/rpozzi/robipostgres:latest

#[Request a public ip and bind it to the container]
#cf ic ip request
#cf ic ip bind <IP_ADDRESS_OBTAINED> robipostgres