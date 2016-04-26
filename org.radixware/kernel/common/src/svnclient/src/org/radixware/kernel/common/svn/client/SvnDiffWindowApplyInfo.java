/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

class SvnDiffWindowApplyInfo {

    InputStream inputStream;
    OutputStream outputStream;

    long sourceViewPosition;
    int sourceViewLength;
    int targetViewSize;

    byte[] sourceData;
    byte[] targetData;
    MessageDigest digest;

    public static SvnDiffWindowApplyInfo create(InputStream source, OutputStream target, MessageDigest digest) {
        SvnDiffWindowApplyInfo baton = new SvnDiffWindowApplyInfo();
        baton.inputStream = source;
        baton.outputStream = target;
        baton.sourceData = new byte[0];
        baton.sourceViewLength = 0;
        baton.sourceViewPosition = 0;
        baton.digest = digest;
        return baton;
    }

    private SvnDiffWindowApplyInfo() {
    }

    public String close() {
        try {
            inputStream.close();
        } catch (IOException ex) {
            //ignore
        }
        inputStream = null;
        try {
            outputStream.close();
        } catch (IOException ex) {
            //ignore
        }
        outputStream = null;
        if (digest != null) {
            try {
                return SvnUtil.bytesToHex(digest.digest());
            } finally {
                digest = null;
            }
        }
        return null;
    }

}
