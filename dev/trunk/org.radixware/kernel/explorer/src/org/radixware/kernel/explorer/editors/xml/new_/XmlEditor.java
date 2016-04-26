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

package org.radixware.kernel.explorer.editors.xml.new_;

import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QCloseEvent;
import java.util.EnumSet;
import java.util.List;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlEditor;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlSchemaProvider;
import org.radixware.kernel.common.client.editors.xmleditor.XmlEditorOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlEditorController;
import org.radixware.kernel.explorer.editors.xml.new_.view.XmlValueEditorFactory;
import org.radixware.kernel.explorer.views.MainWindow;


public class XmlEditor extends MainWindow implements IXmlEditor {

    private final XmlEditorController controller;

    public XmlEditor(final IClientEnvironment environment, final String scheme) throws InstantiationException, IllegalAccessException {
        this(environment, scheme, null, null, null, false);
    }

    public XmlEditor(final IClientEnvironment environment,
            final String scheme,
            final IXmlSchemaProvider schemaProvider,
            final QWidget parent,
            final XmlValueEditorFactory<? extends QWidget> valueEditorFactory,
            final boolean isReadOnly) throws InstantiationException, IllegalAccessException {
        super(parent);
        final QListWidget errorsList = new QListWidget();
        final XmlEditorPresenter presenter = new XmlEditorPresenter(environment, valueEditorFactory, this, errorsList);
        controller = new XmlEditorController(environment, presenter, null, scheme, isReadOnly, schemaProvider);
        addToolBar(presenter.getToolBar());
        errorsList.setVisible(false);
    }

    @Override
    public void setReadOnly(final boolean isReadOnly) {
        controller.setReadOnly(isReadOnly);
    }

    @Override
    public boolean isReadOnly() {
        return controller.isReadOnly();
    }

    public XmlEditorController getController() {
        return controller;
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        controller.close();
        super.closeEvent(event);
    }

    @Override
    public void setValue(final XmlObject value) {
        controller.setValue(value);
    }

    @Override
    public XmlObject getValue() {
        return controller.getDocument() == null ? null : controller.getDocument().asXmlObject();
    }

    @Override
    public void setDisabledOperations(final EnumSet<XmlEditorOperation> operations) {
        controller.setDisabledOperations(operations);
    }

    @Override
    public boolean isOperationDisabled(final XmlEditorOperation operation) {
        return controller.isOperationDisabled(operation);
    }

    @Override
    public boolean valueWasModified() {
        return controller.wasModified();
    }

    @Override
    public List<XmlError> validate(final boolean isVisible) {
        return controller.validate(isVisible);
    }
}
