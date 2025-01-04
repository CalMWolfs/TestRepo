#!/bin/bash

# todo MULTI-VERSION
#TARGET_NAME="build/libs/SkyHanni-mc1.8.9-${UPDATE_VERSION}.jar"
TARGET_NAME="build/libs/TestRepo-${UPDATE_VERSION}.jar"

# Debug: List files before checksum
echo "Listing files before checksum:"

CURRENT_DIR=$(pwd)
echo "Current working directory: ${CURRENT_DIR}"

ls -Rla build/libs

# Print the full path of the file
if [ -f "${TARGET_NAME}" ]; then
    FULL_PATH=$(realpath "${TARGET_NAME}")
    echo "Full path of the file: ${FULL_PATH}"
else
    echo "File does not exist."
    exit 1
fi

# Print the current working directory

read -r -d '' extra_notes <<EOF
Modrinth download: https://modrinth.com/mod/skyhanni/version/${UPDATE_VERSION}

Do **NOT** trust any mod just because they publish a checksum associated with it. These check sums are meant to verify only that two files are identical. They are not a certificate of origin, or a guarantee for the author of these files.

sha256sum: \`$(sha256sum "${TARGET_NAME}"|cut -f 1 -d ' '| tr -d '\n')\`
md5sum: \`$(md5sum "${TARGET_NAME}"|cut -f 1 -d ' '| tr -d '\n')\`

Full path of the file: ${FULL_PATH}
Current working directory: ${CURRENT_DIR}

EOF

extra_notes+="\n\n$(cat build/changelog-GITHUB.txt)"

echo -e "${extra_notes}" > build/update-notes.txt

grep -v "Modrinth download" build/update-notes.txt | sed '1{/^$/d;}' > build/Changelog.md