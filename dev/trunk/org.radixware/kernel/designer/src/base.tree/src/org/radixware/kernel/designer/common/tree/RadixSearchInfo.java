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
package org.radixware.kernel.designer.common.tree;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.netbeans.api.search.SearchRoot;
import org.netbeans.api.search.SearchScopeOptions;
import org.netbeans.api.search.provider.SearchInfo;
import org.netbeans.api.search.provider.SearchListener;
import org.openide.filesystems.FileObject;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

class RadixSearchInfo extends SearchInfo {

    private final RadixObject radixObject;

    public RadixSearchInfo(RadixObject radixObject) {
        this.radixObject = radixObject;
    }

    @Override
    public boolean canSearch() {
        return radixObject.isInBranch();
    }

    @Override
    public List<SearchRoot> getSearchRoots() {
        List<SearchRoot> roots = new LinkedList<>();
        for (FileObject obj : objectsToSearch()) {
            roots.add(new SearchRoot(obj, null));
        }
        return roots;
    }

    @Override
    protected Iterator<FileObject> createFilesToSearchIterator(SearchScopeOptions sso, SearchListener sl, AtomicBoolean ab) {
        return objectsToSearch().iterator();
    }

    @Override
    protected Iterator<URI> createUrisToSearchIterator(SearchScopeOptions sso, SearchListener sl, AtomicBoolean ab) {
        return Collections.emptyIterator();
    }

    private List<FileObject> objectsToSearch() {
        final List<FileObject> fileObjects = new ArrayList<>();

        final IVisitor visitor = new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                if (radixObject.isSaveable()) {
                    final File file = radixObject.getFile();
                    final FileObject fileObject = RadixFileUtil.toFileObject(file);
                    if (fileObject != null) {
                        fileObjects.add(fileObject);

                    }
                }
            }
        };

        radixObject.visitAll(visitor);
        return fileObjects;
    }
}
