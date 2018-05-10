package org.radixware.kernel.common.defs.ads.doc;

import java.io.File;
import org.radixware.kernel.common.defs.IPropertyModule;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ENamingPolicy;
import static org.radixware.kernel.common.enums.ENamingPolicy.FREE;

/**
 * Class property {@link AdsModule}, containing ref for file, which use in TechDoc. Use
 * in {@link MmlTagId}.
 *
 * @see MmlTagId
 * @see DocResources
 * @see AdsModule 
 * @author dkurlyanov
 */
public class DocResource extends RadixObject implements IPropertyModule {

    private File file;

    public DocResource(File file) {
        super(file.getName());
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return FREE;
    }
}
