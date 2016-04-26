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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWindowsStyle;
import java.sql.Timestamp;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeController;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.editors.valeditors.ValDateTimeEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;



public final class TraceFilterToolBar extends QToolBar {

    public final Signal0 saveTrace = new Signal0();
    public final Signal0 clearTrace = new Signal0();
    public final Signal0 showFindDialog = new Signal0();
    public final Signal0 findNext = new Signal0();
    public final Signal0 severityChanged = new Signal0();
    public final Signal1<Timestamp> begTimeChanged = new Signal1<>();
    public final Signal1<Timestamp> endTimeChanged = new Signal1<>();
    private final IClientEnvironment environment;
    private final QComboBox severityComboBox = new QComboBox();

    public TraceFilterToolBar(final IClientEnvironment environment, final QWidget parentWidget) {
        super(parentWidget);
        this.environment = environment;
        setupUi();
    }    

    public EEventSeverity getSeverity() {
        final int currentIndex = severityComboBox.currentIndex();
        if (currentIndex>=0){
            try{
                return EEventSeverity.getForName((String)severityComboBox.itemData(currentIndex));
            }
            catch(NoConstItemWithSuchValueError error){
                return EEventSeverity.DEBUG;
            }            
        }
        return EEventSeverity.DEBUG;
    }

    private void setupUi() {
        this.layout().setMargin(0);
        final QWidget toolBarContent = new QWidget();
        toolBarContent.setObjectName("Rdx.TraceFilterToolBar.toolBarContent");
        this.addWidget(toolBarContent);

        QHBoxLayout hBoxLayout = new QHBoxLayout();
        toolBarContent.setLayout(hBoxLayout);
        hBoxLayout.setWidgetSpacing(5);
        hBoxLayout.setMargin(3);

        QLabel filterLabel = new QLabel(Application.translate("TraceDialog", "Filter"));
        hBoxLayout.addWidget(filterLabel);

        severityComboBox.setMaximumWidth(200);
        severityComboBox.setMinimumWidth(120);
        severityComboBox.setToolTip(Application.translate("TraceDialog", "Filter"));        
        severityComboBox.currentIndexChanged.connect(this, "indexChanged()");
        final QStyle style = new QWindowsStyle();
        style.setParent(this);
        severityComboBox.setStyle(style);
        //final String current        
        final String settingKey = SettingNames.SYSTEM + "/TraceDialog/TraceFilter";
        updateEventSeverityItems(environment.getConfigStore().readInteger(settingKey, -1));
        hBoxLayout.addWidget(severityComboBox);
        hBoxLayout.addSpacing(5);

        QLabel timeLabel = new QLabel(Application.translate("TraceDialog", "Time: from"));
        hBoxLayout.addWidget(timeLabel);

        EditMaskDateTime mask = new EditMaskDateTime("HH:mm:ss dd/MM/yy", null, null);

        ValDateTimeEditor begTime = new ValDateTimeEditor(environment, this, mask, false, false);
        begTime.setMinimumWidth(120);
        begTime.setMaximumWidth(150);
        begTime.valueChanged.connect(begTimeChanged);


        hBoxLayout.addWidget(begTime);

        QLabel toLabel = new QLabel(Application.translate("TraceDialog", "to"));
        hBoxLayout.addWidget(toLabel);

        ValDateTimeEditor endTime = new ValDateTimeEditor(environment, this, mask, false, false);
        endTime.setMinimumWidth(120);
        endTime.setMaximumWidth(150);
        endTime.valueChanged.connect(endTimeChanged);

        hBoxLayout.addWidget(endTime);

        hBoxLayout.addStretch();

        QToolButton findButton = new QToolButton();
        hBoxLayout.addWidget(findButton);
        findButton.setAutoRaise(true);
        findButton.setToolTip(Application.translate("TraceDialog", "Find"));
        findButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FIND));
        findButton.setShortcut(QKeySequence.StandardKey.Find);
        findButton.clicked.connect(showFindDialog);

        QToolButton findNextButton = new QToolButton();
        hBoxLayout.addWidget(findNextButton);
        findNextButton.setAutoRaise(true);
        findNextButton.setToolTip(Application.translate("TraceDialog", "Find Next"));
        findNextButton.setIcon(ExplorerIcon.getQIcon(ExplorerIcon.CommonOperations.FIND_NEXT));
        findNextButton.setShortcut(QKeySequence.StandardKey.FindNext);
        findNextButton.clicked.connect(findNext);

        QToolButton saveButton = new QToolButton();
        hBoxLayout.addWidget(saveButton);
        saveButton.setAutoRaise(true);
        saveButton.setToolTip(Application.translate("TraceDialog", "Save"));
        saveButton.setIcon(ExplorerIcon.getQIcon(ExplorerIcon.CommonOperations.SAVE));
        saveButton.clicked.connect(saveTrace);

        QToolButton clearButton = new QToolButton();
        hBoxLayout.addWidget(clearButton);
        clearButton.setAutoRaise(true);
        clearButton.setToolTip(Application.translate("TraceDialog", "Clear"));
        clearButton.setIcon(ExplorerIcon.getQIcon(ExplorerIcon.CommonOperations.CLEAR));
        clearButton.clicked.connect(clearTrace);

        hBoxLayout.addSpacing(5);
    }
    
    public void updateEventSeverityItems(final int currentLevel){
        final List<TraceProfileTreeController.EventSeverity> eventSeverityItems = 
                TraceProfileTreeController.getEventSeverityItemsByOrder(environment);
        severityComboBox.blockSignals(true);
        try{
            severityComboBox.clear();
            QIcon icon;
            int currentIndex=-1;
            for (TraceProfileTreeController.EventSeverity eventSeverity: eventSeverityItems) {
                if (!Utils.equals(eventSeverity.getValue(), EEventSeverity.NONE.getName())) {                
                    icon = ExplorerIcon.getQIcon(eventSeverity.getIcon());
                    severityComboBox.addItem(icon, eventSeverity.getTitle(), eventSeverity.getValue());
                    try{
                        final EEventSeverity kernelEnum = EEventSeverity.getForName(eventSeverity.getValue());
                        if (kernelEnum.getValue().intValue()==currentLevel){
                            currentIndex = severityComboBox.count() - 1;
                        }
                    }catch(NoConstItemWithSuchValueError error){//NOPMD
                    }
                }
            }
            if (currentIndex>-1){
                severityComboBox.setCurrentIndex(currentIndex);
            }
        }finally{
            severityComboBox.blockSignals(false);
        }
    }

    @SuppressWarnings("unused")
    private void indexChanged() {
        severityChanged.emit();
    }
}
