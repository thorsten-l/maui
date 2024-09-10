#!/bin/bash

mosquitto_sub -h localhost -p 1883 -t testapp1/state -d -u mc000002 -P hallo123 -i client2

