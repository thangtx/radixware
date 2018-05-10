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
package org.radixware.kernel.common.svn.utils;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import static org.junit.Assert.*;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Hex;

public class ReleaseVerifierTest {

    static String CERT_FILE;

    @BeforeClass
    public static void setUpClass() throws Exception {
        if (System.getProperty("user.name").equals("akrylov")) {
            //CERT_FILE = "/Users/akrylov/.subversion/id_rsa";
            CERT_FILE = "/home/akrylov/.ssh/id_rsa";
        } else if (System.getProperty("user.name").equals("abelyaev")) {
            CERT_FILE = "C:/Distrib/s/abelyaev.pem";
        }
    }

    public ReleaseVerifierTest() {
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

    public static void main(String[] args) {
        try {
            if (System.getProperty("user.name").equals("avoloshchuk")){
                CERT_FILE = "C:/keys/svn.ppk";
            } else {
                CERT_FILE = "/home/akrylov/.ssh/id_rsa";
            }
            new ReleaseVerifierTest().testVerify();
        } catch (AuthenticationCancelledException ex) {
            Logger.getLogger(ReleaseVerifierTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of verify method, of class ReleaseVerifier.
     */
    @Test
    //@Ignore
    public void testVerify() throws AuthenticationCancelledException {
        System.out.println("verify");
        String[] urls = new String[]{
            //"releases/1.2.14.4.57",
            //"releases/1.2.14.4.29.3",
            //   "releases/1.2.15.12.39",
            //"releases/1.2.16.9.18",
            //"releases/1.2.19.4.23",
            //"releases/1.1.16.10.21"
            //"releases/1.2.20.7.23",
            //"releases/1.2.19.4.36"
            //"trunk/releases/1.1.18.10.15"
            //   "releases/1.2.24.10.10",
            //    "releases/1.2.23.10.23",
            //"releases/1.0",
            //"releases/1.2.26"
            //"releases/1.2.25.10.27"
           // "releases/1.1.29.10.10"
            //"localization/org.radixware.spanish/releases/1.4"
            "releases/2.1.6.1"
                //"releases/1.1.32.10.4"
        };/*
         * "svn+cplus://svn.compassplus.ru/twrbs/trunk/releases/0.2.6",
         */
        //    "svn+cplus://svn.compassplus.ru/twrbs/tests/testradix/releases/0.2.38"
        //"svn+cplus://svn.compassplus.ru/radix/releases/1.1.50"
        //"svn+cplus://svn.compassplus.ru/twrbs/trunk/releases/0.2.39"
        //"svn+cplus://svn.compassplus.ru/twrbs/trunk/releases/0.2.44"
        //"svn+cplus://svn.compassplus.ru/radix/releases/1.1.82"
        //  "svn+cplus://svn.compassplus.ru/radix/clients/org.radixware.games-ext/patches/1.1.83-5"
        //};
        /*
         * List<String> exp = new LinkedList<String>();
         exp.add("org.radixware/ads/Audit");
         */

        for (String url : urls) {
            ReleaseVerifier instance = new ReleaseVerifier(
                    //"svn+cplus://svn2.compassplus.ru/twrbs/trunk",
                               "svn+cplus://svn2.compassplus.ru/radix", 
                    //  "svn+cplus://svn.compassplus.ru/twrbs/csm/switchmaster/",
                    url, "svn",//"avoloshchuk", 
                    ESvnAuthType.SSH_KEY_FILE, CERT_FILE) {
                        @Override
                        public void error(Exception e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void error(String message) {
                            System.out.println(message);
                        }

                        @Override
                        public void message(String message) {
                            System.out.println(message);
                        }
                    };
            boolean expResult = true;
            long time = System.currentTimeMillis();
            boolean result = instance.verify(null/*
             * exp
             */);
            System.out.println(System.currentTimeMillis() - time);
            assertEquals(expResult, result);
        }
    }

    @Ignore
    @Test
    public void testReadSignature() {
        System.out.println("verify");
        String[] urls = new String[]{/*
         * "svn+cplus://svn.compassplus.ru/twrbs/trunk/releases/0.2.6",
         */ //    "svn+cplus://svn.compassplus.ru/twrbs/trunk/releases/0.2.39"
        };
        for (String url : urls) {
            ReleaseVerifier instance = new ReleaseVerifier("svn+cplus://svn.compassplus.ru/radix", "releases/1.1.53.9", "svn", ESvnAuthType.SSH_KEY_FILE, CERT_FILE) {
                @Override
                public void error(Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void error(String message) {
                    System.out.println(message);
                }

                @Override
                public void message(String message) {
                    System.out.println(message);
                }
            };
            ReleaseVerifier.ReleaseFile file = instance.getFile("org.radixware/ads/Explorer.Meta/src/adcFFMRZLRJFRDNHIDYGFCTMWRJ4Y.xml");
            assertNotNull(file);
            try {
                try {
                    //file.read();
                    String s = "C:/Dev/radix/dev/1.1.53/org.radixware/ads/Explorer.Meta/src/adcFFMRZLRJFRDNHIDYGFCTMWRJ4Y.xml";
                    byte[] fileDigest = DirectoryFileSigner.calcFileDigest(new File(s), false, true);
                    String asStr = Hex.encode(fileDigest);
                    System.out.println(asStr);
                    //"/home/akrylov/Desktop/belyaev.xml"
                    //FileUtils.copyFile(file.file, new File(s));
                    assertEquals(/*
                             * "90CBC1130FA83720D0FC374B755C677484EC17D5"
                             */"1AB490ED9C8DCE15D03EA95A64F82EA6CABB7EB5", asStr);//56F764CB7C1190A8A245D4E0C0D53E1D51179D39
                } finally {
                    FileUtils.deleteFile(file.file);
                }
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
                fail(ex.getMessage());
//            } catch (SVNException ex) {
//                ex.printStackTrace();
//                fail(ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                fail(ex.getMessage());
            }
        }
    }
}
