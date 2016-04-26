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

package org.radixware.kernel.designer.ads.editors.clazz.members.transparent;

import java.util.List;
import javax.swing.Icon;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector.IItemInfo;


final class AddAllItem extends ClassMemberItem {

    private final List<ClassMemberItem> items;
    private final MultipleListItemSelector.IItemInfo info;

    public AddAllItem(List<ClassMemberItem> items) {
        super(null, false);
        this.items = items;

        info = new Info();
    }

    @Override
    public void switchSelection() {
        boolean selected = isSelected();

        for (ClassMemberItem item : items) {
            if (item.isSelected() == selected) {
                if (item != this) {
                    item.switchSelection();
                }
                else {
                    super.switchSelection();
                }
            }
        }
    }

    @Override
    public EAccess getAccess() {
        return null;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public EClassMemberType getClassMemberType() {
        return null;
    }

    @Override
    public IItemInfo getInfo() {
        return info;
    }

    @Override
    public boolean isAbstract() {
        return false;
    }

    @Override
    public void afterSwitchSelection() {
        boolean selected = isSelected();
        for (ClassMemberItem item : items) {
            if (item == this){
                continue;
            }
            if (item.isSelected() == false) {
                if (selected == true){
                    super.switchSelection();
                    return;
                } else {
                    return;
                }
            }
        }
        //All selected
        if (!selected){
            super.switchSelection();
        }
    }

    private final class Info implements MultipleListItemSelector.IItemInfo {

        @Override
        public String getShortName() {
            return getName();
        }

        @Override
        public String getHtmlName() {
            return getName();
        }

        @Override
        public String getName() {
            return "Select All";
        }

        @Override
        public Icon getIcon() {
            return null;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
