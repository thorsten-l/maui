#!/bin/bash

mosquitto_sub -h localhost -p 1883 -t testapp1/command -d -u mc000001 -P test123 -i client1

