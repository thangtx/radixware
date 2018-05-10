/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.CursorShape;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QProgressBar;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IWaitForAadcMemberSwitchDialog;
import org.radixware.kernel.common.client.eas.EasClient;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public final class WaitForAadcMemberSwitchDialog extends ExplorerDialog implements IWaitForAadcMemberSwitchDialog{
    
    private final static ClientIcon INSTANT_SWITH_ICON = new ClientIcon("classpath:images/lightning.svg"){};    
    private final static int UPDATE_PROGRESS_BAR_INTERVAL_MILLS = 100;
    private final static int MAX_PROGRESS_VALUE  = ((int)EasClient.KEEP_AADC_MEMBER_INTERVAL_MILLIS)/UPDATE_PROGRESS_BAR_INTERVAL_MILLS;
    
    private final QProgressBar progressBar = new QProgressBar(this);
    private int timerId;
    private boolean wasClosed;
    private IWaitForAadcMemberSwitchDialog.Button button;

    public WaitForAadcMemberSwitchDialog(final IClientEnvironment environment){
        super(environment, (QWidget)environment.getMainWindow(), false);
        setupUi();
        setDisposeAfterClose(true);
    }
    
    private void setupUi(){
        final Qt.WindowFlags flags = new Qt.WindowFlags();
        flags.set(new Qt.WindowType[]{WidgetUtils.MODAL_DIALOG_WINDOW_TYPE,
                        Qt.WindowType.MSWindowsFixedSizeDialogHint,                        
                        Qt.WindowType.WindowTitleHint
                        });
        if (!SystemTools.isWindows){
            flags.set(Qt.WindowType.WindowSystemMenuHint);
        }
        setWindowFlags(flags);
        setModal(true);
        this.setCursor(new QCursor(CursorShape.ArrowCursor));
        
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("Wait Dialog", "Please Wait"));
        {
            final String message = mp.translate("Wait Dialog", "Switching to another cluster node.");
            final QLabel lbMessage = new QLabel(message, this);
            lbMessage.setAlignment(Qt.AlignmentFlag.AlignCenter);
            dialogLayout().addWidget(lbMessage);
        }
        {
            final String message = mp.translate("Wait Dialog", "Please wait for data to reach a consistent state.");
            final QLabel lbMessage = new QLabel(message, this);
            lbMessage.setAlignment(Qt.AlignmentFlag.AlignCenter);
            dialogLayout().addWidget(lbMessage);            
        }        
        progressBar.setMaximum(MAX_PROGRESS_VALUE);
        progressBar.setValue(0);
        dialogLayout().addWidget(progressBar);        
        {
            final QLabel lbSpace = new QLabel("", this);            
            dialogLayout().addWidget(lbSpace);
        }
        final IPushButton switchButton = addButton(EDialogButtonType.OK);
        final IPushButton cancelButton = addButton(EDialogButtonType.CANCEL);
        switchButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(INSTANT_SWITH_ICON));
        switchButton.setTitle(mp.translate("Wait Dialog", "Immediate switch"));
        switchButton.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                WaitForAadcMemberSwitchDialog.this.button = IWaitForAadcMemberSwitchDialog.Button.IMMEDIATE_SWITCH;
                finish();
            }
        });
        cancelButton.setTitle(mp.translate("Wait Dialog", "Interrupt operation"));
        cancelButton.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                WaitForAadcMemberSwitchDialog.this.button = IWaitForAadcMemberSwitchDialog.Button.CANCEL;
                finish();
            }
        });
        setCenterButtons(true);
        setAutoSize(true);                
    }

    @Override
    public void display() {
        if (!wasClosed){
            getEnvironment().getProgressHandleManager().blockProgress();
            show();
            timerId = startTimer(UPDATE_PROGRESS_BAR_INTERVAL_MILLS);
        }
    }

    @Override
    public Button getPressedButton() {
        return button;
    }

    @Override
    public void finish() {
        if (!wasClosed){
            if (timerId!=0){
                killTimer(timerId);
                timerId = 0;
            }
            close();
            getEnvironment().getProgressHandleManager().unblockProgress();
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        wasClosed = true;
        super.closeEvent(event);
    }        

    @Override
    protected void timerEvent(final QTimerEvent event) {
        if (event.timerId()==timerId){
            event.accept();
            final int value = progressBar.value();
            if (value==MAX_PROGRESS_VALUE){
                killTimer(timerId);
                timerId = 0;
                button = IWaitForAadcMemberSwitchDialog.Button.IMMEDIATE_SWITCH;
                finish();
            }else{
                progressBar.setValue(progressBar.value()+1);
            }                                
        }else{
            super.timerEvent(event);
        }
    }                
}
