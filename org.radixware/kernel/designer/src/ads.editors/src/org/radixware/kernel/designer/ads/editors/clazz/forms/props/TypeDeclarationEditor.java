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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorSupport;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditPanel;


public class TypeDeclarationEditor extends PropertyEditorSupport implements ExPropertyEditor {

    private PropertyEnv env;

    public TypeDeclarationEditor(UIPropertySupport property) {
        super(property);
    }

    private RadixObject getNode() {
        return ((UIPropertySupport) getSource()).getNode();
    }

    @Override
    public String getAsText() {
        AdsUIProperty.TypeDeclarationProperty prop = (AdsUIProperty.TypeDeclarationProperty) getValue();
        AdsTypeDeclaration commandDef = prop.getType();

        AdsType type = commandDef.resolve(prop.getOwnerDefinition()).get();
        return String.format("%s", type != null ? type.getQualifiedName(prop) : commandDef.getQualifiedName());
    }

    @Override
    public void setAsText(String s) {
    }

    @Override
    public boolean supportsCustomEditor() {
        return !getNode().isReadOnly();
    }

    private class Panel extends TypeEditPanel implements PropertyChangeListener {

        Panel() {

            env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
            env.addPropertyChangeListener(this);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
                AdsUIProperty.TypeDeclarationProperty prop = (AdsUIProperty.TypeDeclarationProperty) getValue();

                AdsTypeDeclaration decl = getCurrentType();
                prop.setType(decl);
                setValue(prop);
                try {
                    ((UIPropertySupport) getSource()).setValue(prop);
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    @Override
    public java.awt.Component getCustomEditor() {
        final TypeEditPanel typeEditPanel = new Panel();

        typeEditPanel.open(((AdsUIProperty.TypeDeclarationProperty) getValue()).getType(), AdsUIUtil.getUiDef(getNode()), new IAdsTypedObject() {

            @Override
            public AdsTypeDeclaration getType() {
                return ((AdsUIProperty.TypeDeclarationProperty) getValue()).getType();
            }

            @Override
            public boolean isTypeAllowed(EValType type) {
                return true;
            }

            @Override
            public boolean isTypeRefineAllowed(EValType type) {
                switch (type) {
                    case INT:
                    case CHAR:
                    case STR:
                    case XML:
                    case USER_CLASS:
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public VisitorProvider getTypeSourceProvider(EValType toRefine) {
                return AdsVisitorProviders.newTypeProvider(toRefine, AdsUIUtil.getUiDef(getNode()).getUsageEnvironment());
            }
        });
        typeEditPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
            }
        });
        return typeEditPanel;
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
        UIPropertySupport sup = (UIPropertySupport) this.getSource();
        boolean readOnly = sup.getNode().isReadOnly();

        Color oldColor = g.getColor();
        if (readOnly) {
            g.setColor(UIManager.getColor("textInactiveText"));
        }

        String s = getAsText();
        int offset = g.getFontMetrics().getMaxAscent();
        g.drawString(s, box.x, box.y + offset);

        g.setColor(oldColor);
    }
}
