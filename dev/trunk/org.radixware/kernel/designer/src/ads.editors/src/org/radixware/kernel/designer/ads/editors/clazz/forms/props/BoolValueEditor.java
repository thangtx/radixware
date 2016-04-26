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

/*
 * 11/11/11 12:37 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;


public class BoolValueEditor extends PropertyEditorSupport {

    private static final String NULL = "null";
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    public BoolValueEditor() {
        super();
    }

    public BoolValueEditor(UIPropertySupport property) {
        super(property);
    }

    @Override
    public String getJavaInitializationString() {
        return "\"" + NULL + "\"";
    }

    @Override
    public String[] getTags() {
        return new String[]{NULL, TRUE, FALSE};
    }

    @Override
    public String getAsText() {
        return boolToStr(getValue());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(strToBool(text));
    }

    @Override
    public Boolean getValue() {
        try {
            return (Boolean) getSource().getValue();
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }  catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }

    @Override
    public void setValue(Object value) {
        try {
            getSource().setValue(value);
        } catch (IllegalAccessException  ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public UIPropertySupport getSource() {
        return (UIPropertySupport) super.getSource();
    }

    public AdsUIProperty.BooleanValueProperty getProperty() {
        return (AdsUIProperty.BooleanValueProperty) getSource().getProp();
    }

    private String boolToStr(Boolean val) {
        return val != null ? (val ? TRUE : FALSE) : NULL;
    }

    private Boolean strToBool(String val) {
        return NULL.equals(val) ? null : (FALSE.equals(val) ? Boolean.FALSE : Boolean.TRUE);
    }
}
