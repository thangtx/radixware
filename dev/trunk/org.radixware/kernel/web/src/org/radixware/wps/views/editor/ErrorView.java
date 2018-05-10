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
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
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
import org.radixware.wps.rwt.VerticalBoxContainer;


public final class ErrorView extends Container {

    private Label errorLabel;
    private StaticText text;
    private final VerticalBoxContainer layout = new VerticalBoxContainer();
    private AbstractContainer inner;
    private String error;
    private boolean shown = false;
    final HrefButton detailsButton;
    private final GroupBox groupBox;

    public ErrorView() {
        groupBox = new GroupBox();
        groupBox.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);  
        groupBox.setBorderBoxSizingEnabled(true);
        groupBox.setAdjustMode(GroupBox.AdjustMode.EXPAND_CONTENT_HEIGTH);
        detailsButton = new HrefButton(getEnvironment().getMessageProvider().translate("ErrorView", "show details"));
        detailsButton.getHtml().setCss("padding-left", "5px");
        layout.setVSizePolicy(SizePolicy.EXPAND);        
        layout.addSpace(10);
        layout.add(detailsButton);
        layout.addSpace();        
        groupBox.add(layout);
        groupBox.setLeft(3);
        groupBox.setTop(3);

        groupBox.getAnchors().setBottom(new Anchors.Anchor(1, -3));
        groupBox.getAnchors().setRight(new Anchors.Anchor(1, -3));
        add(groupBox);
        groupBox.setTitleBackground(Color.red);
        groupBox.setTitleForeground(Color.white);
        detailsButton.setLeft(10);        
        detailsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                if (shown) {
                    hideDetails();
                }else{
                    showDetails();
                }
            }
        });
    }
    
    private void showDetails(){
        if (text == null) {
            text = new StaticText();            
            layout.add(text);
            layout.setAutoSize(text, true);
            text.setText(error);
            text.setVisible(true);
        } else {
            text.setText(error);
            text.setVisible(true);
        }
        detailsButton.setText(getEnvironment().getMessageProvider().translate("ErrorView", "hide details"));
        shown = true;        
    }
    
    private void hideDetails(){
        if (text != null) {
            text.setVisible(false);
        }
        detailsButton.setText(getEnvironment().getMessageProvider().translate("ErrorView", "show details"));
        shown = false;        
    }

    public void setError(String error, Throwable exception) {
        if (exception instanceof ObjectNotFoundError) {
            final ObjectNotFoundError objectNotFound = (ObjectNotFoundError) exception;
            if (objectNotFound.inKnownContext()) {
                groupBox.setTitle(objectNotFound.getMessageToShow());
                if (shown){
                    hideDetails();
                }
                detailsButton.setVisible(false);
                this.error = "";
            } else {
                groupBox.setTitle(error);
                this.error = objectNotFound.getMessageToShow()+"\n"+ClientException.exceptionStackToString(exception);
                detailsButton.setVisible(true);
            }
        } else if (exception instanceof BrokenEntityObjectException){
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
            detailsButton.setVisible(true);
        } else{
            groupBox.setTitle(error);
            final ExceptionMessage message = new ExceptionMessage(getEnvironment(), exception);
            final StringBuilder errorTextBuilder = new StringBuilder();
            if (message.hasDialogMessage()){
                errorTextBuilder.append(message.getDialogMessage());                
            }
            final String details = message.getDetails();
            if (details!=null && !details.isEmpty()){
                if (errorTextBuilder.length()>0){
                    errorTextBuilder.append('\n');
                }
                errorTextBuilder.append(details);
                errorTextBuilder.append('\n');
                errorTextBuilder.append(message.getTrace());
            }
            this.error = errorTextBuilder.toString();
            if (text != null) {
                text.setText(this.error);
            }                
            if (ClientException.isSystemFault(exception)){
                getEnvironment().processException(exception);
            }   
            detailsButton.setVisible(true);
        }
    }
}
