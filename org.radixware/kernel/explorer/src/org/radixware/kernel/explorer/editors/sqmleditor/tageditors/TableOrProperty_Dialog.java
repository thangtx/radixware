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

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlProcessor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;



public class TableOrProperty_Dialog extends ValPropEdit_Dialog {

    private String alias;
    // private String tableAlias;
    private final XscmlEditor editText;
    private final ValEditor<String> editLine;
    private final QLineEdit editLineAlias = new QLineEdit(this);
    private ISqmlTableDef presentClassDef;
    private final EDefinitionDisplayMode showMode;
    private final boolean isThisProp;

    private static String createDialogTitle(final IClientEnvironment environment, final ISqmlColumnDef/*RadPropertyDef*/ prop) {
        return prop == null ? environment.getMessageProvider().translate("SqmlEditor", "Table Tag Editor")
                : environment.getMessageProvider().translate("SqmlEditor", "Property Tag Editor");
    }

    public TableOrProperty_Dialog(final XscmlEditor editor, final ISqmlTableDef presentClassDef, final ISqmlColumnDef prop, final String tableAlias, final String alias, final boolean openForCurTable, final EDefinitionDisplayMode showMode) {
        super(editor.getEnvironment(), editor, prop, createDialogTitle(editor.getEnvironment(),prop));
        editLine = new ValEditor<>(getEnvironment(), this, new EditMaskNone(), false, true);
        //editLine= new ValEditor<String>(this,new EditMaskNone(),false,true);
        editLine.setObjectName("editLine");
        editLineAlias.setObjectName("editLineAlias");
        this.editText = editor;
        this.prop = prop;
        this.alias = alias;
        //this.tableAlias=tableAlias;
        this.isThisProp = openForCurTable;
        this.presentClassDef = presentClassDef;
        this.showMode = showMode;
        createUI();

        setFixedSize(sizeHint().width() + 100, sizeHint().height());
        String s = getDisplaiedName(presentClassDef, showMode);
        if (prop != null) {
            final String displProp = getDisplaiedName(prop, showMode);
            final String displDef = (tableAlias == null ? getDisplaiedName(presentClassDef, showMode) : tableAlias);
            s = displDef + "." + displProp;
        }
        editLine.setValue(s);
    }

    private void createUI() {
        editLineAlias.setText(alias);

        final QAction action = new QAction(this);
        action.triggered.connect(this, "bntOpenDialogClick()");
        editLine.addButton("...", action);

        final QGridLayout gridLayout = new QGridLayout();
        final QLabel lbPropName = new QLabel();
        String lb = prop == null ? getEnvironment().getMessageProvider().translate("SqmlEditor", "Table:")
                : getEnvironment().getMessageProvider().translate("SqmlEditor", "Property:");
        lbPropName.setText(lb);
        final QLabel lbValue = new QLabel();
        lb = prop == null ? getEnvironment().getMessageProvider().translate("SqmlEditor", "Table alias:")
                : getEnvironment().getMessageProvider().translate("SqmlEditor", "Property alias:");
        lbValue.setText(lb);

        final EnumSet<EDialogButtonType> buttons;
        if (editText.isReadOnly()) {
            editLine.setEnabled(false);
            editLineAlias.setEnabled(false);
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }

        gridLayout.addWidget(lbPropName, 0, 0);
        gridLayout.addWidget(editLine, 0, 1);
        gridLayout.addWidget(lbValue, 1, 0);
        gridLayout.addWidget(editLineAlias, 1, 1);
        dialogLayout().addLayout(gridLayout);
        addButtons(buttons, true);
    }

    @SuppressWarnings("unused")
    private void bntOpenDialogClick() {
        if (prop != null) {
            final Object obj;
            if (isThisProp) {
                obj = ((SqmlProcessor) editText.getTagConverter()).chooseThisSqmlColumn(presentClassDef, prop, editText.isReadOnly(), this);
            } else {
                obj = ((SqmlProcessor) editText.getTagConverter()).chooseSqmlColumn(presentClassDef, prop, editText.isReadOnly(), this);//((SqmlProcessor)editText.tagConverter).showChoceObject(editText,presentClassDef,prop,true);
            }
            if ((obj != null) && (obj instanceof ISqmlColumnDef)) {
                prop = (ISqmlColumnDef) obj;
                presentClassDef = prop.getOwnerTable()/*Environment.defManager.getClassPresentationDef(prop.getOwnerClassId())*/;
                if (presentClassDef != null) {
                    final String displProp = getDisplaiedName(prop, showMode);
                    final String displDef = getDisplaiedName(presentClassDef, showMode);//(tableAlias==null ? getDisplaiedName(presentClassDef,showMode) : tableAlias );//getDisplaiedName(presentClassDef,showMode);
                    editLine.setValue(displDef + "." + displProp);
                }
            }
        } else {
            final Object obj = ((SqmlProcessor) editText.getTagConverter()).chooseSqmlTable(presentClassDef, editText.isReadOnly(), this, false);//((SqmlProcessor)editText.tagConverter).showChoceObject(editText, presentClassDef);
            if ((obj != null) && (obj instanceof ISqmlTableDef)) {
                presentClassDef = (ISqmlTableDef) obj;
                if (presentClassDef != null) {
                    editLine.setValue(getDisplaiedName(presentClassDef, showMode));
                }
            }
        }
    }

    @Override
    public void accept() {
        alias = editLineAlias.text();
        super.accept();
    }

    public ISqmlTableDef getPresentClassDef() {
        return presentClassDef;
    }

    public String getAlias() {
        return alias;
    }
}