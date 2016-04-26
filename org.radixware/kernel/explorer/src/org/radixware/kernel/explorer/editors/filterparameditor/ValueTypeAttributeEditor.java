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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValConstSetEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.KernelIcon;

import org.radixware.kernel.explorer.types.RdxIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;


final class ValueTypeAttributeEditor extends AbstractAttributeEditor<EValType> {

    private final static Id VAL_TYPE_ENUM_ID = Id.Factory.loadFrom("acsEFO5RTQOG7NRDJH2ACQMTAIZT4");
    private final static List<EValType> SUPPORTED_TYPES =
            Arrays.asList(
            EValType.STR, EValType.INT, EValType.NUM, EValType.DATE_TIME, EValType.BOOL,
            EValType.CHAR, EValType.BIN, EValType.PARENT_REF);
    private final QLabel lbValType;
    private final ValConstSetEditor cbValType;
    private final QGroupBox gbValType;
    private final QListWidget lwValType;
    public final com.trolltech.qt.QSignalEmitter.Signal0 onDoubleClick =
            new com.trolltech.qt.QSignalEmitter.Signal0();

    public ValueTypeAttributeEditor(IClientEnvironment environment, final boolean isList, final boolean isReadonly, final QWidget parent) {
        super(environment);
        setObjectName("attrEditor_" + getAttribute().name());
        final RadEnumPresentationDef enumDef = environment.getApplication().getDefManager().getEnumPresentationDef(VAL_TYPE_ENUM_ID);
        final RadEnumPresentationDef.Items items = enumDef.getEmptyItems();
        for (EValType valType : SUPPORTED_TYPES) {
            items.addItem(valType);
        }

        if (isList) {
            gbValType = new QGroupBox(Application.translate("SqmlEditor", "Value types:"), parent);
            gbValType.setObjectName("gbValType");
            final QVBoxLayout layout = new QVBoxLayout(gbValType);
            lwValType = new QListWidget(gbValType);
            layout.addWidget(lwValType);
            lwValType.setObjectName("lwValType");

            QListWidgetItem item;

            for (RadEnumPresentationDef.Item enumItem : items) {
                item = new QListWidgetItem((RdxIcon) enumItem.getIcon(), enumItem.getTitle(), lwValType);
                item.setData(Qt.ItemDataRole.UserRole, enumItem.getValue());
            }

            lwValType.setCurrentRow(0);
            lwValType.itemChanged.connect(this, "onValueChanged()");
            lwValType.doubleClicked.connect(onDoubleClick);
            if (isReadonly) {
                lwValType.setDisabled(true);
                WidgetUtils.applyTextOptions(lwValType, ETextOptionsMarker.READONLY_VALUE);                
            } else {
                WidgetUtils.applyDefaultTextOptions(lwValType);                
            }

            lbValType = null;
            cbValType = null;
        } else {
            lbValType = new QLabel(getAttribute().getTitle(), parent);
            lbValType.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
            lbValType.setObjectName("lbValType");
            final EditMaskConstSet editMask = new EditMaskConstSet(VAL_TYPE_ENUM_ID, null, null, null);
            editMask.setItems(items);
            cbValType = new ValConstSetEditor(getEnvironment(), parent, editMask, true, isReadonly);
            cbValType.setObjectName("cbValType");
            cbValType.setValue(EValType.STR.getValue());
            cbValType.valueChanged.connect(this, "onValueChanged()");
            lbValType.setBuddy(cbValType);
            /*
            cbValType = new QComboBox(parent);
            cbValType.setObjectName("cbValType");

            for (EValType valType: SUPPORTED_TYPES)
            cbValType.addItem(getValTypeQIcon(valType), getValTypeTitle(valType), valType);

            cbValType.setCurrentIndex(0);
            cbValType.currentIndexChanged.connect(this,"onValueChanged()");
             */
            setupLabelTextOptions(lbValType, isReadonly);
            cbValType.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);

            gbValType = null;
            lwValType = null;
        }

    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
    }

    private static QIcon getValTypeQIcon(final EValType valType) {
        return ExplorerIcon.getQIcon(KernelIcon.getInstance(RadixObjectIcon.getForValType(valType)));
    }

    private static String getValTypeTitle(final EValType valType) {
        switch (valType) {
            case STR:
                return Application.translate("SqmlEditor", "String");
            case INT:
                return Application.translate("SqmlEditor", "Integer");
            case NUM:
                return Application.translate("SqmlEditor", "Decimal");
            case DATE_TIME:
                return Application.translate("SqmlEditor", "Date/Time");
            case BOOL:
                return Application.translate("SqmlEditor", "Boolean");
            case PARENT_REF:
                return Application.translate("SqmlEditor", "Object reference");
            case CHAR:
                return Application.translate("SqmlEditor", "Character");
            case CLOB:
                return Application.translate("SqmlEditor", "Large string");
            case BIN:
                return Application.translate("SqmlEditor", "Binary");
            case BLOB:
                return Application.translate("SqmlEditor", "Large binary");
            default:
                return valType.getName();
        }

    }

    protected ValueTypeAttributeEditor(IClientEnvironment userSession, final boolean isReadonly, final QWidget parent) {
        this(userSession, false, isReadonly, parent);
    }

    @Override
    public boolean updateParameter(ISqmlParameter parameter) {
        if (parameter instanceof ISqmlModifiableParameter) {
            final EValType valType = getAttributeValue();
            ((ISqmlModifiableParameter) parameter).setValType(valType, EditMask.newInstance(valType), parameter.isMandatory(), parameter.getNullString());
        }
        return true;
    }

    @Override
    public void updateEditor(ISqmlParameter parameter) {
        final EValType valType = parameter.getType();
        if (lwValType == null) {
            if (valType == EValType.PARENT_REF) {
                cbValType.setReadOnly(true);
            } else {
                final EditMaskConstSet editMask = (EditMaskConstSet) cbValType.getEditMask();
                final RadEnumPresentationDef.Items items = editMask.getItems(environment.getApplication());
                items.removeItem(EValType.PARENT_REF);
                editMask.setItems(items);
                cbValType.setEditMask(editMask);
            }
            cbValType.setValue(valType.getValue());
        } else {
            for (int i = 0; i < lwValType.count(); i++) {
                if (lwValType.item(i).data(Qt.ItemDataRole.UserRole).equals(valType.getValue())) {
                    lwValType.setCurrentRow(i);
                    break;
                }
            }
        }
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.VALUE_TYPE;
    }

    @Override
    public EValType getAttributeValue() {
        if (lwValType == null) {
            return EValType.getForValue((Long) cbValType.getValue());
        } else {
            final int row = lwValType.currentRow();
            if (row >= 0) {
                final Object objVal = lwValType.currentItem().data(Qt.ItemDataRole.UserRole);
                return EValType.getForValue((Long) objVal);
            } else {
                return null;
            }
        }
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.noneOf(EFilterParamAttribute.class);
    }

    @Override
    public void onBaseAttributeChanged(AbstractAttributeEditor linkedEditor) {
    }

    @Override
    public QLabel getLabel() {
        return lbValType;
    }

    @Override
    public QWidget getEditorWidget() {
        return gbValType == null ? cbValType : gbValType;
    }

    @Override
    public void free() {
        if (cbValType != null) {
            cbValType.close();
        }
    }
}
