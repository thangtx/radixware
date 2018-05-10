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
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.FeatureDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import javax.swing.*;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPresentationSlotMethodDef;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public class ConnectionEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    private PropertyEnv env;
    private final InplaceEditor inplaceEditor = new EventInplaceEditor();
    private final AdsAbstractUIDef uiDef;

    public ConnectionEditor(UIConnectionSupport sup) {
        super(sup);
        this.uiDef = sup.getRootUIDef();
    }

    AdsAbstractUIDef getUiDef() {
        return uiDef;
    }

    AdsUIConnection getConnection() {
        return (AdsUIConnection) getValue();
    }

    private AdsUIItemDef getWidget() {
        return ((UIConnectionSupport) getSource()).getWidget();
    }

    private AdsMethodDef getSlot() {
        final AdsAbstractUIDef ui = getUiDef();
        final AdsUIItemDef widget = getWidget();
        final AdsUIConnection conn = getConnection();
        if (ui != null && conn != null && widget != null) {
            final AdsUIConnection c = ui.getConnections().get(widget, conn.getSignalName());
            if (c != null) {
                return c.getSlot();
            }
        }
        return null;
    }

    @Override
    public String getAsText() {
        final AdsAbstractUIDef ui = getUiDef();
        final AdsUIItemDef widget = getWidget();
        final AdsUIConnection conn = getConnection();
        if (ui != null && conn != null && widget != null) {
            final AdsUIConnection c = ui.getConnections().get(widget, conn.getSignalName());
            if (c != null) {
                final AdsPresentationSlotMethodDef m = c.getSlot();
                if (m != null) {
                    return m.getName();
                }
                return "<???>";
            }
        }
        return "<none>";
    }

    @Override
    public void setAsText(String s) {
    }

    @Override
    public boolean supportsCustomEditor() {
        final AdsAbstractUIDef ui = getUiDef();
        return ui != null && !ui.isReadOnly();
    }

    @Override
    public java.awt.Component getCustomEditor() {
        return new ConnectionPanel(this, getUiDef(), env);
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        this.env = env;
        FeatureDescriptor desc = env.getFeatureDescriptor();
//        desc.setValue("canEditAsText", Boolean.FALSE);
        env.registerInplaceEditorFactory(this);

    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics g, Rectangle box) {
        UIConnectionSupport sup = (UIConnectionSupport) getSource();
        boolean readOnly = sup.getWidget().isReadOnly();

        Color oldColor = g.getColor();
        if (readOnly) {
            g.setColor(UIManager.getColor("textInactiveText"));
        }

        String s = getAsText();
        int offset = g.getFontMetrics().getMaxAscent();
        g.drawString(s, box.x, box.y + offset);

        g.setColor(oldColor);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        return inplaceEditor;
    }

    private class EventInplaceEditor extends JTextField implements InplaceEditor {

        private PropertyModel model;
        private PropertyEditor editor;

        public EventInplaceEditor() {
            setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        final AdsMethodDef slot = getSlot();
                        if (slot != null) {
                            DialogUtils.goToObject(slot);
                        }
                    }
                }
            });
        }

        @Override
        public void connect(PropertyEditor pe, PropertyEnv env) {
            editor = pe;
            setText(pe.getAsText());
        }

        @Override
        public JComponent getComponent() {
            return this;
        }

        @Override
        public void clear() {
        }

        @Override
        public Object getValue() {
            return editor.getValue();
        }

        @Override
        public void setValue(Object o) {
            editor.setValue(o);
        }

        @Override
        public boolean supportsTextEntry() {
            return false;
        }

        @Override
        public void reset() {
            model = null;
        }

        @Override
        public void addActionListener(ActionListener al) {
        }

        @Override
        public void removeActionListener(ActionListener al) {
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            return null;
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            return editor;
        }

        @Override
        public PropertyModel getPropertyModel() {
            return model;
        }

        @Override
        public void setPropertyModel(PropertyModel pm) {
            this.model = pm;
        }

        @Override
        public boolean isKnownComponent(Component c) {
            return c == this;
        }   

        @Override
        public boolean isEditable() {
            return false;
        }
        
    }

}
