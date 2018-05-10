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

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.PropertyRefProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionPanel;


public class PropertyRefPanel extends JPanel implements PropertyChangeListener {

    private PropertyEnv env;
    private PropertyRefEditor editor;
    private ChooseDefinitionPanel panel = new ChooseDefinitionPanel();

    PropertyRefPanel(PropertyRefEditor editor,  PropertyEnv env) {
        this.env = env;
        this.editor = editor;

        env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        env.addPropertyChangeListener(this);
        initComponents();

        setLayout(new java.awt.BorderLayout());
        add(panel, BorderLayout.CENTER);

        Set<Definition> list = new HashSet<>();

//        IEditorPagesContainer container = AdsUIUtil.getOwnerEditorPagesContainer(getNode());
//        if (container != null) {
//            EditorPages pages = container.getEditorPages();
//            if (pages != null) {
//                Collection<Id> availablePageIds = pages.getOrder().getOrderedPageIds();
//                for (Id id : availablePageIds) {
//                    AdsEditorPageDef page = pages.findById(id, EScope.ALL);
//                    if (page != null) {
//                        List<PropertyRef> props = page.getUsedProperties().get();
//                        for (PropertyRef ref : props) {
//                            AdsDefinition def = ref.findProperty();
//                            if (def != null) {
//                                list.add(def);
//                            }
//                        }
//                    }
//                }
//            }
//        }

        final IModelPublishableProperty.Provider provider = AdsUIProperty.PropertyRefProperty.getProvider(getNode());
        if (provider != null) {
            final List<? extends IModelPublishableProperty> properties = provider.getModelPublishablePropertySupport().list(
                    AdsUIUtil.getUiDef(getNode()).getUsageEnvironment(), EScope.ALL, null);

            for (final IModelPublishableProperty property : properties) {
                IAdsPresentableProperty presentableProperty = null;
                if (property instanceof IAdsPresentableProperty) {
                    presentableProperty = (IAdsPresentableProperty) property;
                } else if (property instanceof AdsPropertyPresentationPropertyDef && ((AdsPropertyPresentationPropertyDef) property).isLocal()) {
                    presentableProperty = (IAdsPresentableProperty) ((AdsPropertyPresentationPropertyDef) property).findServerSideProperty();
                }
                if (presentableProperty != null) {
                    final ServerPresentationSupport presentationSupport = presentableProperty.getPresentationSupport();
                    if (presentationSupport != null) {
                        final PropertyPresentation presentation = presentationSupport.getPresentation();
                        if (presentation != null && presentation.isPresentable()) {
                            list.add((Definition) property);
                        }
                    }
                }
            }
            AdsAbstractUIDef uiDef = AdsUIUtil.getUiDef(getNode());
            if (uiDef != null) {
                AdsModelClassDef modelClass = uiDef.getModelClass();
                if (modelClass != null) {
                    for (AdsPropertyDef prop : modelClass.getProperties().get(EScope.ALL)) {
                        if (prop instanceof AdsPropertyPresentationPropertyDef && ((AdsPropertyPresentationPropertyDef) prop).isLocal()) {
                            list.add(prop);
                        }
                    }
                }
            }
        }
        panel.open(ChooseDefinitionCfg.Factory.newInstance(list), false);
    }

    private AdsUIProperty.PropertyRefProperty getProperty() {
        return (AdsUIProperty.PropertyRefProperty) editor.getValue();
    }

    private RadixObject getNode() {
        return ((UIPropertySupport) editor.getSource()).getNode();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(200, 140));
        setPreferredSize(new java.awt.Dimension(320, 260));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {

            final Definition def = panel.getSelected();
            final PropertyRefProperty oldVal = getProperty();
            final PropertyRefProperty prop = new PropertyRefProperty(oldVal.getName());

            prop.setPropertyId(def != null ? def.getId() : null);

            editor.setValue(prop);
            ((UIPropertySupport) editor.getSource()).setValue(prop);

            if (def == null) {
                return;
            }

            final RadixObject node = getNode();
            final AdsUIProperty.StringProperty p = (AdsUIProperty.StringProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "objectName", node);
            assert p != null;
            
            String name = p.value;
            name = def.getName() + name.substring(0, 1).toUpperCase() + name.substring(1);
                    
            final String className = AdsUIUtil.getUiClassName(node);
            if (AdsMetaInfo.RWT_PROP_EDITOR.equals(className))
                name += "Editor";
            if (AdsMetaInfo.RWT_PROP_LABEL.equals(className))
                name += "Label";
            node.setName(name);
        }
    }
}
