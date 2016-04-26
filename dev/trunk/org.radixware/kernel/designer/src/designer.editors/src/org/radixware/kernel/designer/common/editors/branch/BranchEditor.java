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

package org.radixware.kernel.designer.common.editors.branch;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.ERepositoryBranchType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.RadixObjectModalEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.debugger.StartupInfo;
import org.radixware.kernel.designer.debugger.StartupInfoManager;
import org.radixware.kernel.designer.debugger.StartupInfoProfile;

final class BranchEditor extends RadixObjectModalEditor<Branch> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<Branch> {

        @Override
        public BranchEditor newInstance(Branch branch) {
            return new BranchEditor(branch);
        }
    }

    private class ReleaseLayerMessageGroupModelItem {

        List<ReleaseLayerMessageGroupModelItem> children = null;

        public ReleaseLayerMessageGroupModelItem getChild(int index) {
            return children == null ? null : children.get(index);
        }

        public int getChildCount() {
            return children == null ? 0 : children.size();
        }

        public int indexOfChild(Object child) {
            return children == null ? -1 : children.indexOf(child);
        }
    }

//    private DistributeableLayerList layerList;
//    private DistributeableLayerList.DistributionManager distManager;

    private BaseDistUriManager distUriManager = new BaseDistUriManager(getBranch());
    private DebuggerSetup serverDebug = new DebuggerSetup();
    private DebuggerSetup explorerDebug = new DebuggerSetup();
    private DebugProfileSelectorPanel debugProfileSelectorPanel;

    private BranchEditor(Branch branch) {
        super(branch);

        initComponents();
//        distManager = new DistributeableLayerList.DistributionManager(branch);
//        layerList = new DistributeableLayerList(distManager, branch, jPanel4);
        errorMsgLabel.setVisible(false);
        releaseOptionsSplitter.setDividerLocation(0.5);
        branchNameValue.setEditable(false);
        branchNameValue.setText(branch.getName());
        jPanel2.setVisible(false);
        txtTitle.setText(branch.getTitle());

        branchNameValue.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                BranchEditor.this.checkInput();
            }
        });

        final String curBranchLayerName = branch.getBaseDevelopmentLayerUri();
        baseDevelopmentLayerField.setText(curBranchLayerName);

        branchDirectoryValue.setText(branch.getDirectory().getPath());
        baseReleaseVersionValue.setText(branch.getBaseReleaseVersion());

        String lastVersion = branch.getLastReleaseVersion();
        if (lastVersion != null
                && !lastVersion.isEmpty()) {
            lastReleaseVer.setText(lastVersion);
        } else {
            lastReleaseVer.setText(NbBundle.getMessage(BranchEditor.class, "NotDefinedTip"));
        }

        sourceBranchValue.setText(branch.getSourceBranch());

        ERepositoryBranchType branchType = branch.getType();
        typeValue.setText(branchType != null ? branchType.getValue() : NbBundle.getMessage(BranchEditor.class, "NotDefinedTip"));

        setName(NbBundle.getMessage(BranchEditor.class, "CTL_BranchTopComponent"));
        setToolTipText(NbBundle.getMessage(BranchEditor.class, "HINT_BranchTopComponent"));

        btConfigureNotification.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                editNotification();
            }
        });

        tbDebug.add("Server", serverDebug);
        tbDebug.add("Explorer", explorerDebug);

        debugProfileSelectorPanel = new DebugProfileSelectorPanel();

        debbugOptionPanel.add(debugProfileSelectorPanel, BorderLayout.PAGE_START);
    }
    private final IProfileSelectorModel profileSelectorModel = new IProfileSelectorModel() {

        @Override
        public List<String> getProfileNameList() {
            return debbugOptionModel.getProfileNameList();
        }

        @Override
        public String getProfileName() {
            return debbugOptionModel.getManager().getCurrentProfileName(getBranch());
        }

        @Override
        public void setProfileName(String name) {
            saveDebuggerPanel();
            update();
        }

        @Override
        public boolean removeProfile() {
            String currentProfileName = debbugOptionModel.getCurrentProfileName();
            String message = NbBundle.getMessage(BranchEditor.class, "RemoveProfile.Message") + " '" + currentProfileName + "'?";

            if (!StartupInfoManager.DEFAULT_PROFILE_NAME.equals(currentProfileName)
                    && DialogUtils.messageConfirmation(message)) {
                debbugOptionModel.removeProfile();
                return true;
            }
            return false;
        }

        @Override
        public String duplicateProfile() {
            ChooseProfileNameModalDisplayer displayer = new ChooseProfileNameModalDisplayer(debbugOptionModel);
            if (displayer.showModal()) {
                String newProfileName = displayer.getProfileName();
                saveDebuggerPanel();
                debbugOptionModel.duplicateProfile(newProfileName);
                return newProfileName;
            }
            return null;
        }
    };

    @Override
    public String getTitle() {
        return NbBundle.getMessage(BranchEditor.class, "BranchEditor-Title") + " '" + getRadixObject().getName() + "'";
    }

    public Branch getBranch() {
        return getRadixObject();
    }

    @Override
    public void update() {
        initializeDebuggerPanel();
    }

    @Override
    protected void apply() {
        //update branch's name
        // TODO: rename branch directory
        //branch.setName(branchNameValue.getText());
        applyDebuggerPanelChanges();
        distUriManager.apply();
        Branch branch = getBranch();
//        distManager.apply();

        try {
            branch.setTitle(txtTitle.getText());
            branch.save();
        } catch (IOException error) {
            DialogUtils.messageError(error);
        }
    }
    DebugOptionModel debbugOptionModel = new DebugOptionModel(getBranch());

    @Override
    public boolean open(OpenInfo openInfo) {
        debugProfileSelectorPanel.open(profileSelectorModel);
        distUriManager = new BaseDistUriManager(getBranch());

        ((BaseDistUriEditorPanel) distLayersUriPanel).open(distUriManager);

        update();
        checkInput();
        return super.open(openInfo);
    }

    private void checkInput() {
        if (RadixObjectsUtils.isCorrectName(branchNameValue.getText())) {
            branchNameValue.setForeground(java.awt.Color.BLACK);
            setComplete(true);
            errorMsgLabel.setVisible(false);
        } else {   //mark error
            branchNameValue.setForeground(java.awt.Color.RED);
            setComplete(false);
            errorMsgLabel.setVisible(true);
            errorMsgLabel.setText("Incorrect name");
        }
    }

    private void initializeDebuggerPanel() {
        String profileName = debugProfileSelectorPanel.getSelectedProfileName();

        debbugOptionModel.setCurrentProfile(profileName);

        StartupInfo serverInfo = debbugOptionModel.getCurrentProfile().getInfo(StartupInfo.EEnvironment.SERVER);
        StartupInfo explorerInfo = debbugOptionModel.getCurrentProfile().getInfo(StartupInfo.EEnvironment.EXPLORER);

        serverDebug.open(serverInfo);
        explorerDebug.open(explorerInfo);
    }

    private void saveDebuggerPanel() {
        serverDebug.save();
        explorerDebug.save();
    }

    private void applyDebuggerPanelChanges() {
        saveDebuggerPanel();

        debbugOptionModel.apply();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        releaseOptionsSplitter = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btAddGroup = new javax.swing.JButton();
        btRemoveGroup = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        groupTree = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        btConfigureNotification = new javax.swing.JButton();
        distLayersPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        typeValue = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        branchNameValue = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        baseDevelopmentLayerField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        sourceBranchValue = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        baseReleaseVersionValue = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lastReleaseVer = new javax.swing.JTextField();
        branchDirectoryValue = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        errorMsgLabel = new javax.swing.JLabel();
        distLayersUriPanel = new BaseDistUriEditorPanel();
        jLabel6 = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        debbugOptionPanel = new javax.swing.JPanel();
        tbDebug = new javax.swing.JTabbedPane();

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 758, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        releaseOptionsSplitter.setDividerLocation(250);
        releaseOptionsSplitter.setDividerSize(4);
        releaseOptionsSplitter.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.jPanel3.border.title"))); // NOI18N

        jToolBar1.setFloatable(false);

        org.openide.awt.Mnemonics.setLocalizedText(btAddGroup, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.btAddGroup.text")); // NOI18N
        btAddGroup.setFocusable(false);
        btAddGroup.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btAddGroup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btAddGroup);

        org.openide.awt.Mnemonics.setLocalizedText(btRemoveGroup, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.btRemoveGroup.text")); // NOI18N
        btRemoveGroup.setFocusable(false);
        btRemoveGroup.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btRemoveGroup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btRemoveGroup);

        jScrollPane1.setViewportView(groupTree);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );

        releaseOptionsSplitter.setRightComponent(jPanel3);

        jPanel2.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(btConfigureNotification, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.btConfigureNotification.text")); // NOI18N
        jPanel2.add(btConfigureNotification, java.awt.BorderLayout.SOUTH);

        distLayersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.distLayersPanel.border.title"))); // NOI18N

        jPanel4.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout distLayersPanelLayout = new javax.swing.GroupLayout(distLayersPanel);
        distLayersPanel.setLayout(distLayersPanelLayout);
        distLayersPanelLayout.setHorizontalGroup(
            distLayersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(distLayersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
                .addContainerGap())
        );
        distLayersPanelLayout.setVerticalGroup(
            distLayersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(distLayersPanelLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(distLayersPanel, java.awt.BorderLayout.CENTER);

        setMinimumSize(new java.awt.Dimension(460, 295));
        setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.jLabel8.text")); // NOI18N

        typeValue.setEditable(false);
        typeValue.setText(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.typeValue.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.jLabel1.text")); // NOI18N

        branchNameValue.setText(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.branchNameValue.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.jLabel2.text")); // NOI18N

        baseDevelopmentLayerField.setEditable(false);
        baseDevelopmentLayerField.setText(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.baseDevelopmentLayerField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.jLabel7.text")); // NOI18N

        sourceBranchValue.setEditable(false);
        sourceBranchValue.setText(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.sourceBranchValue.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.jLabel3.text")); // NOI18N

        baseReleaseVersionValue.setEditable(false);
        baseReleaseVersionValue.setText(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.baseReleaseVersionValue.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor-LastReleaseTip")); // NOI18N

        lastReleaseVer.setEditable(false);

        branchDirectoryValue.setEditable(false);
        branchDirectoryValue.setText(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.branchDirectoryValue.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.jLabel4.text")); // NOI18N

        errorMsgLabel.setForeground(java.awt.Color.red);
        org.openide.awt.Mnemonics.setLocalizedText(errorMsgLabel, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.errorMsgLabel.text")); // NOI18N

        distLayersUriPanel.setLayout(null);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.jLabel6.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lblTitle, org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.lblTitle.text")); // NOI18N

        txtTitle.setText(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.txtTitle.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(errorMsgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(lblTitle))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTitle)
                            .addComponent(branchDirectoryValue, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                            .addComponent(typeValue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                            .addComponent(lastReleaseVer, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                            .addComponent(baseReleaseVersionValue, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                            .addComponent(sourceBranchValue, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                            .addComponent(baseDevelopmentLayerField, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                            .addComponent(branchNameValue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                            .addComponent(distLayersUriPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(branchNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(baseDevelopmentLayerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sourceBranchValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(baseReleaseVersionValue)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lastReleaseVer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(branchDirectoryValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(distLayersUriPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(182, 182, 182)
                .addComponent(errorMsgLabel)
                .addContainerGap())
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        debbugOptionPanel.setLayout(new java.awt.BorderLayout());
        debbugOptionPanel.add(tbDebug, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(BranchEditor.class, "BranchEditor.debbugOptionPanel.TabConstraints.tabTitle"), debbugOptionPanel); // NOI18N

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField baseDevelopmentLayerField;
    private javax.swing.JTextField baseReleaseVersionValue;
    private javax.swing.JTextField branchDirectoryValue;
    private javax.swing.JTextField branchNameValue;
    private javax.swing.JButton btAddGroup;
    private javax.swing.JButton btConfigureNotification;
    private javax.swing.JButton btRemoveGroup;
    private javax.swing.JPanel debbugOptionPanel;
    private javax.swing.JPanel distLayersPanel;
    private javax.swing.JPanel distLayersUriPanel;
    private javax.swing.JLabel errorMsgLabel;
    private javax.swing.JTree groupTree;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField lastReleaseVer;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JSplitPane releaseOptionsSplitter;
    private javax.swing.JTextField sourceBranchValue;
    private javax.swing.JTabbedPane tbDebug;
    private javax.swing.JTextField txtTitle;
    private javax.swing.JTextField typeValue;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean isOpeningAfterNewObjectCreationRequired() {
        return false;
    }

    private void editNotification() {

    }

    private static final class DebugOptionModel implements ChooseProfileNameModalDisplayer.INameValidator {

        private final StartupInfoManager manager = StartupInfoManager.getInstance();
        private StartupInfoProfile currentProfile;
        private final Map<String, StartupInfoProfile> profiles = new HashMap<>();
        private final Set<String> removeProfiles = new HashSet<>();
        private final Set<String> addProfiles = new HashSet<>();
        private final Branch branch;

        public DebugOptionModel(Branch branch) {
            this.branch = branch;
        }

        @Override
        public boolean valid(String name) {
            if (name == null || name.isEmpty()) {
                return false;
            }
            List<String> profileNameList = getProfileNameList();
            if (profiles.containsKey(name) || profileNameList.contains(name)) {
                return false;
            }
            return !name.contains("*");
        }

        public StartupInfoProfile getCurrentProfile() {
            return currentProfile;
        }

        public String getCurrentProfileName() {
            return currentProfile.getName();
        }

        public void setCurrentProfile(String profileName) {

            currentProfile = profiles.get(profileName);
            if (currentProfile == null) {
                currentProfile = manager.getProfile(branch, profileName);
                profiles.put(profileName, currentProfile);
            }
        }

        public StartupInfoManager getManager() {
            return manager;
        }

        public void removeProfile() {
            String name = currentProfile.getName();
            if (addProfiles.contains(name)) {
                addProfiles.remove(name);
            } else {
                removeProfiles.add(name);
            }
            profiles.remove(name);
        }

        public void duplicateProfile(String newProfileName) {
            if (removeProfiles.contains(newProfileName)) {
                removeProfiles.remove(newProfileName);
            } else {
                addProfiles.add(newProfileName);
            }
            profiles.put(newProfileName, currentProfile.copy(newProfileName));
        }

        public void apply() {
            for (String profile : removeProfiles) {
                manager.removeProfile(branch, profile);
            }

            for (String profile : addProfiles) {
                manager.addProfile(profiles.get(profile));
                profiles.remove(profile);
            }

            for (StartupInfoProfile startupInfoProfile : profiles.values()) {
                startupInfoProfile.save();
            }

            manager.setCurrentProfile(branch, currentProfile.getName());
        }

        public List<String> getProfileNameList() {
            return manager.getProfileNameList(branch);
        }
    }

}
