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

package org.radixware.kernel.explorer.editors.editmask.listeditor;

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;


public class ListEditMaskEditorWidget extends ExplorerWidget implements IEditMaskEditor {
    private final ListValuesTableSetting table;
    private final IClientEnvironment environment;
    private final EValType type;
    private final QVBoxLayout buttonBox = new QVBoxLayout();
    
    public ListEditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent, final EValType type) {
        super(environment,parent);
        this.environment = environment;
        this.type = type;
        table = new ListValuesTableSetting(environment, this, type);
        final QHBoxLayout outterLayout = new QHBoxLayout();
        outterLayout.setAlignment(new Alignment(AlignmentFlag.AlignTop));
        outterLayout.setMargin(0);
        outterLayout.addWidget(table);
        
        
        buttonBox.setMargin(0);
        buttonBox.setAlignment(new Alignment(AlignmentFlag.AlignTop));
        
        final QToolButton btnAddLine = new QToolButton(table);
        btnAddLine.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CREATE));
        btnAddLine.clicked.connect(table, "addNewRow()");
        buttonBox.addWidget(btnAddLine);
        
        final QToolButton btnRemoveLine = new QToolButton(table);
        btnRemoveLine.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE));
        btnRemoveLine.clicked.connect(table, "removeRow()");
        buttonBox.addWidget(btnRemoveLine);
        
        final QToolButton btnMoveUp = new QToolButton(table);
        btnMoveUp.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.UP));
        buttonBox.addWidget(btnMoveUp);
        btnMoveUp.clicked.connect(table, "moveRowUp()");
        
        final QToolButton btnMoveDown = new QToolButton(table);
        btnMoveDown.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DOWN));
        btnMoveDown.clicked.connect(table, "moveRowDown()");
        buttonBox.addWidget(btnMoveDown);
        
        outterLayout.addLayout(buttonBox);
        this.setLayout(outterLayout);
        outterLayout.update();
    }
    
    @Override
    public EditMask getEditMask() {
        final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBean = 
                org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
        final org.radixware.schemas.editmask.EditMask editMask = xmlBean.addNewRadixEditMask();
        editMask.addNewList();
        
        table.addToXml(editMask);
        return EditMask.loadEditMaskList(environment, type, null, xmlBean);
        
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        if(editMask instanceof EditMaskList) {
            final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBean = 
                    org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
            final org.radixware.schemas.editmask.EditMask em = xmlBean.addNewRadixEditMask();
            editMask.writeToXml(em);
           
            table.loadFromXml(em);
            table.update();
        } else {
            throw new IllegalArgumentException("Instance of EditMaskList was expected");
        }
        
    }

    @Override
    public void setHiddenOptions(final EnumSet<EEditMaskOption> options) {
        return;
    }

    @Override
    public void setVisibleOptions(final EnumSet<EEditMaskOption> options) {
        return;
    }

    @Override
    public void setEnabledOptions(final EnumSet<EEditMaskOption> options) {
        if(options.contains(EEditMaskOption.LIST_VALUES)) {
            table.setEnabled(true);
            buttonBox.setEnabled(true);
        }
    }

    @Override
    public void setDisabledOptions(final EnumSet<EEditMaskOption> options) {
        if(options.contains(EEditMaskOption.LIST_VALUES)) {
            table.setDisabled(true);
            buttonBox.setEnabled(false);
        }
    }

    @Override
    public boolean checkOptions() {
        return table.validate();
    }

    @Override
    public String getObjectName() {
        return objectName();
    }
    
}
