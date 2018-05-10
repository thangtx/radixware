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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.types.Restrictions;

public final class RadParentRefExplorerItemDef extends RadExplorerItemDef {

    private final Id parentReferenceId;
    private final Id parentClassId;
    private final List<Id> parentPresentationIds;
    private final Restrictions restrictions;
    private RadClassDef parentClassDef = null;
    private List<RadEditorPresentationDef> parentPresentations = null;
    private DdsReferenceDef ref = null;

    public RadParentRefExplorerItemDef(
            final Id id,
            final Id ownerDefId,
            final Id parentReferenceId,
            final Id parentClassId,
            final Id[] parentPresentationIds,
            final Restrictions restrictions) {
        super(id, ownerDefId);
        this.restrictions = restrictions != null ? restrictions : Restrictions.ZERO;
        this.parentReferenceId = parentReferenceId;
        this.parentClassId = parentClassId;
        if (parentPresentationIds != null && parentPresentationIds.length != 0) {
            this.parentPresentationIds = Collections.unmodifiableList(Arrays.asList(parentPresentationIds));
        } else {
            this.parentPresentationIds = Collections.emptyList();
        }
    }

    @Override
    public void link() {
        super.link();
        getParentClassDef();
        getParentPresentations();
        getContextReference();
    }

    @Override
    @Deprecated
    public final boolean isCommandEnabled(final Id cmdId) {
        return !restrictions.getIsCommandRestricted(cmdId);
    }
    
    @Override
    public final boolean isCommandEnabled(final Id cmdId, final boolean isReadOnlyCommand) {
        return !restrictions.getIsCommandRestricted(cmdId,isReadOnlyCommand);
    }

    public RadClassDef getParentClassDef() {
        if (parentClassDef == null) {
            parentClassDef = getRelease().getClassDef(parentClassId);
        }
        return parentClassDef;
    }

    public final List<RadEditorPresentationDef> getParentPresentations() {
        if (parentPresentations == null) {
            parentPresentations = new ArrayList<>(parentPresentationIds.size());
            final RadClassDef classDef = getParentClassDef();
            for (Id parentPresId : parentPresentationIds) {
                try {
                    parentPresentations.add(classDef.getPresentation().getEditorPresentationById(parentPresId));
                } catch (DefinitionNotFoundError e) {
                    //RADIX-4918
                }
            }
            parentPresentations = Collections.unmodifiableList(parentPresentations);
        }
        return parentPresentations;
    }

    /**
     * @return the parentReferenceId
     */
    public Id getParentReferenceId() {
        return parentReferenceId;
    }

    /**
     * @return the restrictions
     */
    @Override
    public Restrictions getRestrictions() {
        return restrictions;
    }

    @Override
    public DdsReferenceDef getContextReference() {
        if (ref == null) {
            ref = getRelease().getReferenceDef(getParentReferenceId());
        }
        return ref;
    }
}
