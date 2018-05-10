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
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.dialogs.DateTimeDialog;
import static org.radixware.kernel.explorer.editors.valeditors.ValEditor.createAction;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

public class ValDateTimeEditor extends AbstractDateTimeEditor<Timestamp> {        
    
    private final QToolButton setCurrentDateButton = 
        addButton(null, createAction(this, "onSetCurrentDateClick()", "set_current_date_time"));
    private final QToolButton changeButton = 
        addButton(null, createAction(this, "onChangeValueClick()", "edit"));
    private static class Icons extends ClientIcon.CommonOperations {
        
        private Icons(final String fileName) {
            super(fileName, true);
        }
        
        public static final ClientIcon CURRENT_TIME = new Icons("classpath:images/current_time.svg");
    }
    
    public ValDateTimeEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskDateTime editMaskDateTime,
            final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMaskDateTime, mandatory, readOnly);        
        changeButton.setToolTip(environment.getMessageProvider().translate("ValDateTimeEditor", "Edit"));
        changeButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.ValueTypes.DATE_TIME));        
        changeButton.setVisible(!readOnly);
        setCurrentDateButton.setIcon(ExplorerIcon.getQIcon(Icons.CURRENT_TIME));
        setCurrentDateButton.setVisible(!readOnly);
    }

    public ValDateTimeEditor(final IClientEnvironment environment, final QWidget parent){
        this(environment,parent,new EditMaskDateTime(),true,false);
    }

    @SuppressWarnings("unused")
    private void onChangeValueClick() {
        final IClientEnvironment environmnet = getEnvironment();
        final EditMaskDateTime mask = (EditMaskDateTime) getEditMask();
        final DateTimeDialog dialog= 
            new DateTimeDialog(environmnet, parentWidget() == null ? Application.getMainWindow() : parentWidget());
        final String dialogTitle = getDialogTitle();
        if (dialogTitle!=null){
            dialog.setWindowTitle(dialogTitle);
        }
        dialog.setDateRange(mask.getMinimumTime(), mask.getMaximumTime());
        dialog.setCurrentDateTime(getValue());
        dialog.setTimeFieldVisible(mask.timeFieldPresent(environmnet));
        final String timeFormat = mask.getInputTimeFormat(environmnet);
        dialog.setTimeDisplayFormat(timeFormat);
        dialog.setMandatory(isMandatory());
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            final Timestamp newValue = dialog.getCurrentDateTime();
            setTime(newValue, mask);
        }
    }
    
    @SuppressWarnings("unused")
    private void onSetCurrentDateClick() {
        final EditMaskDateTime mask = (EditMaskDateTime) getEditMask();
        final Timestamp currentTime = getEnvironment().getCurrentServerTime();
        setTime(currentTime, mask);
    }

    @Override
    protected String getInputText(final Timestamp value) {
        final EditMaskDateTime mask = (EditMaskDateTime)getEditMask();
        return mask.getInputTextForValue(value, getEnvironment());
    }
    
    @Override
    public void setReadOnly(final boolean readOnly) {
        changeButton.setVisible(!readOnly);
        setCurrentDateButton.setVisible(!readOnly);
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
    
    private void setTime(final Timestamp time, final EditMaskDateTime mask) {
        setFocus();//значение модифицируется только когда редактор сфокусирован            
        setValue(mask.copyFields(time, getEnvironment()));                        
        setFocus();//refresh убирает фокус.
        getLineEdit().setSelection(0, 0);
        updateHistory();
        editingFinished.emit(getValue());
    }

    @Override
    public void setEditMask(EditMask editMask) {
        super.setEditMask(editMask); 
        String toolTipStr;
        IClientEnvironment env = getEnvironment();
        EditMaskDateTime editMaskDateTime = (EditMaskDateTime)editMask;
        if (editMaskDateTime.dateFieldPresent(env) && editMaskDateTime.timeFieldPresent(env)) {
            toolTipStr = env.getMessageProvider().translate("ValDateTimeEditor", "Set Current Date/Time");
        } else if (editMaskDateTime.dateFieldPresent(env)) {
            toolTipStr = env.getMessageProvider().translate("ValDateTimeEditor", "Set Current Date");
        } else {
            toolTipStr = env.getMessageProvider().translate("ValDateTimeEditor", "Set Current Time");
        }
        setCurrentDateButton.setToolTip(toolTipStr);
    }
    
}
