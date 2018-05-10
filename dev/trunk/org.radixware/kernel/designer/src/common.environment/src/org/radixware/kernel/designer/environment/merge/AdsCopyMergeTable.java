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

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SvnEntryComparator;
import org.radixware.kernel.common.svn.SvnPathUtils;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.dialogs.commitpanel.MicroCommitPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import static org.radixware.kernel.designer.environment.merge.MergeUtils.getTableAsStr;

public class AdsCopyMergeTable extends CopyMergeTable {

//    private Layer layer;
    @Override
    protected boolean cellEditable(final int rowIndex, final int columnIndex) {

        if (columnIndex == 2) {
            return list.get(rowIndex).isMayCopy();
        } else if (columnIndex == 3) {
            return list.get(rowIndex).isMayMerge();
        }

        return columnIndex != 4;
    }

    public AdsCopyMergeTable() {
        super();
    }
    AdsMergeChangesOptions options;
    List<AdsMergeChangesItemWrapper> list;

    public void open(AdsMergeChangesOptions options) {
        this.options = options;
        list = options.getList();
        DefaultTableModel model = (DefaultTableModel) this.getModel();

        model.setRowCount(list.size());
//        layer = options.getLayer();
        {
            for (int i = 0; i < list.size(); i++) {
                Definition def_ = list.get(i).getDef();

                model.setValueAt(def_.getQualifiedName(), i, 0);

                model.setValueAt(list.get(i).getFileTitle(), i, 1);// file.getName()
                model.setValueAt(list.get(i).isDone(), i, 4);
            }
        }

        setColumnsWidth();

    }

    @Override
    protected void copyAll() throws Exception {
        try {
            List<Long> revisionNumbers = new ArrayList();
            SVNRepositoryAdapter repository = options.getRepository();

            long rev;
            try {
                rev = repository.getLatestRevision();
            } catch (RadixSvnException ex) {
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
                    sb.append(list.get(index).getDef().getQualifiedName()).append("(").append(list.get(index).getFileTitle()).append(")");
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
                
                final File file = list.get(index).getDef().getFile();
                if (file == null) {//RADIX-12711
                    DialogUtils.messageError("Invalid definitions state. Please close merge dialog, and refresh definitions tree.");
                    return;
                }

                long fromRev = SVN.getFileRevision(options.getFsClient(), file);

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

            if (arr.length > 1) {
                comment += "s";
            }
            comment += commentPostfix;
            comment = MergeCommitDialog.showCommitDialog(MicroCommitPanel.getLastCommitMessage(), comment);

            if (comment == null) {
                return;
            }

            final SVNRepositoryAdapter.Editor editor = repository.createEditor(comment);
            int j = 0;

            List<String> added = new ArrayList();

            for (int index = 0; index < getRowCount(); index++) {

                if (list.get(index).isNotExists()) {
                    checkParentDirs(editor, index, added, destFiles);
                }

                if (!list.get(index).isMayCopy()) {
                    continue;
                }

                int cnt;
                String toPath = list.get(index).getToPath();

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

                list.get(index).setSrcChangesLevel(SvnEntryComparator.NOT_MAY_COPY);
                list.get(index).setMayMerge(false);
                list.get(index).setEquals(true);
                list.get(index).setDone(true);
                this.setValueAt(true, index, 4);
            }
            editor.commit();

            for (File f : destFiles) {
                try {
                    SVN.update(options.getFsClient(), f);
                } catch (Exception ex) {
                    MergeUtils.messageError(ex);
                }
            }

            final ButtonEditor be = (ButtonEditor) this.getColumnModel().getColumn(2).getCellEditor();
            be.stopCellEditing();
            this.repaint();

        } catch (RadixSvnException ex) {
            MergeUtils.messageError(ex);
        }
    }

    @Override
    protected boolean merge(int index) {

        final MergeDialog dialog = new MergeDialog(new MergePanel(this, options, index));
        dialog.showModal();

        return true;
    }

    void checkParentDirs(SVNRepositoryAdapter.Editor editor, int index, List<String> added, List<File> updateFiles) throws RadixSvnException, IOException, Exception {
        File toFile = list.get(index).getDestFile();
        if (list.get(index).isMlb()) {
            File toLngFile = toFile.getParentFile();
            String toFileEmulator = list.get(index).getToPath();
            String toLngFileEmulator = SvnPathUtils.getParentDir(toFileEmulator);

            if (!toLngFile.exists() && !added.contains(toLngFileEmulator)) {

                String toLocaleFileEmulator = SvnPathUtils.getParentDir(toLngFileEmulator);

                File toLocaleFile = toLngFile.getParentFile();
                if (!toLocaleFile.exists() && !added.contains(toLocaleFileEmulator)) {

                    String toModuleFileEmulator = SvnPathUtils.getParentDir(toLocaleFileEmulator);
                    File toModuleFile = toLocaleFile.getParentFile();
                    if (!toModuleFile.exists() && !added.contains(toModuleFileEmulator)) {//create module

                        int cnt = editor.openDirs(SvnPathUtils.getParentDir(toModuleFileEmulator));
                        editor.appendDir(toModuleFileEmulator);
                        editor.closeDirs(cnt);
                        updateFiles.add(list.get(index).getDestFile().getParentFile().getParentFile().getParentFile());

                        cnt = editor.openDirs(toModuleFileEmulator);

                        String fromModuleFile = list.get(index).getSrcFile().getParentFile().getParentFile().getParentFile().getAbsolutePath() + "/module.xml";
                        try (FileInputStream is = new FileInputStream(fromModuleFile)) {
                            editor.appendFile(toModuleFileEmulator + "/module.xml", is);
                        }
                        editor.closeDirs(cnt);

                        added.add(toModuleFileEmulator);

                    }
                    int cnt = editor.openDirs(SvnPathUtils.getParentDir(toLocaleFileEmulator));
                    editor.appendDir(toLocaleFileEmulator);
                    editor.closeDirs(cnt);
                    updateFiles.add(list.get(index).getDestFile().getParentFile().getParentFile());
                    added.add(toLocaleFileEmulator);
                }
                int cnt = editor.openDirs(SvnPathUtils.getParentDir(toLngFileEmulator));
                editor.appendDir(toLngFileEmulator);
                editor.closeDirs(cnt);
                updateFiles.add(list.get(index).getDestFile().getParentFile());
                added.add(toLngFileEmulator);

            }

        } else {
            File toSrcFile = toFile.getParentFile();
            String toFileEmulator = list.get(index).getToPath();
            String toSrcFileEmulator = SvnPathUtils.getParentDir(toFileEmulator);
            if (!toSrcFile.exists() && !added.contains(toSrcFileEmulator)) {//create src or locale

                File toModuleFile = toSrcFile.getParentFile();
                String toModuleFileEmulator = SvnPathUtils.getParentDir(toSrcFileEmulator);

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
                int cnt = editor.openDirs(SvnPathUtils.getParentDir(toSrcFileEmulator));
                editor.appendDir(toSrcFileEmulator);
                editor.closeDirs(cnt);
                updateFiles.add(list.get(index).getDestFile().getParentFile());
                added.add(toSrcFileEmulator);
            }
        }
    }

    @Override
    protected boolean copy(int index) throws Exception {
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
        comment = MergeCommitDialog.showCommitDialog("Copy " + list.get(index).getDef().getQualifiedName(), comment);

        if (comment == null) {
            return false;
        }

        boolean toFileExists = SVN.isFileExists(options.getRepository(), toPath);

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

        editor.commit();

        try {
            for (File updateFile : updateFiles) {
                SVN.update(options.getFsClient(), updateFile);
            }
        } catch (Exception ex) {
            MergeUtils.messageError(ex);
        }

        list.get(index).setSrcChangesLevel(SvnEntryComparator.NOT_MAY_COPY);
        list.get(index).setMayMerge(false);
        list.get(index).setEquals(true);
        list.get(index).setDone(true);
        this.setValueAt(true, index, 4);
        ButtonEditor be = (ButtonEditor) this.getColumnModel().getColumn(2).getCellEditor();
        be.stopCellEditing();
        this.repaint();
        return true;
    }

    @Override
    protected Icon getIconByRow(final int row) {
        return list.get(row).getDef().getIcon().getIcon();
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
    protected boolean isDefaultBackgroundColor(final int row) {
        boolean flag = true;
        Definition lastDef = null;
        for (int i = 0; i <= row; i++) {
            Definition def = list.get(i).getDef();
            if (lastDef != def) {
                flag = !flag;
                lastDef = def;
            }
        }
        return flag;
    }

    @Override
    protected Color getCurrForegroundColor(final int row) {
        if (list.get(row).isFromNotExistsAndToExists()) {
            return (new Color(155, 0, 0));
        } else if (list.get(row).isNotExists()) {
            return (new Color(0, 155, 0));
        } else if (!list.get(row).isEquals()) {
            return (Color.BLUE);
        } else {
            return (UIManager.getColor("Button.foreground"));
        }
    }

//    public void addDefinitions() {
//        ChooseDefinitionConfig init = new ChooseDefinitionConfig(layer, new VisitorProviderEx(null));
//        List<Definition> defList2 = ChooseDefinitionSequence.chooseDefinitionSequence(new ChooseDefinitionConfigs(init, layer));
//        List<Definition> defList = new ArrayList<>(defList2);
//        if (defList.size() != 2) {
//            return;
//        }
//        List<MergeChangesItemWrapper> items;
//        try {
//            items = MergeChangesItemWrapper.getSvnFileOptions(options, (AdsDefinition) defList.get(1));
//        } catch (Exception ex) {
//            DialogUtils.messageError(ex);
//            return;
//        }
//
//        final String sQDefName = defList.get(1).getQualifiedName();
//
//        int r = -1;
//        for (MergeChangesItemWrapper item : items) {
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).getDef().getQualifiedName().compareTo(sQDefName) > 0) {
//                    r = i;
//                    break;
//                }
//            }
//
//            final DefaultTableModel model = (DefaultTableModel) this.getModel();
//            final Object arr[] = {sQDefName, item.getFileTitle(), item.isMayCopy(), item.isMayMerge(), item.isDone()};
//            if (r == -1) {
//                list.add(item);
//                model.addRow(arr);
//                r = this.getRowCount() - 1;
//            } else {
//                list.add(r, item);
//                model.insertRow(r, arr);
//            }
//        }
//        this.setRowSelectionInterval(r, r);
//    }
//
//    private boolean containtsInList(Definition def) {
//        for (MergeChangesItemWrapper item : list) {
//            if (item.getDef() == def) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private class ChooseDefinitionConfigs extends ChooseDefinitionCfgs {
//
//        RadixObject context;
//
//        ChooseDefinitionConfigs(ChooseDefinitionConfig init, RadixObject context) {
//            super(init);
//            this.context = context;
//        }
//
//        @Override
//        protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
//            if (choosenDef instanceof AdsModule) {
//                return new ChooseDefinitionConfig(choosenDef, new VisitorProviderEx((AdsModule) choosenDef));
//            }
//            return null;
//        }
//
//        @Override
//        protected boolean hasNextConfig(Definition choosenDef) {
//            return choosenDef instanceof AdsModule;
//        }
//
//        @Override
//        protected boolean isFinalTarget(Definition choosenDef) {
//            if (!(choosenDef instanceof AdsDefinition)) {
//                return false;
//            }
//            if (choosenDef instanceof AdsLocalizingBundleDef) {
//                return false;
//            }
//            if (containtsInList(choosenDef)) {
//                return false;
//            }
//            try {
//                if (MergeChangesItemWrapper.getSvnFileOptions(options, (AdsDefinition) choosenDef) == null) {
//                    return false;
//                }
//            } catch (Exception ex) {
//                DialogUtils.messageError(ex);
//                return false;
//            }
//            return true;
//        }
//
//        @Override
//        public String getDisplayName() {
//            return "Choose ADS Definition";
//        }
//    }
//    private class VisitorProviderEx extends VisitorProvider {
//
//        HashMap<RadixObject, RadixObject> list = new HashMap<RadixObject, RadixObject>();
//        AdsModule currAdsModule;
//
//        VisitorProviderEx(AdsModule showProps) {
//            this.currAdsModule = showProps;
//        }
//
//        @Override
//        public boolean isTarget(RadixObject object) {
//            if (currAdsModule == null) {
//                return (object instanceof AdsModule);
//            }
//            if (!(object instanceof AdsDefinition)) {
//                return false;
//            }
//            if (object instanceof AdsLocalizingBundleDef) {
//                return false;
//            }
//
//            AdsDefinition ads = (AdsDefinition) object;
//            if (!ads.isTopLevelDefinition()) {
//                return false;
//            }
//            if (ads.getModule() != currAdsModule) {
//                return false;
//            }
//
//            if (containtsInList(ads)) {
//                return false;
//            }
//            try {
//                if (MergeChangesItemWrapper.getSvnFileOptions(options, ads) == null) {
//                    return false;
//                }
//            } catch (Exception ex) {
//                DialogUtils.messageError(ex);
//                return false;
//            }
//            return true;
//        }
//    }
//
//    private class ChooseDefinitionConfig extends ChooseDefinitionCfg {
//
//        protected ChooseDefinitionConfig(
//                final RadixObject context,
//                final VisitorProvider provider) {
//            super(context, provider);
//        }
//    }
}
