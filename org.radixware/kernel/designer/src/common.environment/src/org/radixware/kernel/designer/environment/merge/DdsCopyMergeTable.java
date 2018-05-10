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

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.openide.NotifyDescriptor;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SvnPathUtils;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.dialogs.commitpanel.MicroCommitPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import static org.radixware.kernel.designer.environment.merge.MergeUtils.getTableAsStr;


public class DdsCopyMergeTable extends CopyMergeTable {

    DdsMergeChangesOptions options;
    List<DdsMergeChangesItemWrapper> list;

    public void open(final DdsMergeChangesOptions options) {
        this.options = options;

        DefaultTableModel model = (DefaultTableModel) this.getModel();

        list = options.getList();

        model.setRowCount(list.size());

        {
            for (int i = 0; i < list.size(); i++) {
                model.setValueAt(list.get(i).getWrapperName(), i, 0);
                model.setValueAt(list.get(i).getRelativeFilePath(), i, 1);
            }
        }

        setColumnsWidth();

        if (options.getIncorrectFiles() != null && !options.getIncorrectFiles().isEmpty()) {
            MergeUtils.messageError("Some modules have not been added, since SVN status of this file(s) is incorrect, or structure modules is not commited:\n" + filesListToString(options.getIncorrectFiles()));
        }

    }

    @Override
    protected Icon getIconByRow(final int row) {
        return list == null ? null : list.get(row).getIcon();
    }

    private static String filesListToString(final List<File> files) {
        final StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        for (File file : files) {
            if (isFirst) {
                isFirst = false;
            } else {
                stringBuilder.append(",\n");
            }
            stringBuilder.append(file.getAbsolutePath());
        }
        return stringBuilder.toString();
    }

    @Override
    protected boolean cellEditable(final int rowIndex, final int columnIndex) {

        if (columnIndex == 2) {
            return list.get(rowIndex).isMayCopy();
        } else if (columnIndex == 3) {
            return list.get(rowIndex).isMayMerge();
        }

        return columnIndex != 4;
    }

    @Override
    protected boolean mayCopy(final int rowIndex) {
        return list.get(rowIndex).isMayCopy();
    }

    @Override
    protected boolean mayMerge(final int rowIndex) {
        return list.get(rowIndex).isMayMerge();
    }

    @Override
    protected boolean copy(final int index) throws Exception {
        if (!list.get(index).isMayCopy()) {
            return false;
        }

        long rev = options.getLastRevision();

        if (list.get(index).getSrcChangesLevel() > 1) {
            List<String> comments = new ArrayList();
            List<String> autors = new ArrayList();
            List<Long> revisions = new ArrayList();
            List<Date> dates = new ArrayList();

            SVN.getSvnLog(options.getRepository(), list.get(index).getFromPath(), comments, autors, revisions, dates, rev, 1, list.get(index).getSrcChangesLevel());
            if (!DialogUtils.messageConfirmation("<html><br>In source branch found multiple changes. Continue?<br><br> " + getTableAsStr(comments, autors, revisions, dates) + "</html>", NotifyDescriptor.WARNING_MESSAGE)) {
                return false;
            }
        }

        rev = options.getLastRevision();

        SVNRepositoryAdapter repository = options.getRepository();

        String toPath = list.get(index).getToPath();
        String fromPath = list.get(index).getFromPath();

        String comment;// = "Copy from '" + options.getFromBranchShortName()  + "' revision " + String.valueOf(fromRev);            
        //list.get(index).getDef().getQualifiedName()
        //comment = "";//RADIX-6533
        comment = MicroCommitPanel.getLastCommitMessage();

        boolean canUseEditor = false;

        if (!list.get(index).getDestFile().getParentFile().exists()) {
            canUseEditor = true;
        }
        final boolean isModel = list.get(index).isModel();
        if (isModel && list.get(index).getDestFile().exists()) {
        } else {
            canUseEditor = true;
        }

        if (canUseEditor) {
            comment = MergeCommitDialog.showCommitDialog("Copy " + list.get(index).getWrapperName(), comment);

            if (comment == null) {
                return false;
            }
        }

        boolean toFileExists = SVN.isFileExists(repository, toPath);

        byte buf[] = null;

        if (list.get(index).getXmlStringDataEmulator() != null) {
            buf = list.get(index).getXmlStringDataEmulator().getBytes(FileUtils.XML_ENCODING);
        } else if (toFileExists) {
            SvnEntry dirEntry1 = repository.info(fromPath, rev);
            Long fileSize = dirEntry1.getSize();
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(fileSize.intValue())) {
                repository.getFile(fromPath, rev, null, outputStream);
                buf = outputStream.toByteArray();
                outputStream.close();
            }
        }

        List<File> updateFiles = new ArrayList();
        SVNRepositoryAdapter.Editor editor;// = null;

        editor = repository.createEditor(comment);

        if (!toFileExists) {
            List<String> added = new ArrayList();
            checkParentDirs(editor, index, added, updateFiles);
        }

        if (isModel && list.get(index).getDestFile().exists()) {
            list.get(index).setNewModelXmlVersion(buf);
        } else {

            if (list.get(index).getXmlStringDataEmulator() != null) {

                int cnt = editor.openDirs(SvnPathUtils.getParentDir(toPath));
                if (!toFileExists) {
                    editor.appendFile(list.get(index).getToPath(), buf);
                } else {
                    editor.modifyFile(list.get(index).getToPath(), buf);
                }
                editor.closeDirs(cnt);
                updateFiles.add(list.get(index).getDestFile());

            } else {

                int cnt = editor.openDirs(SvnPathUtils.getParentDir(toPath));
                if (!toFileExists) {
                    editor.copyFile(list.get(index).getFromPath(), toPath, rev);
                } else {
                    editor.modifyFile(list.get(index).getToPath(), buf);
                }
                editor.closeDirs(cnt);
                updateFiles.add(list.get(index).getDestFile());

            }
        }

        if (canUseEditor) {

            editor.commit();

            try {
                for (File updateFile : updateFiles) {
                    SVN.update(options.getFsClient(), updateFile);
                }
            } catch (Exception ex) {
                MergeUtils.messageError(ex);
            }
        } else {
            editor.cancel();
        }

        list.get(index).setMayCopy(false);
        list.get(index).setMayMerge(false);
        list.get(index).setEquals(true);
        list.get(index).setDone();
        this.setValueAt(true, index, 4);
        ButtonEditor be = (ButtonEditor) this.getColumnModel().getColumn(2).getCellEditor();
        be.stopCellEditing();
        this.repaint();
        return true;
    }

    @Override
    protected boolean merge(int index) {

        final MergeDialog dialog = new MergeDialog(new MergePanel(this, options, index));
        dialog.showModal();

        return true;
    }

    @Override
    protected boolean isDefaultBackgroundColor(final int index) {
        boolean flag = true;
        DdsModule lastDef = null;
        for (int i = 0; i <= index; i++) {
            DdsModule def = list.get(i).getSrcDdsModule();
            if (lastDef != def) {
                flag = !flag;
                lastDef = def;
            }
        }
        return flag;
    }

    @Override
    protected Color getCurrForegroundColor(final int index) {
        final boolean destExists = list.get(index).getDestFile().exists();
        final boolean srcExists = list.get(index).getSrcFile().exists();

        if (!srcExists && destExists) {
            return (new Color(155, 0, 0));
        } else if (!destExists) {
            return (new Color(0, 155, 0));
        } else if (!list.get(index).isEqual()) {
            return (Color.BLUE);
        } else {
            return (UIManager.getColor("Button.foreground"));
        }
    }

    void checkParentDirs(SVNRepositoryAdapter.Editor editor, int index, List<String> added, List<File> updateFiles) throws RadixSvnException, IOException, Exception {
        File toFile = list.get(index).getDestFile();

        if (list.get(index).isMlb()) {
            //File toLngFile = toFile.getParentFile();
            String toFileEmulator = list.get(index).getToPath();
           // String toLngFileEmulator = SvnPathUtils.getParentDir(toFileEmulator);

            //if (!toLngFile.exists() && !added.contains(toLngFileEmulator)) 
            {

                String toLocaleFileEmulator = SvnPathUtils.getParentDir(toFileEmulator);

                File toLocaleFile = toFile.getParentFile();
                if (!toLocaleFile.exists() && !added.contains(toLocaleFileEmulator)) {

                    String toModuleFileEmulator = SvnPathUtils.getParentDir(toLocaleFileEmulator);
                    File toModuleFile = toLocaleFile.getParentFile();
                    if (!toModuleFile.exists() && !added.contains(toModuleFileEmulator)) {//create module

                        int cnt = editor.openDirs(SvnPathUtils.getParentDir(toModuleFileEmulator));
                        editor.appendDir(toModuleFileEmulator);
                        editor.closeDirs(cnt);
                        updateFiles.add(list.get(index).getDestFile().getParentFile().getParentFile());

                        cnt = editor.openDirs(toModuleFileEmulator);

                        String fromModuleFile = list.get(index).getSrcFile().getParentFile().getParentFile().getAbsolutePath() + "/module.xml";
                        try (FileInputStream is = new FileInputStream(fromModuleFile)) {
                            editor.appendFile(toModuleFileEmulator + "/module.xml", is);
                        }
                        editor.closeDirs(cnt);

                        added.add(toModuleFileEmulator);

                    }
                    int cnt = editor.openDirs(SvnPathUtils.getParentDir(toLocaleFileEmulator));
                    editor.appendDir(toLocaleFileEmulator);
                    editor.closeDirs(cnt);
                    updateFiles.add(list.get(index).getDestFile().getParentFile());
                    added.add(toLocaleFileEmulator);
                }
                //int cnt = editor.openDirs(SvnPathUtils.getParentDir(toLngFileEmulator));
                //editor.appendDir(toLngFileEmulator);
                //editor.closeDirs(cnt);
                //updateFiles.add(list.get(index).getDestFile().getParentFile());
                //added.add(toLngFileEmulator);

            }

        } else {
            //File toSrcFile = toFile.getParentFile();
            String toFileEmulator = list.get(index).getToPath();
            //String toSrcFileEmulator = SvnPathUtils.getParentDir(toFileEmulator);
            //if (!toSrcFile.exists() && !added.contains(toSrcFileEmulator)) 
            {//create src or locale

                File toModuleFile = toFile.getParentFile();
                String toModuleFileEmulator = SvnPathUtils.getParentDir(toFileEmulator);

                if (!toModuleFile.exists() && !added.contains(toModuleFileEmulator)) {//create module

                    int cnt = editor.openDirs(SvnPathUtils.getParentDir(toModuleFileEmulator));
                    editor.appendDir(toModuleFileEmulator);
                    editor.closeDirs(cnt);
                    updateFiles.add(list.get(index).getDestFile().getParentFile());

                    cnt = editor.openDirs(toModuleFileEmulator);

                    String fromModuleFile = list.get(index).getSrcFile().getParentFile().getAbsolutePath() + "/module.xml";
                    try (FileInputStream is = new FileInputStream(fromModuleFile)) {
                        editor.appendFile(toModuleFileEmulator + "/module.xml", is);
                    }

                    editor.closeDirs(cnt);
                    added.add(toModuleFileEmulator);

                }
                //int cnt = editor.openDirs(SvnPathUtils.getParentDir(toSrcFileEmulator));
                //editor.appendDir(toSrcFileEmulator);
                //editor.closeDirs(cnt);
                //updateFiles.add(list.get(index).getDestFile().getParentFile());
                //added.add(toSrcFileEmulator);
            }
        }
    }

    @Override
    protected void copyAll() throws Exception {
        try {
            List<Long> revisionNumbers = new ArrayList();
            SVNRepositoryAdapter repository = options.getRepository();

            long rev;
            try {
                rev = repository.getLatestRevision();
            } catch (RadixSvnException ex) {//SVN kit bug fix
                rev = repository.getLatestRevision();
            }

            List<Boolean> exists = new ArrayList(list.size());
            List<byte[]> changedData = new ArrayList(list.size());

            List<File> destFiles = new ArrayList(list.size());

            StringBuilder sb = new StringBuilder();
            boolean isFirst = true;
            for (int index = 0; index < getRowCount(); index++) {
                if (list.get(index).getSrcChangesLevel() > 1) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append(list.get(index).getWrapperName()).append("(").append(list.get(index).getSrcFile().getName()).append(")");
                    if (index != 0 && (index % 5) == 0) {
                        sb.append("\n");
                        isFirst = true;
                    }
                }
            }
            String messAsStr = sb.toString();
            if (!messAsStr.isEmpty()) {
                if (!DialogUtils.messageConfirmation("In source files found multiple changes:\n" + messAsStr + "\n Continue?", NotifyDescriptor.WARNING_MESSAGE)) {
                    return;
                }

            }

            for (int index = 0; index < getRowCount(); index++) {
                if (!list.get(index).isMayCopy()) {
                    exists.add(false);
                    continue;
                }

                long fromRev = SVN.getFileRevision(options.getFsClient(), list.get(index).getSrcFile());

                String fromPath = list.get(index).getFromPath();
                if (!revisionNumbers.contains(fromRev)) {
                    revisionNumbers.add(fromRev);
                }
                String toPath = list.get(index).getToPath();
                boolean toFileExists = SVN.isFileExists(repository, toPath);
                if (list.get(index).getXmlStringDataEmulator() != null) {
                    changedData.add(list.get(index).getXmlStringDataEmulator().getBytes(FileUtils.XML_ENCODING));
                } else if (toFileExists) {
                    SvnEntry dirEntry1 = repository.info(fromPath, rev);
                    Long fileSize = dirEntry1.getSize();

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(fileSize.intValue());
                    repository.getFile(fromPath, rev, null, outputStream);
                    byte buf[] = outputStream.toByteArray();
                    changedData.add(buf);
                }
                exists.add(toFileExists);

            }
            if (revisionNumbers.isEmpty()) {
                return;
            }
            Long arr[] = new Long[revisionNumbers.size()];
            revisionNumbers.toArray(arr);
            String commentPostfix = " ";
            String comment = MicroCommitPanel.getLastCommitMessage();

            boolean canUseEditor = false;

            for (int index = 0; index < getRowCount(); index++) {

                if (!list.get(index).getDestFile().getParentFile().exists()) {
                    canUseEditor = true;
                    break;
                }

                if (!list.get(index).isMayCopy()) {
                    continue;
                }

                final boolean isModel = list.get(index).isModel();
                if (isModel && list.get(index).getDestFile().exists()) {

                } else {
                    canUseEditor = true;
                    break;
                }
            }

            if (canUseEditor) {
                comment += commentPostfix;
                comment = MergeCommitDialog.showCommitDialog(MicroCommitPanel.getLastCommitMessage(), comment);

                if (comment == null) {
                    return;
                }
            }

            final SVNRepositoryAdapter.Editor editor = repository.createEditor(comment);
            int j = 0;

            List<String> added = new ArrayList();

            for (int index = 0; index < getRowCount(); index++) {

                if (!list.get(index).getDestFile().exists()) {
                    checkParentDirs(editor, index, added, destFiles);
                }

                if (!list.get(index).isMayCopy()) {
                    continue;
                }

                int cnt;
                String toPath = list.get(index).getToPath();

                final boolean isModel = list.get(index).isModel();
                if (isModel && list.get(index).getDestFile().exists()) {
                    list.get(index).setNewModelXmlVersion(changedData.get(j++));
                } else {
                    if (isModel && !list.get(index).getDestFile().exists()) {
                        list.get(index).setAsNewModel();
                    }
                    if (list.get(index).getXmlStringDataEmulator() != null) {
                        if (!exists.get(index)) {
                            cnt = editor.openDirs(SvnPathUtils.getParentDir(toPath));
                            editor.appendFile(list.get(index).getToPath(), changedData.get(j++));
                            editor.closeDirs(cnt);
                        } else {
                            cnt = editor.openDirs(SvnPathUtils.getParentDir(toPath));
                            editor.modifyFile(list.get(index).getToPath(), changedData.get(j++));
                            editor.closeDirs(cnt);
                        }
                    } else {
                        if (!exists.get(index)) {
                            cnt = editor.openDirs(SvnPathUtils.getParentDir(toPath));
                            editor.copyFile(list.get(index).getFromPath(), toPath, rev);
                            editor.closeDirs(cnt);
                        } else {
                            cnt = editor.openDirs(SvnPathUtils.getParentDir(toPath));
                            editor.modifyFile(list.get(index).getToPath(), changedData.get(j++));
                            editor.closeDirs(cnt);
                        }
                    }
                    destFiles.add(list.get(index).getDestFile());
                }
                list.get(index).setDone();
                this.setValueAt(true, index, 4);
            }

            if (canUseEditor) {
                editor.commit();

                for (File f : destFiles) {
                    try {
                        SVN.update(options.getFsClient(), f);
                    } catch (Exception ex) {
                        MergeUtils.messageError(ex);
                    }
                }
            } else {
                editor.cancel();
            }

            final ButtonEditor be = (ButtonEditor) this.getColumnModel().getColumn(2).getCellEditor();
            be.stopCellEditing();
            this.repaint();

        } catch (RadixSvnException ex) {
            MergeUtils.messageError(ex);
        }
    }

}
