#!/bin/bash

preReleaseParam=""
if ! echo "${UPDATE_VERSION}" | grep -E '.*\.0'>/dev/null; then
  preReleaseParam="--prerelease"
fi

# todo MULTI-VERSION
#TARGET_NAME="build/libs/SkyHanni-mc1.8.9-${UPDATE_VERSION}.jar"
TARGET_NAME="build/libs/TestRepo-${UPDATE_VERSION}.jar"

extra_notes=$(cat build/update-notes.txt)

#gh release create -t "SkyHanni ${UPDATE_VERSION}" -- verify-tag "${UPDATE_VERSION}" --draft \
#  --notes "$extra_notes" $preReleaseParam "${TARGET_NAME}"

# List all files in the build folder
echo "Listing all files in the build folder:"
ls -R build

if [ -f "${TARGET_NAME}" ]; then
  gh release create -t "SkyHanni ${UPDATE_VERSION}" --verify-tag "${UPDATE_VERSION}" --draft \
    --notes "$extra_notes" $preReleaseParam "${TARGET_NAME}"
else
  echo "Error: File ${TARGET_NAME} does not exist."
  exit 1
fi
