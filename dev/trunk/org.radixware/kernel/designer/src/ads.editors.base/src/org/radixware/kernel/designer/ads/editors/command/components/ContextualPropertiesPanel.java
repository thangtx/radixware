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
 * ContextualPropertiesPanel.java
 *
 * Created on 18.05.2009, 18:23:06
 */
package org.radixware.kernel.designer.ads.editors.command.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsGroupCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.enums.ECommandAccessibility;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel;


public class ContextualPropertiesPanel extends javax.swing.JPanel {

    private AdsCommandDef command;
    private JCheckBox confirmCheck = new JCheckBox(NbBundle.getMessage(ContextualPropertiesPanel.class, "AdsCommand-ConfirmationCheck"));
    private JCheckBox localCheck = new JCheckBox(NbBundle.getMessage(ContextualPropertiesPanel.class, "AdsCommand-LocalTip"));
    private JCheckBox readOnlyCheck = new JCheckBox(NbBundle.getMessage(ContextualPropertiesPanel.class, "AdsCommand-ReadOnlyTip"));
    private JCheckBox showBtnCheck = new JCheckBox(NbBundle.getMessage(ContextualPropertiesPanel.class, "ScopeProperties-ShowBtn"));
    private JComboBox scopeAccessBox = new JComboBox();
    private final JCheckBox rereadAfterExecute = new JCheckBox(NbBundle.getMessage(ContextualPropertiesPanel.class, "ScopeProperties-Reread"));
    private final JCheckBox traceGUIActivity = new JCheckBox(NbBundle.getMessage(ContextualPropertiesPanel.class, "ScopeProperties-TraceGUIActivity"));
    
    /**
     * Creates new form ContextualPropertiesPanel
     */
    public ContextualPropertiesPanel() {
        initComponents();
        scopeAccessBox.setPrototypeDisplayValue(ECommandAccessibility.ONLY_FOR_EXISTENT.getName());
        scopeAccessBox.setModel(new CommandAccessibilityComboModel(false));
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ContextualPropertiesPanel.this.isUpdate) {
                    Object selected = ContextualPropertiesPanel.this.scopeAccessBox.getSelectedItem();
                    ContextualPropertiesPanel.this.accessBoxItemChanged(selected);
                }
            }
        };
        scopeAccessBox.addActionListener(actionListener);

        ItemListener showBtnListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!ContextualPropertiesPanel.this.isUpdate) {
                    boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
                    ContextualPropertiesPanel.this.command.getPresentation().setIsVisible(isSelected);
                    ContextualPropertiesPanel.this.changeSupport.fireChange();
                }
            }
        };
        showBtnCheck.addItemListener(showBtnListener);

        ItemListener confirmListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!ContextualPropertiesPanel.this.isUpdate) {
                    boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
                    ContextualPropertiesPanel.this.command.getPresentation().setIsConfirmationRequired(isSelected);
                }
            }
        };
        confirmCheck.addItemListener(confirmListener);

        ItemListener localListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!ContextualPropertiesPanel.this.isUpdate) {
                    boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
                    ContextualPropertiesPanel.this.command.getData().setIsLocal(isSelected);
                    ContextualPropertiesPanel.this.changeSupport.fireChange();
                }
            }
        };
        localCheck.addItemListener(localListener);

        readOnlyCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ContextualPropertiesPanel.this.isUpdate) {
                    command.getData().setReadOnlyCommand(readOnlyCheck.isSelected());
                }
            }
        });
        
                
        rereadAfterExecute.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ((AdsScopeCommandDef) command).getPresentation().setRereadAfterExecute(rereadAfterExecute.isSelected());
            }
        });
        
        traceGUIActivity.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                command.getPresentation().setTraceGuiActivity(traceGUIActivity.isSelected());
            }
        });
    }

    private void accessBoxItemChanged(Object selected) {
        AdsScopeCommandDef.CommandPresentation presentation = ((AdsScopeCommandDef) command).getPresentation();
        ECommandAccessibility oldAccessibility = presentation.getAccessebility();
        ECommandAccessibility newAccessibility = ECommandAccessibility.getForTitle((String) selected);
        if (scopeAccessBox.isEditable()) {
            ECommandAccessibility val = newAccessibility;
            if (val != null) {//model.contains(selected)){(AdsScopeCommandDef.CommandPresentation)((AdsScopeCommandDef)command).getPresentation()
                scopeAccessBox.setBorder(defaultBorder);
                scopeAccessBox.setEditable(false);
                presentation.setAccessebility(val);
            }
        } else {
            presentation.setAccessebility(newAccessibility);
        }
        boolean newForFix = newAccessibility == ECommandAccessibility.ONLY_FOR_FIXED;
        if (oldAccessibility != newAccessibility && (newForFix || oldAccessibility == ECommandAccessibility.ONLY_FOR_FIXED)){
            
            if (newForFix) {
                rereadAfterExecute.setSelected(true);
            } else {
                rereadAfterExecute.setSelected(false);
            }
            
            checkRereadBtnEnabled();
        }
    }
    
    private void checkRereadBtnEnabled() {
        ECommandAccessibility accessibility = ((AdsScopeCommandDef) command).getPresentation().getAccessebility();
        if (accessibility == ECommandAccessibility.ONLY_FOR_FIXED) {
            rereadAfterExecute.setEnabled(true);
        } else {
            rereadAfterExecute.setEnabled(false);
        }
        
        rereadAfterExecute.setSelected(((AdsScopeCommandDef) command).getPresentation().isRereadAfterExecute());
    }
    
    private Border defaultBorder;

    public void open(final AdsCommandDef command) {
        this.command = command;
        setupUI();
        update();
    }

    private void setupUI() {
        boolean isScope = command instanceof AdsScopeCommandDef;

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gbl);
        c.insets = new Insets(0, 0, 0, 10);
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.weighty = 0.0;

        if (isScope) {
            JLabel label = new JLabel(NbBundle.getMessage(ContextualPropertiesPanel.class, "ScopeProperties-Accessibility"));
            c.gridx = 0;
            gbl.setConstraints(label, c);
            content.add(label);
            c.gridx = 1;
            
            scopeAccessBox.setModel(new CommandAccessibilityComboModel(command instanceof AdsGroupCommandDef));
            gbl.setConstraints(scopeAccessBox, c);
            content.add(scopeAccessBox);
        }


        c.gridx++;//isScope ? 2 : 0;
        gbl.setConstraints(readOnlyCheck, c);
        content.add(readOnlyCheck);

        c.gridx++;//isScope ? 2 : 0;
        gbl.setConstraints(confirmCheck, c);
        content.add(confirmCheck);

        c.gridx++;//isScope ? 3 : 1;
        if (isScope) {
            c.insets = new Insets(0, 0, 0, 0);
            gbl.setConstraints(localCheck, c);
            content.add(localCheck);
            c.gridx ++;
        }

        if (isScope) {
            c.insets = new Insets(0, 10, 0, 0);
        }

        gbl.setConstraints(showBtnCheck, c);
        content.add(showBtnCheck);
        
        c.gridx++;
        gbl.setConstraints(traceGUIActivity, c);
        content.add(traceGUIActivity);
        
        
        if (isScope && ((AdsScopeCommandDef) command).getPresentation().canReread()) {
            c.insets = new Insets(0, 10, 0, 0);
            c.gridx++;
            gbl.setConstraints(rereadAfterExecute, c);
            content.add(rereadAfterExecute);
        }

    }
    private boolean isUpdate = false;

    public void update() {
        isUpdate = true;
        if (command != null) {
            confirmCheck.setSelected(command.getPresentation().getIsConfirmationRequired());
            localCheck.setSelected(command.getData().isLocal());
            readOnlyCheck.setSelected(command.getData().isReadOnlyCommand());
            traceGUIActivity.setSelected(command.getPresentation().isTraceGuiActivity());

            if (command instanceof AdsScopeCommandDef) {
                ECommandAccessibility accessibility = ((AdsScopeCommandDef.CommandPresentation) command.getPresentation()).getAccessebility();
                if (accessibility == null) {
                    defaultBorder = scopeAccessBox.getBorder();
                    scopeAccessBox.setBorder(BorderFactory.createLineBorder(Color.RED));
                    scopeAccessBox.setEditable(true);
                    scopeAccessBox.setSelectedItem("<Not defined>");
                } else {
                    scopeAccessBox.setSelectedItem(accessibility.getName());
                    scopeAccessBox.setEditable(false);
                }
                
                checkRereadBtnEnabled();
            }

            showBtnCheck.setSelected(command.getPresentation().getIsVisible());

        }
        isUpdate = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        content = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        content.setMinimumSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(content, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel content;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
}
