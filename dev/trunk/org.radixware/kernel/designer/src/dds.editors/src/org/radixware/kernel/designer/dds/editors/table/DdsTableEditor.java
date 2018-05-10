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

package org.radixware.kernel.designer.dds.editors.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.dds.AuditTriggersUpdater;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.editors.RadixObjectModalEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.dds.script.defs.UserPropTriggersUpdater;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassBodyPanel;
import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;

/**
 * DdsTableDef editor.
 *
 */
public final class DdsTableEditor extends RadixObjectModalEditor<DdsTableDef> {

    private final MainPanel mainPanel;
    private final ColumnsPanel columnsPanel;
    private final AdsSqlClassBodyPanel viewPanel;
    private final IndicesPanel indicesPanel;
    private final TriggersPanel triggersPanel;
    private final InitialValuesPanel initialValuesPanel;
    private final DdsTableDef copy;

    protected DdsTableEditor(DdsTableDef table) {
        super(table);
        initComponents();

        //this.setMinimumSize(new Dimension(780, 680));
        this.setMinimumSize(new Dimension(640, 480));

        copy = DdsTableDef.Factory.newInstance(table, table.getOwnerModel());

        // commented - because memory leaks.
        //final Checker checker = new Checker();
        //checker.check(copy); // to display problems in SQML editors

        boolean readOnly = table.isReadOnly();

//        readOnly = false; // For debug purpose only

        mainPanel = new MainPanel(copy);
        mainPanel.setReadOnly(readOnly);

        columnsPanel = new ColumnsPanel(copy);
        columnsPanel.setReadOnly(readOnly);

        if (table instanceof DdsViewDef) {
            viewPanel = new AdsSqlClassBodyPanel();
            viewPanel.open((DdsViewDef) copy, readOnly ? EditorOpenInfo.DEFAULT_READONLY: EditorOpenInfo.DEFAULT_EDITABLE);
        } else {
            viewPanel = null;
        }

        indicesPanel = new IndicesPanel(copy);
        indicesPanel.setReadOnly(readOnly);

        triggersPanel = new TriggersPanel(copy);
        triggersPanel.setReadOnly(readOnly);

        initialValuesPanel = new InitialValuesPanel(copy);
        initialValuesPanel.setReadOnly(readOnly);

        tabPane.addTab(NbBundle.getBundle(DdsTableEditor.class).getString("Tab_Main"),
                RadixWareIcons.EDIT.PROPERTIES.getIcon(), createTab(mainPanel, mainPanel.getStateDisplayer()));
        tabPane.addTab(NbBundle.getBundle(DdsTableEditor.class).getString("Tab_Columns"),
                DdsDefinitionIcon.COLUMN.getIcon(), columnsPanel);
        if (table instanceof DdsViewDef) {
            tabPane.addTab(NbBundle.getBundle(DdsTableEditor.class).getString("Tab_Query"),
                    RadixObjectIcon.SQML.getIcon(), viewPanel);
        }
        tabPane.addTab(NbBundle.getBundle(DdsTableEditor.class).getString("Tab_Indices"),
                DdsDefinitionIcon.INDEX.getIcon(), indicesPanel);
        tabPane.addTab(NbBundle.getBundle(DdsTableEditor.class).getString("Tab_Triggers"),
                DdsDefinitionIcon.TRIGGER.getIcon(), triggersPanel);
        tabPane.addTab(NbBundle.getBundle(DdsTableEditor.class).getString("Tab_Initial_Values"),
                RadixWareIcons.DATABASE.TABLE_INITIAL_VALUES.getIcon(), initialValuesPanel);

        tabPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                if (tabPane.getSelectedComponent() instanceof IUpdateable) {
                    ((IUpdateable) tabPane.getSelectedComponent()).update();
                }
            }
        });

        this.setComplete(true);

    }

    private JPanel createTab(JPanel mainPanel, StateDisplayer stateDisplayer) {
        JPanel statePanel = new JPanel(new BorderLayout());
        statePanel.setBorder(new EmptyBorder(0, 14, 5, 0));
        statePanel.add(stateDisplayer, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(statePanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPane = new javax.swing.JTabbedPane();

        setLayout(new java.awt.BorderLayout());

        tabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        add(tabPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabPane;
    // End of variables declaration//GEN-END:variables

    private void assign(DdsUniqueConstraintDef uk, DdsUniqueConstraintDef copy) {
        uk.setAutoDbName(copy.isAutoDbName());
        uk.setDbName(copy.getDbName());
        uk.setDescription(copy.getDescription());
        uk.setName(copy.getName());
        uk.getDbOptions().clear();
        uk.getDbOptions().addAll(copy.getDbOptions());
        uk.setDescriptionId(copy.getDescriptionId());
    }

    private void assign(DdsPrimaryKeyDef pk, DdsPrimaryKeyDef copy) {
        pk.setAutoDbName(copy.isAutoDbName());
        pk.setDbName(copy.getDbName());
        pk.setDescription(pk.getDescription());
        //RADIX-8021
        pk.setGeneratedInDb(copy.getIsGenerateInDbOption());
        pk.setName(copy.getName());
        pk.setTablespaceDbName(copy.getTablespaceDbName());

        RadixObjectsUtils.moveItems(copy.getColumnsInfo(), pk.getColumnsInfo());
        pk.getDbOptions().clear();
        pk.getDbOptions().addAll(copy.getDbOptions());

        pk.setDescriptionId(copy.getDescriptionId());
        assign(pk.getUniqueConstraint(), copy.getUniqueConstraint());
    }

    private void assign(DdsTableDef table, DdsTableDef copy) {
        table.setName(copy.getName());
        table.setAutoDbName(copy.isAutoDbName());
        table.setDbName(copy.getDbName());
        table.setDescription(copy.getDescription());
        table.setGeneratedInDb(copy.isGeneratedInDb());
        table.setHidden(copy.isHidden());
        table.setOverwrite(copy.isOverwrite());
        table.setDescriptionId(copy.getDescriptionId());

        DdsTableDef overwritten = table.findOverwritten();
        if (overwritten == null) {
            table.setTablespace(copy.getTablespace());
            RadixObjectsUtils.moveItems(copy.getPartition().getItems(), table.getPartition().getItems());
            table.setGlobalTemporary(copy.isGlobalTemporary());
            table.setOnCommitPreserveRows(copy.isOnCommitPreserveRows());

            table.getAuditInfo().setAuditReferenceId(copy.getAuditInfo().getAuditReferenceId());
            table.getAuditInfo().setEnabledForDelete(copy.getAuditInfo().isEnabledForDelete());
            table.getAuditInfo().setEnabledForInsert(copy.getAuditInfo().isEnabledForInsert());
            table.getAuditInfo().setEnabledForUpdate(copy.getAuditInfo().isEnabledForUpdate());

            table.getExtOptions().clear();
            table.getExtOptions().addAll(copy.getExtOptions());
        }

        columnsPanel.removeListeners();
        RadixObjectsUtils.moveItems(copy.getColumns().getLocal(), table.getColumns().getLocal());
        indicesPanel.removeListeners();
        RadixObjectsUtils.moveItems(copy.getIndices().getLocal(), table.getIndices().getLocal());
        triggersPanel.removeListeners();
        RadixObjectsUtils.moveItems(copy.getTriggers().getLocal(), table.getTriggers().getLocal());

        if (overwritten == null) {
            assign(table.getPrimaryKey(), copy.getPrimaryKey());
        }

        if (table instanceof DdsViewDef) {
            final DdsViewDef view = (DdsViewDef) table;
            final DdsViewDef copyView = (DdsViewDef) copy;
            RadixObjectsUtils.moveItems(copyView.getSqml().getItems(), view.getSqml().getItems());
            view.setDistinct(copyView.isDistinct());
            view.setWithOption(copyView.getWithOption());
            view.getUsedTables().loadFrom(copyView.getUsedTables());
        }
        table.setDeprecated(copy.isDeprecated());
    }

    @Override
    protected void apply() {
        DdsTableDef table = getTable();
        if (!table.isReadOnly()) {
            initialValuesPanel.completeEditing();
            DdsUniqueConstraintsUpdater.update(table, copy);
            assign(table, copy);
            UserPropTriggersUpdater.update(table);
            AuditTriggersUpdater.update(table);

            final List<DdsReferenceDef> refs = new ArrayList<DdsReferenceDef>();
            refs.addAll(table.collectIncomingReferences());
            refs.addAll(table.collectOutgoingReferences());
            for (DdsReferenceDef ref : refs) {
                if (!ref.isReadOnly()) {
                    DbNameUtils.updateAutoDbNames(ref);
                }
            }
        }
    }

    private DdsTableDef getTable() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo openInfo) {
        for (RadixObject obj = openInfo.getTarget(); obj != null; obj = obj.getContainer()) {
            if (obj instanceof DdsColumnDef) {
                tabPane.setSelectedComponent(columnsPanel);
                columnsPanel.setSelectedColumn((DdsColumnDef) obj);
                break;
            } else if (obj instanceof DdsIndexDef) {
                tabPane.setSelectedComponent(indicesPanel);
                indicesPanel.setSelectedIndex((DdsIndexDef) obj);
                break;
            } else if (obj instanceof DdsTriggerDef) {
                tabPane.setSelectedComponent(triggersPanel);
                triggersPanel.setSelectedTrigger((DdsTriggerDef) obj);
                break;
            } else if (obj instanceof Scml.Item) {
                Scml scml = ((Scml.Item) obj).getOwnerScml();
                for (Definition def = scml.getOwnerDefinition(); def != null; def = def.getOwnerDefinition()) {
                    if (def instanceof DdsColumnDef) {
                        tabPane.setSelectedComponent(columnsPanel);
                        columnsPanel.setSelectedColumn((DdsColumnDef) def);
                        columnsPanel.open(openInfo);
                        break;
                    } else if (def instanceof DdsTriggerDef) {
                        tabPane.setSelectedComponent(triggersPanel);
                        triggersPanel.setSelectedTrigger((DdsTriggerDef) def);
                        triggersPanel.open(openInfo);
                        break;
                    }
                }
                break;
            }
        }
        return super.open(openInfo);
    }

    @Override
    public void update() {
        // TODO:
    }

    public static final class Factory implements IEditorFactory<DdsTableDef> {

        @Override
        public DdsTableEditor newInstance(DdsTableDef table) {
            return new DdsTableEditor(table);
        }
    }
}
