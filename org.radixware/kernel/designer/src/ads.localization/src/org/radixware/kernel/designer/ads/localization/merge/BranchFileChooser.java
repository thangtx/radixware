package org.radixware.kernel.designer.ads.localization.merge;

import java.io.File;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.radixware.kernel.common.defs.RadixObjectIcon;

public class BranchFileChooser extends JFileChooser {

    final protected File trunk;
    final protected String devUri;

    BranchFileChooser(final File trunk, final String devUri) {
        super();
        this.trunk = trunk;
        this.devUri = devUri;
    }

    @Override
    public Icon getIcon(File f) {
        if (LocalizingStringMergeUtils.checkPath(f, trunk, devUri)) {
            return RadixObjectIcon.BRANCH.getIcon();
        }
        return super.getIcon(f);
    }

    public static BranchFileChooser createFileChooser(final File trunk, final String devUri, String currentDir){
        BranchFileChooser fileChooser = new BranchFileChooser(trunk, devUri);
        final FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Directories";
            }
        };
        fileChooser.setFileHidingEnabled(false);
        fileChooser.setDialogTitle("Choose Other Branch Directory");
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        if (currentDir != null && !currentDir.isEmpty()) {
            final File currDirFile = new File(currentDir);
            fileChooser.setCurrentDirectory(currDirFile.getParentFile());
            fileChooser.setSelectedFile(currDirFile);
        }
        return fileChooser;
    }
}
