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

package org.radixware.kernel.common.client.eas.resources;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.WidgetFactory;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.DialogButtonsList;

public interface IMessageDialogResource extends ITerminalResource {
    
    public static final class MessageDialogButton{
        
        private final EDialogButtonType buttonType;
        private final String buttonId;
        private final String buttonTitle;
        private final Id buttonIconId;
        
        private MessageDialogButton(final DialogButtonsList.Item buttonXml){
            buttonId = buttonXml.getButtonId();
            buttonType = buttonXml.getButtonType();
            buttonTitle = buttonXml.getButtonTitle();            
            buttonIconId = buttonXml.isSetIconId() ? buttonXml.getIconId() : null;
        }
        
        private MessageDialogButton(final EDialogButtonType type){
            buttonId = UUID.randomUUID().toString();
            buttonTitle = null;
            buttonType = type;
            buttonIconId = null;
        }
        
        public IPushButton createWidget(final WidgetFactory factory, final IClientEnvironment environment){
            final IPushButton button = factory.newDialogButton(buttonType, environment);
            if (buttonTitle!=null && !buttonTitle.isEmpty()){
                button.setTitle(buttonTitle);
            }
            button.setObjectName(buttonId);
            return button;
        }

        public EDialogButtonType getType() {
            return buttonType;
        }

        public String getId() {
            return buttonId;
        }

        public String getTitle() {
            return buttonTitle;
        }
        
        public Id getIconId(){
            return buttonIconId;
        }
        
        public static List<MessageDialogButton> parse(DialogButtonsList xmlButtons){
            final List<MessageDialogButton> result = new LinkedList<>();
            for (DialogButtonsList.Item xmlButton: xmlButtons.getItemList()){
                result.add(new MessageDialogButton(xmlButton));
            }
            return result;
        }
        
        public static List<MessageDialogButton> wrap(final List<EDialogButtonType> buttonTypes){
            final List<MessageDialogButton> result = new LinkedList<>();
            for (EDialogButtonType type: buttonTypes){
                result.add(new MessageDialogButton(type));
            }
            return result;
        }
    }

    public String wait(int msec);

//    public IButton getButton(IMessageBox.StandardButton bt);
    public void setEscapeButton(EDialogButtonType bt);

    public int exec();

    public MessageDialogButton getClickedButton();
}
