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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;



class ValSelPresEditor extends ValEditor<ISqmlSelectorPresentationDef> {

    private final QToolButton selectBtn;

    public ValSelPresEditor(final IClientEnvironment environment, final QWidget parent, final boolean isReadonly) {
        super(environment, parent, new EditMaskNone(), true, isReadonly);
        {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "selectDefinition()");
            action.setToolTip(environment.getMessageProvider().translate("SqmlEditor", "Select Definition"));
            action.setText("...");
            action.setObjectName("select");
            selectBtn = addButton(null, action);
            selectBtn.setText("...");
            selectBtn.setVisible(!isReadonly);
            updateReadOnlyMarker();
        }

        getLineEdit().setReadOnly(true);
    }

    public boolean setPresentation(final Id classId, final Id presentationId) {        
        final ISqmlDefinitions sqmlDefs = getEnvironment().getSqmlDefinitions();
        final ISqmlTableDef classDef = sqmlDefs.findTableById(classId);
        if (classDef==null){
            final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Class presentation #%1$s was not found");
            getEnvironment().getTracer().warning(String.format(message, classId));
            return false;
        }
        final ISqmlSelectorPresentationDef presentationDef = 
            classDef.getSelectorPresentations().getPresentationById(presentationId);
        if (presentationDef==null){
            final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Selector presentation #%1$s was not found");
            getEnvironment().getTracer().warning(String.format(message, presentationId));
            return false;            
        }
        setValue(presentationDef);
        return true;
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        selectBtn.setVisible(!readOnly);
        updateReadOnlyMarker();
    }
    
    @Override
    public void refresh() {
        if (value == null) {
            getLineEdit().setText(getEditMask().getNoValueStr(getEnvironment().getMessageProvider()));
        } else {
            getLineEdit().setText(value.getFullName());
        }
        getLineEdit().setCursorPosition(0);
        getLineEdit().clearFocus();
        clearBtn.setVisible(!isMandatory() && !isReadOnly() && getValue() != null);
    }

    public ISqmlSelectorPresentationDef  selectDefinition() {
        final Id classId, presentationId;
        if (value != null) {
            classId = value.getOwnerClassId();
            presentationId = value.getId();
        } else {
            classId = null;
            presentationId = null;
        }
        final ChooseSelectorPresentationDialog dialog = new ChooseSelectorPresentationDialog(getEnvironment(), classId, presentationId, this);
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            final ISqmlSelectorPresentationDef val = dialog.getCurrentItem();
            setValue(val);
            editingFinished.emit(getValue());
            return val;
        }
        return null;
    }

    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();
        return new QSize(Math.max(size.width(), 250), size.height());
    }

    @Override
    public QSize minimumSizeHint() {
        final QSize size = super.sizeHint();
        return new QSize(Math.max(size.width(), 250), size.height());
    }

    @Override
    public void setPredefinedValues(final List<ISqmlSelectorPresentationDef > predefValues) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public List<ISqmlSelectorPresentationDef > getPredefinedValues() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }
}
