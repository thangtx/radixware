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
package org.radixware.kernel.designer.common.editors.module;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.TabManager;

final class GeneralTab extends TabManager.TabAdapter {

    private static class ChooseCompanionCfg extends ChooseDefinitionCfg {

        public ChooseCompanionCfg(final AdsModule context) {
            super(context.getSegment().getModules().list(new IFilter() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject != context;
                }
            }));
        }
    }
    // members
    private final JTextArea descriptionEditor;
    private final JCheckBox isTestEditor;
    private final JCheckBox chbIsDeprecated;
    private final JCheckBox chbNeedsDoc;
    private final JCheckBox chbUnderConstruction;
    private DefinitionLinkEditPanel companionEditor;
    private DefinitionLinkEditPanel overwriteView;
    private final JPanel panel = new JPanel();
    private final Module module;
    private final DependenciesPanel dependenciesPanel;
    private final ChangeListener companionListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            Definition definiton = companionEditor.getDefinition();
            AdsModule ads = (AdsModule) getModule();
            if (definiton instanceof AdsModule) {
                ads.setCompanionModuleId(definiton.getId());
                if (!ads.getDependences().contains((AdsModule) definiton)) {
                    ads.getDependences().add((AdsModule) definiton);
                    dependenciesPanel.update();
                }
            } else {
                ads.setCompanionModuleId(null);
            }
        }
    };
    private final DocumentListener descriptionListener = new DocumentListener() {
        @Override
        public void changedUpdate(DocumentEvent e) {
            final Module module = getModule();
            module.setDescription(descriptionEditor.getText());
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            changedUpdate(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            changedUpdate(e);
        }
    };
    private final ItemListener isTestListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            final Module module = getModule();
            module.setIsTest(isTestEditor.isSelected());
        }
    };
    private final ItemListener isUnderConstructionListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            final Module module = getModule();
            if (module instanceof AdsModule) {
                ((AdsModule) module).setUnderConstruction(chbUnderConstruction.isSelected());
            }
        }
    };
    private final ItemListener isDeprecatedListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            final Module module = getModule();
            module.setDeprecated(chbIsDeprecated.isSelected());
        }
    };
    private final ItemListener needsDocListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            final Module module = getModule();
            module.setNeedsDoc(chbNeedsDoc.isSelected());
        }
    };

    public GeneralTab(Module module) {
        this.module = module;
        //
        panel.setLayout(new MigLayout("fill, nogrid"));
        panel.add(new JLabel("Description:"), "height min, wrap");
        descriptionEditor = new JTextArea();
        if (BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null || (module instanceof UdsModule)) {
            panel.add(new JScrollPane(descriptionEditor), "width max, height max, wrap");
        } else {
            panel.add(new JScrollPane(descriptionEditor), "width max, height 20%, wrap");
        }
        isTestEditor = new JCheckBox("Test module");

        chbIsDeprecated = new JCheckBox("Deprecated");
        chbNeedsDoc = new JCheckBox("Should be documented");
        if (module instanceof AdsModule) {
            chbUnderConstruction = new JCheckBox("Under construction");
            if (BuildOptions.UserModeHandlerLookup.getUserModeHandler() == null && !(module instanceof UdsModule)) {
                panel.add(isTestEditor, "height min");
                panel.add(chbIsDeprecated, "height min");
                panel.add(chbNeedsDoc, "height min");
                panel.add(chbUnderConstruction, "height min,wrap");
            }
        } else {
            if (BuildOptions.UserModeHandlerLookup.getUserModeHandler() == null) {
                panel.add(isTestEditor, "height min");
                panel.add(chbNeedsDoc, "height min");
                panel.add(chbIsDeprecated, "height min,wrap");
            }
            chbUnderConstruction = null;
        }

        JPanel referencePanel = new JPanel();
        panel.add(referencePanel, "height min,wrap");
        referencePanel.setLayout(new MigLayout("fill"));
        if (module instanceof AdsModule && !((AdsModule) module).isUserModule() && module.findOverwritten() == null && !(module instanceof UdsModule)) {
            referencePanel.add(new JLabel("This module is companion of"), "height min");
            companionEditor = new DefinitionLinkEditPanel();
            referencePanel.add(companionEditor, "height min, width max, wrap");
        }
        if (module.findOverwritten() != null) {
            referencePanel.add(new JLabel("This module overwrites"), "height min");
            overwriteView = new DefinitionLinkEditPanel();
            referencePanel.add(overwriteView, "height min, width max, wrap");
        }
        dependenciesPanel = new DependenciesPanel(module);
        if (BuildOptions.UserModeHandlerLookup.getUserModeHandler() == null && !(module instanceof UdsModule)) {
            panel.add(dependenciesPanel, "width max, height 80%");
        } else {
            panel.add(new JPanel(), "width max, height 80%");
        }

    }

    @Override
    public void initTab() {
        super.initTab();
        updateTab();
    }

    @Override
    public void updateTab() {
        updateDescriptionEditor();
        dependenciesPanel.update();
        updateIsTestEditor();
        updateIsUnderConstruction();
        updateIsDeprecated();
        updateNeedsDoc();
        updateReferences();
        updateState();
    }

    @Override
    public void setReadonlyTab(boolean readonly) {
    }

    @Override
    public String getTabName() {
        return "General";
    }

    @Override
    public JComponent getTabComponent() {
        return panel;
    }

    private void updateDescriptionEditor() {
        final String description = getModule().getDescription();
        descriptionEditor.getDocument().removeDocumentListener(descriptionListener);
        descriptionEditor.setText(description);
        descriptionEditor.getDocument().addDocumentListener(descriptionListener);
    }

    private void updateIsTestEditor() {
        isTestEditor.removeItemListener(isTestListener);
        isTestEditor.setSelected(getModule().isTest());
        isTestEditor.addItemListener(isTestListener);
    }

    private void updateIsUnderConstruction() {
        if (chbUnderConstruction != null) {
            final AdsModule adsModule = (AdsModule) getModule();
            chbUnderConstruction.removeItemListener(isUnderConstructionListener);
            chbUnderConstruction.setSelected(adsModule.isUnderConstruction());
            chbUnderConstruction.addItemListener(isUnderConstructionListener);
        }
    }

    private void updateIsDeprecated() {
        if (chbIsDeprecated != null) {
            final Module mdl = (Module) getModule();
            chbIsDeprecated.removeItemListener(isDeprecatedListener);
            chbIsDeprecated.setSelected(mdl.isDeprecated());
            chbIsDeprecated.addItemListener(isDeprecatedListener);
        }
    }

    private void updateNeedsDoc() {
        if (chbNeedsDoc != null) {
            final Module mdl = (Module) getModule();
            chbNeedsDoc.removeItemListener(needsDocListener);
            chbNeedsDoc.setSelected(mdl.isNeedsDoc());
            chbNeedsDoc.addItemListener(needsDocListener);
        }
    }

    private void updateReferences() {
        if (getModule() instanceof AdsModule && companionEditor != null) {
            AdsModule ads = (AdsModule) getModule();
            companionEditor.removeChangeListener(companionListener);
            companionEditor.open(new GeneralTab.ChooseCompanionCfg(ads), ads.findCompanionModule(), ads.getCompanionModuleId());
            companionEditor.addChangeListener(companionListener);
        }
        Module ovr = getModule().findOverwritten();
        if (ovr != null && overwriteView != null) {
            overwriteView.open(ovr, ovr.getId());
        }
    }

    private void updateState() {
        boolean enabled = !getModule().isReadOnly();
        descriptionEditor.setEditable(enabled);
        dependenciesPanel.updateState();
        isTestEditor.setEnabled(enabled);
        if (companionEditor != null) {
            companionEditor.setEnabled(enabled);
        }

        if (chbUnderConstruction != null) {
            chbUnderConstruction.setEnabled(enabled);
        }

        if (chbIsDeprecated != null) {
            chbIsDeprecated.setEnabled(enabled);
        }
    }

    public Module getModule() {
        return module;
    }
}
