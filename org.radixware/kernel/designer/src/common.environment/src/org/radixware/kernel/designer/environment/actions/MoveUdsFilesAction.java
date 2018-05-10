package org.radixware.kernel.designer.environment.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.translate.SqmlTranslator;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

public class MoveUdsFilesAction implements ActionListener{
    private void performAction(Collection<Branch> branches, final ProgressHandle progressHandle) {

        for (Branch branch : branches) {
            final List<File> srcDirs = new ArrayList<>();
            branch.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    if (!radixObject.isReadOnly() && radixObject instanceof UdsModule) {
                        File f = ((UdsModule) radixObject).getDefinitions().getSourceDir(AdsDefinition.ESaveMode.NORMAL);
                        File udsFilesDir = ((UdsModule) radixObject).getUdsFiles().getFile();
                        if (f.exists() && f.isDirectory()) {
                            try {
                                List<File> files = FileUtils.getFilesList(f);
                                progressHandle.progress("Copy file from " + f.getAbsolutePath() + " to " + udsFilesDir.getAbsolutePath());
                                progressHandle.switchToDeterminate(files.size());
                                int progress = 0;
                                for (File file : files) {
                                    FileUtils.moveFile(file, new File(udsFilesDir, file.getName()));
                                    progressHandle.progress(++progress);
                                }
                                progressHandle.switchToIndeterminate();
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                            srcDirs.add(f);
                        }
                    }
                }
            }, VisitorProviderFactory.createModuleVisitorProvider());
            if (!srcDirs.isEmpty()) {
                if (DialogUtils.messageConfirmation("Delete src directories?")) {
                    for (File file : srcDirs) {
                        try {
                            FileUtils.deleteFileExt(file);
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        final Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
        if (branches.isEmpty()) {
            DialogUtils.messageError("There are no opened branches.");
            return;
        }
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                RadixMutex.writeAccess(new Runnable() {

                    @Override
                    public void run() {
                        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Move UDS Files...");
                        progressHandle.start();
                        try {
                            performAction(branches, progressHandle);
                        } finally {
                            progressHandle.finish();
                        }
                    }
                });
            }
        });
    }
    
}
