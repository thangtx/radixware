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
 * AdsMethodThrowListEditor.java
 *
 * Created on Nov 25, 2008, 2:33:55 PM
 */
package org.radixware.kernel.designer.ads.method.throwslist;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsExceptionClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList.ThrowsListItem;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ProfileUtilities;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.method.profile.ChangeProfilePanel;
import org.radixware.kernel.designer.ads.method.profile.DescriptionCellEditor;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionModel;


public class AdsMethodThrowsListEditor extends javax.swing.JPanel {

    private static final class ThrowsVisitorProvider extends VisitorProvider {

        AdsClassDef owner;
        AdsMethodDef context;
        boolean isOverride = false;

        public ThrowsVisitorProvider(AdsClassDef owner, AdsMethodDef context) {
            this.owner = owner;
            this.context = context;
            isOverride = context.isOverride();
        }
        
       

        @Override
        public boolean isTarget(RadixObject obj) {
            if (obj instanceof AdsExceptionClassDef) {
                if (isOverride){
                    final AdsMethodDef overriddenMethod = context.getHierarchy().findOverridden().get();
                    if (overriddenMethod != null){
                        List<AdsMethodThrowsList.ThrowsListItem> overThrowsList = overriddenMethod.getProfile().getThrowsList().list();
                        if (ProfileUtilities.isCorrectExeptionInThrowList((AdsExceptionClassDef) obj,context,overThrowsList)){
                            return ERuntimeEnvironmentType.compatibility(getUsageEnvironment(owner), getUsageEnvironment(obj));
                        }
                    }
                } else {
                    return ERuntimeEnvironmentType.compatibility(getUsageEnvironment(owner), getUsageEnvironment(obj));
                }
            }
            return false;
        }

        private ERuntimeEnvironmentType getUsageEnvironment(RadixObject object) {
            if (object instanceof IEnvDependent) {
                return ((IEnvDependent) object).getUsageEnvironment();
            }
            return null;
        }
    }
        
    private AdsMethodThrowsList list;
    private AdsMethodDef context;
    private AdsClassDef owner;
    private boolean readonly = false;
    private CommonParametersEditorCellLib.TypeCellEditor typeEditor;
    private ThrowsTableModel tableModel;

    public AdsMethodThrowsListEditor() {
        initComponents();
        addBtn.setEnabled(false);
        removeBtn.setEnabled(false);
        clearBtn.setEnabled(false);
        addBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(owner, new ThrowsVisitorProvider(owner,context));
                List<Definition> defs = ChooseDefinition.chooseDefinitions(cfg);

                if (defs != null && defs.size() > 0) {
                    for (int i = 0; i <= defs.size() - 1; i++) {
                        final AdsTypeDeclaration type = AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) defs.get(i));
                        final ThrowsItem throwsItem = new ThrowsItem(AdsMethodThrowsList.ThrowsListItem.Factory.newTemporaryInstance(list, type, null));
                        tableModel.addValue(throwsItem);
                    }
                    changeSupport.fireChange();
                }
                onFocusEvent();
            }
        });

        removeBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = table.getSelectedRow();
                ThrowsItem item = tableModel.getValueAt(selected);
                tableModel.removeValue(item);
                changeSupport.fireChange();
                onFocusEvent();
            }
        });

        clearBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.clear();
                changeSupport.fireChange();
                onFocusEvent();
            }
        });

        table.setCellSelectionEnabled(true);

        tableModel = ThrowsTableModel.getModelFor(new TreeSet<>(ThrowsItem.THROWS_NAME_COMPARATOR));

        table.setModel(tableModel);

        table.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                onFocusEvent();
            }

            @Override
            public void focusGained(FocusEvent e) {
                onFocusEvent();
            }
        });
    }

    public void open(AdsMethodDef context, AdsClassDef owner, boolean readonly) {
        this.context = context;
        this.list = context.getProfile().getThrowsList();
        this.owner = owner;
        this.readonly = readonly;

        tableModel = ThrowsTableModel.getModelFor(list);
        tableModel.setContext(context);
        table.setModel(tableModel);

        typeEditor = new CommonParametersEditorCellLib.TypeCellEditor(changeSupport, true, context instanceof AdsTransparentMethodDef, tableModel);

        table.getColumnModel().getColumn(0).setCellEditor(typeEditor);
        table.getColumnModel().getColumn(1).setCellEditor(new DescriptionCellEditor());
        table.getColumnModel().getColumn(0).setCellRenderer(new ThrowsCellRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new CommonParametersEditorCellLib.LabelWithInsetsRenderer());

        if (list != null) {
            addBtn.setEnabled(!readonly);
            clearBtn.setEnabled(!readonly);
        }
        onFocusEvent();
    }

    public List<AdsTypeDeclaration> getCurrentThrowsList() {
        List<AdsTypeDeclaration> result = new ArrayList<>();
        for (int i = 0; i <= tableModel.getRowCount() - 1; i++) {
            ThrowsItem item = tableModel.getValueAt(i);
            result.add(item.getType());
        }
        return result;
    }

    public boolean apply() {
        if (this.list != null) {

            list.clear();

            for (ThrowsItem throwsItem : tableModel.getAllThrows()) {
                final ThrowsListItem item = throwsItem.createItem();
                list.add(item);
                throwsItem.getDescriptionModel().applyFor(item);
            }

            return true;
        }
        return false;
    }

    public void update() {
        this.list = context.getProfile().getThrowsList();
        if (list != null) {
            tableModel = ThrowsTableModel.getModelFor(list);
            table.setModel(tableModel);
        } else {
            tableModel.clear();
        }
        onFocusEvent();
    }

    public Map<Object, DescriptionModel> getDescriptionMap() {
        Map<Object, DescriptionModel> descriptionMap = new HashMap<>();

        for (final ThrowsItem throwsItem : tableModel.getAllThrows()) {
            descriptionMap.put(ChangeProfilePanel.THROW_ITEM_DESCRIPTION_KEY + throwsItem.getType().getQualifiedName(), throwsItem.getDescriptionModel());
        }

        return descriptionMap;
    }

    private void onFocusEvent() {
        if (list != null && !readonly) {
            if (tableModel.getRowCount() > 0) {
                clearBtn.setEnabled(true);
                final int selected = table.getSelectedRow();
                removeBtn.setEnabled(selected > -1 && selected < tableModel.getRowCount());
            } else {
                removeBtn.setEnabled(false);
                clearBtn.setEnabled(false);
            }
        } else {
            addBtn.setEnabled(false);
            removeBtn.setEnabled(false);
            clearBtn.setEnabled(false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        addBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsMethodThrowsListEditor.class, "ThrowsDialog")); // NOI18N

        addBtn.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon(13, 13));
        addBtn.setText(org.openide.util.NbBundle.getMessage(AdsMethodThrowsListEditor.class, "AddBtn")); // NOI18N
        addBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        removeBtn.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        removeBtn.setText(org.openide.util.NbBundle.getMessage(AdsMethodThrowsListEditor.class, "RemoveBtn")); // NOI18N
        removeBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        clearBtn.setIcon(RadixWareDesignerIcon.DELETE.CLEAR.getIcon(13, 13));
        clearBtn.setText(org.openide.util.NbBundle.getMessage(AdsMethodThrowsListEditor.class, "ClearBtn")); // NOI18N
        clearBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(addBtn)
                        .addComponent(removeBtn))
                    .addComponent(clearBtn)))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addBtn, clearBtn, removeBtn});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearBtn))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeBtn;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable table;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    class ThrowsCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            ThrowsItem current = AdsMethodThrowsListEditor.this.tableModel.getValueAt(row);
            setText(current.toString());
            RadixIcon icon = current.getType().getIcon();
            if (icon != null) {
                setIcon(icon.getIcon(16, 16));
            } else {
                setIcon(RadixObjectIcon.UNKNOWN.getIcon(16, 16));
            }
            return this;
        }
    }

}
