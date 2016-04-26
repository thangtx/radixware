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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.filters.RadContextFilter;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;

/**
 * Explorer item to represent {@link RadSelectorPresentationDef selector presentation} in explorer tree.
 */
public class RadSelectorExplorerItemDef extends RadExplorerItemDef {

    private final Id targetClassId;
    private final Id selectorPresentationId;
    private final Restrictions restrictions;

    /**
     * Explorer item constructor
     * @param id  explorer item identifier.
     * @param titleOwnerId  identifier of definition which is owner of this explorer item
     * @param titleId identifier of explorer item`s title to display in explorer tree.
     * (it may be {@link RadParagraphDef paragraph} or {@link RadEditorPresentationDef editor presentation} )
     * @param targetClassId identifier of {@link RadClassPresentationDef class definition} which contains  {@link RadSelectorPresentationDef selector presentation}.
     * @param presentationId identifier of  {@link RadSelectorPresentationDef selector presentation}.
     * @param restrictionsMask explorer items restrictions in long representation.
     * @param enabledCommands  if this explorer item restricts some commands this array contains identifiers of
     * commands that can be executed in selector opened by this exporer item.
     * @param inheritanceMask binary mask for inherit icon, title, restrictions from model definition
     * @param visible true if this explorer item is visible in tree
     */
    public RadSelectorExplorerItemDef(
            final Id id,
            final Id titleOwnerId,
            final Id titleId,
            final Id targetClassId,
            final Id presentationId,
            final long restrictionsMask,
            final Id[] enabledCommands,
            final long inheritanceMask,
            final boolean visible) {
        super(id, titleOwnerId, titleId, inheritanceMask, visible);
        this.targetClassId = targetClassId;
        selectorPresentationId = presentationId;
        if (this.inheritanceMask.isRestrictionsInherited()) {
            restrictions = Restrictions.NO_RESTRICTIONS;
        } else {
            final List<Id> enabledCmds = enabledCommands != null ? Arrays.asList(enabledCommands) : null;
            restrictions = Restrictions.Factory.newInstance(ERestriction.fromBitField(restrictionsMask), enabledCmds);
        }
    }

    @Override
    public RadSelectorPresentationDef getModelDefinition() {
        return getDefManager().getSelectorPresentationDef(selectorPresentationId);
    }

    @Override
    public Id getModelDefinitionId() {
        return selectorPresentationId;
    }

    @Override
    public Restrictions getRestrictions() {
        return restrictions;
    }

    @Override
    public Id getModelDefinitionClassId() {
        return targetClassId;
    }
    
    public RadContextFilter getContextFilter(){
        return null;
    }
    
    public RadSortingDef getInitialSorting(){
        return null;
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "explorer Item of table type %s for open selector presentation #%s in class #%s");
        return String.format(desc, toString(), selectorPresentationId, targetClassId);
    }
}
