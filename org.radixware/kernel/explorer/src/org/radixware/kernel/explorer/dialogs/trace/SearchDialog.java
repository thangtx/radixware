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

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class SearchDialog extends QDialog {

    public final Signal0 find = new Signal0();
    
    private SearchComboBox searchComboBox = new SearchComboBox();
    private QRadioButton forwardRadioButton = new QRadioButton();
    private QRadioButton backwardRadioButton = new QRadioButton();
    private QRadioButton caseSensetiveRadioButton = new QRadioButton();
    
    public SearchDialog(QWidget parent) {
        super(parent);
        setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);
        this.setWindowTitle(Application.translate("TraceDialog", "Find"));
        this.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FIND));
        this.setMaximumSize(360, 217);
        this.setMinimumSize(240, 217);
        setupUi();
    }
    
    public boolean isForward() {
        return forwardRadioButton.isChecked();
    }
    
    public boolean isCaseSensetive() {
        return caseSensetiveRadioButton.isChecked();
    }
    
    public String getSearchString() {
        return searchComboBox.currentText();
    }
    
    private void setupUi() {
        QVBoxLayout vBoxLayout = new QVBoxLayout();
        this.setLayout(vBoxLayout);
         
        vBoxLayout.addWidget(searchComboBox);
        
        QGroupBox directionGroupBox = new QGroupBox();
        directionGroupBox.setTitle(Application.translate("TraceDialog", "Direction"));
        vBoxLayout.addWidget(directionGroupBox);
        
        QHBoxLayout directionLayout = new QHBoxLayout();
        directionGroupBox.setLayout(directionLayout);
        
        directionLayout.addWidget(forwardRadioButton);
        directionLayout.addStretch();
        directionLayout.addWidget(backwardRadioButton);
        
        forwardRadioButton.setText(Application.translate("TraceDialog", "F&orward"));
        backwardRadioButton.setText(Application.translate("TraceDialog", "&Backward"));
        
        forwardRadioButton.setChecked(true);
        
        QGroupBox optionsGroupBox = new QGroupBox();
        optionsGroupBox.setTitle(Application.translate("TraceDialog", "Options"));
        vBoxLayout.addWidget(optionsGroupBox);
        
        QHBoxLayout optionsLayout = new QHBoxLayout();
        optionsGroupBox.setLayout(optionsLayout);
        
        optionsLayout.addWidget(caseSensetiveRadioButton);
        optionsLayout.addStretch();
        
        caseSensetiveRadioButton.setText(Application.translate("TraceDialog", "&Case Sensitive"));
        
        QHBoxLayout buttonLayout = new QHBoxLayout();
        vBoxLayout.addLayout(buttonLayout);
            
        QPushButton findButton = new QPushButton();
        findButton.setText(Application.translate("TraceDialog", "&Find"));
        findButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FIND));
        findButton.clicked.connect(this, "runSearch()");
        
        buttonLayout.addStretch();
        buttonLayout.addWidget(findButton);
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FIND));
    }
    
    private void runSearch() {
        if (searchComboBox.currentText().equals(""))
            return;
        searchComboBox.setTopString(searchComboBox.currentText());
        find.emit();
    }
            
}


