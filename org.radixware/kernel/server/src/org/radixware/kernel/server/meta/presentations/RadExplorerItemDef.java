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
package org.radixware.kernel.server.meta.presentations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.common.meta.RadDefinition;
import org.radixware.kernel.server.types.Restrictions;

public abstract class RadExplorerItemDef extends RadDefinition {

    private Release release = null;
    private final Id ownerDefId;

    public RadExplorerItemDef(
            final Id id,
            final Id ownerDefId) {
        super(id);
        this.ownerDefId = ownerDefId;
    }

    public Id getOwnerDefId() {
        return ownerDefId;
    }

    protected void link(final Release release) {
        this.release = release;
    }

    protected Release getRelease() {
        return release;
    }

    public abstract Restrictions getRestrictions();

    @Deprecated
    public abstract boolean isCommandEnabled(final Id cmdId);
    
    public abstract boolean isCommandEnabled(final Id cmdId, final boolean isReadOnlyCommand);

    public abstract DdsReferenceDef getContextReference();

    static final RadExplorerItemDef findEiById(final List<RadExplorerItemDef> eiList, final Id eiId, final List<RadExplorerItemDef> processedParags) {
        for (RadExplorerItemDef ei : eiList) {
            if (ei.getId().equals(eiId)) {
                return ei;
            }
            if (ei instanceof RadParagraphExplorerItemDef) {
                if (processedParags.contains(ei)) {
                    return null;
                }
                processedParags.add(ei);
                final RadExplorerItemDef tmp = findEiById(((RadParagraphExplorerItemDef) ei).getChildren(), eiId, processedParags);
                if (tmp != null) {
                    return tmp;
                }
            }
        }
        return null;
    }

    static final List<RadExplorerItemDef> addInherited(List<RadExplorerItemDef> children, RadParagraphExplorerItemDef paragraph, Id[] explicitItems) {
        List<RadExplorerItemDef> result = null;
        if (explicitItems != null && explicitItems.length > 0) {
            nextId:
            for (Id eiId : explicitItems) {
                RadParagraphExplorerItemDef par = paragraph;
                while (par != null) {
                    for (RadExplorerItemDef item : par.getChildren()) {
                        if (item.getId() == eiId) {
                            if (result == null) {
                                result = new LinkedList<>();
                                result.addAll(children);
                            }
                            result.add(item);
                            continue nextId;
                        }
                    }
                    par = par.getBase();
                }
            }
        }
        return result == null ? children : result;
    }

   

    static public final class CInheritance {

        static public final int RESTRICTION = 32;
        static public final int ICON = 16;
        static public final int TITLE = 128;
    }
}
