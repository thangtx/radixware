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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.types.Id;


public class AdsReturnObject extends AdsBaseObject {
    
    protected AdsReturnObject() {
        this(ObjectFactory.createNodeId(), "");
    }

    protected AdsReturnObject(Id id, String name) {
        super(Kind.RETURN, id, name);
        pins.add(new AdsPin());
    }
    
    protected AdsReturnObject(final AdsReturnObject node) {
        super(node);
    }

    protected AdsReturnObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
    }

    @Override
    public boolean isSourcePin(AdsPin pin) {
        return false;
    }

    @Override
    public boolean isTargetPin(AdsPin pin) {
        return pins.contains(pin);
    }

    public void syncOwnerPins(AdsPage ownerPage) {
        assert ownerPage != null;
        RadixObject owner = ownerPage.getContainer();
        if (owner instanceof AdsScopeObject)
            ((AdsScopeObject)owner).sync();
        if (owner instanceof AdsCatchObject)
            ((AdsCatchObject)owner).sync();
    }

    public void syncOwnerPins() {
        syncOwnerPins(getOwnerPage());
    }

    private static final String TYPE_TITLE = "Return Node";
    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
}