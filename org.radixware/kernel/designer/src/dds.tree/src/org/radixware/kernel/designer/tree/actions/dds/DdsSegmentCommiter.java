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

package org.radixware.kernel.designer.tree.actions.dds;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.modules.subversion.Subversion;
import org.openide.windows.IOColorLines;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.builder.check.common.Checker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.dialogs.db.DdsScriptUtils;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsAadcTransform;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.designer.subversion.util.RadixSvnUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.dds.editors.SqlModalEditor;
import org.radixware.kernel.designer.dds.editors.scripts.transform.DdsAadcTransformEditor;
import org.radixware.kernel.designer.dds.script.ScriptGenerator;
import org.radixware.kernel.designer.dds.script.ScriptGeneratorImpl;
import org.tigris.subversion.svnclientadapter.SVNClientException;


class DdsSegmentCommiter {

    private final List<DdsSegment> segments = new ArrayList(); // list of segments, zero - base (org.radixware.dds)

    public DdsSegmentCommiter(DdsSegment segment) {
        Layer layer = segment.getLayer();
        Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                DdsSegment segment = (DdsSegment) layer.getDds();
                segments.add(0, segment);
            }
        });
    }

    public boolean checkUpToDate(final RadixSvnUtils svn, InputOutput io) throws IOException, SVNClientException {
        if (svn == null) {
            return true;
        }
        for (DdsSegment segment : segments) {
            io.getOut().println("Check versioning status for '" + segment.getQualifiedName() + "'");

            final File segmentDir = segment.getDirectory();
            if (!svn.isUpToDate(segmentDir)) {
                final String message = "Versioning status of '" + segment.getQualifiedName() + "' is not up to date: SVN update required";
                io.getErr().println(message);
                DialogUtils.messageError(message);
                return false;
            }

            final File layerXmlFile = segment.getLayer().getFile();
            if (!svn.isUpToDate(layerXmlFile)) {
                final String fileFullName = layerXmlFile.getAbsolutePath();
                final String message = "Versioning status of '" + fileFullName + "' is not up to date: SVN update required";
                io.getErr().println(message);
                DialogUtils.messageError(message);
                return false;
            }
        }
        return true;
    }

    private boolean check(final InputOutput io) {
        for (DdsSegment segment : segments) {
            io.getOut().println("Check '" + segment.getQualifiedName() + "' for errors");

            final IProblemHandler problemHandler = new IProblemHandler() {
                @Override
                public void accept(RadixProblem problem) {
                    io.getErr().println(problem.getMessage());
                    RadixProblemRegistry.getDefault().accept(problem);
                }
            };


            final Checker checker = new Checker(problemHandler, new CheckOptions());
            if (!checker.check(Collections.singleton(segment))) {
                io.getErr().println("DDS segment '" + segment.getQualifiedName() + "' has errors");
                DialogUtils.messageError("Unable to switch DDS segment '" + segment.getQualifiedName() + "' to structure fixed state\n"
                        + "because it contains errors.");
                return false;
            }
        }
        return true;
    }

    private static boolean hasOwnModifiedModules(DdsSegment segment) throws IOException {
        for (DdsModule module : segment.getModules()) {
            final DdsModelDef modifiedModel = module.getModelManager().getModifiedModel();
            if (modifiedModel != null && modifiedModel.getModifierInfo().isOwn()) {
                return true;
            }
        }
        return false;
    }

//    private SvnSnapshot createSvnSnapshot(final RadixSvnUtils radixSvnUtils) throws IOException {
//        final SvnSnapshot snapshot = new SvnSnapshot(radixSvnUtils);
//        final List<File> scriptsDirs = new ArrayList<File>();
//
//        for (DdsSegment segment : segments) {
//            if (hasOwnModifiedModules(segment)) {
//                final File scriptsDir = segment.getScripts().getDirectory();
//                scriptsDirs.add(scriptsDir);
//            }
//        }
//
//        snapshot.makeSnapshot(scriptsDirs);
//        return snapshot;
//    }
    private static class SegmentModificationInfo {

        private String alterScript, compatibilityLog;
        private boolean backwardCompatible = false;
        private DdsScript ddsScript;
        private DdsAadcTransform transform;

        public String getAlterScript() {
            return alterScript;
        }

        public void setAlterScript(String script) {
            this.alterScript = script;
        }

        public boolean isBackwardCompatible() {
            return backwardCompatible;
        }

        public void setBackwardCompatible(boolean backwardCompatible) {
            this.backwardCompatible = backwardCompatible;
        }

        public DdsScript getDdsScript() {
            return ddsScript;
        }

        public void setDdsScript(DdsScript ddsScript) {
            this.ddsScript = ddsScript;
        }

        public DdsAadcTransform getTransform() {
            return transform;
        }

        public void setTransform(DdsAadcTransform transform) {
            this.transform = transform;
        }
        
        public boolean hasCompatibilityLog() {
            return compatibilityLog != null && !compatibilityLog.isEmpty();
        }
        
        public String getCompatibilityLog() {
            return compatibilityLog;
        }
        
        public void setCompatibilityLog(final String compatibilityLog) {
            this.compatibilityLog = compatibilityLog;
        }

        @Override
        public String toString() {
            return "SegmentModificationInfo{" + "alterScript=" + alterScript + ", compatibilityLog=" + compatibilityLog + ", backwardCompatible=" + backwardCompatible + '}';
        }
    }

    /**
     * Generate modification scripts for segments that has captured modules.
     *
     * @return not empty modification scripts for segments.
     */
    private Map<DdsSegment, SegmentModificationInfo> generateModificationInfo(final InputOutput io) throws IOException {
        final Map<DdsSegment, SegmentModificationInfo> segment2ModificationInfo = new HashMap<>();

        for (DdsSegment segment : segments) {
            if (hasOwnModifiedModules(segment)) {
                IOColorLines.println(io, "Generate upgrade SQL script for '" + segment.getQualifiedName() + "'", Color.GRAY);

                final ScriptGenerator scriptGenerator = ScriptGeneratorImpl.Factory.newAlterInstance(segment);
                final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
                final Collection<DdsDefinition> fixedDefinitions = new ArrayList<>();
                final Collection<DdsDefinition> modifiedDefinitions = new ArrayList<>();
                final String compatibilityLog;

                try(final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    scriptGenerator.generateCompatibilityLog(baos);
                    compatibilityLog = baos.toString();
                }
                
                scriptGenerator.generateModificationScript(cp);
                final String alterScript = cp.toString();
                
                if (alterScript != null && !alterScript.isEmpty()) {
                    final SegmentModificationInfo info = new SegmentModificationInfo();
                    
                    info.setAlterScript(alterScript);
                    info.setCompatibilityLog(compatibilityLog);
                    segment2ModificationInfo.put(segment, info);
                }
            }
        }

        return segment2ModificationInfo;
    }

    private static class SqlModalEditorCfg implements SqlModalEditor.ICfg {

        private SegmentModificationInfo modificationInfo;
        private final DdsSegment segment;
        private final JRadioButton chCompatibleBox = new JRadioButton();
        private final JRadioButton chIncompatibleBox = new JRadioButton();
        private final JButton compatibilityShow = new JButton();
        final JPanel additionalPanel = new JPanel();
        private final DdsAadcTransformEditor editor;
        private DdsAadcTransform transform;

        public SqlModalEditorCfg(final DdsSegment segment, final SegmentModificationInfo modificationInfo) {
            this.segment = segment;
            this.modificationInfo = modificationInfo;
            //this.chCompatibleBox.setSelected(modificationInfo.isBackwardCompatible());
            this.chCompatibleBox.setText("Compatible");
            this.chIncompatibleBox.setText("Incompatible");
            this.compatibilityShow.setText("Show compatibility log");
            
            if (modificationInfo.hasCompatibilityLog()) {
//                this.chCompatibleBox.setEnabled(false);   // RADIX-12999
                this.chIncompatibleBox.setSelected(true);
            }
            else {
                this.compatibilityShow.setEnabled(false);
            }
            this.chCompatibleBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chCompatibleBox.isSelected()) {
                        chIncompatibleBox.setSelected(false);
                    }

                }
            });
            this.chIncompatibleBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chIncompatibleBox.isSelected()) {
                        if (!DdsScriptUtils.showCompatibleWarning()){
                            chIncompatibleBox.setSelected(false);
                        } else {
                            chCompatibleBox.setSelected(false);
                        }
                    }
                }
            });
            this.compatibilityShow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final JTextArea area = new JTextArea(modificationInfo.getCompatibilityLog());
                    final JScrollPane pane = new JScrollPane(area);
                    
                    area.setEditable(false);
                    area.setRows(20);
                    pane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
                    JOptionPane.showMessageDialog(null,pane);
                }
            });
            
            final JPanel panel = new JPanel();
            panel.setBorder(new TitledBorder("Backward compatibility"));
            panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
            panel.add(chCompatibleBox);
            panel.add(chIncompatibleBox);
            panel.add(compatibilityShow);
            additionalPanel.setLayout(new BorderLayout(20, 5));
            additionalPanel.add(panel, BorderLayout.NORTH);
            editor = new DdsAadcTransformEditor();
            editor.setBorder(BorderFactory.createTitledBorder("AADC Transformations"));
            transform = new DdsAadcTransform();
            editor.open(segment.getLayer(), transform, true);
            additionalPanel.add(editor, BorderLayout.CENTER);
        }

        @Override
        public boolean canCloseEditor() {
            editor.check();
            return (chCompatibleBox.isSelected() || chIncompatibleBox.isSelected()) && editor.isOk();
        }

        @Override
        public String getSql() {
            return modificationInfo.getAlterScript();
        }

        @Override
        public String getTitle() {
            return "Commit '" + segment.getQualifiedName() + "'";
        }

        @Override
        public void setSql(String sql) {
            this.modificationInfo.setAlterScript(sql);
            if (chCompatibleBox.isSelected()) {
                this.modificationInfo.setBackwardCompatible(true);
            } else if (chIncompatibleBox.isSelected()) {
                this.modificationInfo.setBackwardCompatible(false);
            }
            this.modificationInfo.setTransform(transform);
        }

        @Override
        public JPanel getAdditionalPanel() {
            return additionalPanel;
        }

        @Override
        public void showClosingProblems() {
            String message;
            if (!chCompatibleBox.isSelected() && !chIncompatibleBox.isSelected()) {
                message = "One of script compatibility options must be choosen";
            } else {
                message = editor.getMessage();
            }
            DialogUtils.messageError(message);
        }
    }

    private boolean editModificationInfo(Map<DdsSegment, SegmentModificationInfo> segment2ModificationInfo) {
        // use iterator for remove posibility
        final Iterator<Map.Entry<DdsSegment, SegmentModificationInfo>> iterator = segment2ModificationInfo.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<DdsSegment, SegmentModificationInfo> entry = iterator.next();
            final DdsSegment segment = entry.getKey();
            final SegmentModificationInfo info = entry.getValue();
            final SqlModalEditorCfg cfg = new SqlModalEditorCfg(segment, info);
            if (SqlModalEditor.editSql(cfg)) {
                final String newAlterScript = cfg.getSql();
                if (newAlterScript != null && !newAlterScript.isEmpty()) {
                    info.setAlterScript(newAlterScript);
                } else {
                    iterator.remove();
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Generate installation scripts for segments that has captured modules.
     *
     * @return installation scripts for segments, script can be empty.
     */
    private Map<DdsSegment, String> generateInstallationScriptContents(final InputOutput io) throws IOException {
        final Map<DdsSegment, String> segment2InstallationScriptContents = new HashMap<>();

        for (DdsSegment segment : segments) {
            if (hasOwnModifiedModules(segment)) {
                IOColorLines.println(io, "Generate installation SQL script for '" + segment.getQualifiedName() + "'", Color.GRAY);
                final ScriptGenerator scriptGenerator = ScriptGeneratorImpl.Factory.newCreationInstance(segment);
                final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
                scriptGenerator.generateModificationScript(cp);
                final String installScript = cp.toString();
                segment2InstallationScriptContents.put(segment, installScript);
            }
        }

        return segment2InstallationScriptContents;
    }

    /**
     * Update script.xml files for segments that has new update SQL files.
     */
    private void updateScriptsXmlFiles(final Map<DdsSegment, SegmentModificationInfo> segment2ModificationInfo, InputOutput io) throws IOException {
        for (DdsSegment segment : segments) { // safe order
            final SegmentModificationInfo modificationInfo = segment2ModificationInfo.get(segment);
            if (modificationInfo != null) {
                final DdsScripts scripts = segment.getScripts();
                io.getOut().println("Update " + scripts.getFile().getAbsolutePath());
                final DdsScript updateScript = modificationInfo.getDdsScript();
                final boolean isBackwardCompatible = modificationInfo.isBackwardCompatible();
                scripts.getUpdatesInfo().registerNew(updateScript, isBackwardCompatible, modificationInfo.getTransform(), null);
                scripts.save();
            }
        }
    }

    private void switchToFixedState(final InputOutput io) throws IOException {
        for (DdsSegment segment : segments) {
            for (DdsModule module : segment.getModules()) {
                final DdsModelDef modifiedModel = module.getModelManager().getModifiedModel();
                if (modifiedModel != null && modifiedModel.getModifierInfo().isOwn()) {
                    io.getOut().println("Switch '" + module.getQualifiedName() + "' to fixed state");
                    module.getModelManager().switchModelToFixedState();
                }
            }
        }
    }

    private void showScript(DdsScript script, final InputOutput io) {
        final File file = script.getFile();
        io.getOut().println("Opening: " + file.getAbsolutePath());
        DialogUtils.editFile(file);
    }

    public void commit(final ProgressHandle progressHandle) throws IOException, SVNClientException {
        progressHandle.switchToDeterminate(10);
        final InputOutput io = IOProvider.getDefault().getIO("Fix DDS Structure", false);
        io.getOut().reset();
        io.select();

        final Branch branch = segments.get(0).getLayer().getBranch();

        final RadixSvnUtils svn = RadixSvnUtils.Factory.newInstance(branch);

        // check that svn status is up to date
        if (!checkUpToDate(svn, io)) {
            return;
        }
        progressHandle.progress(1);

        // check segments for errors
        if (!check(io)) {
            return;
        }
        progressHandle.progress(2);

        // Senerate scripts in memory. Before, because it is more safe if exception.
        final Map<DdsSegment, SegmentModificationInfo> segment2ModificationInfo = generateModificationInfo(io);

        // Edit modification scripts in memory
        if (!editModificationInfo(segment2ModificationInfo)) {
            return;
        }
        progressHandle.progress(3);

        final Map<DdsSegment, String> segment2InstallationScriptContent = generateInstallationScriptContents(io);
        progressHandle.progress(4);

        switchToFixedState(io);
        //final SvnSnapshot svnSnapshot = createSvnSnapshot(svn);
        progressHandle.progress(5);

        // boolean successful = false;

        // RADIX-6977. Update directory structure of scripts
        for (final DdsSegment segment : segments) {
            if (segment.isReadOnly()) {
                continue;
            }
            if (DdsScriptUpdater.isUpgradeRequired(segment.getScripts().getDbScripts())) {
                io.getOut().println("Update directory structure of scripts for layer " + segment.getLayer().getURI());

                final DdsScriptUpdater updater = new DdsScriptUpdater(segment.getScripts().getDbScripts());
                updater.upgrade();
            }
        }

        // try {
        // save modification files
        for (Map.Entry<DdsSegment, SegmentModificationInfo> entry : segment2ModificationInfo.entrySet()) {
            final DdsSegment segment = entry.getKey();
            final SegmentModificationInfo info = entry.getValue();
            final DdsScript modificationScript = segment.getScripts().getDbScripts().getUpgradeScripts().addNew(false);
            io.getOut().println("Save " + modificationScript.getFile().getAbsolutePath());
            final String alterScript = info.getAlterScript();
            modificationScript.setContent(alterScript);
            info.setDdsScript(modificationScript);
        }
        progressHandle.progress(6);

        // save installation files
        for (Map.Entry<DdsSegment, String> entry : segment2InstallationScriptContent.entrySet()) {
            final DdsSegment segment = entry.getKey();
            final String installScriptContent = entry.getValue();
            final DdsScript installScript = segment.getScripts().getDbScripts().getInstallScript();
            io.getOut().println("Save " + installScript.getFile().getAbsolutePath());
            installScript.setContent(installScriptContent);
        }
        progressHandle.progress(7);

        // update scripts.xml
        updateScriptsXmlFiles(segment2ModificationInfo, io);
        progressHandle.progress(8);

        if (svn != null) {
            final List<File> files = new ArrayList<>();
            for (DdsSegment segment : segments) {
                final File segmentDir = segment.getDirectory();
                files.add(segmentDir);
            }
            final File[] filesAsArray = files.toArray(new File[0]);

            final org.netbeans.modules.subversion.util.Context context = new org.netbeans.modules.subversion.util.Context(filesAsArray);
            Subversion.getInstance().getStatusCache().refreshCached(context);
            //org.netbeans.modules.subversion.ui.commit.CommitAction.commit("Commit DDS", context, true);
            org.radixware.kernel.designer.subversion.ui.commit.CommitAction.commit("Commit DDS", context, true, false);

            //svnSnapshot.commit("Commit DDS", io);
        }
        progressHandle.progress(9);

        // successful = true;
        // } finally {
//            if (!successful) {
//                if (svn != null) {
//                    svnSnapshot.rollback(io);
//                }
//            }
        //  }

        //progressHandle.progress(10);

        // show scripts
        for (Map.Entry<DdsSegment, SegmentModificationInfo> entry : segment2ModificationInfo.entrySet()) {
            showScript(entry.getValue().getDdsScript(), io);
        }

        IOColorLines.println(io, "SUCCESSFUL", Color.GREEN.darker().darker());
    }
}
