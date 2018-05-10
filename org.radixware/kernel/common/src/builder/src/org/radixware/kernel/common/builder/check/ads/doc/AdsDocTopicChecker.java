
package org.radixware.kernel.common.builder.check.ads.doc;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.doc.AdsDocTopicDef;

@RadixObjectCheckerRegistration
public class AdsDocTopicChecker extends AdsDefinitionChecker<AdsDocTopicDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsDocTopicDef.class;
    }

    @Override
    public void check(AdsDocTopicDef def, IProblemHandler problemHandler) {
        //super.check(def, problemHandler);
        //CheckUtils.checkMLStringId(def, def.getTitleId(), problemHandler, "title");
    }
}
