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

package org.radixware.kernel.explorer.widgets.propeditors;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.dialogs.IFileDialogSettings;
import org.radixware.kernel.common.client.editors.property.PropEditorController;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.editors.property.PropertyProxy;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.SimpleProperty;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.views.IPropertyStorePossibility;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.kernel.explorer.widgets.IExplorerModelWidget;

/**
 * AbstractPropEditor - абстрактный редактор свойства Содержит сигналы, кнопку изменения признака собственного
 * значения, кнопку вызова кастомного диалога редактора, и кнопки команд свойства, Реализованы базовый функции
 * установки значения свойства, обновление состояния кнопок и их обработчики. О внешнем виде самого редактора
 * здесь ничего неизвестно
 *
 *
 */
public abstract class AbstractPropEditor extends ExplorerWidget implements IPropEditor, IExplorerModelWidget {

    private static class FocusOutEvent extends QEvent {        
        public FocusOutEvent() {
            super(QEvent.Type.User);
        }
    }
    
    private static class MouseButtonHandler extends QEventFilter{
        
        private boolean eventScheduled;
        
        public MouseButtonHandler(final QObject parent){
            super(parent);
            setProcessableEventTypes(EnumSet.of(QEvent.Type.MouseButtonRelease));
        }
        
        public static boolean isLeftMouseButtonPressed(){
            return QApplication.mouseButtons().isSet(com.trolltech.qt.core.Qt.MouseButton.LeftButton);
        }

        @Override
        public boolean eventFilter(final QObject target, final QEvent event) {
            if (!eventScheduled
                && event!=null 
                && event.type() == QEvent.Type.MouseButtonRelease 
                && !isLeftMouseButtonPressed()){
                eventScheduled = true;
                Application.processEventWhenEasSessionReady(parent(), new FocusOutEvent());
            }
            return false;
        }
    }
    
    final public Signal1<Object> edited = new Signal1<>();
    final public Signal0 focused = new Signal0();
    final public Signal0 unfocused = new Signal0();
    private boolean focusOutEventScheduled;
    private MouseButtonHandler mouseButtonHandler;
    protected final PropEditorController controller;
    protected final QEventFilter focusListener = new QEventFilter(this){
        @Override
        public boolean eventFilter(final QObject qo, final QEvent qevent) {
            return AbstractPropEditor.this.eventFilter(qo, qevent);
        }        
    };

    private static class PropertyStorePossiblity implements IPropertyStorePossibility {

        final SimpleProperty property;

        public PropertyStorePossiblity(SimpleProperty prop) {
            this.property = prop;
        }
        
        private IClientEnvironment getEnvironment(){
            return property.getEnvironment();
        }

        @Override
        public boolean canPropertySaveValue() {
            return property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_SAVE
                   || property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_SAVE_AND_LOAD;
        }

        @Override
        public boolean canPropertyReadValue() {
            return property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_LOAD
                   || property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_SAVE_AND_LOAD;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void writePropertyValue(final Object value) {
            IFileDialogSettings settings = property.getFileDialogSettings(IFileDialogSettings.EFileDialogOpenMode.SAVE);            
            final EMimeType type = settings.getMimeType();
            final String fileFilter;
            if (type!=null){
                final StringBuilder mimeTypeBuilder = new StringBuilder();
                mimeTypeBuilder.append(type.getName().toUpperCase());
                mimeTypeBuilder.append("  (*.").append(type.getExt()).append(");; ");
                mimeTypeBuilder.append(getEnvironment().getMessageProvider().translate("PropertyEditor", "All Files"));
                mimeTypeBuilder.append("(*.*)");
                fileFilter = mimeTypeBuilder.toString();
            }else{
                fileFilter = "";
            }
            final QFileDialog dialog = 
                new QFileDialog(null, settings.getFileDialogTitle(), settings.getInitialPath(), fileFilter);
            dialog.setFileMode(QFileDialog.FileMode.AnyFile);
            getEnvironment().getProgressHandleManager().blockProgress();
            dialog.setAcceptMode(QFileDialog.AcceptMode.AcceptSave);            
            String fileName = "";
            try {
                if (dialog.exec() != QDialog.DialogCode.Accepted.value()) {
                    return;
                }
                fileName = dialog.selectedFiles().get(0);
                if (fileName == null || fileName.isEmpty()) {
                    return;
                }
                final File f = new File(fileName);
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    getEnvironment().processException(ex);
                }
                if (f.exists() && f.isFile()) {
                    final FileOutputStream output = new FileOutputStream(f);
                    try{
                        property.saveToStream(output, value);
                    }finally{
                        try{
                            output.close();
                        }catch(IOException ex){
                            getEnvironment().getTracer().error(ex);
                        }
                    }
                }

            } catch (FileNotFoundException ex) {
                String mess = String.format("Failed to save value of property %s to a file\n%s", property, fileName);
                getEnvironment().processException(mess, ex);
            } catch (IOException ex) {
                getEnvironment().processException(ex);
            } finally {
                getEnvironment().getProgressHandleManager().unblockProgress();
            }
        }

        @Override
        public Object readPropertyValue(InputStream stream) {
            IFileDialogSettings settings = property.getFileDialogSettings(IFileDialogSettings.EFileDialogOpenMode.LOAD);
            EMimeType type = settings.getMimeType();
            final Object currentValue = property.getValueObject();
            final String fileFilter;
            if (type != null) {
                final StringBuilder mimeTypeBuilder = new StringBuilder();
                mimeTypeBuilder.append(type.getName().toLowerCase());
                mimeTypeBuilder.append("  (*.").append(type.getExt()).append(')');
                fileFilter = mimeTypeBuilder.toString();
            }else{
                fileFilter = "";
            }
            final QFileDialog dialog = 
                new QFileDialog(null, settings.getFileDialogTitle(), settings.getInitialPath(), fileFilter);
            dialog.setFileMode(QFileDialog.FileMode.ExistingFile);
            dialog.setAcceptMode(QFileDialog.AcceptMode.AcceptOpen);
            getEnvironment().getProgressHandleManager().blockProgress();
            String fileName = "";
            try {                   
                if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
                    fileName = dialog.selectedFiles().get(0);
                } else {                    
                    return currentValue;//if none file was selected ===> return old property value
                }

                if (fileName == null || fileName.isEmpty()) {
                    throw new FileNotFoundException("Invalid file name " + fileName);
                }                
                final Path fp = Paths.get(fileName);
                if (fp == null || !fp.toFile().exists()) {
                    throw new FileNotFoundException();
                }
                final File f = new File(fileName);
                if (f.canRead()) {
                    final FileInputStream input = new FileInputStream(f);
                    try{
                        return property.loadFromStream(input);
                    }finally{
                        try{
                            input.close();
                        }catch (IOException ex) {                     
                            getEnvironment().getTracer().error(ex);
                        }                        
                    }                    
                }
                
            } catch (FileNotFoundException ex) {
                String mess = String.format("Failed to load value of property %s from file\n%s", property, fileName);
                        getEnvironment().processException(mess, ex);
            } catch (IOException ex) {
                getEnvironment().processException(ex);
                        
            } finally {
                getEnvironment().getProgressHandleManager().unblockProgress();
            }
            return currentValue;
        }
    }

    public AbstractPropEditor(final IClientEnvironment environment) {
        super(environment);
        focusListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.FocusIn, QEvent.Type.FocusOut));
        this.controller = new Controller(this);
    }

    public AbstractPropEditor(final Property property) {
        this(property.getEnvironment());
        setPropertyImpl(property);        
    }
    
    void setProperty(final Property property) {
        setPropertyImpl(property);
    }

    private void setPropertyImpl(final Property property) {
        controller.setProperty(property);
        if (property != null) {
            controller.addFocusListener(new PropEditorController.FocusListener() {
                @Override
                public void focused() {
                    focused.emit();
                }

                @Override
                public void unfocused() {
                    unfocused.emit();
                }
            });
            controller.addEditActionListener(new PropEditorController.EditActionListener() {
                @Override
                public void edited(Object obj) {
                    edited.emit(obj);
                }

                @Override
                public void disconnected() {
                    edited.disconnect();
                }
            });
            if (property instanceof SimpleProperty) {
                controller.setPropertyStorePossibility(new PropertyStorePossiblity((SimpleProperty) property));
            }
        }else{
            controller.setPropertyStorePossibility(null);
        }
    }

    protected final void addStandardButtons() {
        final IButton ownValBtn = controller.getOwnValButton();
        if (ownValBtn != null) {
            addButton(ownValBtn);
        }
        final IButton customDlgBtn = controller.getCustomDialogButton();
        if (customDlgBtn != null) {
            addButton(customDlgBtn);
        }
        if (getProperty() instanceof SimpleProperty) {
            final IButton loadFromFileBtn = controller.getLoadFromFileButton();
            if (loadFromFileBtn != null) {
                addButton(loadFromFileBtn);
            }
            final IButton saveToFileButton = controller.getSaveToFileButton();
            if (saveToFileButton != null) {
                addButton(saveToFileButton);
            }
        }
        addCommandButtons();
    }

    @SuppressWarnings("unchecked")//invalid java warning on controller.getCommandToolButtons() call
    protected final void addCommandButtons() {
        final List<ICommandToolButton> commandButtons = controller.getCommandToolButtons();
        for (ICommandToolButton commandButton : commandButtons) {
            addButton(commandButton);
        }
    }

    public final void setPropertyProxy(final PropertyProxy proxy) {        
        controller.setPropertyProxy(proxy);
        if (getProperty()!=null){
            refresh(getProperty());
        }
    }

    protected final Object getPropertyValue() {
        return controller.getPropertyValue();
    }

    protected final Object getPropertyInitialValue() {
        return controller.getPropertyInitialValue();
    }

    //Наследники могут подменять editMask
    protected final EditMask getPropertyEditMask() {
        return controller.getPropertyEditMask();
    }

    protected final boolean isReadonly() {
        return controller.isReadonly();
    }

    protected void processException(final Throwable ex, final String title, final String msg) {
        controller.processException(ex, title, msg);
    }

    @Override
    public final Property getProperty() {
        return controller.getProperty();
    }

    public Qt.AlignmentFlag getTitleAlignment() {
        return null;
    }

    public void setHighlightedFrame(final boolean isHighlighted) {
    }

    public boolean isHighlightedFrame() {
        return false;
    }

    @Override
    public void bind() {
        controller.bind();
        if (parentWidget()==null){
            final Property property = getProperty();
            if (RunParams.isDevelopmentMode()){
                final String warningMessage = 
                    property.getEnvironment().getMessageProvider().translate("TraceMessage", "Call of bind method for property editor with no parent widgget detected.\nIt is necessary to set parent widget for editor of property '%1$s' (#%2$s) before call this method");                
                property.getEnvironment().getTracer().warning(String.format(warningMessage, property.getTitle(), property.getId().toString()));
            }
            if (isHidden() && property.isVisible()){
                setVisible(true);
            }
        }        
    }

    @Override
    public void refresh(ModelItem changedItem) {
        focusOutEventScheduled = false;
        controller.refresh(changedItem);
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    /**
     * Передать значение из редактора в свойства. Вызывается в случае потери фокуса редактора или его
     * уничтожении.
     *
     */
    @Override
    public final void finishEdit() {
        controller.finishEdit();
    }

    @Override
    public boolean eventFilter(final QObject source, final QEvent event) {
        if (event!=null && event.type() == QEvent.Type.FocusOut) {
            if (((QFocusEvent) event).reason() != Qt.FocusReason.PopupFocusReason) {
                focusOutEventScheduled = true;
                if (MouseButtonHandler.isLeftMouseButtonPressed()){
                    installMouseButtonHanlder();
                }else{
                    Application.processEventWhenEasSessionReady(this, new FocusOutEvent());//RADIX-7675
                }
            }
        } else if (event!=null && event.type() == QEvent.Type.FocusIn) {
            focusOutEventScheduled = false;
            removeMouseButtonHandler();
            controller.focusEvent(true, ((QFocusEvent) event).reason() == Qt.FocusReason.PopupFocusReason);
        } 
        return false;
    }
    
    private void installMouseButtonHanlder(){
        if (mouseButtonHandler==null){
            mouseButtonHandler = new MouseButtonHandler(this);
            QApplication.instance().installEventFilter(mouseButtonHandler);
        }
    }
    
    private void removeMouseButtonHandler(){
        if (mouseButtonHandler!=null){
            QApplication.instance().removeEventFilter(mouseButtonHandler);
            mouseButtonHandler = null;
        }
    }

    @Override
    protected void customEvent(QEvent event) {
        if (event instanceof FocusOutEvent) {
            event.accept();
            if (focusOutEventScheduled) {
                if (this.nativeId() == 0) {
                    focusOutEventScheduled = false;                    
                } else {                    
                    if (MouseButtonHandler.isLeftMouseButtonPressed()){//RADIX-7675
                        installMouseButtonHanlder();
                    } else {
                        focusOutEventScheduled = false;
                        removeMouseButtonHandler();
                        try {
                            controller.focusEvent(false, false);
                        } catch (RuntimeException exception) {
                            getEnvironment().getTracer().error(exception);
                        }
                    }
                }
            }
        } else {
            super.customEvent(event);
        }
    }

    @Override
    protected void focusOutEvent(final QFocusEvent event) {
        super.focusOutEvent(event);
        controller.focusOutEvent(event.reason() == Qt.FocusReason.PopupFocusReason);
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        if (!controller.close()) {
            event.ignore();
        } else {
            QApplication.removePostedEvents(this, QEvent.Type.User.value());
            removeMouseButtonHandler();
            edited.disconnect();
            focused.disconnect();
            unfocused.disconnect();
            super.closeEvent(event);
        }
    }

    void clear() {
        controller.clear();
        QApplication.removePostedEvents(this, QEvent.Type.User.value());
        removeMouseButtonHandler();
        edited.disconnect();
        focused.disconnect();
        unfocused.disconnect();
        setProperty(null);
        setVisible(false);
        setParent(null);
    }

    protected abstract void closeEditor();

    protected abstract void updateSettings();

    protected abstract void updateEditor(Object value, PropEditorOptions options);

    protected abstract Object getCurrentValueInEditor();
    
    protected UnacceptableInput getUnacceptableInput(){
        return null;
    }
}
