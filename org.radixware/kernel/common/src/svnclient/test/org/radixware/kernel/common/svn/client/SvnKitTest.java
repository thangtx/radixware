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
package org.radixware.kernel.common.svn.client;

//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.HashMap;
//import org.junit.Test;
//import org.radixware.kernel.common.svn.RadixSvnException;
//import org.radixware.kernel.common.svn.svnkitdept.SVN;
//import org.tmatesoft.svn.core.ISVNLogEntryHandler;
//import org.tmatesoft.svn.core.SVNException;
//import org.tmatesoft.svn.core.SVNLogEntry;
//import org.tmatesoft.svn.core.SVNProperties;
//import org.tmatesoft.svn.core.SVNURL;
//import org.tmatesoft.svn.core.io.SVNRepository;

/**
 *
 * @author akrylov
 */
class SvnKitTest {
    
}

//public class SvnKitTest {
//
//    @Test
//    public void testGetFile() throws SVNException, RadixSvnException {
//        SVNRepository repo = SVN.createRepository(SVNURL.parseURIDecoded("svn+ssh://svn2.compassplus.ru/radix"), "", "svn", SvnAuthType.SSH_KEY_FILE, "/home/akrylov/.ssh/id_rsa");
//        repo.getFile("/dev/trunk/org.radixware/ads/Acs/src/adcO3RN2IYLGXOBDGAOABIFNQAABA.xml", 102043, new SVNProperties(), null);
//
//    }
//
//    @Test
//    public void testSvnLog() throws RadixSvnException, URISyntaxException, SVNException {
//        SVNRepository repo = SVN.createRepository(SVNURL.parseURIDecoded("svn://10.7.2.138/rep1"), "", "", SvnAuthType.NONE, "");
//        long revision = repo.getLatestRevision();
//        repo.log(new String[]{"/dev/trunk/org.radixware/ads/m222/src/rolAKERLVOTMNGPZJHMTPU6HGUHSY.xml"},
//                revision, 1, true, true, new ISVNLogEntryHandler() {
//
//                    @Override
//                    public void handleLogEntry(SVNLogEntry logEntry) throws SVNException {
//                        System.out.println(logEntry);
//                    }
//                });
//
//    }
//}
