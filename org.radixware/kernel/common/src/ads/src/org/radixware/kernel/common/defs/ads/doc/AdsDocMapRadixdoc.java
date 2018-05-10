package org.radixware.kernel.common.defs.ads.doc;

import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.AbstractDefDocItem;
import org.radixware.schemas.radixdoc.AdsMapDefDocItem;
import org.radixware.schemas.radixdoc.Page;

/**
 * @see AdsDocRadixdoc
 * @author dkurlyanov
 */
class AdsDocMapRadixdoc extends AdsDocRadixdoc {

    public AdsDocMapRadixdoc(AdsDocMapDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    public AbstractDefDocItem buildDocItem() {
        AdsMapDefDocItem xAdsMapDefDocItem = AdsMapDefDocItem.Factory.newInstance();
        AdsDocMapDef item = (AdsDocMapDef) source;

        DocReferences references = item.getReferences();
        for (DocReference ref : references) {
            Id id = ref.getDocDef().getId();
            xAdsMapDefDocItem.getItemsList().add(id);
        }

        return super.buildDocItem(xAdsMapDefDocItem);
    }

}
