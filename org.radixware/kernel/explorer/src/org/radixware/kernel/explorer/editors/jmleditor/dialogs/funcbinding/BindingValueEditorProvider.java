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
package org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding;

import com.trolltech.qt.QSignalEmitter.Signal1;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QDoubleSpinBox;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QWidget;
import java.math.BigDecimal;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValArrEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;

class BindingValueEditorProvider extends QObject {

    private ValEditor valEditor = null;
    private QLineEdit strEditor = null;
    private QSpinBox intEditor = null;
    private QDoubleSpinBox doubleEditor = null;
    private QCheckBox booleanEditor = null;
    private QComboBox comboBox = null;
    public final Signal1<Object> valueChanged = new Signal1<>();
    private final AdsUserFuncDef userFunc;
    private Id enumId = null;

    BindingValueEditorProvider(final AdsTypeDeclaration type, final AdsUserFuncDef userFunc, final IClientEnvironment environment, QWidget parent) {
        this.userFunc = userFunc;
        createEditor(type, environment, parent);

    }

    Id getEnumId() {
        return enumId;
    }

    private void createEditor(final AdsTypeDeclaration type, final IClientEnvironment environment, final QWidget parent) {
        if (type.getPath() != null && type.getPath().asList().size() == 1 && type.getPath().asList().get(0).getPrefix() == EDefinitionIdPrefix.ADS_ENUMERATION) {
            final AdsDefinition enumDef = AdsUserFuncDef.Lookup.findTopLevelDefinition(userFunc, type.getPath().asList().get(0));
            enumId = type.getPath().asList().get(0);
            if (enumDef != null && (enumDef instanceof AdsEnumDef)) {
                if ((type.getTypeId() != EValType.ARR_INT) && (type.getTypeId() != EValType.ARR_STR) && (type.getTypeId() != EValType.ARR_CHAR)) {
                    comboBox = new QComboBox();
                    comboBox.currentIndexChanged.connect(this, "currentIndexChanged(Integer)");
                    for (AdsEnumItemDef item : ((AdsEnumDef) enumDef).getItems().list(ExtendableDefinitions.EScope.LOCAL)) {
                        comboBox.addItem(item.getName(), item.getValue().toObject(((AdsEnumDef) enumDef).getItemType()));
                    }
                    return;
                } else {
                    valEditor = ValEditor.createForValType(environment, type.getTypeId(), new EditMaskConstSet(enumId), true, false, parent);
                    valEditor.valueChanged.connect(this, "valEditorChanged(Object)");
                    return;
                }
            }
        }
        try {
            if (type.getTypeId() != EValType.ARR_CLOB && type.getTypeId() != EValType.ARR_BLOB) {
                valEditor = ValEditor.createForValType(environment, type.getTypeId(), EditMask.newInstance(type.getTypeId()), true, false, parent);
                final String title = Application.translate("JmlEditor", "Parameter Value Editor");
                if (valEditor instanceof ValArrEditor) {
                    ((ValArrEditor) valEditor).setDialogTitle(title);
                }
                valEditor.valueChanged.connect(this, "valEditorChanged(Object)");
            }
        } catch (IllegalArgumentException e) {
            valEditor = null;
        }
        if (valEditor != null) {
            return;
        }
        if ((type.getTypeId() == EValType.JAVA_TYPE || type.getTypeId() == EValType.JAVA_CLASS) && type.getExtStr() != null && type.getArrayDimensionCount() == 0) {
            final String typeName = type.getExtStr();
            if ("boolean".equals(typeName) || "java.lang.Boolean".equals(typeName)) {
                booleanEditor = new QCheckBox(parent);
                booleanEditor.stateChanged.connect(this, "valueChanged(Integer)");
            } else if ("char".equals(typeName) || "java.lang.String".equals(typeName)) {
                strEditor = new QLineEdit(parent);
                strEditor.textChanged.connect(this, "valueChanged(String)");
                if ("char".equals(typeName)) {
                    strEditor.setMaxLength(1);
                }
            } else if ("int".equals(typeName) || "java.lang.Integer".equals(typeName)) {
                intEditor = new QSpinBox(parent);
                intEditor.valueChanged.connect(this, "valueChanged(Integer)");
                intEditor.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else if ("long".equals(typeName) || "java.lang.Long".equals(typeName)) {
                doubleEditor = new QDoubleSpinBox(parent);
                doubleEditor.valueChanged.connect(this, "valueChanged(Double)");
                doubleEditor.setRange(Long.MIN_VALUE, Long.MAX_VALUE);
                doubleEditor.setDecimals(0);
            } else if ("short".equals(typeName) || "java.lang.Short".equals(typeName)) {
                intEditor = new QSpinBox(parent);
                intEditor.valueChanged.connect(this, "valueChanged(Integer)");
                intEditor.setRange(Short.MIN_VALUE, Short.MAX_VALUE);
            } else if ("double".equals(typeName) || "java.lang.Double".equals(typeName) || "float".equals(typeName) || "java.lang.Float".equals(typeName)) {
                doubleEditor = new QDoubleSpinBox(parent) {
                    @Override
                    public String textFromValue(double val) {
                        return Double.toString(val);
                    }
                };
                if ("double".equals(typeName) || "java.lang.Double".equals(typeName)) {
                    doubleEditor.setRange(-Double.MAX_VALUE, Double.MAX_VALUE);
                } else {
                    doubleEditor.setRange(-Float.MAX_VALUE, Float.MAX_VALUE);
                }
                doubleEditor.setDecimals(9);
                doubleEditor.valueChanged.connect(this, "valueChanged(Double)");
            }
        }
    }

    void valEditorChanged(final Object value) {
        valueChanged.emit(value);
    }

    void currentIndexChanged(final Integer index) {
        Object val = comboBox.itemData(index);
        valueChanged.emit(val);
    }

    void valueChanged(final String value) {
        valueChanged.emit(value);
    }

    void valueChanged(final Integer value) {
        final Object val = getIntValue(value);
        valueChanged.emit(val);
    }

    void valueChanged(final Double value) {
        final Object val = getDoubleValue(value);
        valueChanged.emit(val);
    }
    
    private Object getStrValue(final String value) {
        if (value == null || value.isEmpty() && strEditor.maxLength() == 1) {
            return Character.MIN_VALUE;
        } else {
            return value;
        }
    }
    
    private Object getIntValue(final Integer value) {
        return booleanEditor != null ? (value > 0) : Long.valueOf(value.longValue());
    }
    
    private Object getDoubleValue(final Double value) {
        return doubleEditor.decimals() == 0 ? Long.valueOf(value.longValue()) : BigDecimal.valueOf(value.doubleValue());
    }

    boolean isSet() {
        return getEditor() != null;
    }
    
    Object getValue() {
        if (valEditor != null) {
            return valEditor.getValue();
        }
        if (strEditor != null) {
            return getStrValue(strEditor.text());
        }
        if (intEditor != null) {
            return getIntValue(intEditor.value());
        }
        if (doubleEditor != null) {
            return getDoubleValue(doubleEditor.value());
        }
        if (booleanEditor != null) {
            return booleanEditor.isChecked();
        }
        if (comboBox != null) {
            return comboBox.itemData(comboBox.currentIndex());
        }
        return null;
    }

    QWidget getEditor() {
        if (valEditor != null) {
            return valEditor;
        }
        if (strEditor != null) {
            return strEditor;
        }
        if (intEditor != null) {
            return intEditor;
        }
        if (doubleEditor != null) {
            return doubleEditor;
        }
        if (booleanEditor != null) {
            return booleanEditor;
        }
        if (comboBox != null) {
            return comboBox;
        }
        return null;
    }

    boolean checkInput() {
        return valEditor == null || !valEditor.isEnabled() ? true : valEditor.checkInput();
    }

    void setEditorEnabled(final boolean enabled) {
        final QWidget editor = getEditor();
        if (editor != null) {
            editor.setEnabled(enabled);

            if (enabled && editor instanceof QComboBox) {
                QComboBox cBox = (QComboBox) editor;
                cBox.currentIndexChanged.emit(cBox.currentIndex());
            }
        }
    }

    @SuppressWarnings("unchecked")
    void setValue(final Object value) {
        if (valEditor != null) {
            valEditor.setValue(value);
        } else if (strEditor != null) {
            final String sValue = value == null ? "" : value.toString();
            strEditor.setText(sValue);
        } else if (intEditor != null) {
            final Integer iValue = value == null ? 0 : Integer.valueOf(((Long) value).intValue());
            intEditor.setValue(iValue);
        } else if (doubleEditor != null) {
            final Double dValue = value == null ? 0 : Double.parseDouble(value.toString());
            doubleEditor.setValue(dValue);
        } else if (booleanEditor != null) {
            final Boolean bValue = value instanceof Boolean ? (Boolean) value : false;
            booleanEditor.setChecked(bValue);
        } else if (comboBox != null) {
            //Boolean bValue=value instanceof Boolean? (Boolean)value : false;
            final int index = comboBox.findData(String.valueOf(value), ItemDataRole.UserRole);
            comboBox.setCurrentIndex(index);
        }
    }
}
