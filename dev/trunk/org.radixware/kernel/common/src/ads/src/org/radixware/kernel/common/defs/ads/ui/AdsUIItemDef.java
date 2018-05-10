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
package org.radixware.kernel.common.defs.ads.ui;

import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.types.Id;

public abstract class AdsUIItemDef extends AdsDefinition {

    protected final UiProperties properties = new UiProperties(this);
    protected final UiProperties attributes = new UiProperties(this);

    public AdsUIItemDef(Id id, String name) {
        super(id, name);
    }

    public UiProperties getProperties() {
        return properties;
    }

    public UiProperties getAttributes() {
        return attributes;
    }

    public AdsAbstractUIDef getOwnerUIDef() {
        return AdsUIUtil.getUiDef(this);
    }

    @Override
    public String getQualifiedName() {
        return super.getQualifiedName();
    }

    public abstract AdsUIItemDef findWidgetById(Id id);

    @Override
    public EAccess getAccessMode() {
        return EAccess.PUBLIC;
    }

    @Override
    public boolean needsDocumentation() {
        return false;
    }

    @Override
    public boolean isPublished() {
        AdsAbstractUIDef def = getOwnerUIDef();
        return def == null ? true : def.isPublished();
    }

    @Override
    public void setContainer(RadixObject container) {
        super.setContainer(container);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        properties.visit(visitor, provider);
        attributes.visit(visitor, provider);
    }
    private NodeUpdateSupport nodeUpdateSupport = null;

    public synchronized NodeUpdateSupport getNodeUpdateSupport() {
        if (nodeUpdateSupport == null) {
            nodeUpdateSupport = new NodeUpdateSupport();
        }
        return nodeUpdateSupport;
    }

    public void fireNodeUpdate() {
        if (nodeUpdateSupport != null) {
            nodeUpdateSupport.fireEvent(new NodeUpdateSupport.NodeUpdateEvent(this));
        }
    }

    public boolean isActionWidget() {
        return false;
    }
}
