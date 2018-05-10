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
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.FeatureDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import static org.openide.explorer.propertysheet.InplaceEditor.COMMAND_SUCCESS;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class ImageEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    private PropertyEnv env;
    private final InplaceEditor inplaceEditor = new IconInplaceEditor();

    public ImageEditor(UIPropertySupport property) {
        super(property);
    }

    AdsModule getModule() {
        return getUiDef().getModule();
    }

    AdsAbstractUIDef getUiDef() {
        return AdsUIUtil.getUiDef(((UIPropertySupport) getSource()).getNode());
    }

    AdsUIProperty.ImageProperty getProperty() {
        return (AdsUIProperty.ImageProperty) getValue();
    }

    @Override
    public String getAsText() {
        AdsUIProperty.ImageProperty prop = (AdsUIProperty.ImageProperty) getValue();
        AdsImageDef imageDef = prop.findImage();
        return String.format("%s", imageDef != null ? imageDef.getName() : "");
    }

    @Override
    public void setAsText(String s) {
    }
    
    public JComponent getDialogPanel(){
        return new ImagePanel(this, env);
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        this.env = env;
        env.registerInplaceEditorFactory(this);
    }

    @Override
    public boolean isPaintable() {
        return false;
    }
    
    @Override
    public InplaceEditor getInplaceEditor() {
        return inplaceEditor;
    }

    private class IconInplaceEditor extends JPanel implements InplaceEditor {

        private PropertyModel model;
        private PropertyEditor editor;
        private JTextField textField;
        private JButton edit;
        private JButton clear;
        private MouseAdapter editListener;
        private MouseAdapter clearListener;
        private JComponent dialogPanel;
        private transient ArrayList<ActionListener> actionListenerList;
//        private boolean listenerAdded = false;

        public IconInplaceEditor() {
            editListener = new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (editor instanceof ImageEditor){
                        ImageEditor imageEditor = (ImageEditor)editor;
                        dialogPanel = imageEditor.getDialogPanel();
                        final ModalDisplayer modalDisplayer = new ModalDisplayer(dialogPanel);
                        if (env != null){
                            FeatureDescriptor desc = env.getFeatureDescriptor();
                            modalDisplayer.setTitle(desc.getDisplayName());
                            if (modalDisplayer.showModal()){
                                if ((env != null) && (env.getState() == PropertyEnv.STATE_NEEDS_VALIDATION)) {
                                    env.setState(PropertyEnv.STATE_VALID);
                                    SwingUtilities.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            IconInplaceEditor.this.fireAction();
                                        }
                                    });
                                }
                            }
                        }
                        dialogPanel = null;
                    }
                }
            };
            clearListener = new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (editor instanceof ImageEditor){
                        ImageEditor curEditor = (ImageEditor)editor;
                        AdsUIProperty.ImageProperty prop = getProperty();
                        prop.setImageId(null);
                        try {
                            ((UIPropertySupport) curEditor.getSource()).setValue(prop);
                        } catch (Exception ex) {
                            Exceptions.printStackTrace(ex);
                        }
                        textField.setText(editor.getAsText());
                        IconInplaceEditor.this.fireAction();
                    }
                }
            };
            textField = new JTextField();
            textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            textField.setEditable(false);
            textField.setBackground(UIManager.getColor("TextField.background"));
            
            edit = new JButton(RadixWareIcons.EDIT.EDIT.getIcon(10));
            clear = new JButton(RadixWareIcons.DELETE.CLEAR.getIcon(10));
            edit.setPreferredSize(new Dimension(15, 17));
            edit.setMaximumSize(new Dimension(15, 17));
            edit.setMinimumSize(new Dimension(15, 17));
            clear.setPreferredSize(new Dimension(15, 17));
            clear.setMaximumSize(new Dimension(15, 17));
            clear.setMinimumSize(new Dimension(15, 17));
            edit.setFocusPainted(false);
            clear.setFocusPainted(false);
            edit.setFocusable(false);
            clear.setFocusable(false);
            
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            
            add(textField);
            add(edit);
            add(clear);
        }

        @Override
        public void connect(PropertyEditor pe, PropertyEnv env) {
            editor = pe;
            textField.setText(pe.getAsText());
            edit.addMouseListener(editListener);
            clear.addMouseListener(clearListener);
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
            textField.setText(editor.getAsText());
        }

        @Override
        public boolean supportsTextEntry() {
            return true;
        }

        @Override
        public void reset() {
            model = null;
        }

        @Override
        public void addActionListener(ActionListener al) {
            synchronized(this){
                if (actionListenerList == null) {
                    actionListenerList = new ArrayList<ActionListener>();
                }

                actionListenerList.add(al);
            }
        }

        @Override
        public void removeActionListener(ActionListener al) {
            synchronized(this){
                if (actionListenerList != null) {
                    actionListenerList.remove(al);
                }
            }
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
            return c == this || c == textField || c == dialogPanel;
        } 
        
        private ActionEvent createActionEvent(){
            return new ActionEvent(this, ActionEvent.ACTION_PERFORMED, COMMAND_SUCCESS);
        }
        
        private ArrayList<ActionListener> createCopy(){
            ArrayList<ActionListener> list;
            synchronized(this){
                if (actionListenerList == null) {
                    return null;
                }
                list = new ArrayList<>(actionListenerList);
            }
            return list;
        }

        public void fireAction() {
            fireAction(createActionEvent(), createCopy()); 
        }
        
        void fireAction(ArrayList<ActionListener> list) {
            fireAction(createActionEvent(), list);
        }
        
        void fireAction(ActionEvent event, ArrayList<ActionListener> list) {
            if (event == null || list == null){
                return;
            }
            
            for (ActionListener l : list) {
                l.actionPerformed(event);
            }
           
        }
    }
}
