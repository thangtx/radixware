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

package org.radixware.wps.views.editors.valeditors;

import java.sql.Timestamp;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.exceptions.WrongFormatException;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.DateTimeDialog;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.InputBox.ValueController;
import org.radixware.wps.rwt.ToolButton;


public class ValDateTimeEditorController extends InputBoxController<Timestamp, EditMaskDateTime> {

    private final ToolButton setCurrentDateButton = new ToolButton();
    private final ToolButton editButton = new ToolButton();
    private String dialogTitle;
    
    private static class Icons extends ClientIcon.CommonOperations {
        
        private Icons(final String fileName) {
            super(fileName, true);
        }
        
        public static final ClientIcon CURRENT_TIME = new Icons("classpath:images/current_time.svg");
    }
    
    public ValDateTimeEditorController(final IClientEnvironment env, final LabelFactory factory) {
        super(env,factory);
        setEditMask(new EditMaskDateTime());
        setCurrentDateButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(Icons.CURRENT_TIME));
        setCurrentDateButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(IButton source) {
                    setCurrentDate();
            }
        });
        addButton(setCurrentDateButton);
        editButton.setToolTip(env.getMessageProvider().translate("ValDateTimeEditor", "Edit"));
        final Icon dateTimeIcon =
                env.getApplication().getImageManager().getIcon(ClientIcon.ValueTypes.DATE_TIME);
        editButton.setIcon(dateTimeIcon);
        editButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(IButton source) {
                    edit();
            }
        });
        editButton.setObjectName("tbEdit");
        addButton(editButton);        
    }

    public ValDateTimeEditorController(final IClientEnvironment env) {
        this(env, null);
    }

    private void updateEditButton() {
        editButton.setVisible(!isReadOnly() && getEditMask().dateFieldPresent(getEnvironment()));
        editButton.setEnabled(this.isEnabled());
    }

    @Override
    protected void afterChangeReadOnly() {
        super.afterChangeReadOnly();
        updateEditButton();
        updateSetCurrentDateButton();
    }

    @Override
    public void setEditMask(EditMaskDateTime editMask) {
        super.setEditMask(editMask);
        updateEditButton();
        updateSetCurrentDateButton();
    }

    @Override
    protected ValueController<Timestamp> createValueController() {
        return new ValueController<Timestamp>() {
            @Override
            public Timestamp getValue(final String text) throws InvalidStringValueException {
                final EditMaskDateTime maskDateTime = getEditMask();
                try {
                    return maskDateTime.getValueForInputText(text, getEnvironment());
                } catch (WrongFormatException ex) {
                    final MessageProvider mp =
                            ValDateTimeEditorController.this.getEnvironment().getMessageProvider();
                    final String reason;
                    if (ex instanceof IClientError){//NOPMD
                        reason = ((IClientError)ex).getLocalizedMessage(mp);
                    }else{
                        reason = ex.getMessage();
                    }
                    if (reason==null || reason.isEmpty()){
                        throw new InvalidStringValueException(mp, InvalidValueReason.Factory.createForWrongFormatValue(getEnvironment()));//NOPMD
                    }else{
                        throw new InvalidStringValueException(mp, InvalidValueReason.Factory.createForInvalidValue(reason));//NOPMD
                    }
                }
            }
        };
    }

    @Override
    protected String calcFocusedText(final Timestamp value, final EditMaskDateTime editMask) {
        return editMask.getInputTextForValue(value, getEnvironment());
    }

    public void edit() {
        final DateTimeDialog dateTimeDialog =
                new DateTimeDialog((WpsEnvironment) getEnvironment(), getEditMask(), getValue());
        if (dialogTitle != null && !dialogTitle.isEmpty()) {
            dateTimeDialog.setWindowTitle(dialogTitle);
        }
        if (dateTimeDialog.execDialog() == DialogResult.ACCEPTED) {
            setValue(dateTimeDialog.getCurrentDateTime());
        }
    }
    
    private void setCurrentDate() {
        final Timestamp currentTime = getEnvironment().getCurrentServerTime();
        setValue(currentTime);
    }
    
    private void updateSetCurrentDateButton() {
        setCurrentDateButton.setVisible(!isReadOnly());
        setCurrentDateButton.setEnabled(this.isEnabled());
        String toolTipStr;
        IClientEnvironment env = getEnvironment();
        EditMaskDateTime editMaskDateTime = getEditMask();
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
