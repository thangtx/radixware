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
package org.radixware.kernel.radixdoc.html;

import org.radixware.kernel.radixdoc.html.SvnFileProvider;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.radixware.kernel.common.components.IProgressHandle;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.radixdoc.IDocLogger;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;

public class HtmlGeneratorTest {

    private final String BRANCH_PATH = "D:\\dev\\radix_test\\trunk1";
    private final String RADIXDOC_OUT = "C:\\Users\\avoloshchuk\\Documents\\radixdoc\\TEST_SVN\\doc.zip";
    private final String PEM_KEY_PATH = "C:/keys/bin/key.pem";
    private final String PPK_EY_PATH = "C:/keys/key.ppk";

    static class TestLocalFileProvider extends LocalFileProvider {

        private List<LayerEntry> cache;
        private List<String> layesr;

        public TestLocalFileProvider(String rootPath, List<String> layesr, File out) {
            super(rootPath, out);

            this.layesr = layesr;
        }

        private boolean documentable(File mdl) {
            final String[] mdls = mdl.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.equals("radixdoc.xml");
                }
            });
            return mdls != null && mdls.length > 0;
        }

        private List<ModuleEntry> getModules(File file) {
            final List<ModuleEntry> modules = new ArrayList<>();
            for (final File mdl : file.listFiles()) {
                if (documentable(mdl)) {
                    modules.add(new ModuleEntry("ads", mdl.getName()));
                }
            }
            return modules;
        }

        @Override
        public List<LayerEntry> getLayers() {
            synchronized (this) {
                if (cache == null) {
                    cache = new ArrayList<>();
                    for (final String layer : layesr) {
                        final File root = new File(getRootPath() + "/" + layer);
                        if (root.exists() && root.isDirectory()) {
                            cache.add(new LayerEntry(layer, layer, 1, getModules(new File(getRootPath() + "/" + layer + "/ads"))));
                        }
                    }
                }
            }
            return cache;
        }
    }

//    @Test
    public void testLocalFilesHtmlGenerator() throws IOException {

        final IRadixdocOptions options = new IRadixdocOptions() {
            @Override
            public Set<EIsoLanguage> getLanguages() {
                return EnumSet.of(EIsoLanguage.ENGLISH);
            }

            @Override
            public boolean isCompressToZip() {
                return false;
            }

            @Override
            public String resolve(String layerUri) {
                return null;
            }

            @Override
            public FileProvider getFileProvider() {
                return new TestLocalFileProvider(BRANCH_PATH,
                        Arrays.asList("org.radixware"), new File(RADIXDOC_OUT));
            }

            @Override
            public IDocLogger getDocLogger() {
                return null;
            }
        };

        HtmlRadixdocGenerator.generate(options, new ProgressHandleImpl(), null);
    }

    @Test
    public void testSvnHtmlGenerator() throws IOException {
        final IRadixdocOptions options = new IRadixdocOptions() {
            SVNRepositoryAdapter repository = null;
            final SvnFileProvider provider = new SvnFileProvider() {
                @Override
                protected SVNRepositoryAdapter getSvnRepository() {
                    try {
                       
                        if (repository == null){
                            repository = SVNRepositoryAdapter.Factory.newInstance("svn+cplus://svn2.compassplus.ru/radix", "releases/2.1.6.9", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), PEM_KEY_PATH);
                        }
                        return repository;
                    } catch (RadixSvnException ex) {
                        Logger.getLogger(HtmlGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Assert.fail("Unable find repository");
                    return null;
                }

                @Override
                protected String[] getLayerPaths() {
                    return new String[]{"org.radixware"};
                }

                @Override
                protected File getOutput() {
                    return new File(RADIXDOC_OUT);
                }

                @Override
                protected long getRevision() {
                    try {
                        return getSvnRepository().getLatestRevision();
                    } catch (RadixSvnException ex) {
                        return -1;
                    }
                }
            };

            @Override
            public Set<EIsoLanguage> getLanguages() {
                return EnumSet.of(EIsoLanguage.ENGLISH);
            }

            @Override
            public boolean isCompressToZip() {
                return true;
            }

            @Override
            public String resolve(String layerUri) {
                return null;
            }

            @Override
            public FileProvider getFileProvider() {
                return provider;
            }

            @Override
            public IDocLogger getDocLogger() {
                return null;
            }
        };

        HtmlRadixdocGenerator.generate(options, new ProgressHandleImpl(), null);
    }

    private static class ProgressHandleImpl implements IProgressHandle {

        public ProgressHandleImpl() {
        }
        int size;
        int progress = 0;

        @Override
        public void switchToIndeterminate() {
        }

        @Override
        public void switchToDeterminate(int size) {
            this.size = size;
        }

        @Override
        public void start() {
            System.out.println("Start...");
        }
        int progress100 = 0;

        @Override
        public void progress(String title, int progress) {
            progress(progress);
        }

        @Override
        public void finish() {
            System.out.println("Finish...");
        }

        @Override
        public void setDisplayName(String name) {
        }

        @Override
        public void progress(int count) {
            this.progress = count;

            int progress100 = (int) (progress * 100.0 / size);
            if (this.progress100 != progress100) {
                System.out.println(progress100 + " %");
                this.progress100 = progress100;
            }
        }

        @Override
        public void start(int count) {
            start();
        }
    }
}
