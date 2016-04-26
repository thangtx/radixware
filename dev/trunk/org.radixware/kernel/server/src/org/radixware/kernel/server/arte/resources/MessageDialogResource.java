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

package org.radixware.kernel.server.arte.resources;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogType;
import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.schemas.eas.DialogButtonsList;
import org.radixware.schemas.eas.MessageDialogOpenMess;
import org.radixware.schemas.eas.MessageDialogOpenRq;
import org.radixware.schemas.eas.MessageDialogWaitButtonMess;
import org.radixware.schemas.eas.MessageDialogWaitButtonRq;
import org.radixware.schemas.easWsdl.MessageDialogOpenDocument;
import org.radixware.schemas.easWsdl.MessageDialogWaitButtonDocument;

public class MessageDialogResource extends Resource {

    public static class MessageDialogButton {

        private final EDialogButtonType buttonType;
        private final String buttonTitle;
        private final String buttonId;
        private final Id buttonIconId;

        public MessageDialogButton(final EDialogButtonType type) {
            this(type, null, null, null);
        }

        public MessageDialogButton(final EDialogButtonType type, final String title) {
            this(type, title, null, null);
        }
        
        public MessageDialogButton(final EDialogButtonType type, final String title, final Id iconId) {
            this(type, title, iconId, null);
        }
        
        public MessageDialogButton(final EDialogButtonType type, final String title, final String id) {
            this(type, title, null, id);
        }

        public MessageDialogButton(final EDialogButtonType type, final String title, final Id iconId, final String id) {
            this.buttonType = type;
            this.buttonTitle = title;
            this.buttonIconId = iconId;
            this.buttonId = id == null || id.isEmpty() ? UUID.randomUUID().toString() : id;
        }

        public final EDialogButtonType getType() {
            return buttonType;
        }

        public final String getTitle() {
            return buttonTitle;
        }

        public final String getId() {
            return buttonId;
        }
        
        public final Id getIconId(){
            return buttonIconId;
        }
        
        public final void toXml(final DialogButtonsList.Item buttonItem){
            buttonItem.setButtonType(buttonType);
            if (buttonTitle!=null && !buttonTitle.isEmpty()){
                buttonItem.setButtonTitle(buttonTitle);
            }
            buttonItem.setButtonId(buttonId);
            if (buttonIconId!=null){
                buttonItem.setIconId(buttonIconId);
            }
        }

        @Override
        public int hashCode() {
            return buttonId.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MessageDialogButton other = (MessageDialogButton) obj;
            return buttonId.equals(other.buttonId);
        }
       
        @Override
        public String toString() {
            return "button with type \'"+buttonType.getName()+"\' and id \'"+buttonId+"\'";
        }                
    }
    
    public static MessageDialogButton show(final Arte arte, final EDialogType type, final String caption, final LinkedHashSet<MessageDialogButton> buttons, final String text)
            throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        final MessageDialogOpenDocument mdoDoc = MessageDialogOpenDocument.Factory.newInstance();
        final MessageDialogOpenRq rq = mdoDoc.addNewMessageDialogOpen().addNewMessageDialogOpenRq();
        rq.setType(type);
        rq.setCaption(caption);
        rq.setText(text);
        final DialogButtonsList buttonsList =  rq.addNewButtons();
        if (buttons!=null){
            for (MessageDialogButton button: buttons){
                button.toXml(buttonsList.addNewItem());
            }        
        }

        final MessageDialogOpenMess mdoMess = (MessageDialogOpenMess) arte.getArteSocket().invokeResource(mdoDoc, MessageDialogOpenMess.class, DEFAULT_TIMEOUT);
        final String id = mdoMess.getMessageDialogOpenRs().getId();

        final MessageDialogWaitButtonDocument mdwDoc = MessageDialogWaitButtonDocument.Factory.newInstance();
        final MessageDialogWaitButtonRq waitRq = mdwDoc.addNewMessageDialogWaitButton().addNewMessageDialogWaitButtonRq();
        waitRq.setId(id);
        waitRq.setTimeout(WAIT_TIMEOUT);

        MessageDialogWaitButtonMess waitRs;
        while (true) {
            waitRs = (MessageDialogWaitButtonMess) arte.getArteSocket().invokeResource(mdwDoc, MessageDialogWaitButtonMess.class, 1000 * (WAIT_TIMEOUT + WAIT_TIMEOUT_ADDON));
            final String pressedButtonId = waitRs.getMessageDialogWaitButtonRs().getPressedButtonId();
            if (pressedButtonId!=null && !pressedButtonId.isEmpty()) {
                for (MessageDialogButton button: buttons){
                    if (pressedButtonId.equals(button.getId())){
                        return button;
                    }
                }
                throw new ResourceUsageException("Button with id=\""+pressedButtonId+"\" was not found", null);
            }
        }
    }    

    /**
     * @param buttons - example: { Vector bts = new Vector(); bts.add(
     * EDialogButtonType.OK.toString() ); }
     * @throws ResourceUsageTimeout
     * @throws ResourceUsageException
     * @throws InterruptedException
     */
    public static EDialogButtonType show(final Arte arte, final EDialogType type, final String caption, final List<EDialogButtonType> buttons, final String text)
            throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        final LinkedHashSet<MessageDialogButton> dialogButtons = new LinkedHashSet<>();
        if (buttons!=null){
            for (EDialogButtonType buttonType: buttons){
                dialogButtons.add(new MessageDialogButton(buttonType));
            }
        }
        return show(arte, type, caption, dialogButtons, text).getType();
    }
       
    public static void information(final Arte arte, final String caption, final String text)
            throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        final List<EDialogButtonType> buttons = new LinkedList<>();
        buttons.add(EDialogButtonType.OK);
        show(arte, EDialogType.INFORMATION, caption, buttons, text);
    }

    public static boolean confirmation(final Arte arte, final String caption, final String text)
            throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        final List<EDialogButtonType> buttons = new LinkedList<>();
        buttons.add(EDialogButtonType.YES);
        buttons.add(EDialogButtonType.NO);
        return show(arte, EDialogType.WARNING, caption, buttons, text).equals(EDialogButtonType.YES);
    }

    public static void warning(final Arte arte, final String caption, final String text)
            throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        final List<EDialogButtonType> buttons = new LinkedList<>();
        buttons.add(EDialogButtonType.OK);
        show(arte, EDialogType.WARNING, caption, buttons, text);
    }

    public static void error(final Arte arte, final String caption, final String text)
            throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        final List<EDialogButtonType> buttons = new LinkedList<>();
        buttons.add(EDialogButtonType.OK);
        show(arte, EDialogType.ERROR, caption, buttons, text);
    }
    private static final int WAIT_TIMEOUT = 60; // ��������� timeout �������� ������ �� ���������, c
    private static final int WAIT_TIMEOUT_ADDON = 20; // ���������� ����� �� �������� ������ �� ���������, c
}
