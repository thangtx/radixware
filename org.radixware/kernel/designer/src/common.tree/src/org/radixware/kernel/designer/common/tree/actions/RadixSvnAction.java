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

package org.radixware.kernel.designer.common.tree.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.modules.subversion.util.Context;
import org.netbeans.modules.subversion.util.SvnUtils;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import org.radixware.kernel.common.defs.IDirectoryRadixObject;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileInfo;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public abstract class RadixSvnAction extends NodeAction {

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return false;
        }
        for (Node node : activatedNodes) {
            final RadixObject radixObject = node.getLookup().lookup(RadixObject.class);
            if (radixObject == null) {
                return false;
            }
            final File file = radixObject.getFile();
            if (file != null && !SvnUtils.isManaged(file)) {
                return false;
            }
        }
        return true;
    }

    private static class ExclusionVisitor implements IVisitor {

        final List<File> exclusions;

        public ExclusionVisitor(List<File> exclusions) {
            this.exclusions = exclusions;
        }

        @Override
        public void accept(RadixObject radixObject) {
            final File dir = ((IDirectoryRadixObject) radixObject).getDirectory();
            if (dir != null) {
                if (radixObject instanceof AdsModule) {
                    exclusions.add(new File(dir, AdsModule.BUILD_DIR_NAME));
                    exclusions.add(new File(dir, AdsModule.API_FILE_NAME));
                    exclusions.add(new File(dir, AdsModule.BINARIES_DIR_NAME));
                    exclusions.add(new File(dir, AdsModule.STRIP_SOURCES_DIR_NAME));
                    exclusions.add(new File(dir, FileUtils.DIRECTORY_XML_FILE_NAME));
                    exclusions.add(new File(dir, FileUtils.DEFINITIONS_XML_FILE_NAME));
                } else if (radixObject instanceof Layer) {
                    exclusions.add(new File(dir, ERepositorySegmentType.KERNEL.getValue()));
                    //exclusions.add(new File(dir, FileUtils.DIRECTORY_XML_FILE_NAME));
                }
            }
        }
    }

    private static class ExclusionVisitorProvider extends VisitorProvider {

        @Override
        public boolean isTarget(RadixObject radixObject) {
            return radixObject instanceof IDirectoryRadixObject;
        }

        @Override
        public boolean isContainer(RadixObject radixObject) {
            if (radixObject.isSaveable() && !(radixObject instanceof IDirectoryRadixObject)) {
                return false;
            }
            return true;
        }
    }

    private static void addExclusions(List<File> exclusions, RadixObject radixObject) {
        final ExclusionVisitor visitor = new ExclusionVisitor(exclusions);
        final ExclusionVisitorProvider provider = new ExclusionVisitorProvider();
        radixObject.visit(visitor, provider);
    }

    /**
     * @return SVN context for commit operation, include Kernel, but add it into
     * exclusions.
     */
    protected static Context getCommitContext(Node[] nodes) {
        final List<File> files = new ArrayList<File>();
        final List<File> exclusions = new ArrayList<File>(); // only folders supported

        for (Node node : nodes) {
//            final Branch branch = node.getLookup().lookup(Branch.class);
//            if (branch != null) {
//                files.add(branch.getDirectory());
//                for (Layer layer : branch.getLayers()) {
//                    final File layerDir = layer.getDirectory();
//                    final File kernelDir = new File(layerDir, ERepositorySegmentType.KERNEL.getValue());
//                    if (!exclusions.contains(kernelDir)) {
//                        exclusions.add(kernelDir);
//                    }
//                }
//                continue;
//            }
//            final Layer layer = node.getLookup().lookup(Layer.class);
//            if (layer != null) {
//                files.add(layer.getDirectory());
//                final File layerDir = layer.getDirectory();
//                final File kernelDir = new File(layerDir, ERepositorySegmentType.KERNEL.getValue());
//                if (!exclusions.contains(kernelDir)) {
//                    exclusions.add(kernelDir);
//                }
//                continue;
//            }
            final RadixObject radixObject = node.getLookup().lookup(RadixObject.class);
            if (radixObject != null) {
                files.addAll(RadixFileUtil.getVersioningFiles(radixObject));
                addExclusions(exclusions, radixObject);
            }
        }
        final Context result = new Context(files, files, exclusions);
        return result;
    }

    private static void addVersioningChildren(List<File> files, File dir) {
        if (dir != null) {
            for (File file : dir.listFiles()) {
                if (RadixFileInfo.isShareable(file)) {
                    files.add(file);
                }
            }
            Logger.getLogger(RadixSvnAction.class.getName()).log(Level.INFO, "Try to add files into non-existent dir");
        }
    }

    /**
     * @return SVN context for update operation, exclude Kernel.
     */
    public static Context getUpdateContext(Node[] nodes) {
        final List<File> files = new ArrayList<File>();
        final List<File> exclusions = new ArrayList<File>(); // only folders supported

        for (Node node : nodes) {
            final RadixObject radixObject = node.getLookup().lookup(RadixObject.class);
            if (radixObject instanceof Branch) {
                final Branch branch = (Branch) radixObject;
                files.add(branch.getFile());
                for (Layer layer : branch.getLayers()) {
                    addVersioningChildren(files, layer.getDirectory());
                }
            } else if (radixObject instanceof Layer) {
                final Layer layer = (Layer) radixObject;
                addVersioningChildren(files, layer.getDirectory());
            } else {
                files.addAll(RadixFileUtil.getVersioningFiles(radixObject));
            }
            addExclusions(exclusions, radixObject);
        }

        final Context result = new Context(files, files, exclusions);
        return result;
    }

    protected static String getContextDisplayName(Node[] activatedNodes) {
        final Set<RadixObject> radixObjects = new HashSet<RadixObject>();

        for (Node node : activatedNodes) {
            final RadixObject radixObject = node.getLookup().lookup(RadixObject.class);
            radixObjects.add(radixObject);
        }

        if (radixObjects.size() == 1) {
            final RadixObject radixObject = radixObjects.iterator().next();
            //return radixObject.getTypeTitle() + " '" + radixObject.getQualifiedName() + "'";
            return radixObject.getQualifiedName();
        } else {
            final String commonTypeTitle = RadixObjectsUtils.getCommonTypeTitle(radixObjects);
            return radixObjects.size() + " " + commonTypeTitle;
        }
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
