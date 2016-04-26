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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.FeatureDescriptor;
import java.beans.PropertyEditorSupport;
import javax.swing.UIManager;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;


public class EnumRefEditor extends PropertyEditorSupport implements ExPropertyEditor {

    private PropertyEnv env;

    public EnumRefEditor(UIPropertySupport property) {
        super(property);
    }

    private RadixObject getNode() {
        return ((UIPropertySupport)getSource()).getNode();
    }

    @Override
    public String getAsText() {
        AdsUIProperty.EnumRefProperty prop = (AdsUIProperty.EnumRefProperty)getValue();
        AdsEnumDef enumDef = prop.findEnum();
        return String.format("%s", enumDef != null ? enumDef.getName() : "");
    }

    @Override
    public void setAsText(String s) {
    }

    @Override
    public boolean supportsCustomEditor () {
        return !getNode().isReadOnly();
    }

    @Override
    public java.awt.Component getCustomEditor () {
        return new EnumRefPanel(this, env);
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        this.env = env;
        FeatureDescriptor desc = env.getFeatureDescriptor();
        desc.setValue("canEditAsText", Boolean.FALSE);
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics g, Rectangle box) {
        UIPropertySupport sup = (UIPropertySupport)this.getSource();
        boolean readOnly = sup.getNode().isReadOnly();

        Color oldColor = g.getColor();
        if (readOnly)
            g.setColor(UIManager.getColor("textInactiveText"));

        String s = getAsText();
        int offset = g.getFontMetrics().getMaxAscent();
        g.drawString(s, box.x, box.y + offset);

        g.setColor(oldColor);
    }
}
