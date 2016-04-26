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

package org.radixware.kernel.designer.common.editors.editmask;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class EditMaskOwner extends RadixObject {

    private final EditOptions editOptions;
    private boolean isCustomReadOnly;

    protected EditMaskOwner(EditOptions editOptions) {
        this.editOptions = editOptions;
        setContainer(editOptions);
    }

    @Override
    public boolean isReadOnly() {
        return super.isReadOnly() || isCustomReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        isCustomReadOnly = isReadOnly;
    }

    @Override
    public RadixIcon getIcon() {
        return RadixWareIcons.EDIT.EDIT; // TODO : change to edit mask icon
    }

    public EditMask getDefaultEditMask() {
        return EditMask.Factory.newDefaultInstance(editOptions);
    }

    public EditMask getEditMask() {
        return editOptions.getEditMask();
    }

    public void createEditMask(EEditMaskType type) {
        editOptions.setEditMaskType(type);
    }

    public void removeEditMask() {
        editOptions.setEditMaskType(null);
    }

    @Override
    public AdsDefinition getDefinition() {
        return editOptions.getOwnerDefinition();
    }

    public static final class Factory {

        public static EditMaskOwner newInstance(EditOptions editOptions) {
            return new EditMaskOwner(editOptions);
        }
    }
}
