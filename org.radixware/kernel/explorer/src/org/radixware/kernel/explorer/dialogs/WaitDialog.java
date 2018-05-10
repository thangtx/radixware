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

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.CursorShape;
import com.trolltech.qt.core.Qt.WindowFlags;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QProgressBar;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.progress.IProgressView;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import static org.radixware.kernel.explorer.utils.WidgetUtils.isGnome3Classic;

public final class WaitDialog extends QDialog implements IProgressView{
    
    private QWidget focusedWidget;
    public final Signal0 canceled = new Signal0();
    private final QVBoxLayout layout = new QVBoxLayout(this);
    private final QLabel label = new QLabel(this);
    private final QProgressBar progressBar = new QProgressBar(this);
    private final IClientEnvironment environment;
    private final QPushButton btnCancel;

    private IProgressHandle lastProgress;
    private int lastWidth = -1;

    public WaitDialog(final IClientEnvironment env){
        super();
        environment = env;
        btnCancel = new QPushButton(env.getMessageProvider().translate("Wait Dialog", "Cancel"), this);
        setupWindow((QWidget)env.getMainWindow());
        setupUi();
    }

    private void setupUi(){
        label.setAlignment(Qt.AlignmentFlag.AlignHCenter);
        label.setObjectName("lbText");
        layout.addWidget(label);
        progressBar.setObjectName("progressBar");
        layout.addWidget(progressBar);
        btnCancel.setObjectName("btnCancel");
        btnCancel.clicked.connect(this,"onCancel()");
        layout.addWidget(btnCancel, 0, Qt.AlignmentFlag.AlignHCenter);
        setLayout(layout);        
    }

    @Override
    protected void changeEvent(final QEvent changeEvent) {
        if (changeEvent!=null && changeEvent.type()==QEvent.Type.LanguageChange){
            btnCancel.setText(environment.getMessageProvider().translate("Wait Dialog", "Cancel"));
            if (lastProgress!=null){
                updateForProgress(lastProgress);
            }            
        }
        super.changeEvent(changeEvent);
    }
    
    

    private void setupWindow(final QWidget parent){
        setParent(parent);
        final WindowFlags flags = new WindowFlags();
        flags.set(new WindowType[]{WidgetUtils.MODAL_DIALOG_WINDOW_TYPE,
                        WindowType.MSWindowsFixedSizeDialogHint,                        
                        WindowType.WindowTitleHint
                        });
        if (!SystemTools.isWindows){
            flags.set(WindowType.WindowSystemMenuHint);
        }
        if (isGnome3Classic()){
            flags.set(WindowType.WindowCloseButtonHint);
        }
        setWindowFlags(flags);
        setModal(true);
        this.setCursor(new QCursor(CursorShape.ArrowCursor));
    }

    @SuppressWarnings("unused")
    private void onCancel(){
        if (lastProgress!=null && lastProgress.canCancel()){
            btnCancel.setDisabled(true);
            lastProgress.cancel();
        }
    }

    @Override
    public void updateForProgress(final IProgressHandle progress) {
        final boolean newProgress = lastProgress!=progress;//NOPMD
        lastProgress = progress;
        setUpdatesEnabled(false);
        try{
            setWindowTitle(progress.getTitle());
            label.setText(progress.getText());
            progressBar.setMaximum(progress.getMaximumValue());
            progressBar.setValue(progress.getValue());
            progressBar.setTextVisible(progressBar.maximum()>0);
            if (progress.canCancel()){
                btnCancel.setDisabled(progress.wasCanceled());
                btnCancel.setVisible(true);
            }
            else{
                btnCancel.setVisible(false);
            }
        }
        finally{
            setUpdatesEnabled(true);
        }
        layout.activate();
        final int width = Math.max(sizeHint().width(), fontMetrics().width(progress.getTitle())+150);
        if (newProgress || lastWidth<width){
            setFixedSize(width, sizeHint().height());
            lastWidth = width;
        }
    }

    @Override
    public void finishProgress(final IProgressHandle progress) {
        if (progress==lastProgress){//NOPMD
            lastProgress = null;
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        event.ignore();//this dialog can be hidden, but cannot be closed
    }

    @Override
    protected void keyPressEvent(final QKeyEvent event) {
        if (event.key()!=Qt.Key.Key_Escape.value() && event.key()!=Qt.Key.Key_F4.value()){
            super.keyPressEvent(event);
        }
    }

/*    private QWidget activeWindow;

    @Override
    protected void showEvent(QShowEvent event) {
        activeWindow = QApplication.activeWindow();
        if (activeWindow!=null && activeWindow.nativeId()!=0)
            activeWindow.setUpdatesEnabled(false);
        super.showEvent(event);
    }

    @Override
    protected void hideEvent(QHideEvent qhe) {
        super.hideEvent(qhe);
        if (activeWindow!=null && activeWindow.nativeId()!=0){
            activeWindow.setUpdatesEnabled(true);
            activeWindow = null;
        }
    }
*/
    @Override
    public void hideProgress() {
        final boolean canRestoreFocus = QApplication.activeWindow()==this;
        hide();
        if (focusedWidget!=null
            && canRestoreFocus
            && focusedWidget.nativeId()!=0
            && QApplication.focusWidget()==null
            ){
            focusedWidget.setFocus();
        }
        focusedWidget = null;
    }

    @Override
    public void showProgress() {
        if (focusedWidget==null){
            focusedWidget = QApplication.focusWidget();            
        }
        show();
    }


}
