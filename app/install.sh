#!/usr/bin/env sh

ant debug
if [ $? != 0 ]
then
    exit 1
fi
adb install bin/DrexelAutoRegister-debug.apk && adb logcat -s "TAG"
