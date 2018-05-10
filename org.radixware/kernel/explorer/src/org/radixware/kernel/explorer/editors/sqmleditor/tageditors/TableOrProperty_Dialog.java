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

import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.editors.valeditors.ValSqmlDefEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.models.SqmlTreeModel;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;



public class TableOrProperty_Dialog extends ValPropEdit_Dialog {
        
    private final ValSqmlDefEditor sqmlDefEditor;
    private final ValStrEditor aliasEditor;
    private ISqmlTableDef classDef;
    private String alias;

    public TableOrProperty_Dialog(final IClientEnvironment environment, 
                                                  final ISqmlTableDef classDef,
                                                  final ISqmlColumnDef prop,
                                                  final String alias,
                                                  final boolean openForCurTable,
                                                  final EDefinitionDisplayMode showMode, 
                                                  final boolean isReadOnly,
                                                  final QWidget parentWidget) {
        super(environment, parentWidget, prop, "SqmlTableOrPropertyDialog");        
        final MessageProvider mp = getEnvironment().getMessageProvider();
        final SqmlTreeModel sqmlModel;
        final String dialogTitle;
        if (prop==null){
            if (openForCurTable){
                final List<ISqmlDefinition> columns = new ArrayList<>();
                columns.addAll(classDef.getColumns().getAll());
                sqmlModel = new SqmlTreeModel(environment, columns, EnumSet.of(SqmlTreeModel.ItemType.PROPERTY));
            }else{
                final EnumSet<SqmlTreeModel.ItemType> itemTypes = EnumSet.of(SqmlTreeModel.ItemType.PROPERTY, SqmlTreeModel.ItemType.MODULE_INFO);
                sqmlModel = new SqmlTreeModel(environment, null, itemTypes);
                sqmlModel.setMarkDeprecatedItems(true);
            }
            dialogTitle = isReadOnly ? mp.translate("SqmlEditor", "Column") : mp.translate("SqmlEditor", "Select Column");
        }else{
            sqmlModel = new SqmlTreeModel(environment, null, EnumSet.of(SqmlTreeModel.ItemType.MODULE_INFO));
            sqmlModel.setMarkDeprecatedItems(true);
            dialogTitle = isReadOnly ? mp.translate("SqmlEditor", "Table") : mp.translate("SqmlEditor", "Select Table");
        }
        sqmlModel.setDisplayMode(showMode);
        sqmlDefEditor = new ValSqmlDefEditor(environment, this, sqmlModel, true, isReadOnly);        
        sqmlDefEditor.setDefinitionDisplayMode(showMode);
        final SqmlDefDisplayProvider displayProvider;
        if (classDef.hasAlias()){
            displayProvider = new SqmlDefDisplayProvider(showMode, classDef.getAlias());
        }else{
            displayProvider = new SqmlDefDisplayProvider(showMode);
        }
        sqmlDefEditor.setDisplayStringProvider(displayProvider);
        sqmlDefEditor.setDialogTitle(dialogTitle);
        sqmlDefEditor.valueChanged.connect(this,"onChangeDefinition()");
        sqmlDefEditor.setObjectName("editLine");
        
        aliasEditor = new ValStrEditor(environment, this, new EditMaskStr(), false, isReadOnly);
        aliasEditor.setObjectName("editLineAlias");
        
        createUI(isReadOnly);
        
        sqmlDefEditor.setValue(prop==null ? classDef : prop);        
        aliasEditor.setValue(alias);
        this.classDef = classDef;
        this.alias = alias;
    }
    
    private boolean isTableDialog(){
        return getProperty()==null;
    }

    private void createUI(final boolean isReadOnly) {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        if (getProperty()==null){
            setWindowTitle(mp.translate("SqmlEditor", "Table Tag Editor"));
        }else{
            setWindowTitle(mp.translate("SqmlEditor", "Property Tag Editor"));
        }        

        final String defLabelText = isTableDialog() ? mp.translate("SqmlEditor", "Table:") : mp.translate("SqmlEditor", "Property:");        
        final QLabel lbDefinition = new QLabel(defLabelText, this);
        final String aliasLabelText = isTableDialog() ? mp.translate("SqmlEditor", "Table alias:") : mp.translate("SqmlEditor", "Property alias:");
        final QLabel lbAlias = new QLabel(aliasLabelText, this);
        final ExplorerTextOptions labelTextOptions = getLabelTextOptions();
        labelTextOptions.applyTo(lbDefinition);
        labelTextOptions.applyTo(lbAlias);
                
        final QFormLayout formLayout = new QFormLayout();
        formLayout.addRow(lbDefinition, sqmlDefEditor);
        formLayout.addRow(lbAlias, aliasEditor);
        dialogLayout().addLayout(formLayout);

        final EnumSet<EDialogButtonType> buttons;
        if (isReadOnly) {
            sqmlDefEditor.setEnabled(false);
            aliasEditor.setEnabled(false);
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        
        addButtons(buttons, true);
    }

    @SuppressWarnings("unused")
    private void onChangeDefinition() {
        aliasEditor.setValue(null);
        final ISqmlDefinition definition = sqmlDefEditor.getValue();
        if (definition instanceof ISqmlColumnDef){
            setProperty((ISqmlColumnDef)definition);
        }else if (definition instanceof ISqmlTableDef){
            classDef = (ISqmlTableDef)definition;
        }
    }

    @Override
    public void accept() {
        if (sqmlDefEditor.getValue()!=null){
            alias = aliasEditor.getValue();
            super.accept();
        }        
    }

    public ISqmlTableDef getPresentClassDef() {
        return classDef;
    }

    public String getAlias() {
        return alias;
    }
}