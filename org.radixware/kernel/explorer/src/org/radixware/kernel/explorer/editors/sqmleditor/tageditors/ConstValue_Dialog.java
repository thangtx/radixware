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

package org.radixware.kernel.explorer.editors.sqmleditor.tageditors;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;


public class ConstValue_Dialog extends ExplorerDialog {

    private final QLineEdit edConstantSet = new QLineEdit(this);
    private final QComboBox cbConstantSetItem = new QComboBox(this);
    private final boolean isReadOnly;
    private final ISqmlEnumDef constSet;
    private ISqmlEnumItem constSetItem;

    public ConstValue_Dialog(final IClientEnvironment environment, final XscmlEditor editText, final ISqmlEnumItem constSetItem, final EDefinitionDisplayMode displayMode) {
        super(environment, editText, "Condition_Dialog_SqmlEditor");
        this.isReadOnly = editText.isReadOnly();
        constSet = environment.getSqmlDefinitions().findEnumById(constSetItem.getOwnerEnumId());
        this.constSetItem = constSetItem;

        this.setWindowTitle(getEnvironment().getMessageProvider().translate("SqmlEditor", "Constant Set Item"));//�������� ������� ���������
        edConstantSet.setObjectName("edConstSet");

        createUI(displayMode);
        setFixedSize(sizeHint().width() + 100, sizeHint().height());

        edConstantSet.setEnabled(false);
        edConstantSet.setText(constSet.getDisplayableText(displayMode));
    }

    private void createUI(final EDefinitionDisplayMode displayMode) {
        final QGridLayout gridLayout = new QGridLayout();
        final QLabel lbConstSet = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Constant set:"), this);
        final QLabel lbConstItem = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Constant set item:"), this);

        cbConstantSetItem.setObjectName("cbConstSetItems");
        fillConstSetList(displayMode);

        final EnumSet<EDialogButtonType> buttons;
        if (isReadOnly) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
            cbConstantSetItem.setEnabled(false);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL);
        }

        gridLayout.addWidget(lbConstSet, 0, 0);
        gridLayout.addWidget(edConstantSet, 0, 1);
        gridLayout.addWidget(lbConstItem, 1, 0);
        gridLayout.addWidget(cbConstantSetItem, 1, 1);
        dialogLayout().addLayout(gridLayout);
        addButtons(buttons, true);
    }

    private void fillConstSetList(final EDefinitionDisplayMode displayMode) {
        final List<ISqmlEnumItem> allItems = new ArrayList<>();
        for (ISqmlEnumItem item: constSet){
            allItems.add(item);
        }
        Collections.sort(allItems, new Comparator<ISqmlEnumItem>(){
            @Override
            public int compare(final ISqmlEnumItem item1, final ISqmlEnumItem item2) {
                return getConstSetItemText(item1, displayMode).compareTo(getConstSetItemText(item2, displayMode));
            }       
        
        });
        for (ISqmlEnumItem item: allItems){
            cbConstantSetItem.addItem(getConstSetItemText(item, displayMode),item);
            if (item.getId().equals(constSetItem.getId())) {
                cbConstantSetItem.setCurrentIndex(cbConstantSetItem.count() - 1);
            }
        }
    }

    @Override
    public void accept() {
        final int index = cbConstantSetItem.currentIndex();
        constSetItem = (ISqmlEnumItem) cbConstantSetItem.itemData(index);
        super.accept();
    }

    private static String getConstSetItemText(final ISqmlEnumItem constSetItem, final EDefinitionDisplayMode displayMode) {
        if (displayMode == EDefinitionDisplayMode.SHOW_FULL_NAMES) {
            return constSetItem.getShortName();
        }
        return constSetItem.getDisplayableText(displayMode);
    }

    public ISqmlEnumItem getConstSetItem() {
        return constSetItem;
    }
}