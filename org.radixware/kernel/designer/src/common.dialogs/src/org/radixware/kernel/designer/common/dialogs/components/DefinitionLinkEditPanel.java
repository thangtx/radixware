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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence.ChooseDefinitionCfgs;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class DefinitionLinkEditPanel extends javax.swing.JPanel {

    private ChooseDefinitionCfg cfg = null;
    private ChooseDefinitionCfgs cfgIterator = null;
    private List<Definition> definitionSequence = null;
    private Definition definition = null;
    private Id definitionId = null;
    private javax.swing.JButton chooseBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton btEditObject;
    private javax.swing.JButton btGoToObject;
    private ChangeSupport changeSupport = new ChangeSupport(this);
    private boolean clearable = true;

    public DefinitionLinkEditPanel() {
        initComponents();

        chooseBtn = exTextField.addButton();
        chooseBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));

        chooseBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseBtnActionPerformed(evt);
            }
        });

        clearBtn = exTextField.addButton();
        clearBtn.setEnabled(false);
        clearBtn.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon(13, 13));

        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        btEditObject = exTextField.addButton();
        btEditObject.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorsManager.getDefault().open(definition);
            }
        });
        btEditObject.setIcon(RadixWareIcons.EDIT.EDIT.getIcon(13, 13));
        btEditObject.setToolTipText(NbBundle.getMessage(DefinitionLinkEditPanel.class, "DefinitionLinkEditPanel-OpenTip"));

        btGoToObject = exTextField.addButton();
        btGoToObject.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NodesManager.selectInProjects(definition);
            }
        });
        btGoToObject.setIcon(RadixWareIcons.TREE.SELECT_IN_TREE.getIcon(13, 13));
        btGoToObject.setToolTipText(NbBundle.getMessage(DefinitionLinkEditPanel.class, "DefinitionLinkEditPanel-SelectTip"));

        exTextField.setPriorButton(chooseBtn);
    }

    @Override
    public void setEnabled(boolean enabled) {
        exTextField.setEnabled(enabled);
        updateState();
    }

    @Override
    public boolean isEnabled() {
        return exTextField.isEnabled();
    }

    @Override
    public int getBaseline(int width, int height) {
        return exTextField.getBaseline(width, height);
    }

    @Override
    public boolean requestFocusInWindow() {
        return exTextField.requestFocusInWindow();
    }

    @Override
    public Dimension getPreferredSize() {
        return exTextField.getPreferredSize();
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (preferredSize != null) {
            exTextField.setPreferredSize(preferredSize);
        }
        super.setPreferredSize(preferredSize);
    }

    @Override
    public Dimension getMinimumSize() {
        return exTextField.getMinimumSize();
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        if (minimumSize != null) {
            exTextField.setMinimumSize(minimumSize);
        }
        super.setMinimumSize(minimumSize);
    }

    @Override
    public Dimension getMaximumSize() {
        return exTextField.getMaximumSize();
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        if (maximumSize != null) {
            exTextField.setMaximumSize(maximumSize);
        }
        super.setMaximumSize(maximumSize);
    }

    /**
     * Set posibility to clear definition identifier.
     */
    public void setClearable(boolean clearable) {
        this.clearable = clearable;
        updateState();
    }

    /**
     * @return true if it is possible to clear definition identifier, false
     * otherwise, default true.
     */
    public boolean isClearable() {
        return this.clearable;
    }

    /**
     * Open editor
     *
     * @param context context for choose object, required because definition can
     * not be defined.
     * @param provider - provider for choose object
     * @param definition - definition to display in edit line.
     * @param definitionId - definition identifier, required if definition is
     * not found but link to it is defined.
     */
    public void open(ChooseDefinitionCfg cfg, Definition definition, Id definitionId) {
        this.cfg = cfg;
        this.definition = definition;
        this.definitionId = definitionId;

        update();
    }

    /**
     * Open editor
     *
     * @param context context for choose object, required because definition can
     * not be defined.
     * @param provider - provider for choose object
     * @param definition - definition to display in edit line.
     * @param definitionId - definition identifier, required if definition is
     * not found but link to it is defined.
     */
    public void open(ChooseDefinitionCfgs cfgIterator, Definition definition, Id definitionId) {
        this.cfgIterator = cfgIterator;
        this.definition = definition;
        this.definitionId = definitionId;

        update();
    }

    /**
     * Open editor in view only mode
     *
     * @param definition - definition to display in edit line.
     * @param definitionId - definition identifier, required if definition is
     * not found but link to it is defined.
     */
    public void open(Definition definition, Id definitionId) {
        this.definition = definition;
        this.definitionId = definitionId;

        update();
    }
    private RadixObject context;

    public boolean setComboMode(RadixObject context) {
        if (context != null) {
            this.context = context;
            return setComboMode();
        }
        return false;
    }

    public boolean setComboMode() {
        if (exTextField.getEditorType() != ExtendableTextField.EDITOR_COMBO) {
            javax.swing.JComboBox box = new javax.swing.JComboBox() {
                @Override
                public void setSelectedItem(Object anObject) {
                    if (anObject instanceof Id) {
                        if (((DefaultComboBoxModel) getModel()).getIndexOf(anObject) == -1) {
                            ((DefaultComboBoxModel) getModel()).addElement(anObject);
                        }
                    }
                    super.setSelectedItem(anObject);
                }
            };
            setupCombo(box);
            exTextField.setEditor(box);
            update();
            return true;
        }
        return false;
    }

    public boolean setComboBoxRenderer(ListCellRenderer renderer) {
        return exTextField.setComboBoxRenderer(renderer);
    }

    public boolean setComboBoxValues(Collection<? extends Definition> objects, boolean addNotDefinedItem) {
        if (exTextField.getEditorType() == ExtendableTextField.EDITOR_COMBO) {
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            for (Definition d : objects) {
                if (d != null) {
                    model.addElement(d);
                }
            }
            if (addNotDefinedItem) {
                model.insertElementAt(null, 0);
            }
            exTextField.setComboBoxModel(model);
        }
        return false;
    }

    private void setupCombo(final javax.swing.JComboBox combo) {
        PopupMenuListener popupListener = new PopupMenuListener() {
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (cfg != null && context != null && cfg.getProvider() != null) {
                    Collection<Definition> defs = DefinitionsUtils.collectTopAround(context, cfg.getProvider());
                    DefaultComboBoxModel model = new DefaultComboBoxModel(defs.toArray());
                    Object selected = model.getSelectedItem();
                    if (!(selected instanceof Id)) {
                        if (model.getElementAt(model.getSize() - 1) instanceof Id) {
                            model.removeElementAt(model.getSize() - 1);
                        }
                    }

                    combo.setModel(model);
                    combo.setSelectedItem(definition);
                } else {
                    DefaultComboBoxModel model = (DefaultComboBoxModel) combo.getModel();
                    Object selected = model.getSelectedItem();
                    if (!(selected instanceof Id)) {
                        if (model.getElementAt(model.getSize() - 1) instanceof Id) {
                            model.removeElementAt(model.getSize() - 1);
                            combo.setModel(model);
                            combo.setSelectedItem(definition);
                        }
                    }
                }
            }
        };

        combo.addPopupMenuListener(popupListener);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (updateLock) {
                    if (isUpdating) {
                        return;
                    }
                    Object selected = combo.getSelectedItem();
                    if (selected instanceof Definition) {
                        definition = (Definition) selected;
                        definitionId = ((Definition) selected).getId();
                    } else if (selected == null) {
                        definition = null;
                        definitionId = null;
                    }

                    updateState();
                }
                changeSupport.fireChange();
            }
        };

        combo.addActionListener(actionListener);
        combo.setRenderer(new DefinitionLinkEditRenderer());
        popupListener.popupMenuWillBecomeVisible(new PopupMenuEvent(combo));
    }
    private boolean isUpdating = false;
    private final Object updateLock = new Object();

    public void update() {
        synchronized (updateLock) {
            try {
                isUpdating = true;
                if (definitionId != null && definition == null) {
                    exTextField.setForeground(Color.RED);
                } else {
                    exTextField.setForeground(Color.BLACK);
                }
                exTextField.repaint();

                if (definition != null) {
                    exTextField.setToolTipText(definition.getToolTip());
                    exTextField.setValue(exTextField.getEditorType() == ExtendableTextField.EDITOR_TEXTFIELD ? definition.getQualifiedName() : definition);
                } else if (definitionId != null) {
                    if (exTextField.getEditorType() == ExtendableTextField.EDITOR_TEXTFIELD) {
                        exTextField.setValue(String.valueOf(definitionId));
                    } else {
                        exTextField.setValue(definitionId);
                    }
                } else {
                    if (exTextField.getEditorType() == ExtendableTextField.EDITOR_COMBO) {
                        exTextField.setValue(null);
                    } else {
                        exTextField.setValue("<Not defined>");
                    }
                }

                updateState();
            } finally {
                isUpdating = false;
            }
        }
    }

    private boolean isInDialog() {
        for (Component component = this.getParent(); component != null; component = component.getParent()) {
            if (component instanceof Dialog) {
                return true;
            }
        }
        return false;
    }

    private void updateState() {
        final boolean enabled = isEnabled();

        boolean modifiyButtonVisible = (cfg != null && (cfg.getContext() != null && cfg.getProvider() != null || cfg.collectAllowedDefinitions() != null)) || cfgIterator != null;

        clearBtn.setEnabled(enabled && definitionId != null && clearable);
        clearBtn.setVisible(clearable && modifiyButtonVisible);
        btEditObject.setEnabled(definition != null/* && enabled*/);
        btGoToObject.setEnabled(definition != null/* && enabled*/);


        chooseBtn.setEnabled(enabled && modifiyButtonVisible);
        chooseBtn.setVisible(modifiyButtonVisible && exTextField.getEditorType() == ExtendableTextField.EDITOR_TEXTFIELD);
        final boolean isInDialog = isInDialog();
        btEditObject.setVisible(!isInDialog);
        btGoToObject.setVisible(!isInDialog);
    }
    private boolean initialized = false;

    @Override
    public void paint(Graphics g) {
        if (!initialized) {
            initialized = true;
            updateState();
        }
        super.paint(g);
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public Id getDefinitionId() {
        return this.definitionId;
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        if (mgr == null) {
            return;
        }
        super.setLayout(mgr); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        exTextField = new org.radixware.kernel.common.components.ExtendableTextField();

        exTextField.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.components.ExtendableTextField exTextField;
    // End of variables declaration//GEN-END:variables

    private void chooseBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (cfgIterator != null) {
            definitionSequence = ChooseDefinitionSequence.chooseDefinitionSequence(cfgIterator);
            if (!definitionSequence.isEmpty()) {
                this.definition = definitionSequence.get(definitionSequence.size() - 1);
                this.definitionId = this.definition.getId();
                onChange();
                update();
                changeSupport.fireChange();
            }
        } else {
            Definition newDefinition = ChooseDefinition.chooseDefinition(cfg);
            if (newDefinition != null) {
                this.definition = newDefinition;
                this.definitionId = newDefinition.getId();
                onChange();
                update();
                changeSupport.fireChange();
            }
        }
    }

    protected void onChange() {
    }

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {
        this.definition = null;
        this.definitionId = null;
        onClear();
        update();
        changeSupport.fireChange();
    }

    protected void onClear() {
    }

    public Definition getDefinition() {
        return definition;
    }

    public List<Definition> getLastSelectedDefinitionSequence() {
        if (cfgIterator == null) {
            throw new IllegalStateException();
        } else {
            return definitionSequence;
        }
    }
}
