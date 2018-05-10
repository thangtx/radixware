
package org.radixware.kernel.common.builder.check.ads.doc;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.doc.AdsDocMapDef;

// TODO: !!! need remark
@RadixObjectCheckerRegistration
public class AdsDocMapChecker extends AdsDefinitionChecker<AdsDocMapDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsDocMapDef.class;
    }

    @Override
    public void check(AdsDocMapDef def, IProblemHandler problemHandler) {

        //super.check(def, problemHandler);
        //CheckUtils.checkMLStringId(def, def.getTitleId(), problemHandler, "title");
    }

}
