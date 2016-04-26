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

package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import java.beans.FeatureDescriptor;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.enums.EAccess;


public class AccessEditor extends PropertyEditorSupport implements ExPropertyEditor {

    private PropertyEnv env;

    public AccessEditor(UIPropertySupport prop) {
        super(prop);
        try {
            setValue(prop.getValue());
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        this.env = env;
        FeatureDescriptor desc = env.getFeatureDescriptor();
        //desc.setValue("canEditAsText", Boolean.FALSE);
    }

    @Override
    public String getAsText() {
        EAccess access = (EAccess) getValue();
        if (access == null) {
            return "<undefined>";
        }
        return access.getName();
    }

    @Override
    public void setAsText(String value) {
        EAccess access;
        if (value.equals(EAccess.DEFAULT.getName())) {
            access = EAccess.DEFAULT;
        } else if (value.equals(EAccess.PRIVATE.getName())) {
            access = EAccess.PRIVATE;
        } else if (value.equals(EAccess.PROTECTED.getName())) {
            access = EAccess.PROTECTED;
        } else if (value.equals(EAccess.PUBLIC.getName())) {
            access = EAccess.PUBLIC;
        } else {
            return;
        }
    //    System.out.println("AAAAAAAASSSSSSSSSSSSSS");
        try {
            ((UIPropertySupport) getSource()).setValue(access);
       //     System.out.println("AAAAAAAASSSSSSSSSSSSSS2");
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
        setValue(access);
    }

    @Override
    public String[] getTags() {
        String[] result = new String[]{
            EAccess.PRIVATE.getName(),
            EAccess.DEFAULT.getName(),
            EAccess.PROTECTED.getName(),
            EAccess.PUBLIC.getName()
        };
        return result;
    }
}
