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

import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyXml;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.editors.valeditors.ValXmlEditor;

public final class PropXmlEditor extends PropEditor {
    
    public PropXmlEditor(final PropertyXml property) {
        super(property, ValEditorFactory.getDefault());        
        setup((PropertyXml) getProperty());
    }

    @Override
    void setProperty(final Property property) {
        super.setProperty(property);
        if (property!=null){
            setup((PropertyXml) getProperty());
        }
    }

    @Override
    void clear() {
        getValEditor().valueChanged.disconnect(this);
        super.clear();
    }
    
    
    
    private void setup(final PropertyXml prop){
        final ValXmlEditor editor = (ValXmlEditor)getValEditor();
        editor.setValClass(prop.getValClass());
        editor.setSchemaId(prop.getSchemaId());
        editor.valueChanged.connect(this, "finishEdit()");        
    }

    @Override
    public void refresh(final ModelItem changedItem) {
        super.refresh(changedItem);
        final ValXmlEditor editor = (ValXmlEditor)getValEditor();
        final PropertyXml prop = (PropertyXml) getProperty();
        editor.setEditButtonVisible(!prop.isCustomEditOnly());
        editor.setEditorDialogTitle(ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), prop.getTitle()));
    }    
}
