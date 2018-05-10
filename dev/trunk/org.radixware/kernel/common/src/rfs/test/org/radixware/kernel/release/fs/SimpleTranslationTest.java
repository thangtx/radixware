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

import org.radixware.kernel.release.utils.ClassUtils;
import org.radixware.kernel.release.utils.DefaultBuildEnvironment;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.junit.Test;
import org.radixware.kernel.common.build.directory.DirectoryFileBuilder;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import static org.junit.Assert.*;
import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsDynamicClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserMethodDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.starter.filecache.CacheEntry;
import org.radixware.kernel.starter.filecache.FileCacheEntry;
import org.radixware.kernel.starter.filecache.JarCacheEntry;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.ERevisionMetaType;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader.HowGetFile;
import org.radixware.kernel.starter.radixloader.RadixLoaderAccessor;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.schemas.product.Directory;
import org.radixware.schemas.product.DirectoryDocument;


public class SimpleTranslationTest {

    public static class MyLoader extends RadixLoader {

        File workDir;
        String URL;
        private final HashMap<String, CacheEntry> entries = new HashMap<>();

        public MyLoader(File dir, String topLayerUrl) {
            super(Arrays.asList(topLayerUrl), RadixLoader.LocalFiles.getInstance(""));
            this.workDir = dir;
            this.URL = topLayerUrl;
            RadixLoader.setInstance(this);
        }

        @Override
        public File getRoot() {
            return workDir;
        }

        public RadixLoaderAccessor getAccessor() {
            return new DefaultAccessor(this);
        }

        @Override
        public String getRepositoryUri() {
            return "";
        }

        @Override
        public RevisionMeta createRevisionMetaImpl(long revisionNum, ERevisionMetaType type) throws IOException, XMLStreamException {
            return new RevisionMeta(new RadixLoaderAccessor() {
                @Override
                public CacheEntry getFile(String file, long revision, HowGetFile howGet) throws RadixLoaderException {
                    return getFileImpl(file, revision, howGet);
                }

                @Override
                public CacheEntry getFile(String file, long revision) throws RadixLoaderException {
                    return getFileImpl(file, revision);
                }

                @Override
                public RadixLoader getLoader() {
                    return MyLoader.this;
                }
            }, revisionNum, HowGetFile.fromDir(workDir));
        }

        @Override
        public synchronized String getNativeLibrary(FileMeta fileMeta, RevisionMeta revisionMeta) throws RadixLoaderException {
            return null;
        }

        @Override
        public CacheEntry getFileImpl(String file, long revisionNum) throws RadixLoaderException {
            CacheEntry e = entries.get(file);
            if (e == null) {
                if (file.endsWith(".jar")) {
                    try {
                        e = new JarCacheEntry(new JarFile(new File(workDir, file)), true);
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        fail(ex.getMessage());
                    }
                } else {
                    try {
                        e = new FileCacheEntry(FileUtils.readBinaryFile(new File(workDir, file)));
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        fail(ex.getMessage());
                    }
                }
                entries.put(file, e);
            }
            return e;
        }

        @Override
        public Set<String> actualize(Collection<RevisionMeta> oldRevisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> preloadGroupSuffixes) throws RadixLoaderException {
            return Collections.emptySet();
        }

        @Override
        protected Set<String> actualizeImpl(final long targetRevNum, Collection<RevisionMeta> oldRevisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> preloadGroupSuffixes) throws RadixLoaderException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected CacheEntry getFileImpl(String file, long revision, HowGetFile howGet) throws RadixLoaderException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getDescription() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class TestReleaseRepository extends ReleaseRepository {

        private final RevisionMeta revisionMeta;
        private final int revisionVersion = -1;

        TestReleaseRepository(Branch srcBranch) throws IOException, XMLStreamException {
            this.revisionMeta = new MyLoader(srcBranch.getDirectory(), srcBranch.getLayers().findTop().get(0).getURI()).createRevisionMetaImpl(revisionVersion, null);
        }

        @Override
        protected RevisionMeta getRevisionMeta() {
            return revisionMeta;
        }

        @Override
        public void processException(Throwable e) {
            e.printStackTrace();
        }
    }

    public void regenerateDirectoryXml(ClassUtils utils) throws IOException {
        for (Layer l : utils.branch.getLayers()) {
            File dir = l.getDirectory();
            File dirXml = new File(dir, "directory.xml");
            if (!dirXml.exists()) {
                DirectoryDocument xDoc = DirectoryDocument.Factory.newInstance();
                Directory xDef = xDoc.addNewDirectory();
                Directory.Includes xInc = xDef.addNewIncludes();
                if ((new File(dir, "ads")).exists()) {
                    xInc.addNewInclude().setFileName("ads/directory.xml");
                }
                if ((new File(dir, "dds")).exists()) {
                    xInc.addNewInclude().setFileName("dds/directory.xml");
                }
                if ((new File(dir, "kernel")).exists()) {
                    xInc.addNewInclude().setFileName("kernel/directory.xml");
                    File kernelDir = new File(dir, "kernel");
                    File[] kernelModules = kernelDir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File arg0) {
                            return arg0.isDirectory();
                        }
                    });
                    DirectoryDocument kDoc = DirectoryDocument.Factory.newInstance();
                    Directory kDef = kDoc.addNewDirectory();
                    Directory.Includes kInc = kDef.addNewIncludes();

                    for (File file : kernelModules) {
                        kInc.addNewInclude().setFileName(file.getName() + "/directory.xml");

                        DirectoryDocument mDoc = DirectoryDocument.Factory.newInstance();
                        Directory mDef = mDoc.addNewDirectory();
                        Directory.FileGroups mGs = mDef.addNewFileGroups();
                        Directory.FileGroups.FileGroup mg = mGs.addNewFileGroup();

                        String groupType = "KernelCommon";
                        if (file.getName().equals("server")) {
                            groupType = "KernelServer";
                        } else if (file.getName().equals("explorer")) {
                            groupType = "KernelExplorer";
                        }

                        File dirIndexFile = new File(file, "directory.xml");
                        DirectoryFileBuilder.main(new String[]{dirIndexFile.getAbsolutePath(), ".*/(\\.svn||src||xsd||doc||project||build)/.*", groupType, "(bin||lib)/.*\\.jar"});
                        DirectoryFileSigner.main(new String[]{dirIndexFile.getAbsolutePath()});
                    }
                    kDoc.save(new File(kernelDir, "directory.xml"));
                }
                xDoc.save(dirXml);
            }
        }
    }

    @Test
    public void buildTest() throws Exception {
        ClassUtils utils = new ClassUtils(2, 1, false) {
            @Override
            public void executeTest() throws Exception {

                AdsModule m = chooseAdsModule(0);

                final AdsDynamicClassDef clazz1 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);
                final AdsDynamicClassDef clazz2 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);
                final AdsDynamicClassDef clazz3 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);

                clazz1.setName("clazz1");
                clazz2.setName("clazz2");
                clazz3.setName("clazz3");

                m.getDefinitions().add(clazz1);
                m.getDefinitions().add(clazz2);
                m.getDefinitions().add(clazz3);

                AdsModule m2 = chooseAdsModule(1);

                final AdsDynamicClassDef clazz4 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);
                final AdsDynamicClassDef clazz5 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);
                final AdsDynamicClassDef clazz6 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);


                clazz4.setName("clazz4");
                clazz5.setName("clazz5");
                clazz6.setName("clazz6");

                m2.getDefinitions().add(clazz4);
                m2.getDefinitions().add(clazz5);
                m2.getDefinitions().add(clazz6);

                m2.getDependences().add(m);

                clazz4.getInheritance().setSuperClass(clazz1);
                clazz5.getInheritance().setSuperClass(clazz2);
                clazz6.getInheritance().setSuperClass(clazz3);


                AdsUserMethodDef method = AdsUserMethodDef.Factory.newInstance();


                clazz6.getMethods().getLocal().add(method);
                method.getSource().getItems().clear();
                method.getSource().getItems().add(new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(clazz1)));
                method.getSource().getItems().add(Scml.Text.Factory.newInstance(" v = null;\n"));


                final HashMap<Id, Definition> defs = new HashMap<Id, Definition>();

                branch.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        defs.put(((Definition) radixObject).getId(), (Definition) radixObject);
                    }
                }, new VisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return radixObject instanceof Definition;
                    }
                });

                for (Definition def : defs.values()) {
                    if (def.isSaveable()) {
                        def.save();
                    }
                }

                enablePlatformLibs();
                regenerateDirectoryXml(this);
                BuildActionExecutor executor = new BuildActionExecutor(new DefaultBuildEnvironment());

                executor.execute(branch);

                assertFalse(executor.wasErrors());
                // now uploads new copy of branch using rfs loader and try to compile it
                TestReleaseRepository rp = new TestReleaseRepository(branch);
                Branch rfsBranch = rp.getBranch();

                assertNotNull(rfsBranch);
                try {

                    rfsBranch.visit(new IVisitor() {
                        @Override
                        public void accept(RadixObject radixObject) {
                            Definition def = (Definition) radixObject;
                            System.out.println(def.toString());
                            Definition old = defs.get(def.getId());
                            if (old == null) {
                                fail("Unexpected definition:" + def.toString());
                            }
                            assertEquals(old.getClass(), def.getClass());
                            defs.remove(def.getId());
                        }
                    }, new VisitorProvider() {
                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            return radixObject instanceof Definition;
                        }
                    });
                    if (defs.size() > 0) {
                        System.out.println("--------------------------------------------------------");
                        for (Definition def : defs.values()) {
                            System.out.println(def.toString());
                        }
                        System.out.println("--------------------------------------------------------");
                    }
                    assertEquals(0, defs.size());

                    executor = new BuildActionExecutor(new DefaultBuildEnvironment());

                    executor.execute(rfsBranch);

                    assertFalse(executor.wasErrors());

                    AdsClassDef clazz6rfs = (AdsClassDef) rfsBranch.find(new VisitorProvider() {
                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            return radixObject instanceof AdsClassDef && ((AdsClassDef) radixObject).getId() == clazz6.getId();
                        }
                    });

                    assertNotNull(clazz6rfs);

                    DefaultBuildEnvironment env = new DefaultBuildEnvironment(BuildActionExecutor.EBuildActionType.COMPILE_SINGLE);

                    executor = new BuildActionExecutor(env);

                    executor.execute(clazz6rfs);

                    assertFalse(executor.wasErrors());
                } finally {
                    rp.close();
                }
            }
        };
        utils.test();
    }
}
