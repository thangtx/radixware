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

package org.radixware.kernel.common.client.editors.property;

import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.UnacceptableInput;


public class PropertyProxy {
    
    private final Property property;
    
    public PropertyProxy(final Property property){
        this.property = property;
    }
    
    public Property getProperty(){
        return property;
    }
    
    public Object getPropertyValue() {
        //Для некоторых свойств может потребоваться дополнительное
        //преобразование класса значения свойства в класс, потдерживаемый
        //редактором свойства (valEditor)
        return getProperty().getValueObject();
    }
    
    public Object getPropertyInitialValue() {
        return getProperty().getInitialValue();
    }
    
    public UnacceptableInput getPropertyUnacceptableInput(){
        return getProperty().getUnacceptableInput();
    }

    //Наследники могут подменять editMask
    public EditMask getPropertyEditMask() {
        return getProperty().getEditMask();
    }

    public boolean isPropertyReadonly() {
        return getProperty().isReadonly();
    }
    
}
