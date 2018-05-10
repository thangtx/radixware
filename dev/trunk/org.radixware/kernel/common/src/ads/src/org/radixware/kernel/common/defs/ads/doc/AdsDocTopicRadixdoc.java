package org.radixware.kernel.common.defs.ads.doc;

import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.AbstractDefDocItem;
import org.radixware.schemas.radixdoc.AdsTopicDefDocItem;
import org.radixware.schemas.radixdoc.Page;

/**
 * @see AdsDocRadixdoc
 * @author dkurlyanov
 */
class AdsDocTopicRadixdoc extends AdsDocRadixdoc<AdsDocTopicDef> {

    public AdsDocTopicRadixdoc(AdsDocTopicDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    public AbstractDefDocItem buildDocItem() {
        AdsTopicDefDocItem xAdsTopicDefDocItem = AdsTopicDefDocItem.Factory.newInstance();
        return super.buildDocItem(xAdsTopicDefDocItem);
    }
}
