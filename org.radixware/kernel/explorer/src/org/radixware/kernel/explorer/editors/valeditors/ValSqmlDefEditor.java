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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.meta.sqml.ISqmlBrokenDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitionsFilter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.explorer.dialogs.ChooseSqmlDefinitionDialog;

import org.radixware.kernel.explorer.models.SqmlTreeModel;


public class ValSqmlDefEditor extends ValEditor<ISqmlDefinition> {

    private final SqmlTreeModel treeModel;
    private ISqmlDefinitionsFilter filter;
    private final QToolButton selectBtn;
    private String dialogTitle;
    private EDefinitionDisplayMode displayMode = EDefinitionDisplayMode.SHOW_SHORT_NAMES;

    public ValSqmlDefEditor(IClientEnvironment environment, final QWidget parent, final SqmlTreeModel definitionsModel, final boolean mandatory, final boolean readonly) {
        super(environment, parent, new EditMaskNone(), mandatory, readonly);
        treeModel = definitionsModel;

        {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "selectDefinition()");
            action.setToolTip(environment.getMessageProvider().translate("SqmlEditor", "Select Definition"));
            action.setText("...");
            selectBtn = addButton(null, action);
            selectBtn.setText("...");
        }

        getLineEdit().setReadOnly(true);
        updateButtons();
    }

    public final void setSelectButtonIcon(final QIcon icon) {
        selectBtn.setText(icon == null ? "..." : null);
        selectBtn.setIcon(icon);
    }

    public final void setSelectButtonHint(final String hint) {
        selectBtn.setToolTip(hint);
    }

    public final void setDefinitionsFilter(final ISqmlDefinitionsFilter definitionsFilter) {
        filter = definitionsFilter;
    }

    public final ISqmlDefinitionsFilter getDefinitionsFilter() {
        return filter;
    }

    public final EDefinitionDisplayMode getDefinitionDisplayMode() {
        return displayMode;
    }

    public final void setDefinitionDisplayMode(final EDefinitionDisplayMode mode) {
        displayMode = mode;
        refresh();
    }

    public final String getDialogTitle() {
        return dialogTitle;
    }

    public final void setDialogTitle(final String title) {
        dialogTitle = title;
    }

    @Override
    public void setMandatory(boolean mandatory) {
        super.setMandatory(mandatory);
        updateButtons();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        updateButtons();
    }

    @Override
    public void refresh() {
        if (value == null) {
            getLineEdit().setText(getEditMask().getNoValueStr(getEnvironment().getMessageProvider()));
        } else {
            getLineEdit().setText(value.getDisplayableText(displayMode));
        }
        getLineEdit().setCursorPosition(0);
        getLineEdit().clearFocus();
        clearBtn.setVisible(!isMandatory() && !isReadOnly() && getValue() != null);
        updateButtons();
    }

    @Override
    protected ValidationResult calcValidationResult(final ISqmlDefinition value) {
        if (value instanceof ISqmlBrokenDefinition){
            final ISqmlBrokenDefinition definition = (ISqmlBrokenDefinition)getValue();
            final String reason;
            if (definition.getDefinitionTypeName()==null || definition.getDefinitionTypeName().isEmpty()){
                reason = getEnvironment().getMessageProvider().translate("SqmlEditor", "Definition not found");
            }
            else{
                final String template = getEnvironment().getMessageProvider().translate("SqmlEditor", "%s not found");
                reason = String.format(template, definition.getDefinitionTypeName());
            }
            return ValidationResult.Factory.newInvalidResult(reason);
        }
        return super.calcValidationResult(value);
    }



    public ISqmlDefinition selectDefinition() {
        final List<ISqmlDefinition> path = new ArrayList<>();
        if (value instanceof ISqmlColumnDef) {
            final ISqmlTableDef ownerTable = ((ISqmlColumnDef) value).getOwnerTable();
            path.add(ownerTable);
            path.add(value);
        } else if (value != null) {
            path.add(value);
        }
        final ChooseSqmlDefinitionDialog dialog =
                new ChooseSqmlDefinitionDialog(getEnvironment(), treeModel, filter, path, false, this);
        dialog.setWindowTitle(dialogTitle);
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            final ISqmlDefinition val = dialog.getCurrentItem();
            setValue(val);
            editingFinished.emit(getValue());
            return val;
        }
        return null;
    }
        
    private void updateButtons() {
        selectBtn.setVisible(!isReadOnly());
        updateReadOnlyMarker();
    }

    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();
        return new QSize(Math.max(size.width(), 200), size.height());
    }

    @Override
    public QSize minimumSizeHint() {
        final QSize size = super.sizeHint();
        return new QSize(Math.max(size.width(), 200), size.height());
    }
   
}
