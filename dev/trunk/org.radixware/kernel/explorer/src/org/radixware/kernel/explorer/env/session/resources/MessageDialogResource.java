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

package org.radixware.kernel.explorer.env.session.resources;

import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMessageBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.resources.IMessageDialogResource;
import org.radixware.kernel.common.client.eas.resources.ITerminalResource;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.WidgetFactory;
import org.radixware.kernel.common.enums.EDialogButtonType;
import static org.radixware.kernel.common.enums.EDialogButtonType.ABORT;
import static org.radixware.kernel.common.enums.EDialogButtonType.ALL;
import static org.radixware.kernel.common.enums.EDialogButtonType.APPLY;
import static org.radixware.kernel.common.enums.EDialogButtonType.BACK;
import static org.radixware.kernel.common.enums.EDialogButtonType.CANCEL;
import static org.radixware.kernel.common.enums.EDialogButtonType.CLOSE;
import static org.radixware.kernel.common.enums.EDialogButtonType.DISCARD;
import static org.radixware.kernel.common.enums.EDialogButtonType.HELP;
import static org.radixware.kernel.common.enums.EDialogButtonType.IGNORE;
import static org.radixware.kernel.common.enums.EDialogButtonType.NO;
import static org.radixware.kernel.common.enums.EDialogButtonType.NO_BUTTON;
import static org.radixware.kernel.common.enums.EDialogButtonType.NO_TO_ALL;
import static org.radixware.kernel.common.enums.EDialogButtonType.OK;
import static org.radixware.kernel.common.enums.EDialogButtonType.OPEN;
import static org.radixware.kernel.common.enums.EDialogButtonType.RESET;
import static org.radixware.kernel.common.enums.EDialogButtonType.RESTORE_DEFAULTS;
import static org.radixware.kernel.common.enums.EDialogButtonType.RETRY;
import static org.radixware.kernel.common.enums.EDialogButtonType.SAVE;
import static org.radixware.kernel.common.enums.EDialogButtonType.SAVE_ALL;
import static org.radixware.kernel.common.enums.EDialogButtonType.SKIP;
import static org.radixware.kernel.common.enums.EDialogButtonType.YES;
import static org.radixware.kernel.common.enums.EDialogButtonType.YES_TO_ALL;
import org.radixware.kernel.common.enums.EDialogType;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.ValueConverter;
import org.radixware.schemas.eas.MessageDialogOpenRq;

public final class MessageDialogResource extends ExplorerMessageBox implements ITerminalResource, IMessageDialogResource {    

    private final QTimer timer = new QTimer();
    private final QEventLoop eventLoop = new QEventLoop(this);
    private final IClientEnvironment environment;    
    private final Map<String,IMessageDialogResource.MessageDialogButton> buttonsById = new HashMap<>();
    private boolean active;

    @SuppressWarnings("LeakingThisInConstructor")
    private MessageDialogResource(final IClientEnvironment environment, final QMessageBox.Icon icon, final String caption, final String text, final List<MessageDialogButton> buttons) {
        super(null);
        setIcon(icon);
        setWindowTitle(caption);
        setText(text);
        this.environment = environment;        
        if (buttons != null) {
            QAbstractButton button;
            final WidgetFactory widgetFactory = environment.getApplication().getWidgetFactory();
            for (MessageDialogButton dialogButton : buttons) {
                button = (QAbstractButton)dialogButton.createWidget(widgetFactory, environment);
                addButton(button, dialogButtonEnum2QtButtonRole(dialogButton.getType()));
                if (dialogButton.getIconId()!=null){
                    final QIcon buttonIcon = 
                        ExplorerIcon.getQIcon(environment.getDefManager().getImage(dialogButton.getIconId()));
                    button.setIcon(buttonIcon);
                }
                buttonsById.put(dialogButton.getId(), dialogButton);
            }
        }
        timer.setSingleShot(true);                
        timer.timeout.connect(this,"exitEventLoop()");
        finished.connect(this,"exitEventLoop()");
    }

    public static MessageDialogResource open(final IClientEnvironment environment, MessageDialogOpenRq request) {
        final QMessageBox.Icon icon = ValueConverter.getQtDialogTypeByDialogTypeEnum(request.getType());
        final String text = request.getText() != null ? "<P>" + request.getText() + "</P>" : "";
        final String caption;
        if (request.getCaption() == null || request.getCaption().isEmpty()){
            caption = getDialogTitleByType(environment.getMessageProvider(),request.getType());
        }
        else{
            caption = request.getCaption();
        }
        final List<IMessageDialogResource.MessageDialogButton> buttons = 
                IMessageDialogResource.MessageDialogButton.parse(request.getButtons());
        return new MessageDialogResource(environment, icon, caption, text, buttons);
    }

    public static MessageDialogResource open(final IClientEnvironment environment, org.radixware.schemas.eas.NextDialogRequest.MessageBox messageBox) {
        final String title = getDialogTitleByType(environment.getMessageProvider(),messageBox.getType());
        final String message;
        if (messageBox.getText() != null) {
            message = messageBox.getText();
        } else {
            message = messageBox.getHtmlText();
        }
        final QMessageBox.Icon icon = ValueConverter.getQtDialogTypeByDialogTypeEnum(messageBox.getType());
        final List<EDialogButtonType> buttonTypes = new ArrayList<>(2);
        if (messageBox.getContinueButtonType() != null) {
            buttonTypes.add(messageBox.getContinueButtonType());
        }
        if (messageBox.getCancelButtonType() != null) {
            buttonTypes.add(messageBox.getCancelButtonType());
        }
        final List<IMessageDialogResource.MessageDialogButton> buttons = 
            IMessageDialogResource.MessageDialogButton.wrap(buttonTypes);
        return new MessageDialogResource(environment, icon, title, message, buttons);
    }

    private static String getDialogTitleByType(final MessageProvider msgProvider, EDialogType type) {
        switch (type) {
            case INFORMATION:
                return msgProvider.translate("MessageBox", "Information");
            case CONFIRMATION:
                return msgProvider.translate("MessageBox", "Question");
            case WARNING:
                return msgProvider.translate("MessageBox", "Warning");
            case ERROR:
                return msgProvider.translate("MessageBox", "Error");
            default:
                return msgProvider.translate("MessageBox", "Message");
        }
    }

    @Override
    public void closeEvent(final QCloseEvent closeEvent) {
        active = false;
        super.closeEvent(closeEvent);
    }

    @Override
    public void done(final int result) {
        active = false;
        super.done(result);
    }
    
    

    @Override
    public String wait(int msec) {
        if (!active) {
            environment.getProgressHandleManager().blockProgress();
            active = true;
            show();
        }
        timer.start(msec);
        eventLoop.exec();
        final String buttonName = getClickedButtonName();
        if (buttonName != null) {
            environment.getProgressHandleManager().unblockProgress();
            return buttonName;
        }
        return null;
    }
    
    @SuppressWarnings("unused")
    private void exitEventLoop(){
        if (timer.isActive()){
            timer.stop();
        }
        eventLoop.exit();        
    }

    @Override
    public String getId() {
        return getClass().getName() + "@" + winId();
    }

    @Override
    public void free() {
        if (active) {
            close();
        }
    }

    @Override
    public void setEscapeButton(final EDialogButtonType bt) {
        QMessageBox.StandardButton qbt = ExplorerMessageBox.getQButton(bt);
        if (qbt != null) {
            setEscapeButton(qbt);
        }
    }

    @Override
    public IMessageDialogResource.MessageDialogButton getClickedButton() {        
        return buttonsById.get(getClickedButtonName());
    }
        
    private static QMessageBox.ButtonRole dialogButtonEnum2QtButtonRole(final EDialogButtonType button) {
        switch (button) {
            case ABORT:
            case BACK:
            case IGNORE:
            case SKIP:
            case NO:
            case NO_BUTTON:
            case NO_TO_ALL:
                return QMessageBox.ButtonRole.NoRole;
            case ALL:
            case OK:
            case OPEN:
            case SAVE:
            case SAVE_ALL:
                return QMessageBox.ButtonRole.AcceptRole;
            case APPLY:
            case EXECUTE:
                return QMessageBox.ButtonRole.ApplyRole;
            case CANCEL:
            case CLOSE:
            case DISCARD:            
                return QMessageBox.ButtonRole.RejectRole;
            case HELP:
                return QMessageBox.ButtonRole.HelpRole;
            case RESET:
            case RESTORE_DEFAULTS:
                return QMessageBox.ButtonRole.ResetRole;
            case RETRY:
            case REPORT:
                return QMessageBox.ButtonRole.ActionRole;
            case YES:
            case YES_TO_ALL:
            case NEXT:
                return QMessageBox.ButtonRole.YesRole;
            default:
                throw new IllegalArgumentException("Can't find ButtonRole for " + button.name());
        }
    }
    
}
