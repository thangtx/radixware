/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
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

import java.util.List;
import org.radixware.kernel.common.client.dialogs.IMandatoryPropertiesConfirmationDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.CheckBox;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.GridBoxContainer;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.TableLayout;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBoxContainer;


public final class MandatoryPropertiesConfirmationDialog extends Dialog implements IMandatoryPropertiesConfirmationDialog{
    
    private final WpsEnvironment env;
    private final Label lbQuestion = new Label();
    private final TableLayout tlMandatoryProps = new TableLayout();    
    private final CheckBox cbDontAskAgain = new CheckBox();
    private final GridBoxContainer mainLayout = new GridBoxContainer();
    
    private List<String> propertyTitles;
    private boolean showDontAskAgain = true;
    
    public MandatoryPropertiesConfirmationDialog(final WpsEnvironment environment) {
        super(environment.getDialogDisplayer(),null,false);
        env = environment;
        setupUi();
    }
    
    private void setupUi(){
        final MessageProvider mp = env.getMessageProvider();
        setHeight(40);
        setAutoHeight(true);
        setAutoWidth(true);
        setResizable(false);
        setMaxWidth(640);
        setMaxHeight(480);
        setWindowIcon(env.getApplication().getImageManager().getIcon(ClientIcon.Message.CONFIRMATION));        
        {                            
            final Label lbHeader  = new Label(mp.translate("Editor", "The following mandatory properties are not defined: "));
            lbHeader.setVSizePolicy(SizePolicy.MINIMUM_EXPAND);
            lbHeader.setTextWrapDisabled(true);
            lbHeader.setAlign(Alignment.CENTER);            
            addToLayout(lbHeader);
            mainLayout.setRowHeight(0, 17);
            addSpace();
        }
        {
            final Container contentContainer = new Container();
            contentContainer.add(tlMandatoryProps);
            contentContainer.getHtml().setCss("overflow", "auto");
            addToLayout(contentContainer);
            addSpace();
        }
        {            
            lbQuestion.setVSizePolicy(SizePolicy.MINIMUM_EXPAND);
            lbQuestion.setTextWrapDisabled(true);
            lbQuestion.setAlign(Alignment.CENTER);
            addToLayout(lbQuestion);
            mainLayout.setRowHeight(mainLayout.getRowCount()-1, 17);
            addSpace();
        }                    
        {
            cbDontAskAgain.setText(mp.translate("Editor", "Don't ask me again"));
            addToLayout(cbDontAskAgain);
        }
        mainLayout.getAnchors().setTop(new Anchors.Anchor(0, 3));
        mainLayout.getAnchors().setLeft(new Anchors.Anchor(0, 3));
        mainLayout.getAnchors().setBottom(new Anchors.Anchor(1, -3));
        mainLayout.getAnchors().setRight(new Anchors.Anchor(1, -3));        
        add(mainLayout);
        addCloseAction(EDialogButtonType.YES);
        addCloseAction(EDialogButtonType.NO);
    }        

    private void addMandatoryProperty(final String propertyTitle){
        final TableLayout.Row row = tlMandatoryProps.addRow();
        final TableLayout.Row.Cell cell = row.addCell();
        final Label lbPropertyTitle = new Label(propertyTitle);
        cell.add(lbPropertyTitle);       
    }


    @Override
    public DialogResult execDialog(final IWidget parentWidget) {
        env.getProgressHandleManager().blockProgress();
        try{
            return super.execDialog(parentWidget);
        }finally{
            env.getProgressHandleManager().unblockProgress();
        }
    }

    @Override
    public DialogResult execDialog() {
        env.getProgressHandleManager().blockProgress();
        try{        
            return super.execDialog();
        }finally{
            env.getProgressHandleManager().unblockProgress();
        }
    }    

    @Override
    public void setPropertyTitles(final List<String> titles) {
        propertyTitles = titles;
        tlMandatoryProps.clearRows();
        for (String title: propertyTitles){
            addMandatoryProperty(title);
        }
    }

    @Override
    public List<String> getPropertyTitles() {
        return propertyTitles;
    }

    @Override
    public void setConfirmationMessage(final String text) {
        lbQuestion.setText(text);
    }

    @Override
    public String getConfirmationMessage() {
        return lbQuestion.getText();
    }

    @Override
    public void setDontAskAgainOptionVisible(final boolean isVisible) {         
        if (showDontAskAgain!=isVisible){
            showDontAskAgain = isVisible;
            if (isVisible){
                addSpace();
                addToLayout(cbDontAskAgain);
            }else{
                removeLastLayoutItem();
                removeLastLayoutItem();
            }           
        }
    }
    
    private void addSpace(){
        final UIObject space = new UIObject(new Div()){};    
        mainLayout.add(space, mainLayout.getRowCount(), 0);
        mainLayout.setRowHeight(mainLayout.getRowCount()-1, 3);
    }
    
    private void addToLayout(final UIObject object){
        mainLayout.add(object, mainLayout.getRowCount(), 0);
    }
    
    private void removeLastLayoutItem(){
        mainLayout.removeRow(mainLayout.getRowCount()-1);
    }

    @Override
    public boolean isDontAskAgainOptionVisible() {
        return showDontAskAgain;
    }

    @Override
    public boolean isDontAskAgainOptionChecked() {
        return showDontAskAgain && cbDontAskAgain.isSelected();
    }        
}
