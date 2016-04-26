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
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.rwt.DropDownListDelegate;
import org.radixware.wps.rwt.DropDownListDelegate.DropDownListItem;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.ValueController;


public class ValEnumEditorController<T extends IKernelEnum> extends InputBoxController<T, EditMaskConstSet> {

    private final class DropDownEnumListDelegate<T extends IKernelEnum> extends DropDownListDelegate<T> {

        private List<DropDownListItem<T>> listBoxItems;

        public DropDownEnumListDelegate(final IClientEnvironment env, final EditMaskConstSet editMaskConstSet) {
            updateItems(env, editMaskConstSet);
        }

        @SuppressWarnings("unchecked")
        public void updateItems(final IClientEnvironment env, final EditMaskConstSet editMaskConstSet) {
            final EditMaskConstSet.DisplayMode displayMode = editMaskConstSet.getDisplayMode();
            final RadEnumPresentationDef.Items enumItems = editMaskConstSet.getItems(env.getApplication());
            listBoxItems = new LinkedList<>();
            for (RadEnumPresentationDef.Item enumItem : enumItems) {
                final String title = enumItem.getTitle();
                final boolean hasTitle = title != null && !title.isEmpty();
                final String listItemTitle =
                        displayMode == EditMaskConstSet.DisplayMode.SHOW_TITLE && hasTitle ? title : enumItem.getName();
                T enumValue = (T)enumItem.getConstant();
                listBoxItems.add(new DropDownListItem(listItemTitle, enumValue, enumItem.getIcon()));
            }
        }

        @Override
        protected List<DropDownListItem<T>> getItems() {
            return listBoxItems;
        }
    }
    private DropDownEnumListDelegate<T> dropDownDelegate;

    public ValEnumEditorController(final IClientEnvironment env) {
        this(env, null);
    }

    public ValEnumEditorController(final IClientEnvironment env, Id enumerationId) {
        super(env);
        if (enumerationId != null) {
            setEditMask(new EditMaskConstSet(enumerationId));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void applyEditMask(final InputBox box) {
        if (dropDownDelegate == null) {
            dropDownDelegate = new DropDownEnumListDelegate<>(getEnvironment(), getEditMask());
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
