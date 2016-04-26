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

package org.radixware.wps.views.editor;

import java.awt.Color;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;

import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.rwt.HrefButton;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.StaticText;


public final class ErrorView extends Container {

    private Label errorLabel;
    private StaticText text;
    private AbstractContainer inner;
    private String error;
    private boolean shown = false;
    private final GroupBox groupBox;

    public ErrorView() {
        groupBox = new GroupBox();
        groupBox.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
        final HrefButton button = new HrefButton(getEnvironment().getMessageProvider().translate("ErrorView", "show details"));
        groupBox.add(button);
        groupBox.setLeft(3);
        groupBox.setTop(3);

        groupBox.getAnchors().setBottom(new Anchors.Anchor(1, -3));
        groupBox.getAnchors().setRight(new Anchors.Anchor(1, -3));
        add(groupBox);
        groupBox.setTitleBackground(Color.red);
        groupBox.setTitleForeground(Color.white);
        button.setLeft(10);
        button.setTop(10);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                if (!shown) {
                    if (text == null) {
                        text = new StaticText();

                        text.setLeft(3);
                        text.getAnchors().setTop(new Anchors.Anchor(1, 3, button));
                        text.getAnchors().setBottom(new Anchors.Anchor(1, -3));
                        text.getAnchors().setRight(new Anchors.Anchor(1, -3));
                        groupBox.add(text);
                        text.setText(error);
                        text.setVisible(true);
                    } else {
                        text.setText(error);
                        text.setVisible(true);
                    }
                    button.setText(getEnvironment().getMessageProvider().translate("ErrorView", "hide details"));
                    shown = true;
                } else {
                    if (text != null) {
                        text.setVisible(false);
                    }
                    button.setText(getEnvironment().getMessageProvider().translate("ErrorView", "show details"));
                    shown = false;
                }
            }
        });
    }

    public void setError(String error, Throwable exception) {
        if (exception instanceof BrokenEntityObjectException){
            final MessageProvider mp = getEnvironment().getMessageProvider();
            final BrokenEntityObjectException brokenEntityException = (BrokenEntityObjectException)exception;
            groupBox.setTitle(error);            
            final String title = brokenEntityException.getLocalizedMessage(mp);
            String detailMessageText = brokenEntityException.getCauseExceptionMessage();            
            if (detailMessageText !=null && !detailMessageText.isEmpty()){
                detailMessageText += "\n";
            }
            detailMessageText+=brokenEntityException.getCauseExceptionStack();                    
            this.error = title+"\n"+detailMessageText;
            if (text != null) {
                text.setText(this.error);
            }
        } else{
            groupBox.setTitle(error);
            final ExceptionMessage message = new ExceptionMessage(getEnvironment(), exception);
            if (message.hasDialogMessage()){
                this.error = message.getDialogMessage()+"\n"+message.getDetails();
            }else{
                this.error = "";
            }
            if (text != null) {
                text.setText(this.error);
            }                
            if (ClientException.isSystemFault(exception)){
                getEnvironment().processException(exception);
            }            
        }
    }
}
