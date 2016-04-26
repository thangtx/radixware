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
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.builder.DirectoryFile.DirInclude;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.utils.FileUtils;


public abstract class DirectoryFileChecker {

    public static final class Factory {

        private Factory() {
        }

        public static DirectoryFileChecker newInstance(final RadixObject radixObject, final IBuildEnvironment env) {
            if (radixObject instanceof AdsModule) {
                return new DirectoryFileChecker.AdsModuleChecker((AdsModule) radixObject, env);
            } else if (radixObject instanceof AdsSegment) {
                return new DirectoryFileChecker.SegmentChecker((AdsSegment) radixObject, env);
            } else if (radixObject instanceof Layer) {
                return new DirectoryFileChecker.LayerChecker((Layer) radixObject, env);
            } else if (radixObject instanceof Branch) {
                return new DirectoryFileChecker.BranchChecker((Branch) radixObject, env);
            } else {
                if (radixObject.isInBranch()) {
                    final Module module = radixObject.getModule();
                    if (module instanceof AdsModule) {
                        return new DirectoryFileChecker.AdsModuleChecker((AdsModule) module, env);
                    }
                }
                throw new IllegalUsageError("Unsupported context type");
            }
        }
    }
    protected final IBuildEnvironment env;

    public DirectoryFileChecker(final IBuildEnvironment env) {
        this.env = env;
    }

    protected DirectoryFile checkSavedFile(File existingFile, RadixObject context, boolean update) {
        if (!existingFile.isFile()) {
            if (!update) {
                env.getBuildProblemHandler().accept(RadixProblem.Factory.newError(context, "No directory.xml file found"));
                return null;
            }
        }
        try {
            return DirectoryFile.Factory.loadFrom(existingFile);
        } catch (XmlException ex) {
            if (!update) {
                env.getBuildProblemHandler().accept(RadixProblem.Factory.newError(context, "Invalid file format: " + existingFile.getPath()));
            }
        } catch (IOException ex) {
            if (!update) {
                env.getBuildProblemHandler().accept(RadixProblem.Factory.newError(context, "Unable to read from file: " + existingFile.getPath()));
            }
        }
        return null;
    }

    protected boolean save(DirectoryFile actualFile, File existingFile, RadixObject context) {
        final boolean ex = existingFile.exists();

        try {
            actualFile.save(existingFile);
            if (ex) {
                env.getFlowLogger().message("[dist] File updated " + existingFile.getPath());
            } else {
                env.getFlowLogger().message("[dist] File created " + existingFile.getPath());
            }
            return true;
        } catch (IOException ex1) {
            env.getBuildProblemHandler().accept(RadixProblem.Factory.newError(context, "Unable to save file: " + existingFile.getPath()));
            return false;
        }
    }

    public abstract boolean check(boolean withSources);

    public abstract boolean update(boolean withSources);

    public final boolean check() {
        return check(false);
    }

    public final boolean update() {
        return update(false);
    }

    private static final class AdsModuleChecker extends DirectoryFileChecker {

        private final AdsModule module;

        public AdsModuleChecker(final AdsModule module, final IBuildEnvironment env) {
            super(env);
            this.module = module;
        }

        @Override
        public boolean check(boolean withSources) {
            return check(false, withSources);
        }

        @Override
        public boolean update(boolean withSources) {
            return check(true, withSources);
        }

        private boolean check(final boolean update, final boolean withSources) {
            RadixProblemRegistry.getDefault().clear(Collections.singletonList(module));
            env.getFlowLogger().stateMessage("[dist] Checking state of module " + module.getQualifiedName());
            final File existingFile = new File(module.getDirectory(), FileUtils.DIRECTORY_XML_FILE_NAME);
            final DirectoryFile storedFile = checkSavedFile(existingFile, module, update);
            final DirectoryFile actualFile;
            try {
                actualFile = DirectoryFile.Factory.loadFrom(module, withSources);
            } catch (IOException ex) {
                env.getBuildProblemHandler().accept(RadixProblem.Factory.newError(module, "Unable to compute actual directory.xml file content: " + ex.getMessage()));
                return false;
            }

            if (!actualFile.equals(storedFile)) {
                if (update) {
                    if (!save(actualFile, existingFile, module)) {
                        return false;
                    }
                } else {
                    env.getBuildProblemHandler().accept(RadixProblem.Factory.newError(module, "File " + existingFile.getPath() + " is not up to date"));
                    return false;
                }
            } else {
                if (update) {
                    env.getFlowLogger().message("[dist] No update required for " + existingFile.getPath());
                }
            }
            return true;
        }
    }

    private static final class SegmentChecker extends DirectoryFileChecker {

        private final transient Segment segment;

        public SegmentChecker(final AdsSegment segment, final IBuildEnvironment env) {
            super(env);
            this.segment = segment;
        }

        @Override
        public boolean check(boolean withSources) {
            return check(false, withSources);
        }

        @Override
        public boolean update(boolean withSources) {
            return check(true, withSources);
        }

        private boolean wasCancelled() {
            if (env.getFlowLogger().getCancellable() != null) {
                return env.getFlowLogger().getCancellable().wasCancelled();
            } else {
                return false;
            }
        }

        private boolean check(final boolean update, final boolean withSources) {
            RadixProblemRegistry.getDefault().clear(Collections.singletonList(segment));
            boolean result = true;
            env.getFlowLogger().message("[dist] Checking state of segment " + segment.getQualifiedName());
            final File existingFile = new File(segment.getDirectory(), FileUtils.DIRECTORY_XML_FILE_NAME);
            final DirectoryFile storedFile = checkSavedFile(existingFile, segment, update);
            final DirectoryFile actualFile = DirectoryFile.Factory.loadFrom(segment);

            if (!actualFile.equals(storedFile)) {
                if (update) {
                    if (!save(actualFile, existingFile, segment)) {
                        return false;
                    }
                } else {
                    env.getBuildProblemHandler().accept(RadixProblem.Factory.newError(segment, "File " + existingFile.getPath() + " is not up to date"));
                    result = false;
                }
            }

            if (segment.getType() == ERepositorySegmentType.ADS) {
                final List<AdsModule> modules = ((AdsSegment) segment).getModules().list();
                StringBuilder message = new StringBuilder();
                if (update) {
                    message.append("Updating ");
                } else {
                    message.append("Checking ");
                }
                message.append("distributuion state of ");
                message.append(modules.size());
                message.append(" modules...");
                final IProgressHandle progressHandle = env.getBuildDisplayer().getProgressHandleFactory().createHandle(message.toString(), new Cancellable() {
                    @Override
                    public boolean cancel() {
                        if (env.getFlowLogger().getCancellable() != null) {
                            return env.getFlowLogger().getCancellable().cancel();
                        } else {
                            return false;
                        }
                    }

                    @Override
                    public boolean wasCancelled() {
                        return DirectoryFileChecker.SegmentChecker.this.wasCancelled();
                    }
                });
                final boolean isOk[] = new boolean[]{true};
                try {
                    progressHandle.start(modules.size());
                    env.getMutex().readAccess(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0, len = modules.size(); i < len; i++) {
                                if (wasCancelled()) {
                                    break;
                                }
                                final AdsModule module = modules.get(i);
                                if (!new DirectoryFileChecker.AdsModuleChecker(module, env).check(update, withSources)) {
                                    isOk[0] = false;
                                }
                                progressHandle.progress(i);
                            }
                        }
                    });
                } finally {
                    progressHandle.finish();
                }
                result = isOk[0];
            }
            return result;
        }
    }

    public static final class LayerChecker extends DirectoryFileChecker {

        private final Layer layer;

        public LayerChecker(Layer layer, IBuildEnvironment env) {
            super(env);
            this.layer = layer;
        }

        @Override
        public boolean check(boolean withSources) {
            return check(false, withSources);
        }

        @Override
        public boolean update(boolean withSources) {
            return check(true, withSources);
        }

        public boolean updateDirectoryLayer() {
            final File dirLayerIndex = new File(layer.getDirectory(), "directory-layer.xml");
            final DirectoryFile storedLayerDirIndex = checkSavedFile(dirLayerIndex, layer, true);


            final DirectoryFile dirLayer = DirectoryFile.Factory.loadFromDirectoryLayer(layer);
            if (!dirLayer.equals(storedLayerDirIndex)) {

                if (!save(dirLayer, dirLayerIndex, layer)) {
                    return false;
                }

            }
            return true;
        }

        private boolean check(boolean update, boolean withSources) {
            RadixProblemRegistry.getDefault().clear(Collections.singletonList(layer));
            env.getFlowLogger().message("[dist] Checking state of layer " + layer.getQualifiedName());
            boolean result = true;
            final File existingFile = new File(layer.getDirectory(), FileUtils.DIRECTORY_XML_FILE_NAME);
            final DirectoryFile storedFile = checkSavedFile(existingFile, layer, update);

            final DirectoryFile actualFile = DirectoryFile.Factory.loadFromMainFile(layer);

            final List<DirInclude> includes = actualFile.getIncludes();

            boolean layerIndexFound = false;

            for (DirInclude include : includes) {
                if (include.getFileName().equals("directory-layer.xml")) {
                    layerIndexFound = true;
                    break;
                }
            }

            if (!layerIndexFound) {
                if (!update) {
                    env.getBuildProblemHandler().accept(RadixProblem.Factory.newError(layer, "Missing reference to directory-layer.xml"));
                    result = false;
                }
            }

            if (!actualFile.equals(storedFile)) {
                if (!update) {
                    env.getBuildProblemHandler().accept(RadixProblem.Factory.newError(layer, "File " + existingFile.getPath() + " is not up to date"));
                    result = false;
                } else {

                    if (!save(actualFile, existingFile, layer)) {
                        result = false;
                    }
                }
            }

            final File dirLayerIndex = new File(layer.getDirectory(), "directory-layer.xml");
            final DirectoryFile storedLayerDirIndex = checkSavedFile(dirLayerIndex, layer, update);

            final DirectoryFile dirLayer = DirectoryFile.Factory.loadFromDirectoryLayer(layer);
            if (!dirLayer.equals(storedLayerDirIndex)) {
                if (update) {
                    if (!save(dirLayer, dirLayerIndex, layer)) {
                        result = false;
                    }
                } else {
                    if (storedFile != null) {
                        env.getBuildProblemHandler().accept(RadixProblem.Factory.newError(layer, "File " + dirLayerIndex.getPath() + " is not up to date"));
                    }
                    result = false;
                }
            }
//            SegmentChecker segmentChecker = new SegmentChecker(layer.getDds(), env);
//            if (!segmentChecker.check(update, withSources)) {
//                result = false;
//            }
            DirectoryFileChecker.SegmentChecker segmentChecker = new DirectoryFileChecker.SegmentChecker((AdsSegment) layer.getAds(), env);
            if (!segmentChecker.check(update, withSources)) {
                result = false;
            }
//            segmentChecker = new SegmentChecker(layer.getUds(), env);
//            if (!segmentChecker.check(update, withSources)) {
//                result = false;
//            }
            return result;
        }
    }

    private static class BranchChecker extends DirectoryFileChecker {

        private final Branch branch;

        public BranchChecker(Branch branch, IBuildEnvironment env) {
            super(env);
            this.branch = branch;
        }

        @Override
        public boolean check(boolean withSources) {
            return check(false, withSources);
        }

        @Override
        public boolean update(boolean withSources) {
            return check(true, withSources);
        }

        private boolean check(boolean update, boolean withSources) {
            boolean result = true;
            for (Layer l : branch.getLayers().getInOrder()) {
                if (l.isReadOnly()) {
                    continue;
                }
                if (!new DirectoryFileChecker.LayerChecker(l, env).check(update, withSources)) {
                    result = false;
                }
            }
            return result;
        }
    }
}
