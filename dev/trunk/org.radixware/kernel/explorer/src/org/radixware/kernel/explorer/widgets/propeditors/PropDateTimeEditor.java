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

import java.sql.Timestamp;
import org.radixware.kernel.common.client.editors.property.PropertyProxy;
import org.radixware.kernel.common.client.enums.EWidgetMarker;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;

public class PropDateTimeEditor extends PropEditor {    

    public PropDateTimeEditor(final Property property) {
        this(property,getValEditorFactory());
    }
    
    protected PropDateTimeEditor(final Property property, final ValEditorFactory factory){
        super(property, factory);
        setup(property);
    }

    @Override
    void setProperty(final Property property) {
        super.setProperty(property);
        if (property!=null){
            setup(property);
        }
    }
        
    private void setup(final Property property){
        setPropertyProxy(new PropertyProxy(property){
                                @Override
                                public Object getPropertyValue() {
                                    final Object timestamp = super.getPropertyValue();
                                    if (timestamp == null) {
                                        return timestamp;
                                    }
                                    final EditMaskDateTime mask = (EditMaskDateTime) getProperty().getEditMask();
                                    return mask.copyFields((Timestamp) timestamp, getEnvironment());
                                }            
                            }
                );
    }
    
    public static ValEditorFactory getValEditorFactory(){
        return ValEditorFactory.getDefault();
    }
    
    @Override
    public final EWidgetMarker getWidgetMarker() {
        return EWidgetMarker.DATE_TIME_PROP_EDITOR;
    }      
}
