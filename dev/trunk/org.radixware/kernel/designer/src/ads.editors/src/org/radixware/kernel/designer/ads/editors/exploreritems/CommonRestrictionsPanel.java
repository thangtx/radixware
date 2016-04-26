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
 * CommonRestrictionsPanel.java
 *
 * Created on 01.06.2009, 17:37:34
 */
package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.RadixError;


public class CommonRestrictionsPanel extends javax.swing.JPanel {

    public static final Integer SELECTOR_EXPLORER_ITEM = 0;
    public static final Integer PARENT_REF_EXPLORER_ITEM = 1;
    public static final Integer EDITOR_PRESENTATION = 2;
    public static final Integer SELECTOR_PRESENTATION = 3;
    private JCheckBox createCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-Create"));
    private JCheckBox deleteAllCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-DeleteAll"));
    private JCheckBox deleteCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-Delete"));
    private JCheckBox editorUnderCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-EditorUnderSelector"));
    private JCheckBox insertAllCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-InsertAll"));
    private JCheckBox insertCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-InsertIntoTree"));
    private JCheckBox multipleCopyCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-MultipleCopy"));
    private JCheckBox runEditorCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-RunEditor"));
//    private JCheckBox transferAllOutCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-TransAllOut"));
//    private JCheckBox transferInCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-TransIn"));
//    private JCheckBox transferOutCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-TransOut"));
    private JCheckBox updateCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-Update"));
    private JCheckBox contextlessUsageCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-ContextlessUsage"));
    private JCheckBox anyCommandCheck = new JCheckBox(NbBundle.getMessage(CommonRestrictionsPanel.class, "ParentRefRestrictions-AnyCommand"));
    private Restrictions restrictions;
    private Map<JCheckBox, ERestriction> checkmap;

    /** Creates new form CommonRestrictionsPanel */
    public CommonRestrictionsPanel() {
        initComponents();
        checkmap = new HashMap<JCheckBox, ERestriction>(ERestriction.values().length);
        checkmap.put(insertCheck, ERestriction.INSERT_INTO_TREE);
        checkmap.put(insertAllCheck, ERestriction.INSERT_ALL_INTO_TREE);
        checkmap.put(runEditorCheck, ERestriction.RUN_EDITOR);
        checkmap.put(editorUnderCheck, ERestriction.EDITOR);
        checkmap.put(createCheck, ERestriction.CREATE);
        checkmap.put(deleteCheck, ERestriction.DELETE);
        checkmap.put(deleteAllCheck, ERestriction.DELETE_ALL);
        checkmap.put(updateCheck, ERestriction.UPDATE);
//        checkmap.put(transferInCheck, ERestriction.TRANSFER_IN);
//        checkmap.put(transferOutCheck, ERestriction.TRANSFER_OUT);
//        checkmap.put(transferAllOutCheck, ERestriction.TRANSFER_OUT_ALL);
        checkmap.put(multipleCopyCheck, ERestriction.MULTIPLE_COPY);
        checkmap.put(contextlessUsageCheck, ERestriction.CONTEXTLESS_USAGE);
        checkmap.put(anyCommandCheck, ERestriction.ANY_COMMAND);

        ActionListener checksListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!CommonRestrictionsPanel.this.isUpdate) {
                    if (e.getSource().getClass().equals(JCheckBox.class)) {
                        JCheckBox box = (JCheckBox) e.getSource();
                        ERestriction flag = CommonRestrictionsPanel.this.checkmap.get(box);
                        if (flag != null) {
                            onCheckBoxSelectionChanged(box, flag);
                        }
                    }
                }
            }
        };

        insertCheck.addActionListener(checksListener);
        insertAllCheck.addActionListener(checksListener);
        runEditorCheck.addActionListener(checksListener);
        editorUnderCheck.addActionListener(checksListener);
        createCheck.addActionListener(checksListener);
        deleteCheck.addActionListener(checksListener);
        deleteAllCheck.addActionListener(checksListener);
        updateCheck.addActionListener(checksListener);
//        transferInCheck.addActionListener(checksListener);
//        transferOutCheck.addActionListener(checksListener);
//        transferAllOutCheck.addActionListener(checksListener);
        multipleCopyCheck.addActionListener(checksListener);
        contextlessUsageCheck.addActionListener(checksListener);
        anyCommandCheck.addActionListener(checksListener);
    }

    private void onCheckBoxSelectionChanged(JCheckBox box, ERestriction flag) {
        boolean isSelected = box.isSelected();
        if (isSelected) {
            restrictions.deny(flag);
        } else {
            restrictions.allow(flag);
        }

        if (flag.equals(ERestriction.DELETE)) {
            if (isSelected) {
                deleteAllCheck.setEnabled(false);
                deleteAllCheck.setSelected(true);
                restrictions.deny(ERestriction.DELETE_ALL);
            } else {
                deleteAllCheck.setEnabled(restrictions.canAllow(ERestriction.DELETE_ALL) && restrictions.canDeny(ERestriction.DELETE_ALL));
            }
        }
        if (flag.equals(ERestriction.INSERT_INTO_TREE)) {
            if (isSelected) {
                insertAllCheck.setEnabled(false);
                insertAllCheck.setSelected(true);
                restrictions.deny(ERestriction.INSERT_ALL_INTO_TREE);
            } else {
                insertAllCheck.setEnabled(restrictions.canAllow(ERestriction.INSERT_ALL_INTO_TREE) && restrictions.canDeny(ERestriction.INSERT_ALL_INTO_TREE));
            }
        }
    }

    @Override
    public Dimension getMinimumSize() {
        return super.getPreferredSize();
    }

    private void setupUI() {
        removeAll();
        setupLayout();
        if (type == PARENT_REF_EXPLORER_ITEM) {
            restrictions = ((AdsParentRefExplorerItemDef) context).getRestrictions();
            add(updateCheck);
            add(anyCommandCheck);
        } else if (type == EDITOR_PRESENTATION) {
            restrictions = ((AdsPresentationDef) context).getRestrictions(false);
            add(createCheck);
            add(deleteCheck);
            add(updateCheck);
        } else if (type == SELECTOR_EXPLORER_ITEM || type == SELECTOR_PRESENTATION) {
            if (type == SELECTOR_PRESENTATION) {
                restrictions = ((AdsPresentationDef) context).getRestrictions(false);
            } else {
                restrictions = ((AdsSelectorExplorerItemDef) context).getRestrictions();
            }
            add(insertCheck);
            add(insertAllCheck);
            add(runEditorCheck);
            add(editorUnderCheck);
            add(createCheck);
            add(deleteCheck);
            add(deleteAllCheck);
            add(updateCheck);
//            add(transferInCheck);
//            add(transferOutCheck);
//            add(transferAllOutCheck);
            add(multipleCopyCheck);
            if (type == SELECTOR_PRESENTATION) {
                add(contextlessUsageCheck);
            }
        } else {
            throw new RadixError(NbBundle.getMessage(CommonRestrictionsPanel.class, "Usupported definition type: " + context.getClass().getName()));
        }
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
        setupUI();
    }

    public boolean isReadonly() {
        return this.readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension superSize = super.getPreferredSize();
        if (superSize.width < 150) {
            return new Dimension(150, superSize.height);
        }
        return superSize;
    }
    private AdsDefinition context;
    private boolean isRestrictionsInherited = false;
    private boolean readonly = false;
    private boolean isUpdate = false;
    private int type = SELECTOR_EXPLORER_ITEM;

    public void open(AdsDefinition context, int type) {
        this.context = context;
        this.type = type;
        setupUI();
        update();
    }

    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    public void setIsResctrictionsInhertied(boolean isRestrictionsInherited) {
        this.isRestrictionsInherited = isRestrictionsInherited;
    }

    public void update() {
        isUpdate = true;
        if (restrictions != null) {
            boolean commonReadonlyState = (context != null && context.isReadOnly()) || readonly;

            readonly = isRestrictionsInherited || commonReadonlyState || restrictions.isReadOnly();

            insertCheck.setSelected(restrictions.isDenied(ERestriction.INSERT_INTO_TREE));
            insertAllCheck.setSelected(restrictions.isDenied(ERestriction.INSERT_ALL_INTO_TREE));
            runEditorCheck.setSelected(restrictions.isDenied(ERestriction.RUN_EDITOR));
            editorUnderCheck.setSelected(restrictions.isDenied(ERestriction.EDITOR));
            createCheck.setSelected(restrictions.isDenied(ERestriction.CREATE));
            deleteCheck.setSelected(restrictions.isDenied(ERestriction.DELETE));
            deleteAllCheck.setSelected(restrictions.isDenied(ERestriction.DELETE_ALL));
            updateCheck.setSelected(restrictions.isDenied(ERestriction.UPDATE));
//            transferInCheck.setSelected(restrictions.isDenied(ERestriction.TRANSFER_IN));
//            transferOutCheck.setSelected(restrictions.isDenied(ERestriction.TRANSFER_OUT));
//            transferAllOutCheck.setSelected(restrictions.isDenied(ERestriction.TRANSFER_OUT_ALL));
            multipleCopyCheck.setSelected(restrictions.isDenied(ERestriction.MULTIPLE_COPY));
            contextlessUsageCheck.setSelected(restrictions.isDenied(ERestriction.CONTEXTLESS_USAGE));
            anyCommandCheck.setSelected(restrictions.isDenied(ERestriction.ANY_COMMAND));

            insertCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.INSERT_INTO_TREE) && restrictions.canDeny(ERestriction.INSERT_INTO_TREE));
            insertAllCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.INSERT_ALL_INTO_TREE) && restrictions.canDeny(ERestriction.INSERT_ALL_INTO_TREE) && !restrictions.isDenied(ERestriction.INSERT_INTO_TREE));
            runEditorCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.RUN_EDITOR) && restrictions.canDeny(ERestriction.RUN_EDITOR));
            editorUnderCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.EDITOR) && restrictions.canDeny(ERestriction.EDITOR));
            createCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.CREATE) && restrictions.canDeny(ERestriction.CREATE));
            deleteCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.DELETE) && restrictions.canDeny(ERestriction.DELETE));
            deleteAllCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.DELETE_ALL) && restrictions.canDeny(ERestriction.DELETE_ALL) && !restrictions.isDenied(ERestriction.DELETE));
            updateCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.UPDATE) && restrictions.canDeny(ERestriction.UPDATE));
//            transferInCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.TRANSFER_IN) && restrictions.canDeny(ERestriction.TRANSFER_IN));
//            transferOutCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.TRANSFER_OUT) && restrictions.canDeny(ERestriction.TRANSFER_OUT));
//            transferAllOutCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.TRANSFER_OUT_ALL) && restrictions.canDeny(ERestriction.TRANSFER_OUT_ALL));
            multipleCopyCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.MULTIPLE_COPY) && restrictions.canDeny(ERestriction.MULTIPLE_COPY));
            contextlessUsageCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.CONTEXTLESS_USAGE) && restrictions.canDeny(ERestriction.CONTEXTLESS_USAGE));
            anyCommandCheck.setEnabled(!readonly && restrictions.canAllow(ERestriction.ANY_COMMAND) && restrictions.canDeny(ERestriction.ANY_COMMAND));
        }
        isUpdate = false;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CommonRestrictionsPanel.class, "SelectorRestrictions-DenyTip"))); // NOI18N
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        insertCheck.setAlignmentX(0);
        insertCheck.setAlignmentY(0);
        insertAllCheck.setAlignmentX(0);
        insertAllCheck.setAlignmentY(0);
        runEditorCheck.setAlignmentX(0);
        runEditorCheck.setAlignmentY(0);
        editorUnderCheck.setAlignmentX(0);
        editorUnderCheck.setAlignmentY(0);
        createCheck.setAlignmentX(0);
        createCheck.setAlignmentY(0);
        deleteCheck.setAlignmentX(0);
        deleteCheck.setAlignmentY(0);
        deleteAllCheck.setAlignmentX(0);
        deleteAllCheck.setAlignmentY(0);
        updateCheck.setAlignmentX(0);
        updateCheck.setAlignmentY(0);
//        transferInCheck.setAlignmentX(0);
//        transferInCheck.setAlignmentY(0);
//        transferOutCheck.setAlignmentX(0);
//        transferOutCheck.setAlignmentY(0);
//        transferAllOutCheck.setAlignmentX(0);
//        transferAllOutCheck.setAlignmentY(0);
        multipleCopyCheck.setAlignmentX(0);
        multipleCopyCheck.setAlignmentY(0);
        contextlessUsageCheck.setAlignmentX(0);
        contextlessUsageCheck.setAlignmentY(0);
        anyCommandCheck.setAlignmentX(0);
        anyCommandCheck.setAlignmentY(0);
    }
}
