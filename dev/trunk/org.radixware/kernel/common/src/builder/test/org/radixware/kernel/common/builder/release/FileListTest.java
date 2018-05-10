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

import org.junit.Test;
import static org.junit.Assert.*;


public class FileListTest {

    @Test
    public void testFileList() throws Exception {
        ReleaseSettings settings = TestUtils.createSettings(true);
        settings.setPerformCleanAndBuild(false);
        ReleaseBuilder makeRelease = new ReleaseBuilder(settings);
        assertTrue(makeRelease.process());
    }

//    @Test
//    public void testFileListOnRbs() throws Exception {
//        if ("akrylov".equals(System.getProperty("user.name"))) {
//            ReleaseSettings settings = TestUtils.createSettingsForRbs();
//            settings.setPerformCleanAndBuild(false);
//            ReleaseBuilder makeRelease = new ReleaseBuilder(settings);
//            assertTrue(makeRelease.process());
//        }
//    }
}
