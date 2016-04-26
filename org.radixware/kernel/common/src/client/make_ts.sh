#!/bin/bash

makets() {
    CURRENT_DIR=$(realpath .)
    EXPLORER=$CURRENT_DIR/../../../explorer/src/org/radixware/kernel/explorer
    WEB=$CURRENT_DIR/../../../web/src/org/radixware/wps/
    CLIENT=$CURRENT_DIR/src/org/radixware/kernel/common/client
    lupdate -no-obsolete -extensions java $EXPLORER $CLIENT $WEB -ts ./client_en.ts
}

command -v lupdate >/dev/null 2>&1 && makets || echo >&2 "Program 'lupdate' is either not installed or not in \$PATH"
