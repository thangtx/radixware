package org.radixware.kernel.common.defs.ads.doc;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;

/**
 * RadixObjects for {@link DocReference}.
 *
 * @see DocReference
 * @see AdsDocMapDef
 * @author dkurlyanov
 */
public class DocReferences extends RadixObjects<DocReference> {

    public DocReferences(RadixObject container) {
        super(container);
    }

    public boolean contains(AdsDocDef def) {
        for (DocReference ref : this) {
            if (ref.getDocDef() == def) {
                return true;
            }
        }
        return false;
    }

}
