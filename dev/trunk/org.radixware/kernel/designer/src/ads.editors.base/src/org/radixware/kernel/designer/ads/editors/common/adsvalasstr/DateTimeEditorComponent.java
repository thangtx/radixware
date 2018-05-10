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

/*
 * 10/6/11 4:50 PM
 *
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.dialogs.datetimepicker.DateAndTimePicker;
import org.radixware.kernel.common.dialogs.RadixImageDistributor;


public class DateTimeEditorComponent extends BaseEditorComponent<DateTimeEditorComponent.LocalModel> {
/*
    public static final class LocalModel extends BaseEditorComponent.BaseEditorModel<Date> {

        @Override
        protected Date toLocal(AdsValAsStr value) {
            try {
                if (value != null) {
                    return VAL_AS_STR_DATE_FORMATTER.parse(value.toString());
                }
                return null;
            } catch (ParseException ex) {
                return null;
            }
        }

        @Override
        protected AdsValAsStr toExternal(Date local) {
            if (local != null) {
                return AdsValAsStr.Factory.newInstance(VAL_AS_STR_DATE_FORMATTER.format(local));
            }
            return AdsValAsStr.NULL_VALUE;
        }

        @Override
        public void updateValue(Object... params) {
            Date value = (Date) params[0];
            setLocalValue(value, true);
        }
    }*/
/*
    private static final class AdvanceSpinnerDateModel extends SpinnerDateModel {

        private boolean undefined;

        public AdvanceSpinnerDateModel() {
            super();
        }

        public boolean isUndefined() {
            return undefined;
        }

        @Override
        public void setValue(Object value) {
            if (value == null) {
                undefined = true;
            } else {
                super.setValue(value);
                undefined = false;
            }
        }
    }*/
/*
    private static final class AdvanceDateTimeSpinner extends JSpinner {

        public AdvanceDateTimeSpinner() {
            super(new AdvanceSpinnerDateModel());

            setEditor(new JSpinner.DateEditor(this, AdsValAsStr.DefaultPresenter.DATE_FORMAT));

            ((JButton) getComponent(0)).addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (getModel().isUndefined()) {
                        setValue(getModel().getPreviousValue());
                    }
                }
            });
            ((JButton) getComponent(1)).addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (getModel().isUndefined()) {
                        setValue(getModel().getNextValue());
                    }
                }
            });
        }

        @Override
        public AdvanceSpinnerDateModel getModel() {
            return (AdvanceSpinnerDateModel) super.getModel();
        }
    }
    */
    private JFormattedTextField editor;
    private static final DateFormat VAL_AS_STR_DATE_FORMATTER = new SimpleDateFormat(AdsValAsStr.DefaultPresenter.VAL_AS_STR_DATE_FORMAT);
    public static final DateFormat PRESENTATION_DATE_FORMATTER = new SimpleDateFormat(AdsValAsStr.DefaultPresenter.DATE_FORMAT);
    private final DocumentListener listener;
    private final FocusListener focusListener;
    private DateAndTimePicker dateAndTimePicker;
    
    
    public static final class LocalModel extends BaseEditorComponent.BaseEditorModel<Date> {

        @Override
        protected Date toLocal(AdsValAsStr value) {
            try {
                if (value != null) {
                    return VAL_AS_STR_DATE_FORMATTER.parse(value.toString());
                }
                return null;
            } catch (ParseException ex) {
                return null;
            }
        }

        @Override
        protected AdsValAsStr toExternal(Date local) {
            if (local != null) {
                return AdsValAsStr.Factory.newInstance(VAL_AS_STR_DATE_FORMATTER.format(local));
            }
            return AdsValAsStr.NULL_VALUE;
        }

        @Override
        public void updateValue(Object... params) {
            
            Date value = (Date) params[0];
            setLocalValue(value, true);
        }
    }

    public DateTimeEditorComponent() {
        super(new LocalModel());
        
        dateAndTimePicker = new DateAndTimePicker(new RadixImageDistributor());
        editor = dateAndTimePicker.getEditor();

        listener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                notifyComponentChanged();
            }
        };
        
        focusListener = new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (!getModel().isSetValue()) {

                    lockModelChange();
                    updateEditorComponent();
                    unLockModelChange();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!getModel().isSetValue()) {
                    
                    lockModelChange();
                    editor.getFocusListeners()[0].focusLost(e);
                    getModel().updateValue(dateAndTimePicker.getDateAndTime());
                    dateAndTimePicker.setDate(getModel().getLocalValue());
                    updateEditorComponent();
                    unLockModelChange();
                }
            }
        };
       
        
       /* spinner = new AdvanceDateTimeSpinner();

        editor = ((JSpinner.DateEditor) spinner.getEditor()).getTextField();
        editor.setFocusLostBehavior(JFormattedTextField.PERSIST);

        listener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                notifyComponentChanged();
            }
        };
        
        focusListener = new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (!getModel().isSetValue()) {

                    lockModelChange();
                    updateEditorComponent();
                    unLockModelChange();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!getModel().isSetValue()) {

                    lockModelChange();
                    updateEditorComponent();
                    unLockModelChange();
                }
            }
        };*/
    }

    @Override
    protected void updateModelValue() {
        Date currValue;
       /*try {
            //currValue = (Date) editor.getFormatter().stringToValue(editor.getText());
            currValue = dateAndTimePicker.getDate();
        } catch (ParseException ex) {
            currValue = (Date) editor.getValue();
        }
        currValue = dateAndTimePicker.getDate();
        if(currValue == null) {
            
        } else {
            getModel().updateValue(currValue);
        }*/
        currValue = dateAndTimePicker.getDateAndTime();
        if(currValue != null) {
            getModel().updateValue(currValue);
        }
    }

    @Override
    protected void updateEditorComponent() {
        if (getModel().getLocalValue() != null) {
            //editor.setValue(getModel().getLocalValue()); //simpleDateF.format()
            dateAndTimePicker.setDate(getModel().getLocalValue());
            //getModel().updateValue(dateAndTimePicker.getDateAndTime()); //.setValue(AdsValAsStr.NULL_VALUE);
        } else {
            editor.setValue(null);
            if (isFocusOwner()) { //! setT only
                editor.setText("");
            } else {
                editor.setText(AdsValAsStr.NULL_VALUE.toString());
            }
        }
    }

    @Override
    public JPanel getEditorComponent() {
        return dateAndTimePicker;
    }

    @Override
    protected void connectEditorComponent() {
        editor.getDocument().addDocumentListener(listener);
        editor.addFocusListener(focusListener);
    }

    @Override
    protected void disconnectEditorComponent() {
        editor.getDocument().removeDocumentListener(listener);
        editor.addFocusListener(focusListener);
    }

    @Override
    public int getBaseline(int width, int height) {
        return dateAndTimePicker.getBaseline(width, height);
    }

    @Override
    public boolean isFocusOwner() {
        return editor.isFocusOwner();
    }

    @Override
    public void requestFocus() {
        editor.requestFocus();
    }
}