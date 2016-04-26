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

package org.radixware.kernel.starter.meta;

import java.util.Arrays;


public class FileMeta {

    private final String name;
    private final String archive;
    private final byte[] digest;
    private final String groupType;
    private volatile FileMeta next;
    private final LayerMeta layer;

    public FileMeta(final String name, final LayerMeta layer, final byte[] digest, final String groupType) {
        this(name, layer, null, digest, groupType);
    }

    public FileMeta(final String name, final LayerMeta layer, final String archive, final byte[] digest, final String groupType) {
        this.name = name;
        this.archive = archive;
        this.layer = layer;
        this.digest = digest == null ? null : Arrays.copyOf(digest, digest.length);
        this.groupType = groupType;
        this.next = null;
    }

    public String getArchive() {
        return archive;
    }

    public String getName() {
        return name;
    }

    public String getGroupType() {
        return groupType;
    }

    public String getString() {
        return groupType;
    }

    public String getStore() {
        if (archive == null) {
            return name;
        }
        return archive;
    }

    public LayerMeta getLayer() {
        return layer;
    }

    public FileMeta getNext() {
        return next;
    }
    private final Object semAddNext = new Object();

    public void addNext(final FileMeta next) {
        synchronized (semAddNext) {
            if (this.next == null) {
                this.next = next;
            } else {
                this.next.addNext(next);
            }
        }
    }
    
    public byte[] getDigest() {
        return digest;
    }

    boolean hasSameDigest(final FileMeta fileMeta) {
        return Arrays.equals(digest, fileMeta.digest);
    }
}
