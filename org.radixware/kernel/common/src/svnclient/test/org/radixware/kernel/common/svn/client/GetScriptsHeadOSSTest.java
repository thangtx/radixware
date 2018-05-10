package org.radixware.kernel.common.svn.client;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.SvnCredentials;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnEntryHandler;
import org.radixware.kernel.common.svn.client.SvnRepository;

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
/**
 *
 * @author akrylov
 */
public class GetScriptsHeadOSSTest {

    @Test
    @Ignore
    public void test() throws RadixSvnException, URISyntaxException {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("https://svn.radixware.org/repos/radix/"), new SvnCredentials[]{SvnCredentials.Factory.newCertificateInstance("akrylov", new File("/home/akrylov/cert2015/akrylov.p12"), "IYiEw=ZXHl)3".toCharArray())});
        String root = instance.getRootUrl();
        instance.getDir("", -1, true, true, true, new SvnEntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                System.out.println(entry.getName());
            }
        });
        SvnEntry.Kind kind = instance.checkPath("scripts/org.radixware", -1);
        Assert.assertEquals(SvnEntry.Kind.DIRECTORY, kind);

    }

    @Test
    @Ignore
    public void testCopyScripts() throws RadixSvnException, URISyntaxException {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("https://svn.radixware.org/repos/radix/"), new SvnCredentials[]{SvnCredentials.Factory.newCertificateInstance("akrylov", new File("/home/akrylov/cert2015/akrylov.p12"), "IYiEw=ZXHl)3".toCharArray())});
        String root = instance.getRootUrl();
        long revision = instance.getLatestRevision();
        SvnEditor editor = instance.getEditor("Copy scripts");
        editor.openDir("scripts", -1);
        editor.openDir("scripts/org.radixware", -1);
        editor.openDir("scripts/org.radixware/0-2.1.7", -1);

        editor.addFile("scripts/org.radixware/0-2.1.7/0.sql", "dev/trunk/org.radixware/dds/scripts/install/0.sql", revision);
        editor.addFile("scripts/org.radixware/0-2.1.7/1.sql", "dev/trunk/org.radixware/dds/scripts/install/1.sql", revision);
        editor.closeEdit();
    }
}
