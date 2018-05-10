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

package org.radixware.kernel.explorer.webdriver;

import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QWidget;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.explorer.dialogs.ExceptionMessageDialog;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvUnexpectedAlertOpenException;

public class WebDriverUserPromptManager {
    
    private final static WebDriverUserPromptManager INSTANCE = new WebDriverUserPromptManager();
    
    private final static List<QMessageBox.ButtonRole> DISMISS_BUTTON_ROLES = 
        Arrays.asList(QMessageBox.ButtonRole.RejectRole, QMessageBox.ButtonRole.NoRole, QMessageBox.ButtonRole.ResetRole, QMessageBox.ButtonRole.DestructiveRole);
    
    private final static List<QMessageBox.ButtonRole> ACCEPT_BUTTON_ROLES = 
        Arrays.asList(QMessageBox.ButtonRole.AcceptRole, QMessageBox.ButtonRole.YesRole, QMessageBox.ButtonRole.ApplyRole, QMessageBox.ButtonRole.ActionRole);    
    
    private final static List<QMessageBox.StandardButton> DISMISS_BUTTONS = 
        Arrays.asList(QMessageBox.StandardButton.Cancel, QMessageBox.StandardButton.Close, QMessageBox.StandardButton.No, QMessageBox.StandardButton.Abort,
                      QMessageBox.StandardButton.Ignore, QMessageBox.StandardButton.Discard, QMessageBox.StandardButton.Escape, QMessageBox.StandardButton.Reset,
                      QMessageBox.StandardButton.NoToAll);
    
    private final static List<QMessageBox.StandardButton> ACCEPT_BUTTONS = 
        Arrays.asList(QMessageBox.StandardButton.Ok, QMessageBox.StandardButton.Apply, QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.Open,
                      QMessageBox.StandardButton.Save, QMessageBox.StandardButton.SaveAll, QMessageBox.StandardButton.YesToAll);    
    
    private WebDriverUserPromptManager(){
        
    }    
    
    public QDialog findPromptDialog(){
        final QWidget modalWidget = QApplication.activeModalWidget();
        if (isPromptDialog(modalWidget)){
            return (QDialog)modalWidget;
        }
        final QWidget activeWindow = QApplication.activeWindow();
        if (isPromptDialog(activeWindow)){
            return (QDialog)activeWindow;                    
        }
        final List<QWidget> topLevelWidgets = QApplication.topLevelWidgets();
        for (QWidget topWidget: topLevelWidgets){                                
            if (isPromptDialog(topWidget)){
                return (QDialog)topWidget;
            }
        }
        return null;
    }    
    
    private static boolean isPromptDialog(final QWidget widget){
        return widget!=null && widget.nativeId()!=0 
               && (widget instanceof ExplorerMessageBox 
                   || widget instanceof QMessageBox
                   || widget instanceof ExceptionMessageDialog);
    }
    
    private static void dismissPromptDialog(final QDialog messageBox){
        QAbstractButton button = findDismissButton(messageBox);
        if (button!=null){
            button.click();
        }else{
            messageBox.reject();
        }
    }
    
    private static void acceptPromptDialog(final QDialog messageBox){
        final QAbstractButton button = findAcceptButton(messageBox);
        if (button!=null){
            button.click();
        }else{
            messageBox.accept();
        }     
    }
    
    private static QAbstractButton findDismissButton(final QDialog messageBox){
        if (messageBox instanceof ExplorerMessageBox){
            return findDismissButton((ExplorerMessageBox)messageBox);
        }else if (messageBox instanceof QMessageBox){
            return findDismissButton((QMessageBox)messageBox);
        }else if (messageBox instanceof ExceptionMessageDialog){
            return findCloseButton((ExceptionMessageDialog)messageBox);
        }else{
            return null;
        }        
    }
    
    private static QAbstractButton findDismissButton(final ExplorerMessageBox messageBox){
        for (QMessageBox.StandardButton button: DISMISS_BUTTONS){
            if (messageBox.button(button)!=null){
                return messageBox.button(button);
            }
        }
        final List<QAbstractButton> buttons = messageBox.buttons();
        for (QMessageBox.ButtonRole buttonRole: DISMISS_BUTTON_ROLES){
            for (QAbstractButton button: buttons){
                if (messageBox.buttonRole(button)==buttonRole){
                    return button;
                }
            }
        }
        final QAbstractButton escapeButton = messageBox.escapeButton();
        if (escapeButton!=null){
            return escapeButton;
        }        
        for (QMessageBox.StandardButton button: ACCEPT_BUTTONS){
            if (messageBox.button(button)!=null){
                return messageBox.button(button);
            }
        }
        for (QMessageBox.ButtonRole buttonRole: ACCEPT_BUTTON_ROLES){
            for (QAbstractButton button: buttons){
                if (messageBox.buttonRole(button)==buttonRole){
                    return button;
                }
            }
        }
        final QAbstractButton defaultButton = messageBox.defaultButton();
        if (defaultButton!=null){
            return defaultButton;
        }
        return buttons.isEmpty() ? null : buttons.get(0);
    }
    
    private static QAbstractButton findDismissButton(final QMessageBox messageBox){
        for (QMessageBox.StandardButton button: DISMISS_BUTTONS){
            if (messageBox.button(button)!=null){
                return messageBox.button(button);
            }
        }
        final List<QAbstractButton> buttons = messageBox.buttons();
        for (QMessageBox.ButtonRole buttonRole: DISMISS_BUTTON_ROLES){
            for (QAbstractButton button: buttons){
                if (messageBox.buttonRole(button)==buttonRole){
                    return button;
                }
            }
        }
        final QAbstractButton escapeButton = messageBox.escapeButton();
        if (escapeButton!=null){
            return escapeButton;
        }        
        for (QMessageBox.StandardButton button: ACCEPT_BUTTONS){
            if (messageBox.button(button)!=null){
                return messageBox.button(button);
            }
        }
        for (QMessageBox.ButtonRole buttonRole: ACCEPT_BUTTON_ROLES){
            for (QAbstractButton button: buttons){
                if (messageBox.buttonRole(button)==buttonRole){
                    return button;
                }
            }
        }
        final QAbstractButton defaultButton = messageBox.defaultButton();
        if (defaultButton!=null){
            return defaultButton;
        }
        return buttons.isEmpty() ? null : buttons.get(0);
    }
    
    private static QAbstractButton findAcceptButton(final QDialog messageBox){
        if (messageBox instanceof ExplorerMessageBox){
            return findAcceptButton((ExplorerMessageBox)messageBox);
        }else if (messageBox instanceof QMessageBox){
            return findAcceptButton((QMessageBox)messageBox);
        }else if (messageBox instanceof ExceptionMessageDialog){
            return findCloseButton((ExceptionMessageDialog)messageBox);
        }else{
            return null;
        }        
    }    
    
    private static QAbstractButton findAcceptButton(final ExplorerMessageBox messageBox){
        for (QMessageBox.StandardButton button: ACCEPT_BUTTONS){
            if (messageBox.button(button)!=null){
                return messageBox.button(button);
            }
        }
        final List<QAbstractButton> buttons = messageBox.buttons();
        for (QMessageBox.ButtonRole buttonRole: ACCEPT_BUTTON_ROLES){
            for (QAbstractButton button: buttons){
                if (messageBox.buttonRole(button)==buttonRole){
                    return button;
                }
            }
        }
        final QAbstractButton defaultButton = messageBox.defaultButton();
        if (defaultButton!=null){
            return defaultButton;
        }
        return buttons.isEmpty() ? null : buttons.get(0);        
    }        
    
    private static QAbstractButton findAcceptButton(final QMessageBox messageBox){
        for (QMessageBox.StandardButton button: ACCEPT_BUTTONS){
            if (messageBox.button(button)!=null){
                return messageBox.button(button);
            }
        }
        final List<QAbstractButton> buttons = messageBox.buttons();
        for (QMessageBox.ButtonRole buttonRole: ACCEPT_BUTTON_ROLES){
            for (QAbstractButton button: buttons){
                if (messageBox.buttonRole(button)==buttonRole){
                    return button;
                }
            }
        }
        final QAbstractButton defaultButton = messageBox.defaultButton();
        if (defaultButton!=null){
            return defaultButton;
        }
        return buttons.isEmpty() ? null : buttons.get(0);        
    }     
    
    private static QAbstractButton findCloseButton(final ExceptionMessageDialog dialog){
        final QDialogButtonBox buttonBox = (QDialogButtonBox)dialog.findChild(QDialogButtonBox.class, "exceptionbox_buttonbox");
        return buttonBox.button(QDialogButtonBox.StandardButton.Ok);
    }
    
    public String getMessageText(final QDialog dlg){
        if (dlg instanceof ExplorerMessageBox){
            return ((ExplorerMessageBox)dlg).text();
        }else if (dlg instanceof ExceptionMessageDialog){
            final String message = ((ExceptionMessageDialog)dlg).getMessage();
            final String details = ((ExceptionMessageDialog)dlg).getDetails();
            if (details!=null && !details.isEmpty()){
                return "Message:\n"+message+"\nDetails:\n"+details;
            }
            return message;
        }else{
            return "Unsupported prompt dialog "+dlg.getClass().getName();
        }
    }
    
    public boolean handleUserPrompt(final WebDrvCapabilities.EPromptAction action) throws WebDrvUnexpectedAlertOpenException{
        final QDialog messageBox = findPromptDialog();
        if (messageBox!=null){
            if (action==WebDrvCapabilities.EPromptAction.DISMISS){
                dismissPromptDialog(messageBox);
                return true;
            }else if (action==WebDrvCapabilities.EPromptAction.ACCEPT){
                acceptPromptDialog(messageBox);
                return true;
            }else{
                final String title = messageBox.windowTitle();
                final String text = getMessageText(messageBox);
                dismissPromptDialog(messageBox);
                throw new WebDrvUnexpectedAlertOpenException(title, text);
            }
        }
        return false;
    }    
    
    public static WebDriverUserPromptManager getInstance(){
        return INSTANCE;
    }
}
