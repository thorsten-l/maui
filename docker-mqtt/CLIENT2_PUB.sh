#!/bin/bash

mosquitto_pub -h localhost -p 1883 -t testapp1/command -d -u mc000002 -P hallo123 -i client2 -m "Hello from client2"

