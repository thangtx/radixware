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
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.types.Id;


public class AdsScopeObject extends AdsBaseObject {

    private AdsPage page = null;

    protected AdsScopeObject() {
        this(ObjectFactory.createNodeId(), DEFAULT_NAME);
    }

    protected AdsScopeObject(Id id, String name) {
        super(Kind.SCOPE, id, name);

        page = new AdsPage(this);

        AdsStartObject start = (AdsStartObject) ObjectFactory.createNode(Kind.START, null, new Point(300, 10));
        page.add(start);

        AdsPin entry = new AdsPin();
        entry.setOrigId(start);
        pins.add(entry);

        AdsReturnObject stop = (AdsReturnObject) ObjectFactory.createNode(Kind.RETURN, null, new Point(300, 700));
        page.add(stop);

        AdsPin leave = new AdsPin();
        leave.setOrigId(stop);
        pins.add(leave);
    }

    protected AdsScopeObject(final AdsScopeObject node) {
        super(node);
        page = new AdsPage(node.getPage(), this);
        sync();
    }

    protected AdsScopeObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
        page = new AdsPage(xNode.getPage(), this);
    }

    @Override
    public boolean setName(String name) {
        page.setName(name);
        return super.setName(name);
    }

    @Override
    public boolean isSourcePin(AdsPin pin) {
        return pins.indexOf(pin) > 0;
    }

    @Override
    public boolean isTargetPin(AdsPin pin) {
        return pins.indexOf(pin) == 0;
    }

    @Override
    public final void sync() {
        syncOrigPins(page, true);
    }

    public AdsPage getPage() {
        return page;
    }

    @Override
    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode, ESaveMode saveMode) {
        super.appendTo(xNode, saveMode);
        page.appendTo(xNode.addNewPage(), saveMode);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        page.visit(visitor, provider);
    }

    private static final String TYPE_TITLE = "Scope Node";
    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
}
