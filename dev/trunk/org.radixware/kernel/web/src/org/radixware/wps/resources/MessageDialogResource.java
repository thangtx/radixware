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

package org.radixware.wps.resources;

import java.util.*;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.resources.IMessageDialogResource;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.client.widgets.WidgetFactory;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EDialogType;
import org.radixware.schemas.eas.MessageDialogOpenRq;
import org.radixware.wps.rwt.MessageBox;


public class MessageDialogResource extends MessageBox implements IMessageDialogResource {
    
    private IMessageDialogResource.MessageDialogButton clickedButton;
    private final String resourceId = UUID.randomUUID().toString();
    private Map<String,IMessageDialogResource.MessageDialogButton> buttonsById = new HashMap<>();
    
    private static Set<EDialogButtonType> list2set(List<EDialogButtonType> list) {
        if (list == null) {
            return null;
        } else {
            HashSet<EDialogButtonType> set = new HashSet<>();
            for (EDialogButtonType bt : list) {
                set.add(bt);
            }
            return set;
        }
    }
    
    private MessageDialogResource(final IClientEnvironment environment, final EDialogIconType icon, final String caption, final String text, final List<IMessageDialogResource.MessageDialogButton> buttons) {
        super(caption, text, null, icon);
        if (buttons != null) {
            clearCloseActions();
            final WidgetFactory factory = environment.getApplication().getWidgetFactory();
            IPushButton button;
            final Set<EDialogButtonType> buttonTypes = new HashSet<>();
            for (IMessageDialogResource.MessageDialogButton dialogButton : buttons) {
                button = dialogButton.createWidget(factory, environment);
                addCloseAction(button, StandardButton.getDialogResult(dialogButton.getType()));
                if (dialogButton.getIconId()!=null){
                    button.setIcon(environment.getDefManager().getImage(dialogButton.getIconId()));
                }
                buttonsById.put(dialogButton.getId(), dialogButton);
                buttonTypes.add(dialogButton.getType());
            }
            setupDefaultButtons(buttonTypes);
        }
    }
    
    
    public static MessageDialogResource open(final IClientEnvironment environment, MessageDialogOpenRq request) {
        final EDialogIconType icon = getDialogTypeByDialogTypeEnum(request.getType());
        final String text = request.getText() != null ? "<p>" + request.getText() + "</p>" : "";
        final String caption;
        if (request.getCaption() == null) {
            caption = getDialogTitleByType(environment.getMessageProvider(), request.getType());
        } else {
            caption = request.getCaption();
        }
        final List<IMessageDialogResource.MessageDialogButton> buttons;
        if (request.getButtons()==null || request.getButtons().getItemList()==null || request.getButtons().getItemList().isEmpty()){
            buttons = IMessageDialogResource.MessageDialogButton.wrap(Collections.singletonList(EDialogButtonType.OK));
        }else{
            buttons = IMessageDialogResource.MessageDialogButton.parse(request.getButtons());
        }
        return new MessageDialogResource(environment, icon, caption, text, buttons);
    }
    
    public static MessageDialogResource open(final IClientEnvironment environment, org.radixware.schemas.eas.NextDialogRequest.MessageBox messageBox) {
        final String title = getDialogTitleByType(environment.getMessageProvider(), messageBox.getType());
        final String message;
        if (messageBox.getText() != null) {
            message = messageBox.getText();
        } else {
            message = messageBox.getHtmlText();
        }
        final EDialogIconType icon = getDialogTypeByDialogTypeEnum(messageBox.getType());
        final List<EDialogButtonType> buttonTypes = new ArrayList<>(2);
        if (messageBox.getContinueButtonType() != null) {
            buttonTypes.add(messageBox.getContinueButtonType());
        }
        if (messageBox.getCancelButtonType() != null) {
            buttonTypes.add(messageBox.getCancelButtonType());
        }
        if (buttonTypes.isEmpty()){
            buttonTypes.add(EDialogButtonType.OK);
        }
        final List<IMessageDialogResource.MessageDialogButton> buttons = 
            IMessageDialogResource.MessageDialogButton.wrap(buttonTypes);
        return new MessageDialogResource(environment, icon, title, message, buttons);
    }
    
    public static EDialogIconType getDialogTypeByDialogTypeEnum(final EDialogType dialogType) {
        EDialogIconType icon = null;
        switch (dialogType) {
            case INFORMATION:
                icon = EDialogIconType.INFORMATION;
                break;
            case CONFIRMATION:
                icon = EDialogIconType.QUESTION;
                break;
            case WARNING:
                icon = EDialogIconType.WARNING;
                break;
            case ERROR:
                icon = EDialogIconType.CRITICAL;
                break;
            
        }
        return icon;
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
    public String wait(final int msec) {
        final long[] start = new long[]{-1};
        final IPeriodicalTask task = findRoot().startTimer(new TimerEventHandler() {
            @Override
            public void processTimerEvent(IPeriodicalTask task) {
                if (start[0] < 0) {
                    start[0] = System.currentTimeMillis();
                } else {
                    if (System.currentTimeMillis() - msec > start[0]) {
                        close(DialogResult.REJECTED);
                    }
                }
            }
        });
        try {
            execDialog();
            synchronized (clickLock) {
                return clickedButton==null ? null : clickedButton.getId();
            }
        } finally {
            findRoot().killTimer(task);
        }
    }
    
    @Override
    public void setEscapeButton(EDialogButtonType bt) {
        setEscapeAction(bt);
    }
    
    private final Object clickLock = new Object();

    @Override
    protected DialogResult onClose(final String action, final DialogResult actionResult) {
        synchronized (clickLock) {
            clickedButton = buttonsById.get(action);
            return clickedButton==null ? null : actionResult;
        }
    }

    @Override
    public int exec() {
        final DialogResult result = execDialog();
        return result==null ? -1 : result.ordinal();
    }        
    
    @Override
    public IMessageDialogResource.MessageDialogButton getClickedButton() {
        synchronized (clickLock) {
            return clickedButton;
        }
    }
    
    @Override
    public String getId() {
        return resourceId;
    }
    
    @Override
    public void free() throws TerminalResourceException {
        if (!wasClosed()) {
            close(DialogResult.REJECTED);
        }
    }
}
