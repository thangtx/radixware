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

package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;

/**
 * Audit Mask
 * See rx_audit.doc.
 */
class DdsAuditMask {

    private final RadixObject owner;
    private boolean insert = false;
    private boolean update = false;
    private boolean delete = false;

    public DdsAuditMask(RadixObject owner) {
        super();
        this.owner = owner;

    }

    public boolean isEnabled() {
        return isInsert() || isUpdate() || isDelete();
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        if (this.insert != insert) {
            this.insert = insert;
            owner.setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        if (this.update != update) {
            this.update = update;
            owner.setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        if (this.delete != delete) {
            this.delete = delete;
            owner.setEditState(EEditState.MODIFIED);
        }
    }

    public long toLong() {
        int auditMask = 0;
        if (isInsert()) {
            auditMask |= 1;
        }
        if (isUpdate()) {
            auditMask |= 2;
        }
        if (isDelete()) {
            auditMask |= 4;
        }
        return auditMask;
    }

    public void loadFrom(long bitMask) {
        this.insert = ((bitMask & 1) != 0);
        this.update = ((bitMask & 2) != 0);
        this.delete = ((bitMask & 4) != 0);
    }
}
