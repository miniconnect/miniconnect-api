#!/bin/sh

selfDir="$( dirname -- "$( realpath -- "$0" )" )"

htmlDir="${selfDir}/projects/lang/build/reports/jmh/html"

"$selfDir/gradlew" lang:jmh
mkdir -p "${htmlDir}/"
"$selfDir/gradlew" lang:jmhReport
x-www-browser "${htmlDir}/index.html" &
