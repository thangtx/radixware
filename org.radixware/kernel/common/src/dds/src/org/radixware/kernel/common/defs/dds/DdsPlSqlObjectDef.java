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

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;

/**
 * Метаинформация о PL/SQL объекте.
 */
public abstract class DdsPlSqlObjectDef extends DdsDefinition implements IDdsDbDefinition {

    private class PurityLevel extends DdsPurityLevel {

        public PurityLevel() {
        }

        @Override
        protected void onChanged() {
            DdsPlSqlObjectDef.this.setEditState(EEditState.MODIFIED);
        }
    }

    protected DdsPlSqlObjectDef(EDefinitionIdPrefix idPrefix, final String name) {
        super(idPrefix, name);
    }

    protected DdsPlSqlObjectDef(org.radixware.schemas.ddsdef.PlSqlObject xPlSqlObject) {
        super(xPlSqlObject);

        this.purityLevel.loadFromBitMask(xPlSqlObject.getPurityLevelMask());
        if (xPlSqlObject.isSetHeader()) {
            header.loadFrom(xPlSqlObject.getHeader());
        }
        if (xPlSqlObject.isSetBody()) {
            body.loadFrom(xPlSqlObject.getBody());
        }
        if (xPlSqlObject.isSetDeprecated()) {
            this.isDeprecated = xPlSqlObject.getDeprecated();
        }
    }
    private boolean isDeprecated = false;

    @Override
    public boolean isDeprecated() {
        return isDeprecated || super.isDeprecated();
    }

    public void setDeprecated(boolean isDeprecated) {
        if (this.isDeprecated != isDeprecated) {
            this.isDeprecated = isDeprecated;
            fireNameChange();
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public String getDbName() {
        return getName();
    }
    private final DdsPurityLevel purityLevel = new PurityLevel();

    public DdsPurityLevel getPurityLevel() {
        return purityLevel;
    }
    private final DdsPlSqlHeaderDef header = new DdsPlSqlHeaderDef(this);
    private final DdsPlSqlBodyDef body = new DdsPlSqlBodyDef(this);

    public DdsPlSqlHeaderDef getHeader() {
        return header;
    }

    public DdsPlSqlBodyDef getBody() {
        return body;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.getHeader().visit(visitor, provider);
        this.getBody().visit(visitor, provider);
    }

    @Override
    public boolean isGeneratedInDb() {
        return true;
    }

    protected abstract class DdsPlSqlObjectClipboardSupport<T extends DdsPlSqlObjectDef> extends DdsClipboardSupport<T> {

        public DdsPlSqlObjectClipboardSupport(T plSqlObject) {
            super(plSqlObject);
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            return getBody().getItems().canPaste(objectsInClipboard, resolver);
        }

        @Override
        public void paste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            checkForCanPaste(objectsInClipboard, resolver);

            for (Transfer objectInClipboard : objectsInClipboard) {
                List<Transfer> objectInClipboardAsList = Collections.singletonList(objectInClipboard);

                if (objectInClipboard.getObject() instanceof DdsPrototypeDef) {
                    getHeader().getItems().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                } else {
                    getBody().getItems().getClipboardSupport().paste(objectInClipboardAsList, resolver);
                }
            }
        }
    }

    public ISqmlEnvironment getSqmlEnvironment() {
        return new DdsSqmlEnvironment(this);
    }
}
