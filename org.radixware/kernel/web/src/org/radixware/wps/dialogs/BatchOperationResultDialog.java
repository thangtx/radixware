/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.dialogs;

import java.awt.Color;
import java.util.List;
import org.radixware.kernel.common.client.dialogs.IBatchOperationResultDialog;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.AbstractBatchOperationResult;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ListBox;
import org.radixware.wps.rwt.TextArea;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.text.WpsTextOptions;


public class BatchOperationResultDialog extends Dialog implements IBatchOperationResultDialog{
    
    private static final class ExceptionInfoDialog extends Dialog{
        
        public ExceptionInfoDialog(final WpsEnvironment environment,  final AbstractBatchOperationResult.ExceptionInfo exceptionInfo){
            super(environment,"",false);
            final MessageProvider mp = getEnvironment().getMessageProvider();
            setWindowTitle(mp.translate("Selector", "Error Information"));
            final Icon icon = getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.ERROR);
            setWindowIcon(icon);            
            final TextArea messageText = new TextArea();
            add(messageText);
            messageText.setLineWrap(false);
            messageText.setReadOnly(true);

            messageText.setTop(3);
            messageText.setLeft(3);
            messageText.getAnchors().setRight(new Anchors.Anchor(1, -3));
            messageText.getAnchors().setBottom(new Anchors.Anchor(1, -3));
            messageText.setText(exceptionInfo.getExceptionStackTrace());
            addCloseAction(EDialogButtonType.CLOSE);
            setWidth(400);
            setHeight(300);
        }
        
    }
        
    private final Label lbMessage = new Label();
    private final ListBox resultList = new ListBox();        
    
    public BatchOperationResultDialog(final WpsEnvironment environment,  final AbstractBatchOperationResult result){
        super(environment,"",false);
        setupUi(result);
    }
    
    private void setupUi(final AbstractBatchOperationResult result){
        final VerticalBoxContainer layout = new VerticalBoxContainer();
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("Selector", "Warning"));
        final Icon icon = getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.WARNING);
        setWindowIcon(icon);        
        layout.getAnchors().setLeft(new Anchors.Anchor(0, 3));
        layout.getAnchors().setRight(new Anchors.Anchor(1, -3));
        layout.getAnchors().setTop(new Anchors.Anchor(0, 3));
        layout.getAnchors().setBottom(new Anchors.Anchor(1, -3));
        add(layout);
        lbMessage.setText(mp.translate("Selector", "Following objects was not processed:"));
        final WpsTextOptions messageTextOptions = 
            WpsTextOptions.getDefault((WpsEnvironment)getEnvironment()).increaseFontSize(2)
            .changeFontWeight(EFontWeight.BOLD).changeBackgroundColor(null).changeAlignment(ETextAlignment.CENTER);
        lbMessage.setTextOptions(messageTextOptions);
        layout.setAlignment(Alignment.CENTER);
        layout.add(lbMessage);
        layout.addSpace();
        layout.addSpace();
        layout.add(resultList);
        resultList.setBorderBoxSizingEnabled(true);
        resultList.setBorderColor(Color.BLACK);
        resultList.getHtml().setCss("border-width", "1px");
        resultList.getHtml().setCss("border-style", "solid");
        resultList.getHtml().setCss("background-color", "inherit");
        fillResultList(result);
        resultList.addDoubleClickListener(new ListBox.DoubleClickListener() {
            @Override
            public void itemDoubleClick(final ListBox.ListBoxItem item) {
                BatchOperationResultDialog.this.onDoubleClick(item);
            }
        });
        layout.setAutoSize(resultList, true);
        setMinimumWidth(500);
        setMinimumHeight(300);        
        addButton(EDialogButtonType.CLOSE);        
    }
    
    @SuppressWarnings("unchecked")
    private void fillResultList(final AbstractBatchOperationResult result){
        final List<String> rejections = result.getRejectedObjectPids();
        for (String rejection: rejections){            
            final String message = result.getRejectionMessage(rejection);            
            if (message!=null && !message.isEmpty()){
                final ListBox.ListBoxItem resultItem = new ListBox.ListBoxItem();
                resultItem.setText(message);
                resultItem.setTextWrapDisabled(true);
                final AbstractBatchOperationResult.ExceptionInfo exceptionInfo = result.getExceptionInfo(rejection);
                if (exceptionInfo!=null){
                    resultItem.setUserData(exceptionInfo);                    
                }
                resultList.add(resultItem);
            }
        }
    }
    
    @Override
    public void setMessage(final String messageText){
        lbMessage.setText(messageText);
    }

    private void onDoubleClick(final ListBox.ListBoxItem item){
        if (item.getUserData() instanceof AbstractBatchOperationResult.ExceptionInfo){
            final AbstractBatchOperationResult.ExceptionInfo exceptionInfo = 
                (AbstractBatchOperationResult.ExceptionInfo)item.getUserData();
            new ExceptionInfoDialog((WpsEnvironment)getEnvironment(), exceptionInfo).execDialog();
        }        
    }
}
