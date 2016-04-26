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

package org.radixware.kernel.common.client.meta.explorerItems;

import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;


/**
 * Explorer item to represent {@link RadEditorPresentationDef editor presentation} of
 * entity inserted in explorer tree from {@link org.radixware.kernel.explorer.views.Selector selector}.
 * <p>Explorer item of this type does not linked with {@link IModelDefinition} directly. Explorer
 * recives identifier of actual {@link RadEditorPresentationDef editor presentation} from server.
 * So method {@link  #getModelDefinition()} will always thows an {@link AbstractMethodError} exception.
 */
public final class RadParentRefExplorerItemDef extends RadExplorerItemDef {

    private final Restrictions restrictions;

    /**
     * Explorer item constructor
     * @param id  explorer item identifier.
     * @param titleOwnerId  identifier of {@link RadEditorPresentationDef editor presentation} which is owner of this explorer item
     * @param inheritanceMask binary mask for inherit icon, title, restrictions from model definition
     * @param restrictionsMask explorer items restrictions in long representation.
     * @param visible true if this explorer item is visible in tree
     */
    public RadParentRefExplorerItemDef(
            final Id id,
            final Id titleOwnerId,
            final Id titleId,
            final long inheritanceMask,
            final long restrictionsMask,
            final boolean visible) {
        super(id, titleOwnerId, titleId, inheritanceMask, visible);
        if (this.inheritanceMask.isRestrictionsInherited()) {
            restrictions = Restrictions.NO_RESTRICTIONS;
        } else {
            restrictions = Restrictions.Factory.newInstance(ERestriction.fromBitField(restrictionsMask),null);
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public IModelDefinition getModelDefinition() {
        throw new AbstractMethodError();
    }

    @Override
    public Id getModelDefinitionId() {
        return null;
    }

    @Override
    public Id getModelDefinitionClassId() {
        return null;
    }

    @Override
    public Restrictions getRestrictions() {
        return restrictions;
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "explorer Item of parent object type %s");
        return String.format(desc, super.getDescription());
    }
}
