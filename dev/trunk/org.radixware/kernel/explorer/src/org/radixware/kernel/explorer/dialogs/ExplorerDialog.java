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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QLayout;
import java.util.EnumMap;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.views.IDialogWithStandardButtons;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.env.ExplorerSettings;

import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerDialogButtonBox;

public abstract class ExplorerDialog extends QtDialog implements IDialogWithStandardButtons {
    
    private class DialogLayout extends QVBoxLayout{
        
        public DialogLayout(final QWidget owner){
            super(owner);
        }

        @Override
        public QSize maximumSize() {
            return layoutMaximumSize(super.maximumSize());
        }
        
        @Override
        public QSize minimumSize() {
            return layoutMinimumSize(super.minimumSize());
        }        
                        
    }
    
    private ExplorerDialogButtonBox buttonBox;
    private IClientEnvironment environment;
    private final DialogSizeManager sizeManager;
    private boolean confirmOnReject;
    private boolean autoHeight;
    private boolean autoWidth;
    private final int defaultSizeX;
    private final int defaultSizeY;
    
    protected final com.trolltech.qt.QSignalEmitter.Signal0 acceptButtonClick = new com.trolltech.qt.QSignalEmitter.Signal0();
    protected final com.trolltech.qt.QSignalEmitter.Signal0 rejectButtonClick = new com.trolltech.qt.QSignalEmitter.Signal0();

    public ExplorerDialog(final IClientEnvironment environment, final QWidget parent) {
        this(environment, parent, null, 0, 0);
    }
    
    public ExplorerDialog(final IClientEnvironment environment, 
                          final QWidget parent, 
                          final String dlgName) {
        this(environment, parent, dlgName, 0, 0);
    }

    public ExplorerDialog(final IClientEnvironment environment, 
                          final QWidget parent, 
                          final String dlgName, 
                          final int defaultWidth, 
                          final int defaultHeight) {
        super(parent);
        this.environment = environment;
        final String key;
        if (dlgName==null){
            key = SettingNames.SYSTEM + "/" + getClass().getSimpleName();
        }else{
            key = SettingNames.SYSTEM + "/" + dlgName;
        }
        defaultSizeX = defaultWidth;
        defaultSizeY = defaultHeight;
        sizeManager = new DialogSizeManager((ExplorerSettings)environment.getConfigStore(), this, key);
        setupUi();
    }
    
    public ExplorerDialog(final IClientEnvironment environment, final QWidget parent, final boolean restoreGeometry) {
        super(parent);
        this.environment = environment;
        final String key;
        if (restoreGeometry){
            key = SettingNames.SYSTEM + "/" + getClass().getSimpleName();
        }else{
            key = null;
        }       
        defaultSizeX = 0;
        defaultSizeY = 0;
        sizeManager = new DialogSizeManager((ExplorerSettings)environment.getConfigStore(), this, key);
        setupUi();
    }
    
    private void setupUi(){
        loadGeometryFromConfig();
        finished.connect(this, "saveGeometryToConfig()");
        setLayout(new DialogLayout(this));
        setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);        
    }

    public final IClientEnvironment getEnvironment() {
        return environment;
    }
    
    @SuppressWarnings("unused")
    protected final void saveGeometryToConfig(){
        sizeManager.saveGeometry();
    }
    
    protected final void loadGeometryFromConfig(){
        sizeManager.loadGeometry(defaultSizeX, defaultSizeY);
    }

    public QVBoxLayout dialogLayout() {
        return (QVBoxLayout) layout();
    }

    public void forceClose() {
        confirmOnReject = false;
        reject();
    }

    @Override
    public int exec() {
        if (width() < minimumSizeHint().width()) {
            resize(new QSize(minimumSizeHint().width(), height()));
        }
        return super.exec();
    }

    @Override
    public void done(int result) {
        if (result==QDialog.DialogCode.Rejected.value() && confirmOnReject && !confirmOnReject()){
            return;
        }
        super.done(result);
    }
    
    protected boolean confirmOnReject(){        
        final String dialogTitle = 
            getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm to Close");
        final String dialogMessage = 
            getEnvironment().getMessageProvider().translate("ExplorerMessage", "Do you really want to close this dialog?");
        return getEnvironment().messageConfirmation(dialogTitle, dialogMessage);        
    }
    
    public final void setConfirmToReject(final boolean confirm){
        confirmOnReject = confirm;
    }
    
    public final boolean confirmToReject(){
        return confirmOnReject;
    }

    @Override
    public QSize sizeHint() {
        final QSize superSizeHint = super.sizeHint();
        if (superSizeHint.width() < minimumSizeHint().width()) {
            return new QSize(minimumSizeHint().width(), superSizeHint.height());
        }
        return superSizeHint;
    }

    @Override
    public QSize minimumSizeHint() {
        final QSize screenSize = QApplication.desktop().availableGeometry(QCursor.pos()).size();
        final int hardLimit = Math.min(screenSize.width() - 480, 1000);
        final QFontMetrics fm = fontMetrics();
        final int windowTitleWidth = Math.min(fm.width(windowTitle()) + 200, hardLimit);
        final QSize superSizeHint = super.minimumSizeHint();
        if (superSizeHint == null || !superSizeHint.isValid() || superSizeHint.width() < windowTitleWidth) {
            final int height = superSizeHint == null ? 0 : superSizeHint.height();
            return new QSize(windowTitleWidth, height);
        }
        return superSizeHint;
    }

    //Implementation of IDialog
    @Override
    public IPushButton addButton(final EDialogButtonType button) {
        if (buttonBox == null) {
            buttonBox = new ExplorerDialogButtonBox(getEnvironment(),this);
            buttonBox.accepted.connect(acceptButtonClick);
            buttonBox.rejected.connect(rejectButtonClick);
            buttonBox.setObjectName("buttonBox");
            dialogLayout().addWidget(buttonBox,0,Qt.AlignmentFlag.AlignBottom);
        }
        return (IPushButton)buttonBox.addButton(button);
    }
    
    public EnumMap<EDialogButtonType,IPushButton> addButtons(final EnumSet<EDialogButtonType> buttonTypes, final boolean autoConnect){
        final EnumMap<EDialogButtonType,IPushButton> buttons = new EnumMap<>(EDialogButtonType.class);
        for (EDialogButtonType buttonType: buttonTypes){
            buttons.put(buttonType, addButton(buttonType));
        }
        if (autoConnect){
            acceptButtonClick.connect(this, "accept()");
            rejectButtonClick.connect(this, "reject()");
        }
        return buttons;
    }

    @Override
    public void clearButtons() {
        if (buttonBox!=null){
            buttonBox.clearButtons();
        }
    }        

    @Override
    public IPushButton getButton(final EDialogButtonType button) {
        return buttonBox==null ? null : buttonBox.getButton(button);
    }

    @Override
    public void removeButton(final EDialogButtonType button) {
        if (buttonBox != null) {
            buttonBox.removeButton(button);
        }
    }

    public void setAutoHeight(final boolean isAuto) {
        if (autoHeight!=isAuto){
            autoHeight = isAuto;
            updateLayoutSizeConstraint();
            updateGeometry();            
        }
    }

    public boolean isAutoHeight() {
        return autoHeight;
    }
    
    public void setAutoWidth(final boolean isAuto) {
        if (autoWidth!=isAuto){
            autoWidth = isAuto;
            updateLayoutSizeConstraint();
            updateGeometry();
        }
    }        

    public boolean isAutoWidth() {
        return autoWidth;
    }

    public void setAutoSize(final boolean isAuto) {
        setAutoHeight(isAuto);
        setAutoWidth(isAuto);
    }
    
    private void updateLayoutSizeConstraint(){
        if (autoWidth || autoHeight){
            dialogLayout().setSizeConstraint(QLayout.SizeConstraint.SetMinAndMaxSize);
        }else{
            dialogLayout().setSizeConstraint(QLayout.SizeConstraint.SetDefaultConstraint);
        }
    }

    public boolean isAutoSize() {
        return isAutoHeight() && isAutoWidth();
    }
        
    protected final void removeButtonBox(){
        if (buttonBox!=null){
            buttonBox.clearButtons();
            dialogLayout().removeWidget(buttonBox);
            buttonBox = null;
        }
    }
    
    protected final void setCenterButtons(final boolean center){
        if (buttonBox!=null){
            buttonBox.setCenterButtons(center);
        }
    }
    
    protected QSize layoutMaximumSize(final QSize size){
        if (autoHeight){
            size.setHeight(layout().sizeHint().height());
        }
        if (autoWidth){
            size.setWidth(layout().sizeHint().width());
        }
        return size;
    }
    
    protected QSize layoutMinimumSize(final QSize size){
        if (autoHeight){
            size.setHeight(layout().sizeHint().height());
        }
        if (autoWidth){
            size.setWidth(layout().sizeHint().width());
        }
        return size;    
    }    
}
