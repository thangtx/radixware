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
package org.radixware.wps.views.editor.xml.view;

import java.util.EnumSet;
import java.util.List;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlEditor;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlSchemaProvider;
import org.radixware.kernel.common.client.editors.xmleditor.StandardXmlSchemaProvider;
import org.radixware.kernel.common.client.editors.xmleditor.XmlEditorOperation;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.rwt.uploading.FileUploader;
import org.radixware.wps.rwt.uploading.IUploadedDataReader;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocument;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlEditorController;
import org.radixware.wps.rwt.ListBox;
import org.radixware.wps.rwt.UIObject;

public class XmlEditor extends VerticalBoxContainer implements IXmlEditor {

    private final WpsEnvironment environment;
    private final XmlEditorController controller;    

    public XmlEditor(final WpsEnvironment env, final String xmlSchemaAsStr, final boolean isReadOnly) {
        this(env, xmlSchemaAsStr, null, null, isReadOnly);
    }

    public XmlEditor(final WpsEnvironment env, final SchemaTypeSystem schemaTypeSystem, final boolean isReadOnly) {
        this(env, schemaTypeSystem, null, null, isReadOnly);
    }

    public XmlEditor(final WpsEnvironment env,
            final SchemaTypeSystem schemaTypeSystem,
            final IXmlSchemaProvider schemaProvider,
            final XmlValueEditorFactory<? extends UIObject> valueEditorFactory,
            final boolean isReadOnly) {
        environment = env;
        final ListBox errorsList = new ListBox();
        final XmlEditorPresenter presenter;
        final IXmlSchemaProvider actualSchemaProvider;
        if (schemaProvider==null){
            actualSchemaProvider = new StandardXmlSchemaProvider(environment);
        }else{
            actualSchemaProvider = schemaProvider;
        }
        presenter = new XmlEditorPresenter(env, valueEditorFactory, actualSchemaProvider, this, errorsList);
        controller = 
            new XmlEditorController(env, presenter, null, schemaTypeSystem, isReadOnly, schemaTypeSystem == null ? null : actualSchemaProvider);
        presenter.openAction.getUploader().addUploadCompleteListener(new FileUploader.UploadCompleteListener() {
            @Override
            public void afterUploadingComplete(String fileName, long fileSize, IUploadedDataReader reader) {
                final XmlDocument document = presenter.getXmlDocument(reader);
                if (document != null) {
                    try {
                        controller.openXmlDocument(document);
                    } catch (XmlException exception) {
                        environment.processException(exception);
                    }
                }
            }
        });
    }

    public XmlEditor(final WpsEnvironment env,
            final String xmlSchemaAsStr,
            final IXmlSchemaProvider schemaProvider,
            final XmlValueEditorFactory<? extends UIObject> valueEditorFactory,
            final boolean isReadOnly) {
        environment = env;
        final ListBox errorsList = new ListBox();
        final IXmlSchemaProvider actualSchemaProvider;
        if (schemaProvider==null){
            actualSchemaProvider = new StandardXmlSchemaProvider(environment);
        }else{
            actualSchemaProvider = schemaProvider;
        }        
        final XmlEditorPresenter presenter = new XmlEditorPresenter(env, valueEditorFactory, actualSchemaProvider, this, errorsList);
        controller = 
            new XmlEditorController(env, presenter, null, xmlSchemaAsStr, isReadOnly, (xmlSchemaAsStr == null || xmlSchemaAsStr.isEmpty()) ? null : actualSchemaProvider);
        presenter.openAction.getUploader().addUploadCompleteListener(new FileUploader.UploadCompleteListener() {
            @Override
            public void afterUploadingComplete(String fileName, long fileSize, IUploadedDataReader reader) {
                final XmlDocument document = presenter.getXmlDocument(reader);
                if (document != null) {
                    try {
                        controller.openXmlDocument(document);
                    } catch (XmlException exception) {
                        environment.processException(exception);
                    }
                }
            }
        });
    }

    @Override
    public void setValue(final XmlObject value) {
        controller.setValue(value.copy());
    }

    @Override
    public XmlObject getValue() {
        return controller.getDocument() == null ? null : controller.getDocument().asXmlObject();
    }

    XmlEditorController getController() {
        return controller;
    }

    @Override
    public void setReadOnly(final boolean isReadOnly) {
        controller.setReadOnly(isReadOnly);
    }

    @Override
    public boolean isReadOnly() {
        return controller.isReadOnly();
    }

    @Override
    public void setDisabledOperations(final EnumSet<XmlEditorOperation> operations) {
        controller.setDisabledOperations(operations);
    }

    @Override
    public boolean isOperationDisabled(XmlEditorOperation operation) {
        return controller.isOperationDisabled(operation);
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/client.js", "org/radixware/wps/rwt/inputBox.js"};
    }

    @Override
    public boolean close() {
        return controller.close();
    }

    @Override
    public boolean valueWasModified() {
        return controller.wasModified();
    }

    @Override
    public List<XmlError> validate(boolean isVisible) {
        return controller.validate(isVisible);
    }
}
