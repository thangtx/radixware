package org.radixware.kernel.common.defs.ads.module;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.doc.DocResources;
import org.radixware.kernel.common.enums.EIsoLanguage;

public class ModuleDocumentation extends RadixObject {

    Map<EIsoLanguage, DocResources> resources = new HashMap<>();

    public ModuleDocumentation(AdsModule module) {
        super();
        setContainer(module);
    }

    public DocResources getResources(EIsoLanguage lang) {
        if (!resources.keySet().contains(lang)) {
            DocResources res = new DocResources(this, lang);
            resources.put(lang, res);
        }
        return resources.get(lang);
    }

}
