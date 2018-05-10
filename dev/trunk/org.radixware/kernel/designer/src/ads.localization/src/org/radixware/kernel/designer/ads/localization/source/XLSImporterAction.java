package org.radixware.kernel.designer.ads.localization.source;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.DialogDescriptor;
import org.openide.util.Exceptions;
import org.openide.windows.IOColorLines;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.radixware.kernel.common.builder.impexp.ImportConflict;
import org.radixware.kernel.common.builder.impexp.ImportConflicts;
import org.radixware.kernel.common.builder.impexp.Mls2XMLUtils;
import org.radixware.kernel.common.builder.impexp.Xls2MlsImporter;
import org.radixware.kernel.common.builder.impexp.Mls2XmlImportInfo;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

public class XLSImporterAction extends AbstractAction {

    private final String outputKey = "Import Strings";

    private final MlsTablePanel component;
    private volatile InputOutput inputOutput;

    public XLSImporterAction(MlsTablePanel component) {
        super("Import Strings", RadixWareIcons.MLSTRING_EDITOR.IMPORT_STR.getIcon(20));
        this.component = component;
        this.inputOutput = IOProvider.getDefault().getIO(outputKey, false);
        try {
            inputOutput.getOut().reset();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Branch branch = RadixFileUtil.getOpenedBranches().iterator().next();
        final Xls2MlsOptionsPanel panel = new Xls2MlsOptionsPanel();
        StateAbstractDialog dialog = new StateAbstractDialog(panel, "Import Options") {};

        if (dialog.showModal()) {
            ProgressUtils.showProgressDialogAndRun(new Runnable() {

                @Override
                public void run() {
                    inputOutput.select();
                    try {
                        printLn(inputOutput, "Operation 'Import localizing strings' stated.", Color.BLUE.darker());
                        Xls2MlsImporter mls2XlsImporter = new Xls2MlsImporter(branch);
                        try {

                            mls2XlsImporter.doImport(panel.getInFile(), panel.getSetCkecked(), panel.getSetAgreed());

                            Mls2XmlImportInfo info = mls2XlsImporter.getImportInfo();
                            for (String problem : info.getProblems()) {
                                printLn(inputOutput, problem, Color.RED.darker());
                            }
                            List<ImportConflicts> conflictList = info.getConflicts();
                            if (conflictList != null && !conflictList.isEmpty()) {
                                final ImportConflictsPanel conflicts = new ImportConflictsPanel();
                                conflicts.open(conflictList);
                                StateAbstractDialog options = new StateAbstractDialog(conflicts, "Conflicts") {
                                };
                                if (options.showModal()) {
                                    for (ImportConflicts importConflicts : conflictList) {
                                        for (ImportConflict conflict : importConflicts) {
                                            if (conflict.getAcceptImportValue()) {
                                                Mls2XMLUtils.aceptValue(importConflicts.getString(), conflict.getLanguage(), conflict.getImportValue(), importConflicts.getImportInfo());
                                            } else {
                                                Mls2XMLUtils.aceptValue(importConflicts.getString(), conflict.getLanguage(), importConflicts.getString().getValue(conflict.getLanguage()), importConflicts.getImportInfo());
                                            }
                                        }
                                    }
                                } else {
                                    inputOutput.getOut().println("Conflicted strings are not imported");
                                }
                            }

                        } catch (IOException ex) {
                            DialogUtils.messageError(ex);
                        }
                        printLn(inputOutput, "Operation 'Import localizing strings' finished.", Color.BLUE.darker());
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }, "Import Strings...");

            component.setCurrentRowString();
        }

    }
    
    void printLn(InputOutput io, String mess, Color color) throws IOException {
        IOColorLines.println(io, mess, color);
    }
}
