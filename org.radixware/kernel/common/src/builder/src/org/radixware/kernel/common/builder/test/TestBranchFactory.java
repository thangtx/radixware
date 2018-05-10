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

package org.radixware.kernel.common.builder.test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositoryBranchType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;


public class TestBranchFactory {

    /**
     * @return new instance of branch in temp directory with one layer with
     * English language. Branch must be removed after using.
     */
    public static Branch newInstance() throws IOException {
        final Branch branch = newEmptyInstance();
        Layer layer = branch.getLayers().addNew("test.radix.all", "Test", "", "C+", Collections.singletonList(EIsoLanguage.ENGLISH));
        branch.setBaseDevelopmentLayer(layer);
        return branch;
    }

    /**
     * @return new instance of branch in temp directory with not layers. Branch
     * must be removed after using.
     */
    public static Branch newEmptyInstance() throws IOException {
        final File testBranchTmpDir = File.createTempFile("RadixWareTest", "Branch");
        testBranchTmpDir.delete();
        if (!testBranchTmpDir.mkdir()) {
            throw new IOException("Unable to create test branch directory: " + String.valueOf(testBranchTmpDir));
        }
        testBranchTmpDir.deleteOnExit();
        return Branch.Factory.newInstance(testBranchTmpDir, "branch.test.repositury.url", "0.0-0", "00.0.0", ERepositoryBranchType.DEV);        
    }

    public static Branch findRadixBranch() throws IOException {
        final String workspace = System.getenv("WORKSPACE");
        if (workspace == null || workspace.isEmpty()) {
            return null;
        }

        final File devDir = new File(workspace, "dev");
        final File trunkDir = new File(devDir, "trunk");

        if (new File(trunkDir, "branch.xml").isFile()) {
            return Branch.Factory.loadFromDir(trunkDir);
        }

        final File[] files = new File(workspace).listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory() && !".svn".equals(f.getName().toLowerCase())) {
                    return Branch.Factory.loadFromDir(f);
                }
            }
        }
        return null;
    }
}
