#!/bin/bash
#bash setenv.sh

CONTAINER_NAME="rpdb2"
DB2_DB_NAME="sample"
DB2_DB_USER="db2inst1"
DB2_DB_PWD="robi1572"

echo "Container Name 		= " $CONTAINER_NAME
echo "Database Name 		= " $DB2_DB_NAME
echo "DB Username 		= " $DB2_DB_USER
echo "DB Password 		= " $DB2_DB_PWD

#################################################
############## Run DB2 container ##############
#################################################
docker run -it -p 50000:50000 -e DB2INST1_PASSWORD=$DB2_DB_PWD -e LICENSE=accept ibmcom/db2express-c:latest bash

# $ su - db2inst1
# $ db2start
# $ db2sampl

##### DB2 configuration on Luca Amato Data Power
# cap-sg-prd-2.integration.ibmcloud.com:15845

##### DB2 configuration on Robi laptop
# cap-sg-prd-3.integration.ibmcloud.com:16315