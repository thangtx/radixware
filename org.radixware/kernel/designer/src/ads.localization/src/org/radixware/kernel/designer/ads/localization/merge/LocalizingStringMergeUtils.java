package org.radixware.kernel.designer.ads.localization.merge;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.version.Version;
import org.radixware.schemas.product.BranchDocument;

public class LocalizingStringMergeUtils {
     
    public static boolean checkPath(File f, File trunk, final String devUri){
        return f != null && f.isDirectory() && f.exists() && isBranchWithDevUri(f, devUri) && !f.equals(trunk);
    }
    
    private static boolean isBranchWithDevUri(final File dir, final String devUri) {
        if (!Branch.isBranchDir(dir)) {
            return false;
        }
        try {
            final Branch branch = Branch.Factory.loadFromDir(dir);
            if (branch != null
                    && branch.getBaseDevelopmentLayerUri() != null
                    && branch.getBaseDevelopmentLayerUri().equals(devUri)) {
                return true;
            }
        } catch (IOException ex) {
            Logger.getLogger(LocalizingStringMergeUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return false;
    }
    
     public static boolean radixVersionNotLessThen_2_1_7(final File branchFile) throws XmlException, IOException {

        String lastReleaseNumber = null;
        final File branchXmlFile = new File(branchFile.getAbsolutePath() + File.separator + org.radixware.kernel.common.constants.FileNames.BRANCH_XML);
        if (branchXmlFile.exists()) {
            final BranchDocument branchDocument = BranchDocument.Factory.parse(branchXmlFile);
            final org.radixware.schemas.product.Branch xBranch = branchDocument.getBranch();
            lastReleaseNumber = xBranch.getLastRelease();
        }

        final File radixLayerFile = new File(branchFile.getAbsolutePath() + File.separator + FileNames.RADIX_LAYER_URI);

        if (!radixLayerFile.exists() || !Layer.isLayerDir(radixLayerFile)) {
            return false;
        }

        final File radixLayerXmlFile = new File(radixLayerFile.getAbsolutePath() + File.separator + FileNames.LAYER_XML);
        if (!radixLayerXmlFile.exists()) {
            return false;
        }

        final org.radixware.schemas.product.Layer xLayer = org.radixware.schemas.product.LayerDocument.Factory.parse(radixLayerXmlFile).getLayer();
        String layerReleaseNumber = xLayer.getReleaseNumber();
        if (layerReleaseNumber == null || layerReleaseNumber.isEmpty()) {
            layerReleaseNumber = lastReleaseNumber;
        }
        if (layerReleaseNumber == null || layerReleaseNumber.isEmpty()) {
            return false;
        }
        final Version version = new Version(layerReleaseNumber);
        return new Version("2.1.7").compareTo(version) >= 0;

    }
}
