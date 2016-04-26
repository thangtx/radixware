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

import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;


public abstract class DdsPlSqlPartDef extends DdsDefinition {

    protected static class DdsPlSqlPartItems extends DdsDefinitions<DdsPlSqlObjectItemDef> {

        public DdsPlSqlPartItems(DdsPlSqlPartDef owner) {
            super(owner);
        }
    }

    protected DdsPlSqlPartDef(DdsPlSqlObjectDef plSqlObjectDef, final String name) {
        super(plSqlObjectDef.getId(), name);
        setContainer(plSqlObjectDef);
    }

    public DdsPlSqlObjectDef getPlSqlObjectDef() {
        return (DdsPlSqlObjectDef) getContainer();
    }

    public abstract DdsDefinitions<DdsPlSqlObjectItemDef> getItems();

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.getItems().visit(visitor, provider);
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }
}
