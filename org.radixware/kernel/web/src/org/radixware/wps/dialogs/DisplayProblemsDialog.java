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

package org.radixware.wps.dialogs;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IDisplayProblemsDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.exceptions.IProblemHandler;
import org.radixware.kernel.common.client.exceptions.StandardProblemHandler;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.*;


public class DisplayProblemsDialog extends Dialog implements IDisplayProblemsDialog{
    
    private final TableLayout problemsList = new TableLayout();
    
    private static class DisplayProblemWidget extends Container{
        public DisplayProblemWidget(final IClientEnvironment environment, final Exception exception, final String title){       
            setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            final ExceptionMessage exceptionMessage = new ExceptionMessage(environment,exception);
            {
                final boolean isError = exceptionMessage.getSeverity().getValue()>EEventSeverity.WARNING.getValue();                
                final Image image = new Image();
                image.setWidth(30);
                image.setHeight(30);
                image.setLeft(10);
                image.setTop(15);
                final ClientIcon icon = isError ? ClientIcon.Message.ERROR : ClientIcon.Message.INFORMATION;
                image.setIcon((WpsIcon) environment.getApplication().getImageManager().getIcon(icon));
                add(image);                
            }
            {                
                final StaticText messageText = new StaticText();
                if (title!=null && !title.isEmpty()){
                    messageText.setText(title+":\n"+exceptionMessage.getDialogMessage());
                }
                else{
                    messageText.setText(exceptionMessage.getDialogMessage());
                }
                
                final boolean hasDetails = exceptionMessage.getDetails() != null && !exceptionMessage.getDetails().isEmpty();
                final boolean hasTrace = exceptionMessage.getTrace() != null && !exceptionMessage.getTrace().isEmpty();

                final String exceptionDetails;
                if (hasDetails || hasTrace){
                    if (hasTrace){
                        exceptionDetails = 
                            exceptionMessage.getDetails() + "\n\nStack trace:\n" + exceptionMessage.getTrace();
                    }
                    else{
                        exceptionDetails = exceptionMessage.getDetails();
                    }
                }
                else{
                    exceptionDetails = "";
                }

                if (!exceptionDetails.isEmpty()) {
                    final HrefButton button = new HrefButton(environment.getMessageProvider().translate("ExceptionDialog", "show details"));

                    button.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(final IButton source) {
                            Dialog exceptionDialog = new Dialog(environment, environment.getMessageProvider().translate("ExceptionDialog", "Exceptions Details"));

                            StaticText messageText = new StaticText();
                            exceptionDialog.add(messageText);
                            exceptionDialog.addCloseAction(EDialogButtonType.CLOSE).setDefault(true);
                            messageText.setTop(3);
                            messageText.setLeft(3);
                            messageText.getAnchors().setRight(new Anchors.Anchor(1, -3));
                            messageText.getAnchors().setBottom(new Anchors.Anchor(1, -3));
                            messageText.setText(exceptionDetails);

                            exceptionDialog.setWidth(400);
                            exceptionDialog.setHeight(300);
                            exceptionDialog.getHtml().setAttr("dlgId", "exception_details");
                            exceptionDialog.execDialog();
                        }
                    });
                    messageText.appendButton(button);
                    add(messageText);
                    messageText.setTop(10);
                    messageText.setLeft(50);
                    messageText.getAnchors().setRight(new Anchors.Anchor(1, -3));
                    messageText.getAnchors().setBottom(new Anchors.Anchor(1, -3));                    
                }
            }
        }
    }
    
    public DisplayProblemsDialog(final IClientEnvironment environment, final IDialogDisplayer dialogDisplayer, final StandardProblemHandler problemHandler){
        super(dialogDisplayer,"");            
        
        setAutoWidth(true);
        setMaxWidth(640);
        setMinimumWidth(480);
        setAutoHeight(true);
        setMaxHeight(450);
                
        String problemTitle;
        IProblemHandler.ProblemSource problemSource;
        int problemsCount = 0;
        for (IProblemHandler.Problem problem: problemHandler){
            problemSource = problemHandler.getProblemSource(problem);
            problemTitle = problemSource.getOperationDescription();
            if (problemTitle!=null && !problemTitle.isEmpty() && 
                problemSource.getSourceDescription()!=null && 
                !problemSource.getSourceDescription().isEmpty()){
                problemTitle+=" "+problemSource.getSourceDescription();
            }        
            addProblem(environment, problem.getException(), problemTitle);
            problemsCount++;
        }        
        add(problemsList);
        problemsList.getAnchors().setRight(new Anchors.Anchor(1, -3));        
        problemsList.getHtml().setCss("margin-top", Integer.valueOf(problemsCount*-2)+"px");
        addCloseAction(EDialogButtonType.CLOSE).setDefault(true);
    }
    
    private void addProblem(final IClientEnvironment environment, final Exception exception, final String title){
        final TableLayout.Row row = problemsList.addRow();
        final TableLayout.Row.Cell cell = row.addCell();
        
        final ExceptionMessage exceptionMessage = new ExceptionMessage(environment,exception);
        {
            final boolean isError = exceptionMessage.getSeverity().getValue()>EEventSeverity.WARNING.getValue();                
            final Image image = new Image();            
            image.setWidth(30);
            image.setHeight(30);
            image.setLeft(10);
            image.setTop(15);
            final ClientIcon icon = isError ? ClientIcon.Message.ERROR : ClientIcon.Message.INFORMATION;
            image.setIcon((WpsIcon) environment.getApplication().getImageManager().getIcon(icon));
            cell.add(image);
            image.getHtml().setCss("float", "left");
            image.getHtml().setCss("position", "relative");
        }
        {
            final StaticText messageText = new StaticText();
            if (title!=null && !title.isEmpty()){
                messageText.setText(title+":\n"+exceptionMessage.getDialogMessage());
            }
            else{
                messageText.setText(exceptionMessage.getDialogMessage());
            }

            final boolean hasDetails = exceptionMessage.getDetails() != null && !exceptionMessage.getDetails().isEmpty();
            final boolean hasTrace = exceptionMessage.getTrace() != null && !exceptionMessage.getTrace().isEmpty();

            final String exceptionDetails;
            if (hasDetails || hasTrace){
                if (hasTrace){
                    exceptionDetails = 
                        exceptionMessage.getDetails() + "\n\nStack trace:\n" + exceptionMessage.getTrace();
                }
                else{
                    exceptionDetails = exceptionMessage.getDetails();
                }
            }
            else{
                exceptionDetails = "";
            }

            if (!exceptionDetails.isEmpty()) {
                final HrefButton button = new HrefButton(environment.getMessageProvider().translate("ExceptionDialog", "show details"));

                button.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(final IButton source) {
                        Dialog exceptionDialog = new Dialog(environment, environment.getMessageProvider().translate("ExceptionDialog", "Exceptions Details"));

                        StaticText messageText = new StaticText();
                        exceptionDialog.add(messageText);
                        exceptionDialog.addCloseAction(EDialogButtonType.CLOSE).setDefault(true);
                        messageText.setTop(3);
                        messageText.setLeft(3);
                        messageText.getAnchors().setRight(new Anchors.Anchor(1, -3));
                        messageText.getAnchors().setBottom(new Anchors.Anchor(1, -3));
                        messageText.setText(exceptionDetails);

                        exceptionDialog.setWidth(400);
                        exceptionDialog.setHeight(300);
                        exceptionDialog.getHtml().setAttr("dlgId", "exception_details");
                        exceptionDialog.execDialog();
                    }
                });
                messageText.appendButton(button);
                cell.add(messageText);                
                messageText.setTop(10);
                messageText.setLeft(20);
                messageText.getHtml().setCss("position", "relative");
                messageText.getHtml().setCss("width", "90%");
            }
        }        
    }
    
}