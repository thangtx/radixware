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

import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;


public class ValPropEdit_Dialog extends ExplorerDialog {

    protected ValEditor valEditor;
    private boolean isMandatory;
    protected ISqmlColumnDef prop;
    protected List<Object> val = new ArrayList<>();

    public ValPropEdit_Dialog(final IClientEnvironment environment, final QWidget parent, final ISqmlColumnDef prop, final String dialogTitle, final boolean isMandatory) {
        super(environment, parent, dialogTitle);
        this.isMandatory = isMandatory;
        this.setWindowTitle(dialogTitle);
        this.prop = prop;
        valEditor = createValEditor();
        valEditor.setObjectName("valEditor");
    }

    public ValPropEdit_Dialog(final IClientEnvironment environment, final QWidget parent, final ISqmlColumnDef prop, final String dialogTitle) {
        super(environment, parent, dialogTitle);
        this.setWindowTitle(dialogTitle);
        this.prop = prop;
    }

    protected final ValEditor createValEditor() {
        final EValType type = prop.getType();
        final ValEditor editor;

        if (type == EValType.ANY/* && (prop.getEditMask() instanceof EditMaskStr)*/) {
            editor = new ValStrEditor(getEnvironment(), this);//, (EditMaskStr)prop.getEditMask(), isMandatory, false);
            //editor.setEditMask(null);Value(prop);
        } else if (type == EValType.BOOL) {//added  by dkanatova radix-7108
            editor = new ValBoolEditor(getEnvironment(), this);
        } else {
            editor = ValEditor.createForValType(getEnvironment(), type, prop.getEditMask(), isMandatory, false, this);
        }
        if (type == EValType.PARENT_REF) {
            try {
                if (prop.getSelectorPresentationId() != null && prop.getSelectorPresentationClassId() != null) {
                    ((ValRefEditor) editor).setSelectorPresentation(prop.getSelectorPresentationClassId(), prop.getSelectorPresentationId());
                }
            } catch (DefinitionError e) {
                getEnvironment().processException(e);
            }
        }
        if (type.isArrayType()) {
            editor.setVisible(false);
        }
        return editor;
        /* if (type == EValType.BOOL) {
         return new ValBoolEditor(getEnvironment(), this, prop.getEditMask(), true, false);
         }
         if (type == EValType.CHAR) {
         if (prop.getEditMask() instanceof EditMaskConstSet) {
         return new ValConstSetEditor(getEnvironment(), this, (EditMaskConstSet) prop.getEditMask(), isMandatory, false);
         }
         return new ValCharEditor(getEnvironment(), this, prop.getEditMask(), isMandatory, false);
         }
         if ((type == EValType.BIN) || (type == EValType.BLOB)) {
         return new ValBinEditor(getEnvironment(), this, (EditMaskNone) prop.getEditMask(), isMandatory, false);
         }
         if (type == EValType.DATE_TIME) {
         if (prop.getEditMask() instanceof EditMaskDateTime) {
         return new ValDateTimeEditor(getEnvironment(), this, (EditMaskDateTime) prop.getEditMask(), isMandatory, false);
         }
         if (prop.getEditMask() instanceof EditMaskTimeInterval) {
         return new ValTimeIntervalEditor(getEnvironment(), this, (EditMaskTimeInterval) prop.getEditMask(), isMandatory, false);
         }
         }
         if (type == EValType.INT) {
         if (prop.getEditMask() instanceof EditMaskInt) {
         return new ValIntEditor(getEnvironment(), this, (EditMaskInt) prop.getEditMask(), isMandatory, false);
         }
         if (prop.getEditMask() instanceof EditMaskTimeInterval) {
         return new ValTimeIntervalEditor(getEnvironment(), this, (EditMaskTimeInterval) prop.getEditMask(), isMandatory, false);
         }
         if (prop.getEditMask() instanceof EditMaskConstSet) {
         return new ValConstSetEditor(getEnvironment(), this, (EditMaskConstSet) prop.getEditMask(), isMandatory, false);
         }
         }
         if (type == EValType.NUM) {
         return new ValNumEditor(getEnvironment(), this, (EditMaskNum) prop.getEditMask(), isMandatory, false);
         }
         if ((type == EValType.STR) || ((type == EValType.CLOB))) {
         if (prop.getEditMask() instanceof EditMaskStr) {
         return new ValStrEditor(getEnvironment(), this, (EditMaskStr) prop.getEditMask(), isMandatory, false);
         }
         if (prop.getEditMask() instanceof EditMaskConstSet) {
         return new ValConstSetEditor(getEnvironment(), this, (EditMaskConstSet) prop.getEditMask(), isMandatory, false);
         }
         }
         if (type == EValType.PARENT_REF) {
         ValRefEditor valRefEditor = new ValRefEditor(getEnvironment(), this, true, false);
         try {
         if (prop.getSelectorPresentationId() != null && prop.getSelectorPresentationClassId() != null) {
         valRefEditor.setSelectorPresentation(prop.getSelectorPresentationClassId(), prop.getSelectorPresentationId());
         }
         } catch (DefinitionError e) {
         getEnvironment().processException(e);
         }
         return valRefEditor;
         }
         if(type.isArrayType()){
         final ValEditor valEditor = new ValArrEditor(getEnvironment(), type, null, this);
         valEditor.setVisible(false);
         return valEditor;
         }
         throw new IllegalUsageError("Cannot create value editor of \"" + type.name() + "\" type");*/
    }

    protected String getDisplaiedName(final ISqmlTableDef classDef, final EDefinitionDisplayMode displayMode) {
        return classDef.getDisplayableText(displayMode);
    }

    protected String getDisplaiedName(final ISqmlColumnDef prop, final EDefinitionDisplayMode displayMode) {
        return displayMode == EDefinitionDisplayMode.SHOW_FULL_NAMES ? prop.getShortName() : prop.getDisplayableText(displayMode);
    }

    public List<Object> getValue() {
        return val;
    }

    public ISqmlColumnDef getProperty() {
        return prop;
    }
}