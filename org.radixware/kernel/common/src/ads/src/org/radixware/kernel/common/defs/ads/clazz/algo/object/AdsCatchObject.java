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

package org.radixware.kernel.common.defs.ads.clazz.algo.object;

import java.awt.Point;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.AdsExceptionClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.utils.Utils;


public class AdsCatchObject extends AdsBaseObject {

    private Id exceptionId = null;
    private AdsPage page = null;

    protected AdsCatchObject() {
        this(ObjectFactory.createNodeId(), DEFAULT_NAME);
    }

    protected AdsCatchObject(Id id, String name) {
        super(Kind.CATCH, id, name);

        page = new AdsPage(this);
        page.add(ObjectFactory.createNode(Kind.START, null, new Point(300, 10)));
        AdsReturnObject stop = (AdsReturnObject) ObjectFactory.createNode(Kind.RETURN, null, new Point(300, 700));
        page.add(stop);

        AdsPin leave = new AdsPin();
        leave.setOrigId(stop);
        pins.add(leave); // leave
    }

    protected AdsCatchObject(final AdsCatchObject node) {
        super(node);
        exceptionId = node.exceptionId;
        page = new AdsPage(node.getPage(), this);
        sync();
    }

    protected AdsCatchObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
        exceptionId = xNode.getExceptionId();
        page = new AdsPage(xNode.getPage(), this);
    }

    @Override
    public boolean setName(String name) {
        page.setName(name);
        return super.setName(name);
    }

    @Override
    public boolean isSourcePin(AdsPin pin) {
        return pins.contains(pin);
    }

    @Override
    public boolean isTargetPin(AdsPin pin) {
        return false;
    }

    @Override
    public final void sync() {
        syncOrigPins(page, false);
    }

    public AdsPage getPage() {
        return page;
    }

    public AdsExceptionClassDef getExceptionDef() {
        if (exceptionId == null) {
            return null;
        }
        try {
            return (AdsExceptionClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(getOwnerDefinition()).findById(exceptionId).get();
        } catch (Exception e) {
            return null;
        }
    }

    public void setExceptionDef(AdsExceptionClassDef exceptionDef) {
        if (!Utils.equals(getExceptionDef(), exceptionDef)) {
            exceptionId = (exceptionDef == null) ? null : exceptionDef.getId();
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        AdsExceptionClassDef exDef = getExceptionDef();
        if (exDef != null) {
            list.add(exDef);
        }
    }

    @Override
    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode, ESaveMode saveMode) {
        super.appendTo(xNode, saveMode);
        xNode.setExceptionId(exceptionId);
        page.appendTo(xNode.addNewPage(), saveMode);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        page.visit(visitor, provider);
    }
}
