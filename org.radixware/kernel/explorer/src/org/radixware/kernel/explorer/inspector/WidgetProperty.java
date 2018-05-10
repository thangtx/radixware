/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.inspector;

import java.lang.reflect.Method;
import org.radixware.kernel.explorer.inspector.delegates.InspectorDelegate;

/**
 *
 * @author szotov
 */
public class WidgetProperty {

    private final String name;
    private final Object value;
    private boolean isReadOnly;
    private final String className;
    private final InspectorDelegate delegate;
    private final Class type;
    private final Method setterMethod;

    public WidgetProperty(String name, Object value, String className, InspectorDelegate delegate, Class type, boolean isReadOnly, Method setterMethod) {
        this.name = name;
        this.value = value;
        this.className = className;
        this.isReadOnly = isReadOnly;
        this.delegate = delegate;
        this.type = type;
        this.setterMethod = setterMethod;   
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public String getClassName() {
        return className;
    }

    InspectorDelegate getDelegate() {
        return delegate;
    }
    
    public Class getType(){
        return type;
    }
    
    public Method getSetterMethod() {
        return setterMethod;
    }    
    
}
