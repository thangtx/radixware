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
package org.radixware.kernel.common.svn.utils.xml;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.schemas.product.ApiChangesDocument;

public class ApiObserverTest {

    public ApiObserverTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of process method, of class ApiObserver.
     */
    @Test
    public void testProcess() throws Exception {
        SVNRepositoryAdapter svn = SVNRepositoryAdapter.Factory.newInstance("svn+cplus://svn2.compassplus.ru/twrbs/trunk/", "releases", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), "/home/akrylov/.ssh/id_rsa");
        ApiObserver observer = new ApiObserver(svn, -1, "1.1.25.1", "1.1.23.10.59");
        observer.process();
        observer.textualReport();
        ApiChangesDocument xDoc = observer.xmlReport();
        System.out.println(xDoc);
    }
}
