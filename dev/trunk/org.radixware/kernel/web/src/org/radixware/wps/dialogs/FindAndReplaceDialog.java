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
import org.radixware.kernel.common.client.dialogs.IFindAndReplaceDialog;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.rwt.*;


public class FindAndReplaceDialog extends Dialog implements IFindAndReplaceDialog{
    
    private static final class PresenterImpl extends IFindAndReplaceDialog.Presenter<FindAndReplaceDialog>{
               
        private IFindAndReplaceDialog.ISearchWidget customSearchWidget;
        
        private final FormBox     formBox = new FormBox();
        private final PushButton  pbFind = new PushButton();
        private final PushButton  pbReplace = new PushButton();        
        private final PushButton  pbReplaceAll = new PushButton();
        private final TextField   tfFindWhat = new TextField();
        private final TextField   tfReplaceWith = new TextField();
        private final CheckBox    cbMatchCase = new CheckBox();
        private final CheckBox    cbWholeWord  = new CheckBox();
        private final RadioButton rbForward = new RadioButton();
        private final RadioButton rbBackward = new RadioButton();
        private String findLabel;
        private UIObject searchWidget;
        private UIObject spReplaceButton;
        private UIObject spReplaceAllButton;

        public PresenterImpl(final FindAndReplaceDialog dialog){
            super(dialog);
        }
        
        public final void addFindClickListener(final IButton.ClickHandler listener){
            pbFind.addClickHandler(listener);            
        }
        
        public final void addReplaceClickListener(final IButton.ClickHandler listener){
            pbReplace.addClickHandler(listener);
        }

        public final void addReplaceAllClickListener(final IButton.ClickHandler listener){
            pbReplaceAll.addClickHandler(listener);
        }

        @Override
        protected void createUi(final boolean canReplace) {
            
            final MessageProvider mp = dialog.getEnvironment().getMessageProvider();            
            final TableLayout mainContainer = new TableLayout();     
            mainContainer.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
            final TableLayout.Row mainContainerRow = mainContainer.addRow();            
            {
                final TableLayout tlContent = new TableLayout();
                tlContent.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
                {                    
                    tfFindWhat.getHtml().setCss("margin-right", null);
                    tfFindWhat.setFocused(true);
                    searchWidget = tfFindWhat;
                    findLabel = mp.translate("FindAndReplaceDialog", "Find What:");
                    formBox.addLabledEditor(findLabel, tfFindWhat);
                    tfReplaceWith.getHtml().setCss("margin-right", null);
                    formBox.addLabledEditor(mp.translate("FindAndReplaceDialog", "Replace With:"), tfReplaceWith);
                    tlContent.addRow().addCell().add(formBox);
                }
                tlContent.addVerticalSpace();
                {                    
                    final TableLayout tlOptions = new TableLayout();
                    final TableLayout.Row rowOptions = tlOptions.addRow();
                    {
                        final GroupBox gbOptions = new GroupBox();
                        gbOptions.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
                        cbMatchCase.setText(mp.translate("FindAndReplaceDialog", "Match Case"));
                        cbMatchCase.getHtml().setCss("white-space","nowrap");
                        gbOptions.add(cbMatchCase);                        
                        cbWholeWord.setText(mp.translate("FindAndReplaceDialog", "Whole Word"));
                        cbWholeWord.getHtml().setCss("white-space","nowrap");
                        gbOptions.add(cbWholeWord);
                        gbOptions.setMinimumHeight(75);      
                        gbOptions.getHtml().setCss("max-height", "75px");
                        gbOptions.setTitle(mp.translate("FindAndReplaceDialog", "Options:"));             
                        gbOptions.setTitleAlign(Alignment.CENTER);
                        rowOptions.addCell().add(gbOptions);
                    }
                    rowOptions.addSpace();
                    {
                        final GroupBox gbDirection = new GroupBox();
                        gbDirection.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
                        RadioButton.Group directionGroup = new RadioButton.Group();
                        rbForward.setText(mp.translate("FindAndReplaceDialog", "Forward"));
                        rbForward.setGroup(directionGroup);
                        gbDirection.add(rbForward);                        
                        rbBackward.setText(mp.translate("FindAndReplaceDialog", "Backward"));
                        rbBackward.setGroup(directionGroup);
                        gbDirection.add(rbBackward);
                        gbDirection.setMinimumHeight(75);
                        gbDirection.getHtml().setCss("max-height", "75px");
                        gbDirection.setTitle(mp.translate("FindAndReplaceDialog", "Direction:"));
                        gbDirection.setTitleAlign(Alignment.CENTER);
                        rowOptions.addCell().add(gbDirection);
                    }
                    tlContent.addRow().addCell().add(tlOptions);
                }
                
                final AbstractContainer contentContainer = new AbstractContainer();
                contentContainer.add(tlContent);
                contentContainer.setVSizePolicy(SizePolicy.EXPAND);
                mainContainerRow.addCell().add(contentContainer);
            }
            mainContainerRow.addSpace(5);
            {
                final VerticalBox buttonsContainer = new VerticalBox();
                buttonsContainer.setSizePolicy(SizePolicy.MINIMUM_EXPAND, SizePolicy.EXPAND);
                pbFind.setText(mp.translate("FindAndReplaceDialog", "Find"));
                buttonsContainer.add(pbFind);                
                pbReplace.setText(mp.translate("FindAndReplaceDialog", "Replace"));                
                spReplaceButton = buttonsContainer.addSpace();
                buttonsContainer.add(pbReplace);                
                pbReplaceAll.setText(mp.translate("FindAndReplaceDialog", "Replace All"));
                spReplaceAllButton = buttonsContainer.addSpace();
                buttonsContainer.add(pbReplaceAll);                
                final PushButton pbClose = new PushButton(mp.translate("FindAndReplaceDialog", "Close"));
                pbClose.addClickHandler(new IButton.ClickHandler() {
                    @Override
                    public void onClick(IButton source) {
                        dialog.rejectDialog();
                    }
                });
                buttonsContainer.addSpace();
                buttonsContainer.add(pbClose);                
                TableLayout.Row.Cell buttonsCell = mainContainerRow.addCell();
                buttonsCell.add(buttonsContainer);
                //buttonsCell.setAutoExpandContent(true);
            }
            mainContainer.setTop(3);
            mainContainer.setLeft(3);
            mainContainer.getAnchors().setBottom(new Anchors.Anchor(1, 0));
            dialog.add(mainContainer);
            dialog.setAutoHeight(true);
            dialog.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
            dialog.setAutoWidth(true);
            dialog.setResizable(false);
            setCanReplace(dialog,canReplace);
            final TextField.TextChangeListener textChangeListener = 
                new TextField.TextChangeListener() {
                    @Override
                    public void textChanged(final String oldText, final String newText) {
                        refresh();
                    }
                };
            tfFindWhat.addTextListener(textChangeListener);
            tfReplaceWith.addTextListener(textChangeListener);
        }

        @Override
        protected void loadState(final ClientSettings settings, final String configKey, final State state) {
            cbMatchCase.setSelected(state.isMatchCase());
            cbWholeWord.setSelected(state.isWholeWord());
            if (state.isForward()){
                rbForward.setSelected(true);                
            }
            else{
                rbBackward.setSelected(true);
            }
            tfFindWhat.setText(state.getFindWhat());
            tfReplaceWith.setText(state.getReplaceWith());
        }   
        
        public final void setCanReplace(final FindAndReplaceDialog dialog, final boolean canReplace){
            tfReplaceWith.setVisible(canReplace);
            spReplaceButton.setVisible(canReplace);
            pbReplace.setVisible(canReplace);
            spReplaceAllButton.setVisible(canReplace);
            pbReplaceAll.setVisible(canReplace);
            final MessageProvider mp = dialog.getEnvironment().getMessageProvider();
            if (canReplace){
                dialog.setWindowTitle(mp.translate("FindAndReplaceDialog", "Replace"));
                findLabel = mp.translate("FindAndReplaceDialog", "Find What:");
            }
            else{
                dialog.setWindowTitle(mp.translate("FindAndReplaceDialog", "Find"));
                findLabel = mp.translate("FindAndReplaceDialog", "Find:");
            }
            formBox.changeEditorLabel(searchWidget, findLabel);
            refresh();
        }
        
        public final void refresh(){
        }
        
        public final void addSearchParameter(final String label, final UIObject widget){
            formBox.addLabledEditor(label, widget);
        }        
                
        public final void setSearchWidget(final IFindAndReplaceDialog.ISearchWidget customWidget){
            if (customWidget!=null && customSearchWidget==null){
                tfFindWhat.setVisible(false);
                searchWidget = (UIObject)customWidget;
                formBox.addLabledEditor(findLabel, searchWidget);
                searchWidget.setVisible(true);
                customSearchWidget = customWidget;
                customSearchWidget.setSearchString(tfFindWhat.getText());
            }
            else if (customWidget==null && customSearchWidget!=null){
                customSearchWidget.setVisible(false);
                tfFindWhat.setVisible(true);
                searchWidget = tfFindWhat;
                customSearchWidget = null;
            }
            refresh();
        }        
        
        public final  boolean isMatchCaseChecked() {
            return cbMatchCase.isSelected();
        }

        public final boolean isWholeWordChecked() {
            return cbWholeWord.isSelected();
        }

        public final boolean isForwardChecked() {
            return rbForward.isSelected();
        }

        public final String getFindWhat() {
            final String result = 
                customSearchWidget==null ? tfFindWhat.getText() : customSearchWidget.getSearchString();
            return result==null ? "" : result;
        }

        public final String getReplaceWith(){
            return tfReplaceWith.getText()==null ? "" : tfReplaceWith.getText();
        }        
    }
    
    private final PresenterImpl presenter = new PresenterImpl(this);
    private final Controller controller;

    public FindAndReplaceDialog(final IClientEnvironment environment,final String configPrefix, final boolean canReplace){
        super(environment, "");
        controller = new Controller(this, presenter, environment.getConfigStore(), configPrefix, canReplace);
        presenter.addFindClickListener(new IButton.ClickHandler() {
                @Override
                public void onClick(final IButton source) {
                    controller.notifyFindActionListeners();
                }
            }
        );        
        presenter.addReplaceClickListener(new IButton.ClickHandler() {
                @Override
                public void onClick(final IButton source) {
                    controller.notifyReplaceActionListeners();
                }
            }
        );
        presenter.addReplaceAllClickListener(new IButton.ClickHandler() {
                @Override
                public void onClick(final IButton source) {
                    controller.notifyReplaceAllActionListeners();
                }
            }
        );
        addBeforeCloseListener(new Dialog.BeforeCloseButtonListener(){
                @Override
                public boolean beforeClose(final EDialogButtonType button, final DialogResult result) {
                    controller.beforeClose();
                    return true;
                }            
            }
        );
    }
    
    @Override
    public void addFindActionListener(final IFindActionListener listener) {
        controller.addFindActionListener(listener);
    }

    @Override
    public void removeFindActionListener(final IFindActionListener listener) {
        controller.removeFindActionListener(listener);
    }

    @Override
    public void addReplaceActionListener(final IReplaceActionListener listener) {
        controller.addReplaceActionListener(listener);
    }

    @Override
    public void removeReplaceActionListener(final IReplaceActionListener listener) {
        controller.removeReplaceActionListener(listener);
    }

    @Override
    public void addSearchParameter(final String label, final IWidget widget) {
        presenter.addSearchParameter(label, (UIObject)widget);
    }

    @Override
    public void setSearchWidget(final ISearchWidget customWidget) {
        presenter.setSearchWidget(customWidget);                
    }

    @Override
    public boolean isMatchCaseChecked() {
        return presenter.isMatchCaseChecked();
    }

    @Override
    public boolean isWholeWordChecked() {
        return presenter.isWholeWordChecked();
    }

    @Override
    public boolean isForwardChecked() {
        return presenter.isForwardChecked();
    }

    @Override
    public String getFindWhat() {
        return presenter.getFindWhat();
    }

    @Override
    public String getReplaceWith() {
        return presenter.getReplaceWith();
    }

    @Override
    public boolean match(final String text) {
        return controller.match(text);
    }
            
    
}
