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

import java.sql.Timestamp;

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.dialogs.DateTimeDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

public class ValDateTimeEditor extends AbstractDateTimeEditor<Timestamp> {        
    private String dialogTitle;
    private final QToolButton changeButton = addButton(null, createAction(this, "onChangeValueClick()"));   
    
    public ValDateTimeEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskDateTime editMaskDateTime,
            final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMaskDateTime, mandatory, readOnly);        
        changeButton.setToolTip(environment.getMessageProvider().translate("ValDateTimeEditor", "Edit"));
        changeButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.ValueTypes.DATE_TIME));
        changeButton.setVisible(!readOnly);
    }

    public ValDateTimeEditor(final IClientEnvironment environment, final QWidget parent){
        this(environment,parent,new EditMaskDateTime(),true,false);
    }

    @SuppressWarnings("unused")
    private void onChangeValueClick() {
        final java.util.Locale locale = getEnvironment().getLocale();
        final EditMaskDateTime mask = (EditMaskDateTime) getEditMask();
        final DateTimeDialog dialog= 
            new DateTimeDialog(getEnvironment(), parentWidget() == null ? Application.getMainWindow() : parentWidget());
        if (dialogTitle!=null){
            dialog.setWindowTitle(dialogTitle);
        }
        dialog.setDateRange(mask.getMinimumTime(), mask.getMaximumTime());
        dialog.setCurrentDateTime(getValue());
        dialog.setTimeFieldVisible(mask.timeFieldPresent(locale));
        final String timeFormat = mask.getInputTimeFormat(locale);
        dialog.setTimeDisplayFormat(timeFormat);
        dialog.setMandatory(isMandatory());
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            final Timestamp newValue = dialog.getCurrentDateTime();
            setFocus();//значение модифицируется только когда редактор сфокусирован            
            setValue(mask.copyFields(newValue, locale));                        
            setFocus();//refresh убирает фокус.
            getLineEdit().setSelection(0, 0);
            updateHistory();
            editingFinished.emit(getValue());
        }
    }
    
    public void setDialogTitle(final String newTitle){
        dialogTitle = newTitle;
    }
    
    public String getDialogTitle(){
        return dialogTitle;
    }

    @Override
    protected String getInputText(final Timestamp value) {
        final EditMaskDateTime mask = (EditMaskDateTime)getEditMask();
        return mask.getInputTextForValue(value, getEnvironment().getLocale());
    }
    
    @Override
    public void setReadOnly(final boolean readOnly) {
        changeButton.setVisible(!readOnly);        
        super.setReadOnly(readOnly);
    }
        
    @Override
    protected String valueAsStr(final Timestamp value) {
        return ValAsStr.toStr(value, EValType.DATE_TIME);
    }

    @Override
    protected Timestamp stringAsValue(final String stringVal) {        
        return (Timestamp) ValAsStr.fromStr(stringVal, EValType.DATE_TIME);
    }    

    @Override
    protected Timestamp getTimestampFromValue(final Timestamp value) {
        return value;
    }

    @Override
    protected Timestamp getValueFromTimestamp(final Timestamp timestamp) {
        return timestamp;
    }
}