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

package org.radixware.kernel.common.builder.utils;

import org.radixware.kernel.common.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;


public class JarCompareTest {

    @Test
    public void test() {
        final File f1 = new File("/home/akrylov/ssd/twrbs/trunk/com.tranzaxis/kernel/server/bin/xb_cryptoPrint.jar");
        final File f2 = new File("/home/akrylov/development/twrbs-test/trunk/com.tranzaxis/kernel/server/bin/xb_cryptoPrint.jar");
        try {
            final String change = findAllChangesInJars(f1, f2);
            if (change != null) {
                fail(change);
            }
        } catch (IOException ex) {
            Logger.getLogger(JarCompareTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String findAllChangesInJars(File oldJar, File newJar) throws IOException {
        final HashMap<String, byte[]> one = FileUtils.readJar(oldJar);
        final HashMap<String, byte[]> another = FileUtils.readJar(newJar);
        for (String n : one.keySet()) {
            if (!another.containsKey(n)) {
                System.out.println("Deleted '" + n + "'");
            } else {
                byte[] bytes1 = one.get(n);
                byte[] bytes2 = another.get(n);
                if (!Arrays.equals(bytes1, bytes2)) {
                    System.out.println("Modified '" + n + "'");
                }
            }
        }
        for (String n : another.keySet()) {
            if (!one.containsKey(n)) {
                System.out.println("Added '" + n + "'");
            }
        }
        return null;
    }
}
