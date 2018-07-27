# presto-phaelmoita-udfs [![Build Status](https://travis-ci.org/phaelmoita/presto-phaelmoita-udfs.svg?branch=master)](https://travis-ci.org/phaelmoita/presto-phaelmoita-udfs)
With that project you will be able to get geoip using [Maxmind Database](https://dev.maxmind.com/geoip/legacy/geolite/)

Some issues are needs like:
- Load database file from s3 or hdfs

Call this funcion is simple, just with 3 params:
- ip
- path_database
- type_result (country = 0, state = 1, city = 2)
```
select geo_ip('192.64.147.150', '/GeoIP2-City.mmdb', '0'), geo_ip('192.64.147.150', '/GeoIP2-City.mmdb', '1'), geo_ip('192.64.147.150', '/GeoIP2-City.mmdb', '2')
```

# AWS EMR
I added a single script to bootstrap your cluster emr ([bootstrap-emr.sh](https://github.com/phaelmoita/presto-phaelmoita-udfs/blob/master/bootstrap-emr.sh)) so that you can install the plugin when cluster is started.
