package org.radixware.kernel.designer.ads.editors.documentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Dependences;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.doc.AdsDocDef;
import org.radixware.kernel.common.defs.ads.doc.AdsDocMapDef;
import org.radixware.kernel.common.defs.ads.doc.DocReference;
import org.radixware.kernel.common.defs.ads.doc.DocReferences;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EDocRefType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.SwingUtils;

public final class AdsDocMapEditor extends AdsDocEditor<AdsDocMapDef> {

    private LocalizingEditorPanel titleEditor = new LocalizingEditorPanel();
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootNode;

    public AdsDocMapEditor(AdsDocMapDef map) {

        super(map);
        map.getIdPath();
        initComponents();
        panelTitle.setLayout(new BorderLayout());
        editorName.setText(getRadixObject().getName());

        // tree
        treeModel = (DefaultTreeModel) tree.getModel();
        rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        buildTree();
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value,
                        selected, expanded, leaf, row, hasFocus);

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

                // rootMap
                if (node.getUserObject() == getRadixObject()) {
                    label.setIcon(getRadixObject().getIcon().getIcon());
                    return label;
                }

                // ref
                DocReference ref = (DocReference) node.getUserObject();
                if (ref.isValid()) {
                    AdsDocDef def = ref.getDocDef();
                    label.setIcon(def.getIcon().getIcon());
                    label.setText(def.toString());
                } else {
                    label.setBackground(Color.RED);
                    label.setText(ref.getPath().toString());
                }
                if (ref.getType() == EDocRefType.WEAK) {
                    label.setForeground(Color.GRAY);
                }

                return label;
            }
        });
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                update();
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    goTo();
                }
            }
        });

        titleEditor.open(new HandleInfo() {

            @Override
            public Id getTitleId() {
                return AdsDocMapEditor.this.getRadixObject().getTitleId();
            }

            @Override
            public AdsDefinition getAdsDefinition() {
                return AdsDocMapEditor.this.getRadixObject();
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (multilingualStringDef != null) {
                    AdsDocMapEditor.this.getRadixObject().setTitleId(multilingualStringDef.getId());
                } else {
                    AdsDocMapEditor.this.getRadixObject().setTitleId(null);
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                if (getAdsMultilingualStringDef() != null) {
                    getAdsMultilingualStringDef().setValue(language, newStringValue);
                }
            }
        });
        panelTitle.add(titleEditor, BorderLayout.CENTER);

        // Button
        SwingUtils.InitButton(buttonAppendStrongRef,
                RadixWareIcons.CREATE.ADD.getIcon(),
                "Append strong reference dita object",
                this,
                KeyEvent.VK_INSERT);
        SwingUtils.InitButton(buttonAppendWeakRef,
                RadixWareIcons.CREATE.ADD_SUB.getIcon(),
                "Append weak reference dita object",
                this,
                KeyEvent.VK_INSERT,
                InputEvent.CTRL_DOWN_MASK);
        SwingUtils.InitButton(buttonRemove,
                RadixWareIcons.DELETE.DELETE.getIcon(),
                "Remove reference dita object",
                this,
                KeyEvent.VK_DELETE);
        SwingUtils.InitButton(buttonGoTo,
                RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon(),
                "Go to select document definition editor",
                this,
                KeyEvent.VK_G,
                InputEvent.CTRL_DOWN_MASK);
        SwingUtils.InitButton(buttonUp,
                RadixWareIcons.ARROW.MOVE_UP.getIcon(),
                "Move object up in order in which the documentation is generated",
                this,
                KeyEvent.VK_U,
                InputEvent.CTRL_DOWN_MASK);
        SwingUtils.InitButton(buttonDown,
                RadixWareIcons.ARROW.MOVE_DOWN.getIcon(),
                "Move object down in order in which the documentation is generated",
                this,
                KeyEvent.VK_D,
                InputEvent.CTRL_DOWN_MASK);

        // Enabled
        Boolean enable = !getRadixObject().isReadOnly();
        buttonAppendStrongRef.setEnabled(enable);
        buttonAppendWeakRef.setEnabled(enable);
        editorName.setEditable(enable);
        titleEditor.setEnabled(enable);

        update();
    }

    private void buildTree() {
        rootNode.removeAllChildren();
        buildNode(rootNode, getRadixObject());
        treeModel.nodeStructureChanged(rootNode);
    }

    private void buildNode(DefaultMutableTreeNode node, AdsDocMapDef map) {
        for (DocReference ref : map.getReferences()) {
            AdsDocDef docDef = ref.getDocDef();
            boolean allowsChildren = (docDef instanceof AdsDocMapDef);
            DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(ref, allowsChildren);
            // TODO: !!! кастляь для исключения StackOverflow
            boolean isStackOverflow = (docDef == getRadixObject());
            if (allowsChildren && !isStackOverflow) {
                buildNode(subNode, (AdsDocMapDef) docDef);
            }
            node.add(subNode);
        }
    }

    private void afterChange(DocReference selectedRef) {
        afterChange();
        setSelectReference(selectedRef);
    }

    private void afterChange() {
        buildTree();
        update();
    }

    public void update() {
        SwingUtils.checkValidName(editorName, getRadixObject());

        // enabled
        DocReference ref = getSelectReference();
        DocReferences references = getRadixObject().getReferences();

        boolean selectRef = (ref != null);
        boolean selectValidDocDef = (selectRef) && (getSelectDocDef() != null);
        boolean selectChildRef = (selectRef) && (ref.getOwnerDefinition() == getRadixObject());
        boolean selectRefIsLast = (selectRef) && (references.last().equals(ref));
        boolean selectRefIsFirst = (selectRef) && (references.first().equals(ref));
        boolean readOnly = getRadixObject().isReadOnly();

        buttonGoTo.setEnabled(selectRef && selectValidDocDef);
        buttonRemove.setEnabled(selectValidDocDef && selectChildRef && !readOnly);
        buttonUp.setEnabled(selectChildRef && !selectRefIsFirst && !readOnly);
        buttonDown.setEnabled(selectChildRef && !selectRefIsLast && !readOnly);
    }

    private void setSelectReference(DocReference ref) {
        for (Enumeration e = rootNode.depthFirstEnumeration(); e.hasMoreElements();) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.getUserObject() == ref) {
                TreeNode[] nodes = node.getPath();
                TreePath path = new TreePath(nodes);
                tree.setSelectionPath(path);
            }
        }
    }

    private DocReference getSelectReference() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if ((node == null) || !(node.getUserObject() instanceof DocReference)) {
            return null;
        }
        DocReference ref = (DocReference) node.getUserObject();
        return ref;
    }

    private AdsDocDef getSelectDocDef() {
        DocReference ref = getSelectReference();
        if (ref == null) {
            return null;
        }
        return ref.getDocDef();
    }

    private void goTo() {
        AdsDocDef def = getSelectDocDef();
        if (def != null) {
            DialogUtils.goToObject(def);
        }
    }

    private void appendRef(ChooseDefinitionCfg cfg, EDocRefType type) {
        // select
        List<Definition> definitions = ChooseDefinition.chooseDefinitions(cfg);
        if (definitions == null) {
            return;
        }

        // add
        for (Definition def : definitions) {
            DocReference ref = new DocReference(new AdsPath(def), type);
            getRadixObject().getReferences().add(ref);
        }

        afterChange();
    }

    private void swap(int deltaIndex) {
        DocReferences references = getRadixObject().getReferences();
        DocReference ref = getSelectReference();
        int index = references.indexOf(ref);
        getRadixObject().getReferences().swap(index, index + deltaIndex);
        afterChange(ref);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        editorName = new javax.swing.JTextField();
        panelTitle = new javax.swing.JPanel();
        jlabelName = new javax.swing.JLabel();
        jToolBar = new javax.swing.JToolBar();
        buttonAppendStrongRef = new javax.swing.JButton();
        buttonAppendWeakRef = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        buttonUp = new javax.swing.JButton();
        buttonDown = new javax.swing.JButton();
        buttonGoTo = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();

        editorName.setText(org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.editorName.text")); // NOI18N
        editorName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                editorNameKeyReleased(evt);
            }
        });

        panelTitle.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout panelTitleLayout = new javax.swing.GroupLayout(panelTitle);
        panelTitle.setLayout(panelTitleLayout);
        panelTitleLayout.setHorizontalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelTitleLayout.setVerticalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 63, Short.MAX_VALUE)
        );

        org.openide.awt.Mnemonics.setLocalizedText(jlabelName, org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.jlabelName.text")); // NOI18N

        jToolBar.setFloatable(false);

        org.openide.awt.Mnemonics.setLocalizedText(buttonAppendStrongRef, org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonAppendStrongRef.text")); // NOI18N
        buttonAppendStrongRef.setToolTipText(org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonAppendStrongRef.toolTipText")); // NOI18N
        buttonAppendStrongRef.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        buttonAppendStrongRef.setFocusable(false);
        buttonAppendStrongRef.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonAppendStrongRef.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonAppendStrongRef.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonAppendStrongRef.setName("buttonAppendStrongRef"); // NOI18N
        buttonAppendStrongRef.setOpaque(false);
        buttonAppendStrongRef.setPreferredSize(new java.awt.Dimension(24, 24));
        buttonAppendStrongRef.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonAppendStrongRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAppendStrongRefActionPerformed(evt);
            }
        });
        jToolBar.add(buttonAppendStrongRef);

        org.openide.awt.Mnemonics.setLocalizedText(buttonAppendWeakRef, org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonAppendWeakRef.text")); // NOI18N
        buttonAppendWeakRef.setToolTipText(org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonAppendWeakRef.toolTipText")); // NOI18N
        buttonAppendWeakRef.setFocusable(false);
        buttonAppendWeakRef.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonAppendWeakRef.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonAppendWeakRef.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonAppendWeakRef.setName("buttonAppendWeakRef"); // NOI18N
        buttonAppendWeakRef.setPreferredSize(new java.awt.Dimension(24, 24));
        buttonAppendWeakRef.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonAppendWeakRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAppendWeakRefActionPerformed(evt);
            }
        });
        jToolBar.add(buttonAppendWeakRef);

        org.openide.awt.Mnemonics.setLocalizedText(buttonRemove, org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonRemove.text")); // NOI18N
        buttonRemove.setToolTipText(org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonRemove.toolTipText")); // NOI18N
        buttonRemove.setFocusable(false);
        buttonRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonRemove.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonRemove.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonRemove.setName("buttonRemove"); // NOI18N
        buttonRemove.setPreferredSize(new java.awt.Dimension(32, 32));
        buttonRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });
        jToolBar.add(buttonRemove);

        org.openide.awt.Mnemonics.setLocalizedText(buttonUp, org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonUp.text")); // NOI18N
        buttonUp.setToolTipText(org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonUp.toolTipText")); // NOI18N
        buttonUp.setFocusable(false);
        buttonUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonUp.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonUp.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonUp.setName("buttonUp"); // NOI18N
        buttonUp.setPreferredSize(new java.awt.Dimension(32, 32));
        buttonUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpActionPerformed(evt);
            }
        });
        jToolBar.add(buttonUp);

        org.openide.awt.Mnemonics.setLocalizedText(buttonDown, org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonDown.text")); // NOI18N
        buttonDown.setToolTipText(org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonDown.toolTipText")); // NOI18N
        buttonDown.setFocusable(false);
        buttonDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonDown.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonDown.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonDown.setName("buttonDown"); // NOI18N
        buttonDown.setPreferredSize(new java.awt.Dimension(32, 32));
        buttonDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDownActionPerformed(evt);
            }
        });
        jToolBar.add(buttonDown);

        org.openide.awt.Mnemonics.setLocalizedText(buttonGoTo, org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonGoTo.text")); // NOI18N
        buttonGoTo.setToolTipText(org.openide.util.NbBundle.getMessage(AdsDocMapEditor.class, "AdsDocMapEditor.buttonGoTo.toolTipText")); // NOI18N
        buttonGoTo.setFocusable(false);
        buttonGoTo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonGoTo.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonGoTo.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonGoTo.setName("buttonGoTo"); // NOI18N
        buttonGoTo.setPreferredSize(new java.awt.Dimension(32, 32));
        buttonGoTo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonGoTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGoToActionPerformed(evt);
            }
        });
        jToolBar.add(buttonGoTo);

        tree.setModel(new javax.swing.tree.DefaultTreeModel(new javax.swing.tree.DefaultMutableTreeNode(getRadixObject())));
        tree.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(tree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addComponent(jToolBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jlabelName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editorName, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlabelName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void editorNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editorNameKeyReleased
        changeName(editorName, editorName.getText());
    }//GEN-LAST:event_editorNameKeyReleased

    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
        AdsDocDef docDef = getSelectDocDef();
        if (DialogUtils.messageConfirmation("Delete reference to '" + docDef.getName() + "'?")) {
            DocReference ref = getSelectReference();
            getRadixObject().getReferences().remove(ref);
            afterChange();
        }
    }//GEN-LAST:event_buttonRemoveActionPerformed

    private void buttonAppendWeakRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAppendWeakRefActionPerformed
        // Услоивие выборки дифиниций
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(getRadixObject().getLayer(), new AdsVisitorProvider() {
            @Override
            public boolean isTarget(RadixObject object) {
                return (object instanceof AdsDocDef)
                        && (object != getRadixObject())
                        && (!getRadixObject().getReferences().contains((AdsDocDef) object));
            }
        }
        );
        appendRef(cfg, EDocRefType.WEAK);
    }//GEN-LAST:event_buttonAppendWeakRefActionPerformed

    private void buttonUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpActionPerformed
        swap(-1);
    }//GEN-LAST:event_buttonUpActionPerformed

    private void buttonGoToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGoToActionPerformed
        goTo();
    }//GEN-LAST:event_buttonGoToActionPerformed

    private void buttonDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownActionPerformed
        swap(1);
    }//GEN-LAST:event_buttonDownActionPerformed

    private void buttonAppendStrongRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAppendStrongRefActionPerformed
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(getRadixObject().getLayer(), new AdsVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {

                if (!(object instanceof AdsDocDef)) {
                    return false;
                }

                AdsDocDef docDef = ((AdsDocDef) object);
                Module mapModule = getRadixObject().getModule();
                Dependences dependences = mapModule.getDependences();
                Module objectModule = docDef.getModule();
                Set<Id> dependencesIds = dependences.getModuleIds();

                return (object != getRadixObject())
                        && (!getRadixObject().getReferences().contains(docDef))
                        && (objectModule.getId() == mapModule.getId()
                        || dependencesIds.contains(objectModule.getId()));

            }
        }
        );
        appendRef(cfg, EDocRefType.STRONG);
    }//GEN-LAST:event_buttonAppendStrongRefActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAppendStrongRef;
    private javax.swing.JButton buttonAppendWeakRef;
    private javax.swing.JButton buttonDown;
    private javax.swing.JButton buttonGoTo;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JButton buttonUp;
    private javax.swing.JTextField editorName;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar;
    private javax.swing.JLabel jlabelName;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables

}
