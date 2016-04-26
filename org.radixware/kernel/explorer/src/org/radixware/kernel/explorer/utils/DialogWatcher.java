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

package org.radixware.kernel.explorer.utils;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;
import org.radixware.kernel.explorer.widgets.ExplorerDialogButtonBox;


public class DialogWatcher {

    public static interface IDialogHandler {

        public boolean dialogAccepted(final QWidget dialog);

        public void doAction(final QWidget dialog);
    }

    public static class FindAndClose implements IDialogHandler {

        private Class<? extends QDialog> dialogToClose;                

        public FindAndClose(Class<? extends QDialog> dialogClass) {
            dialogToClose = dialogClass;
        }

        @Override
        public boolean dialogAccepted(QWidget dialog) {
            return dialogToClose.isInstance(dialog);
        }

        @Override
        public void doAction(QWidget dialog) {
            if (dialog instanceof ExplorerDialog) {
                final ExplorerDialog explorerDialog = (ExplorerDialog) dialog;
                List<QObject> childs = explorerDialog.findChildren(ExplorerDialogButtonBox.class, "buttonBox");
                ExplorerDialogButtonBox buttonBox = null;
                for (QObject obj : childs) {
                    if (obj.parent() == explorerDialog) {
                        buttonBox = (ExplorerDialogButtonBox) obj;
                        break;
                    }
                }
                if (buttonBox != null) {
                    buttonBox.accepted.emit();
                    //final QPushButton button = buttonBox.button(QDialogButtonBox.StandardButton.Ok);
                    //button.click();
                    return;
                }
            } else if (dialog instanceof QMessageBox) {
                final QMessageBox messageBox = (QMessageBox) dialog;
                final QAbstractButton button;
                if (messageBox.button(QMessageBox.StandardButton.Ok) != null) {
                    button = messageBox.button(QMessageBox.StandardButton.Ok);
                } else if (messageBox.button(QMessageBox.StandardButton.Yes) != null) {
                    button = messageBox.button(QMessageBox.StandardButton.Yes);
                } else if (messageBox.button(QMessageBox.StandardButton.Apply) != null) {
                    button = messageBox.button(QMessageBox.StandardButton.Apply);
                } else if (!messageBox.buttons().isEmpty()) {
                    button = messageBox.buttons().get(0);
                } else {
                    button = null;
                }
                if (button != null) {
                    button.click();
                } else {
                    messageBox.accept();
                }
            } else if (dialog instanceof ExplorerMessageBox){
                final ExplorerMessageBox messageBox = (ExplorerMessageBox)dialog;
                final QAbstractButton button;
                if (messageBox.button(QMessageBox.StandardButton.Ok) != null) {
                    button = messageBox.button(QMessageBox.StandardButton.Ok);
                } else if (messageBox.button(QMessageBox.StandardButton.Yes) != null) {
                    button = messageBox.button(QMessageBox.StandardButton.Yes);
                } else if (messageBox.button(QMessageBox.StandardButton.Apply) != null) {
                    button = messageBox.button(QMessageBox.StandardButton.Apply);
                } else if (!messageBox.buttons().isEmpty()) {
                    button = messageBox.buttons().get(0);
                } else {
                    button = null;
                }
                if (button != null) {
                    button.click();
                } else {
                    messageBox.accept();
                }                            
            } else if (dialog instanceof QDialog) {
                ((QDialog) dialog).accept();
            } else {
                dialog.close();
                dialog.dispose();
            }
        }
    }
    private QTimer timer;
    private final List<IDialogHandler> handlers = new LinkedList<>();
    private static DialogWatcher instance;

    protected DialogWatcher() {
    }

    private QTimer createTimer() {
        final QTimer t = new QTimer();
        t.timeout.connect(this, "findAndInvoke()");
        t.setInterval(1000);//1 second
        t.setSingleShot(false);
        return t;
    }

    public static DialogWatcher getInstance() {
        if (instance == null) {
            instance = new DialogWatcher();
        }
        return instance;
    }

    public void closeDialogLater(Class<? extends QDialog> dialogClass) {
        invokeLater(new FindAndClose(dialogClass));
    }

    public void invokeLater(IDialogHandler dlgHandler) {
        handlers.add(dlgHandler);
        if (timer==null || !timer.isActive()){
            timer = createTimer();
            timer.start();
        }
    }
    
    public boolean isActive(){
        return timer!=null && timer.isActive();
    }

    @SuppressWarnings("unused")
    private void findAndInvoke() {
        if (!handlers.isEmpty()){
            final IDialogHandler handler = handlers.get(0);            
            final QWidget dialog = findDialog(handler);
            if (dialog instanceof ExplorerDialog && 
               ((ExplorerDialog)dialog).getEnvironment().getEasSession().isBusy()){
                return;
            }
            if (dialog != null) {
                handlers.remove(0);
                if (handlers.isEmpty()){
                    timer.stop();
                }
                handler.doAction(dialog);
            }
        }
    }

    public void stopWatcher() {
        if (timer != null) {
            this.timer.stop();
            handlers.clear();
        }
    }

    private static QWidget findDialog(final IDialogHandler handler) {
        final QWidget modalWidget = QApplication.activeModalWidget();
        if (handler.dialogAccepted(modalWidget)) {
            return modalWidget;
        }
        final QWidget activeWidget = QApplication.activeWindow();
        if (handler.dialogAccepted(activeWidget)) {
            return activeWidget;
        }
        List<QWidget> widgets = QApplication.topLevelWidgets();
        for (QWidget widget : widgets) {
            if (handler.dialogAccepted(widget)) {
                return widget;
            }
        }
        widgets = QApplication.allWidgets();
        for (QWidget widget : widgets) {
            if (handler.dialogAccepted(widget)) {
                return (QDialog) widget;
            }
        }
        return null;
    }
}
