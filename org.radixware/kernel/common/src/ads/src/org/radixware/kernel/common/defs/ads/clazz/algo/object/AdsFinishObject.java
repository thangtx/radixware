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

import org.radixware.kernel.common.types.Id;


public class AdsFinishObject extends AdsBaseObject {
    
    protected AdsFinishObject() {
        this(ObjectFactory.createNodeId(), EMPTY_NAME);
    }

    protected AdsFinishObject(Id id, String name) {
        super(Kind.FINISH, id, EMPTY_NAME);
        pins.add(new AdsPin());
    }

    protected AdsFinishObject(final AdsFinishObject node) {
        super(node);
    }
    
    protected AdsFinishObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
    }

    @Override
    public boolean isSourcePin(AdsPin pin) {
        return false;
    }

    @Override
    public boolean isTargetPin(AdsPin pin) {
        return this.pins.contains(pin);
    }
}