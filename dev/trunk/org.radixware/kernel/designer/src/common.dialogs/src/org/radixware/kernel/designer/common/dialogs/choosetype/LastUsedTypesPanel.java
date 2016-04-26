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
 * LastUsedTypesPanel.java
 *
 * Created on 11.03.2010, 14:21:28
 */
package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemRenderer;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueEditorPanel;


public final class LastUsedTypesPanel extends ValueEditorPanel<AdsTypeDeclaration> {

    public LastUsedTypesPanel(final Definition context) {
        this();

        list.setModel(new DefaultListModel());
        list.setCellRenderer(new Render(context, list));
    }

    public LastUsedTypesPanel() {
        super();
        initComponents();

        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                fireValueChange(getValue(), null);
            }
        });

        list.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && selectedAction != null) {
                    SwingUtilities.invokeLater(selectedAction);
                }
            }
        });
    }

    @Override
    public void setVisible(boolean aFlag) {
        jScrollPane1.setVisible(aFlag);
    }

    @Override
    public AdsTypeDeclaration getValue() {
        return (AdsTypeDeclaration) list.getSelectedValue();
    }

    @Deprecated
    @Override
    public void setValue(AdsTypeDeclaration value) {
    }

    @Override
    public boolean isValueChanged() {
        return false;
    }

    @Deprecated
    public void addElement(AdsTypeDeclaration type) {
        ((DefaultListModel) list.getModel()).addElement(type);
    }

    @Deprecated
    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    @Override
    public void addMouseListener(MouseListener listener) {
        list.addMouseListener(listener);
    }

    @Override
    public void removeMouseListener(MouseListener listener) {
        list.removeMouseListener(listener);
    }

    public void open(String path, ITypeFilter filter) {
        list.setModel(new ListModel(path, filter));
        list.setCellRenderer(new Render(filter.getContext(), list));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jScrollPane1.setViewportView(list);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList list;
    // End of variables declaration//GEN-END:variables
    private Runnable selectedAction;

    void setSelectedAction(Runnable action) {
        this.selectedAction = action;
    }

    private static final class ListModel extends AbstractListModel {

        private List<AdsTypeDeclaration> list;

        public ListModel(String path, ITypeFilter filter) {
            LastUsedTypeCollection instance = LastUsedTypeCollection.getInstance(path);
            instance.reloadLastUsed(filter);
            list = instance.getLastUsed();
        }

        @Override
        public int getSize() {
            return list.size();
        }

        @Override
        public Object getElementAt(int index) {
            return list.get(index);
        }
    }

    private static final class Render extends AbstractItemRenderer {

        final Definition context;

        public Render(Definition context, JList list) {
            super(list);
            this.context = context;
        }

        @Override
        public String getObjectName(Object object) {
            if (object instanceof AdsTypeDeclaration) {
                return ((AdsTypeDeclaration) object).getQualifiedName(context);
            }
            return "<unknown item>";
        }

        @Override
        public String getObjectLocation(Object object) {
            if (object instanceof AdsTypeDeclaration) {
                AdsTypeDeclaration declaration = (AdsTypeDeclaration) object;
                AdsType type = declaration.resolve(context).get();
                if (type == null) {
                    return ETypeNature.RADIX_TYPE.getNatureName();
                } else {
                    if (declaration.isVoid()) {
                        return ETypeNature.RADIX_TYPE.getNatureName();
                    } else if (declaration.isUndefined()) {
                        return ETypeNature.RADIX_TYPE.getNatureName();
                    } else if (declaration.isTypeArgument()) {
                        return ETypeNature.TYPE_PARAMETER.getNatureName();
                    } else {
                        EValType typeid = declaration.getTypeId();

                        if (declaration.isArray()) {
                            return ETypeNature.JAVA_ARRAY.getNatureName();
                        }

                        if (typeid.equals(EValType.USER_CLASS)) {
                            return ETypeNature.RADIX_CLASS.getNatureName();
                        } else if (typeid.equals(EValType.JAVA_CLASS)) {
                            return ETypeNature.JAVA_CLASS.getNatureName();
                        } else {
                            if (typeid.equals(EValType.XML)) {
                                if (type instanceof XmlType) {
                                    XmlType asXmlType = (XmlType) type;
                                    if (asXmlType.getSource() == null) {
                                        return ETypeNature.RADIX_TYPE.getNatureName();
                                    } else {
                                        return ETypeNature.RADIX_XML.getNatureName();
                                    }
                                } else {
                                    return ETypeNature.RADIX_XML.getNatureName();
                                }
                            } else if (type instanceof AdsEnumType
                                    && ((AdsEnumType) type).getSource().getItemType().equals(typeid)) {
                                return ETypeNature.RADIX_ENUM.getNatureName();
                            } else if (typeid.equals(EValType.JAVA_TYPE)) {
                                return ETypeNature.JAVA_PRIMITIVE.getNatureName();
                            } else {
                                return ETypeNature.RADIX_TYPE.getNatureName();
                            }
                        }

                    }
                }
            }
            return "";
        }

        @Override
        public RadixIcon getObjectIcon(Object object) {
            if (object instanceof AdsTypeDeclaration) {
                return ((AdsTypeDeclaration) object).getIcon();
            }
            return null;
        }

        @Override
        public RadixIcon getObjectLocationIcon(Object object) {
            return null;
        }
    }

}