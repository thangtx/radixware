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
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.io.IOException;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;



class ValSelPresEditor extends ValEditor<AdsSelectorPresentationDef> {

    private static class AdsClassSearcher extends AdsVisitorProvider.AdsTopLevelDefVisitorProvider {

        private Id classId;

        public AdsClassSearcher(final Id classId) {
            super();
            this.classId = classId;
        }

        @Override
        public boolean isTarget(final RadixObject radixObject) {
            return (radixObject instanceof AdsEntityObjectClassDef)
                    && ((AdsEntityObjectClassDef) radixObject).getId().equals(classId);
        }
    }
    private final QToolButton selectBtn;

    public ValSelPresEditor(final IClientEnvironment environment, final QWidget parent, final boolean isReadonly) {
        super(environment, parent, new EditMaskNone(), true, isReadonly);
        {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "selectDefinition()");
            action.setToolTip(Application.translate("SqmlEditor", "Select Definition"));
            action.setText("...");
            selectBtn = addButton(null, action);
            selectBtn.setText("...");
            selectBtn.setVisible(!isReadonly);
            updateReadOnlyMarker();
        }

        getLineEdit().setReadOnly(true);
    }

    public boolean setPresentation(final Id classId, final Id presentationId) {
        final Branch branch;
        try {
            branch = getEnvironment().getApplication().getDefManager().getRepository().getBranch();
        } catch (IOException ex) {
            getEnvironment().processException(ex);
            return false;
        }
        final AdsEntityObjectClassDef classDef = (AdsEntityObjectClassDef) branch.find(new AdsClassSearcher(classId));
        if (classDef != null) {
            final AdsSelectorPresentationDef presentation =
                    classDef.getPresentations().getSelectorPresentations().findById(presentationId, EScope.LOCAL_AND_OVERWRITE).get();
            if (presentation != null) {
                setValue(presentation);
                return true;
            }
        }
        return false;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        selectBtn.setVisible(!readOnly);
        updateReadOnlyMarker();
    }

    @Override
    public void refresh() {
        if (value == null) {
            getLineEdit().setText(getEditMask().getNoValueStr(getEnvironment().getMessageProvider()));
        } else {
            getLineEdit().setText(value.getQualifiedName());
        }
        getLineEdit().setCursorPosition(0);
        getLineEdit().clearFocus();
        clearBtn.setVisible(!isMandatory() && !isReadOnly() && getValue() != null);
    }

    public AdsSelectorPresentationDef selectDefinition() {
        final Branch branch;
        try {
            branch = getEnvironment().getApplication().getDefManager().getRepository().getBranch();
        } catch (IOException ex) {
            getEnvironment().processException(ex);
            return null;
        }
        final Id classId, presentationId;
        if (value != null) {
            classId = value.getOwnerClass().getId();
            presentationId = value.getId();
        } else {
            classId = null;
            presentationId = null;
        }
        final ChooseSelectorPresentationDialog dialog = new ChooseSelectorPresentationDialog(getEnvironment(), branch, classId, presentationId, this);
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            final AdsSelectorPresentationDef val = dialog.getCurrentItem();
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
    public void setPredefinedValues(final List<AdsSelectorPresentationDef> predefValues) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public List<AdsSelectorPresentationDef> getPredefinedValues() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }
}
