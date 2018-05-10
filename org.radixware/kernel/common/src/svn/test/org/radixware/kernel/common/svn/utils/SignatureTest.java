/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.svn.utils;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import org.radixware.kernel.common.utils.Hex;


public class SignatureTest {

    @Test
    public void run() throws IOException, NoSuchAlgorithmException {
        byte[] bytes = DirectoryFileSigner.calcFileDigest(new File("/home/akrylov/ssd/radix/dev/trunk/org.radixware/kernel/common/bin/ads.jar"));
        System.out.println(Hex.encode(bytes));
    }
    
}
