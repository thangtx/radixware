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
package org.radixware.kernel.common.svn.utils.xml;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;

public abstract class AbstractSvnObserver {

    protected final SVNRepositoryAdapter repository;
    protected final long revision;

    public AbstractSvnObserver(SVNRepositoryAdapter repository, long revision) {
        this.repository = repository;
        this.revision = revision;
    }

    protected List<String> listLayers(String branchPath) throws RadixSvnException {
        final List<String> candidates = new LinkedList<>();
        repository.getDir(branchPath, revision, new SVNRepositoryAdapter.EntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                if (entry.getKind() == SvnEntry.Kind.DIRECTORY) {
                    candidates.add(entry.getName());
                }
            }
        });
        List<String> layers = new LinkedList<>();
        for (String entry : candidates) {
            String entryPath = SvnPath.append(SvnPath.append(branchPath, entry), "layer.xml");
            if (SVN.getKind(repository, entryPath, revision) == SvnEntry.Kind.FILE) {
                layers.add(entry);
            }
        }
        return layers;
    }

    protected List<String> listAdsModules(final String layerPath) throws RadixSvnException {
        final List<String> candidates = new LinkedList<>();
        final String adsPath = SvnPath.append(layerPath, "ads");
        repository.getDir(adsPath, revision, new SVNRepositoryAdapter.EntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                if (entry.getKind() == SvnEntry.Kind.DIRECTORY) {
                    candidates.add(SvnPath.append(adsPath, entry.getName()));
                }
            }
        });
        List<String> modules = new LinkedList<>();
        for (String entry : candidates) {
            String entryPath = SvnPath.append(entry, "module.xml");
            if (SVN.getKind(repository, entryPath, revision) == SvnEntry.Kind.FILE) {
                modules.add(entry);
            }
        }
        return modules;
    }
}
