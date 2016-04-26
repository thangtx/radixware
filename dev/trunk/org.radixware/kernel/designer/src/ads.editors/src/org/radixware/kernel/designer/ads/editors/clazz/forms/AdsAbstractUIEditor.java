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
package org.radixware.kernel.designer.ads.editors.clazz.forms;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.netbeans.spi.palette.PaletteController;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.TopComponentGroup;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil.IVisitorUI;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtUIDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.AbsoluteLayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.GridLayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.HorizontalLayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.LayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.VerticalLayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.CustomPropertiesPanel;
import org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.JavaScriptPanel;
import org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.SignalsPanel;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Palette;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.editors.RadixObjectTopComponent;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class AdsAbstractUIEditor extends RadixObjectEditor<AdsAbstractUIDef> {

    private GraphSceneImpl scene = null;
    private JScrollPane scroll = null;

    /**
     * Creates new form AdsUIEditorView
     */
    public AdsAbstractUIEditor(final AdsAbstractUIDef uiDef) {
        super(uiDef);
        initComponents();
        if (uiDef instanceof AdsRwtUIDef) {
            toolBar.remove(btGLayout);
            toolBar.remove(btVLayout);
            toolBar.remove(btHLayout);
            toolBar.remove(btSignals);
            toolBar.remove(btParams);
            toolBar.remove(jSeparator1);
            toolBar.remove(btActions);
            btEditJScript.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JavaScriptPanel panel = new JavaScriptPanel();
                    panel.open(((AdsRwtUIDef) uiDef).getJsCode());
                    ModalDisplayer displayer = new ModalDisplayer(panel, "Edit JavaScript Code");
                    if (displayer.showModal()) {
                        ((AdsRwtUIDef) uiDef).setJsCode(panel.getCode());
                    }
                }
            });
        } else {
            toolBar.remove(btActions);
            toolBar.remove(btEditJScript);
        }

        final List<EIsoLanguage> languages = uiDef.getModule().getSegment().getLayer().getLanguages();
        comboLang.setModel(new javax.swing.DefaultComboBoxModel(languages.toArray()));
        comboLang.setSelectedItem(Settings.getLanguage());
    }

    private void closeWindowsGroup() {
        TopComponent activated = WindowManager.getDefault().getRegistry().getActivated();
        if (activated != null && activated instanceof RadixObjectTopComponent) {
            RadixObjectTopComponent tc = (RadixObjectTopComponent) activated;
            if (tc.getEditor() instanceof AdsAbstractUIEditor) {
                return;
            }
        }
        TopComponentGroup group = WindowManager.getDefault().findTopComponentGroup("uidesigner");
        if (group != null) {
            group.close();
        }
    }

    @Override
    public void onClosed() {
        closeWindowsGroup();
    }

    @Override
    public void onHidden() {
        closeWindowsGroup();
    }

    @Override
    public void onShown() {
        TopComponentGroup group = WindowManager.getDefault().findTopComponentGroup("uidesigner");
        if (group != null) {
            group.open();
        }
    }

    @Override
    public void onActivate() {
        updatePalette();
    }

    private void updatePalette() {
        if (lookup.lookup(PaletteController.class) != null) {
            Palette.updatePalette(getRadixObject());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolBar = new javax.swing.JToolBar();
        accessPanel = new org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel();
        deprecatedCheckBox = new javax.swing.JCheckBox();
        spCustom1 = new javax.swing.JToolBar.Separator();
        btHLayout = new javax.swing.JToggleButton();
        btVLayout = new javax.swing.JToggleButton();
        btGLayout = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btSnap = new javax.swing.JToggleButton();
        spCustom = new javax.swing.JToolBar.Separator();
        btSignals = new javax.swing.JButton();
        btActions = new javax.swing.JButton();
        btParams = new javax.swing.JButton();
        btEditJScript = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        javax.swing.Box.Filler filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lbLang = new javax.swing.JLabel();
        comboLang = new javax.swing.JComboBox();

        setLayout(new java.awt.BorderLayout());

        toolBar.setBorder(null);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.add(accessPanel);

        deprecatedCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsAbstractUIEditor.class, "AdsAbstractUIEditor.deprecatedCheckBox.text")); // NOI18N
        deprecatedCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                deprecatedCheckBoxItemStateChanged(evt);
            }
        });
        toolBar.add(deprecatedCheckBox);
        toolBar.add(spCustom1);

        btHLayout.setIcon(AdsDefinitionIcon.WIDGETS.HORIZONTAL_LAYOUT.getIcon(20));
        btHLayout.setToolTipText(org.openide.util.NbBundle.getMessage(AdsAbstractUIEditor.class, "HorizontalLayout")); // NOI18N
        btHLayout.setFocusable(false);
        btHLayout.setMaximumSize(new java.awt.Dimension(24, 24));
        btHLayout.setMinimumSize(new java.awt.Dimension(24, 24));
        btHLayout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btHLayoutActionPerformed(evt);
            }
        });
        toolBar.add(btHLayout);

        btVLayout.setIcon(AdsDefinitionIcon.WIDGETS.VERTICAL_LAYOUT.getIcon(20));
        btVLayout.setToolTipText(org.openide.util.NbBundle.getMessage(AdsAbstractUIEditor.class, "VerticalLayout")); // NOI18N
        btVLayout.setFocusable(false);
        btVLayout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btVLayout.setMaximumSize(new java.awt.Dimension(24, 24));
        btVLayout.setMinimumSize(new java.awt.Dimension(24, 24));
        btVLayout.setPreferredSize(new java.awt.Dimension(24, 24));
        btVLayout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btVLayout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btVLayoutActionPerformed(evt);
            }
        });
        toolBar.add(btVLayout);

        btGLayout.setIcon(AdsDefinitionIcon.WIDGETS.GRID_LAYOUT.getIcon(20));
        btGLayout.setToolTipText(org.openide.util.NbBundle.getMessage(AdsAbstractUIEditor.class, "GridLayout")); // NOI18N
        btGLayout.setFocusable(false);
        btGLayout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btGLayout.setMaximumSize(new java.awt.Dimension(24, 24));
        btGLayout.setMinimumSize(new java.awt.Dimension(24, 24));
        btGLayout.setPreferredSize(new java.awt.Dimension(24, 24));
        btGLayout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btGLayout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGLayoutActionPerformed(evt);
            }
        });
        toolBar.add(btGLayout);
        toolBar.add(jSeparator1);

        btSnap.setIcon(RadixWareDesignerIcon.WIDGETS.SNAP.getIcon(20));
        btSnap.setToolTipText(org.openide.util.NbBundle.getMessage(AdsAbstractUIEditor.class, "Snap")); // NOI18N
        btSnap.setFocusable(false);
        btSnap.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btSnap.setMaximumSize(new java.awt.Dimension(24, 24));
        btSnap.setMinimumSize(new java.awt.Dimension(24, 24));
        btSnap.setPreferredSize(new java.awt.Dimension(24, 24));
        btSnap.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btSnap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSnapActionPerformed(evt);
            }
        });
        toolBar.add(btSnap);
        toolBar.add(spCustom);

        btSignals.setIcon(RadixWareDesignerIcon.WIDGETS.SIGNALS.getIcon(20));
        btSignals.setToolTipText(org.openide.util.NbBundle.getMessage(AdsAbstractUIEditor.class, "Signals")); // NOI18N
        btSignals.setFocusable(false);
        btSignals.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btSignals.setMaximumSize(new java.awt.Dimension(24, 24));
        btSignals.setMinimumSize(new java.awt.Dimension(24, 24));
        btSignals.setPreferredSize(new java.awt.Dimension(24, 24));
        btSignals.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btSignals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSignalsActionPerformed(evt);
            }
        });
        toolBar.add(btSignals);

        btActions.setText(org.openide.util.NbBundle.getMessage(AdsAbstractUIEditor.class, "AdsAbstractUIEditor.btActions.text")); // NOI18N
        btActions.setFocusable(false);
        btActions.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btActions.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btActions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btActionsActionPerformed(evt);
            }
        });
        toolBar.add(btActions);

        btParams.setIcon(RadixWareDesignerIcon.WIDGETS.PARAMS.getIcon(20));
        btParams.setToolTipText(org.openide.util.NbBundle.getMessage(AdsAbstractUIEditor.class, "Params")); // NOI18N
        btParams.setFocusable(false);
        btParams.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btParams.setMaximumSize(new java.awt.Dimension(24, 24));
        btParams.setMinimumSize(new java.awt.Dimension(24, 24));
        btParams.setPreferredSize(new java.awt.Dimension(24, 24));
        btParams.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btParams.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btParamsActionPerformed(evt);
            }
        });
        toolBar.add(btParams);

        btEditJScript.setText(org.openide.util.NbBundle.getMessage(AdsAbstractUIEditor.class, "AdsAbstractUIEditor.btEditJScript.text")); // NOI18N
        btEditJScript.setFocusable(false);
        btEditJScript.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btEditJScript.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(btEditJScript);
        toolBar.add(jSeparator4);
        toolBar.add(filler1);

        lbLang.setText(org.openide.util.NbBundle.getMessage(AdsAbstractUIEditor.class, "Language")); // NOI18N
        lbLang.setFocusable(false);
        toolBar.add(lbLang);

        comboLang.setFocusable(false);
        comboLang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboLangActionPerformed(evt);
            }
        });
        toolBar.add(comboLang);

        add(toolBar, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btHLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btHLayoutActionPerformed
        // TODO add your handling code here:
        layoutActionPerformed(evt);
    }//GEN-LAST:event_btHLayoutActionPerformed

    private void btVLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btVLayoutActionPerformed
        // TODO add your handling code here:
        layoutActionPerformed(evt);
    }//GEN-LAST:event_btVLayoutActionPerformed

    private void btGLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGLayoutActionPerformed
        // TODO add your handling code here:
        layoutActionPerformed(evt);
    }//GEN-LAST:event_btGLayoutActionPerformed

    private void btSnapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSnapActionPerformed
        // TODO add your handling code here:
        scene.setSnaped(btSnap.isSelected());
    }//GEN-LAST:event_btSnapActionPerformed

    private void comboLangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboLangActionPerformed
        // TODO add your handling code here:
        if (scene == null) {
            return;
        }
        Settings.setLanguage((EIsoLanguage) comboLang.getSelectedItem());
        scene.updateSelection();
        scene.revalidate();
        scene.validate();
    }//GEN-LAST:event_comboLangActionPerformed

    private void btSignalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSignalsActionPerformed
        // TODO add your handling code here:
        AdsAbstractUIDef ui = scene.getUI();

        SignalsPanel panel = new SignalsPanel(ui);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        StateAbstractDialog md = new StateAbstractDialog(panel, NbBundle.getMessage(getClass(), "Signals")) {
            @Override
            protected void apply() {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        if ((md.showModal()) && (!ui.isReadOnly())) {

            List<AdsUISignalDef> signals = panel.getSignals();
            AdsDefinitions<AdsUISignalDef> defSignals = null;
            if (ui instanceof AdsCustomWidgetDef) {
                defSignals = ((AdsCustomWidgetDef) ui).getSignals();
            } else if (ui instanceof AdsRwtCustomDialogDef) {
                defSignals = ((AdsRwtCustomDialogDef) ui).getCloseSignals();
            }
            if (defSignals != null) {
                defSignals.clear();
                for (int i = 0, size = signals.size(); i < size; i++) {
                    defSignals.add(signals.get(i));
                }
            }
        }
    }//GEN-LAST:event_btSignalsActionPerformed

    private void btParamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btParamsActionPerformed
        // TODO add your handling code here:
        AdsCustomWidgetDef customDef = (AdsCustomWidgetDef) scene.getUI();
        CustomPropertiesPanel panel = new CustomPropertiesPanel(scene.getUI(), customDef);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        StateAbstractDialog md = new StateAbstractDialog(panel, NbBundle.getMessage(getClass(), "Custom_Properties")) {
            @Override
            protected void apply() {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        if ((md.showModal()) && (!customDef.isReadOnly())) {
            customDef.getProperties().clear();
            List<AdsUIProperty> custProps = panel.getCustomProperties();
            for (int i = 0, size = custProps.size(); i < size; i++) {
                customDef.getProperties().add(custProps.get(i));
            }
        }
    }//GEN-LAST:event_btParamsActionPerformed

private void deprecatedCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_deprecatedCheckBoxItemStateChanged
    // TODO add your handling code here:
    final AdsAbstractUIDef uiDef = getUi();
    final boolean selected = (evt.getStateChange() == ItemEvent.SELECTED);
    uiDef.setDeprecated(selected);
}//GEN-LAST:event_deprecatedCheckBoxItemStateChanged

    private void btActionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btActionsActionPerformed
        Set selection = scene.getSelectedObjects();
        if (selection.size() == 1) {
            List sl = new ArrayList(selection);
            if (sl.get(0) instanceof AdsRwtWidgetDef && AdsUIUtil.isRwtActionHolder((AdsRwtWidgetDef) sl.get(0))) {
                RwtWidgetActionsList panel = new RwtWidgetActionsList((AdsRwtWidgetDef) sl.get(0));
                ModalDisplayer displayer = new ModalDisplayer(panel);
                if (displayer.showModal()) {
                    panel.apply();
                }
            } else {
                DialogUtils.messageError("Widget expected to be selected");
            }
        } else {
            DialogUtils.messageError("One object should be selected");
        }
    }//GEN-LAST:event_btActionsActionPerformed

    private void layoutActionPerformed(ActionEvent e) {
        synchronized (LayoutProcessor.LOCK) {

            JToggleButton bt = (JToggleButton) e.getSource();
            BaseWidget wg = scene.getContainerSelected();
            if (wg == null) {
                return;
            }

            wg.stopContainerListening();

            if (!bt.equals(btHLayout)) {
                btHLayout.setSelected(false);
            } else {
                wg.setLayoutProcessor(bt.isSelected() ? new HorizontalLayoutProcessor(wg) : new AbsoluteLayoutProcessor(wg));
            }

            if (!bt.equals(btVLayout)) {
                btVLayout.setSelected(false);
            } else {
                wg.setLayoutProcessor(bt.isSelected() ? new VerticalLayoutProcessor(wg) : new AbsoluteLayoutProcessor(wg));
            }

            if (!bt.equals(btGLayout)) {
                btGLayout.setSelected(false);
            } else {
                wg.setLayoutProcessor(bt.isSelected() ? new GridLayoutProcessor(wg) : new AbsoluteLayoutProcessor(wg));
            }

            wg.startContainerListening();

            getUi().setEditState(RadixObject.EEditState.MODIFIED);
            // for propSheet reread
            RadixObject object = (RadixObject) scene.findObject(wg);
            scene.setSelectedObjects(Collections.singleton(object));
            scene.validate();
        }
    }

    public AdsAbstractUIDef getUi() {
        return getRadixObject();
    }
    private InstanceContent lookupContent = new InstanceContent();
    private Lookup lookup = new AbstractLookup(lookupContent);

    @Override
    public Lookup getLookup() {
        RadixObject obj = getRadixObject();
        PaletteController palette = lookup.lookup(PaletteController.class);
        if (obj.isReadOnly()) {
            if (palette != null) {
                lookupContent.remove(palette);
            }
        } else {
            if (palette == null) {
                palette = Palette.createPalette(obj);
                if (palette != null) {
                    lookupContent.add(palette);
                }
            }
            // move to onActivate
            //Palette.updatePalette(obj);
        }
        //setSheet(createSheet());
        return lookup;
    }

    @Override
    public boolean open(OpenInfo info) {
        if (scene != null) { // remove old listeners
            final AdsAbstractUIDef uiDef = scene.getUI();
            if (uiDef instanceof AdsUIDef) {
                final AdsWidgetDef widget = ((AdsUIDef) uiDef).getWidget();
                AdsUIUtil.visitUI(widget, new IVisitorUI() {
                    @Override
                    public void visit(RadixObject node, boolean active) {
                        final BaseWidget wg = (BaseWidget) scene.findWidget(node);
                        if (wg != null) {
                            AdsUIUtil.removePropertyChangeListener(node, WeakListeners.propertyChange(wg, node));
                            AdsUIUtil.removeContainerListener(node, wg);
                        }
                    }
                }, true);
            }
        }

        final AdsAbstractUIDef uiDef = getUi();
        if (uiDef instanceof AdsUIDef) {
            spCustom.setVisible(uiDef instanceof AdsCustomWidgetDef);
            btSignals.setVisible(uiDef instanceof AdsCustomWidgetDef);
            btParams.setVisible(uiDef instanceof AdsCustomWidgetDef);
        }

        boolean opened = super.open(info);
        if (opened || true) {
            if (DrawUtil.DEFAULT_FONT_METRICS == null) {
                Graphics2D gr = (Graphics2D) getGraphics();
                DrawUtil.DEFAULT_FONT_METRICS = (gr != null ? gr.getFontMetrics() : null);
                assert DrawUtil.DEFAULT_FONT_METRICS != null;
            }
        }

        if (scroll != null) {
            remove(scroll);
        }

        scene = new GraphSceneImpl(this);
        add(scroll = new JScrollPane(scene.createView()), BorderLayout.CENTER);

        scene.setUI(uiDef);

        final RadixObject target = info.getTarget();

        accessPanel.open(uiDef);
        deprecatedCheckBox.setSelected(uiDef.isDeprecated());

        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (target != null && !(target instanceof AdsAbstractUIDef)) {
                    scene.focus(target);
                } else {
                    scene.setSelectedObjects(Collections.singleton(uiDef.getWidget()));
                }
                updateToolBar();
            }
        });
        return opened;
    }

    @Override
    public void notifySelectionChanged(final List<? extends RadixObject> newSelectedObjects) {
        for (RadixObject obj : newSelectedObjects) {
            if (obj instanceof AdsWidgetDef) {
                ((AdsWidgetDef) obj).fireNodeUpdate();
            }

            if (obj instanceof AdsRwtWidgetDef) {
                ((AdsRwtWidgetDef) obj).fireNodeUpdate();
            }

            if (obj instanceof AdsLayout) {
                ((AdsLayout) obj).fireNodeUpdate();
            }
            if (obj instanceof AdsLayout.SpacerItem) {
                ((AdsLayout.SpacerItem) obj).fireNodeUpdate();
            }
        }
        super.notifySelectionChanged(newSelectedObjects);
    }

    public void updateToolBar() {
        assert scene != null;
        AdsAbstractUIDef uiDef = scene.getUI();
        BaseWidget widget = scene.getContainerSelected();
        if (widget != null && widget.canChangeLayout() && !uiDef.isReadOnly()) {
            LayoutProcessor lp = widget.getLayoutProcessor();
            btHLayout.setEnabled(true);
            btHLayout.setSelected(lp != null && lp instanceof HorizontalLayoutProcessor);
            btVLayout.setEnabled(true);
            btVLayout.setSelected(lp != null && lp instanceof VerticalLayoutProcessor);
            btGLayout.setEnabled(true);
            btGLayout.setSelected(lp != null && lp instanceof GridLayoutProcessor);
        } else {
            btHLayout.setEnabled(false);
            btHLayout.setSelected(false);
            btVLayout.setEnabled(false);
            btVLayout.setSelected(false);
            btGLayout.setEnabled(false);
            btGLayout.setSelected(false);
        }
        btSnap.setEnabled(!uiDef.isReadOnly());
        btSnap.setSelected(scene.isSnaped());
        lbLang.setEnabled(!uiDef.isReadOnly());
        comboLang.setEnabled(!uiDef.isReadOnly());
        deprecatedCheckBox.setEnabled(!uiDef.isReadOnly());
    }

    @Override
    public boolean isShowProperties() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update() {
        updatePalette();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getUi().fireNodeUpdate();
                scene.updateSelection(); // palette and sheet refresh
            }
        });
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsAbstractUIDef> {

        @Override
        public RadixObjectEditor<AdsAbstractUIDef> newInstance(AdsAbstractUIDef uiDef) {
            return new AdsAbstractUIEditor(uiDef);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel accessPanel;
    private javax.swing.JButton btActions;
    private javax.swing.JButton btEditJScript;
    private javax.swing.JToggleButton btGLayout;
    private javax.swing.JToggleButton btHLayout;
    private javax.swing.JButton btParams;
    private javax.swing.JButton btSignals;
    private javax.swing.JToggleButton btSnap;
    private javax.swing.JToggleButton btVLayout;
    private javax.swing.JComboBox comboLang;
    private javax.swing.JCheckBox deprecatedCheckBox;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JLabel lbLang;
    private javax.swing.JToolBar.Separator spCustom;
    private javax.swing.JToolBar.Separator spCustom1;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
}
