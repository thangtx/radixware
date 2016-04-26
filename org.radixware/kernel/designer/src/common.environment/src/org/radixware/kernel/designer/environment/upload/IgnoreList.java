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

package org.radixware.kernel.designer.environment.upload;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

/**
 * Ignore list for RadixObjectUpdater.
 * Contains files that user cancel to update.
 */
class IgnoreList {

    private IgnoreList() {
        AbstractRadixObjectUploaderUI.getDefault().addOpenProjectsChangedListener(openProjectsListener);
    }
    private static final IgnoreList IGNORE_LIST_INSTANCE = new IgnoreList();

    public static IgnoreList getDefault() {
        return IGNORE_LIST_INSTANCE;
    }
    // ignore list
    private final Map<File, Long> ignoredFilePaths2TimeWhenIgnored = new HashMap<File, Long>();
    // open projects listener
    private final PropertyChangeListener openProjectsListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("openProjects".equals(evt.getPropertyName())) {
                onOpenedProjectsChanged();
            }
        }
    };

    private static boolean isParent(File parent, File child) {
        for (File file = child.getParentFile(); file != null; file = file.getParentFile()) {
            if (parent.equals(file)) {
                return true;
            }
        }
        return false;
    }

    private synchronized void onOpenedProjectsChanged() {
        final Collection<Branch> openedBranches = RadixFileUtil.getOpenedBranches();
        final File[] files = ignoredFilePaths2TimeWhenIgnored.keySet().toArray(new File[0]);

        for (final File file : files) {
            boolean exist = false;
            for (final Branch branch : openedBranches) {
                final File branchDir = branch.getDirectory();
                if (branchDir != null && isParent(branchDir, file)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                ignoredFilePaths2TimeWhenIgnored.remove(file);
            }
        }
    }

    /**
     * Add file to ignore list.
     */
    public synchronized void add(final File file) {
        assert file != null;

        final Long timeWhenIgnored = file.lastModified();
        ignoredFilePaths2TimeWhenIgnored.put(file, timeWhenIgnored);
    }

    /**
     * @return true if file is in ignore list and was not modified from time when was ignored or if its projects was closed.
     * @return false if file is not in ignore list or was ignored but modified after placing in ignore list (in this case file will be removed from ignore list).
     */
    public synchronized boolean contains(File file) {
        if (file == null) {
            return false;
        }

        final Long timeWhenIgnored = ignoredFilePaths2TimeWhenIgnored.get(file);
        if (timeWhenIgnored != null) {
            final long fileTime = file.lastModified();
            if (fileTime != timeWhenIgnored.longValue()) {
                ignoredFilePaths2TimeWhenIgnored.remove(file);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
