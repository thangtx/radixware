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
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlProcessor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;



public class TypifiedValue_Dialog extends ValPropEdit_Dialog {

    private final XscmlEditor editText;
    private final ValEditor<String> editLineProp;
    private ISqmlTableDef presentClassDef;
    private final boolean isReadOnly;
    private final EDefinitionDisplayMode showMode;
    private final QGridLayout gridLayout = new QGridLayout();
    //private final QCheckBox cbLiteral = new QCheckBox(this);
    private boolean isLiteral;

    @SuppressWarnings({"unchecked"})
    public TypifiedValue_Dialog(final IClientEnvironment environment, final ISqmlColumnDef prop, final XscmlEditor editText, final Object val, final EDefinitionDisplayMode showMode, final boolean isLiteral) {
        super(environment, editText, prop, environment.getMessageProvider().translate("SqmlEditor", "Edit Value"), false);
        editLineProp = new ValEditor<>(environment, this, new EditMaskNone(), false, true);
        this.showMode = showMode;
        isReadOnly = editText.isReadOnly();
        this.setWindowTitle(environment.getMessageProvider().translate("SqmlEditor", "Edit Value"));//�������� ��������
        this.editText = editText;
        setValue(val);
        valEditor.setValue(this.val.get(0));
        this.isLiteral = isLiteral;

        createUI();
        setFixedSize(sizeHint().width() + 100, sizeHint().height());
        presentClassDef = prop.getOwnerTable();
        final String displProp = getDisplaiedName(prop, showMode);
        final String displDef = getDisplaiedName(presentClassDef, showMode);
        editLineProp.setValue(displDef + "." + displProp);
        valEditor.setReadOnly(isReadOnly);
    }

    private void setValue(final Object val) {
        if (prop.getType() == EValType.PARENT_REF) {
            final Pid pid = (Pid) val;
            try {
                this.val.clear();
                this.val.add(new Reference(pid, pid.getDefaultEntityTitle(getEnvironment().getEasSession())));
            } catch (ServiceClientException ex) {
                getEnvironment().processException(ex);
                //Logger.getLogger(Condition_Dialog.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                //Logger.getLogger(Condition_Dialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.val.clear();
            this.val.add(val);
        }
    }

    private void createUI() {
        final QAction action = new QAction(this);
        action.triggered.connect(this, "bntOpenDialogClick()");
        editLineProp.setObjectName("editLineProp");
        editLineProp.addButton("...", action);

        //cbLiteral.setObjectName("cbLiteral");
        //cbLiteral.setChecked(isLiteral);
        //cbLiteral.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Literal"));

        //gridLayout=new QGridLayout();
        final QLabel lbPropName = new QLabel(this);
        lbPropName.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Property:"));
        final QLabel lbValue = new QLabel(this);
        lbValue.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Value:"));

        final EnumSet<EDialogButtonType> buttons;
        if (isReadOnly) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
            editLineProp.setEnabled(false);
            //cbLiteral.setEnabled(false);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }

        gridLayout.addWidget(lbPropName, 0, 0);
        gridLayout.addWidget(editLineProp, 0, 1);
        gridLayout.addWidget(lbValue, 1, 0);
        gridLayout.addWidget(valEditor, 1, 1);
        //gridLayout.addWidget(cbLiteral, 2, 1);
        dialogLayout().addLayout(gridLayout);
        addButtons(buttons, true);
    }

    @SuppressWarnings({"unused", "unchecked"})
    private void bntOpenDialogClick() {
        final Object obj = ((SqmlProcessor) editText.getTagConverter()).chooseSqmlColumn(presentClassDef, prop, isReadOnly, this);//((SqmlProcessor)editText.tagConverter).showChoceObject(editText,presentClassDef,prop,this);
        if ((obj != null) && (obj instanceof ISqmlColumnDef)) {
            prop = (ISqmlColumnDef) obj;
            presentClassDef = prop.getOwnerTable();//Environment.defManager.getClassPresentationDef(prop.getOwnerTableId()/*getOwnerClassId()*/);
            if (presentClassDef != null) {
                final String displProp = getDisplaiedName(prop, showMode);
                final String displDef = getDisplaiedName(presentClassDef, showMode);
                editLineProp.setValue(displDef + "." + displProp);
                final ValEditor newValEditor = createValEditor();
                if (newValEditor != null) {
                    dialogLayout().removeWidget(valEditor);
                    valEditor.setParent(null);
                    valEditor = newValEditor;
                    gridLayout.addWidget(valEditor, 1, 1);
                    getButton(EDialogButtonType.OK).setEnabled(true);
                } else {
                    valEditor.setValue(null);
                    valEditor.setEnabled(false);
                    getButton(EDialogButtonType.OK).setEnabled(false);
                }
            }
        }
    }

    @Override
    public void accept() {
        if (valEditor!=null && !valEditor.checkInput()){
            return;
        }
        if ((valEditor != null) && (valEditor.getValue() != null)) {
            this.val.clear();
            if (valEditor instanceof ValRefEditor) {
                val.add(((ValRefEditor) valEditor).getValue().getPid());
            } else {

                val.add(valEditor.getValue());
            }
        } else {
            val = null;
        }
        //isLiteral = cbLiteral.isChecked();
        isLiteral = true;
        super.accept();
    }

    public boolean isLiteral() {
        return isLiteral;
    }
}