#!/bin/bash
PRESTO_CONFIG_SCRIPT=$(cat <<EOF
while [ ! -f /var/run/presto/presto-server.pid ]
do
  sleep 1
done
#DO
echo "presto is running"
#coping geoip database to emr
sudo aws s3 cp s3://${your-bucket}/geo_ip/GeoIP2-City.mmdb /
#copy udf to emr
sudo aws s3 cp s3://${your-bucket}/jars/udfs/phaelmoita-udfs-1.0-SNAPSHOT.zip ./
#add plugin to prest
sudo unzip -o phaelmoita-udfs-1.0-SNAPSHOT.zip -d /usr/lib/presto/plugin
#restart presto
sudo restart presto-server
#End install plugin
exit 0
EOF
)
echo "${PRESTO_CONFIG_SCRIPT}" | tee -a /tmp/presto_config.sh
chmod u+x /tmp/presto_config.sh
/tmp/presto_config.sh &
exit 0