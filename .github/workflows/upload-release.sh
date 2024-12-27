#!/bin/bash

preReleaseParam=""
if ! echo "${UPDATE_VERSION}" | grep -E '.*\.0'>/dev/null; then
  preReleaseParam="--prerelease"
fi

# todo MULTI-VERSION
#TARGET_NAME="build/libs/SkyHanni-mc1.8.9-${UPDATE_VERSION}.jar"
TARGET_NAME="build/libs/TestRepo-${UPDATE_VERSION}.jar"

extra_notes=$(cat build/update-notes.txt)

echo "extra_notes: $extra_notes"

gh release create -t "SkyHanni ${UPDATE_VERSION}" -- verify-tag "${UPDATE_VERSION}" --draft \
  --notes "$extra_notes" $preReleaseParam "${TARGET_NAME}"
