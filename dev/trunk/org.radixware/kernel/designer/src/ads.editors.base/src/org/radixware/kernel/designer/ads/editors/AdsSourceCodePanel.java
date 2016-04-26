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
 * AdsMethodCodePanel.java
 *
 * Created on Nov 21, 2008, 11:15:11 AM
 */
package org.radixware.kernel.designer.ads.editors;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSources;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.designer.ads.editors.CustomizableJmlEditor.IActionInstaller;
import org.radixware.kernel.designer.common.editors.jml.JmlEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsSourceCodePanel extends javax.swing.JPanel {

    private JmlEditor defaultEditor;
    private JTabbedPane tabs;
    private boolean updating = false;
    private Map<ERuntimeEnvironmentType, JmlEditor> editors = null;
    private boolean isReadOnly = false;
    private AdsSources context;

    //------------------------------------------------------------------------
    private IActionInstaller installer;
    private final ChangeListener tabChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            updateComponents(null, isReadOnly);
        }
    };

    public AdsSourceCodePanel() {
        this(null);
    }

    public AdsSourceCodePanel(IActionInstaller installer) {
        initComponents();

        this.installer = installer;
        defaultEditor = createDefaultEditor();

        add(defaultEditor, BorderLayout.CENTER);
        //setName(NbBundle.getMessage(AdsSourceCodePanel.class, "CodeEditorName"));
        chSeparateSource.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (context != null && !updating) {
                    updating = true;
                    try {
                        if (chSeparateSource.isSelected()) {
                            if (context.canSeparateSources()) {
                                context.setSourcesSeparated(true);
                            }
                        } else {
                            context.setSourcesSeparated(false);
                        }
                    } finally {
                        updating = false;
                        updateComponents(null, isReadOnly);
                    }
                }
            }
        });
    }

    private void updateComponents(OpenInfo info, boolean readonly) {
        try {
            updating = true;

            isReadOnly = readonly;
            AdsSources sources = context;
            if (sources.isSourcesSeparated()) {
                if (defaultEditor != null) {
                    remove(defaultEditor);
                    defaultEditor = null;
                }
                if (tabs == null) {
                    tabs = new JTabbedPane();
//                    tabs.addChangeListener(tabChangeListener);
                    add(tabs, BorderLayout.CENTER);
                }
                if (editors == null) {
                    editors = new HashMap<>();
                }
                List<ERuntimeEnvironmentType> envs = new ArrayList<>(sources.getDefinedSourceTypes());
                Collections.sort(envs, new Comparator<ERuntimeEnvironmentType>() {

                    @Override
                    public int compare(ERuntimeEnvironmentType o1, ERuntimeEnvironmentType o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                for (ERuntimeEnvironmentType env : envs) {
                    Jml src = sources.getSource(env);
                    if (src != null) {
                        JmlEditor editor = editors.get(env);
                        if (editor == null) {
                            editor = createDefaultEditor();
                            editors.put(env, editor);
                            tabs.add(env.getName(), editor);
                        }
                        editor.open(src, info);
                        if (info != null) {
                            if (src == info.getTarget() || src.isParentOf(info.getTarget())) {
                                tabs.setSelectedComponent(editor);
                            }
                        }
                        editor.setEditable(!readonly);
                    }
                }
            } else {
                if (tabs != null) {
                    remove(tabs);
                    tabs = null;
                    editors.clear();
                    editors = null;
                }
                JmlEditor editor = defaultEditor;
                if (editor == null) {
                    editor = createDefaultEditor();
                    add(editor, BorderLayout.CENTER);
                    defaultEditor = editor;
                }
                editor.open(sources.getSource((ERuntimeEnvironmentType) null), info);
                editor.setEditable(!readonly);
            }
            chSeparateSource.setEnabled(!readonly && context.canSeparateSources());
            chSeparateSource.setSelected(context.isSourcesSeparated());
            if (!context.canSeparateSources()) {
                jPanel1.setVisible(false);
            } else {
                jPanel1.setVisible(true);
            }
        } finally {
            updating = false;
        }
    }

    public void open(AdsSources context, OpenInfo openInfo, boolean readonly) {
        this.context = context;
        updateComponents(openInfo, readonly);
    }

    public boolean requestFocusToEditor() {
        JmlEditor editor = getCurrentEditor();
        if (editor != null) {
            return editor.getPane().requestFocusInWindow();
        } else {
            return false;
        }
    }

    private JmlEditor getCurrentEditor() {
        if (defaultEditor != null) {
            return defaultEditor;
        } else if (tabs != null) {
            int index = tabs.getSelectedIndex();
            if (index >= 0 && index < tabs.getTabCount()) {
                return (JmlEditor) tabs.getComponentAt(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void requestFocus() {
        requestFocusInWindow();
    }

    @Override
    public boolean requestFocusInWindow() {
        JmlEditor editor = getCurrentEditor();
        if (editor != null) {
            return editor.getPane().requestFocusInWindow();
        }
        return false;
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        if (mgr != null) {
            super.setLayout(mgr);
        }
    }

    private JmlEditor createDefaultEditor() {
        return new CustomizableJmlEditor(installer);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        chSeparateSource = new javax.swing.JCheckBox();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        chSeparateSource.setText(org.openide.util.NbBundle.getMessage(AdsSourceCodePanel.class, "AdsSourceCodePanel.chSeparateSource.text")); // NOI18N
        jPanel1.add(chSeparateSource, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chSeparateSource;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
