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

package org.radixware.wps.rwt;

import java.util.LinkedList;
import java.util.List;


public class RadioButton extends CheckBox {

    public RadioButton() {
        setInputType("radio");
    }

    public static final class Group {

        private List<RadioButton> items = new LinkedList<>();
        private final CheckBox.SelectionStateListener selectionListener = new SelectionStateListener() {
            @Override
            public void onSelectionChange(CheckBox cb) {
            }
        };

        private void addMember(RadioButton b) {
            if (!items.contains(b)) {
                items.add(b);
                b.addSelectionStateListener(selectionListener);
            }
        }

        private void removeMember(RadioButton b) {
            items.remove(b);
            b.removeSelectionStateListener(selectionListener);
        }

        private void select(RadioButton button) {
            for (RadioButton item : items) {
                if (item == button) {
                    item.superSetSelected(true);
                } else {
                    item.superSetSelected(false);
                }
            }
        }
    }
    private Group group;

    public void setGroup(Group group) {
        if (this.group != null) {
            this.group.removeMember(this);
        }
        this.group = group;
        if (this.group != null) {
            this.group.addMember(this);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        if (this.group != null) {
            if (selected) {
                this.group.select(this);
            }
        } else {
            superSetSelected(selected);
        }

    }

    private void superSetSelected(boolean selected) {
        super.setSelected(selected);
    }
}
