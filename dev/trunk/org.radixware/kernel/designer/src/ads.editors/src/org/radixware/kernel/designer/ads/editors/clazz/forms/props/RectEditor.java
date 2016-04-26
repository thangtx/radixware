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

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;


public class RectEditor extends PropertyEditorSupport {

    public RectEditor(UIPropertySupport property) {
        super(property);
    }

    @Override
    public String getAsText() {
        AdsUIProperty.RectProperty prop = (AdsUIProperty.RectProperty)getValue();
        return String.format("[%d,%d,%d,%d]", Integer.valueOf(prop.x), Integer.valueOf(prop.y), Integer.valueOf(prop.width), Integer.valueOf(prop.height));
    }

    @Override
    public void setAsText(String s) {
        AdsUIProperty.RectProperty prop = (AdsUIProperty.RectProperty)getValue();
        Pattern p = Pattern.compile("^\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\]$");
        Matcher m = p.matcher(s);
        if (m.find()) {
            prop.x = Integer.valueOf(m.group(1));
            prop.y = Integer.valueOf(m.group(2));
            prop.width = Integer.valueOf(m.group(3));
            prop.height = Integer.valueOf(m.group(4));
        } else
            throw new IllegalArgumentException ("Could not parse property, should be [x,y,width,height]");
        setValue(prop);
        try {
            ((UIPropertySupport)getSource()).setValue(prop);
        } catch (Throwable ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
