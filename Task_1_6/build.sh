#!/usr/bin/env bash
set -euo pipefail
cd -- "$(dirname -- "${BASH_SOURCE[0]}")"

mkdir -p build/manual/classes build/manual/docs
find src/main/java -name '*.java' -print > build/manual/sources.txt
javac --release 17 -d build/manual/classes @build/manual/sources.txt
javadoc -d build/manual/docs -sourcepath src/main/java -subpackages ru.nsu.legkov -quiet
jar --create --file build/manual/app.jar --main-class ru.nsu.legkov.Main -C build/manual/classes .
java -jar build/manual/app.jar "$@"
