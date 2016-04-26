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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.props;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import org.openide.nodes.PropertySupport;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject.Prop;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Bin;


public class AppPropertySupport extends PropertySupport implements PropertyChangeListener {

    private final Prop prop;

    @SuppressWarnings("unchecked")
    public AppPropertySupport(Prop prop) {
        super(prop.getName(), getClass(prop), prop.getName(), prop.getName(), true, false/*!prop.isReadOnly()*/);
        this.prop = prop;
    }

    public Prop getProp() {
        return prop;
    }

    private static Class<?> getClass(Prop prop) {
        EValType type = prop.getType().getTypeId();
        if (type == null)
            return AppPropertySupport.class;
        
        switch (type) {
            case BOOL:
                return Boolean.TYPE;
            case CHAR:
                return Character.class;
            case DATE_TIME:
                return Timestamp.class;
            case BIN:
                return Bin.class;
            case INT:
                return Long.TYPE;
            case NUM:
                return Double.TYPE;
            case STR:
                return String.class;
        }

        return String.class;
        //return AppPropertySupport.class; // to lock editor
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        EValType type = prop.getType().getTypeId();
        ValAsStr val = prop.getValue();
        if (type == null)
            return String.valueOf(val);

        switch (type) {
            case BOOL:
            case CHAR:
            case DATE_TIME:
            case BIN:
            case INT:
            case NUM:
            case STR:
                if (val != null)
                    return val.toObject(type);
        }
        return String.valueOf(val);
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        EValType type = prop.getType().getTypeId();
        if (type == null) {
            prop.setValue((String)null);
            return;
        }

        switch (type) {
            case BOOL:
            case CHAR:
            case DATE_TIME:
            case BIN:
            case INT:
            case NUM:
            case STR:
                if (val != null) {
                    prop.setValue(ValAsStr.Factory.newInstance(val, type));
                    return;
                }
        }
        
        prop.setValue((String)null);
    }

    @Override
    public PropertyEditor getPropertyEditor() {        
        return super.getPropertyEditor();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

}
