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
package org.radixware.kernel.common.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;

import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.utils.FileUtils;

public class DirectoryFileBuilder {

    private final IBuildEnvironment buildEnv;
    private final IProblemHandler problemHandle;
    private final BuildActionExecutor.EBuildActionType actionType;

    private final class DiagnosePH implements IProblemHandler {

        boolean errorneous = false;

        @Override
        public void accept(RadixProblem problem) {
            if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                throw new RadixError(problem.getMessage());
            }
        }
    }

    DirectoryFileBuilder(IBuildEnvironment buildEnv, BuildActionExecutor.EBuildActionType actionType) {
        this.buildEnv = buildEnv;
        this.problemHandle = buildEnv.getBuildProblemHandler();
        this.actionType = actionType;
    }

    private DirectoryFileBuilder() {
        this.buildEnv = null;
        this.problemHandle = new DiagnosePH();
        this.actionType = null;
    }

    private boolean wasCancelled() {
        if (buildEnv.getFlowLogger().getCancellable() != null) {
            return buildEnv.getFlowLogger().getCancellable().wasCancelled();
        } else {
            return false;
        }
    }

    public void execute(final Collection<AdsModule> modules) {
        final IProgressHandle progressHandle = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Indexing " + modules.size() + " modules...", new Cancellable() {
            @Override
            public boolean cancel() {
                if (buildEnv.getFlowLogger().getCancellable() != null) {
                    return buildEnv.getFlowLogger().getCancellable().cancel();
                } else {
                    return false;
                }
            }

            @Override
            public boolean wasCancelled() {
                return DirectoryFileBuilder.this.wasCancelled();
            }
        });
        try {
            progressHandle.start(modules.size());
            buildEnv.getMutex().readAccess(new Runnable() {
                @Override
                public void run() {
                    HashSet<AdsSegment> segments = new HashSet<>();
                    indexModules(modules, segments, progressHandle);
                    updateSegmentsAndLayers(segments);
                }
            });
        } finally {
            progressHandle.finish();
        }
    }

//    private Directory.FileGroups getFileGroups(Directory xDef) {
//        Directory.FileGroups groups = xDef.getFileGroups();
//        if (groups == null) {
//            groups = xDef.addNewFileGroups();
//        }
//        return groups;
//    }
//    private static String getRelativePath(File home, File f) {
//        final String result = FileUtils.getRelativePath(home, f);
//        return result.replace('\\', '/'); // because for SVN operations
//    }
    private <T extends Module> void cleanUpModules(Collection<T> modules, HashSet<AdsSegment> segments, IProgressHandle ph) {
        try {
            if (ph != null) {
                ph.switchToDeterminate(modules.size());
            }
            int index = 0;
            for (T module : modules) {
                if (wasCancelled()) {
                    return;
                }
                final File homeDir = module.getDirectory();
                File indexFile = new File(homeDir, "directory.xml");
                if (indexFile.exists()) {
                    indexFile.delete();
                }
                Segment s = module.getSegment();
                if (!segments.contains(s)) {
                    segments.add((AdsSegment) s);
                }
                if (ph != null) {
                    ph.progress(index++);
                }
            }
        } finally {
            if (ph != null) {
                ph.switchToIndeterminate();
            }
        }
    }
//    private static final FileFilter xmlFilter = new FileFilter() {
//
//        @Override
//        public boolean accept(File pathname) {
//            return pathname.getName().endsWith(".xml") && !pathname.getName().equals("directory.xml");
//        }
//    };

//    private class TaskScheduler {
//
//        final SegmentCache segments;
//        final Counter counter;
//        final SyncProblerHandler problems;
//
//        public TaskScheduler(final SegmentCache segments, final Counter counter, SyncProblerHandler problems) {
//            this.segments = segments;
//            this.counter = counter;
//            this.problems = problems;
//        }
//
//        private void execute(Collection<AdsModule>[] collections) {
//            final CountDownLatch latch = new CountDownLatch(collections.length);
//
//            for (Collection<AdsModule> c : collections) {
//                run(c, latch);
//            }
//
//            try {
//                latch.await();
//                System.out.println("ALL TASKS COMPLETED");
//            } catch (InterruptedException ex) {
//            }
//        }
//
//        private void run(final Collection<AdsModule> modules, final CountDownLatch latch) {
//            final Thread t = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    updateModulesImpl(modules, segments, counter, problems);
//                    latch.countDown();
//                }
//            });
//            t.start();
//        }
//    }
    private void updateModules(final Collection<AdsModule> modules, final Set<AdsSegment> segments, final IProgressHandle ph) {

        try {
            if (ph != null) {
                ph.switchToDeterminate(modules.size());
            }

//            if (modules.size() > 10) {
//                final Collection<AdsModule> one = new ArrayList<AdsModule>();
//                final Collection<AdsModule> another = new ArrayList<AdsModule>();
//                int i = 0;
//                for (AdsModule module : modules) {
//                    if (i % 2 == 0) {
//                        one.add(module);
//                    } else {
//                        another.add(module);
//                    }
//                    i++;
//                }
//                new TaskScheduler(new SegmentCache(segments), new Counter(ph), new SyncProblerHandler(problemHandle)).execute(new Collection[]{one, another});
//            } else {
            updateModulesImpl(modules, new SegmentCache(segments), new Counter(ph), new SyncProblerHandler(problemHandle));
            //}
//            for (AdsModule module : modules) {
//                if (wasCancelled()) {
//                    return;
//                }
//                try {
//                    if (module.generateAPI(false)) {
//                        buildEnv.getFlowLogger().message("[dist] Module API updated: " + module.getQualifiedName());
//                    }
//                    DirectoryFile dirFile = DirectoryFile.Factory.loadFrom(module);
//
//                    final File homeDir = module.getDirectory();
//                    final File indexFile = new File(homeDir, FileUtils.DIRECTORY_XML_FILE_NAME);
//
//                    final boolean exist = indexFile.exists();
//                    dirFile.save(indexFile);
//                    if (exist) {
//                        buildEnv.getFlowLogger().message("[dist] Directory index file updated: " + indexFile.getAbsolutePath());
//                    } else {
//                        buildEnv.getFlowLogger().message("[dist] Directory index file created: " + indexFile.getAbsolutePath());
//                    }
//                    //DirectoryFileSigner.main(new String[]{indexFile.getAbsolutePath()});
//                    final Segment s = module.getSegment();
//                    if (!segments.contains(s)) {
//                        segments.add(s);
//                    }
//                } catch (IOException ex) {
//                    problemHandle.accept(RadixProblem.Factory.newError(module, "Directory index file creation error: " + ex.getMessage()));
//                }
//                if (ph != null) {
//                    ph.progress(index++);
//                }
//            }
        } finally {
            if (ph != null) {
                ph.switchToIndeterminate();
            }
        }
    }

    private class Counter {

        private int index = 0;
        private final IProgressHandle handler;

        public Counter(IProgressHandle handler) {
            this.handler = handler;
        }

        private void inc() {
            synchronized (this) {
                if (handler != null) {
                    handler.progress(index);
                }
                index++;
            }
        }
    }

    private class SyncProblerHandler implements IProblemHandler {

        final IProblemHandler src;

        public SyncProblerHandler(IProblemHandler src) {
            this.src = src;
        }

        @Override
        public void accept(RadixProblem problem) {
            synchronized (this) {
                src.accept(problem);
            }
        }
    }

    private class SegmentCache {

        private final Set<AdsSegment> segments;

        public SegmentCache(Set<AdsSegment> segments) {
            this.segments = segments;
        }

        private void addSegment(AdsSegment s) {
            synchronized (this) {
                segments.add(s);
            }
        }
    }

    private void updateModulesImpl(final Collection<AdsModule> modules, final SegmentCache segments, final Counter counter, final SyncProblerHandler problems) {
        if (modules.isEmpty()) {
            return;
        }
        //final int[] i = {0};
        final CountDownLatch waiter = new CountDownLatch(modules.size());
        final ExecutorService operationThreadPool = Executors.newFixedThreadPool(Math.min(10, modules.size()));
        for (final AdsModule module : modules) {

            operationThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (wasCancelled()) {
                            return;
                        }
                        try {
                            if (module.generateAPI(false)) {
                                buildEnv.getFlowLogger().message("[dist] Module API updated: " + module.getQualifiedName());
                            }
                            if (buildEnv.getActionType() == BuildActionExecutor.EBuildActionType.RELEASE || buildEnv.getActionType() == BuildActionExecutor.EBuildActionType.UPDATE_DIST) {
                                if (module.generateUsages(false)) {
                                    buildEnv.getFlowLogger().message("[dist] Module usages list updated: " + module.getQualifiedName());
                                }
                            }

                            final DirectoryFile dirFile = DirectoryFile.Factory.loadFrom(module, false);

                            final File homeDir = module.getDirectory();
                            final File indexFile = new File(homeDir, FileUtils.DIRECTORY_XML_FILE_NAME);

                            final boolean exist = indexFile.exists();
                            dirFile.save(indexFile);
                            if (exist) {
                                buildEnv.getFlowLogger().message("[dist] Directory index file updated: " + indexFile.getAbsolutePath());
                            } else {
                                buildEnv.getFlowLogger().message("[dist] Directory index file created: " + indexFile.getAbsolutePath());
                            }
                            //DirectoryFileSigner.main(new String[]{indexFile.getAbsolutePath()});
                            final AdsSegment s = (AdsSegment) module.getSegment();
                            segments.addSegment(s);

                        } catch (IOException ex) {
                            Logger.getLogger(DirectoryFileBuilder.class.getName()).log(Level.SEVERE, "Error on creation of directory index file", ex);
                            problems.accept(RadixProblem.Factory.newError(module, "Directory index file creation error: " + ex.getMessage()));
                        }
                    } finally {
                        waiter.countDown();
                        counter.inc();
                    }

                }
            });
        }
        try {
            waiter.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        } finally {
            operationThreadPool.shutdown();
        }
    }

    private void updateSegmentsAndLayers(Collection<AdsSegment> s) {
        final HashSet<Layer> layers = new HashSet<>();

        for (Segment segment : s) {
            if (segment.isReadOnly()) {
                continue;
            }
            final File segmentHome = segment.getDirectory();
            final File segmentIndexFile = new File(segmentHome, FileUtils.DIRECTORY_XML_FILE_NAME);
            DirectoryFile segmentFile = DirectoryFile.Factory.loadFrom(segment);
            DirectoryFile oldFile = null;

            try {
                oldFile = segmentIndexFile.exists() ? DirectoryFile.Factory.loadFrom(segmentIndexFile) : null;
            } catch (XmlException | IOException ex) {
                Logger.getLogger(DirectoryFileBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (oldFile == null || !oldFile.equals(segmentFile)) {
                try {
                    boolean exist = segmentIndexFile.exists();
                    segmentFile.save(segmentIndexFile);
                    if (exist) {
                        buildEnv.getFlowLogger().message("[dist] Directory index file updated: " + segmentIndexFile.getAbsolutePath());
                    } else {
                        buildEnv.getFlowLogger().message("[dist] Directory index file created: " + segmentIndexFile.getAbsolutePath());
                    }
                } catch (IOException ex) {
                    problemHandle.accept(RadixProblem.Factory.newError(segment, "Directory index file creation error: " + ex.getMessage()));
                }
            }
            layers.add(segment.getLayer());
        }

        updateLayers(layers);
    }

    public void updateLayers(Collection<Layer> layers) {
        for (Layer layer : layers) {
            if (layer.isReadOnly()) {
                continue;
            }
            final File layerHome = layer.getDirectory();
            final File layerIndexFile = new File(layerHome, FileUtils.DIRECTORY_XML_FILE_NAME);
            DirectoryFile oldFile = null;
            try {
                oldFile = layerIndexFile.exists() ? DirectoryFile.Factory.loadFrom(layerIndexFile) : null;
            } catch (XmlException | IOException ex) {
                Logger.getLogger(DirectoryFileBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
            DirectoryFile layerFile = DirectoryFile.Factory.loadFromMainFile(layer);
            if (oldFile == null || !oldFile.equals(layerFile)) {
                try {
                    boolean exist = layerIndexFile.exists();
                    layerFile.save(layerIndexFile);
                    if (exist) {
                        buildEnv.getFlowLogger().message("[dist] Directory index file updated: " + layerIndexFile.getAbsolutePath());
                    } else {
                        buildEnv.getFlowLogger().message("[dist] Directory index file created: " + layerIndexFile.getAbsolutePath());
                    }
                } catch (IOException ex) {
                    problemHandle.accept(RadixProblem.Factory.newError(layer, "Directory index file creation error: " + ex.getMessage()));
                }
            }
            final File layerDirIndexFile = new File(layerHome, "directory-layer.xml");
            layerFile = DirectoryFile.Factory.loadFromDirectoryLayer(layer);

            oldFile = null;
            try {
                oldFile = layerDirIndexFile.exists() ? DirectoryFile.Factory.loadFrom(layerDirIndexFile) : null;
            } catch (XmlException | IOException ex) {
                Logger.getLogger(DirectoryFileBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (oldFile == null || !oldFile.equals(layerFile)) {
                try {
                    boolean exist = layerDirIndexFile.exists();
                    layerFile.save(layerDirIndexFile);
                    if (exist) {
                        buildEnv.getFlowLogger().message("[dist] Directory index file updated: " + layerDirIndexFile.getAbsolutePath());
                    } else {
                        buildEnv.getFlowLogger().message("[dist] Directory index file created: " + layerDirIndexFile.getAbsolutePath());
                    }
                } catch (IOException ex) {
                    problemHandle.accept(RadixProblem.Factory.newError(layer, "Directory index file creation error: " + ex.getMessage()));
                }
            }
        }
    }

    private void indexModules(final Collection<AdsModule> modules, final HashSet<AdsSegment> segments, final IProgressHandle ph) {
        if (actionType == BuildActionExecutor.EBuildActionType.CLEAN) {
            cleanUpModules(modules, segments, ph);
            return;
        }
        updateModules(modules, segments, ph);
    }
    
    public void indexDdsModules(final Collection<DdsModule> modules){
        List<DdsSegment> segments = new ArrayList<>();
        for (DdsModule module: modules){
            try {
                File dir = module.getDirectory();
                final File directoryXmlFile = new File(dir, FileUtils.DIRECTORY_XML_FILE_NAME);
                final boolean exist = directoryXmlFile.exists();
                module.saveDirectoryXml();
                if (exist) {
                    buildEnv.getFlowLogger().message("[dist] Directory index file updated: " + directoryXmlFile.getAbsolutePath());
                } else {
                    buildEnv.getFlowLogger().message("[dist] Directory index file created: " + directoryXmlFile.getAbsolutePath());
                }
                DdsSegment segment = module.getSegment();
                if (segment == null) {
                    continue;
                }
                if (!segments.contains(segment)) {
                    segments.add(segment);
                }
            } catch (IOException ex) {
                problemHandle.accept(RadixProblem.Factory.newError(module, "Directory index file creation error: " + ex.getMessage()));
            }
        }
        
        for (DdsSegment segment : segments) {
            try {
                File dir = segment.getDirectory();
                final File directoryXmlFile = new File(dir, FileUtils.DIRECTORY_XML_FILE_NAME);
                final boolean exist = directoryXmlFile.exists();

                segment.saveDirectoryXml();

                if (exist) {
                    buildEnv.getFlowLogger().message("[dist] Directory index file updated: " + directoryXmlFile.getAbsolutePath());
                } else {
                    buildEnv.getFlowLogger().message("[dist] Directory index file created: " + directoryXmlFile.getAbsolutePath());
                }
            } catch (IOException ex) {
                problemHandle.accept(RadixProblem.Factory.newError(segment, "Directory index file creation error: " + ex.getMessage()));
            }
        }
    }
}
