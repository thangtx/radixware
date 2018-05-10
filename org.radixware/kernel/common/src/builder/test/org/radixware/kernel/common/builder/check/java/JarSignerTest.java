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
package org.radixware.kernel.common.builder.check.java;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import org.radixware.kernel.common.utils.Hex;

/**
 *
 * @author akrylov
 */
public class JarSignerTest {

    @Test
    public void test() throws IOException, NoSuchAlgorithmException {
        final File jarfile = new File("/home/akrylov/BUG/ЕЧ/tx/server.jar");        
        byte[] digestBytes = DirectoryFileSigner.calcFileDigest(jarfile);
        String inhex = Hex.encode(digestBytes);
        System.out.println(inhex);
        //DirectoryFileSigner.
    }
}
