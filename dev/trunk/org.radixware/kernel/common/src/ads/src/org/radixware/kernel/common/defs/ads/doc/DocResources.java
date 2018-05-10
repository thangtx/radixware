package org.radixware.kernel.common.defs.ads.doc;

import java.io.File;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDocumentation;
import org.radixware.kernel.common.enums.EIsoLanguage;

/**
 * RadixObjects for {@link DocResource}.
 *
 * @see DocResource
 * @see AdsModule
 * @author dkurlyanov
 */
public class DocResources extends RadixObjects<DocResource> {

    private final static String DIR_NAME = "resource";

    private File dir;

    public DocResources(AdsModule module, EIsoLanguage lang) {
        this(module.getDocumentation(), lang);
    }

    public DocResources(ModuleDocumentation moduleDocumentation, EIsoLanguage lang) {
        setContainer(moduleDocumentation);

        Module module = moduleDocumentation.getModule();
        dir = new File(module.getFile().getParentFile(), "src" + File.separator + "doc" + File.separator + lang.getValue() + File.separator + DIR_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File[] fileList = dir.listFiles();
        for (File file : fileList) {
            DocResource res = new DocResource(file);
            this.add(res);
        }
    }

    public File getDir() {
        return dir;
    }

    public DocResource get(File file) {
        for (DocResource res : this) {
            if (res.getFile().equals(file)) {
                return res;
            }
        }
        return null;
    }

}
