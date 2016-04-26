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

import org.radixware.wps.views.editor.xml.editors.XmlValueEditor;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlValueEditor;
import org.radixware.wps.rwt.UIObject;

    

public abstract class XmlValueEditorFactory<T extends UIObject>{
    
    private static final DefaultXmlValueEditorFactory DEFAULT_INSTANCE = new DefaultXmlValueEditorFactory();
    
    private static class DefaultXmlValueEditorFactory extends XmlValueEditorFactory<UIObject>{
        @Override
        public XmlValueEditor createEditor(final IClientEnvironment environment, final IXmlValueEditingOptionsProvider editingOptionsProvider, final XmlModelItem modelItem) {
            return new XmlValueEditor(environment, modelItem, editingOptionsProvider);
        }
    }
    
    public static XmlValueEditorFactory<UIObject> getDefaultInstance(){
        return DEFAULT_INSTANCE;
    }
    
    public abstract IXmlValueEditor<T> createEditor(final IClientEnvironment environment, final IXmlValueEditingOptionsProvider editingOptionsProvider, final XmlModelItem modelItem);
}