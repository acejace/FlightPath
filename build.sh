#!/usr/bin/bash


javac -cp src/flightpath320 src/flightpath320/*.java
mv src/flightpath320/*.class bin/flightpath320/
java -cp bin flightpath320.FlightPath
