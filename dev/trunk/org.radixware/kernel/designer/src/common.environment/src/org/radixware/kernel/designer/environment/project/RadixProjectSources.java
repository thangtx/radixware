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

package org.radixware.kernel.designer.environment.project;

import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.Icon;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.api.queries.SharabilityQuery;
import org.netbeans.spi.project.support.GenericSources;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Branch.Layers;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.environment.files.RadixSharabilityQueryImplementation;
import org.radixware.kernel.designer.environment.files.RadixVisibilityQueryImplementation;


@Deprecated
public class RadixProjectSources implements Sources {

    private static class RadixProjectSourceGroup implements SourceGroup {

        final FileObject root;

        public RadixProjectSourceGroup(FileObject root) {
            this.root = root;
        }

        @Override
        public FileObject getRootFolder() {
            return root;
        }

        @Override
        public boolean contains(FileObject fileObject) throws IllegalArgumentException {
            if (root.equals(fileObject) || FileUtil.isParentOf(root, fileObject)) {
                final File file = FileUtil.toFile(fileObject);
                final RadixVisibilityQueryImplementation vqi = new RadixVisibilityQueryImplementation();
                final RadixSharabilityQueryImplementation sqi = new RadixSharabilityQueryImplementation();
                return (vqi.isVisible(file) && sqi.getSharability(file) != SharabilityQuery.NOT_SHARABLE);
            } else {
                return false;
            }
        }

        @Override
        public Icon getIcon(boolean opened) {
            return RadixObjectIcon.BRANCH.getIcon();
        }

        @Override
        public String getName() {
            return this.getClass().getSimpleName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
        }
    }
    private final Project project;

    public RadixProjectSources(Project project) {
        this.project = project;
    }

    @Override
    public SourceGroup[] getSourceGroups(String type) {
        final Branch branch = project.getLookup().lookup(Branch.class);
        final Layers layers = branch.getLayers();
        final SourceGroup[] result = new SourceGroup[layers.size()];
        for (int i = 0; i < layers.size(); i++) {
            final Layer layer = layers.get(i);
            final File layerDir = layer.getDirectory();
            final FileObject fo = RadixFileUtil.toFileObject(layerDir);
            result[i] = new RadixProjectSourceGroup(fo);
        }
        return result;
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
    }
}
