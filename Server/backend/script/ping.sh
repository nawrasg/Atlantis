#!/bin/bash

IP=$1

RESULT=$(ping -c1 "$IP" | grep -E -o '[0-9]+ received' | cut -f1 -d' ')

if [ "$RESULT" -eq 0 ] 
then
  echo -1
else
  echo "$RESULT"
fi