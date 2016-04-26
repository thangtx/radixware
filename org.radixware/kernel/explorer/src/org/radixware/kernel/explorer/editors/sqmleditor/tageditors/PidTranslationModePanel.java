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
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QVBoxLayout;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.SqmlDefComboBox;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;


public class PidTranslationModePanel extends ExplorerWidget {

    private final QGroupBox gbTag;
    private final QComboBox cbPidTranslationMode;
    private final boolean isReadonly;
    private SqmlDefComboBox cbIndeces = null;
    private ISqmlTableIndices indices = null;

    public PidTranslationModePanel(final IClientEnvironment environment, final ISqmlTableDef contextTable, final boolean isReadonly) {
        super(environment);
        gbTag = new QGroupBox(environment.getMessageProvider().translate("SqmlEditor", "PID translation:"), this);
        cbPidTranslationMode = new QComboBox(gbTag);
        this.isReadonly = isReadonly;
        setupUi(contextTable);
        onTranslationModeChanged();
    }

    private void setupUi(final ISqmlTableDef contextTable) {
        final QVBoxLayout mainLayout = new QVBoxLayout();
        mainLayout.setMargin(0);
        final QGridLayout layout = new QGridLayout(gbTag);
        final QLabel lbPidTranslationMode = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "PID translation mode:"), gbTag);
        layout.addWidget(lbPidTranslationMode, 0, 0);
        layout.addWidget(cbPidTranslationMode, 0, 1);
        final QLabel lbSecondaryKey = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Secondary key:"), gbTag);
        indices = contextTable.getIndices();
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

        mainLayout.addWidget(gbTag);
        this.setLayout(mainLayout);
    }

    public void readFromTag(final EPidTranslationMode tranclationMode, final Id pidTranslationSecondaryKeyId/*org.radixware.schemas.xscml.Sqml.Item.ThisTableRef thisTableRef*/) {
        final EPidTranslationMode translationMode =
                tranclationMode == null ? EPidTranslationMode.AS_STR : tranclationMode;
        if (translationMode == EPidTranslationMode.SECONDARY_KEY_PROPS && indices != null && indices.size() > 0) {
            cbPidTranslationMode.setCurrentIndex(2);
            final Id indexId = pidTranslationSecondaryKeyId;
            final ISqmlTableIndexDef indexDef = indices.getIndexById(indexId);
            if (cbIndeces != null && indexDef != null) {
                cbIndeces.setValue(indexDef);
            }
        } else {
            cbPidTranslationMode.setCurrentIndex(translationMode.ordinal());
        }
    }

    public EPidTranslationMode getTranslationMode() {
        return (EPidTranslationMode) cbPidTranslationMode.itemData(cbPidTranslationMode.currentIndex(), Qt.ItemDataRole.UserRole);
    }

    public Id getIndecesValue() {
        if (cbIndeces != null && cbIndeces.getValue() != null) {
            return cbIndeces.getValue().getId();
        }
        return null;
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
}
