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


public class AdsTermObject extends AdsBaseObject {
    
    protected AdsTermObject() {
        this(ObjectFactory.createNodeId(), EMPTY_NAME);
    }

    protected AdsTermObject(Id id, String name) {
        super(Kind.TERMINATE, id, EMPTY_NAME);
        pins.add(new AdsPin());
    }
    
    protected AdsTermObject(final AdsTermObject node) {
        super(node);
    }

    protected AdsTermObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
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

    private static final String TYPE_TITLE = "Term Node";
    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
}