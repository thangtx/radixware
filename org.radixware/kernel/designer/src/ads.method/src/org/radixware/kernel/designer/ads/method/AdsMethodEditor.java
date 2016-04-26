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

package org.radixware.kernel.designer.ads.method;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.AccessChangedEvent;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.AdsSourceCodePanel;
import org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel;

import org.radixware.kernel.designer.ads.editors.clazz.transparent.PublishedClassSynchronizer;
import org.radixware.kernel.designer.ads.method.profile.ChangeProfilePanel;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.PublicationUtils;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


public class AdsMethodEditor extends RadixObjectEditor<AdsMethodDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsMethodDef> {

        @Override
        public IRadixObjectEditor<AdsMethodDef> newInstance(AdsMethodDef method) {
            return new AdsMethodEditor(method);
        }
    }
    //profile editor
    private JButton profileBtn;
    private JButton changePublishedBtn;
    private JPanel rpcButtonsPanel;
    //code editor
    private AdsSourceCodePanel codeEditor = new AdsSourceCodePanel();
    private AdsCommandHandlerPropertiesPanel commandHandlerPanel = new AdsCommandHandlerPropertiesPanel();
    private AdsPresentationSlotPropertiesPanel slotPanel = new AdsPresentationSlotPropertiesPanel();
    private Color defaultForeground;
    private ActionListener callableListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean selected = callableCheck.isSelected();
            getRadixObject().setReflectiveCallable(selected);
            update();
            changeReflectiveCallableProp();
        }
    };

    /**
     * Creates new form AdsMethodEditorView
     */
    public AdsMethodEditor(AdsMethodDef method) {
        super(method);
        codeEditor.setBorder(BorderFactory.createEtchedBorder());

        initComponents();        
        setupProfileEditor();

        abstractCheck = accessList.addCheckBox("Abstract");
        staticCheck = accessList.addCheckBox("Static");
        deprecatedCheck = accessList.addCheckBox("Deprecated");
        callableCheck = accessList.addCheckBox("Reflective callable");

        setupOvrBtns();
        callableCheck.addActionListener(callableListener);
        openAccessPanel();
        defaultForeground = staticCheck.getForeground();
    }

    private void openAccessPanel() {

        AdsMethodDef method = getMethod();
        if (method.isInterfaceMethod()) {
            accessList.open(method, EAccess.PRIVATE, EAccess.PROTECTED, EAccess.DEFAULT);
        } else {
            if (method.isOverride() || method.isOverwrite()) {
                accessList.open(method, EAccess.PRIVATE);
            } else {
                if (method instanceof AdsTransparentMethodDef) {
                    ArrayList<EAccess> values = new ArrayList<>();
                    final EAccess min = method.getMinimumAccess();
                    for (final EAccess access : EAccess.values()) {
                        if (access != min) {
                            values.add(access);
                        }
                    }
                    accessList.open(method, values.toArray(new EAccess[values.size()]));
                    return;
                }
                accessList.open(method);
            }
        }
    }

    public final AdsMethodDef getMethod() {
        return getRadixObject();
    }

    private void setupProfileEditor() {
        profileBtn = profileEditor.addButton();
        profileBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        profileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeProfilePanel panel = new ChangeProfilePanel();
                panel.editProfile(getMethod());
            }
        });
        profileBtn.setToolTipText(NbBundle.getMessage(AdsMethodEditor.class, "ProfileTip"));
        profileBtn.setEnabled(false);

        changePublishedBtn = profileEditor.addButton();
        changePublishedBtn.setIcon(RadixWareIcons.METHOD.PUBLISHED.getIcon(16, 16));
        changePublishedBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (PublishedClassSynchronizer.replace((AdsTransparentMethodDef) getMethod())) {
                    AdsMethodEditor.this.update();
                }
            }
        });
        changePublishedBtn.setToolTipText(NbBundle.getMessage(AdsMethodEditor.class, "ChangePublishedTip"));
        changePublishedBtn.setEnabled(false);
    }

    private void switchChecks(boolean state) {
        abstractCheck.setEnabled(state && !(this.getMethod().getOwnerClass() instanceof AdsModelClassDef));
        staticCheck.setEnabled(state);

        AdsTransparence transparence = getMethod().getOwnerClass().getTransparence();
        deprecatedCheck.setEnabled(state || transparence != null && transparence.isTransparent());
    }

    private void setupChecks() {
        switchChecks(false);
        abstractCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isUpdate) {
                    final AdsMethodDef method = getMethod();
                    if (e.getStateChange() == ItemEvent.SELECTED) {

                        method.getProfile().getAccessFlags().setAbstract(true);
                    }
                    if (e.getStateChange() == ItemEvent.DESELECTED) {

                        method.getProfile().getAccessFlags().setAbstract(false);
                        abstractCheck.setEnabled(method.getOwnerClass().getAccessFlags().isAbstract()
                                && !(method instanceof AdsCommandHandlerMethodDef
                                || method.getOwnerClass() instanceof AdsInterfaceClassDef));
                    }
                    updateCodeEditorVisibility();
                    updateChecks();
                }
            }
        });
        staticCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isUpdate) {
                    final AdsMethodDef method = getMethod();
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        method.getProfile().getAccessFlags().setStatic(true);
                    } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                        method.getProfile().getAccessFlags().setStatic(false);
                    }
                    if (method instanceof AdsTransparentMethodDef) {
                        staticCheck.setEnabled(false);
                    }
                    updateChecks();
                }
            }
        });

        deprecatedCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isUpdate) {
                    final AdsMethodDef method = getMethod();
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        method.getProfile().getAccessFlags().setDeprecated(true);
                    }
                    if (e.getStateChange() == ItemEvent.DESELECTED) {
                        method.getProfile().getAccessFlags().setDeprecated(false);
                    }
                }
            }
        });
    }

    private void setupListeners() {
        setupChecks();
    }

    private void setupOvrBtns() {
        overrideCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isUpdate) {
                    boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                    final AdsMethodDef method = getMethod();
                    method.setOverride(selected);

                    final AdsMethodDef ovr = method.getHierarchy().findOverridden().get();
                    overrideCheck.setEnabled(!readonly && (ovr != null || method.isOverride()));
                    if (selected) {
                        if (ovr == null) {
                            overrideCheck.setForeground(Color.RED);
                        } else {
                            overrideCheck.setForeground(Color.BLACK);
                        }
                    } else {
                        overrideCheck.setForeground(Color.BLACK);
                    }
                    editOvrBtn.setEnabled(ovr != null);
                    showOvrBtn.setEnabled(ovr != null);
                }
            }
        });

        overwriteCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isUpdate) {
                    boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                    final AdsMethodDef method = getMethod();
                    method.setOverwrite(selected);

                    final AdsMethodDef ovr = method.getHierarchy().findOverwritten().get();
                    overwriteCheck.setEnabled(!readonly && (ovr != null || method.isOverwrite()));
                    if (selected) {
                        if (ovr == null) {
                            overwriteCheck.setForeground(Color.RED);
                        } else {
                            overwriteCheck.setForeground(Color.BLACK);
                        }
                    } else {
                        overwriteCheck.setForeground(Color.BLACK);
                    }
                    editOverwrittenBtn.setEnabled(ovr != null);
                    showOverwriteBtn.setEnabled(ovr != null);
                }

            }
        });

        editOvrBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final AdsMethodDef method = getMethod();
                if (method != null) {
                    AdsMethodDef ovr = method.getHierarchy().findOverridden().get();
                    if (ovr != null) {
                        EditorsManager.getDefault().open(ovr);
                    }
                }
            }
        });

        showOvrBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final AdsMethodDef method = getMethod();
                if (method != null) {
                    AdsMethodDef ovr = method.getHierarchy().findOverridden().get();
                    if (ovr != null) {
                        NodesManager.selectInProjects(ovr);
                    }
                }
            }
        });

        editOverwrittenBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final AdsMethodDef method = getMethod();
                if (method != null) {
                    final AdsMethodDef ovr = method.getHierarchy().findOverwritten().get();
                    if (ovr != null) {
                        EditorsManager.getDefault().open(ovr);
                    }
                }
            }
        });

        showOverwriteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final AdsMethodDef method = getMethod();
                if (method != null) {
                    final AdsMethodDef ovr = method.getHierarchy().findOverwritten().get();
                    if (ovr != null) {
                        NodesManager.selectInProjects(ovr);
                    }
                }
            }
        });
    }

    private void updateChecks() {
        final AdsMethodDef method = getMethod();

        if (method != null) {
            final AdsAccessFlags accessFlags = method.getProfile().getAccessFlags();
            final boolean availAbstract = !accessFlags.isStatic() && !accessFlags.isFinal() && method.getAccessMode() != EAccess.PRIVATE;

            if (abstractCheck.isSelected() && !availAbstract) {
                abstractCheck.setSelected(false);
                accessFlags.setAbstract(false);
            }

            abstractCheck.setEnabled(availAbstract);
            staticCheck.setEnabled(!accessFlags.isAbstract());

            updatePublishedState();
        }
    }

    private void updateCodeEditorVisibility() {
        final AdsMethodDef method = getMethod();

        if (method != null) {
            boolean isSlot = method instanceof AdsPresentationSlotMethodDef;
            boolean isCommand = method instanceof AdsCommandHandlerMethodDef;
            boolean isAbstract = method.getProfile().getAccessFlags().isAbstract();
            if (isSlot || isCommand) {
                codeEditor.setVisible(!isAbstract);
            } else {
                editorsPane.setVisible(!isAbstract);
            }
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

        abstractCheck = new javax.swing.JCheckBox();
        staticCheck = new javax.swing.JCheckBox();
        deprecatedCheck = new javax.swing.JCheckBox();
        callableCheck = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        editorsPane = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        profileEditor = new org.radixware.kernel.common.components.ExtendableTextField();
        showOvrBtn = new javax.swing.JButton();
        editOvrBtn = new javax.swing.JButton();
        overrideCheck = new javax.swing.JCheckBox();
        showOverwriteBtn = new javax.swing.JButton();
        overwriteCheck = new javax.swing.JCheckBox();
        editOverwrittenBtn = new javax.swing.JButton();
        accessList = new org.radixware.kernel.designer.ads.common.dialogs.AccessPanel();
        envSelectorPanel = new org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel();

        abstractCheck.setText(org.openide.util.NbBundle.getMessage(AdsMethodEditor.class, "AbstractTip")); // NOI18N

        staticCheck.setText(org.openide.util.NbBundle.getMessage(AdsMethodEditor.class, "StaticTip")); // NOI18N

        deprecatedCheck.setText(org.openide.util.NbBundle.getMessage(AdsMethodEditor.class, "DeprecatedTip")); // NOI18N

        callableCheck.setText(org.openide.util.NbBundle.getMessage(AdsMethodEditor.class, "CallableTip")); // NOI18N

        setMaximumSize(null);

        editorsPane.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        profileEditor.setEditorType(org.radixware.kernel.common.components.ExtendableTextField.EDITOR_HTML_TEXTFIELD);
        profileEditor.addTextMouseListener();
        profileEditor.setEditable(false);

        showOvrBtn.setIcon(RadixWareIcons.TREE.SELECT_IN_TREE.getIcon(10,10));
        showOvrBtn.setMaximumSize(new java.awt.Dimension(23, 23));
        showOvrBtn.setMinimumSize(new java.awt.Dimension(23, 23));
        showOvrBtn.setPreferredSize(new java.awt.Dimension(23, 23));

        editOvrBtn.setIcon(RadixWareIcons.ARROW.EDIT.EDIT.getIcon(10,10)        );
        editOvrBtn.setMaximumSize(new java.awt.Dimension(23, 23));
        editOvrBtn.setMinimumSize(new java.awt.Dimension(23, 23));
        editOvrBtn.setPreferredSize(new java.awt.Dimension(23, 23));

        overrideCheck.setText(org.openide.util.NbBundle.getMessage(AdsMethodEditor.class, "OverrideTip")); // NOI18N
        overrideCheck.setEnabled(false);

        showOverwriteBtn.setIcon(RadixWareIcons.TREE.SELECT_IN_TREE.getIcon(10,10));
        showOverwriteBtn.setMaximumSize(new java.awt.Dimension(23, 23));
        showOverwriteBtn.setMinimumSize(new java.awt.Dimension(23, 23));
        showOverwriteBtn.setPreferredSize(new java.awt.Dimension(23, 23));

        overwriteCheck.setText(org.openide.util.NbBundle.getMessage(AdsMethodEditor.class, "AdsMethodEditor.overwriteCheck.text")); // NOI18N
        overwriteCheck.setEnabled(false);

        editOverwrittenBtn.setIcon(RadixWareIcons.EDIT.EDIT.getIcon(10,10));
        editOverwrittenBtn.setMaximumSize(new java.awt.Dimension(23, 23));
        editOverwrittenBtn.setMinimumSize(new java.awt.Dimension(23, 23));
        editOverwrittenBtn.setPreferredSize(new java.awt.Dimension(23, 23));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(profileEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(accessList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addComponent(overwriteCheck)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editOverwrittenBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(showOverwriteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overrideCheck)
                        .addGap(8, 8, 8)
                        .addComponent(editOvrBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(showOvrBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(envSelectorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(profileEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(overwriteCheck)
                    .addComponent(accessList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editOverwrittenBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showOverwriteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(overrideCheck)
                    .addComponent(editOvrBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showOvrBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(envSelectorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {overrideCheck, overwriteCheck});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(editorsPane, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(262, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(143, 143, 143)
                    .addComponent(editorsPane, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox abstractCheck;
    private org.radixware.kernel.designer.ads.common.dialogs.AccessPanel accessList;
    private javax.swing.JCheckBox callableCheck;
    private javax.swing.JCheckBox deprecatedCheck;
    private javax.swing.JButton editOverwrittenBtn;
    private javax.swing.JButton editOvrBtn;
    private javax.swing.JPanel editorsPane;
    private org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel envSelectorPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox overrideCheck;
    private javax.swing.JCheckBox overwriteCheck;
    private org.radixware.kernel.common.components.ExtendableTextField profileEditor;
    private javax.swing.JButton showOverwriteBtn;
    private javax.swing.JButton showOvrBtn;
    private javax.swing.JCheckBox staticCheck;
    // End of variables declaration//GEN-END:variables
    private final RadixObject.RenameListener adsMethodProfileChangeListener = new RadixObject.RenameListener() {
        @Override
        public void onEvent(RadixObject.RenameEvent e) {
            profileEditor.setValue(getMethod().getProfile().getProfileHtml());
        }
    };
    private boolean readonly = false;
    private final AdsDefinition.AccessListener accListener = new AdsDefinition.AccessListener() {
        @Override
        public void onEvent(AccessChangedEvent e) {
            update();
            changeAccessModeForDescendants();
        }
    };

    @Override
    public boolean open(OpenInfo info) {
        profileBtn.setEnabled(true);
        setupListeners();
        update(info);
        requestFocus();
        getMethod().getAccessChangeSupport().addEventListener(accListener);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jPanel2.setSize(jPanel2.getSize());
                jPanel2.revalidate();
                jPanel2.repaint();
            }
        });
        return super.open(info);
    }
    private boolean isUpdate = false;

    @Override
    public void update() {
        update((OpenInfo) null);
    }

    @Override
    public void requestFocus() {
        codeEditor.requestFocusToEditor();
    }
    private final ChangeListener envSelectorListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            update();
        }
    };

    public void update(OpenInfo info) {
        final AdsMethodDef method = getMethod();
        if (method != null) {
            if (EnvSelectorPanel.isMeaningFullFor(method)) {
                envSelectorPanel.setVisible(true);
                envSelectorPanel.removeChangeListener(envSelectorListener);
                envSelectorPanel.open(method);
                envSelectorPanel.addChangeListener(envSelectorListener);
                //        jPanel2.setSize(jPanel2.getWidth(), 124);
            } else {
                envSelectorPanel.setVisible(false);
                envSelectorPanel.removeChangeListener(envSelectorListener);
                //    jPanel2.setSize(jPanel2.getWidth(),124);
            }
            isUpdate = true;
            profileEditor.setToolTipText(method.getToolTip());

            method.getProfile().addRenameListener(adsMethodProfileChangeListener);

            final EMethodNature nature = method.getNature();
            final AdsAccessFlags flags = method.getProfile().getAccessFlags();

            readonly = method.isReadOnly()
                    || nature.equals(EMethodNature.ALGO_START)
                    || nature.equals(EMethodNature.ALGO_STROB)
                    || nature.equals(EMethodNature.SYSTEM)
                    || method instanceof AdsTransparentMethodDef;

            //access flags
            openFlags(flags, method.getOwnerClass());
            //code editor
            if (method instanceof AdsUserMethodDef
                    && !(nature.equals(EMethodNature.ALGO_START)
                    || nature.equals(EMethodNature.ALGO_STROB)
                    || nature.equals(EMethodNature.SYSTEM)
                    || method.getOwnerClass() instanceof AdsInterfaceClassDef)) {
                AdsUserMethodDef user = (AdsUserMethodDef) method;
                //Jml code = user.getSource();
                codeEditor.open(user.getSources(), info, readonly);
                editorsPane.add(codeEditor, BorderLayout.CENTER);
                updateCodeEditorVisibility();
            } else {
                if (method instanceof AdsRPCMethodDef) {
                    if (rpcButtonsPanel == null) {
                        rpcButtonsPanel = new JPanel();

                        final AdsRPCMethodDef rpc = (AdsRPCMethodDef) method;
                        editorsPane.add(rpcButtonsPanel);
                        rpcButtonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
                        JButton goToServerButton = new JButton("Open server side method");
                        goToServerButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                final AdsMethodDef ssm = rpc.findServerSideMethod();
                                if (ssm != null) {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            EditorsManager.getDefault().open(ssm);
                                        }
                                    });
                                }
                            }
                        });
                        rpcButtonsPanel.add(goToServerButton);
                        JButton goToCommand = new JButton("Open command");
                        goToCommand.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                final AdsCommandDef ssm = rpc.findCommand();
                                if (ssm != null) {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            EditorsManager.getDefault().open(ssm);
                                        }
                                    });
                                }
                            }
                        });
                        rpcButtonsPanel.add(goToCommand);


                    }
                }
            }

            //flags processing
            if (method.isConstructor()) {
                staticCheck.setEnabled(false);
            }
            if (method instanceof AdsTransparentMethodDef) {
                accessList.setEnabled(!readonly);
                changePublishedBtn.setEnabled(!method.isReadOnly());
                updatePublishedState();
            }
            if (method instanceof AdsCommandHandlerMethodDef) {
                abstractCheck.setEnabled(false);
                staticCheck.setEnabled(false);
                editorsPane.add(commandHandlerPanel, BorderLayout.NORTH);
                commandHandlerPanel.open((AdsCommandHandlerMethodDef) method);
            }

            if (method instanceof AdsPresentationSlotMethodDef) {
                editorsPane.add(slotPanel, BorderLayout.NORTH);
                slotPanel.open((AdsPresentationSlotMethodDef) method);
            }

            AdsMethodDef overriden = method.getHierarchy().findOverridden().get();
            AdsMethodDef overwritten = method.getHierarchy().findOverwritten().get();
            boolean isOverriden = method.isOverride();
            boolean isOverwritten = method.isOverwrite();

            boolean overridenVisible = overriden != null || method.isOverride();
            editOvrBtn.setEnabled(overriden != null);
            showOvrBtn.setEnabled(overriden != null);
            overrideCheck.setEnabled(overridenVisible && !readonly);
            overrideCheck.setSelected(isOverriden);
            overrideCheck.setForeground((isOverriden && overriden == null) ? Color.RED : Color.BLACK);

            boolean overwriteVisible = overwritten != null || method.isOverwrite();
            editOverwrittenBtn.setEnabled(overwritten != null);
            showOverwriteBtn.setEnabled(overwritten != null);
            overwriteCheck.setEnabled(overwriteVisible && !readonly);
            overwriteCheck.setSelected(isOverwritten);
            overwriteCheck.setForeground((isOverwritten && overwritten == null) ? Color.RED : Color.BLACK);

//            optionsPanel.setVisible(overridenVisible || overwriteVisible);

            //overrideCheck.setEnabled(!readonly);
            //overwriteCheck.setEnabled(!readonly);

            openAccessPanel();
            //profile editor
            profileEditor.setValue(method.getProfile().getProfileHtml());

            callableCheck.setSelected(method.isReflectiveCallable());
            callableCheck.setEnabled(!readonly && method.getNature() != EMethodNature.PRESENTATION_SLOT);

            isUpdate = false;
        }
    }

    private void updatePublishedState() {

        final Collection<RadixPlatformClass.Method> toBePublished;

        final AdsMethodDef method = getMethod();
        final AdsClassDef ownerclass = method.getOwnerClass();
        final PlatformLib currentLib = ((AdsSegment) ownerclass.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(ownerclass.getUsageEnvironment());

        if (AdsTransparence.isTransparent(ownerclass)) {
            final String classcontext = ownerclass.getTransparence().getPublishedName();
            final RadixPlatformClass context = currentLib.findPlatformClass(classcontext);

            if (context == null) {
                DialogUtils.messageError("Platform class '" + classcontext + "' not found.");
                return;
            }

            toBePublished = PublicationUtils.getMethods(context, new PublicationUtils.UnpublishedMethodFilter(ownerclass));

            changePublishedBtn.setEnabled(context != null && !toBePublished.isEmpty());
            if (context != null) {
                RadixPlatformClass.Method platformMethod = context.findMethodByProfile(method);
                if (platformMethod != null) {
                    final AdsAccessFlags accessFlags = method.getProfile().getAccessFlags();

                    final boolean platformIsStatic = platformMethod.isStatic();
                    final boolean adsIsStatic = accessFlags.isStatic();
                    boolean errorState = platformIsStatic != adsIsStatic;
                    staticCheck.setEnabled(platformIsStatic != adsIsStatic);
                    if (errorState) {
                        staticCheck.setForeground(Color.RED);
                    } else {
                        staticCheck.setForeground(defaultForeground);
                    }

                    final boolean isAbstract = platformMethod.isAbstract();
                    final boolean radixAbstract = accessFlags.isAbstract();
                    errorState = radixAbstract != isAbstract;

                    if (errorState) {
                        abstractCheck.setForeground(Color.RED);
                        abstractCheck.setEnabled(true);
                    } else {
                        abstractCheck.setForeground(defaultForeground);
                        abstractCheck.setEnabled(false);
                    }
                }
                if (!toBePublished.isEmpty()) {
                    changePublishedBtn.setToolTipText(NbBundle.getMessage(AdsMethodEditor.class, "ChangePublishedTip"));
                } else {
                    changePublishedBtn.setToolTipText(NbBundle.getMessage(AdsMethodEditor.class, "PublishedTips-Count"));
                }
            } else {
                changePublishedBtn.setToolTipText(NbBundle.getMessage(AdsMethodEditor.class, "PublishedTips-LoadError"));
            }
        } else {
            changePublishedBtn.setEnabled(false);
            changePublishedBtn.setToolTipText(NbBundle.getMessage(AdsMethodEditor.class, "ChangePublishedTip"));
        }
    }

    private void openFlags(AdsAccessFlags flags, AdsClassDef owner) {
        final AdsMethodDef method = getMethod();

        abstractCheck.setSelected(flags.isAbstract());
        staticCheck.setSelected(flags.isStatic());
        deprecatedCheck.setSelected(flags.isDeprecated());
        if (!readonly) {
            if (owner instanceof AdsInterfaceClassDef
                    || method instanceof AdsTransparentMethodDef) {
                switchChecks(false);

            } else {
                staticCheck.setEnabled(true);

                abstractCheck.setEnabled(!method.isFinal() && (method.getOwnerClass().getAccessFlags().isAbstract()
                        || abstractCheck.isSelected()));

                updateChecks();
            }
            deprecatedCheck.setEnabled(true);
        } else {
            switchChecks(false);
        }
    }

    private Collection<AdsMethodDef> applyChanges() {
        OverVisitor visitor = new OverVisitor();
        getRadixObject().getModule().getSegment().getLayer().getBranch().visitAll(visitor);
        if (visitor.desc.size() > 0) {
            String question = NbBundle.getMessage(AdsMethodEditor.class, "ChangeConfirmationMessage");
            String message = "\n" + question;
            message += "\n\n";
            for (AdsMethodDef m : visitor.desc) {
                message += m.getQualifiedName() + "\n";
            }
            message += "\n";

            if (DialogUtils.messageConfirmation(message)) {
                return visitor.desc;
            }
        }
        return new ArrayList<AdsMethodDef>();
    }

    private void changeAccessModeForDescendants() {
        Collection<AdsMethodDef> res = applyChanges();
        if (res.size() > 0) {
            final EAccess accessMode = getRadixObject().getAccessMode();
            boolean isFinal = getRadixObject().isFinal();
            boolean isPublished = getRadixObject().isPublished();
            for (AdsMethodDef m : res) {
                m.setAccessMode(accessMode);
                m.setPublished(isPublished);
                m.setFinal(isFinal);
            }
        }
    }

    private void changeReflectiveCallableProp() {
        Collection<AdsMethodDef> res = applyChanges();
        if (res.size() > 0) {
            final boolean callable = getRadixObject().isReflectiveCallable();
            for (AdsMethodDef m : res) {
                m.setReflectiveCallable(callable);
            }
        }
    }

    private class OverVisitor implements IVisitor {

        Collection<AdsMethodDef> desc;

        OverVisitor() {
            this.desc = new HashSet<AdsMethodDef>();
        }

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof AdsMethodDef) {
                if (((Definition) radixObject).getId().equals(getRadixObject().getId())) {
                    if (DefinitionsUtils.isOverridesOrOverwrites((Definition) radixObject, getRadixObject())) {

                        AdsMethodDef rdx = (AdsMethodDef) radixObject;
                        AdsMethodDef method = getRadixObject();

                        if (!rdx.getAccessMode().equals(method.getAccessMode())
                                || rdx.isReflectiveCallable() != method.isReflectiveCallable()
                                || rdx.isPublished() != method.isPublished()
                                || rdx.isFinal() != method.isFinal()) {
                            desc.add((AdsMethodDef) radixObject);
                        }
                    }
                }
            }
        }
    }
}
