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

package org.radixware.kernel.explorer.editors.xml.new_.view;

import org.radixware.kernel.explorer.editors.xml.new_.view.editors.XmlValueEdit;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlValueEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;


public abstract class XmlValueEditorFactory<T extends QWidget> {

    private final static DefaultXmlValueEditorFactory DEFAULT_INSTANCE = new DefaultXmlValueEditorFactory();

    private static class DefaultXmlValueEditorFactory extends XmlValueEditorFactory<ValEditor> {

        @Override
        public XmlValueEdit createEditor(final IClientEnvironment environment, final IXmlValueEditingOptionsProvider editingOptionsProvider, final XmlModelItem modelItem, final boolean isReadOnly) {
            return new XmlValueEdit(environment, modelItem,editingOptionsProvider, isReadOnly);
        }
    }

    public abstract IXmlValueEditor<T> createEditor(final IClientEnvironment environment, final IXmlValueEditingOptionsProvider editingOptionsProvider, final XmlModelItem modelItem, final boolean isReadOnly);

    public static XmlValueEditorFactory<ValEditor> getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }
}