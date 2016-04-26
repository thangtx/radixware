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
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

/**
 * Base class for explorer item definitions.
 * Derived classes used by explorer to represent {@link IModelDefinition model definition} in
 * explorer tree.
 */
public abstract class RadExplorerItemDef extends TitledDefinition {

    //Inheritance bits
    static protected final class ExplorerItemInheritance {

        static private final int ICON = 0x10;
        static private final int RESTRICTION = 0x20;
        static private final int TITLE = 0x80;
        private final long mask;

        public ExplorerItemInheritance(final long inheritanceMask) {
            mask = inheritanceMask;
        }

        public boolean isIconInherited() {
            return (mask & ICON) != 0;
        }

        public boolean isRestrictionsInherited() {
            return (mask & RESTRICTION) != 0;
        }

        public boolean isTitleInherited() {
            return (mask & TITLE) != 0;
        }
    }
    private final boolean isVisibleInTree;
    protected final ExplorerItemInheritance inheritanceMask;

    /**
     * Explorer item constructor
     * @param id  explorer item identifier.
     * @param titleId identifier of explorer item`s title to display in explorer tree.
     * @param titleOwnerId  identifier of definition which is owner of this explorer item
     * (it may be {@link RadParagraphDef paragraph} or {@link RadEditorPresentationDef editor presentation} )
     * @param inheritanceMask binary mask for inherit icon, title, restrictions from model definition
     * @param visible true if this explorer item is visible in tree
     */
    public RadExplorerItemDef(
            final Id id,
            final Id titleOwnerId,
            final Id titleId,
            final long inheritanceMask,
            final boolean visible) {
        super(id, null, titleOwnerId, titleId);
        this.inheritanceMask = new ExplorerItemInheritance(inheritanceMask);
        isVisibleInTree = visible;
    }

    /**
     * Returns {@link IModelDefinition model definition} for this explorer item.
     * @return model definition for this explorer item.
     */
    public abstract IModelDefinition getModelDefinition();

    /**
     * Returns identifier of {@link IModelDefinition model definition} for this explorer item.
     * @return model definition identifier.
     */
    public abstract Id getModelDefinitionId();

    /**
     * Returns identifier of class definition which contains {@link IModelDefinition model definition} for this explorer item.
     * @returns identifier of class which contains model definition.
     */
    public abstract Id getModelDefinitionClassId();

    /**
     * Returns restrictions defined in this explorer item.
     * This restrictions will be added to {@link IModelDefinition model definition} restrictions.
     * @return restrictions defined in this explorer item.
     * @see IModelDefinition#getRestrictions()
     */
    public abstract Restrictions getRestrictions();

    /**
     * Returns true if this explorer item is shown in tree; otherwise returns false.
     * @return true if this explorer item is shown in tree.
     */
    public boolean isVisible() {
        return isVisibleInTree;
    }

    public boolean isValid() {
        try {
            return getModelDefinition() != null;
        } catch (DefinitionError err) {
            return false;
        }
    }

    @Override
    public String getTitle() {
        if (inheritanceMask.isTitleInherited()) {
            return getModelDefinition().getTitle();
        }
        return super.getTitle();
    }

    /**
     * Returns explorer item`s qt-icon.<p>
     * Icon can be definied in this explorer item or inherited from {@link IModelDefinition model definition}.<p>
     * If no icon defined for this explorer item method returns null.
     * If icon defined but cannot be load method puts error message in trace.
     * @return explorer item`s qt-icon.
     */
    public Icon getIcon() {
        try {
            return getModelDefinition().getIcon();
        } catch (DefinitionError err) {
            final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get icon for explorer item %s");
            getApplication().getTracer().error(String.format(mess, toString()), err);
            return null;
        }
    }
}
