#!/bin/sh

startDir=`pwd`
selfDir="$( dirname -- "$( realpath "$0" )" )"
cd "${selfDir}"

htmlDir="${selfDir}/projects/lang/build/reports/jmh/html"

./gradlew lang:jmh
mkdir -p "${htmlDir}/"
./gradlew lang:jmhReport
x-www-browser "${htmlDir}/index.html" &

cd "${startDir}"
