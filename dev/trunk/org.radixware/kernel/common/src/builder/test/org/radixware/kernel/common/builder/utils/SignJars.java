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
package org.radixware.kernel.common.builder.utils;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import org.radixware.kernel.common.build.directory.DigestWriter;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;

/**
 *
 * @author akrylov
 */
public class SignJars {

    @Test
    public void test() throws IOException, NoSuchAlgorithmException {
        File dir = new File("/home/akrylov/development/twrbs/1.1.33.10/com.tranzaxis/ads/Standby.Test/bin");
        File files[] = dir.listFiles();
        for (File file : files) {
            DigestWriter.writeDigestToFile(file, DirectoryFileSigner.calcFileDigest(file, true), true);
        }
    }
}
