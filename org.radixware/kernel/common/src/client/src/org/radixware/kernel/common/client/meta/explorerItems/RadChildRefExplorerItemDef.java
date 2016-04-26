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

import org.radixware.kernel.common.types.Id;

/**
 * Explorer item to represent {@link RadSelectorPresentationDef selector presentation} when it opens
 * by child reference.
 */
public final class RadChildRefExplorerItemDef extends RadSelectorExplorerItemDef {

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
    public RadChildRefExplorerItemDef(
            final Id id,
            final Id titleOwnerId,
            final Id titleId,
            final Id targetClassId,
            final Id presentationId,
            final long restrictionsMask,
            final Id[] enabledCommands,
            final long inheritanceMask,
            final boolean visible) {
        super(id, titleOwnerId, titleId, targetClassId, presentationId, restrictionsMask, enabledCommands, inheritanceMask, visible);
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "explorer Item of child table type %s for open selector presentation #%s in class #%s");
        return String.format(desc, super.getDescription(), getModelDefinitionId(), getModelDefinitionClassId());
    }
}
