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

package org.radixware.kernel.release.fs;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.junit.Test;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import static org.junit.Assert.*;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader.HowGetFile;


public class APILoadTest {

    @Test
    public void testLoadRadix() {
        final File workDir;
        if (System.getProperty("user.name").equals("akrylov")) {
            //workDir = new File("/home/akrylov/dev/twrbs/trunk");
            workDir = new File("/home/akrylov/dev/radix/trunk");
        } else {
            if (System.getProperty("user.name").equals("avoloshchuk")) {
                workDir = new File("E:\\radix");
            } else {
                return;
            }
        }

        ReleaseRepository repository = new ReleaseRepository() {
            private RevisionMeta meta = null;

            @Override
            protected RevisionMeta getRevisionMeta() {
                if (meta == null) {
                    try {
                        meta = new RevisionMeta(new SimpleTranslationTest.MyLoader(workDir,
                                //"com.tranzaxis.rbs-multicarta"
                                "org.radixware").getAccessor(), 0, HowGetFile.fromDir(workDir));
                    } catch (IOException | XMLStreamException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        fail(ex.getMessage());
                    }
                }
                return meta;
            }

            @Override
            public void processException(Throwable e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        };

        try {

            int count = 10;

            long sum = 0;
            for (int i = 0; i < count; i++) {
                long t = System.currentTimeMillis();
                repository.getBranch().visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        System.out.println(radixObject.getQualifiedName());
                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());
                t = System.currentTimeMillis() - t;
                repository.close();
                System.out.println("Visited at: " + t + "ms");
                sum += t;
            }
            double av = (double) sum / count;
            System.out.println("Average upload time: " + Math.round(av) + "ms");

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    @Test
    public void testFindDefinition() {
        final File workDir;
        if (System.getProperty("user.name").equals("akrylov")) {
            //workDir = new File("/home/akrylov/dev/twrbs/trunk");
            workDir = new File("/home/akrylov/dev/radix/trunk");
        } else {
            if (System.getProperty("user.name").equals("avoloshchuk")) {
                workDir = new File("E:\\radix");
            } else {
                return;
            }
        }

        ReleaseRepository repository = new ReleaseRepository() {
            private RevisionMeta meta = null;

            @Override
            protected RevisionMeta getRevisionMeta() {
                if (meta == null) {
                    try {
                        meta = new RevisionMeta(new SimpleTranslationTest.MyLoader(workDir,
                                //"com.tranzaxis.rbs-multicarta"
                                "org.radixware").getAccessor(), 0, HowGetFile.fromDir(workDir));
                    } catch (IOException | XMLStreamException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        fail(ex.getMessage());
                    }
                }
                return meta;
            }

            @Override
            public void processException(Throwable e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        };

        try {

            int count = 10;


            long sum = 0;
            for (int i = 0; i < count; i++) {
                long t = System.currentTimeMillis();
                repository.getBranch().visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        AdsModule module = (AdsModule) radixObject;
                        module.getRepository().containsDefinition(module.getId());
                    }
                }, new VisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return radixObject instanceof AdsModule;
                    }

                    @Override
                    public boolean isContainer(RadixObject radixObject) {
                        return radixObject instanceof Branch || radixObject instanceof AdsSegment || radixObject instanceof Layer;
                    }
                });
                t = System.currentTimeMillis() - t;
                repository.close();
                System.out.println("Not found at: " + t + "ms");
                sum += t;
            }
            double av = (double) sum / count;
            System.out.println("Average lookup time: " + Math.round(av) + "ms");

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}
