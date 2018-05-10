package org.radixware.kernel.common.builder.check.ads.doc;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.doc.DocReference;

@RadixObjectCheckerRegistration
public class DocRefChecker extends RadixObjectChecker<DocReference> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DocReference.class;
    }

    @Override
    public void check(DocReference ref, IProblemHandler problemHandler) {
        super.check(ref, problemHandler);

//        Definition refDef = ref.getPath().resolve(ref.getDefinition()).get();
//        if (refDef == null) {
//            switch (ref.getType()) {
//                case STRONG:
//                    error(ref, problemHandler, "Can not find strong referenced definition " + ref.getQualifiedName());
//                    break;
//                case WEAK:
//                    warning(ref, problemHandler, "Can not find weak referenced definition " + ref.getQualifiedName());
//                    break;
//            }
//        }

    }

}
