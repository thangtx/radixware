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

package org.radixware.wps.views.editor.property;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IFileDialogSettings;
import org.radixware.kernel.common.client.editors.property.PropEditorController;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.editors.property.PropertyProxy;
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
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.UIObject;


public abstract class AbstractPropEditor extends UIObject implements IPropEditor {

    protected final PropEditorController<AbstractPropEditor> controller;
    protected UIObject editorWidget;
    private final IClientEnvironment env;

    private class PropertyStorePossiblity implements IPropertyStorePossibility {

        private final SimpleProperty property;
        private File tmpDir;

        public PropertyStorePossiblity(SimpleProperty property) {
            this.property = property;
        }

        @Override
        public boolean canPropertySaveValue() {
            return (property != null
                    && (property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_SAVE
                    || property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_SAVE_AND_LOAD));
        }

        @Override
        public boolean canPropertyReadValue() {
            return (property != null
                    && (property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_LOAD
                    || property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_SAVE_AND_LOAD));
        }

        @Override
        @SuppressWarnings("unchecked")
        public void writePropertyValue(final Object value) {//save

            if (property == null) {
                return;
            }
            FileOutputStream output = null;
            File f = null;
            try {
                File dir = createTempDir();
                final Property prop = getProperty();
                String fileName = prop.getTitle().replaceAll("[/</>*?|'\":]", "");//exclude windows file name prohibited chars
                if (dir != null && dir.isDirectory() && dir.exists()) {

                    f = new File(dir, fileName);
                    f.createNewFile();

                    output = new FileOutputStream(f);
                    SimpleProperty pr = (SimpleProperty) prop;
                    pr.saveToStream(output, value);//save file to starter temporary directory
                    if (f.exists() && f.isFile()) {
                        EMimeType type = pr.getFileDialogSettings(IFileDialogSettings.EFileDialogOpenMode.LOAD).getMimeType();
                        String mimeType = type != null ? "." + type.getExt() : null;
                        ((WpsEnvironment) getEnvironment()).sendFileToTerminal(f.getName(), f, mimeType, false, false);//send file to terminal
                    } else {
                        throw new FileNotFoundException("Could not create file for property value storing.");
                    }
                } else {
                    throw new FileNotFoundException("Could not create temporary directory for property value storing.");
                }
            } catch (FileNotFoundException ex) {
                String mess = String.format("File not found \n%s", f);
                getEnvironment().processException(mess, ex);
            } catch (IOException ex) {
                String mess = String.format("Failed to save value of property %s to a file\n%s", getProperty(), f);
                getEnvironment().processException(mess, ex);
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException ex) {
                        getEnvironment().getTracer().error(ex);
                    }
                }
            }
        }

        @Override
        public Object readPropertyValue(InputStream stream) {//load
            /* stream здесь всегда null.
             * Поэтому все операции с чтением/записью значения свойства в файл 
             * будут производиться с помощью LoadFileAction */

            /* try {
             Object value = property.loadFromStream(stream);
             return value;
             } catch (IOException ex) {
             getEnvironment().processException(ex);
             } finally {
             if (stream != null) {
             try {
             stream.close();
             } catch (IOException ex) {
             getEnvironment().getTracer().error(ex);
             }
             }
             }*/
            return null;
        }

        private File createTempDir() {
            tmpDir = RadixLoader.getInstance().createTempFile("PropertyValueStore");
            boolean isDir = tmpDir.mkdir();
            if (isDir) {
                return tmpDir;
            }
            return null;
        }
    }

    public AbstractPropEditor(Property property) {
        super(new Div());
        this.env = property.getEnvironment();
        this.controller = new Controller(this, property);
        setPropertyStorePossibilityImpl(property);
    }

    private void setPropertyStorePossibilityImpl(Property property) {
        if (property instanceof SimpleProperty) {
            controller.setPropertyStorePossibility(new PropertyStorePossiblity((SimpleProperty) property));
        }
    }

    @Override
    public Property getProperty() {
        return controller.getProperty();
    }

    protected void setEditorWidget(UIObject widget) {
        this.editorWidget = widget;
        this.html.add(widget.getHtml());
        widget.setParent(this);
        widget.setHCoverage(100);
        widget.setVCoverage(100);
    }

    @Override
    public void finishEdit() {
        controller.finishEdit();
    }

    protected void processException(final Throwable ex, final String title, final String msg) {
        controller.processException(ex, title, msg);
    }
    
    public final void setPropertyProxy(final PropertyProxy proxy){
        controller.setPropertyProxy(proxy);
        if (getProperty()!=null){
            refresh(getProperty());
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject res = super.findObjectByHtmlId(id);
        if (res != null) {
            return res;
        } else {
            if (editorWidget != null) {
                return editorWidget.findObjectByHtmlId(id);
            } else {
                return null;
            }
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        if (editorWidget != null) {
            editorWidget.visit(visitor);
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
        final List<ICommandToolButton> commandButtons = controller.getCommandToolButtons();
        for (ICommandToolButton commandButton : commandButtons) {
            addButton(commandButton);
        }

    }

    @Override
    public void setStyleSheet(String styleSheet) {
    }

    @Override
    public boolean close() {
        return controller.close();
    }

    @Override
    public void refresh(ModelItem aThis) {
        controller.refresh(aThis);
    }

    @Override
    public boolean setFocus(Property aThis) {
        if (aThis == controller.getProperty()) {
            setFocused(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void bind() {
        controller.bind();
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return env;
    }

    protected abstract void closeEditor();

    protected abstract void updateSettings();

    protected abstract void updateEditor(Object currentValue, Object initialValue, PropEditorOptions options);

    protected abstract Object getCurrentValueInEditor();

    public abstract void setLabelVisible(boolean visible);

    public abstract boolean getLabelVisible();
    
    protected UnacceptableInput getUnacceptableInput(){
        return null;
    }
}
