package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import java.text.MessageFormat;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsProperiesGroupDef;

@RadixObjectCheckerRegistration
public class AdsProperiesGroupChecker  extends AdsDefinitionChecker<AdsProperiesGroupDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsProperiesGroupDef.class;
    }

    @Override
    public void check(AdsProperiesGroupDef definition, IProblemHandler problemHandler) {
        super.check(definition, problemHandler); 
    }
    
    
}
