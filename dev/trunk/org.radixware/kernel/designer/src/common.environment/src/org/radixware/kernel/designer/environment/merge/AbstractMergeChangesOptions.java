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
package org.radixware.kernel.designer.environment.merge;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.ISvnFSClient;

public abstract class AbstractMergeChangesOptions {

    static private Set<SVNRepositoryAdapter> repositorySet = new HashSet();

    private SVNRepositoryAdapter repository;
    private ISvnFSClient fsClient;
    private File fromBranchFile;
    private File toBranchFile;
    private String toBranchFullName;
    private Layer srcLayer;

    private String fromPreffix;
    private String toPreffix;

    public static void freeSVNRepositoryOptions() {
        for (SVNRepositoryAdapter repository : repositorySet) {
            try {
                repository.close();
            } catch (Exception ex) {
                Logger.getLogger(AdsMergeChangesOptions.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        repositorySet.clear();
    }

    public SVNRepositoryAdapter getRepository() {
        return repository;
    }

    public ISvnFSClient getFsClient() {
        return fsClient;
    }

    public void setFsClient(ISvnFSClient client) {
        fsClient = client;
    }

    public void setRepository(final SVNRepositoryAdapter repository) {
        this.repository = repository;
        if (!repositorySet.contains(repository)) {
            repositorySet.add(repository);
        }
    }

    public File getToBranchFile() {
        return toBranchFile;
    }

    public void setToBranchFile(final File toBranchFile) {
        this.toBranchFile = toBranchFile;
    }

    public File getFromBranchFile() {
        return fromBranchFile;
    }

    public void setFromBranchFile(final File fromBranchFile) {
        this.fromBranchFile = fromBranchFile;
    }

    public String getToBranchFullName() {
        return toBranchFullName;
    }

    public void setToBranchFullName(final String toBranchFullName) {
        this.toBranchFullName = toBranchFullName;
    }

    protected abstract String getFromBranchShortName();

    protected abstract String getToBranchShortName();

    public Layer getSrcLayer() {
        return srcLayer;
    }

    public void setSrcLayer(final Layer srcLayer) {
        this.srcLayer = srcLayer;
    }

    public String getFromPreffix() {
        return fromPreffix;
    }

    public void setFromPreffix(final String fromPreffix) {
        this.fromPreffix = fromPreffix;
    }

    public String getToPreffix() {
        return toPreffix;
    }

    public void setToPreffix(final String toPreffix) {
        this.toPreffix = toPreffix;
    }

    public long getLastRevision() throws RadixSvnException {
        long rev;
        try {
            rev = getRepository().getLatestRevision();
        } catch (RadixSvnException ex) {//SVN kit bug fix
            rev = getRepository().getLatestRevision();
        }
        return rev;
    }

    abstract public String getNameByIndex(final int index);

    abstract public String getFromPathByIndex(final int index);

    abstract public String getToPathByIndex(final int index);

}
