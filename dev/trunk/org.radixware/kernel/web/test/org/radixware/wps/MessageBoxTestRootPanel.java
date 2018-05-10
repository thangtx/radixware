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

package org.radixware.wps;

import java.util.EnumSet;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.*;
import org.radixware.wps.views.editors.valeditors.ValBoolEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValIntEditorController;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


class WaitDialogTestRootPanel extends RootPanel {

    private WpsEnvironment env;

    public WaitDialogTestRootPanel(WpsEnvironment env) {
        this.env = env;
    }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return env.getDialogDisplayer();
    }

    @Override
    public IMainView getExplorerView() {
        return null;
    }

    @Override
    public void closeExplorerView() {
    }

    @Override
    protected Runnable componentRendered(HttpQuery query) {
        return new Runnable() {

            @Override
            public void run() {
                createTestUI();
            }
        };
    }
    
    private void addEditorController(ValEditorController controller, int top, int left){
        final UIObject editor = (UIObject)controller.getValEditor();
        add(editor);
        editor.setTop(top);
        editor.setLeft(left);
    }

    private void createTestUI() {
        final VerticalBoxContainer mainLayout = new VerticalBoxContainer();
        {
            mainLayout.setTop(20);
            mainLayout.setLeft(200);
            mainLayout.setVSizePolicy(SizePolicy.EXPAND);
            mainLayout.setWidth(600);            
            add(mainLayout);
        }
        final ValStrEditorController messageTitle;
        final ValListEditorController iconType;
        final ValBoolEditorController showDetails;
        final ValStrEditorController optionTitle;
        {
            final FormBox messageBoxSettings = new FormBox();
            {//title
                messageTitle = new ValStrEditorController(env);
                messageTitle.setMandatory(false);
                messageTitle.setValue("Message Title");
                messageBoxSettings.addLabledEditor("Title:", (UIObject)messageTitle.getValEditor());
            }            
            {//icon type
                final EditMaskList maskList = new EditMaskList();
                for (EDialogIconType type: EnumSet.allOf(EDialogIconType.class)){
                    maskList.addItem(type.getValue(), type.getValue());
                }
                iconType = new ValListEditorController(env, maskList);
                iconType.setMandatory(false);
                iconType.setValue(EDialogIconType.INFORMATION.getValue());
                messageBoxSettings.addLabledEditor("Icon type:", (UIObject)iconType.getValEditor());
            }
            {//Details button
                showDetails = new ValBoolEditorController(env);
                showDetails.setMandatory(true);
                showDetails.setValue(Boolean.FALSE);
                messageBoxSettings.addLabledEditor("Show details button:", (UIObject)showDetails.getValEditor());
            }
            {//Options title
                optionTitle = new ValStrEditorController(env);
                optionTitle.setMandatory(false);
                messageBoxSettings.addLabledEditor("Option title:", (UIObject)optionTitle.getValEditor());
            }
            mainLayout.add(messageBoxSettings);
            mainLayout.addSpace();
        }        
        final CheckBox cbRandomContent = new CheckBox();
        final ValIntEditorController linesInMessage;
        final ValIntEditorController numberOfSymbols;
        final ValBoolEditorController isHtml;
        final TextArea customMessageText = new TextArea();
        {
            cbRandomContent.setText("Random content");
            cbRandomContent.setSelected(true);
            mainLayout.add(cbRandomContent);
            mainLayout.addSpace();
            final FormBox messageTextSettings = new FormBox();
            {//Lines in message
                linesInMessage = new ValIntEditorController(env);
                final EditMaskInt mask = new EditMaskInt();
                mask.setMinValue(1);
                linesInMessage.setEditMask(mask);
                linesInMessage.setValue(Long.valueOf(1));
                linesInMessage.setMandatory(true);
                messageTextSettings.addLabledEditor("Lines in message:", (UIObject)linesInMessage.getValEditor());
            }
            {//Number of symbols
                numberOfSymbols = new ValIntEditorController(env);
                numberOfSymbols.setMandatory(false);
                final EditMaskInt mask = new EditMaskInt();
                mask.setMinValue(1);
                numberOfSymbols.setEditMask(mask);                                
                messageTextSettings.addLabledEditor("Number of symbols in first line:", (UIObject)numberOfSymbols.getValEditor());
            }
            {//is Html
                isHtml = new ValBoolEditorController(env);
                isHtml.setMandatory(true);
                isHtml.setValue(Boolean.FALSE);
                messageTextSettings.addLabledEditor("Is HTML:", (UIObject)isHtml.getValEditor());                
            }
            mainLayout.add(messageTextSettings);
            mainLayout.addSpace();
            final Label lbCustmoMessage = new Label("Custom message text:");
            lbCustmoMessage.setEnabled(false);
            mainLayout.add(lbCustmoMessage);            
            customMessageText.setEnabled(false);
            customMessageText.setHeight(400);
            mainLayout.add(customMessageText);
            mainLayout.addSpace();
            cbRandomContent.addSelectionStateListener(new CheckBox.SelectionStateListener() {
                @Override
                public void onSelectionChange(CheckBox cb) {
                    messageTextSettings.setEnabled(cb.isSelected());
                    customMessageText.setEnabled(!cb.isSelected());
                    lbCustmoMessage.setEnabled(!cb.isSelected());
                }
            });            
        }
        final PushButton pbShowMessage = new PushButton("Show Message");
        mainLayout.add(pbShowMessage);
        pbShowMessage.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(final IButton source) {
                final String message;
                if (cbRandomContent.isSelected()){
                    final StringBuilder messageBuilder = new StringBuilder();
                    if (isHtml.getValue()==Boolean.TRUE){
                        messageBuilder.append("<p>");
                    }
                    int line;
                    if (numberOfSymbols.getValue()!=null){
                        line = 2;
                        for (int i=1,count=numberOfSymbols.getValue().intValue(); i<=count; i++){
                            messageBuilder.append('w');
                        }
                    }else{
                        line = 1;
                    }
                    for (int count=linesInMessage.getValue().intValue();line<=count;line++){
                        if (line>1){
                            messageBuilder.append('\n');
                        }
                        messageBuilder.append("line ");
                        messageBuilder.append(String.valueOf(line));
                    }
                    if (isHtml.getValue()==Boolean.TRUE){
                        messageBuilder.append("<p>");
                    }                    
                    message = messageBuilder.toString();
                }else{
                    message = customMessageText.getText();
                }
                
                final EDialogIconType type = 
                    iconType.getValue()==null ? null : EDialogIconType.getForValue((String)iconType.getValue());
                final boolean details = showDetails.getValue()==Boolean.TRUE;
                final MessageBox messageBox = new MessageBox(env.getDialogDisplayer(), 
                                                             messageTitle.getValue(), 
                                                             message,
                                                             details ? "Show Details" : null, 
                                                             details ? "Details" : null, 
                                                             EnumSet.of(EDialogButtonType.CLOSE),
                                                             type);
                if (optionTitle.getValue()!=null){
                    messageBox.setOptionText(optionTitle.getValue());
                }
                messageBox.execMessageBox();
            }
        });
    }
}
