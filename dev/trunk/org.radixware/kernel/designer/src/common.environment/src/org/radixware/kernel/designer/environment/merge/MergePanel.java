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
package org.radixware.kernel.designer.environment.merge;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.*;
import java.util.*;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.DialogDescriptor;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SvnEntryComparator;
import org.radixware.kernel.common.svn.SvnPathUtils;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.dialogs.text.TextEditor;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;

public class MergePanel extends JPanel {

    private final int index;
    private MergeTable table = null;
    private MergeTable table2 = null;
    private JSplitPane splitPane;
    private JButton btDiff = null;
    private JButton btMerge = null;
    private final AbstractMergeChangesOptions options;
    private MergeDialog dialog = null;
    private CopyMergeTable copyMergeTable;

    MergePanel(CopyMergeTable copyMergeTable, AbstractMergeChangesOptions options, int index) {
        this.copyMergeTable = copyMergeTable;
        this.options = options;
        this.index = index;
        init(options, index);
    }

    public void beforeShow() {
        splitPane.setDividerLocation(this.getWidth() / 2);
        splitPane.updateUI();
    }

    public void setDialog(MergeDialog dialog) {
        this.dialog = dialog;

        dialog.setTitle("Merge Changes Definition "
                + options.getNameByIndex(index)
                + " from \'"
                + options.getFromBranchShortName() + "\' to \'" + options.getToBranchShortName() + "\'");
    }

    private void collectRevisionsAndCreateTable(final AdsMergeChangesOptions options_, final int index_) {

        final List<MergeItemWrapper> fromItems = new ArrayList();
        final List<MergeItemWrapper> toItems = new ArrayList();
        final LastRevisions last = new LastRevisions();

        ProgressUtils.showProgressDialogAndRun(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isMlb = options_.getList().get(index_).isMlb();

                    if (isMlb && options_.getFromFormatVersion() >= 2 && options_.getToFormatVersion() < 2) {
                        collectRevisionListFromNewFormatToOld(last, options_, index_, fromItems, toItems);
                    } else if (isMlb && options_.getFromFormatVersion() < 2 && options_.getToFormatVersion() >= 2) {
                        collectRevisionListFromOldFormatToNew(last, options_, index_, fromItems, toItems);
                    } else {
                        collectRevisionListNew(last, options_, index_, fromItems, toItems);
                    }
                } catch (Exception ex) {
                    MergeUtils.messageError(ex);
                }
            }
        }, "Generate changes list");

        boolean isMlbWithDifferentFormat = options_.getFromFormatVersion() != options_.getToFormatVersion() && options_.getList().get(index_).isMlb();

        table = new MergeTable(this, options_, index_, fromItems, true, last.from, last.isEquel, isMlbWithDifferentFormat);
        table2 = new MergeTable(this, options_, index_, toItems, false, last.to, last.isEquel, isMlbWithDifferentFormat);

    }

    private void collectRevisionsDdsCreateTable(final DdsMergeChangesOptions options_, final int index_) {

        final List<MergeItemWrapper> fromItems = new ArrayList();
        final List<MergeItemWrapper> toItems = new ArrayList();
        final LastRevisions last = new LastRevisions();

        ProgressUtils.showProgressDialogAndRun(new Runnable() {
            @Override
            public void run() {
                try {
                    collectRevisionListNew(last, options_, index_, fromItems, toItems);

                } catch (Exception ex) {
                    MergeUtils.messageError(ex);
                }
            }
        }, "Generate changes list");

        boolean isMlbWithDifferentFormat = false;

        table = new MergeTable(this, options_, index_, fromItems, true, last.from, last.isEquel, isMlbWithDifferentFormat);
        table2 = new MergeTable(this, options_, index_, toItems, false, last.to, last.isEquel, isMlbWithDifferentFormat);

    }

    private void init(final AbstractMergeChangesOptions options_, final int index_) {

        JScrollPane jScrollPane = new JScrollPane();
        this.setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        toolBar.setBorderPainted(true);
        toolBar.setFloatable(false);

        btDiff = new JButton();
        toolBar.add(btDiff, 0);
        btDiff.setIcon(RadixWareIcons.SUBVERSION.DIFF.getIcon());
        btDiff.setToolTipText("Compare revisions");
        btDiff.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (table.getRowCount() == 0 || table2.getRowCount() == 0) {
                        return;
                    }

                    String s1 = table.getRevisionAsStr(table.getSelectedRow());
                    String s2 = table2.getRevisionAsStr(table2.getSelectedRow());

                    String t1 = table.getRowTitle(table.getSelectedRow());
                    String t2 = table2.getRowTitle(table2.getSelectedRow());

                    //                    String ss1 = table.getMlbRevisionAsStr(table.getSelectedRow());
                    //                    String ss2 = table2.getMlbRevisionAsStr(table2.getSelectedRow());
                    //
                    //                    //if (options.getList().get(index).isMlStringExists()) {
                    //                    if (!ss1.isEmpty() || !ss2.isEmpty()) {
                    ////                        List<String> sl1 = new ArrayList<>();
                    ////                        sl1.add(s1);
                    ////                        sl1.add(ss1);
                    ////                        List<String> sl2 = new ArrayList<>();
                    ////                        sl2.add(s2);
                    ////                        sl2.add(ss2);
                    ////
                    ////                        List<String> tabTitles = new ArrayList<>(2);
                    ////                        tabTitles.add("Definition");
                    ////                        tabTitles.add("Multilanguage string");
                    ////
                    ////                        List<String> titles1 = new ArrayList<>(2);
                    ////                        titles1.add(t1);
                    ////                        titles1.add(t1 + " - multilanguage string");
                    ////                        List<String> titles2 = new ArrayList<>(2);
                    ////                        titles2.add(t2);
                    ////                        titles2.add(t2 + " - multilanguage string");
                    ////
                    ////                        DiffManager.diff(sl1, sl2, tabTitles, titles1, titles2, "Revision Compare");
                    //                    } else
                    {
                        DiffManager.diff(s1, s2, t1, t2, "Revision Compare");
                    }
                } catch (RadixSvnException ex) {
                    MergeUtils.messageError(ex);
                }
            }
        });

        btMerge = new JButton();
        toolBar.add(btMerge, 1);
        btMerge.setIcon(RadixWareIcons.SUBVERSION.UPDATE.getIcon());
        btMerge.setToolTipText("Merge selected revisions");
        btMerge.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (options_ instanceof AdsMergeChangesOptions) {
                        doMergeAds((AdsMergeChangesOptions) options_);
                    } else {
                        doMergeDds((DdsMergeChangesOptions) options_);
                    }

                } catch (RadixSvnException ex) {
                    MergeUtils.messageError(ex);
                }
            }
        });

        this.add(toolBar, BorderLayout.NORTH);
        this.add(jScrollPane, BorderLayout.CENTER);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(28);

        jScrollPane.add(splitPane);
        jScrollPane.setViewportView(splitPane);

        if (options_ instanceof AdsMergeChangesOptions) {
            collectRevisionsAndCreateTable((AdsMergeChangesOptions) options_, index_);
        } else {
            collectRevisionsDdsCreateTable((DdsMergeChangesOptions) options_, index_);
        }

        JScrollPane jScrollPaneLeft = new JScrollPane();

        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BorderLayout());
        JLabel l = new JLabel("Source");
        panelLeft.setBackground(Color.red);
        panelLeft.add(l, BorderLayout.NORTH);
        panelLeft.add(table, BorderLayout.CENTER);
        jScrollPaneLeft.add(panelLeft);

        jScrollPaneLeft.setViewportView(table);

        JScrollPane jScrollPaneRight = new JScrollPane();
        jScrollPaneRight.add(table2);
        jScrollPaneRight.setViewportView(table2);

        splitPane.setLeftComponent(jScrollPaneLeft);
        splitPane.setRightComponent(jScrollPaneRight);

        table.repaint();
        table2.repaint();
        checkButtons();
    }
    private static final String tempSuffix = ".bak";

    private void doMergeAds(final AdsMergeChangesOptions adsOptions) throws ISvnFSClient.SvnFsClientException, RadixSvnException {
        List<Long> priorList = new ArrayList<>();
        List<Long> currList = new ArrayList<>();
        table.getSelectedRevisions(priorList, currList);

        if (currList.isEmpty()) {
            MergeUtils.messageError("There are no selected revisions.");
            return;
        }

        //options.
        AdsMergeChangesItemWrapper itemWrapper = adsOptions.getList().get(index);

        File f = itemWrapper.getSrcFile();
        File t = itemWrapper.getDestFile();

        //final String tempSuffix = ".bak";
        boolean mustUpdateFrom = false;
        if (itemWrapper.isMlb()
                && (adsOptions.getFromFormatVersion() >= 2 && adsOptions.getToFormatVersion() < 2
                || adsOptions.getFromFormatVersion() < 2 && adsOptions.getToFormatVersion() >= 2)) {
            File removeFile = new File(t.getAbsolutePath() + tempSuffix);

            //final String bakShortName = removeFile.getName();
            final String bakPath = itemWrapper.getToPath() + tempSuffix;

            SVN.update(adsOptions.getFsClient(), removeFile);

            if (removeFile.exists()) {
                SVN.delete(adsOptions.getFsClient(), removeFile, "Remove backup file \'" + removeFile.getName() + "\'");
                SVN.update(adsOptions.getFsClient(), removeFile);
            }

            List<Long> realPriorList = new ArrayList();
            List<String> priorAsStr = new ArrayList();

            List<Long> realCurrList = new ArrayList<>();
            List<String> currAsStr = new ArrayList();

            table.getSelectedRevisionsForConvertMlbAsXml(realPriorList, priorAsStr, realCurrList, currAsStr);
            priorList.clear();
            currList.clear();

            for (int i = 0; i < realPriorList.size(); i++) {
                {

                    SVNRepositoryAdapter.Editor editor = adsOptions.getRepository().createEditor("Recreate mlb file \'" + removeFile.getName() + "\' r" + String.valueOf(realPriorList.get(i)));
                    String pathDir = SvnPathUtils.getParentDir(itemWrapper.getToPath());
                    int cnt = editor.openDirs(pathDir);
                    if (i == 0) {
                        editor.appendFile(bakPath, priorAsStr.get(i).getBytes());
                    } else {
                        editor.modifyFile(bakPath, priorAsStr.get(i).getBytes());
                    }
                    editor.closeDirs(cnt);
                    editor.commit();
                    priorList.add(adsOptions.getRepository().getLatestRevision());
                }

                {
                    SVNRepositoryAdapter.Editor editor = adsOptions.getRepository().createEditor("Recreate mlb file \'" + removeFile.getName() + "\' r" + String.valueOf(realCurrList.get(i)));
                    String pathDir = SvnPathUtils.getParentDir(itemWrapper.getToPath());
                    int cnt = editor.openDirs(pathDir);
                    editor.modifyFile(bakPath, currAsStr.get(i).getBytes());
                    editor.closeDirs(cnt);
                    editor.commit();
                    currList.add(adsOptions.getRepository().getLatestRevision());
                }
            }

            SVN.update(adsOptions.getFsClient(), removeFile);

            long revision = adsOptions.getRepository().getLatestRevision();
            SVNRepositoryAdapter.Editor editor = adsOptions.getRepository().createEditor("Remove backup file \'" + removeFile.getName() + "\'");
            String pathDir = SvnPathUtils.getParentDir(itemWrapper.getToPath());
            int cnt = editor.openDirs(pathDir);
            editor.deleteEntry(bakPath, revision);
            editor.closeDirs(cnt);
            editor.commit();
            currList.add(adsOptions.getRepository().getLatestRevision());
            mustUpdateFrom = true;

            f = removeFile;

        }

        String contextBefore = readFileAsString(t.getAbsolutePath());

        boolean isFirst = true;
        boolean isMustShow = true;

        for (int i = 0; i < priorList.size(); i++) {
            try {
                SVN.merge(adsOptions.getFsClient(), f, priorList.get(i), currList.get(i), t);
            } catch (ISvnFSClient.SvnFsClientException ex) {
                MergeUtils.messageError(ex);
            }

            boolean isBad = SVN.isСonflictedSvnStatus(adsOptions.getFsClient(), t);

            if (isBad) {
                isMustShow = false;
                final String revert_to_original_version = "Revert to the original version";
                final String close_this_dialog = "Close this dialog";
                final String view_files = "View conflicted file";
                while (true) {

                    List<String> buttons = new ArrayList<>();
                    if (isFirst) {
                        buttons.add(revert_to_original_version);
                        buttons.add(view_files);
                        buttons.add(close_this_dialog);
                    } else {
                        buttons.add(revert_to_original_version);
                        //revert_to_prior_version = "Revert to " + String.valueOf(currList.get(i - 1)) + " revision";
                        //buttons.add(revert_to_prior_version);
                        buttons.add(view_files);
                        buttons.add(close_this_dialog);
                    }

                    final String result = DialogUtils.showCustomMessageBox(
                            "In applying the changes conflict was detected (changes from "
                            + String.valueOf(priorList.get(i))
                            + " to "
                            + String.valueOf(currList.get(i)) + " revision ).", buttons, DialogDescriptor.QUESTION_MESSAGE);
                    if (result == null || result.isEmpty() || result.equals(close_this_dialog)) {
                        if (DialogUtils.messageConfirmation("Are you sure you want to close the dialog? Conflicting files will not be reverted.")) {

                            if (mustUpdateFrom) {
                                SVN.update(adsOptions.getFsClient(), f);
                            }

                            dialog.close(false);
                            return;
                        }
                        continue;
                    }

                    if (result.equals(view_files)) {

                        String content;

                        content = readFileAsString(t.getAbsolutePath());
                        TextEditor.editTextModal(content,
                                "View revision",
                                "text/xml", false, new Object[]{DialogDescriptor.CLOSED_OPTION});
                        continue;

                    }
                    if (result.equals(revert_to_original_version)) {
                        try {
                            SVN.revert(adsOptions.getFsClient(), t);
                            if (mustUpdateFrom) {
                                SVN.update(adsOptions.getFsClient(), f);
                            }
                        } catch (ISvnFSClient.SvnFsClientException ex) {
                            MergeUtils.messageError(ex);
                        }
                    }

                    break;
                }
                break;
            }
            isFirst = false;
        }

        if (mustUpdateFrom) {
            SVN.update(adsOptions.getFsClient(), f);
        }

        String contentAfter;
        contentAfter = readFileAsString(t.getAbsolutePath());

        String content2 = contentAfter;

        boolean cycle = true;

        if (isMustShow) {
            while (cycle) {

                boolean mustStop;
                String title = adsOptions.getList().get(index).getDef().getQualifiedName();

                mustStop = !DiffManager.diff(contextBefore, content2, "Before merge - " + title, "After merge - " + title, "Merge Changes",
                        new Object[]{DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION});

                if (mustStop) {
                    try {
                        SVN.revert(adsOptions.getFsClient(), t);
                        return;
                    } catch (ISvnFSClient.SvnFsClientException ex) {
                        MergeUtils.messageError(ex);
                        return;
                    }
                }

                String mess = "";

                try {
                    {
                        XmlObject.Factory.parse(content2);
                        writeStringToFile(t, content2);
                    }
                } catch (DefinitionError ex) {
                    mess = "Definition error detected - " + ex.getMessage() + ".";
                } catch (XmlException ex) {
                    mess = "Xml parse error detected - " + ex.getMessage() + ".";
                } catch (IOException ex) {
                    mess = "IOException was detected - " + ex.getMessage() + ".";
                }

                if (!mess.isEmpty()) {
                    MergeUtils.messageError(mess);
                }

                cycle = false;
                String comment;
                while (true) {
                    //comment = DialogUtils.inputBox("Enter SVN comment");                                
                    comment = MergeCommitDialog.showCommitDialog(adsOptions.getList().get(index).getDef());
                    if (comment == null) {
                        //comment = "";
                        //if (DialogUtils.messageConfirmation("You are really want revert all changes?")) 
                        {
                            try {
                                SVN.revert(adsOptions.getFsClient(), t);
                                return;
                            } catch (ISvnFSClient.SvnFsClientException ex) {
                                MergeUtils.messageError(ex);
                                return;
                            }
                        }
                    } else {
                        break;
                    }
                }
                try {
                    //int l = 1;
                    SVN.commit(adsOptions.getFsClient(), t, comment);
                    if (mustUpdateFrom) {
                        SVN.update(adsOptions.getFsClient(), t);
                    }
                    dialog.close(false);

                    adsOptions.getList().get(index).setDone(true);
                    adsOptions.getList().get(index).setMayMerge(false);
                    adsOptions.getList().get(index).setSrcChangesLevel(SvnEntryComparator.NOT_MAY_COPY);

                    copyMergeTable.setValueAt(Boolean.TRUE, index, 4);
                    DefaultCellEditor be = (DefaultCellEditor) copyMergeTable.getColumnModel().getColumn(2).getCellEditor();
                    be.stopCellEditing();
                    be = (DefaultCellEditor) copyMergeTable.getColumnModel().getColumn(3).getCellEditor();
                    be.stopCellEditing();
                    copyMergeTable.repaint();
                    return;

                } catch (ISvnFSClient.SvnFsClientException ex) {
                    MergeUtils.messageError(ex);
                }
            }
        }
    }

    private void doMergeDds(final DdsMergeChangesOptions ddsOptions) throws ISvnFSClient.SvnFsClientException {
        List<Long> priorList = new ArrayList<>();
        List<Long> currList = new ArrayList<>();
        table.getSelectedRevisions(priorList, currList);

        if (currList.isEmpty()) {
            MergeUtils.messageError("There are no selected revisions.");
            return;
        }

        //options.
        DdsMergeChangesItemWrapper itemWrapper = ddsOptions.getList().get(index);

        File f = itemWrapper.getSrcFile();
        File t = itemWrapper.getDestFile();

        //final String tempSuffix = ".bak";
        boolean mustUpdateFrom = false;
//        if (itemWrapper.isMlb()
//                && (adsOptions.getFromFormatVersion() >= 2 && adsOptions.getToFormatVersion() < 2
//                || adsOptions.getFromFormatVersion() < 2 && adsOptions.getToFormatVersion() >= 2)) {
//            File removeFile = new File(t.getAbsolutePath() + tempSuffix);
//
//            //final String bakShortName = removeFile.getName();
//            final String bakPath = itemWrapper.getToPath() + tempSuffix;
//
//            SVN.update(adsOptions.getRepository(), removeFile);
//
//            if (removeFile.exists()) {
//                SVN.delete(adsOptions.getRepository(), removeFile, "Remove backup file \'" + removeFile.getName() + "\'");
//                SVN.update(adsOptions.getRepository(), removeFile);
//            }
//
//            List<Long> realPriorList = new ArrayList();
//            List<String> priorAsStr = new ArrayList();
//
//            List<Long> realCurrList = new ArrayList<>();
//            List<String> currAsStr = new ArrayList();
//
//            table.getSelectedRevisionsForConvertMlbAsXml(realPriorList, priorAsStr, realCurrList, currAsStr);
//            priorList.clear();
//            currList.clear();
//
//            for (int i = 0; i < realPriorList.size(); i++) {
//                {
//                    SVNEditor editor = new SVNEditor(adsOptions.getRepository(), "Recreate mlb file \'" + removeFile.getName() + "\' r" + String.valueOf(realPriorList.get(i)));
//                    String pathDir = SvnPathUtils.getParentDir(itemWrapper.getToPath());
//                    int cnt = editor.openDirs(pathDir);
//                    if (i == 0) {
//                        editor.appendFile(bakPath, priorAsStr.get(i).getBytes());
//                    } else {
//                        editor.modifyFile(bakPath, priorAsStr.get(i).getBytes());
//                    }
//                    editor.closeDirs(cnt);
//                    editor.commit();
//                    priorList.add(adsOptions.getRepository().getLatestRevision());
//                }
//
//                {
//                    SVNEditor editor = new SVNEditor(adsOptions.getRepository(), "Recreate mlb file \'" + removeFile.getName() + "\' r" + String.valueOf(realCurrList.get(i)));
//                    String pathDir = SvnPathUtils.getParentDir(itemWrapper.getToPath());
//                    int cnt = editor.openDirs(pathDir);
//                    editor.modifyFile(bakPath, currAsStr.get(i).getBytes());
//                    editor.closeDirs(cnt);
//                    editor.commit();
//                    currList.add(adsOptions.getRepository().getLatestRevision());
//                }
//            }
//
//            SVN.update(adsOptions.getRepository(), removeFile);
//
//            long revision = adsOptions.getRepository().getLatestRevision();
//            SVNEditor editor = new SVNEditor(adsOptions.getRepository(), "Remove backup file \'" + removeFile.getName() + "\'");
//            String pathDir = SvnPathUtils.getParentDir(itemWrapper.getToPath());
//            int cnt = editor.openDirs(pathDir);
//            editor.deleteEntry(bakPath, revision);
//            editor.closeDirs(cnt);
//            editor.commit();
//            currList.add(adsOptions.getRepository().getLatestRevision());
//            mustUpdateFrom = true;
//
//            f = removeFile;
//
//        }

        String contextBefore = readFileAsString(t.getAbsolutePath());

        boolean isFirst = true;
        boolean isMustShow = true;

        for (int i = 0; i < priorList.size(); i++) {
            try {
                SVN.merge(ddsOptions.getFsClient(), f, priorList.get(i), currList.get(i), t);
            } catch (ISvnFSClient.SvnFsClientException ex) {
                MergeUtils.messageError(ex);
            }

            boolean isBad = SVN.isСonflictedSvnStatus(ddsOptions.getFsClient(), t);

            if (isBad) {
                isMustShow = false;
                final String revert_to_original_version = "Revert to the original version";
                final String close_this_dialog = "Close this dialog";
                final String view_files = "View conflicted file";
                while (true) {

                    List<String> buttons = new ArrayList<>();
                    if (isFirst) {
                        buttons.add(revert_to_original_version);
                        buttons.add(view_files);
                        buttons.add(close_this_dialog);
                    } else {
                        buttons.add(revert_to_original_version);
                        //revert_to_prior_version = "Revert to " + String.valueOf(currList.get(i - 1)) + " revision";
                        //buttons.add(revert_to_prior_version);
                        buttons.add(view_files);
                        buttons.add(close_this_dialog);
                    }

                    final String result = DialogUtils.showCustomMessageBox(
                            "In applying the changes conflict was detected (changes from "
                            + String.valueOf(priorList.get(i))
                            + " to "
                            + String.valueOf(currList.get(i)) + " revision ).", buttons, DialogDescriptor.QUESTION_MESSAGE);
                    if (result == null || result.isEmpty() || result.equals(close_this_dialog)) {
                        if (DialogUtils.messageConfirmation("Are you sure you want to close the dialog? Conflicting files will not be reverted.")) {

                            if (mustUpdateFrom) {
                                SVN.update(ddsOptions.getFsClient(), f);
                            }

                            dialog.close(false);
                            return;
                        }
                        continue;
                    }

                    if (result.equals(view_files)) {

                        String content;

                        content = readFileAsString(t.getAbsolutePath());
                        TextEditor.editTextModal(content,
                                "View revision",
                                "text/xml", false, new Object[]{DialogDescriptor.CLOSED_OPTION});
                        continue;

                    }
                    if (result.equals(revert_to_original_version)) {
                        try {
                            SVN.revert(ddsOptions.getFsClient(), t);
                            if (mustUpdateFrom) {
                                SVN.update(ddsOptions.getFsClient(), f);
                            }
                        } catch (ISvnFSClient.SvnFsClientException ex) {
                            MergeUtils.messageError(ex);
                        }
                    }

                    break;
                }
                break;
            }
            isFirst = false;
        }

        if (mustUpdateFrom) {
            SVN.update(ddsOptions.getFsClient(), f);
        }

        String contentAfter;
        contentAfter = readFileAsString(t.getAbsolutePath());

        String content2 = contentAfter;

        boolean cycle = true;

        if (isMustShow) {
            while (cycle) {

                boolean mustStop;
                String title = ddsOptions.getList().get(index).getWrapperName();

                mustStop = !DiffManager.diff(contextBefore, content2, "Before merge - " + title, "After merge - " + title, "Merge Changes",
                        new Object[]{DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION});

                if (mustStop) {
                    try {
                        SVN.revert(ddsOptions.getFsClient(), t);
                        return;
                    } catch (ISvnFSClient.SvnFsClientException ex) {
                        MergeUtils.messageError(ex);
                        return;
                    }
                }

                String mess = "";

                try {
                    {
                        XmlObject.Factory.parse(content2);
                        writeStringToFile(t, content2);
                    }
                } catch (DefinitionError ex) {
                    mess = "Definition error detected - " + ex.getMessage() + ".";
                } catch (XmlException ex) {
                    mess = "Xml parse error detected - " + ex.getMessage() + ".";
                } catch (IOException ex) {
                    mess = "IOException was detected - " + ex.getMessage() + ".";
                }

                if (!mess.isEmpty()) {
                    MergeUtils.messageError(mess);
                }

                cycle = false;
                String comment = "";

                if (!ddsOptions.getList().get(index).isModel()) {
                    while (true) {
                        //comment = DialogUtils.inputBox("Enter SVN comment");                                
                        comment = MergeCommitDialog.showCommitDialog(ddsOptions.getList().get(index).getSrcDdsModule());
                        if (comment == null) {
                            //comment = "";
                            //if (DialogUtils.messageConfirmation("You are really want revert all changes?")) 
                            {
                                try {
                                    SVN.revert(ddsOptions.getFsClient(), t);
                                    return;
                                } catch (ISvnFSClient.SvnFsClientException ex) {
                                    MergeUtils.messageError(ex);
                                    return;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }

                try {

                    if (!ddsOptions.getList().get(index).isModel()) {
                        SVN.commit(ddsOptions.getFsClient(), t, comment);
                        if (mustUpdateFrom) {
                            SVN.update(ddsOptions.getFsClient(), t);
                        }
                    } else {
                        byte newModelXmlVersion[] = FileUtils.readBinaryFile(ddsOptions.getList().get(index).getDestFile());
                        ddsOptions.getList().get(index).setNewModelXmlVersion(newModelXmlVersion);
                        SVN.revert(ddsOptions.getFsClient(), t);
                    }

                    dialog.close(false);

                    //TODO
                    ddsOptions.getList().get(index).setDone();
//                    adsOptions.getList().get(index).setMayMerge(false);
//                    adsOptions.getList().get(index).setSrcChangesLevel(SvnEntryComparator.NOT_MAY_COPY);

                    copyMergeTable.setValueAt(Boolean.TRUE, index, 4);
                    DefaultCellEditor be = (DefaultCellEditor) copyMergeTable.getColumnModel().getColumn(2).getCellEditor();
                    be.stopCellEditing();
                    be = (DefaultCellEditor) copyMergeTable.getColumnModel().getColumn(3).getCellEditor();
                    be.stopCellEditing();
                    copyMergeTable.repaint();
                    return;

                } catch (ISvnFSClient.SvnFsClientException | IOException ex) {
                    MergeUtils.messageError(ex);
                }
            }
        }
    }

    public void checkButtons() {
        if (table.getRowCount() == 0 || table2.getRowCount() == 0) {
            btDiff.setEnabled(false);
        } else {
            btDiff.setEnabled(true);
        }
    }

    private class LastRevisions {

        boolean isEquel;
        long from;
        long to;
    }

    private static void writeStringToFile(File filePath, String content) throws IOException {
        try (BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), FileUtils.XML_ENCODING))) {
            out.write(content);
        }
    }

    private static String readFileAsString(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        }
        Long len = file.length();
        StringBuilder sb = new StringBuilder(len.intValue());
        try {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filePath), FileUtils.XML_ENCODING))) {
                        String s2;
                        boolean isFirst = true;
                        while ((s2 = in.readLine()) != null) {
                            if (isFirst) {
                                isFirst = false;
                            } else {
                                sb.append("\n");
                            }
                            sb.append(s2);
                        }
                    }
        } catch (IOException ex) {
            MergeUtils.messageError(ex);
        }
        return sb.toString();
    }

    private void collectRevisionListNew(MergePanel.LastRevisions last, AbstractMergeChangesOptions options,
            int index,
            List<MergeItemWrapper> fromItems,
            List<MergeItemWrapper> toItems) throws RadixSvnException, UnsupportedEncodingException {

        final int revisionQuantum = 10;

        List<MergeItemWrapper> fromWrapper = new ArrayList();
        List<MergeItemWrapper> toWrapper = new ArrayList();

//        List<MergeItemWrapper> delayFromWrapper = new ArrayList();
//        List<MergeItemWrapper> delayToWrapper = new ArrayList();
        boolean mustStopFrom = false;
        boolean mustStopTo = false;

        long lF = SVN.getDirLatestRevision(options.getRepository());
        long lT = lF;

        long lastFrom = -1;
        long lastTo = -1;

        int t = -1, f;

        while (!mustStopFrom || !mustStopTo) {
            List<String> commentsFrom_ = new ArrayList();
            List<String> authorsFrom_ = new ArrayList();
            List<Long> revisionsFrom_ = new ArrayList();
            List<Date> datesFrom_ = new ArrayList();

            List<String> commentsTo_ = new ArrayList();
            List<String> authorsTo_ = new ArrayList();
            List<Long> revisionsTo_ = new ArrayList();
            List<Date> datesTo_ = new ArrayList();

            if (!mustStopFrom) {
                SVN.getSvnLog(options.getRepository(), options.getFromPathByIndex(index), commentsFrom_, authorsFrom_, revisionsFrom_, datesFrom_, lF, 1, revisionQuantum);
                int sz = revisionsFrom_.size();
                if (!revisionsFrom_.isEmpty()) {
                    lF = revisionsFrom_.get(sz - 1);
                }
                if (revisionsFrom_.size() < revisionQuantum) {
                    mustStopFrom = true;
                } else if (sz > 0) {
                    revisionsFrom_.remove(sz - 1);
                }
            }

            if (!mustStopTo) {
                SVN.getSvnLog(options.getRepository(), options.getToPathByIndex(index), commentsTo_, authorsTo_, revisionsTo_, datesTo_, lT, 1, revisionQuantum);
                int sz = revisionsTo_.size();
                if (sz < revisionQuantum) {
                    mustStopTo = true;
                }
                if (!revisionsTo_.isEmpty()) {
                    lT = revisionsTo_.get(sz - 1);
                } else if (sz > 0) {
                    revisionsTo_.remove(sz - 1);
                }
            }

            List<byte[]> fromHashes_;
            if (revisionsFrom_.isEmpty()) {
                fromHashes_ = new ArrayList(0);
            } else {
                fromHashes_ = SvnEntryComparator.getFileHashes(options.getRepository(), options.getFromPathByIndex(index), revisionsFrom_);
            }

            List<byte[]> toHashes_;
            if (revisionsTo_.isEmpty()) {
                toHashes_ = new ArrayList(0);
            } else {
                toHashes_ = SvnEntryComparator.getFileHashes(options.getRepository(), options.getToPathByIndex(index), revisionsTo_);
            }

            for (int i = 0; i < revisionsFrom_.size(); i++) {
                MergeItemWrapper item = new MergeItemWrapper();
                item.revision = revisionsFrom_.get(i);
                item.author = authorsFrom_.get(i);
                item.comment = commentsFrom_.get(i);
                item.date = datesFrom_.get(i);
                item.hash = fromHashes_.get(i);
                fromWrapper.add(item);
            }

            for (int i = 0; i < revisionsTo_.size(); i++) {
                MergeItemWrapper item = new MergeItemWrapper();
                item.revision = revisionsTo_.get(i);
                item.author = authorsTo_.get(i);
                item.comment = commentsTo_.get(i);
                item.date = datesTo_.get(i);
                item.hash = toHashes_.get(i);
                toWrapper.add(item);
            }

            {

//
//                delayFromWrapper.clear();
//                delayToWrapper.clear();
                Collections.sort(fromWrapper);
                Collections.sort(toWrapper);

//                Collections.sort(fromWrapper);
//                Collections.sort(toWrapper);
            }

            l:
            for (int i = 0; i < toWrapper.size(); i++) {
                for (int j = 0; j < fromWrapper.size(); j++) {
                    if (Arrays.equals(toWrapper.get(i).hash, fromWrapper.get(j).hash)) {

                        lastFrom = SVN.getPriorRevision(options.getRepository(), options.getFromPathByIndex(index), fromWrapper.get(j).revision);
                        lastTo = SVN.getPriorRevision(options.getRepository(), options.getToPathByIndex(index), toWrapper.get(i).revision);

                        t = i + 1;
                        f = j + 1;

                        int n = toWrapper.size();
                        for (int r = 0; r < n - t; r++) {
                            toWrapper.remove(toWrapper.size() - 1);
                        }

                        int m = fromWrapper.size();
                        for (int r = 0; r < m - f; r++) {
                            fromWrapper.remove(fromWrapper.size() - 1);
                        }

                        break l;
                    }
                }
            }
            if (t != -1) {
                break;
            }
            boolean isMustCheck = false;

            if (mustStopFrom && mustStopTo) {

                if (isMustCheck) {
                    l:
                    for (int i = 0; i < toWrapper.size(); i++) {
                        for (int j = 0; j < fromWrapper.size(); j++) {
                            if (Arrays.equals(toWrapper.get(i).hash, fromWrapper.get(j).hash)) {

                                lastFrom = SVN.getPriorRevision(options.getRepository(), options.getFromPathByIndex(index), fromWrapper.get(j).revision);
                                lastTo = SVN.getPriorRevision(options.getRepository(), options.getToPathByIndex(index), toWrapper.get(i).revision);

                                t = i + 1;
                                f = j + 1;

                                int n = toWrapper.size();
                                for (int r = 0; r < n - t; r++) {
                                    toWrapper.remove(toWrapper.size() - 1);
                                }

                                int m = fromWrapper.size();
                                for (int r = 0; r < m - f; r++) {
                                    fromWrapper.remove(fromWrapper.size() - 1);
                                }

                                break l;
                            }
                        }
                    }
                }
                break;
            }
        }

        last.isEquel = t != -1;
        last.from = lastFrom;
        last.to = lastTo;
        fromItems.addAll(fromWrapper);
        toItems.addAll(toWrapper);
    }

    private void collectRevisionListFromNewFormatToOld(MergePanel.LastRevisions last, AdsMergeChangesOptions options,
            int index,
            List<MergeItemWrapper> fromItems,
            List<MergeItemWrapper> toItems) throws Exception {

        AdsMergeChangesItemWrapper item = options.getList().get(index);
        List<EIsoLanguage> lngList = options.getCommonLangList();

        List<Long> allFromRevisionList = new ArrayList();
        List<String> commentsList = new ArrayList();
        List<String> authorsList = new ArrayList();
        List<Long> revisionsList = new ArrayList();
        List<Date> datesList = new ArrayList();

        long lastRev = options.getRepository().getLatestRevision();

        for (EIsoLanguage lng : lngList) {
            String currFromPath = item.getFromLocalePath() + "/" + lng.getValue() + "/" + item.getMlbShortName();
            if (SVN.isExists(options.getRepository(), currFromPath, lastRev)) {

                commentsList.clear();
                authorsList.clear();
                revisionsList.clear();
                datesList.clear();

                SVN.getSvnLog(options.getRepository(), currFromPath, commentsList, authorsList, revisionsList, datesList, 1);
                for (int i = 0; i < revisionsList.size(); i++) {
                    long currRev = revisionsList.get(i);
                    if (!allFromRevisionList.contains(currRev)) {
                        allFromRevisionList.add(currRev);
                        MergeItemWrapper revisionItem = new MergeItemWrapper();
                        revisionItem.author = authorsList.get(i);
                        revisionItem.comment = commentsList.get(i);
                        revisionItem.date = datesList.get(i);
                        revisionItem.revision = currRev;
                        fromItems.add(revisionItem);
                    }
                }
            }
        }

        String toFileAsStr = FileUtils.readTextFile(item.getDestFile(), FileUtils.XML_ENCODING);

        Collections.sort(fromItems);

        for (MergeItemWrapper fromItem : fromItems) {
            List<String> dataAsXml = new ArrayList();
            for (EIsoLanguage lng : lngList) {
                String currFromPath = item.getFromLocalePath() + "/" + lng.getValue() + "/" + item.getMlbShortName();
                if (SVN.isExists(options.getRepository(), currFromPath, fromItem.revision)) {
                    dataAsXml.add(SVN.getFileAsStr(options.getRepository(), currFromPath, fromItem.revision));
                } else {
                    dataAsXml.add(null);
                }
            }
            AdsDefinitionDocument doc = MergeUtils.convertFromNewFormatToOld(lngList, dataAsXml, toFileAsStr, item.getMlbShortName());
            fromItem.xmlAsString = MergeUtils.saveToString(doc);
        }

        commentsList.clear();
        authorsList.clear();
        revisionsList.clear();
        datesList.clear();

        SVN.getSvnLog(options.getRepository(), item.getToPath(), commentsList, authorsList, revisionsList, datesList, 1);

        for (int i = 0; i < revisionsList.size(); i++) {
            long currRev = revisionsList.get(i);

            MergeItemWrapper revisionItem = new MergeItemWrapper();
            revisionItem.author = authorsList.get(i);
            revisionItem.comment = commentsList.get(i);
            revisionItem.date = datesList.get(i);
            revisionItem.revision = currRev;
            revisionItem.xmlAsString = SVN.getFileAsStr(options.getRepository(), item.getToPath(), currRev);
            toItems.add(revisionItem);
        }

        Collections.sort(toItems);

        l:
        for (int i = 0; i < fromItems.size(); i++) {
            MergeItemWrapper fromItem = fromItems.get(i);
            for (int j = 0; j < toItems.size(); j++) {
                if (fromItem.xmlAsString.equals(toItems.get(j).xmlAsString)) {
                    last.isEquel = true;
                    last.from = fromItem.revision;
                    last.to = toItems.get(j).revision;
                    for (int k = fromItems.size() - 1; k > i; k--) {
                        fromItems.remove(k);
                    }
                    for (int k = toItems.size() - 1; k > j; k--) {
                        toItems.remove(k);
                    }
                    break l;
                }
            }
        }
    }

    private void collectRevisionListFromOldFormatToNew(MergePanel.LastRevisions last, AdsMergeChangesOptions options,
            int index,
            List<MergeItemWrapper> fromItems,
            List<MergeItemWrapper> toItems) throws Exception {

        AdsMergeChangesItemWrapper item = options.getList().get(index);
        List<String> commentsList = new ArrayList();
        List<String> authorsList = new ArrayList();
        List<Long> revisionsList = new ArrayList();
        List<Date> datesList = new ArrayList();

        SVN.getSvnLog(options.getRepository(), item.getFromPath(), commentsList, authorsList, revisionsList, datesList, 1);

        for (int i = 0; i < revisionsList.size(); i++) {
            long currRev = revisionsList.get(i);

            MergeItemWrapper revisionItem = new MergeItemWrapper();
            revisionItem.author = authorsList.get(i);
            revisionItem.comment = commentsList.get(i);
            revisionItem.date = datesList.get(i);
            revisionItem.revision = currRev;

            String s = SVN.getFileAsStr(options.getRepository(), item.getFromPath(), currRev);

            AdsDefinitionDocument doc = MergeUtils.convertFromOldFormatToNew(item.getLanguage(), s, null);
            s = MergeUtils.saveToString(doc);
            revisionItem.xmlAsString = s;
            fromItems.add(revisionItem);
        }

        Collections.sort(fromItems);

        commentsList.clear();
        authorsList.clear();
        revisionsList.clear();
        datesList.clear();

        SVN.getSvnLog(options.getRepository(), item.getToPath(), commentsList, authorsList, revisionsList, datesList, 1);

        for (int i = 0; i < revisionsList.size(); i++) {
            long currRev = revisionsList.get(i);

            MergeItemWrapper revisionItem = new MergeItemWrapper();
            revisionItem.author = authorsList.get(i);
            revisionItem.comment = commentsList.get(i);
            revisionItem.date = datesList.get(i);
            revisionItem.revision = currRev;
            revisionItem.xmlAsString = SVN.getFileAsStr(options.getRepository(), item.getToPath(), currRev);
            toItems.add(revisionItem);
        }
        Collections.sort(toItems);

        l:
        for (int i = 0; i < fromItems.size(); i++) {
            MergeItemWrapper fromItem = fromItems.get(i);
            for (int j = 0; j < toItems.size(); j++) {
                if (fromItem.xmlAsString.equals(toItems.get(j).xmlAsString)) {
                    last.isEquel = true;
                    last.from = fromItem.revision;
                    last.to = toItems.get(j).revision;
                    for (int k = fromItems.size() - 1; k > i; k--) {
                        fromItems.remove(k);
                    }
                    for (int k = toItems.size() - 1; k > j; k--) {
                        toItems.remove(k);
                    }
                    break l;
                }
            }
        }
    }
}
