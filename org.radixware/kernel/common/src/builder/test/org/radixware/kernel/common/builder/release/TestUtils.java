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
package org.radixware.kernel.common.builder.release;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.repository.Branch;

public class TestUtils {

    public static ReleaseSettings createSettings(boolean radix) throws IOException {
        Branch branch = Branch.Factory.loadFromDir(findBranch(radix));
        if (branch == null) {
            return null;
        }
        ReleaseSettings settings = new ReleaseSettings(branch, new DefaultLogger(), new DefaultBuildEnvironment(), false, null);
        settings.TEST_MODE = true;
//        if (!radix) {
//            settings.setUserName("radixtest");
//            settings.setAuthType(ESvnAuthType.SSH_PASSWORD);
//        }
        return settings;
    }

    public static ReleaseSettings createSettingsForRbs() throws IOException {
        Branch branch = Branch.Factory.loadFromDir(findRbsBranch());
        if (branch == null) {
            return null;
        }
        ReleaseSettings settings = new ReleaseSettings(branch, new DefaultLogger(), new DefaultBuildEnvironment(), false, null);
        settings.TEST_MODE = true;

        return settings;
    }

    static File findBranch(boolean radix) {
        File path = null;

        if ("akrylov".equals(System.getProperty("user.name"))) {
            if (radix) {
                path = new File("/home/akrylov/radix/trunk");
            } else {
                path = new File("/home/akrylov/twrbs/trunk");
            }
        }
        if ("abelyaev".equals(System.getProperty("user.name"))) {
            if (radix) {
                path = new File("C:\\Dev\\radix\\dev\\trunk");
            } else {
                path = new File("/Users/akrylov/test/radix/dev/trunk");
            }
        }
        if (path == null) {
            return null;
        }
        if (path.exists()) {
            while (path != null) {
                File bd = new File(path, "branch.xml");
                if (bd.exists() && bd.isFile()) {
                    return path;
                }
                path = path.getParentFile();
            }
        }
        return null;

    }

    static File findRbsBranch() {
        File path = null;

        if ("akrylov".equals(System.getProperty("user.name"))) {
            path = new File("/Users/akrylov/dev/twrbs");

        }
        if (path == null) {
            return null;
        }
        if (path.exists()) {
            while (path != null) {
                File bd = new File(path, "branch.xml");
                if (bd.exists() && bd.isFile()) {
                    return path;
                }
                path = path.getParentFile();
            }
        }
        return null;

    }

}
