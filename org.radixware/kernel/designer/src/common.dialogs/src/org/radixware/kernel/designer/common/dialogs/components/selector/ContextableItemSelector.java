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

package org.radixware.kernel.designer.common.dialogs.components.selector;

import java.util.Collection;


public abstract class ContextableItemSelector<ItemType, ContextType> extends ItemSelector<ItemType> {

    private ContextType context;

    public ContextableItemSelector() {
    }

    protected ContextableItemSelector(ISelectorLayout layout) {
        super(layout);
    }

    public void open(ContextType context, ItemType item) {
        this.context = context;
        open(item);
    }
    
    public void open(ContextType context, Collection<ItemType> item) {
        this.context = context;
        open(item);
    }
    
    public ContextType getContext() {
        return context;
    }
}
