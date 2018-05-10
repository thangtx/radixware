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

import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;

import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IEditMaskEditorDialog;
import org.radixware.kernel.common.client.widgets.IEditMaskEditorFactory;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public final class ValEditMaskEditor extends ValEditor<EditMask> {

    private RadEnumPresentationDef enumDef;
    private EEditMaskType maskType;
    private EValType valType;
    private EnumSet<EEditMaskOption> hiddenOptions, disabledOptions;
    private QToolButton editBtn;

    private ValEditMaskEditor(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, new EditMaskNone(), true, false);
        getEditMask().setNoValueStr("<" + getEnvironment().getMessageProvider().translate("EditMask", "Default") + ">");
        getLineEdit().setReadOnly(true);
        final QAction runEditMaskEditorAction = new QAction(this);
        runEditMaskEditorAction.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));
        runEditMaskEditorAction.triggered.connect(this, "runEditMaskEditor()");
        editBtn = addButton("", runEditMaskEditorAction);
        editBtn.setObjectName("rx_tbtn_edit");
    }        

    public ValEditMaskEditor(final IClientEnvironment environment, final QWidget parent, final RadEnumPresentationDef enumDef) {
        this(environment, parent);
        this.enumDef = enumDef;
    }    

    public ValEditMaskEditor(final IClientEnvironment environment, final QWidget parent, final EEditMaskType editMaskType) {
        this(environment, parent);
        this.maskType = editMaskType;
    }

    public ValEditMaskEditor(final IClientEnvironment environment, final QWidget parent, final EValType valType) {
        this(environment, parent);
        this.valType = valType;
    }
    
    private void updateEditBtn(){
        editBtn.setVisible(maskType!=EEditMaskType.BOOL);
    }

    public void setValType(final EValType valType) {
        this.valType = valType;
        enumDef = null;
        maskType = null;
        setValue(null);
    }

    public void setMaskType(final EEditMaskType maskType) {
        this.maskType = maskType;
        valType = null;
        enumDef = null;
        setValue(null);
    }

    public void setEnumDef(final RadEnumPresentationDef enumDef) {
        this.enumDef = enumDef;
        valType = null;
        maskType = null;
        setValue(null);
    }

    public EValType getValType() {
        return valType;
    }

    public EEditMaskType getEditMaskType() {
        return maskType;
    }

    public RadEnumPresentationDef getEnumDef() {
        return enumDef;
    }

    public void runEditMaskEditor() {
        final IEditMaskEditorFactory factory = getEnvironment().getApplication().getEditMaskEditorFactory();
        final IEditMaskEditorDialog dialog;
        if (enumDef != null) {
            dialog = factory.newEditMaskConstSetEditorDialog(getEnvironment(), this, enumDef);
        } else if (maskType != null) {
            dialog = factory.newEditMaskEditorDialog(getEnvironment(), this, maskType);
        } else {
            final EValType actualValType = valType.isArrayType() ? valType.getArrayItemType() : valType;
            dialog = factory.newEditMaskEditorDialog(getEnvironment(), this, actualValType);
        }
        if (super.getValue() != null) {
            dialog.setEditMask(getValue());
        }
        if (hiddenOptions != null) {
            dialog.setHiddenOptions(hiddenOptions);
        }
        if (disabledOptions != null) {
            dialog.setDisabledOptions(disabledOptions);
        }
        dialog.setReadOnly(isReadOnly());
        if (dialog.execDialog(this) != DialogResult.REJECTED) {
            setValue(dialog.getEditMask());
        }
    }

    @Override
    protected boolean isButtonCanChangeValue(final QAbstractButton button) {
        return !isReadOnly();
    }

    public void setHiddenOptions(final EnumSet<EEditMaskOption> options) {
        hiddenOptions = options;
    }

    public void setDisabledOptions(final EnumSet<EEditMaskOption> options) {
        disabledOptions = options;
    }

    @Override
    public EditMask getValue() {
        return super.getValue() == null ? getDefaultEditMask() : EditMask.newCopy(super.getValue());
    }

    @Override
    public void setValue(EditMask value) {
        if (value == null) {
            super.setValue(null);
        } else {
            final IClientApplication application = getEnvironment().getApplication();
            if (isDefaultEditMask(value, application)) {
                super.setValue(null);
            } else {
                super.setValue(EditMask.newCopy(value));
            }
            if (value instanceof EditMaskConstSet) {
                enumDef = ((EditMaskConstSet) value).getRadEnumPresentationDef(application);
                maskType = null;
                valType = null;
            }
        }
        updateEditBtn();
    }

    @Override
    protected String getStringToShow(Object value) {
        if (value instanceof EditMask) {
            return "<" + maskTypeTitle(getEnvironment(), ((EditMask) value).getType()) + ">";
        } else {
            return "<" + getEnvironment().getMessageProvider().translate("EditMask", "Default") + ">";
        }
    }

    private EditMask getDefaultEditMask() {
        if (enumDef != null) {
            return new EditMaskConstSet(enumDef.getId());
        } else if (maskType != null) {
            return EditMask.newInstance(maskType);
        } else {
            return EditMask.newInstance(valType);
        }
    }

    private static boolean isDefaultEditMask(final EditMask editMask, final IClientApplication application) {
        final org.radixware.schemas.editmask.RadixEditMaskDocument actualEditMask =
                org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
        editMask.writeToXml(actualEditMask.addNewRadixEditMask());
        final org.radixware.schemas.editmask.RadixEditMaskDocument defaultEditMask =
                org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
        final EditMask defEditMask;
        if (editMask instanceof EditMaskConstSet) {
            defEditMask =
                    new EditMaskConstSet(((EditMaskConstSet) editMask).getRadEnumPresentationDef(application).getId());
        } else {
            defEditMask = EditMask.newInstance(editMask.getType());
        }
        defEditMask.writeToXml(defaultEditMask.addNewRadixEditMask());
        return actualEditMask.xmlText().equals(defaultEditMask.xmlText());
    }

    private static String maskTypeTitle(final IClientEnvironment environment, final EEditMaskType maskType) {
        switch (maskType) {
            case DATE_TIME:
                return environment.getMessageProvider().translate("EditMask", "Date/time");
            case ENUM:
                return environment.getMessageProvider().translate("EditMask", "Enumeration");
            case INT:
                return environment.getMessageProvider().translate("EditMask", "Integer number");
            case NUM:
                return environment.getMessageProvider().translate("EditMask", "Real number");
            case LIST:
                return environment.getMessageProvider().translate("EditMask", "List");
            case TIME_INTERVAL:
                return environment.getMessageProvider().translate("EditMask", "Time interval");
            case STR:
                return environment.getMessageProvider().translate("EditMask", "String");
            case BOOL:
                return environment.getMessageProvider().translate("EditMask", "Boolean");
            case FILE_PATH:
                return environment.getMessageProvider().translate("EditMask", "FilePath");
            case OBJECT_REFERENCE:
                return environment.getMessageProvider().translate("EditMask", "Parent Reference");
            default:
                return "Unknown Mask";
        }
    }
}
