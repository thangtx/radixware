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
package org.radixware.kernel.designer.tree.ads.nodes.defs;

import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;

public abstract class AdsMixedNode<T extends RadixObject> extends AdsObjectNode<T> {

    protected final MixedNodeChildrenAdapter children;
    private IRadixEventListener accListener = null;
    public final MixedNodeChildrenAdapter parentAdapter;

    @SuppressWarnings("unchecked")
    protected AdsMixedNode(T definition) {
        this(definition, null);
    }

    @SuppressWarnings("unchecked")
    protected AdsMixedNode(T definition, MixedNodeChildrenAdapter parentAdapter) {
        super(definition, Children.LEAF);
        this.parentAdapter = parentAdapter;
        Class<? extends MixedNodeChildrenProvider<?>>[] cps = getChildrenProviders();
        if (cps != null) {
            children = new MixedNodeChildrenAdapter<T>(this, definition, cps, transitiveCreation());//NOPMD
            Index index = children.getIndex();
            if (index != null) {
                getLookupContent().add(index);
            }
        } else {
            children = null;
        }
        checkChildren();
    }

    public boolean noFilter() {
        return false;
    }

    final void checkChildren() {
        if (children != null && !children.isEmpty()) {
            if (!Utils.equals(getChildren(), children)) {
                setChildren(children);
            }
        } else {
            setChildren(Children.LEAF);
        }
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        return new CreationSupport() {
            @Override
            public ICreatureGroup[] createCreatureGroups(RadixObject object) {
                if (children != null) {
                    return children.createCreatureGroups();
                } else {
                    return null;
                }
            }
        };
    }

    protected abstract Class[] getChildrenProviders();

    protected boolean transitiveCreation() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getRadixObject() {
        return (T) super.getRadixObject();
    }
}
