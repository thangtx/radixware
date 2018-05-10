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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout.SizeConstraint;
import com.trolltech.qt.gui.QVBoxLayout;
import java.util.Collections;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.filterparameditor.ParameterEditorWidget;
import org.radixware.kernel.explorer.editors.valeditors.SqmlDefComboBox;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class Parameter_Dialog extends ExplorerDialog {

    private final QGroupBox gbTag;
    private final QComboBox cbPidTranslationMode;
    private final QCheckBox cbIsLiteral;
    private final boolean isReadonly;
    private SqmlDefComboBox cbIndeces = null;
    private ISqmlTableIndices indices = null;
    private ParameterEditorWidget editor = null;
    private final ISqmlParameter parameter;

    public Parameter_Dialog(final IClientEnvironment environment, final ISqmlParameter parameter, final ISqmlTableDef contextTable, final XscmlEditor editText) {
        super(environment, editText, null);
        gbTag = new QGroupBox(environment.getMessageProvider().translate("SqmlEditor", "PID translation:"), this);
        cbPidTranslationMode =  new QComboBox(gbTag);
        cbIsLiteral = new QCheckBox(environment.getMessageProvider().translate("SqmlEditor", "is literal"), this);
        this.isReadonly = editText.isReadOnly();
        setupUi(parameter, contextTable);
        onTranslationModeChanged();
        this.parameter = parameter;
    }

    private void setupUi(final ISqmlParameter parameter, final ISqmlTableDef contextTable) {
        {
            final QGroupBox gbParameter = new QGroupBox(getEnvironment().getMessageProvider().translate("SqmlEditor", "Parameter attributes:"), this);
            layout().addWidget(gbParameter);
            final QVBoxLayout layout = new QVBoxLayout(gbParameter);
            editor = new ParameterEditorWidget(getEnvironment(), contextTable, true, gbParameter);
            layout.addWidget(editor);
            editor.open(parameter, Collections.<String>emptyList());
        }
        final Id referencedTableId = parameter.getReferencedTableId();
        final ISqmlTableDef referencedTable;
        if (referencedTableId != null) {
            referencedTable = getEnvironment().getSqmlDefinitions().findTableById(referencedTableId);
        } else {
            referencedTable = null;
        }
        if (referencedTable != null) {
            final QGridLayout layout = new QGridLayout(gbTag);
            final QLabel lbPidTranslationMode = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "PID translation mode:"), gbTag);
            layout.addWidget(lbPidTranslationMode, 0, 0);
            layout.addWidget(cbPidTranslationMode, 0, 1);
            final QLabel lbSecondaryKey = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Secondary key:"), gbTag);
            indices = referencedTable.getIndices();
            cbIndeces = new SqmlDefComboBox(getEnvironment(), indices, this);
            cbIndeces.setObjectName("cbIndices");
            layout.addWidget(lbSecondaryKey, 1, 0);
            layout.addWidget(cbIndeces, 1, 1);

            cbPidTranslationMode.setEditable(false);
            cbPidTranslationMode.addItem(getEnvironment().getMessageProvider().translate("SqmlEditor", "PK string representation"), EPidTranslationMode.AS_STR);
            cbPidTranslationMode.addItem(getEnvironment().getMessageProvider().translate("SqmlEditor", "Primary Key column(s)"), EPidTranslationMode.PRIMARY_KEY_PROPS);
            if (indices.size() > 0) {
                cbPidTranslationMode.addItem(getEnvironment().getMessageProvider().translate("SqmlEditor", "Secondary Key column(s)"), EPidTranslationMode.SECONDARY_KEY_PROPS);
                cbIndeces.setValue(indices.get(0));
            } else {
                cbIndeces.setValue(null);
                cbIndeces.setReadOnly(true);
            }
            cbPidTranslationMode.currentIndexChanged.connect(this, "onTranslationModeChanged()");
            cbPidTranslationMode.setCurrentIndex(0);
            if (isReadonly) {
                cbPidTranslationMode.setEnabled(false);
                cbIndeces.setReadOnly(true);
            }
            layout().addWidget(gbTag);
            cbIsLiteral.setVisible(false);
        }//referencedTableId!=null
        else {
            layout().addWidget(cbIsLiteral);
            cbIsLiteral.setDisabled(isReadonly);
            gbTag.setVisible(false);
        }
        final EnumSet<EDialogButtonType> buttons;
        if (isReadonly && !parameter.canHavePersistentValue()) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        addButtons(buttons, true);
        setWindowTitle(String.format(getEnvironment().getMessageProvider().translate("SqmlEditor", "Parameter: '%s'"), parameter.getTitle()));
        setWindowIcon(ExplorerIcon.getQIcon(parameter.getIcon()));
        layout().setSizeConstraint(SizeConstraint.SetFixedSize);
    }

    public void readFromTag(final org.radixware.schemas.xscml.Sqml.Item.Parameter parameter) {
        cbIsLiteral.setChecked(parameter.isSetLiteral() && parameter.getLiteral());
    }

    public void readFromTag(final org.radixware.schemas.xscml.Sqml.Item.EntityRefParameter parameter) {
        final EPidTranslationMode translationMode =
                parameter.getPidTranslationMode() == null ? EPidTranslationMode.AS_STR : parameter.getPidTranslationMode();
        if (translationMode == EPidTranslationMode.SECONDARY_KEY_PROPS && indices != null && indices.size() > 0) {
            cbPidTranslationMode.setCurrentIndex(2);
            final Id indexId = parameter.getPidTranslationSecondaryKeyId();
            final ISqmlTableIndexDef indexDef = indices.getIndexById(indexId);
            if (cbIndeces != null && indexDef != null) {
                cbIndeces.setValue(indexDef);
            }
        } else {
            cbPidTranslationMode.setCurrentIndex(translationMode.ordinal());
        }
    }

    public void writeToTag(final org.radixware.schemas.xscml.Sqml.Item.Parameter parameter) {
        parameter.setLiteral(cbIsLiteral.isChecked());        
        parameter.setExpressionList(this.parameter.getType().isArrayType());
    }

    public void writeToTag(final org.radixware.schemas.xscml.Sqml.Item.EntityRefParameter parameter) {
        final EPidTranslationMode translationMode =
                (EPidTranslationMode) cbPidTranslationMode.itemData(cbPidTranslationMode.currentIndex(), Qt.ItemDataRole.UserRole);
        parameter.setPidTranslationMode(translationMode);
        if (translationMode == EPidTranslationMode.SECONDARY_KEY_PROPS && cbIndeces != null && cbIndeces.getValue() != null) {
            parameter.setPidTranslationSecondaryKeyId(cbIndeces.getValue().getId());
        }
        parameter.setExpressionList(this.parameter.getType().isArrayType());
    }

    @SuppressWarnings("unused")
    private void onTranslationModeChanged() {
        if (cbIndeces != null) {
            if (cbPidTranslationMode.currentIndex() < 2) {
                cbIndeces.setValue(null);
                cbIndeces.setReadOnly(true);
            } else if (!isReadonly && indices.size() > 0) {
                cbIndeces.setValue(indices.get(0));
                cbIndeces.setReadOnly(false);
            }
        }
    }

    @Override
    public void done(final int result) {
        if (result == QDialog.DialogCode.Accepted.value()) {
            if (editor != null && !editor.writeAttributes(parameter)) {
                return;
            }
        }
        super.done(result);
    }
}
