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

package org.radixware.kernel.designer.common.dialogs.usages;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Collections;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IOverridable;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class FindUsagesCfgPanel extends JPanel { // changed to public by vmokhov in order to use in CheckResultsTree

    private static final class UsagesPanel extends JPanel {
        
        final JRadioButton findUsages = new JRadioButton("Find usages", true);
        private JRadioButton optAny;
        private JRadioButton optSet;
        private JRadioButton optGet;
        
        public UsagesPanel(boolean property) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            
            add(findUsages);
            
            if (property) {
                add(Box.createHorizontalStrut(8));
                final ButtonGroup group = new ButtonGroup();
                optAny = new JRadioButton("Any");
                optSet = new JRadioButton("Set");
                optGet = new JRadioButton("Get");
                
                add(optAny);
                add(optGet);
                add(optSet);
                
                group.add(optAny);
                group.add(optGet);
                group.add(optSet);
                
                optAny.setSelected(true);
                
                findUsages.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        final boolean selected = findUsages.isSelected();
                        optAny.setEnabled(selected);
                        optSet.setEnabled(selected);
                        optGet.setEnabled(selected);
                    }
                });
            }
        }
        
        FindUsagesCfg.ESearchType getSearchType() {
            if (optAny != null) {
                if (optAny.isSelected()) {
                    return FindUsagesCfg.ESearchType.FIND_USAGES;
                }
                
                return optGet.isSelected() ? FindUsagesCfg.ESearchType.FIND_USAGES_GET : FindUsagesCfg.ESearchType.FIND_USAGES_SET;
            }
            return FindUsagesCfg.ESearchType.FIND_USAGES;
        }
    }
    
    private static String getLabel(final Definition definition) {
        String result = "<html>" + definition.getTypeTitle() + " <b>" + definition.getName() + "</b>";
        final RadixObject owner = definition.getOwnerForQualifedName();
        if (owner != null) {
            result += " of " + owner.getTypeTitle() + " <b>" + owner.getName() + "</b>";
        }
        return result;
    }
    private final JRadioButton findUsed;
    private final JRadioButton findAllDescendants;
    private final JRadioButton findDirectDescendantsOnly;
    private final JRadioButton findReplacers;
    private final JRadioButton findReplaced;
    private final JRadioButton findSubPresentations;
    private final JRadioButton findAllSubPresentations;
    private final JCheckBox searchInCurrentModuleOnly;
    private final UsagesPanel usagesPanel;
    
    private FindUsagesCfgPanel(final Definition definition) {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        final String labelText = getLabel(definition);
        final JLabel label = new JLabel(labelText);
        add(label, BorderLayout.NORTH);
        
        final ButtonGroup group = new ButtonGroup();
        final JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        
        usagesPanel = new UsagesPanel(definition instanceof AdsPropertyDef);
        usagesPanel.findUsages.setMnemonic(KeyEvent.VK_U);
        
        radioPanel.add(usagesPanel);
        group.add(usagesPanel.findUsages);
        usagesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        findUsed = new JRadioButton("Find used", false);
        findUsed.setMnemonic(KeyEvent.VK_S);
        radioPanel.add(findUsed);
        group.add(findUsed);
        
        if (definition instanceof AdsPresentationDef) {
            findAllSubPresentations = new JRadioButton("Find all subpresentations", false);
            findAllSubPresentations.setMnemonic(KeyEvent.VK_R);
            radioPanel.add(findAllSubPresentations);
            group.add(findAllSubPresentations);
            
            findSubPresentations = new JRadioButton("Find direct subpresentations only", false);
            findSubPresentations.setMnemonic(KeyEvent.VK_R);
            radioPanel.add(findSubPresentations);
            group.add(findSubPresentations);
            
            
            if (definition instanceof AdsEditorPresentationDef) {
                findReplacers = new JRadioButton("Find replacing presentations", false);
                findReplacers.setMnemonic(KeyEvent.VK_R);
                radioPanel.add(findReplacers);
                group.add(findReplacers);
            } else {
                findReplacers = null;
            }
            
            if (definition instanceof AdsEditorPresentationDef) {
                findReplaced = new JRadioButton("Find replaced presentations", false);
                findReplaced.setMnemonic(KeyEvent.VK_R);
                radioPanel.add(findReplaced);
                group.add(findReplaced);
            } else {
                findReplaced = null;
            }
            
        } else {
            findSubPresentations = null;
            findAllSubPresentations = null;
            findReplacers = null;
            findReplaced = null;
        }
        
        String title = "";
        if (definition instanceof AdsClassDef) {
            if (!title.isEmpty()) {
                title += "/";
            }
            title += "subtypes";
        }
        if (definition instanceof IOverridable) {
            if (!title.isEmpty()) {
                title += "/";
            }
            title += "overrides";
        }
        if ((definition instanceof IOverwritable) || (definition instanceof Module)) {
            if (!title.isEmpty()) {
                title += "/";
            }
            title += "overwrites";
        }
        
        if (!title.isEmpty()) {
            findAllDescendants = new JRadioButton("Find all " + title);
            findAllDescendants.setMnemonic(KeyEvent.VK_A);
            radioPanel.add(findAllDescendants);
            group.add(findAllDescendants);
            
            findDirectDescendantsOnly = new JRadioButton("Find direct " + title + " only");
            findDirectDescendantsOnly.setMnemonic(KeyEvent.VK_D);
            radioPanel.add(findDirectDescendantsOnly);
            group.add(findDirectDescendantsOnly);
        } else {
            findAllDescendants = null;
            findDirectDescendantsOnly = null;
        }
        
        radioPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(radioPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        
        final Module module = definition.getModule();
        if (module != null && definition != module) {
            searchInCurrentModuleOnly = new JCheckBox("Search in current module only", false);
            searchInCurrentModuleOnly.setMnemonic(KeyEvent.VK_C);
            add(searchInCurrentModuleOnly, BorderLayout.SOUTH);
            if (definition instanceof AdsDefinition) {
                final EAccess access = ((AdsDefinition) definition).getAccessMode();
                if (access == EAccess.PRIVATE || access == EAccess.DEFAULT) {
                    searchInCurrentModuleOnly.setSelected(true);
                }
            }
        } else {
            searchInCurrentModuleOnly = null;
        }
        
        final Dimension size = new Dimension(400, 170);
        setPreferredSize(size);
        setMinimumSize(size);
        //setMaximumSize(size);
    }
    
    private FindUsagesCfg.ESearchType getSearchType() {
        if (findAllSubPresentations != null && findAllSubPresentations.isSelected()) {
            return FindUsagesCfg.ESearchType.FIND_ALL_SUBPRESENTATIONS;
        } else if (findSubPresentations != null && findSubPresentations.isSelected()) {
            return FindUsagesCfg.ESearchType.FIND_SUBPRESENTATIONS;
        } else if (findReplacers != null && findReplacers.isSelected()) {
            return FindUsagesCfg.ESearchType.FIND_REPLACERS;
        } else if (findReplaced != null && findReplaced.isSelected()) {
            return FindUsagesCfg.ESearchType.FIND_REPLACED;
        } else if (usagesPanel != null && usagesPanel.findUsages.isSelected()) {
            return usagesPanel.getSearchType();
        } else if (findUsed != null && findUsed.isSelected()) {
            return FindUsagesCfg.ESearchType.FIND_USED;
        } else if (findAllDescendants != null && findAllDescendants.isSelected()) {
            return FindUsagesCfg.ESearchType.FIND_ALL_DESCEDANTS;
        } else if (findDirectDescendantsOnly != null && findDirectDescendantsOnly.isSelected()) {
            return FindUsagesCfg.ESearchType.FIND_DIRECT_DESCENDANTS_ONLY;
        } else {
            throw new IllegalStateException();
        }
    }
    
    private boolean isSearchInCurrentModuleOnly() {
        return searchInCurrentModuleOnly != null && searchInCurrentModuleOnly.isSelected();
    }
    
    public static FindUsagesCfg askCfg(final Definition definition) {
        final FindUsagesCfgPanel cfgPanel = new FindUsagesCfgPanel(definition);
        
        final ModalDisplayer modalDisplayer = new ModalDisplayer(cfgPanel, "Find Usages");
        if (modalDisplayer.showModal()) {
            final FindUsagesCfg cfg = new FindUsagesCfg(definition);
            
            final FindUsagesCfg.ESearchType searchType = cfgPanel.getSearchType();
            cfg.setSearchType(searchType);
            
            if (cfgPanel.isSearchInCurrentModuleOnly()) {
                final Module module = definition.getModule();
                cfg.setRoots(Collections.singleton(module));
            }
            
            return cfg;
        } else {
            return null;
        }
    }
}
