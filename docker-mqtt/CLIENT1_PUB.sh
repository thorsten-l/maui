#!/bin/bash

# remove retained message
# mosquitto_pub -h localhost -p 1883 -t testapp1/state -d -u mc000001 -P test123 -i client1 -q 1 -r -m ""

# publish message
mosquitto_pub -h localhost -p 1883 -t testapp1/state -d -u mc000001 -P test123 -i client1 -q 1 -m "Hello from client1 on : `date`"
