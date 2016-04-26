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

package org.radixware.kernel.explorer.editors.profiling.prefix;

import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QTreeWidgetItem;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.defs.value.ValAsStr;


public class PrefixTreeItem extends QTreeWidgetItem {

    private final RadEnumPresentationDef.Item enumItem;
    private boolean checked;

    public PrefixTreeItem(final RadEnumPresentationDef.Item enumItem) {
        this.enumItem = enumItem;
        this.setText(0, enumItem.getTitle());
        setChecked(false);
    }

    public boolean isChecked() {
        return checked;
    }

    private void setChecked(final boolean isChecked) {
        this.checked = isChecked;
        final CheckState checkState = isChecked ? CheckState.Checked : CheckState.Unchecked;
        setCheckState(0, checkState);
    }

    public final void changeState(final boolean isChecked) {
        setChecked(isChecked);
        setChildsEnabled();
        changeFont();
    }

    private void changeFont() {
        this.setBoldFont(checked);
        PrefixTreeItem item = (PrefixTreeItem) this.parent();
        while ((item != null) && isNeedChangeFont(item)) {
            item.setBoldFont(checked);
            item = (PrefixTreeItem) item.parent();
        }
    }

    private boolean isNeedChangeFont(final PrefixTreeItem parent) {
        for (int i = 0; i < parent.childCount(); i++) {
            final PrefixTreeItem childItem = (PrefixTreeItem) parent.child(i);
            if (!childItem.equals(this) && (childItem.isChecked() || !isNeedChangeFont(childItem))) {
                return false;
            }
        }
        return true;
    }

    private void setBoldFont(final boolean bold) {
        final QFont font = font(0);
        font.setBold(bold);
        setFont(0, font);
    }

    private void setChildsEnabled() {
        for (int i = 0; i < childCount(); i++) {
            final PrefixTreeItem childItem = (PrefixTreeItem) child(i);
            childItem.setChecked(checked);
            childItem.setDisabled(checked);
            childItem.setBoldFont(false);
            childItem.setChildsEnabled();
        }
    }

    public String getValue() {
        return ValAsStr.toStr(enumItem.getValue(), enumItem.getValType());
    }
    //public String getName(){
    //    return enumItem.getValue().toString();
    // }
}
