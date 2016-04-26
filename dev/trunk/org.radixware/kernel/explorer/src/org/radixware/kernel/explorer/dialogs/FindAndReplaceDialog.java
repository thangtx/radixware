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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.dialogs.IFindAndReplaceDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;


public class FindAndReplaceDialog extends QtDialog implements IFindAndReplaceDialog{
    
    private static class Icon extends ClientIcon.Dialog{
        private Icon(final String filename){
            super(filename,true);
        }
        public static final Icon FIND = new Icon("classpath:images/find.svg");
        public static final Icon REPLACE = new Icon("classpath:images/replace.svg");
    }
    
    private static final class PresenterImpl extends IFindAndReplaceDialog.Presenter<FindAndReplaceDialog>{
        
        final private static String GEOMETRY = "geometry";
        final private Ui_FindAndReplaceDialog ui = new Ui_FindAndReplaceDialog();        
        private IFindAndReplaceDialog.ISearchWidget searchWidget;
        
        public PresenterImpl(final FindAndReplaceDialog dialog){
            super(dialog);
        }

        @Override
        protected void createUi(final boolean canReplace) {
            ui.setupUi(dialog);
            ui.pbtFind.setIcon(ExplorerIcon.getQIcon(Icon.FIND));
            ui.pbReplace.setIcon(ExplorerIcon.getQIcon(Icon.REPLACE));
            ui.pbReplaceAll.setIcon(ExplorerIcon.getQIcon(Icon.REPLACE));
            ui.pbClose.setIcon(ExplorerIcon.getQIcon(Icon.EXIT));
            setCanReplace(dialog, canReplace); 
            dialog.layout().setSizeConstraint(QLayout.SizeConstraint.SetFixedSize);
            dialog.setWindowIcon(ExplorerIcon.getQIcon(Icon.FIND));
        }
        
        public final void connectFindClickSignal(final Object target, final String slot){
            ui.pbtFind.clicked.connect(target,slot);
        }
        
        public final void connectReplaceClickSignal(final Object target, final String slot){
            ui.pbReplace.clicked.connect(target,slot);
        }

        public final void connectReplaceAllClickSignal(final Object target, final String slot){
            ui.pbReplaceAll.clicked.connect(target,slot);
        }        

        @Override
        protected void loadState(final ClientSettings settings, final String configKey, final State state) {
            if (settings.contains(configKey+"/"+GEOMETRY)){
                dialog.restoreGeometry(((ExplorerSettings)settings).readQByteArray(configKey+"/"+GEOMETRY));
            }
            ui.checkBoxCase.setChecked(state.isMatchCase());
            ui.checkBoxWhole.setChecked(state.isWholeWord());            
            if (state.isForward()){
                ui.radioForward.setChecked(true);
            }
            else{
                ui.radioBackward.setChecked(true);
            }
            ui.leFindWhat.setText(state.getFindWhat());
            ui.leReplaceWith.setText(state.getReplaceWith());
        }

        @Override
        protected void saveState(ClientSettings settings, String configKey) {
            if (settings.contains(configKey+"/"+GEOMETRY)){
                dialog.restoreGeometry(((ExplorerSettings)settings).readQByteArray(configKey+"/"+GEOMETRY));
            }            
            super.saveState(settings, configKey);
        }                
        
        public final void setCanReplace(final FindAndReplaceDialog dialog, final boolean canReplace){
            ui.lbReplaceWith.setVisible(canReplace);
            ui.leReplaceWith.setVisible(canReplace);
            ui.pbReplace.setVisible(canReplace);
            ui.pbReplaceAll.setVisible(canReplace);
            if (canReplace){
                dialog.setWindowTitle(Application.translate("FindAndReplaceDialog", "Replace"));
                ui.lbFindWhat.setText(Application.translate("FindAndReplaceDialog", "&Find What:"));
            }
            else{
                dialog.setWindowTitle(Application.translate("FindAndReplaceDialog", "Find"));
                ui.lbFindWhat.setText(Application.translate("FindAndReplaceDialog", "&Find:"));
            }
            refresh();
        }

        public final void refresh(){
            ui.pbtFind.setEnabled(!dialog.getFindWhat().isEmpty());            
            ui.pbReplace.setEnabled(!dialog.getReplaceWith().isEmpty());
            ui.pbReplaceAll.setEnabled(!dialog.getReplaceWith().isEmpty());
        }
        
        public final void addSearchParameter(final QWidget label, final QWidget widget){                                               
            final int row = ui.loGrid.rowCount();
            if (label!=null)
                ui.loGrid.addWidget(label, row, 0);
            ui.loGrid.addWidget(widget, row, 1);
            final QWidget previousWidget = ui.loGrid.itemAtPosition(row-1, 1).widget();
            if (previousWidget!=null)
                QWidget.setTabOrder(previousWidget, widget);
        }
        
        public final void setSearchWidget(final IFindAndReplaceDialog.ISearchWidget customWidget){
            if (customWidget!=null && searchWidget==null){
                ui.loGrid.removeWidget(ui.leFindWhat);
                ui.leFindWhat.setVisible(false);
                ((QWidget)customWidget).setParent(dialog);
                ui.loGrid.addWidget((QWidget)customWidget, 0, 1);
                customWidget.setVisible(true);
                searchWidget = customWidget;
                searchWidget.setSearchString(ui.leFindWhat.text());            
            }
            else if (customWidget==null && searchWidget!=null){
                ui.loGrid.removeWidget((QWidget)searchWidget);
                searchWidget.setVisible(false);
                ui.loGrid.addWidget(ui.leFindWhat);
                ui.leFindWhat.setVisible(true);
                searchWidget = null;            
            }
            refresh();
        }        
        
        public final  boolean isMatchCaseChecked() {
                    return ui.checkBoxCase.checkState() == Qt.CheckState.Checked;
        }

        public final boolean isWholeWordChecked() {
                return ui.checkBoxWhole.checkState() == Qt.CheckState.Checked;
        }

        public final boolean isForwardChecked() {
                return ui.radioForward.isChecked();
        }

        public final String getFindWhat() {
            return searchWidget==null ? ui.leFindWhat.text() : searchWidget.getSearchString();
        }

        public final String getReplaceWith(){
            return ui.leReplaceWith.text();
        }
        
    }
    
    private final PresenterImpl presenter = new PresenterImpl(this);
    private final IFindActionListener defaultFindActionListener = new IFindActionListener() {
        @Override
        public void find() {
            FindAndReplaceDialog.this.find.emit();
        }
    };
    private final IReplaceActionListener defaultReplaceActionListener = new IReplaceActionListener() {

        @Override
        public void replace(final boolean replaceAll) {
            if (replaceAll){
                FindAndReplaceDialog.this.replaceAll.emit();
            }else{
                FindAndReplaceDialog.this.replace.emit();
            }
        }
    };
    
    private final Controller controller;
    
    final public Signal0 find= new Signal0();
    final public Signal0 replace= new Signal0();
    final public Signal0 replaceAll= new Signal0();
    
    public FindAndReplaceDialog(final QWidget parent, final ExplorerSettings settings, final String configPrefix, final boolean canReplace){
        //super(Application.getInstance().getEnvironment(),parent,null);
        super(parent);
        controller = new Controller(this, presenter, settings, configPrefix, canReplace);
        presenter.connectFindClickSignal(controller, "notifyFindActionListeners()");
        presenter.connectReplaceClickSignal(controller, "notifyReplaceActionListeners()");
        presenter.connectReplaceAllClickSignal(controller, "notifyReplaceAllActionListeners()");
        controller.addFindActionListener(defaultFindActionListener);
        controller.addReplaceActionListener(defaultReplaceActionListener);        
        finished.connect(controller, "beforeClose()");        
    }
/*
    @Override
    protected void loadGeometryFromConfig() {
    }

    @Override
    protected void saveGeometryToConfig() {        
    }        
*/
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
        final QLabel labelWidget = new QLabel(label, this);
        labelWidget.setBuddy((QWidget)widget);
        presenter.addSearchParameter(labelWidget, (QWidget)widget);        
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
    
    @SuppressWarnings("unused")
    //slot for ui.leFindWhat.textChanged
    private void on_leFindWhat_textChanged(){
        presenter.refresh();
    }

    @SuppressWarnings("unused")
    //slot for ui.leReplaceWith.textChanged
    private void on_leReplaceWith_textChanged(){
        presenter.refresh();
    }
    
    public void refresh(){
        presenter.refresh();
    }        
}