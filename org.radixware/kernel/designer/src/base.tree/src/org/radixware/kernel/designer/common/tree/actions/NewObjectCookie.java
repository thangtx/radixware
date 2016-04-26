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

package org.radixware.kernel.designer.common.tree.actions;

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;

/**
 * Cookie interface for node that supports operation "New..." in context menu or enables 
 * "New Definition" button on toolbar and it's  global menu item
 */
public class NewObjectCookie implements Node.Cookie {

    private static final ICreatureGroup[] NONE = new ICreatureGroup[0];
    private final RadixObject radixObject;
    private CreationSupport creationSupport;

    public NewObjectCookie(RadixObject radixObject, CreationSupport support) {
        this.radixObject = radixObject;
        this.creationSupport = support;
    }

    /**
     * Returns an array of classes of radix creation Radproviders associated with the node
     */
    public ICreatureGroup[] getCreatureGroups() {
        ICreatureGroup[] creatureGroups = creationSupport.createCreatureGroups(radixObject);
        if (creatureGroups != null) {
            return creatureGroups;
        } else {
            return NONE;
        }
    }

    public boolean isEnabled() {
        ICreatureGroup[] creatureGroups = creationSupport.createCreatureGroups(radixObject);
        if (creatureGroups != null && creatureGroups.length > 0) {
            return true;
        } else {
            return false;
        }
    }
}