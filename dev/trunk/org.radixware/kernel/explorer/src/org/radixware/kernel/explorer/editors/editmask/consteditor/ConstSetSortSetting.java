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

package org.radixware.kernel.explorer.editors.editmask.consteditor;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.schemas.editmask.EditMask;


public class ConstSetSortSetting extends QWidget implements IEditMaskEditorSetting {
    
    private final Signal1<EEditMaskEnumOrder> itemChanged = new Signal1<EEditMaskEnumOrder>();
    private final QComboBox cbChooseItemOrder;
    
    public ConstSetSortSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        final MessageProvider msgProvider = environment.getMessageProvider();
        
        final QLabel label = new QLabel(this);
        final String title = msgProvider.translate("EditMask", "Sort by:");
        label.setText(title);
        
        cbChooseItemOrder = new QComboBox(this);
        String itemText = msgProvider.translate("EditMask", "Order");
        cbChooseItemOrder.addItem(itemText, EEditMaskEnumOrder.BY_ORDER);
        itemText = msgProvider.translate("EditMask", "Value");
        cbChooseItemOrder.addItem(itemText, EEditMaskEnumOrder.BY_VALUE);
        itemText = msgProvider.translate("EditMask", "Description");
        cbChooseItemOrder.addItem(itemText, EEditMaskEnumOrder.BY_TITLE);
                
        cbChooseItemOrder.currentIndexChanged.connect((QWidget)this, "onItemChanged()");
        final QHBoxLayout layout = new QHBoxLayout();
        layout.addWidget(label);
        layout.addWidget(cbChooseItemOrder);
        this.setLayout(layout);
    }
    
    @Override
    public void addToXml(final EditMask editMask) {
        editMask.getEnum().setOrderBy(
                (EEditMaskEnumOrder)cbChooseItemOrder.itemData(cbChooseItemOrder.currentIndex())
        );
    }

    @Override
    public void loadFromXml(final EditMask editMask) {
        if(editMask.getEnum().isSetOrderBy()) {
            final int index = cbChooseItemOrder.findData(editMask.getEnum().getOrderBy());
            if(index >= 0) {
                cbChooseItemOrder.setCurrentIndex(index);
            } else {
                setDefaultValue();
            }
        }
    }
    
    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.CONST_SORT;
    }

    @Override
    public void setDefaultValue() {
        cbChooseItemOrder.setCurrentIndex(0);
    }

    @Override
    public Object getValue() {
        return cbChooseItemOrder.itemData(cbChooseItemOrder.currentIndex());
    }
    
    @SuppressWarnings("unused")
    private void onItemChanged() {
        final int index = cbChooseItemOrder.currentIndex();
        final EEditMaskEnumOrder currentOrder = (EEditMaskEnumOrder) cbChooseItemOrder.itemData(index);
        itemChanged.emit(currentOrder);
    }
    
    public Signal1<EEditMaskEnumOrder> itemChanged() {
        return itemChanged;
    }
}
