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
package org.radixware.kernel.common.builder.release;

import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.utils.Utils;

class RepositoryLayer extends RepositoryDir {

    private Layer layer;

    public RepositoryLayer(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryNode createChildNode(SvnEntry svnde, long revision) throws RadixSvnException {
        if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
            if (Utils.equals(svnde.getName(), "ads")) {
                return new RepositoryAdsSegment(parent, repository, revision, svnde.getName());
            } else if (Utils.equals(svnde.getName(), "dds")) {
                return new RepositoryDdsSegment(parent, repository, revision, svnde.getName());
            }
        }
        return super.createChildNode(svnde, revision);
    }

    public RepositoryAdsSegment findAds() throws RadixSvnException {
        RepositoryNode node = findChildByName(sourceRevision, "ads");
        if (node instanceof RepositoryAdsSegment) {
            return (RepositoryAdsSegment) node;
        }
        return null;
    }

    public RepositoryDdsSegment findDds() throws RadixSvnException {
        RepositoryNode node = findChildByName(sourceRevision, "dds");
        if (node instanceof RepositoryDdsSegment) {
            return (RepositoryDdsSegment) node;
        }
        return null;
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryLayer(parent, repository, sourceRevision, name);
    }

    public boolean linkWithLayer(Layer layer) throws RadixSvnException {
        this.layer = layer;

        final RepositoryAdsSegment ads = findAds();
        if (ads != null) {
            ads.linkWithSegment(layer.getAds());
        }

        final RepositoryDdsSegment dds = findDds();
        if (dds != null) {
            dds.linkWithSegment(layer.getDds());
        }

        return true;
    }

    public void acceptBaseLayer(RepositoryLayer layer) {
        //  Directory xDef = layer.findDirectoryXml();
    }
}
