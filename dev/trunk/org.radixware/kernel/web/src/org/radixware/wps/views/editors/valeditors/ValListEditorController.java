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

package org.radixware.wps.views.editors.valeditors;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.wps.rwt.DropDownListDelegate;
import org.radixware.wps.rwt.DropDownListDelegate.DropDownListItem;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.ValueController;


public class ValListEditorController<T> extends InputBoxController<T, EditMaskList> {

    private final class DropDownListDelegateImpl extends DropDownListDelegate<T> {

        private List<DropDownListItem<T>> listBoxItems;

        public DropDownListDelegateImpl(final IClientEnvironment env, final EditMaskList editMaskList) {
            updateItems(env, editMaskList);
        }

        @Override
        protected List<DropDownListItem<T>> getItems() {
            return listBoxItems;
        }

        @SuppressWarnings("unchecked")
        public void updateItems(final IClientEnvironment env, final EditMaskList editMaskList) {
            listBoxItems = new LinkedList<>();
            final List<EditMaskList.Item> items = editMaskList.getItems();
            final InputBox.DisplayController<T> displayController = getInputBox().getDisplayController();
            for (EditMaskList.Item item : items) {
                final String title = displayController.getDisplayValue((T)item.getValue(), false, false);
                listBoxItems.add(new DropDownListItem<>(title, (T)item.getValue()));
            }
        }
    }

    public ValListEditorController(final IClientEnvironment env, final EditMaskList editMask) {
        super(env);
        setEditMask(editMask);
    }

    public ValListEditorController(final IClientEnvironment env) {
        this(env, new EditMaskList());
    }
    
    private DropDownListDelegateImpl dropDownDelegate;

    @Override
    @SuppressWarnings("unchecked")
    protected void applyEditMask(final InputBox box) {
        if (dropDownDelegate == null) {
            dropDownDelegate = new DropDownListDelegateImpl(getEnvironment(), getEditMask());
            box.addDropDownDelegate(dropDownDelegate);
        } else {
            dropDownDelegate.updateItems(getEnvironment(), getEditMask());
            box.updataButtonsState();
        }
        super.applyEditMask(box);
    }

    @Override
    protected ValueController<T> createValueController() {
        return null;//keyboard input is not allowed
    }
}