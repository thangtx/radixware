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

import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;


final class SearchDialog extends ExplorerDialog {
    
    private final static String SEARCH_SETTINGS_KEY = SettingNames.SYSTEM+"/TraceDialog/SearchSettings";
    private final static String DIRECTION_KEY = SEARCH_SETTINGS_KEY+"/direction";
    private final static String FORWARD_DIRECTION_VALUE = "forward";
    private final static String BACKWARD_DIRECTION_VALUE = "backward";
    private final static String CASE_SENSITIVE_KEY = SEARCH_SETTINGS_KEY+"/case_sensitive";  
    
    private final SearchComboBox searchComboBox;
    private final QRadioButton forwardRadioButton = new QRadioButton(this);
    private final QRadioButton backwardRadioButton = new QRadioButton(this);
    private final QRadioButton caseSensitiveRadioButton = new QRadioButton(this);
    
    private boolean isForward = true;
    private boolean isCaseSensitive;
    private String searchString;
    
    public SearchDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, false);
        searchComboBox = new SearchComboBox(parent, environment, SEARCH_SETTINGS_KEY);
        setupUi();
        loadSettings();
    }
        
    private void setupUi() {
        final MessageProvider messageProvider = getEnvironment().getMessageProvider();        
        setWindowTitle(messageProvider.translate("TraceDialog", "Find"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FIND));
        setMaximumSize(360, 217);
        setMinimumSize(240, 217);
         
        dialogLayout().addWidget(searchComboBox);
        searchComboBox.editTextChanged.connect(this, "updateFindButton()");
        {
            final QGroupBox gbDirection = new QGroupBox(this);
            gbDirection.setObjectName("rx_gbDirection");
            gbDirection.setTitle(messageProvider.translate("TraceDialog", "Direction"));
            dialogLayout().addWidget(gbDirection);        
        
            final QHBoxLayout layout = new QHBoxLayout();
            gbDirection.setLayout(layout);
        
            layout.addWidget(forwardRadioButton);
            layout.addStretch();
            layout.addWidget(backwardRadioButton);
        
            forwardRadioButton.setText(messageProvider.translate("TraceDialog", "F&orward"));
            forwardRadioButton.setObjectName("rx_rbForward");
            backwardRadioButton.setText(messageProvider.translate("TraceDialog", "&Backward"));
            backwardRadioButton.setObjectName("rx_rbBackward");
        
            forwardRadioButton.setChecked(true);
        }
        {
            final QGroupBox gbOptions = new QGroupBox(this);
            gbOptions.setObjectName("rx_gbOptions");
            gbOptions.setTitle(messageProvider.translate("TraceDialog", "Options"));
            dialogLayout().addWidget(gbOptions);
        
            final QHBoxLayout layout = new QHBoxLayout();
            gbOptions.setLayout(layout);
        
            layout.addWidget(caseSensitiveRadioButton);
            layout.addStretch();
        
            caseSensitiveRadioButton.setText(messageProvider.translate("TraceDialog", "&Case Sensitive"));
            caseSensitiveRadioButton.setObjectName("rx_rbCaseSensitive");
        }
        final IPushButton button = addButton(EDialogButtonType.OK);
        button.setTitle(messageProvider.translate("TraceDialog", "&Find"));
        button.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.FIND));
        button.setObjectName("rx_btnFind");
        button.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                isForward = forwardRadioButton.isChecked();
                isCaseSensitive = caseSensitiveRadioButton.isChecked();        
                searchString = searchComboBox.currentText();
                saveSettings();
                accept();
            }
        });
        updateFindButton();
        searchComboBox.setFocus();
        setDisposeAfterClose(true);        
    }
    
    private void loadSettings(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        final String directionValue = settings.readString(DIRECTION_KEY, FORWARD_DIRECTION_VALUE);            
        if (BACKWARD_DIRECTION_VALUE.equals(directionValue)){
            forwardRadioButton.setChecked(false);
            backwardRadioButton.setChecked(true);            
        }
        caseSensitiveRadioButton.setChecked(settings.readBoolean(CASE_SENSITIVE_KEY, false));
    }
    
    public boolean isForward() {
        return isForward;
    }
    
    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }
    
    public String getSearchString() {
        return searchString;
    }
    
    private void updateFindButton(){
        getButton(EDialogButtonType.OK).setEnabled(!searchComboBox.currentText().isEmpty());
    }
    
    private void saveSettings(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        settings.writeString(DIRECTION_KEY, isForward ? FORWARD_DIRECTION_VALUE : BACKWARD_DIRECTION_VALUE);
        settings.writeBoolean(CASE_SENSITIVE_KEY, isCaseSensitive);
        searchComboBox.saveHistory();
    }
}