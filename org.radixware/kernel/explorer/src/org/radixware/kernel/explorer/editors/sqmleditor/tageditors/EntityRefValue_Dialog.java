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
import com.trolltech.qt.gui.QLayout.SizeConstraint;
import com.trolltech.qt.gui.QLineEdit;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.dialogs.SelectEntityDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;


public class EntityRefValue_Dialog extends ExplorerDialog {

    private final PidTranslationModePanel pidTranslModePanel;
    private final boolean isReadonly;
    private final ValEditor<String> edRefObj;
    //private final ValBoolEditor edIsLiteral;
    private final QLineEdit edDisplayVal = new QLineEdit(this);
    private Pid pid;
    private final ISqmlTableDef table;

    public EntityRefValue_Dialog(final IClientEnvironment environment, final ISqmlTableDef table, final String objTitle, final XscmlEditor editText) {
        super(environment, editText, null);
        this.table = table;
        edRefObj = new ValEditor<>(environment, this, new EditMaskNone(), false, true);
        edRefObj.setObjectName("edDisplayVal");
        //edIsLiteral = new ValBoolEditor(environment, this, new EditMaskNone(), true, false);
        //edIsLiteral.setValue(Boolean.valueOf(false));
        this.isReadonly = editText.isReadOnly();
        pidTranslModePanel = new PidTranslationModePanel(environment, table, isReadonly);
        setupUi(objTitle);
    }

    private void setupUi(final String objTitle) {
        final QAction actChooseObj = new QAction(this);
        actChooseObj.triggered.connect(this, "bntChooseObj()");
        edRefObj.addButton("...", actChooseObj);

        final QGridLayout layout = new QGridLayout();
        layout.setSpacing(5);
        layout.setMargin(10);
        final QLabel lbRefObj = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Referenced Object:"), this);
        layout.addWidget(lbRefObj, 0, 0);
        edRefObj.setValue(objTitle);
        layout.addWidget(edRefObj, 0, 1);
        final QLabel lbDisplName = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Name displayed:"), this);
        layout.addWidget(lbDisplName, 1, 0);
        layout.addWidget(edDisplayVal, 1, 1);
        //final QLabel lbIsLiteral = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Literal:"), this);
        //layout.addWidget(lbIsLiteral, 2, 0);
        //layout.addWidget(edIsLiteral, 2, 1);

        layout().addItem(layout);
        layout().addWidget(pidTranslModePanel);
        final EnumSet<EDialogButtonType> buttons;
        if (isReadonly) {
            edDisplayVal.setEnabled(false);
            edRefObj.setEnabled(false);
            //edIsLiteral.setEnabled(false);
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        addButtons(buttons, true);
        setWindowTitle(getEnvironment().getMessageProvider().translate("SqmlEditor", "Entity Object Reference"));
        //setWindowIcon(table.getIcon().getQIcon());
        layout().setSizeConstraint(SizeConstraint.SetFixedSize);
    }

    public void writeToTag(final org.radixware.schemas.xscml.Sqml.Item.EntityRefValue entityRefValue) {
        final EPidTranslationMode translationMode = pidTranslModePanel.getTranslationMode();
        entityRefValue.setPidTranslationMode(translationMode);
        final Id indecesValue = pidTranslModePanel.getIndecesValue();
        if (translationMode == EPidTranslationMode.SECONDARY_KEY_PROPS && indecesValue != null) {
            entityRefValue.setPidTranslationSecondaryKeyId(indecesValue);
        }
        final String displayVal = edDisplayVal.text();
        entityRefValue.setDisplayValue(displayVal);
        if (pid != null) {
            entityRefValue.setReferencedObjectPidAsStr(pid.toString());
            entityRefValue.setReferencedTableId(pid.getTableId());
        }
        entityRefValue.setLiteral(true);
    }

    public void readFromTag(final org.radixware.schemas.xscml.Sqml.Item.EntityRefValue entityRefValue) {
        final String displayValue = entityRefValue.getDisplayValue();
        if ((displayValue != null) && (!displayValue.isEmpty())) {
            edDisplayVal.setText(displayValue);
        }
        //edIsLiteral.setValue(entityRefValue.getLiteral());
        pidTranslModePanel.readFromTag(entityRefValue.getPidTranslationMode(), entityRefValue.getPidTranslationSecondaryKeyId());
    }

    public String getRefObjectTitle() {
        return edRefObj.getValue();
    }

    @SuppressWarnings("unused")
    private void bntChooseObj() throws ServiceClientException, InterruptedException {
        /* EntityModel entityModel= ((SqmlProcessor)editText.tagConverter).chooseSqmlTableObject(table,isReadonly, null);
        if(entityModel!=null){
        pid= entityModel.getPid();
        edRefObj.setValue(pid.getDefaultEntityTitle());
        }*/
        final SelectEntityDialog chooseObjDialog;
        try {
            final RadClassPresentationDef classDef = getEnvironment().getApplication().getDefManager().getClassPresentationDef(table.getId());
            final RadSelectorPresentationDef selPresDef = classDef.getDefaultSelectorPresentation();
            final GroupModel groupModel = GroupModel.openTableContextlessSelectorModel(getEnvironment(), selPresDef);

            chooseObjDialog = new SelectEntityDialog(groupModel, false);
            if (com.trolltech.qt.gui.QDialog.DialogCode.resolve(chooseObjDialog.exec()).equals(com.trolltech.qt.gui.QDialog.DialogCode.Accepted)) {
                final EntityModel entityModel = chooseObjDialog.selectedEntity;
                if (entityModel != null) {
                    pid = entityModel.getPid();
                    edRefObj.setValue(pid.getDefaultEntityTitle(getEnvironment().getEasSession()));
                }
            }
        } catch (InterruptedException ex) {
        }
    }
}
