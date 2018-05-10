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

package org.radixware.kernel.common.svn.notification;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class NotificationConfigTest {

    public NotificationConfigTest() {
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

    static File findBranch(boolean radix) {
        File path = null;

        if ("akrylov".equals(System.getProperty("user.name"))) {
            if (radix) {
                path = new File("/home/akrylov/dev/radix/trunk");
            } else {
                path = new File("/home/akrylov/dev/twrbs/trunk");
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

    @Test
    public void testLoadFromRadixRepository() throws Exception {
//        File b = findBranch(false);
//        SVNRepository repository = SVN.findRepository(b, "svn", ESvnAuthType.SSH_KEY_FILE, "/home/akrylov/Desktop/cert.pem");
//        assertNotNull(repository);
//        NotificationConfig config = NotificationConfig.Factory.loadFrom(repository, null);
//        assertNotNull(repository);
//        List<NotificationEvent> releaseToTestEvents = config.getEvents().getEventsByType(ENotificationEventType.RELEASE_CREATED);
//        if (releaseToTestEvents.isEmpty()) {
//            NotificationEvent e = new NotificationEvent(ENotificationEventType.RELEASE_CREATED);
//            config.getEvents().add(e);
//            NotificationSubscriber s = new NotificationSubscriber();
//            s.setEmail("a.krylov@compassplus.ru");
//            e.getSubscribers().add(s);
//            releaseToTestEvents.add(e);
//            config.saveToRepository(repository);
//            repository.closeSession();
//            b = findBranch(false);
//            repository = SVN.findRepository(b, "svn", ESvnAuthType.SSH_KEY_FILE, "/home/akrylov/Desktop/cert.pem");
//            config = NotificationConfig.Factory.loadFrom(repository, null);
//            releaseToTestEvents = config.getEvents().getEventsByType(ENotificationEventType.RELEASE_CREATED);
//            assertFalse(releaseToTestEvents.isEmpty());
//            e = releaseToTestEvents.get(0);
//            assertFalse(e.getSubscribers().isEmpty());
//            s = e.getSubscribers().list().get(0);
//            assertEquals("a.krylov@compassplus.ru", s.getEmail());
//            config.getEvents().clear();
//            config.saveToRepository(repository);
//        }
    }
}
