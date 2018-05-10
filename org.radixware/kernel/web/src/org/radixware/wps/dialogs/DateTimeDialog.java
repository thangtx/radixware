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

package org.radixware.wps.dialogs;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import org.radixware.kernel.common.client.dialogs.IDateTimeDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ImageManager;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.ValueEditor.ValueChangeListener;
import org.radixware.wps.rwt.*;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;
import org.radixware.wps.views.editors.valeditors.ValTimeEditorController;


public class DateTimeDialog extends Dialog implements IDateTimeDialog {
    
    private static class Icons extends ClientIcon.CommonOperations {
        
        private Icons(final String fileName) {
            super(fileName, true);
        }
        
        public static final ClientIcon CURRENT_TIME = new Icons("classpath:images/current_time.svg");
    }
    
    //private final static int CALENDAR_WIDGET_WIDTH = 225;
    private final static int CALENDAR_WIDGET_HEIGHT = 246;
    
    private final static Long NOON_TIME_MILLIS = Long.valueOf(12l * 60l * 60l * 1000l);

    private final CalendarWidget calendarWidget;
    private final ValTimeEditorController timeEditorController;
    private final ValListEditorController<String> ampmEditorController;
    private final EditMaskDateTime editMask;
    private final boolean showTime;
    private final Timestamp initialTimestamp;
    private PushButton pbReset, pbOk;
    private boolean internalChange;

    public DateTimeDialog(final WpsEnvironment env, final EditMaskDateTime editMask, final Timestamp initial) {
        super(env.getDialogDisplayer(), "", false);
        this.editMask = editMask;
        showTime = editMask.timeFieldPresent(env);
        calendarWidget = new CalendarWidget(env);        
        if (showTime){
            timeEditorController = new ValTimeEditorController(env){

                @Override
                protected String calcFocusedText(final Long value, final EditMaskDateTime editMask) {
                    final String inputText =  super.calcFocusedText(value, editMask);
                    if (editMask==null){
                        return inputText;
                    }
                    if (editMask.halfDayFieldPresent(getEnvironment())){
                        return inputText.substring(0, inputText.length()-3);
                    }else{
                        return inputText;
                    }
                }

                @Override
                protected String getDisplayString(final Long value) {
                    final String text = super.getDisplayString(value);
                    final EditMaskDateTime editMask = getEditMask();
                    if (editMask!=null && value!=null && editMask.halfDayFieldPresent(env)){
                        return text.substring(0, text.length()-3);
                    }else{
                        return text;
                    }
                }
                
                @Override
                protected Long parseInput(final String inputText) throws InputBox.InvalidStringValueException {
                    if (getEditMask()!=null && getEditMask().halfDayFieldPresent(env)){
                        return super.parseInput(inputText+" "+ampmEditorController.getValue());
                    }else{
                        return super.parseInput(inputText);
                    }
                }

                @Override
                protected String getInputMaskString() {
                    final String inputMask = super.getInputMaskString().replace(" AM", "");
                    return inputMask.replace("AM", "");
                }
            };
            
            final EditMaskList list = new EditMaskList();
            list.addItem("AM", "AM");
            list.addItem("PM", "PM");
            ampmEditorController = new ValListEditorController<>(env, list);
        } else {
            timeEditorController = null;
            ampmEditorController = null;
        }        
        initialTimestamp = initial;
        setupUi();
        if (initialTimestamp != null) {
            setCurrentDateTime(initialTimestamp);
        }
        updateButtons();
    }

    private void setupUi() {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        final TableLayout layout = new TableLayout();        
        final ImageManager imageManager = getEnvironment().getApplication().getImageManager();
        {
            final TableLayout buttonsLayout = new TableLayout();
            final TableLayout.Row buttonsContainer = buttonsLayout.addRow();
            final PushButton pbNow;
            if (showTime) {
                pbNow = new PushButton(mp.translate("DateTimeDialog", "Now"));
                pbNow.setToolTip(mp.translate("DateTimeDialog", "Set Current Date/Time"));
            } else {
                pbNow = new PushButton(mp.translate("DateTimeDialog", "Now"));
                pbNow.setToolTip(mp.translate("DateTimeDialog", "Set Current Date"));
            }
            buttonsContainer.addCell().add(pbNow);
            pbNow.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(IButton source) {
                    setCurrentDateTime(getEnvironment().getCurrentServerTime());
                }
            });
            pbNow.setIcon(imageManager.getIcon(Icons.CURRENT_TIME));
            buttonsContainer.addSpace().setHSizePolicy(SizePolicy.EXPAND);
            pbReset = new PushButton(mp.translate("DateTimeDialog", "Reset"));
            pbReset.setToolTip(mp.translate("DateTimeDialog", "Reset Current Value to Initial"));            
            buttonsContainer.addCell().add(pbReset);
            pbReset.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(IButton source) {
                    if (initialTimestamp != null) {
                        setCurrentDateTime(initialTimestamp);
                    }
                }
            });
            pbReset.setIcon(imageManager.getIcon(Icons.UNDO));
            layout.addRow().addCell().add(buttonsLayout);
            layout.addVerticalSpace();
        }
        if (showTime) {
            setWindowTitle(mp.translate("DateTimeDialog", "Date and Time"));
            final TableLayout.Row.Cell calendarWidgetCell = layout.addRow().addCell();            
            calendarWidgetCell.add(calendarWidget);
            
            final UIObject timeEditor = (UIObject) timeEditorController.getValEditor();
            /*final LabeledUIObject leTime =
                    new LabeledUIObject(mp.translate("DateTimeDialog", "Time") + ":", timeEditor);*/
            timeEditorController.setEditMask(editMask);
            timeEditorController.setMandatory(true);
            timeEditorController.addValueChangeListener(new ValueChangeListener<Long>() {
                @Override
                public void onValueChanged(final Long oldValue, final Long newValue) {                    
                        updateButtons();
                    if (!internalChange){
                        updateAmPm();
                    }
                }
            });
            ampmEditorController.setMandatory(true);
            ampmEditorController.addValueChangeListener(new ValueChangeListener<String>() {
                @Override
                public void onValueChanged(final String oldValue, final String newValue) {
                    onAmPmChange(newValue);
                }
            });
            layout.addVerticalSpace();
            final TableLayout.Row.Cell timeEditorCell = layout.addRow().addCell();            
            final TableLayout timeEditorLayout = new TableLayout();
            //timeEditorContainer.setHeight(20);
            timeEditorCell.setAutoExpandContent(true);
            timeEditorCell.add(timeEditorLayout);
            final TableLayout.Row row = timeEditorLayout.addRow();
            row.addCell().add(new Label(mp.translate("DateTimeDialog", "Time") + ":"));
            row.addSpace();
            row.addCell().add(timeEditor);
            final UIObject amPmEditor = (UIObject)ampmEditorController.getValEditor();                  
            amPmEditor.setPreferredWidth(50);
            amPmEditor.setWidth(50);
            amPmEditor.setVisible(isAmPm());
            row.addSpace();
            row.addCell().add(amPmEditor);
            updateAmPm();
            setHeight(353);
        } else {
            setWindowTitle(mp.translate("DateTimeDialog", "Date"));
            final TableLayout.Row.Cell calendarWidgetCell = layout.addRow().addCell();
            calendarWidgetCell.add(calendarWidget);
            setHeight(330);
        }
        setDateRange(editMask.getMinimumTime(), editMask.getMaximumTime());
        layout.setLeft(2);
        layout.setTop(1);
        layout.getAnchors().setRight(new Anchors.Anchor(1, -3));
        layout.getAnchors().setBottom(new Anchors.Anchor(1, -1));
        //layout.setWidth(CALENDAR_WIDGET_WIDTH);
        //layout.getHtml().setCss("max-width", CALENDAR_WIDGET_WIDTH+"px");
        add(layout);
        pbOk = addCloseAction(EDialogButtonType.OK);
        pbOk.setDefault(true);
        final PushButton pbCancel = addCloseAction(EDialogButtonType.CANCEL);
        calendarWidget.addDateChangeListener(new CalendarWidget.DateChangeListener() {

            @Override
            public void dateChanged(final Date oldDate, final Date newDate) {
                updateButtons();
                if (pbOk.isEnabled()) {
                    pbOk.setFocused(true);
                } else {
                    pbCancel.setFocused(true);
                }
            }
        });
        calendarWidget.setMinimumHeight(CALENDAR_WIDGET_HEIGHT);
        //calendarWidget.setMinimumWidth(CALENDAR_WIDGET_WIDTH);        
        //calendarWidget.getHtml().setCss("max-width", CALENDAR_WIDGET_WIDTH+"px");
        setWidth(226);
        
        addBeforeCloseListener(new BeforeCloseButtonListener() {

            @Override
            public boolean beforeClose(final EDialogButtonType button, final DialogResult result) {
                if (result==DialogResult.ACCEPTED && showTime && !timeEditorController.checkInput()){
                    return false;
                }
                updateButtons();
                return result != DialogResult.ACCEPTED || pbOk.isEnabled();
            }
        });
        
        setResizable(false);
    }
    
    private boolean isAmPm(){
        return editMask.halfDayFieldPresent(getEnvironment());
    }
    
    private void updateAmPm(){
        if (isAmPm()){
            final Long value = timeEditorController.getValue();
            if (value!=null && timeEditorController.hasAcceptableInput()){
                internalChange = true;                
                try{
                    ampmEditorController.setValue(value>=NOON_TIME_MILLIS ? "PM" : "AM");
                }finally{
                    internalChange = false;
                }
            }
        }
    }    

    private void updateButtons() {
        final boolean isTimeValid =
                timeEditorController == null || timeEditorController.getValidationResult() == ValidationResult.ACCEPTABLE;
        if (isTimeValid) {
            final Timestamp current = getCurrentDateTime();
            final boolean isValid = editMask.validate(getEnvironment(), current)==ValidationResult.ACCEPTABLE;//NOPMD
            pbOk.setEnabled(isValid);
            pbReset.setEnabled(initialTimestamp != null && current.getTime() != initialTimestamp.getTime());
        } else {
            pbOk.setEnabled(false);
            pbReset.setEnabled(initialTimestamp != null);
        }
    }
    
    private void onAmPmChange(final String newValue){
        if (!internalChange){
            final Long value = timeEditorController.getValue();
            if (value!=null && timeEditorController.hasAcceptableInput()){
                final boolean isPm = "PM".equals(newValue);
                internalChange = true;
                try{
                    if (value<NOON_TIME_MILLIS && isPm){
                        timeEditorController.setValue(value+NOON_TIME_MILLIS);
                    }else if (value>=NOON_TIME_MILLIS && !isPm){
                        timeEditorController.setValue(value-NOON_TIME_MILLIS);
                    }
                }finally{
                    internalChange = false;
                }
            }
            
        }
    }

    @Override
    public final Timestamp getCurrentDateTime() {
        final long date = calendarWidget.getCurrentDate().getTime();
        if (showTime && timeEditorController.getValue() != null) {
            return new Timestamp(timeEditorController.getValue().longValue() + date);
        }
        return new Timestamp(date);
    }

    @Override
    public final void setCurrentDateTime(final Timestamp currentDateTime) {
        calendarWidget.setCurrentDate(currentDateTime);
        if (showTime) {
            timeEditorController.setTime(currentDateTime);            
        }
    }

    @Override
    public final Timestamp getMaxDate() {
        return new Timestamp(calendarWidget.getMaxDate().getTime());
    }

    @Override
    public final void setMaxDate(final Timestamp maxDate) {
        calendarWidget.setMaxDate(maxDate);
    }

    @Override
    public final void setMaxDate(final Calendar calendar) {
        calendarWidget.setMaxDate(calendar);
    }

    @Override
    public final Timestamp getMinDate() {
        return new Timestamp(calendarWidget.getMinDate().getTime());
    }

    @Override
    public final void setMinDate(final Timestamp minDate) {
        calendarWidget.setMinDate(minDate);
    }

    @Override
    public final void setMinDate(final Calendar calendar) {
        calendarWidget.setMinDate(calendar);
    }

    @Override
    public final void setDateRange(final Timestamp minDate, final Timestamp maxDate) {
        calendarWidget.setDateRange(minDate, maxDate);
    }

    @Override
    public final void setDateRange(final Calendar minDate, final Calendar maxDate) {
        calendarWidget.setDateRange(minDate, maxDate);
    }
}
