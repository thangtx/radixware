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

package org.radixware.kernel.common.builder.check.java;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.utils.DefaultBuildEnvironment;
import org.radixware.kernel.common.repository.Branch;


public class LinkageAnalyzerTest {

    public LinkageAnalyzerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of process method, of class LinkageAnalyzer.
     */
    /**
     * Test of open method, of class LinkageAnalyzer.
     */
    @Test
    public void test() throws IOException {
        IBuildEnvironment env = new DefaultBuildEnvironment();
        LinkageAnalyzer analyzer = new LinkageAnalyzer(env);
        Branch currentBranch = Branch.Factory.loadFromDir(new File("/home/akrylov/radix/trunk"));
        try {
            analyzer.process(currentBranch);
            assertFalse(env.getBuildProblemHandler().wasErrors());
        } finally {
            analyzer.close();
        }
    }
}
